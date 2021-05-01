package billy.ronico.jeu_de_dame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import billy.ronico.jeu_de_dame.models.Setting

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var setting = intent.getParcelableExtra<Setting>("setting")
        if (setting !is Setting)
            setting = Setting(0, 6, 3)

        // Recuperation des boutons
        val btn_multijoueur = findViewById<Button>(R.id.btn_multijoueur)
        val btn_joueur_vs_ia = findViewById<Button>(R.id.btn_joueur_vs_ia)
        val btn_setting = findViewById<Button>(R.id.btn_setting)

        // Liaison avec les 2 activitys

        val self = this

        btn_multijoueur.setOnClickListener {
            val intent = Intent(self, MultiplayerActivity::class.java)
            intent.putExtra("setting", setting)
            startActivity(intent)
        }

        btn_joueur_vs_ia.setOnClickListener {
            val intent = Intent(self, IaActivity::class.java)
            intent.putExtra("setting", setting)
            startActivity(intent)
        }

        btn_setting.setOnClickListener {
            val intent = Intent(self, SettingActivity::class.java)
            intent.putExtra("setting", setting)
            startActivity(intent)
        }

    }
}