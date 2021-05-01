package billy.ronico.jeu_de_dame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import billy.ronico.jeu_de_dame.models.Setting
import billy.ronico.jeu_de_dame.utils.persisteSetting

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val setting = intent.getParcelableExtra<Setting>("setting")!!

        val btn_valide_setting = findViewById<Button>(R.id.btn_valide_setting)
        val radio_group_couleur_damier = findViewById<RadioGroup>(R.id.radio_group_couleur_damier)
        val radio_group_taille_damier = findViewById<RadioGroup>(R.id.radio_group_taille_damier)
        val radio_group_difficult = findViewById<RadioGroup>(R.id.radio_group_difficult)

        val radio_couleur_damier_1 = findViewById<RadioButton>(R.id.radio_couleur_damier_1)
        val radio_couleur_damier_2 = findViewById<RadioButton>(R.id.radio_couleur_damier_2)
        val radio_couleur_damier_3 = findViewById<RadioButton>(R.id.radio_couleur_damier_3)

        val radio_taille_damier_1 = findViewById<RadioButton>(R.id.radio_taille_damier_1)
        val radio_taille_damier_2 = findViewById<RadioButton>(R.id.radio_taille_damier_2)
        val radio_taille_damier_3 = findViewById<RadioButton>(R.id.radio_taille_damier_3)

        val radio_difficult_1 = findViewById<RadioButton>(R.id.radio_difficult_1)
        val radio_difficult_2 = findViewById<RadioButton>(R.id.radio_difficult_2)
        val radio_difficult_3 = findViewById<RadioButton>(R.id.radio_difficult_3)

        when (setting.colorCase) {
            0 -> radio_couleur_damier_1.isChecked = true
            1 -> radio_couleur_damier_2.isChecked = true
            2 -> radio_couleur_damier_3.isChecked = true
        }

        when (setting.tailleDamier) {
            6 -> radio_taille_damier_1.isChecked = true
            8 -> radio_taille_damier_2.isChecked = true
            10 -> radio_taille_damier_3.isChecked = true
        }

        when (setting.profondeur) {
            2 -> radio_difficult_1.isChecked = true
            3 -> radio_difficult_2.isChecked = true
            4 -> radio_difficult_3.isChecked = true
        }

        btn_valide_setting.setOnClickListener {

            when (radio_group_couleur_damier.checkedRadioButtonId) {
                R.id.radio_couleur_damier_1 -> {
                    setting.colorCase = 0
                }
                R.id.radio_couleur_damier_2 -> {
                    setting.colorCase = 1
                }
                R.id.radio_couleur_damier_3 -> {
                    setting.colorCase = 2
                }
            }

            when (radio_group_taille_damier.checkedRadioButtonId) {
                R.id.radio_taille_damier_1 -> setting.tailleDamier = 6
                R.id.radio_taille_damier_2 -> setting.tailleDamier = 8
                R.id.radio_taille_damier_3 -> setting.tailleDamier = 10
            }

            when (radio_group_difficult.checkedRadioButtonId) {
                R.id.radio_difficult_1 -> setting.profondeur = 2
                R.id.radio_difficult_2 -> setting.profondeur = 3
                R.id.radio_difficult_3 -> setting.profondeur = 4
            }

            persisteSetting(this, setting)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("setting", setting as Parcelable)
            startActivity(intent)

        }

    }

}