package it.polito.mad.playgroundsreservations.reservations.favorite_playgrounds

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Sport
import it.polito.mad.playgroundsreservations.reservations.ViewModel
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryVariantColor

@Composable
fun FavoritePlaygroundBox(playground: Playground, favoritePlaygrounds: SnapshotStateList<Playground>) {
    val viewModel: ViewModel = viewModel()

    var showAdditionalInfo by remember { mutableStateOf(false) }
    var isFavoritePlayground by remember { mutableStateOf(favoritePlaygrounds.contains(playground)) }

    LaunchedEffect(favoritePlaygrounds.size) {
        isFavoritePlayground = favoritePlaygrounds.contains(playground)
    }

    val sportIcon = when (playground.sport) {
        Sport.TENNIS -> R.drawable.tennis_ball
        Sport.BASKETBALL -> R.drawable.basketball_ball
        Sport.FOOTBALL -> R.drawable.football_ball
        Sport.VOLLEYBALL -> R.drawable.volleyball_ball
        Sport.GOLF -> R.drawable.golf_ball
    }

    val sportName = when (playground.sport) {
        Sport.TENNIS -> R.string.sport_tennis
        Sport.BASKETBALL -> R.string.sport_basketball
        Sport.FOOTBALL -> R.string.sport_football
        Sport.VOLLEYBALL -> R.string.sport_volleyball
        Sport.GOLF -> R.string.sport_golf
    }

    val playgroundImage = when (playground.sport) {
        Sport.TENNIS -> R.drawable.tennis_court
        Sport.BASKETBALL -> R.drawable.basketball_court
        Sport.FOOTBALL -> R.drawable.football_pitch
        Sport.VOLLEYBALL -> R.drawable.volleyball_court
        Sport.GOLF -> R.drawable.golf_field
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
                        Image(
                            painter = painterResource(id = playgroundImage),
                            contentDescription = "Playground image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(75.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(IntrinsicSize.Max),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row {
                            Text(
                                text = playground.name,
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
                                Image(
                                    painter = painterResource(id = sportIcon),
                                    contentDescription = "Sport icon"
                                )
                            }

                            Column {
                                Text(text = stringResource(id = sportName))
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

                    OutlinedButton(
                        onClick = {
                            if (isFavoritePlayground) {
                                viewModel.removeFavoritePlayground(playground.id)
                                favoritePlaygrounds.remove(playground)
                            } else {
                                viewModel.addFavoritePlayground(playground.id)
                                favoritePlaygrounds.add(playground)
                            }
                        }
                    ) {
                        val starIcon = if (isFavoritePlayground) R.drawable.filled_star else R.drawable.bordered_star

                        Image(
                            painter = painterResource(id = starIcon),
                            contentDescription = "Add playground to favorites",
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
                        text = if (showAdditionalInfo)
                            stringResource(id = R.string.see_less)
                        else stringResource(id = R.string.see_more),
                        color = PrimaryVariantColor,
                        textAlign = TextAlign.Center
                    )
                }
            }

            AnimatedVisibility(visible = showAdditionalInfo) {
                Column {
                    Row {
                        Text(
                            text = "Address",
                            color = PrimaryColor,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }

                    Row {
                        Text(
                            text = playground.address,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }

                    Row {
                        Text(
                            text = "Price per hour",
                            color = PrimaryColor,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .padding(horizontal = 5.dp)
                        )
                    }

                    Row {
                        Text(
                            text = "${playground.pricePerHour} €/h",
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }

                    Row {
                        Text(
                            text = "Maximum number of people",
                            color = PrimaryColor,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .padding(horizontal = 5.dp)
                        )
                    }

                    Row {
                        Text(
                            text = "${playground.maxPlayers} people",
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    }
}