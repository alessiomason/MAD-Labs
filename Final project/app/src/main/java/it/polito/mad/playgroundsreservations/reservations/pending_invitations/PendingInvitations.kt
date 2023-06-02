package it.polito.mad.playgroundsreservations.reservations.pending_invitations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.reservations.LoadingScreen
import it.polito.mad.playgroundsreservations.reservations.ViewModel
import it.polito.mad.playgroundsreservations.reservations.invite_friends.InviteFriendsArgs
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PlaygroundsReservationsTheme

class PendingInvitations: Fragment() {
    private val args by navArgs<InviteFriendsArgs>()
    lateinit var reservation: MutableState<Reservation?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ACTIVITY TITLE
        activity?.title = activity?.resources?.getString(R.string.pending_invitations)

        return ComposeView(requireContext()).apply {
            setContent {
                PlaygroundsReservationsTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        PendingInvitationsScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun PendingInvitationsScreen() {
    val viewModel: ViewModel = viewModel()

    val stillLoading = remember { mutableStateOf(true) }
    val invitedToReservations = remember { mutableStateListOf<Pair<Reservation, Playground>>() }

    LaunchedEffect(true) {
        viewModel.getInvitedToReservations(invitedToReservations, stillLoading)
    }

    if (stillLoading.value && invitedToReservations.isEmpty()) {
        LoadingScreen()
    } else {
        stillLoading.value = false
        PendingInvitationsScreenContent(invitedToReservations = invitedToReservations)
    }
}

@Composable
fun PendingInvitationsScreenContent(invitedToReservations: SnapshotStateList<Pair<Reservation, Playground>>) {
    Text(text = "Work in progress...")
    LazyColumn {

    }
}