package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.smarttoolfactory.ratingbar.RatingBar
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
                        SeeRatingsScreen(
                            playgroundId = args.playgroundId,
                            navController = findNavController()
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SeeRatingsScreen(playgroundId: String, navController: NavController) {
    val reservationsViewModel: ReservationsViewModel = viewModel()

    val ratingsList = remember { mutableStateOf(listOf<PlaygroundRating>()) }
    reservationsViewModel.getRatingsByPlaygroundId(playgroundId,ratingsList)



    if (ratingsList==null)
        Text("Loading")
    else
        Prova(paramValue = playgroundId, ratingsList)
    //SeeRatingsScreenContent(playgroundId = , navController = )
    // SeeRatingsScreenContent(playground = playground.value!!, reservationId = 3, navController)
}

@Composable
fun Prova(paramValue: String, ratingList: MutableState<List<PlaygroundRating>>) {
// Esegui l'osservazione quando ratings cambia
    // Puoi fare qualcosa con i nuovi valori di ratingsState qui
    //Text(text = ratingList.toString())
    LazyColumn{
        items(ratingList.value){item: PlaygroundRating ->
            ListItemComponent(item = item)
        }
    }
   /* for (item in ratingList.value) {
        ListItemComponent(item = item)
    }

    */
}
/* LazyColumn {
    items(ratingsList) { item ->
        ListItemComponent(item)
     }

 */


@Composable
fun ListItemComponent(item: PlaygroundRating) {
    var isExpanded by remember { mutableStateOf(false) }
    var starCount = item.rating
    var i = 1;

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { if (item.description != "") isExpanded = !isExpanded }
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


            RatingBar(
                rating = starCount,
                space = 2.dp,
                imageVectorEmpty = ImageVector.vectorResource(id = R.drawable.bordered_star),
                imageVectorFFilled = ImageVector.vectorResource(id = R.drawable.filled_star),
                tintEmpty = Color(0xff00668b),
                tintFilled = Color(0xff00668b),
                itemSize = 36.dp,
                gestureEnabled = false
            ) {
                starCount = it
            }
            if (item.description != "") {
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    var res:Int
                    if (isExpanded==true)
                        res = R.drawable.arrow_circle_up
                    else
                        res=R.drawable.arrow_circle_down

                    Icon(
                            painter = painterResource(res),
                        contentDescription = null
                    )
                }
            }


        }

        if (isExpanded) {
            Text(
                text = item.description,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}