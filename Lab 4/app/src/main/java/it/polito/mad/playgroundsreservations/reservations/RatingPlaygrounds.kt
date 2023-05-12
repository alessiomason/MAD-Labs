package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import android.widget.RatingBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.color.utilities.SchemeTonalSpot
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Sports
import it.polito.mad.playgroundsreservations.reservations.ui.theme.PlaygroundsReservationsTheme

class RatingPlaygrounds : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent?.extras?.getInt("myReservationId");

        setContent {
                PlaygroundsReservationsTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MyScreen(id)
                    }
                }
            }
        }
    }

    @Composable
    fun MyScreen(id:Int?) {
        var rating by remember {
            mutableStateOf(0)
        }
        val reservationsViewModel:ReservationsViewModel = viewModel()
        val reservations = reservationsViewModel.getUserReservations(1)
        //reservations.observe()
        reservations.value;


        var id = R.drawable.football_pitch;
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = id),
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
        PlaygroundsReservationsTheme {
            //MyScreen(id, sport =)
        }
    }