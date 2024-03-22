package heartRateMonitor.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import java.time.LocalDate

// CLASS TO SELECT THE DATE AND TO CHECK IF THE SELECTED DATE IS WITHIN RANGE OF 1 WEEK

class DateCheckHelper(private val selectedDay: MutableState<LocalDate>) {

    @Composable
    fun SelectDay(
        currentDayTitle: String,
    ) {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .height(70.dp)
                .padding(top = 30.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = {
                val newDate = selectedDay.value.minusDays(1)
                checkIsDateWithinRange(newDate, context)
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Day",
                    tint = Color.White
                )
            }

            Text(
                text = currentDayTitle, textAlign = TextAlign.Center, style = TextStyle(
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
            )
            if (selectedDay.value == LocalDate.now()) {
                IconButton(enabled = false, onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Day",
                        tint = Color.Transparent
                    )
                }
            } else {
                IconButton(onClick = {
                    val newDate = selectedDay.value.plusDays(1)
                    checkIsDateWithinRange(newDate, context)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Day",
                        tint = Color.White
                    )
                }
            }
        }
    }

    fun checkIsDateWithinRange(date: LocalDate, context: Context) {
        val now = LocalDate.now()

        if (date.isBefore(now.minusWeeks(1))) {
            Toast.makeText(
                context, "No data available past this date", Toast.LENGTH_SHORT
            ).show()
        } else {
            selectedDay.value = date
        }
    }
}