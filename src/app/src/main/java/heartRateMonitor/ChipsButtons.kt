package heartRateMonitor

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import heartRateMonitor.viewmodel.ActivityViewModel
import com.mutualmobile.composesensors.rememberHeartRateSensorState


@Composable
fun ChipDaily(
    modifier: Modifier = Modifier,
    navController: NavController,
) {

    Box(modifier = modifier
        .background(Color(0xFF788597), shape = RoundedCornerShape(24.dp))
        .clickable {
            navController.navigate("daily_page")
        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Daily Metrics",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipActivities(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Box(modifier = modifier
        .background(Color(0xFF788597), shape = RoundedCornerShape(24.dp))
        .clickable {
            navController.navigate("activities_page")
        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Daily Events",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipExercise(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Box(modifier = modifier
        .background(Color(0xFF4A76E4), shape = RoundedCornerShape(24.dp))
        .clickable {
            navController.navigate("exercise_page")
        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Exercise",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipEmotion(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Box(modifier = modifier
        .background(Color(0xFFFFD300), shape = RoundedCornerShape(24.dp))
        .clickable {
            navController.navigate("emotions_page")
        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Emotions",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipHabits(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Box(modifier = modifier
        .background(Color(0xFF72F862), shape = RoundedCornerShape(24.dp))
        .clickable {
            navController.navigate("habits_page")
        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Habits",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipWalking(
    modifier: Modifier = Modifier,
    navController: NavController,
    activity: String,
    activityViewModel: ActivityViewModel,
) {
    val context = LocalContext.current
    val heartRateState = rememberHeartRateSensorState()

    Box(modifier = modifier
        .background(Color(0xFF4876FF), shape = RoundedCornerShape(24.dp))
        .clickable {
            val heartRate = kotlin.math
                .ceil(heartRateState.heartRate)
                .toInt()
            clickAddActivity(context, activity, heartRate, activityViewModel)
            navController.navigate("main_page")
        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipRunning(
    modifier: Modifier = Modifier,
    navController: NavController,
    activity: String,
    activityViewModel: ActivityViewModel,
) {
    val context = LocalContext.current
    val heartRateState = rememberHeartRateSensorState()

    Box(modifier = modifier
        .background(Color(0xFF0084F7), shape = RoundedCornerShape(24.dp))
        .clickable {
            val heartRate = kotlin.math
                .ceil(heartRateState.heartRate)
                .toInt()
            clickAddActivity(context, activity, heartRate, activityViewModel)
            navController.navigate("main_page")
        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipGymExercise(
    modifier: Modifier = Modifier,
    navController: NavController,
    activity: String,
    activityViewModel: ActivityViewModel,
) {
    val context = LocalContext.current
    val heartRateState = rememberHeartRateSensorState()

    Box(modifier = modifier
        .background(Color(0xFF00CCF5), shape = RoundedCornerShape(24.dp))
        .clickable {
            val heartRate = kotlin.math
                .ceil(heartRateState.heartRate)
                .toInt()
            clickAddActivity(context, activity, heartRate, activityViewModel)
            navController.navigate("main_page")
        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipAngry(
    modifier: Modifier = Modifier,
    navController: NavController,
    activity: String,
    activityViewModel: ActivityViewModel,
) {
    val context = LocalContext.current
    val heartRateState = rememberHeartRateSensorState()

    Box(modifier = modifier
        .background(Color(0xFFFFF44F), shape = RoundedCornerShape(24.dp))
        .clickable {
            val heartRate = kotlin.math
                .ceil(heartRateState.heartRate)
                .toInt()
            clickAddActivity(context, activity, heartRate, activityViewModel)
            navController.navigate("main_page")

        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipAnxious(
    modifier: Modifier = Modifier,
    navController: NavController,
    activity: String,
    activityViewModel: ActivityViewModel,
) {
    val context = LocalContext.current
    val heartRateState = rememberHeartRateSensorState()

    Box(modifier = modifier
        .background(Color(0xFFEAAA00), shape = RoundedCornerShape(24.dp))
        .clickable {
            val heartRate = kotlin.math
                .ceil(heartRateState.heartRate)
                .toInt()
            clickAddActivity(context, activity, heartRate, activityViewModel)
            navController.navigate("main_page")

        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipScared(
    modifier: Modifier = Modifier,
    navController: NavController,
    activity: String,
    activityViewModel: ActivityViewModel,
) {
    val context = LocalContext.current
    val heartRateState = rememberHeartRateSensorState()

    Box(modifier = modifier
        .background(Color(0xFFFDFD96), shape = RoundedCornerShape(24.dp))
        .clickable {
            val heartRate = kotlin.math
                .ceil(heartRateState.heartRate)
                .toInt()
            clickAddActivity(context, activity, heartRate, activityViewModel)
            navController.navigate("main_page")

        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipStressed(
    modifier: Modifier = Modifier,
    navController: NavController,
    activity: String,
    activityViewModel: ActivityViewModel,
) {
    val context = LocalContext.current
    val heartRateState = rememberHeartRateSensorState()

    Box(modifier = modifier
        .background(Color(0xFFFFDF00), shape = RoundedCornerShape(24.dp))
        .clickable {
            val heartRate = kotlin.math
                .ceil(heartRateState.heartRate)
                .toInt()
            clickAddActivity(context, activity, heartRate, activityViewModel)
            navController.navigate("main_page")

        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}


@Composable
fun ChipSmoking(
    modifier: Modifier = Modifier,
    navController: NavController,
    activity: String,
    activityViewModel: ActivityViewModel,
) {
    val context = LocalContext.current
    val heartRateState = rememberHeartRateSensorState()

    Box(modifier = modifier
        .background(Color(0xFF58DB5E), shape = RoundedCornerShape(24.dp))
        .clickable {
            val heartRate = kotlin.math
                .ceil(heartRateState.heartRate)
                .toInt()
            clickAddActivity(context, activity, heartRate, activityViewModel)
            navController.navigate("main_page")

        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ChipDrinking(
    modifier: Modifier = Modifier,
    navController: NavController,
    activity: String,
    activityViewModel: ActivityViewModel
) {
    val context = LocalContext.current
    val heartRateState = rememberHeartRateSensorState()

    Box(modifier = modifier
        .background(Color(0xFF8BC34A), shape = RoundedCornerShape(24.dp))
        .clickable {
            val heartRate = kotlin.math
                .ceil(heartRateState.heartRate)
                .toInt()
            clickAddActivity(context, activity, heartRate, activityViewModel)
            navController.navigate("main_page")

        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

private fun clickAddActivity(
    context: Context, activity: String, heartRate: Int, viewModel: ActivityViewModel
) {
    addActivity(context, activity, heartRate, viewModel, 0.0, 0.0, "Unknown")
}

private fun addActivity(
    context: Context,
    activity: String,
    heartRate: Int,
    viewModel: ActivityViewModel,
    latitude: Double,
    longitude: Double,
    address: String
) {
    val activityAdded = viewModel.insertActivity(activity, heartRate, latitude, longitude, address)
    if (activityAdded) {
        Toast.makeText(context, "$activity Added!", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(
            context, "Heart Rate value not detected, $activity not added!", Toast.LENGTH_SHORT
        ).show()
    }
}