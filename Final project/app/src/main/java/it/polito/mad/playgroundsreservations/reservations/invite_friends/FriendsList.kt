package it.polito.mad.playgroundsreservations.reservations.invite_friends

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.polito.mad.playgroundsreservations.database.User
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryVariantColor

@Composable
fun FriendsList(
    user: MutableState<User?>,
    friends: MutableState<List<User>>,
    recentlyInvited: MutableState<List<User>>
) {
    var showAllRecentlyInvited by remember { mutableStateOf(false) }
    var showAllFriends by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recently invited",
                    color = PrimaryColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                TextButton(
                    onClick = { showAllRecentlyInvited = !showAllRecentlyInvited },
                    colors = ButtonDefaults.textButtonColors(contentColor = PrimaryVariantColor),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = if (showAllRecentlyInvited) "See less" else "See all",
                        color = PrimaryVariantColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        items(recentlyInvited.value.take(2)
        ) { friend ->
            FriendBox(friend)
        }

        if (recentlyInvited.value.size > 2) {
            items(
                recentlyInvited.value.drop(2)
            ) { friend ->
                AnimatedVisibility(
                    visible = showAllRecentlyInvited,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    FriendBox(friend)
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your friends",
                    color = PrimaryColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                TextButton(
                    onClick = { showAllFriends = !showAllFriends },
                    colors = ButtonDefaults.textButtonColors(contentColor = PrimaryVariantColor),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = if (showAllFriends) "See less" else "See all",
                        color = PrimaryVariantColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        items(
            friends.value.take(2)
        ) { friend ->
            FriendBox(friend)
        }

        if (friends.value.size > 2) {
            items(
                friends.value.drop(2)
            ) { friend ->
                AnimatedVisibility(
                    visible = showAllFriends,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    FriendBox(friend)
                }
            }
        }
    }
}