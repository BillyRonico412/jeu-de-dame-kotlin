package billy.ronico.jeu_de_dame

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import billy.ronico.jeu_de_dame.controlers.Partie

class FinPartieDialog(val textMessage: String, val ctx: AppCompatActivity): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val layoutDialog = activity?.layoutInflater?.inflate(R.layout.fin_partie_dialog_layout, null)

        val textView = layoutDialog?.findViewById<TextView>(R.id.text_vainqueur_dialog)

        textView?.text = textMessage

        builder.setView(layoutDialog)
            .setPositiveButton(resources.getText(R.string.btn_rejouer), null)
            .setNegativeButton(resources.getText(R.string.btn_quitter)) { dialog, id ->
                val intent = Intent(ctx, MainActivity::class.java)
                startActivity(intent)
            }

        return builder.create()
    }

}