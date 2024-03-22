package heartRateMonitor.ui.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.example.android.wearable.composeforwearos.R
import heartRateMonitor.SplashScreen
import heartRateMonitor.data.entities.ActivityCount
import heartRateMonitor.data.entities.ActivityInfo
import heartRateMonitor.data.entities.HeartRateInfo
import heartRateMonitor.utils.DateCheckHelper
import heartRateMonitor.viewmodel.ActivityViewModel
import heartRateMonitor.viewmodel.HeartRateViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.sqrt

class DailyPage {

    private val selectedDay = mutableStateOf(LocalDate.now())
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
    fun MainPage(heartRateViewModel: HeartRateViewModel, activityViewModel: ActivityViewModel) {

        val todayActivities by activityViewModel.todayActivities.observeAsState(initial = emptyList())
        val retrievedHeartRates by heartRateViewModel.retrievedHeartRates.observeAsState(initial = emptyList())
        val maxHeartRate by heartRateViewModel.maxHeartRate.observeAsState(initial = 0)
        val minHeartRate by heartRateViewModel.minHeartRate.observeAsState(initial = 0)
        val isLoading = remember { mutableStateOf(true) }
        val isDataReady = remember { mutableStateOf(false) }
        var currentDayTitle by remember { mutableStateOf("Daily Metrics") }

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
                    val loadHeartRateStats =
                        async { heartRateViewModel.getHeartRateStatsEveryOneMinute(dateStr) }
                    val loadHeartRates = async { heartRateViewModel.getHeartRatesForDate(dateStr) }
                    val loadMaxHeartRate =
                        async { heartRateViewModel.getMaxHeartRateForDate(dateStr) }
                    val loadMinHeartRate =
                        async { heartRateViewModel.getMinHeartRateForDate(dateStr) }
                    val loadAllActivities = async { activityViewModel.getAllActivities() }

                    awaitAll(
                        loadActivities,
                        loadHeartRateStats,
                        loadHeartRates,
                        loadMaxHeartRate,
                        loadMinHeartRate,
                        loadAllActivities
                    )
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
            ActivityGraph(
                todayActivities = todayActivities,
                maxHeartRate = maxHeartRate,
                minHeartRate = minHeartRate,
                currentDayTitle = currentDayTitle,
                isDataAvailable = retrievedHeartRates.isNotEmpty(),
                retrievedHeartRates = retrievedHeartRates,
            )
        }
    }


    @Composable
    fun ActivityGraph(
        todayActivities: List<ActivityInfo>,
        maxHeartRate: Int?,
        minHeartRate: Int?,
        currentDayTitle: String,
        isDataAvailable: Boolean,
        retrievedHeartRates: List<HeartRateInfo>,
    ) {
        if (isDataAvailable) {
            val listState = rememberScalingLazyListState()

            val itemSpacing = 6.dp
            val scrollOffset = with(LocalDensity.current) {
                -(itemSpacing / 2).roundToPx()
            }
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
                        .fillMaxWidth()
                        .fillMaxSize(),
                    anchorType = ScalingLazyListAnchorType.ItemStart,
                    verticalArrangement = Arrangement.spacedBy(itemSpacing),
                    state = listState,
                    autoCentering = AutoCenteringParams(itemIndex = 2, itemOffset = scrollOffset)
                ) {
                    item {
                        val dateCheckHelper = DateCheckHelper(selectedDay)
                        dateCheckHelper.SelectDay(currentDayTitle = currentDayTitle)
                    }
                    item {
                        GenerateGraph(todayActivities, retrievedHeartRates)
                    }
                    item {
                        MaxAndMinView(maxHeartRate, minHeartRate, retrievedHeartRates)
                    }
                    item {
                        ColorLabelsView(todayActivities)
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
    fun GenerateGraph(
        todayActivities: List<ActivityInfo>, retrievedHeartRates: List<HeartRateInfo>
    ) {
        val canvasWidth = 200.dp
        val canvasHeight = 170.dp
        val startTime = 0f
        val endTime = 24f
        val maxHeartRate = 200
        val minHeartRate = 0
        val textMeasurer = rememberTextMeasurer()
        val activityPoints = mutableListOf<Pair<ActivityInfo, Offset>>()
        val context = LocalContext.current
        val zoomedInScale = 1.2f

        var scale by remember { mutableFloatStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        Box(
            modifier = Modifier
                .width(250.dp)
                .height(170.dp)
                .padding(start = 15.dp, end = 15.dp, top = 10.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Canvas(
                modifier = Modifier
                    .size(canvasWidth, canvasHeight)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(onDoubleTap = { tapOffset ->
                            if (scale == 1f) {
                                scale = zoomedInScale
                                offset = tapOffset
                            } else {
                                scale = 1f
                                offset = Offset.Zero
                            }
                        }, onTap = { tapOffset ->
                            activityPoints.forEach { (activity, circleOffset) ->
                                if (isPointInsideCircle(tapOffset, circleOffset)) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Event: ${activity.activityName} \n " + "Heart Rate: ${activity.heartRate} \n " + "Time: ${activity.time} \n " + "Location: ${activity.location}",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            }
                        })
                    },
            ) {
                val graphWidthSize = size.width - 30f
                val graphHeightSize = size.height - 50f

                val textStyle = TextStyle(color = Color.Gray, fontSize = 8.sp)
                drawLine(
                    color = Color.Gray,
                    start = Offset(-15f, graphHeightSize * (1 - 60f / maxHeartRate)),
                    end = Offset(graphWidthSize + 10f, graphHeightSize * (1 - 60f / maxHeartRate)),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
                )

                drawLine(
                    color = Color.Gray,
                    start = Offset(-15f, graphHeightSize * (1 - 100f / maxHeartRate)),
                    end = Offset(graphWidthSize + 10f, graphHeightSize * (1 - 100f / maxHeartRate)),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
                )

                todayActivities.forEach { activity ->

                    val (hours, minutes) = (activity.time).split(":").map { it.toInt() }
                    val totalActivityMinutes = hours * 60 + minutes
                    val totalStartMinutes = (startTime * 60).toInt()
                    val totalEndMinutes = (endTime * 60).toInt()
                    val normalizedTime =
                        (totalActivityMinutes - totalStartMinutes).toFloat() / (totalEndMinutes - totalStartMinutes)
                    val x = graphWidthSize * normalizedTime
                    val normalizedHeartRate =
                        (activity.heartRate - minHeartRate).toFloat() / (maxHeartRate - minHeartRate)
                    val y = graphHeightSize * (1 - normalizedHeartRate)
                    val color = activityColorMap[activity.activityName] ?: Color.Gray
                    drawCircle(color = color, center = Offset(x, y), radius = 10f)
                    val offSet = Offset(x, y)
                    activityPoints.add(activity to offSet)
                }


                val yLabels = listOf(0, 60, 100, 150, 200)
                yLabels.forEach { label ->
                    val yPos = graphHeightSize * (1 - label.toFloat() / maxHeartRate)
                    val xOffset = -30f
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "$label",
                        topLeft = Offset(0f + xOffset, yPos),
                        style = textStyle
                    )
                }

                val hours = listOf(0, 6, 12, 18, 24)

                hours.forEach { hour ->
                    val normalizedTime = (hour).toFloat() / (endTime - startTime)
                    val xPos = graphWidthSize * normalizedTime

                    drawText(
                        textMeasurer = textMeasurer,
                        text = "$hour",
                        topLeft = Offset(xPos, graphHeightSize + 30f),
                        style = textStyle
                    )
                }

                val sortedHeartRates = retrievedHeartRates.sortedBy { it.time }
                val path = Path()

                sortedHeartRates.forEachIndexed { index, heartRateInfo ->
                    val (hour, minute) = heartRateInfo.time.split(":").map { it.toInt() }
                    val time = hour * 60 + minute

                    val normalizedTime = time.toFloat() / (endTime * 60)
                    val x = graphWidthSize * normalizedTime

                    val normalizedHeartRate =
                        (heartRateInfo.heartRate - minHeartRate).toFloat() / (maxHeartRate - minHeartRate)
                    val y = graphHeightSize * (1 - normalizedHeartRate)

                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        val previousHeartRateInfo = sortedHeartRates[index - 1]
                        val (prevHour, prevMinute) = previousHeartRateInfo.time.split(":")
                            .map { it.toInt() }
                        val prevTime = prevHour * 60 + prevMinute
                        val timeDifference = time - prevTime
                        if (timeDifference <= 20) {
                            path.lineTo(x, y)
                        } else {
                            path.moveTo(x, y)
                        }
                    }
                }
                drawPath(
                    path = path,
                    color = Color(0xFFFF0000).copy(alpha = 0.5f),
                    style = Stroke(width = 2f)
                )
            }
        }
    }


    @Composable
    fun ColorLabelsView(todayActivities: List<ActivityInfo>) {
        val uniqueActivitiesInGraph = todayActivities.map { it.activityName }.distinct()
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalAlignment = Alignment.Start
            ) {
                val pairsOfActivities =
                    activityColorMap.entries.filter { it.key in uniqueActivitiesInGraph }.chunked(2)

                pairsOfActivities.forEach { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ActivityView(
                            ActivityCount(pair[0].key, 0),
                            Modifier
                                .width(90.dp)
                                .padding(end = 5.dp)
                        )

                        if (pair.size > 1) {
                            ActivityView(ActivityCount(pair[1].key, 0))
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ActivityView(activityCount: ActivityCount, modifier: Modifier = Modifier) {
        val color = activityColorMap[activityCount.activityName] ?: Color.Gray
        Row(
            modifier = modifier
        ) {
            Canvas(
                modifier = Modifier
                    .size(16.dp)
                    .padding(end = 4.dp)
            ) {
                drawCircle(color)
            }
            Text(
                text = activityCount.activityName, style = TextStyle(
                    fontSize = 10.sp, color = Color.White
                )
            )
        }
    }

    @Composable
    fun MaxAndMinView(
        maxHeartRateValue: Int?, minHeartRateValue: Int?, retrievedHeartRates: List<HeartRateInfo>
    ) {
        val restingHeartRate = getRestingHeartRate(retrievedHeartRates)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp, start = 2.dp)
        ) {
            Row(
                modifier = Modifier.width(150.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_heart_red),
                    contentDescription = "Maximum Heart Rate",
                    modifier = Modifier.size(18.dp)
                )
                if (maxHeartRateValue != null) {
                    if (maxHeartRateValue > 0) {
                        Text(text = "$maxHeartRateValue", fontSize = 12.sp)
                    } else {
                        Text(text = "-", fontSize = 12.sp, color = Color(0xFFFF0000))
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_heart_blue),
                    contentDescription = "Minimum Heart Rate",
                    modifier = Modifier.size(18.dp)
                )
                if (minHeartRateValue != null) {
                    if (minHeartRateValue > 0) {
                        Text(text = "$minHeartRateValue", fontSize = 12.sp)
                    } else {
                        Text(text = "-", fontSize = 12.sp, color = Color(0xFF0000FF))
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_heart_yellow),
                    contentDescription = "Resting Heart Rate",
                    modifier = Modifier.size(18.dp)
                )
                Text(text = restingHeartRate, fontSize = 12.sp)
            }
            Row(
                modifier = Modifier.width(150.dp)
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Max", textAlign = TextAlign.Center, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "Min", textAlign = TextAlign.Center, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(25.dp))
                Text(text = "Resting", textAlign = TextAlign.Center, fontSize = 12.sp)
            }
        }
    }

    private fun isPointInsideCircle(
        point: Offset, circleCenter: Offset
    ): Boolean {
        return sqrt((circleCenter.x - point.x).pow(2) + (circleCenter.y - point.y).pow(2)) <= 20.0
    }

    private fun getRestingHeartRate(retrievedHeartRates: List<HeartRateInfo>): String {
        val restingHeartRate = retrievedHeartRates.filter { it.restingHeartRate }
        val averageRestingHeartRate = restingHeartRate.map { it.heartRate }.average()
        val averageRestingHeartRateInt = averageRestingHeartRate.toInt()
        return averageRestingHeartRateInt.toString()
    }
}
