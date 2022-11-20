package ca.unb.mobiledev.slope

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.w3c.dom.Text


class GameOverMenuDialog(actHandle: CloseHandle, val distance:Int) : androidx.fragment.app.DialogFragment() {

    private val activityHandle = actHandle

   // private lateinit var textScore: TextView

    //var distance:Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.gameover_menu, container, false)

        val btnTryAgain = view.findViewById<Button>(R.id.btnTryAgain)
        val btnQuit = view.findViewById<Button>(R.id.btnQuit)
        val textScore = view.findViewById<TextView>(R.id.score_text)

        btnTryAgain.setOnClickListener {
            //TODO: Need to do something to reset the game here
            activityHandle.unPause()
            this.dismiss()
        }
        btnQuit.setOnClickListener {
            activityHandle.close()
            this.dismiss()
        }
        textScore.text = "Your Score Was: " + distance.toString() + "m"
        

        return view
    }

   /* fun setScoreText(distance:Int){
        textScore.text = "Hi"//"@string/your_score_was" + distance.toString() + "m"
    }*/

}
