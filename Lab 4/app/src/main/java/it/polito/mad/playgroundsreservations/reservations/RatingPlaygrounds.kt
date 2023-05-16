package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.compose.rememberNavController
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.PlaygroundRating
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
        activity?.title = activity?.resources?.getString(R.string.rate_court)

        return ComposeView(requireContext()).apply {
            setContent {
                PlaygroundsReservationsTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        RatingPlaygroundsScreen(args.playgroundId,args.reservationId)
                    }
                }
            }
        }
    }
}

@Composable
fun RatingPlaygroundsScreen(playgroundId: Int,reservationId:Int) {
    val reservationsViewModel: ReservationsViewModel = viewModel()
    val playground: MutableState<Playground?> = remember { mutableStateOf(null) }
    reservationsViewModel.getPlayground(playgroundId, playground)

    if (playground.value == null)
        Text("Loading")
    else
        RatingPlaygroundsScreenContent(playground = playground.value!!,reservationId=reservationId)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingPlaygroundsScreenContent(playground: Playground,reservationId: Int) {
    var rating by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }
    val context=LocalContext.current;
    val image = when (playground.sport) {
        Sports.TENNIS -> R.drawable.tennis_court
        Sports.BASKETBALL -> R.drawable.basketball_court
        Sports.FOOTBALL -> R.drawable.football_pitch
        Sports.VOLLEYBALL -> R.drawable.volleyball_court
        Sports.GOLF -> R.drawable.golf_field
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Aggiunto padding
        verticalArrangement = Arrangement.Top, // Modificato l'allineamento verticale
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = playground.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape) // Applica il clipping circolare al contenitore Box
                .border(1.dp, Color.Black, CircleShape) // Aggiungi un bordo al cerchio
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize() // L'immagine occupa l'intero spazio disponibile nel contenitore Box
            )
        }

        Text(text = playground.address)
        Text(text = playground.sport.toString())
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically, // Allineamento verticale
            modifier = Modifier.padding(bottom = 16.dp) // Aggiunto padding inferiore
        ) {
            Text(text = stringResource(id = R.string.rating_label), fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp)) // Aggiunta label
            RatingBar(rating = rating, onRatingChanged = { newRating -> rating = newRating })
        }
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(text = stringResource(id = R.string.optional_description_rating)) }
        )
        Button(
            onClick = {

                      val playgroundRating=PlaygroundRating(playgroundId = playground.id, reservationId =reservationId , rating = rating, description = text);
            //    val navController = rememberNavController()
              //  val action = ShowReservationFragmentDirections.actionShowReservationFragmentToRatingPlaygrounds(myReservation.playgroundId,myReservation.id)
                //navController.navigate(action)


            },
            content = { Text(stringResource(id =R.string.save_rating)) }
        )
    }
}
@Composable
fun RatingBar(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        for (i in 1..5) {
            val drawable = if (i <= rating) {
                R.drawable.baseline_star_24
            } else {
               R.drawable.baseline_star_border_24
            }
            Image(
                painter = painterResource(drawable),
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
        RatingPlaygroundsScreenContent(playground = myPlayground, reservationId = 1)
    }
}