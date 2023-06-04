package it.polito.mad.playgroundsreservations.reservations.favorite_playgrounds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.mad.playgroundsreservations.Global
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sport
import it.polito.mad.playgroundsreservations.reservations.LoadingScreen
import it.polito.mad.playgroundsreservations.reservations.ViewModel
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PlaygroundsReservationsTheme
import it.polito.mad.playgroundsreservations.reservations.ui.theme.SecondaryColor

class FavoriteCourtsFragment: Fragment() {
    lateinit var reservation: MutableState<Reservation?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ACTIVITY TITLE
        activity?.title = "Favorite courts"

        return ComposeView(requireContext()).apply {
            setContent {
                PlaygroundsReservationsTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        FavoritePlaygroundsScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritePlaygroundsScreen() {
    val viewModel: ViewModel = viewModel()

    var stillLoading by remember { mutableStateOf(true) }
    val playgrounds = remember { mutableStateListOf<Playground>() }
    val favoritePlaygrounds = remember { mutableStateListOf<Playground>() }

    LaunchedEffect(true) {
        viewModel.getPlaygrounds(playgrounds)
        viewModel.getUserPlaygrounds(Global.userId!!, favoritePlaygrounds)
    }

    if (stillLoading && (   // prevents from going back into loading
            playgrounds.isEmpty() || favoritePlaygrounds.isEmpty()
        )
    )
        LoadingScreen()
    else {
        stillLoading = false
        FavoritePlaygroundsScreenContent(
            playgrounds = playgrounds,
            favoritePlaygrounds = favoritePlaygrounds
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePlaygroundsScreenContent(
    playgrounds: SnapshotStateList<Playground>,
    favoritePlaygrounds: SnapshotStateList<Playground>
) {
    var searchQuery by remember { mutableStateOf("") }
    val sportFilter = remember { mutableStateOf< Sport?>(null) }
    val regionFilter = remember { mutableStateOf<String?>(null) }
    val cityFilter = remember { mutableStateOf<String?>(null) }

    Column {
        Row {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                maxLines = 1,
                label = { Text(text = "Search all playgrounds")},
                colors = TextFieldDefaults.textFieldColors(
                    focusedLabelColor = SecondaryColor,
                    focusedIndicatorColor = SecondaryColor
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { searchQuery = "" },
                        modifier = Modifier.padding(horizontal = 5.dp)
                    ) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear")
                    }
                }
            )
        }

        PlaygroundsFilters(
            playgrounds = playgrounds,
            sportFilter = sportFilter,
            regionFilter = regionFilter,
            cityFilter = cityFilter
        )

        if (searchQuery == "" && sportFilter.value == null && regionFilter.value == null && cityFilter.value == null) {
            FavoritePlaygroundsList(playgrounds = favoritePlaygrounds)
        } else {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(
                    items = playgrounds.filter { playground ->
                        playground.name.contains(searchQuery, ignoreCase = true) &&
                                (sportFilter.value == null || playground.sport == sportFilter.value) &&
                                (regionFilter.value == null || playground.region == regionFilter.value) &&
                                (cityFilter.value == null || playground.city == cityFilter.value)
                    }, key = { it.id }) { playground ->
                    FavoritePlaygroundBox(
                        playground = playground,
                        favoritePlaygrounds = favoritePlaygrounds
                    )
                }
            }
        }

    }
}