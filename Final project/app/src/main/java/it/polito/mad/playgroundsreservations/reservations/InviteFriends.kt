package it.polito.mad.playgroundsreservations.reservations

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.smarttoolfactory.ratingbar.RatingBar
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.User
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PlaygroundsReservationsTheme
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PrimaryVariantColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.SecondaryColor
import it.polito.mad.playgroundsreservations.reservations.ui.theme.SecondaryVariantColor

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

    val users: MutableState<List<User>> = remember { mutableStateOf(emptyList()) }
    viewModel.getUsers(users)

    if (users.value.isEmpty())
        MyLoadingRatingPlaygrounds()
    else
        InviteFriendsScreenContent(users, reservationId, navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteFriendsScreenContent(
    users: MutableState<List<User>>,
    reservationId: String,
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("v") }

    Column(Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            onValueChange = { searchQuery = it },
            maxLines = 1,
            label = { Text("Search") }
        )

        if (searchQuery == "") {
            Text(text = "Start typing")
        } else {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(users.value.filter { friend ->
                    friend.fullName.contains(searchQuery, ignoreCase = true)
                }) { friend ->
                    Friend(friend)
                }
            }
        }
    }
}

@Composable
fun Friend(friend: User) {
    val storageReference = Firebase.storage.reference.child("profileImages/${friend.id}")
    var imageUrl: Uri? by remember { mutableStateOf(null) }
    var showDefaultImage by remember { mutableStateOf(false) }
    var showAdditionalInfo by remember { mutableStateOf(false) }

    storageReference.downloadUrl.addOnSuccessListener {
        imageUrl = it
    }.addOnFailureListener {
        showDefaultImage = true
        Log.d("IMAGE URL", it.message ?: "")
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
            Row(modifier = Modifier
                .height(IntrinsicSize.Max)
            ) {
                Column(modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .weight(1f)) {
                    if (imageUrl != null) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = stringResource(R.string.age),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(CircleShape)
                        ) {
                            val state = painter.state
                            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                                CircularProgressIndicator()
                            } else {
                                SubcomposeAsyncImageContent()
                            }
                        }
                    } else if (showDefaultImage) {
                        Image(
                            painter = painterResource(id = R.drawable.user_profile),
                            contentDescription = stringResource(id = R.string.age),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(CircleShape)
                        )
                    } else {
                        CircularProgressIndicator()
                    }
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
                                        painter = painterResource(id = R.drawable.tennis_ball),
                                        contentDescription = ""
                                    )
                                }

                                Column {
                                    Text(text = "Tennis")
                                }
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

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.add_friend),
                            contentDescription = "Add person to friends"
                        )
                    }

                    OutlinedButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bordered_star),
                            contentDescription = "Add person to friends"
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