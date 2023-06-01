package it.polito.mad.playgroundsreservations.reservations.invite_friends

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.smarttoolfactory.ratingbar.RatingBar
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Invitation
import it.polito.mad.playgroundsreservations.database.InvitationStatus
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sport
import it.polito.mad.playgroundsreservations.database.User
import it.polito.mad.playgroundsreservations.reservations.ViewModel
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryVariantColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.SecondaryColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.SecondaryVariantColor

@Composable
fun FriendBox(
    friend: User,
    sport: Sport,
    reservation: MutableState<Reservation?>,
    friends: SnapshotStateList<User>
) {
    val viewModel: ViewModel = viewModel()
    var showAdditionalInfo by remember { mutableStateOf(false) }
    var isInvited by remember { mutableStateOf(
        reservation.value!!.invitations.map { it.userId }.contains(friend.id)
    ) }
    var isFriend by remember { mutableStateOf(friends.contains(friend)) }

    val sportIcon = when (sport) {
        Sport.TENNIS -> R.drawable.tennis_ball
        Sport.BASKETBALL -> R.drawable.basketball_ball
        Sport.FOOTBALL -> R.drawable.football_ball
        Sport.VOLLEYBALL -> R.drawable.volleyball_ball
        Sport.GOLF -> R.drawable.golf_ball
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .border(
            BorderStroke(2.dp, PrimaryColor),
            shape = MaterialTheme.shapes.medium
        )) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                    ) {
                        ProfileImage(friendId = friend.id, size = 75.dp)
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(IntrinsicSize.Max),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row {
                            Text(
                                text = friend.fullName,
                                color = PrimaryVariantColor,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Column {
                                        Image(
                                            painter = painterResource(id = sportIcon),
                                            contentDescription = "Sport icon"
                                        )
                                    }

                                    /* Not enough space to show
                                    Column {
                                        Text(text = stringResource(id = sportName))
                                    }
                                    */
                                }
                            }

                            Column {
                                RatingBar(
                                    rating = friend.rating,
                                    space = 2.dp,
                                    imageVectorEmpty = ImageVector.vectorResource(id = R.drawable.bordered_star),
                                    imageVectorFFilled = ImageVector.vectorResource(id = R.drawable.filled_star),
                                    tintEmpty = SecondaryVariantColor,
                                    tintFilled = SecondaryVariantColor,
                                    itemSize = 24.dp,
                                    gestureEnabled = false
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(IntrinsicSize.Max),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (isInvited) {
                                // viewModel.disinvite(friend, reservation)
                                reservation.value!!.invitations.removeIf {
                                    it.userId == friend.id
                                }
                            } else {
                                // viewModel.invite(friend, reservation)
                                reservation.value!!.invitations.add(
                                    Invitation(
                                        friend.id,
                                        friend.fullName,
                                        InvitationStatus.PENDING
                                    )
                                )
                            }

                            isInvited = !isInvited
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor)
                    ) {
                        val invitedIcon = if (isInvited) R.drawable.check_icon else R.drawable.add_person

                        Image(
                            painter = painterResource(id = invitedIcon),
                            contentDescription = "Add person to friends",
                            modifier = Modifier
                                .size(24.dp)
                                .aspectRatio(1f)
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            if (isFriend) {
                                viewModel.unfriend(friend)
                                friends.remove(friend)
                            } else {
                                viewModel.befriend(friend)
                                friends.add(friend)
                            }
                            isFriend = !isFriend
                        }
                    ) {
                        val starIcon = if (isFriend) R.drawable.filled_star else R.drawable.bordered_star

                        Image(
                            painter = painterResource(id = starIcon),
                            contentDescription = "Add person to friends",
                            modifier = Modifier
                                .size(24.dp)
                                .aspectRatio(1f)
                        )
                    }
                }
            }

            Row {
                TextButton(
                    onClick = { showAdditionalInfo = !showAdditionalInfo },
                    colors = ButtonDefaults.textButtonColors(contentColor = PrimaryVariantColor),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (showAdditionalInfo) "See less" else "See more",
                        color = PrimaryVariantColor,
                        textAlign = TextAlign.Center
                    )
                }
            }

            AnimatedVisibility(visible = showAdditionalInfo) {
                Column {
                    Row {
                        Text(
                            text = stringResource(id = R.string.bio),
                            color = PrimaryColor,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }

                    Row {
                        Text(
                            text = friend.bio,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    }
}