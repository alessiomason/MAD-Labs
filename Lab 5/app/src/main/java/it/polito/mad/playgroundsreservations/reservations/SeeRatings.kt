package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.PlaygroundRating
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PlaygroundsReservationsTheme


class SeeRatings : Fragment() {
    private val args by navArgs<SeeRatingsArgs>()
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
                        // RatingPlaygroundsScreen(args.playgroundId, args.reservationId, findNavController())
                            SeeRatingsScreen(playgroundId = args.playgroundId, navController =findNavController() )
                            //Prova(paramValue)
                        }
                    }
                }
            }
        }
    }


@Composable
fun SeeRatingsScreen(playgroundId: Int, navController: NavController) {
    val reservationsViewModel: ReservationsViewModel = viewModel()

    //var ratings: MutableState<PlaygroundRating?> = remember { mutableStateOf(null) }
    var ratings=reservationsViewModel.getRatingsByPlaygroundId(playgroundId);

    val ratingsList = remember { mutableStateListOf<PlaygroundRating>() }
    // Osserva il LiveData
    LaunchedEffect(ratings) {
        ratings.observeForever { rating ->
            rating?.let {
                for(el in it)
                    el?.let { it1 -> ratingsList.add(it1) }
            }
        }
    }


    //reservationsViewModel.get
    // reservationsViewModel.getPlaygroundRatings(playgroundId)

    if (ratings == null)
        Text("Loading")
    else
        Prova(paramValue = playgroundId,ratingsList);
    //SeeRatingsScreenContent(playgroundId = , navController = )
       // SeeRatingsScreenContent(playground = playground.value!!, reservationId = 3, navController)
}

@Composable
fun Prova(paramValue:Int, ratingList:SnapshotStateList<PlaygroundRating>) {
// Esegui l'osservazione quando ratings cambia
        // Puoi fare qualcosa con i nuovi valori di ratingsState qui

    for (item in ratingList)
    {
        ListItemComponent(item = item)
    }
}
   /* LazyColumn {
       items(ratingsList) { item ->
           ListItemComponent(item)
        }

    */


@Composable
fun ListItemComponent(item: PlaygroundRating) {
    var isExpanded by remember { mutableStateOf(false) }
    val starCount = item.rating
    var i=1;

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
           /* Text(
                text = "Rating"+i.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            */


            repeat(5) { index ->
                Icon(
                    painter = painterResource(
                        if (index < starCount) R.drawable.baseline_star_24 else R.drawable.baseline_star_border_24
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }

            if (item.description!="") {
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        painter = painterResource(R.drawable.basketball_ball),
                        contentDescription = null
                    )
                }
            }



        }

        if (isExpanded) {
            Text(
                text = "Testo della casella di testo a comparsa",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

