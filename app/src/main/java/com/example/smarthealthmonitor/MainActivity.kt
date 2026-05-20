package com.example.smarthealthmonitor
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smarthealthmonitor.navigation.SmartHealthNavGraph
import com.example.smarthealthmonitor.SmartHealthMonitorTheme
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            // Punto de entrada principal
            SmartHealthNavGraph()
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier
) {

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    SmartHealthMonitorTheme {

        Greeting("Android")
    }
}

@Preview(
    showBackground = true,
    name = "Light"
)

@Preview(
    showBackground = true,
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)

@Composable
fun ThemePreview() {

    SmartHealthMonitorTheme {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            Text(
                text = "SmartHealth Monitor",

                style = MaterialTheme.typography.headlineMedium,

                color = MaterialTheme.colorScheme.primary,

                modifier = Modifier.padding(24.dp)
            )
        }
    }
}