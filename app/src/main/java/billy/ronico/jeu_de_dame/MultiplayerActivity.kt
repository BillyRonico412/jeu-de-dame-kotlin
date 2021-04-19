package billy.ronico.jeu_de_dame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import billy.ronico.jeu_de_dame.controlers.Partie
import billy.ronico.jeu_de_dame.enums.CouleurCase
import billy.ronico.jeu_de_dame.enums.EtatCase
import billy.ronico.jeu_de_dame.models.Coordonne
import billy.ronico.jeu_de_dame.views.Case
import billy.ronico.jeu_de_dame.views.Damier

class MultiplayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer)

        val layoutDamier = findViewById<LinearLayout>(R.id.layout_damier)

        val partie: Partie = Partie(8, 12, this)

        layoutDamier.addView(partie.getDamier())

    }
}