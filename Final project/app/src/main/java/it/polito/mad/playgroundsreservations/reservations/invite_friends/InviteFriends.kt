package it.polito.mad.playgroundsreservations.reservations.invite_friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.Global
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.User
import it.polito.mad.playgroundsreservations.reservations.MyLoadingRatingPlaygrounds
import it.polito.mad.playgroundsreservations.reservations.ViewModel
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PlaygroundsReservationsTheme

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
                        InviteFriendsScreen("", findNavController())
                    }
                }
            }
        }
    }
}

@Composable
fun InviteFriendsScreen(reservationId: String, navController: NavController) {
    val viewModel: ViewModel = viewModel()

    val user: MutableState<User?> = remember { mutableStateOf(null) }
    val friends = remember { mutableStateOf(emptyList<User>()) }
    val recentlyInvited = remember { mutableStateOf(emptyList<User>()) }
    val users = remember { mutableStateOf(emptyList<User>()) }

    viewModel.getUser(Global.userId!!, user, friends, recentlyInvited)
    viewModel.getUsers(users)

    if (user.value == null || friends.value.isEmpty() || recentlyInvited.value.isEmpty() || users.value.isEmpty())
        MyLoadingRatingPlaygrounds()
    else
        InviteFriendsScreenContent(user, friends, recentlyInvited, users, reservationId, navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteFriendsScreenContent(
    user: MutableState<User?>,
    friends: MutableState<List<User>>,
    recentlyInvited: MutableState<List<User>>,
    users: MutableState<List<User>>,
    reservationId: String,
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            onValueChange = { searchQuery = it },
            maxLines = 1,
            label = { Text(stringResource(id = R.string.search_users)) }
        )

        if (searchQuery == "") {
            FriendsList(user, friends, recentlyInvited)
        } else {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(users.value.filter { friend ->
                    friend.fullName.contains(searchQuery, ignoreCase = true)
                }) { friend ->
                    FriendBox(friend)
                }
            }
        }
    }
}