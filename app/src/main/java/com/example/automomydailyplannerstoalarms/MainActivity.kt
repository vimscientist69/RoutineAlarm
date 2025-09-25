package com.example.automomydailyplannerstoalarms

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.automomydailyplannerstoalarms.ui.theme.AutomomyDailyPlannersToAlarmsTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    private val weekdayText = mutableStateOf("")
    private val tuesdayText = mutableStateOf("")
    private val saturdayText = mutableStateOf("")
    private val sundayText = mutableStateOf("")

    // Queue to store all alarm intents
    private val alarmQueue = mutableListOf<Intent>()
    private var alarmLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weekdayLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { weekdayText.value = readTextFromUri(it) }
        }

        val tuesdayLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { tuesdayText.value = readTextFromUri(it) }
        }

        val saturdayLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { saturdayText.value = readTextFromUri(it) }
        }

        val sundayLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { sundayText.value = readTextFromUri(it) }
        }

        // Launcher to sequentially process alarms
        alarmLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (alarmQueue.isNotEmpty()) {
                val next = alarmQueue.removeAt(0)
                alarmLauncher?.launch(next)
            } else {
                Toast.makeText(this, "All alarms processed.", Toast.LENGTH_LONG).show()
            }
        }

        setContent {
            AutomomyDailyPlannersToAlarmsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Button(onClick = { weekdayLauncher.launch("text/*") }, modifier = Modifier.fillMaxWidth()) {
                            Text("Upload Weekday Planner")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { tuesdayLauncher.launch("text/*") }, modifier = Modifier.fillMaxWidth()) {
                            Text("Upload Tuesday Planner")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { saturdayLauncher.launch("text/*") }, modifier = Modifier.fillMaxWidth()) {
                            Text("Upload Saturday Planner")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { sundayLauncher.launch("text/*") }, modifier = Modifier.fillMaxWidth()) {
                            Text("Upload Sunday Planner")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { enqueueAllAlarms() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Create All Alarms")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    private fun readTextFromUri(uri: android.net.Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }

    // Enqueue all planners sequentially
    private fun enqueueAllAlarms() {
        alarmQueue.clear()
        createAlarms("weekday", weekdayText.value)
        createAlarms("tuesday", weekdayText.value)
        createAlarms("saturday", saturdayText.value)
        createAlarms("sunday", sundayText.value)

        // Start processing the first alarm
        if (alarmQueue.isNotEmpty()) {
            val first = alarmQueue.removeAt(0)
            alarmLauncher?.launch(first)
        }
    }

    private fun createAlarms(type: String, planner: String) {
        if (planner.isBlank()) return

        val regex = Regex("""(\d{1,2}):(\d{2})(am|pm)? - .* / (.+)""", RegexOption.IGNORE_CASE)

        val daysOfWeek = when (type) {
            "weekday" -> listOf(
                java.util.Calendar.MONDAY,
                java.util.Calendar.WEDNESDAY,
                java.util.Calendar.THURSDAY,
                java.util.Calendar.FRIDAY
            )
            "tuesday" -> listOf(
                java.util.Calendar.TUESDAY,
            )
            "saturday" -> listOf(java.util.Calendar.SATURDAY)
            "sunday" -> listOf(java.util.Calendar.SUNDAY)
            else -> emptyList()
        }

        planner.lines().forEach { line ->
            val match = regex.find(line.trim())
            if (match != null) {
                var hour = match.groupValues[1].toInt()
                val minute = match.groupValues[2].toInt()
                val ampm = match.groupValues[3]?.lowercase()
                val label = match.groupValues[4].trim()

                if (hour in 1..12 && ampm != null) {
                    if (ampm == "pm" && hour != 12) hour += 12
                    if (ampm == "am" && hour == 12) hour = 0
                }

                val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_HOUR, hour)
                    putExtra(AlarmClock.EXTRA_MINUTES, minute)
                    putExtra(AlarmClock.EXTRA_MESSAGE, label)
                    putExtra(AlarmClock.EXTRA_DAYS, ArrayList(daysOfWeek))
                }

                alarmQueue.add(intent)
            }
        }
    }
}
