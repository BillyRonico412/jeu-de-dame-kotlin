package billy.ronico.jeu_de_dame

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import billy.ronico.jeu_de_dame.controlers.Partie

class ConfirmResetDialog(val partie: Partie) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        builder.setMessage(resources.getText(R.string.question_rejouer))
            .setPositiveButton(resources.getText(R.string.text_oui)) { _, _ ->
                partie.restart()
            }
            .setNegativeButton(resources.getText(R.string.text_non), null)

        return builder.create()

    }

}