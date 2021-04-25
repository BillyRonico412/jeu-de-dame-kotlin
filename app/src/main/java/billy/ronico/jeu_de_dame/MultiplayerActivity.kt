package billy.ronico.jeu_de_dame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toolbar
import billy.ronico.jeu_de_dame.controlers.Partie
import billy.ronico.jeu_de_dame.enums.CouleurCase
import billy.ronico.jeu_de_dame.enums.EtatCase
import billy.ronico.jeu_de_dame.models.Coordonne
import billy.ronico.jeu_de_dame.views.Case
import billy.ronico.jeu_de_dame.views.Damier

class MultiplayerActivity : AppCompatActivity() {

    val partie = Partie(8, 12, ) {
        val fragmentDialog = FinPartieDialog(it, this)
        fragmentDialog.show(supportFragmentManager, "dialogFinPartie")
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer)

        partie.initDamier(this)

        partie.partie()

        val layoutDamier = findViewById<LinearLayout>(R.id.layout_damier)

        layoutDamier.addView(partie.getDamier())

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_reset -> {
                partie.restartGame()
                true
            }
            else -> false
        }
    }

}