package heartRateMonitor

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import heartRateMonitor.ui.views.ActivityDetailPage
import heartRateMonitor.ui.views.ActivityPage
import heartRateMonitor.ui.views.DailyPage
import heartRateMonitor.ui.views.EmotionsPage
import heartRateMonitor.ui.views.ExercisePage
import heartRateMonitor.ui.views.HabitsPage
import heartRateMonitor.ui.views.MainPage
import heartRateMonitor.viewmodel.ActivityViewModel
import heartRateMonitor.viewmodel.HeartRateViewModel

@Composable
fun MainNavGraph(
    navController: NavHostController,
    activityViewModel: ActivityViewModel,
    heartRateViewModel: HeartRateViewModel
) {

    SwipeDismissableNavHost(navController = navController, startDestination = "main_page") {
        composable("main_page") {
            MainPage(navController).HomePage()
        }
        composable("exercise_page") {
            ExercisePage(navController, activityViewModel).MainPage()
        }
        composable("emotions_page") {
            EmotionsPage(navController, activityViewModel).MainPage()
        }
        composable("habits_page") {
            HabitsPage(navController, activityViewModel).MainPage()
        }
        composable("daily_page") {
            DailyPage().MainPage(heartRateViewModel, activityViewModel)
        }
        composable("activities_page") {
            ActivityPage(navController).MainPage(activityViewModel)
        }
        composable(
            route = "activityDetail/{activityId}",
            arguments = listOf(navArgument("activityId") { type = NavType.IntType })
        ) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getInt("activityId") ?: return@composable
            ActivityDetailPage(activityViewModel, activityId)
        }
    }
}
