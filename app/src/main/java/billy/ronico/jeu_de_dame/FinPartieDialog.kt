package billy.ronico.jeu_de_dame

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

class FinPartieDialog(val boolVictoire: Boolean, val ctx: AppCompatActivity): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val layoutDialog = activity?.layoutInflater?.inflate(R.layout.fin_partie_dialog_layout, null)

        val textView = layoutDialog?.findViewById<TextView>(R.id.text_vainqueur_dialog)

        textView?.text = if (boolVictoire)
            resources.getString(R.string.joueur_blanc)
        else resources.getString(R.string.joueur_noir)

        builder.setView(layoutDialog)
            .setPositiveButton(resources.getText(R.string.btn_rejouer), null)
            .setNegativeButton(resources.getText(R.string.btn_quitter)) { _, _ ->
                val intent = Intent(ctx, MainActivity::class.java)
                startActivity(intent)
            }

        return builder.create()

    }

}