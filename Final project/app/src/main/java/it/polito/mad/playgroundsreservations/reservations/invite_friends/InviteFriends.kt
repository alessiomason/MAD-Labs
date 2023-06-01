package it.polito.mad.playgroundsreservations.reservations.invite_friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.Global
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.User
import it.polito.mad.playgroundsreservations.reservations.LoadingScreen
import it.polito.mad.playgroundsreservations.reservations.ViewModel
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PlaygroundsReservationsTheme
import it.polito.mad.playgroundsreservations.reservations.ui.theme.SecondaryColor

class InviteFriends: Fragment() {
    private val args by navArgs<InviteFriendsArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ACTIVITY TITLE
        activity?.title = activity?.resources?.getString(R.string.invite_friends)

        return ComposeView(requireContext()).apply {
            setContent {
                PlaygroundsReservationsTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        InviteFriendsScreen(args.reservationId, findNavController())
                    }
                }
            }
        }
    }
}

@Composable
fun InviteFriendsScreen(reservationId: String, navController: NavController) {
    val viewModel: ViewModel = viewModel()

    var stillLoading by remember { mutableStateOf(true) }
    val reservation = remember { mutableStateOf<Reservation?>(null) }
    val user = remember { mutableStateOf<User?>(null) }
    val friends = remember { mutableStateListOf<User>() }
    val recentlyInvited = remember { mutableStateListOf<User>() }
    val users = remember { mutableStateListOf<User>() }

    LaunchedEffect(true) {
        viewModel.getReservation(reservationId, reservation)
        viewModel.getUser(Global.userId!!, user, friends, recentlyInvited)
        viewModel.getUsers(users)
    }

    if (stillLoading && (   // prevents from going back into loading
            reservation.value == null ||
            user.value == null ||
            (user.value?.friends?.size != null && user.value!!.friends.isNotEmpty() && friends.isEmpty()) ||
            (user.value?.recentlyInvited?.size != null && user.value!!.recentlyInvited.isNotEmpty() && recentlyInvited.isEmpty()) ||
            users.isEmpty()
        )
    )
        LoadingScreen()
    else {
        stillLoading = false
        InviteFriendsScreenContent(
            reservation = reservation,
            user = user,
            friends = friends,
            recentlyInvited = recentlyInvited,
            users = users,
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteFriendsScreenContent(
    reservation: MutableState<Reservation?>,
    user: MutableState<User?>,
    friends: SnapshotStateList<User>,
    recentlyInvited: SnapshotStateList<User>,
    users: SnapshotStateList<User>,
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            onValueChange = { searchQuery = it },
            maxLines = 1,
            label = { Text(stringResource(id = R.string.search_users)) },
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor = SecondaryColor,
                focusedIndicatorColor = SecondaryColor
            ),
            trailingIcon = {
                IconButton(
                    onClick = { searchQuery = "" },
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        )

        if (reservation.value!!.invitations.isNotEmpty()) {
            InvitedBox(reservation = reservation)
        }

        if (searchQuery == "") {
            FriendsList(
                reservation = reservation,
                user = user,
                friends = friends,
                recentlyInvited = recentlyInvited,
                sport = reservation.value!!.sport
            )
        } else {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(
                    items = users.filter { friend ->
                        friend.fullName.contains(searchQuery, ignoreCase = true)
                    }, key = { it.id }) { friend ->
                    FriendBox(
                        friend = friend,
                        sport = reservation.value!!.sport,
                        reservation = reservation,
                        friends = friends
                    )
                }
            }
        }
    }
}