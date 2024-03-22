package heartRateMonitor.ui.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import heartRateMonitor.SplashScreen
import heartRateMonitor.viewmodel.ActivityViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ActivityDetailPage(activityViewModel: ActivityViewModel, activityId: Int) {
    val activityInfo = activityViewModel.getActivityById(activityId).observeAsState().value

    if (activityInfo != null) {
        val scalingLazyState = remember {
            ScalingLazyListState(
                initialCenterItemIndex = 0, initialCenterItemScrollOffset = 150
            )
        }

        Scaffold(vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
            positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyState) }) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scalingLazyState,
                anchorType = ScalingLazyListAnchorType.ItemStart,
                autoCentering = null,
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Text(
                        text = activityInfo.activityName,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(bottom = 10.dp, top = 20.dp)
                    )
                }
                item {
                    Text(
                        text = "Date: ${changeDateFormat(activityInfo.date)}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                    )
                }
                item {
                    Text(
                        text = "Time: ${activityInfo.time}",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                    )
                }
                item {
                    Text(
                        text = "Heart Rate: ${activityInfo.heartRate}",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                    )
                }
                item {
                    Text(
                        text = "Location: ${activityInfo.location}",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    } else {
        SplashScreen()
    }
}

fun changeDateFormat(date: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val newDate = LocalDate.parse(date, formatter)
    return newDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}
