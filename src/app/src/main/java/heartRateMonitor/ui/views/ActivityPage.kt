package heartRateMonitor.ui.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import heartRateMonitor.SplashScreen
import heartRateMonitor.data.entities.ActivityInfo
import heartRateMonitor.utils.DateCheckHelper
import heartRateMonitor.viewmodel.ActivityViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ActivityPage(
    private val navController: NavController
) {
    private val selectedDay = mutableStateOf(LocalDate.now())
    private val isDataAvailable = mutableStateOf(true)
    private val activityColorMap = mapOf(
        "Walking" to Color(0xFF4876FF),
        "Running" to Color(0xFF0084F7),
        "Gym Exercise" to Color(0xFF00CCF5),
        "Angry" to Color(0xFFFFF44F),
        "Anxious" to Color(0xFFEAAA00),
        "Scared" to Color(0xFFFDFD96),
        "Stressed" to Color(0xFFFFDF00),
        "Smoking" to Color(0xFF58DB5E),
        "Drinking Alcohol" to Color(0xFF8BC34A),
    )

    @Composable
    fun MainPage(activityViewModel: ActivityViewModel) {
        val todayActivities by activityViewModel.todayActivities.observeAsState(initial = emptyList())
        val isLoading = remember { mutableStateOf(true) }
        val isDataReady = remember { mutableStateOf(false) }
        var currentDayTitle by remember { mutableStateOf("Daily Activities") }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(selectedDay.value) {
            isLoading.value = true
            isDataReady.value = false

            coroutineScope.launch {
                try {
                    val dateStr = selectedDay.value.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    currentDayTitle =
                        selectedDay.value.format(DateTimeFormatter.ofPattern("MMM dd"))

                    val loadActivities = async { activityViewModel.loadActivitiesForDate(dateStr) }
                    val loadAllActivities = async { activityViewModel.getAllActivities() }

                    awaitAll(loadActivities, loadAllActivities)
                    delay(500)
                    isDataReady.value = true
                } catch (e: Exception) {
                    Log.e("MainPage", "Error retrieving data", e)
                } finally {
                    isLoading.value = false
                }
            }
        }

        if (isLoading.value) {
            SplashScreen()
        } else if (isDataReady.value) {
            ShowActivities(
                todayActivities = todayActivities,
                currentDayTitle = currentDayTitle,
                isDataAvailable = todayActivities.isNotEmpty(),
            )
        }
    }


    @Composable
    fun ShowActivities(
        todayActivities: List<ActivityInfo>, currentDayTitle: String, isDataAvailable: Boolean
    ) {
        if (isDataAvailable) {
            val scalingLazyState = remember { ScalingLazyListState(initialCenterItemIndex = 0) }

            Scaffold(vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
                positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyState) }) {
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scalingLazyState,
                    anchorType = ScalingLazyListAnchorType.ItemStart,
                    autoCentering = null
                ) {
                    item {
                        val dateCheckHelper = DateCheckHelper(selectedDay)
                        dateCheckHelper.SelectDay(currentDayTitle = currentDayTitle)
                    }
                    items(todayActivities) { activity ->
                        ActivityRow(activity) {
                            navController.navigate("activityDetail/${activity.id}")
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        } else {
            val scalingLazyState = remember { ScalingLazyListState(initialCenterItemIndex = 0) }

            Scaffold(vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
                positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyState) }) {
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scalingLazyState,
                    anchorType = ScalingLazyListAnchorType.ItemStart,
                    autoCentering = null
                ) {
                    item {
                        val dateCheckHelper = DateCheckHelper(selectedDay)
                        dateCheckHelper.SelectDay(currentDayTitle = currentDayTitle)
                    }
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("No Data Available", style = MaterialTheme.typography.title1)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ActivityRow(activity: ActivityInfo, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(start = 15.dp, bottom = 2.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = activity.time,
                style = MaterialTheme.typography.body2,
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = activity.activityName,
                fontSize = 14.sp,
                color = activityColorMap[activity.activityName] ?: Color.White,
                textAlign = TextAlign.Right,
            )
        }
    }
}


