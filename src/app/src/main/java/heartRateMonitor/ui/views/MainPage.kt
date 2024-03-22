package heartRateMonitor.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import heartRateMonitor.ChipActivities
import heartRateMonitor.ChipDaily
import heartRateMonitor.ChipEmotion
import heartRateMonitor.ChipExercise
import heartRateMonitor.ChipHabits
import heartRateMonitor.HeartRateScreen

class MainPage(private val navController: NavController) {

    @Composable
    fun HomePage() {
        val listState = rememberScalingLazyListState()
        val contentModifier = Modifier
            .width(65.dp)
            .padding(bottom = 6.dp)
            .height(40.dp)
        val statisticsModifier = Modifier
            .padding(top = 10.dp)
            .height(40.dp)
            .width(150.dp)

        Scaffold(timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        }, vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }, positionIndicator = {
            PositionIndicator(
                scalingLazyListState = listState
            )
        }) {
            ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                autoCentering = AutoCenteringParams(itemIndex = 1),
                state = listState,
                verticalArrangement = Arrangement.Center,
            ) {
                item { HeartRateScreen() }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ChipExercise(contentModifier, navController)
                        ChipEmotion(contentModifier, navController)
                        ChipHabits(contentModifier, navController)
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ChipDaily(statisticsModifier, navController = navController)
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ChipActivities(statisticsModifier, navController = navController)
                    }
                }
            }
        }
    }
}