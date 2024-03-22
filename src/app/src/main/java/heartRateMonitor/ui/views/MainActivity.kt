package heartRateMonitor.ui.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import heartRateMonitor.MainNavGraph
import heartRateMonitor.SplashScreen
import heartRateMonitor.data.entities.LogData
import heartRateMonitor.ui.theme.ComposeForWearOSTheme
import heartRateMonitor.utils.HeartRateNotificationService
import heartRateMonitor.utils.Restarter
import heartRateMonitor.viewmodel.ActivityViewModel
import heartRateMonitor.viewmodel.HeartRateViewModel
import heartRateMonitor.viewmodel.LogDataViewModel
import heartRateMonitor.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val activityViewModel: ActivityViewModel by viewModels()
    private val heartRateViewModel: HeartRateViewModel by viewModels()
    private val logDataViewModel: LogDataViewModel by viewModels()
    private var startTime: Long = 0

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val isGranted = it.value

                when (it.key) {
                    Manifest.permission.BODY_SENSORS -> {
                        if (!isGranted) {
                            showToast("Body sensor permission denied")
                        }
                    }

                    Manifest.permission.POST_NOTIFICATIONS -> {
                        if (!isGranted) {
                            showToast("Notification permission denied")
                        }
                    }

                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        if (!isGranted) {
                            showToast("Location permission denied")
                        }
                    }

                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        if (!isGranted) {
                            showToast("Location permission denied")
                        }
                    }
                }

            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionsIfNeeded()
        setContent {
            ComposeForWearOSTheme {
                WearApp()
            }
        }
    }

    @Composable
    fun WearApp() {
        val isLoading by viewModel.isLoading.collectAsState()
        val navController = rememberSwipeDismissableNavController()
        if (isLoading) {
            SplashScreen()
        } else {
            MainNavGraph(
                navController = navController,
                activityViewModel = activityViewModel,
                heartRateViewModel
            )
        }
    }

    private fun requestPermissionsIfNeeded() {
        val permissionsToRequest = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.BODY_SENSORS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            val intent = Intent(this, HeartRateNotificationService::class.java)
            startForegroundService(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        startTime = SystemClock.elapsedRealtime()
    }

    override fun onPause() {
        super.onPause()
        val endTime = SystemClock.elapsedRealtime()
        val duration = endTime - startTime
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val logData = LogData(duration, time, date)
        logDataViewModel.insertLogData(logData)
    }

    override fun onDestroy() {
        super.onDestroy()
        val broadcastIntent = Intent()
        broadcastIntent.action = "restart_service"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
    }
}