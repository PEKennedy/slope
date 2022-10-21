package ca.unb.mobiledev.slope

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.slider.Slider


class SettingsDialog(actHandle: CloseHandle) : androidx.fragment.app.DialogFragment() {
    private val activityHandle = actHandle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.pause_menu, container, false)
        val volSlider = view.findViewById<Slider>(R.id.volBar)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnMute = view.findViewById<Button>(R.id.btnMute)
        val btnClose = view.findViewById<Button>(R.id.btnClose)

        val sharedPref = activity?.getSharedPreferences(getString(R.string.PREF_FILE_NAME), Context.MODE_PRIVATE)

        // Load settings (?)
        sharedPref?.getFloat(getString(R.string.saved_volume_value_key), volSlider.value)

//        val btnSettings = view.findViewById<Button>(R.id.btnSettings)
//        val btnHelp = view.findViewById<Button>(R.id.btnHelp)
//        val btnQuit = view.findViewById<Button>(R.id.btnQuit)
//        val btnClose = view.findViewById<Button>(R.id.btnClose)
//

        btnSave.setOnClickListener {
            with (sharedPref?.edit()) {
                this?.putFloat(getString(R.string.saved_volume_value_key), volSlider.value)
            }
            sharedPref.apply { this?.all }
        }
        btnMute.setOnClickListener {
            volSlider.value = 0F;
        }
        btnClose.setOnClickListener {
            activityHandle.close()
        }

        return view
    }

}
