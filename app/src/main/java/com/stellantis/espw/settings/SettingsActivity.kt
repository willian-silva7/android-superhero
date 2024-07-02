package com.stellantis.espw.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.stellantis.espw.databinding.ActivitySettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "settings")

class SettingsActivity: AppCompatActivity() {

    companion object{
        const val VOLUME_LVL = "volume_lvl"
        const val KEY_BLUETOOTH = "key_bluetooh"
        const val KEY_DARKMODE = "key_darkmode"
        const val KEY_VIBRATION = "key_vibration"
    }

    private lateinit var binding: ActivitySettingsBinding
    private var firsTime:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            getSettings().filter { firsTime }.collect {settingsModel ->
                if (settingsModel != null) {
                    runOnUiThread{
                        binding.switchBluetooh.isChecked = settingsModel.bluetooh
                        binding.switchDarkMode.isChecked = settingsModel.darkMode
                        binding.switchVibration.isChecked = settingsModel.vibration
//                    binding.rangeSlider.setValues(settingsModel.volume.toFloat())
                        firsTime = !firsTime
                    }
                }
            }
        }

        initUI()
    }

    private fun initUI() {
        /*binding.rangeSlider.addOnChangeListener { slider, value, fromUser ->
            Log.i("Aris", "O vaklor é: $value" )
            CoroutineScope(Dispatchers.IO).launch {
                saveVolume(value.toInt() )
            }
        }*/

        binding.switchBluetooh.setOnCheckedChangeListener { buttonView, value ->
            CoroutineScope(Dispatchers.IO).launch {
                saveOptions(KEY_BLUETOOTH, value)
            }
        }
        binding.switchDarkMode.setOnCheckedChangeListener { buttonView, value ->
            if (value) {
                enableDarkMode()
            }else{
                disableDarkMode()
            }

            CoroutineScope(Dispatchers.IO).launch {
                saveOptions(KEY_DARKMODE, value)
            }
        }
        binding.switchVibration.setOnCheckedChangeListener { buttonView, value ->
            CoroutineScope(Dispatchers.IO).launch {
                saveOptions(KEY_VIBRATION, value)
            }
        }
    }

    private suspend fun saveVolume(value: Int){
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(VOLUME_LVL)] = value
        }
    }

    private suspend fun saveOptions(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    private fun getSettings(): Flow<SettingsModel?>{
        return dataStore.data.map { preferences ->
                SettingsModel(
                    volume = preferences[intPreferencesKey(VOLUME_LVL)] ?: 50,  // Valor padrão se a chave não existir
                    bluetooh = preferences[booleanPreferencesKey(KEY_BLUETOOTH)] ?: true,
                    darkMode = preferences[booleanPreferencesKey(KEY_DARKMODE)] ?: false, // Valor padrão se a chave não existir
                    vibration = preferences[booleanPreferencesKey(KEY_VIBRATION)] ?: true
                )
            }
    }

    private fun enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        delegate.applyDayNight()
    }

    private fun disableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        delegate.applyDayNight()
    }

}