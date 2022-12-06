package ca.unb.mobiledev.slope

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// Help dialogue. Opens when help button is pressed
class HelpDialog: androidx.fragment.app.DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.help_menu, container, false)
        val btnClose = view.findViewById<Button>(R.id.btnClose)

        // Listener for close button
        btnClose.setOnClickListener {
            this.dismiss()
        }

        return view
    }
}