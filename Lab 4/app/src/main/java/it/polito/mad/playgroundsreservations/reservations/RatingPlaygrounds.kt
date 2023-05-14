package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Sports
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PlaygroundsReservationsTheme

class RatingPlaygrounds : Fragment() {
    private val args by navArgs<RatingPlaygroundsArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ACTIVITY TITLE
        activity?.title = "Rate court"//activity?.resources?.getString(R.string.reservation)

        return ComposeView(requireContext()).apply {
            setContent {
                PlaygroundsReservationsTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        RatingPlaygroundsScreen(args.playgroundId)
                    }
                }
            }
        }
    }
}

@Composable
fun RatingPlaygroundsScreen(playgroundId: Int) {
    val reservationsViewModel: ReservationsViewModel = viewModel()
    val playground: MutableState<Playground?> = remember { mutableStateOf(null) }
    reservationsViewModel.getPlayground(playgroundId, playground)

    if (playground.value == null)
        Text("Loading")
    else
        RatingPlaygroundsScreenContent(playground = playground.value!!)
}

@Composable
fun RatingPlaygroundsScreenContent(playground: Playground) {
    var rating by remember { mutableStateOf(0) }
    val image = when (playground.sport) {
        Sports.TENNIS -> R.drawable.tennis_court
        Sports.BASKETBALL -> R.drawable.basketball_court
        Sports.FOOTBALL -> R.drawable.football_pitch
        Sports.VOLLEYBALL -> R.drawable.volleyball_court
        Sports.GOLF -> R.drawable.golf_field
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = playground.name)
        Text(text = playground.address)
        Text(text = playground.sport.toString())
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        RatingBar(rating = rating, onRatingChanged = { newRating -> rating = newRating })
    }
}

@Composable
fun RatingBar(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        for (i in 1..5) {
            val drawable = if (i <= rating) {
                Icons.Filled.Star
            } else {
                Icons.Outlined.Star
            }
            Icon(
                painter = rememberVectorPainter(drawable),
                contentDescription = null,
                modifier = Modifier.clickable { onRatingChanged(i) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyScreenPreview() {
    val myPlayground = Playground(
        id = 1,
        name = "Playground name",
        address = "Playground address",
        sport = Sports.VOLLEYBALL
    )

    PlaygroundsReservationsTheme {
        RatingPlaygroundsScreenContent(playground = myPlayground)
    }
}