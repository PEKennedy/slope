package ca.unb.mobiledev.slope

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.slider.Slider


class SettingsDialog : androidx.fragment.app.DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val volSave: String = getString(R.string.saved_volume_value_key)

        val view = inflater.inflate(R.layout.settings_menu, container, false)
        val volSlider = view.findViewById<Slider>(R.id.volBar)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnMute = view.findViewById<Button>(R.id.btnMute)
        val btnClose = view.findViewById<Button>(R.id.btnClose)

        val sharedPref = requireActivity().getSharedPreferences(getString(R.string.PREF_FILE_NAME), Context.MODE_PRIVATE)
        var editor = sharedPref.edit()

        // Load settings (?)
        volSlider.value = sharedPref.getFloat(volSave, 1F)

        btnSave.setOnClickListener {
            editor.putFloat(volSave, volSlider.value)
            editor.commit()

            Log.i("Save:", "volume value is: " + volSlider.value)
            Log.i("Storage:", "Stored value is: " + sharedPref.getFloat(volSave, 1F))
        }
        btnMute.setOnClickListener {
            volSlider.value = 0F;

            Log.i("Mute:", "volume value is: " + volSlider.value)
            Log.i("Storage:", "Stored value is: " + sharedPref.getFloat(volSave, 1F))
        }
        btnClose.setOnClickListener {
            //activityHandle.unPause()
            this.dismiss()
        }

        return view
    }

}
