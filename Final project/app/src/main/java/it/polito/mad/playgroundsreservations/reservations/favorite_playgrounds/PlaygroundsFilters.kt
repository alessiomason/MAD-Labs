package it.polito.mad.playgroundsreservations.reservations.favorite_playgrounds

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Sport
import it.polito.mad.playgroundsreservations.reservations.ui.theme.SecondaryVariantColor

@Composable
fun PlaygroundsFilters(
    playgrounds: SnapshotStateList<Playground>,
    sportFilter: MutableState<Sport?>,
    regionFilter: MutableState<String?>,
    cityFilter: MutableState<String?>
) {
    val sportFilterExpanded = remember { mutableStateOf(false) }
    val regionFilterExpanded = remember { mutableStateOf(false) }
    val cityFilterExpanded = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = SecondaryVariantColor),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = sportFilter.value?.name ?: "All sports",
                    color = Color.White,
                    modifier = Modifier.clickable { sportFilterExpanded.value = true }
                )
                SportDropDownMenu(sportFilter = sportFilter, sportFilterExpanded = sportFilterExpanded)
            }

            Row {
                Text(
                    text = regionFilter.value ?: "All regions",
                    color = Color.White,
                    modifier = Modifier.clickable { regionFilterExpanded.value = true }
                )
                RegionDropDownMenu(
                    playgrounds = playgrounds,
                    regionFilter = regionFilter,
                    regionFilterExpanded = regionFilterExpanded
                )
            }

            Row {
                Text(
                    text = cityFilter.value ?: "All cities",
                    color = Color.White,
                    modifier = Modifier.clickable { cityFilterExpanded.value = true }
                )
                CityDropDownMenu(
                    playgrounds = playgrounds,
                    cityFilter = cityFilter,
                    cityFilterExpanded = cityFilterExpanded
                )
            }
        }
    }
}

@Composable
fun SportDropDownMenu(sportFilter: MutableState<Sport?>, sportFilterExpanded: MutableState<Boolean>) {
    DropdownMenu(
        expanded = sportFilterExpanded.value,
        onDismissRequest = {
            sportFilterExpanded.value = false
        }
    ) {
        Sport.values().forEach { item ->
            DropdownMenuItem(onClick = {
                sportFilter.value = item
                sportFilterExpanded.value = false
            }) {
                val sportName = when (item) {
                    Sport.TENNIS -> R.string.sport_tennis
                    Sport.BASKETBALL -> R.string.sport_basketball
                    Sport.FOOTBALL -> R.string.sport_football
                    Sport.VOLLEYBALL -> R.string.sport_volleyball
                    Sport.GOLF -> R.string.sport_golf
                }

                Text(text = stringResource(sportName))
            }
        }
    }
}

@Composable
fun RegionDropDownMenu(
    playgrounds: SnapshotStateList<Playground>,
    regionFilter: MutableState<String?>,
    regionFilterExpanded: MutableState<Boolean>
) {
    DropdownMenu(
        expanded = regionFilterExpanded.value,
        onDismissRequest = {
            regionFilterExpanded.value = false
        }
    ) {
        playgrounds.map {
            it.region
        }.toSet().forEach { item ->
            DropdownMenuItem(onClick = {
                regionFilter.value = item
                regionFilterExpanded.value = false
            }) {
                Text(text = item)
            }
        }
    }
}

@Composable
fun CityDropDownMenu(
    playgrounds: SnapshotStateList<Playground>,
    cityFilter: MutableState<String?>,
    cityFilterExpanded: MutableState<Boolean>
) {
    DropdownMenu(
        expanded = cityFilterExpanded.value,
        onDismissRequest = {
            cityFilterExpanded.value = false
        }
    ) {
        playgrounds.map {
            it.city
        }.toSet().forEach { item ->
            DropdownMenuItem(onClick = {
                cityFilter.value = item
                cityFilterExpanded.value = false
            }) {
                Text(text = item)
            }
        }
    }
}
