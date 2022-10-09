package ca.unb.mobiledev.slope

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment


class PauseMenuDialog(actHandle: CloseHandle) : androidx.fragment.app.DialogFragment() {
    private val activityHandle = actHandle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.pause_menu, container, false)
        val btnSettings = view.findViewById<Button>(R.id.btnSettings)
        val btnHelp = view.findViewById<Button>(R.id.btnHelp)
        val btnQuit = view.findViewById<Button>(R.id.btnQuit)
        val btnClose = view.findViewById<Button>(R.id.btnClose)

        btnSettings.setOnClickListener {

        }
        btnHelp.setOnClickListener {

        }
        btnQuit.setOnClickListener {
            activityHandle.close()
        }
        btnClose.setOnClickListener {
            activityHandle.unPause()
            this.dismiss()
        }

        return view
    }
}
