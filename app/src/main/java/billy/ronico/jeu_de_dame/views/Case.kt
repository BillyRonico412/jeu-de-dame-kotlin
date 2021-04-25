package billy.ronico.jeu_de_dame.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import billy.ronico.jeu_de_dame.R
import billy.ronico.jeu_de_dame.enums.CouleurCase
import billy.ronico.jeu_de_dame.enums.EtatCase

// Une case est un ImageView qui peut être coloré, contenir ou non une piece (image)
// Et peut avoir un comportement particulier au click

@SuppressLint("ResourceType")
class Case(
        context: Context, var couleurCase: CouleurCase,
        var etatCase: EtatCase
) : androidx.appcompat.widget.AppCompatImageView(context) {

    // Initialisation des couleurs et de la dimension d'une case
    val couleurDamier1: Int = ContextCompat.getColor(context, R.color.color_damier_1)
    val couleurDamier2: Int = ContextCompat.getColor(context, R.color.color_damier_2)
    val couleurVert: Int = ContextCompat.getColor(context, R.color.color_vert)
    val couleurRouge: Int = ContextCompat.getColor(context, R.color.color_rouge)
    val couleurOrange: Int = ContextCompat.getColor(context, R.color.color_orange)
    val dimensionCase: Int = resources.getDimension(R.dimen.taille_case).toInt()

    // Permet de definir les bordures
    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)

        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6F
        paint.color = Color.BLACK

        canvas?.drawRect(Rect(0, 0, dimensionCase, dimensionCase), paint)

    }

    init {
        updateCouleur(couleurCase)
        updateEtat(etatCase)
        layoutParams = ViewGroup.LayoutParams(dimensionCase, dimensionCase)
        setPadding(10)
    }

    // Permet de mettre à jour la couleur d'une case ( dans la vue egalement )
    fun updateCouleur(_couleurCase: CouleurCase) {
        couleurCase = _couleurCase
        when (couleurCase) {
            CouleurCase.COULEURCASE1 -> setBackgroundColor(couleurDamier1)
            CouleurCase.COULEURCASE2 -> setBackgroundColor(couleurDamier2)
            CouleurCase.ROUGE -> setBackgroundColor(couleurRouge)
            CouleurCase.VERT -> setBackgroundColor(couleurVert)
            CouleurCase.ORANGE -> setBackgroundColor(couleurOrange)
        }
    }

    // Permet de mettre à jour les pieces d'une case ( dans la vue egalement )
    fun updateEtat(_etatCase: EtatCase) {
        etatCase = _etatCase
        when (etatCase) {
            EtatCase.VIDE -> setImageResource(0)
            EtatCase.BLANC -> setImageResource(R.drawable.pion_blanc)
            EtatCase.NOIR -> setImageResource(R.drawable.pion_noir)
            EtatCase.BLANC_DAME -> setImageResource(R.drawable.dame_blanc)
            EtatCase.NOIR_DAME -> setImageResource(R.drawable.dame_noir)
        }
    }

    // Permet de definir une fonction à éxécuté au click de la case.
    fun addEvent(callBackEvent: () -> Unit) {
        setOnClickListener {
            callBackEvent()
        }
    }

}