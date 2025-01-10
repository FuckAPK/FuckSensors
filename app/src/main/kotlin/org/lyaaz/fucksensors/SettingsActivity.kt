package org.lyaaz.fucksensors

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import org.lyaaz.fucksensors.sensor.SensorEnvironment
import org.lyaaz.fucksensors.sensor.SensorLocation
import org.lyaaz.fucksensors.sensor.SensorMotion
import org.lyaaz.fucksensors.sensor.SensorType
import org.lyaaz.fucksensors.ui.AppTheme as Theme

class SettingsActivity : ComponentActivity() {

    private var currentUiMode: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        currentUiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        setContent {
            Theme {
                SettingsScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newUiMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (newUiMode != currentUiMode) {
            recreate()
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    Theme {
        SettingsScreen()
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val prefs = Utils.getPrefs(context)

    data class SensorData<T>(
        val type: Class<T>,
        val name: String,
        val stringType: String
    ) where T : Enum<T>, T : SensorType

    val cats = listOf(
        SensorData(SensorMotion::class.java, stringResource(R.string.title_motion_cate), Settings.PREF_MOTION),
        SensorData(SensorLocation::class.java, stringResource(R.string.title_location_cate), Settings.PREF_LOCATION),
        SensorData(
            SensorEnvironment::class.java,
            stringResource(R.string.title_environment_cate),
            Settings.PREF_ENVIRONMENT
        )
    )

    var ALL by remember {
        mutableStateOf(prefs.getBoolean(Settings.PREF_ALL, true))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .imePadding()
    ) {
        item {
            SwitchPreferenceItem(
                title = stringResource(id = R.string.title_all),
                checked = ALL,
                onCheckedChange = {
                    ALL = it
                    prefs.edit {
                        putBoolean(Settings.PREF_ALL, it)
                    }
                },
            )
        }
        items(cats) { (sensorType, title, key) ->
            var all by remember {
                mutableStateOf(prefs.getBoolean(key, true))
            }
            PreferenceCategory(title) {
                SwitchPreferenceItem(
                    title = stringResource(id = R.string.title_all),
                    enabled = ALL,
                    checked = all,
                    onCheckedChange = {
                        all = it
                        prefs.edit {
                            putBoolean(key, it)
                        }
                    }
                )
                sensorType.enumConstants!!.forEach { sensor ->
                    val key = sensor.type.toString()
                    var checked by remember {
                        mutableStateOf(prefs.getBoolean(key, true))
                    }
                    SwitchPreferenceItem(
                        title = sensor.name.removePrefix("TYPE_"),
                        enabled = ALL and all,
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            prefs.edit {
                                putBoolean(key, it)
                            }
                        }
                    )
                }
            }
        }
        item {
            PreferenceCategory(
                title = stringResource(R.string.title_others_cate)
            ) {
                var checked by remember {
                    mutableStateOf(prefs.getBoolean(Settings.PREF_OTHERS, true))
                }
                SwitchPreferenceItem(
                    title = stringResource(R.string.title_all),
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        prefs.edit {
                            putBoolean(Settings.PREF_OTHERS, it)
                        }
                    },
                )
            }
        }
        item {
            Spacer(
                modifier = Modifier.windowInsetsBottomHeight(
                    WindowInsets.systemBars
                )
            )
        }
    }
}

@Composable
fun PreferenceCategory(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
fun SwitchPreferenceItem(
    title: String,
    summary: String? = null,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
    noSwitch: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 2.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(if (enabled) 1.0f else 0.6f)
                )
                if (summary != null) {
                    Text(
                        text = summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(if (enabled) 1.0f else 0.6f)
                    )
                }
            }
            if (!noSwitch) {
                Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
            }
        }
    }
}