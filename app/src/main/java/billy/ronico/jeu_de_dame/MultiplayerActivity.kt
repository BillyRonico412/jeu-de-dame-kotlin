package billy.ronico.jeu_de_dame

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import billy.ronico.jeu_de_dame.controlers.Partie
import billy.ronico.jeu_de_dame.models.Setting
import billy.ronico.jeu_de_dame.views.Damier
import com.beust.klaxon.Klaxon
import java.io.File
import java.io.InputStream


class MultiplayerActivity : AppCompatActivity() {

    lateinit var partie: Partie

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer)

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

        partie = Partie(nbrePiece, taille)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        val damier = Damier(
            this,
            partie.taille,
            partie.etatJeu,
            (width - 50) / partie.taille,
            allCouleurCaseJouable[setting.colorCase],
            allCouleurCaseNonJouable[setting.colorCase]
        )

        partie.initDamier(damier)

        val layoutDamier = findViewById<LinearLayout>(R.id.layout_damier_multijoueur)

        layoutDamier.addView(damier)

        partie.initFunAfficheDialog {
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
                val fragmentResetDialog = ConfirmResetDialog(partie)
                fragmentResetDialog.show(supportFragmentManager, "resetDialog")
                true
            }
            R.id.btn_rewind -> {
                partie.precedent()
                true
            }
            else -> false
        }
    }

}