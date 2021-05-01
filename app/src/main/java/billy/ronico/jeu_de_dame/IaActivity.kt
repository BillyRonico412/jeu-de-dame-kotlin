package billy.ronico.jeu_de_dame

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import billy.ronico.jeu_de_dame.controlers.Partie
import billy.ronico.jeu_de_dame.controlers.PartieAvecIA
import billy.ronico.jeu_de_dame.models.Setting
import billy.ronico.jeu_de_dame.views.Damier

class IaActivity : AppCompatActivity() {

    lateinit var partieAvecIa: PartieAvecIA

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ia)

        val setting = intent.getParcelableExtra<Setting>("setting")!!

        val taille = setting.tailleDamier

        val nbrePiece = setting.nbrePiece

        val allCouleurCaseJouable: MutableList<Int> = mutableListOf(
            ContextCompat.getColor(this, R.color.color_case_jouer_1),
            ContextCompat.getColor(this, R.color.color_case_jouer_2),
            ContextCompat.getColor(this, R.color.color_case_jouer_3)
        )

        val allCouleurCaseNonJouable: MutableList<Int> = mutableListOf(
            ContextCompat.getColor(this, R.color.color_case_non_jouer_1),
            ContextCompat.getColor(this, R.color.color_case_non_jouer_2),
            ContextCompat.getColor(this, R.color.color_case_non_jouer_3)
        )

        partieAvecIa = PartieAvecIA(nbrePiece, taille, setting.profondeur)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        val damier = Damier(
            this,
            partieAvecIa.taille,
            partieAvecIa.etatJeu,
            (width - 50) / partieAvecIa.taille,
            allCouleurCaseJouable[setting.colorCase],
            allCouleurCaseNonJouable[setting.colorCase]
        )

        partieAvecIa.initDamier(damier)

        val layoutDamier = findViewById<LinearLayout>(R.id.layout_damier_ia)

        layoutDamier.addView(damier)

        partieAvecIa.initFunAfficheDialog {
            val fragmentDialog = FinPartieDialog(it, this)
            fragmentDialog.show(supportFragmentManager, "victoireDialog")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_reset -> {
                val fragmentResetDialog = ConfirmResetDialog(partieAvecIa)
                fragmentResetDialog.show(supportFragmentManager, "resetDialog")
                true
            }
            R.id.btn_rewind -> {
                partieAvecIa.precedent()
                true
            }
            else -> false
        }
    }
}