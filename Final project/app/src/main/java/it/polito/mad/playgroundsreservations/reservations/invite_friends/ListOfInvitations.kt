package it.polito.mad.playgroundsreservations.reservations.invite_friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.database.Invitation
import it.polito.mad.playgroundsreservations.database.InvitationStatus
import it.polito.mad.playgroundsreservations.reservations.ui.theme.AcceptedColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PendingColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PlaygroundsReservationsTheme
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.RefusedColor

class ListOfInvitations(val invitations: SnapshotStateList<Invitation>): Fragment() {
    private val args by navArgs<InviteFriendsArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaygroundsReservationsTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            invitations.forEach {
                                InvitationBox(invitation = it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationBox(invitation: Invitation) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                BorderStroke(2.dp, PrimaryColor),
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row {
                    Column {
                        ProfileImage(friendId = invitation.userId, small = true)
                    }

                    Column {
                        Text(
                            text = invitation.fullName,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
            }

            Column {
                val badgeColor = when (invitation.invitationStatus) {
                    InvitationStatus.ACCEPTED -> AcceptedColor
                    InvitationStatus.REFUSED -> RefusedColor
                    InvitationStatus.PENDING -> PendingColor
                }

                Badge(
                    containerColor = badgeColor,
                    contentColor = Color.White
                ) {
                    Text(
                        text = invitation.invitationStatus.name,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}