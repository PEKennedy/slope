package ca.unb.mobiledev.slope

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class CreditsDialog: androidx.fragment.app.DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.credits_menu, container, false)
        val btnClose = view.findViewById<Button>(R.id.btnClose)
        btnClose.setOnClickListener {
            this.dismiss()
        }

        return view
    }

}