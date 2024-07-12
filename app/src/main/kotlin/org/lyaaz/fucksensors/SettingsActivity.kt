package org.lyaaz.fucksensors

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import org.lyaaz.fucksensors.sensor.SensorEnvironment
import org.lyaaz.fucksensors.sensor.SensorLocation
import org.lyaaz.fucksensors.sensor.SensorMotion

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("WorldReadableFiles")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        try {
            @Suppress("DEPRECATION")
            getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", MODE_WORLD_READABLE)
        } catch (e: Exception) {
            getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", MODE_PRIVATE)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, MySettingsFragment())
            .commit()
    }

    class MySettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            val preferenceScreen = this.preferenceScreen

            val firstTitle = resources.getString(R.string.title_all)

            // Motion
            val motionCate = PreferenceCategory(requireContext()).apply {
                title = resources.getString(R.string.title_motion_cate)
                isIconSpaceReserved = false
            }
            preferenceScreen.addPreference(motionCate)

            motionCate.addPreference(
                SwitchPreferenceCompat(requireContext()).apply {
                    key = Settings.PREF_MOTION
                    title = firstTitle
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
            SensorMotion.values().asSequence().forEach {
                val s = SwitchPreferenceCompat(requireContext()).apply {
                    key = it.type.toString()
                    title = it.name.substring(5)
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
                motionCate.addPreference(s)
            }

            for (i in 1 until motionCate.preferenceCount) {
                motionCate.getPreference(i).dependency = Settings.PREF_MOTION
            }

            // Location
            val locationCate = PreferenceCategory(requireContext()).apply {
                title = resources.getString(R.string.title_location_cate)
                isIconSpaceReserved = false
            }
            preferenceScreen.addPreference(locationCate)

            locationCate.addPreference(
                SwitchPreferenceCompat(requireContext()).apply {
                    key = Settings.PREF_LOCATION
                    title = firstTitle
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
            SensorLocation.values().asSequence().forEach {
                locationCate.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = it.type.toString()
                        title = it.name.substring(5)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )
            }
            for (i in 1 until locationCate.preferenceCount) {
                locationCate.getPreference(i).dependency = Settings.PREF_LOCATION
            }


            // Environment
            val environmentCate = PreferenceCategory(requireContext()).apply {
                title = resources.getString(R.string.title_environment_cate)
                isIconSpaceReserved = false
            }
            preferenceScreen.addPreference(environmentCate)

            environmentCate.addPreference(
                SwitchPreferenceCompat(requireContext()).apply {
                    key = Settings.PREF_ENVIRONMENT
                    title = firstTitle
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
            SensorEnvironment.values().asSequence().forEach {
                environmentCate.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = it.type.toString()
                        title = it.name.substring(5)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )
            }
            for (i in 1 until environmentCate.preferenceCount) {
                environmentCate.getPreference(i).dependency = Settings.PREF_ENVIRONMENT
            }

            // Others
            val othersCate = PreferenceCategory(requireContext()).apply {
                title = resources.getString(R.string.title_others_cate)
                isIconSpaceReserved = false
            }
            preferenceScreen.addPreference(othersCate)

            othersCate.addPreference(
                SwitchPreferenceCompat(requireContext()).apply {
                    key = Settings.PREF_OTHERS
                    title = firstTitle
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}