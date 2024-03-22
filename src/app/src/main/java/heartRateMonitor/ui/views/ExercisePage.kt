package heartRateMonitor.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
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
import heartRateMonitor.ChipGymExercise
import heartRateMonitor.ChipRunning
import heartRateMonitor.ChipWalking
import heartRateMonitor.viewmodel.ActivityViewModel


class ExercisePage(
    private val navController: NavController, private val activityViewModel: ActivityViewModel
) {
    @Composable
    fun MainPage() {
        val listState = rememberScalingLazyListState()
        val contentModifier = Modifier
            .padding(bottom = 8.dp, end = 5.dp)
            .width(150.dp)
            .height(40.dp)
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            autoCentering = AutoCenteringParams(itemIndex = 1),
            state = listState,
            verticalArrangement = Arrangement.Center,
        ) {
            item { ChipWalking(contentModifier, navController, "Walking", activityViewModel) }
            item { ChipRunning(contentModifier, navController, "Running", activityViewModel) }
            item {
                ChipGymExercise(
                    contentModifier, navController, "Gym Exercise", activityViewModel
                )
            }
        }
    }
}