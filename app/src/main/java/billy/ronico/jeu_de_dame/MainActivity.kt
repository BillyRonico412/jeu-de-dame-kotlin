package billy.ronico.jeu_de_dame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Recuperation des boutons
        val btn_multijoueur = findViewById<Button>(R.id.btn_multijoueur)
        val btn_joueur_vs_ia = findViewById<Button>(R.id.btn_joueur_vs_ia)

        // Liaison avec les 2 activitys

        val self = this

        btn_multijoueur.setOnClickListener {
            val intent = Intent(self, MultiplayerActivity::class.java)
            startActivity(intent)
        }

    }
}