package it.polito.mad.playgroundsreservations.reservations.pending_invitations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sport
import it.polito.mad.playgroundsreservations.reservations.invite_friends.ProfileImage
import it.polito.mad.playgroundsreservations.reservations.ui.theme.AcceptedColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryVariantColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.RefusedColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.SecondaryTransparentColor
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PendingInvitationBox(i: Pair<Reservation, Playground>) {
    val (reservation, playground) = i
    var showAdditionalInfo by remember { mutableStateOf(false) }

    val image = when (playground.sport) {
        Sport.TENNIS -> R.drawable.tennis_court
        Sport.BASKETBALL -> R.drawable.basketball_court
        Sport.FOOTBALL -> R.drawable.football_pitch
        Sport.VOLLEYBALL -> R.drawable.volleyball_court
        Sport.GOLF -> R.drawable.golf_field
    }

    val sportIcon = when (playground.sport) {
        Sport.TENNIS -> R.drawable.tennis_ball
        Sport.BASKETBALL -> R.drawable.basketball_ball
        Sport.FOOTBALL -> R.drawable.football_ball
        Sport.VOLLEYBALL -> R.drawable.volleyball_ball
        Sport.GOLF -> R.drawable.golf_ball
    }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .border(
                BorderStroke(2.dp, PrimaryColor),
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 10.dp)
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = image),
                                contentDescription = "",
                                contentScale = ContentScale.Crop
                            )
                        }

                        Image(
                            painter = painterResource(id = sportIcon),
                            contentDescription = "",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 10.dp)
                ) {
                    Text(
                        text = reservation.time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
                        color = PrimaryVariantColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.time_duration),
                            contentDescription = "Duration icon",
                            tint = PrimaryColor,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .size(20.dp)
                        )
                        Text(
                            text = stringResource(
                                id = if (reservation.duration.toHours() == 1L)
                                    R.string.reservation_box_duration_single_hour
                                else R.string.reservation_box_duration,
                                reservation.duration.toHours()
                            )
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.court_generic_icon),
                            contentDescription = "Court generic Icon",
                            tint = PrimaryColor,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .size(20.dp)
                        )
                        Text(text = playground.name)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.user_icon),
                            contentDescription = "User icon",
                            tint = PrimaryColor,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .size(20.dp)
                        )
                        Text(text = stringResource(id = R.string.invited_by, reservation.userFullName))
                    }
                }
            }

            Row {
                TextButton(
                    onClick = {
                        // @salvo vai la onClick è tutta tua
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = PrimaryVariantColor),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileImage(friendId = reservation.userId.id, small = true)
                        Text(text = stringResource(id = R.string.show_inviter_profile, reservation.userFullName))
                    }
                }
            }

            Row {
                TextButton(
                    onClick = { showAdditionalInfo = !showAdditionalInfo },
                    colors = ButtonDefaults.textButtonColors(contentColor = PrimaryVariantColor),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = if (showAdditionalInfo) "See less" else "See more",
                        color = PrimaryVariantColor,
                        textAlign = TextAlign.Center
                    )
                }
            }

            AnimatedVisibility(visible = showAdditionalInfo) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .background(
                            color = SecondaryTransparentColor,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .padding(top = 5.dp)
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.price_per_hour,
                                playground.pricePerHour
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .padding(bottom = 5.dp)
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.playground_address,
                                playground.address
                            )
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RefusedColor),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 5.dp)
                        .padding(vertical = 10.dp)
                ) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.cross_circle_icon),
                            contentDescription = "User icon",
                            tint = RefusedColor,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .size(20.dp)
                        )
                        Text(text = "Refuse")
                    }
                }

                OutlinedButton(
                    onClick = { /*TODO*/ },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AcceptedColor),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp, end = 10.dp)
                        .padding(vertical = 10.dp)
                ) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.check_icon),
                            contentDescription = "User icon",
                            tint = AcceptedColor,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .size(20.dp)
                        )
                        Text(text = "Accept")
                    }
                }
            }
        }
    }
}