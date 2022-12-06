package ca.unb.mobiledev.slope

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


class GameOverMenuDialog(actHandle: GameActivity, val distance:Int) : androidx.fragment.app.DialogFragment() {

    private val activityHandle = actHandle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.gameover_menu, container, false)

        val btnTryAgain = view.findViewById<Button>(R.id.btnTryAgain)
        val btnQuit = view.findViewById<Button>(R.id.btnQuit)

        val textScore = view.findViewById<TextView>(R.id.score_text)
        val lastScore = view.findViewById<TextView>(R.id.last_score_text)
        val highScore = view.findViewById<TextView>(R.id.best_score_text)
        val newHighScore = view.findViewById<TextView>(R.id.new_high_score_text)

        btnTryAgain.setOnClickListener {
            activityHandle.startGame()
            activityHandle.unPause()
            this.dismiss()
        }
        btnQuit.setOnClickListener {
            activityHandle.close()
            this.dismiss()
        }

        val sharedPref = requireActivity().getSharedPreferences(getString(R.string.PREF_FILE_NAME), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val highestDist = sharedPref.getInt("Highest_dist",0)
        val lastDist = sharedPref.getInt("Last_dist",0)

        if(distance > highestDist){
            editor.putInt("Highest_dist",distance)
        }
        else{
            newHighScore.visibility = View.GONE
        }
        editor.putInt("Last_dist",distance)
        editor.commit()


        textScore.text = "Your Score Was:  " + distance.toString() + "m"
        lastScore.text = "Your Last Score: " + lastDist.toString() + "m"
        highScore.text = "Your Best Score: " + highestDist.toString() + "m"

        return view
    }
}
