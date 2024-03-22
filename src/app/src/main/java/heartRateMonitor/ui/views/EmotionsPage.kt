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
import heartRateMonitor.ChipAngry
import heartRateMonitor.ChipAnxious
import heartRateMonitor.ChipScared
import heartRateMonitor.ChipStressed
import heartRateMonitor.viewmodel.ActivityViewModel


class EmotionsPage(
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
            item { ChipAngry(contentModifier, navController, "Angry", activityViewModel) }
            item { ChipAnxious(contentModifier, navController, "Anxious", activityViewModel) }
            item { ChipScared(contentModifier, navController, "Scared", activityViewModel) }
            item { ChipStressed(contentModifier, navController, "Stressed", activityViewModel) }
        }
    }
}