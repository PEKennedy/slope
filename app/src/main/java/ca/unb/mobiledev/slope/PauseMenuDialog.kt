package ca.unb.mobiledev.slope

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class PauseMenuDialog(actHandle: CloseHandle) : androidx.fragment.app.DialogFragment() {
    private val activityHandle = actHandle
    private val settingsMenu = SettingsDialog();
    private val helpMenu = HelpDialog()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.pause_menu, container, false)
        val btnSettings = view.findViewById<Button>(R.id.btnSettings)
        val btnHelp = view.findViewById<Button>(R.id.btnHelp)
        val btnQuit = view.findViewById<Button>(R.id.btnQuit)
        val btnClose = view.findViewById<Button>(R.id.btnClose)

        btnSettings.setOnClickListener {
//            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val nextFrag = settingsMenu

            nextFrag.show(parentFragmentManager,"SettingsFragment")

           /* requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack("findThisFragment")
                //.replace(((view as ViewGroup).parent as View).id, nextFrag, "findThisFragment")
                //.addToBackStack("pause_menu")
                .commitNow()*/
            //this.dismiss()


//            fragmentManager.beginTransaction()
//                .replace(((view as ViewGroup).parent as View).id, nextFrag, "findThisFragment")
//                .addToBackStack(null)
//                .commit()

//            val fragmentManager = supportFragmentManager//supportFragmentManager
//            settingsMenu.show(fragmentManager,"settings")
        }
        btnHelp.setOnClickListener {
            val nextFrag = helpMenu
            nextFrag.show(parentFragmentManager,"helpFragment")
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

    override fun onCancel(dialog: DialogInterface) {
        activityHandle.unPause()
        super.onCancel(dialog)
    }
}
