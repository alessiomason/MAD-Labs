package it.polito.mad.playgroundsreservations.reservations.favorite_playgrounds

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Sport
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryVariantColor

@Composable
fun FavoritePlaygroundsList(
    canChoosePlayground: Boolean,
    choosePlayground: (HashMap<String, String>) -> Unit,
    playgrounds: SnapshotStateList<Playground>
) {
    var showAllFavoritePlaygrounds by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item(key = "your_favorite_playgrounds_title") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Favorite playgrounds",
                    color = PrimaryColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                if (playgrounds.size > 2) {
                    TextButton(
                        onClick = { showAllFavoritePlaygrounds = !showAllFavoritePlaygrounds },
                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryVariantColor),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = if (showAllFavoritePlaygrounds)
                                stringResource(id = R.string.see_less)
                            else stringResource(id = R.string.see_all),
                            color = PrimaryVariantColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        items(
            items = playgrounds.take(2),
            key = { "your_favorite_playgrounds_${it.id}" }
        ) { playground ->
            FavoritePlaygroundBox(
                canChoosePlayground = canChoosePlayground,
                choosePlayground = choosePlayground,
                playground = playground,
                favoritePlaygrounds = playgrounds
            )
        }

        if (playgrounds.size > 2) {
            items(
                items = playgrounds.drop(2),
                key = { "your_favorite_playgrounds_${it.id}" }
            ) { playground ->
                AnimatedVisibility(
                    visible = showAllFavoritePlaygrounds,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    FavoritePlaygroundBox(
                        canChoosePlayground = canChoosePlayground,
                        choosePlayground = choosePlayground,
                        playground = playground,
                        favoritePlaygrounds = playgrounds
                    )
                }
            }
        }
    }
}