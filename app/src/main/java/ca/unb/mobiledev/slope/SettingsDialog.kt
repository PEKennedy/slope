package ca.unb.mobiledev.slope

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.slider.Slider

// Settings dialogue for the game. Controls music volume
class SettingsDialog : androidx.fragment.app.DialogFragment() {

    // Default method called when dialogue is created. Returns the view
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

        // Listener for save button
        btnSave.setOnClickListener {
            editor.putFloat(volSave, volSlider.value)
            editor.commit()
        }

        // Listener for mute button
        btnMute.setOnClickListener {
            volSlider.value = 0F;
        }

        // Listener for close/resume button
        btnClose.setOnClickListener {
            this.dismiss()
        }

        return view
    }
}
