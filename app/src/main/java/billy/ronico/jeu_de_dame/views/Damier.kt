package billy.ronico.jeu_de_dame.views

import android.content.Context
import android.widget.LinearLayout
import billy.ronico.jeu_de_dame.controlers.EtatJeu
import billy.ronico.jeu_de_dame.enums.CouleurCase
import billy.ronico.jeu_de_dame.enums.EtatCase
import billy.ronico.jeu_de_dame.models.Coordonne

// Un damier est un LinearLayout contenant d'autres LineareLayout qui va constituer une ligne.
// Chacune des lignes (LinearLayout) contient des Cases.

class Damier(
    context: Context,
    val taille: Int,
    var etatJeu: EtatJeu,
    val dimensionCase: Int,
    val couleurCaseJouable: Int,
    val couleurCaseNonJouable: Int
) : LinearLayout(context) {

    // Tableau de tableau de case
    private val tabCase: Array<Array<Case>> = Array(taille) { ligne ->
        Array(taille) { col ->
            if (ligne % 2 == col % 2) Case(
                context,
                CouleurCase.COULEURCASE2,
                EtatCase.VIDE,
                dimensionCase,
                couleurCaseJouable,
                couleurCaseNonJouable
            )
            else Case(
                context,
                CouleurCase.COULEURCASE1,
                EtatCase.VIDE,
                dimensionCase,
                couleurCaseJouable,
                couleurCaseNonJouable
            )
        }
    }

    init {
        // Appel de la fonction déssiné et initEvent à la création du damier
        orientation = VERTICAL
        dessineDamier()
        initEvent()
    }

    // Permet de dessiner le damier, à appeler qu'une seul fois
    private fun dessineDamier() {
        for (ligne in 0 until taille) {
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            for (col in 0 until taille)
                linearLayout.addView(tabCase[ligne][col])
            addView(linearLayout)
        }
    }

    fun updateEtatJeu(_etatJeu: EtatJeu) {
        etatJeu = _etatJeu
        initCouleur()
        liaisonDamierEtatJeu()
    }

    // Permet de mettre à jour le damier
    fun liaisonDamierEtatJeu() {
        for (ligne in 0 until taille)
            for (col in 0 until taille)
                changeEtatCase(Coordonne(ligne, col), etatJeu.tabCase[ligne][col])
    }

    private fun getCase(coordonne: Coordonne) = tabCase[coordonne.x][coordonne.y]

    // Permet de changer l'état d'une case à partir d'une coordonnée et de mettre à jour la vue
    fun changeEtatCase(coordonne: Coordonne, etatCase: EtatCase) {
        tabCase[coordonne.x][coordonne.y].updateEtat(etatCase)
    }

    // Permet de changer la couleur d'une case à partir d'une coordonnée et de mettre à jour la vue
    fun changeCouleurCase(coordonne: Coordonne, couleurCase: CouleurCase) {
        tabCase[coordonne.x][coordonne.y].updateCouleur(couleurCase)
    }

    // Permet de definir un evenement sur une case à partir d'une coordonné (écrase l'ancien evenement)
    fun addEventCase(coordonne: Coordonne, callBack: () -> Unit) {
        tabCase[coordonne.x][coordonne.y].addEvent(callBack)
    }

    // Permet de redefinir pour chaque case l'evenement de base
    // La case devient rouge un moment
    fun initEvent() {
        for (ligne in 0 until taille)
            for (col in 0 until taille)
                if (ligne % 2 != col % 2)
                    tabCase[ligne][col].addEvent {
                        changeCouleurCase(Coordonne(ligne, col), CouleurCase.ROUGE)
                        android.os.Handler().postDelayed({
                            changeCouleurCase(Coordonne(ligne, col), CouleurCase.COULEURCASE1)
                        }, 500)
                    }
    }

    fun initEvent(coordonne: Coordonne) {
        tabCase[coordonne.x][coordonne.y].addEvent {
            changeCouleurCase(coordonne, CouleurCase.ROUGE)
            android.os.Handler().postDelayed({
                changeCouleurCase(coordonne, CouleurCase.COULEURCASE1)
            }, 500)
        }
    }


    fun initCouleur(couleur: CouleurCase) {
        for (ligne in 0 until taille)
            for (col in 0 until taille)
                if (getCase(Coordonne(ligne, col)).couleurCase == couleur)
                    getCase(Coordonne(ligne, col)).updateCouleur(CouleurCase.COULEURCASE1)
    }

    fun initCouleur() {
        for (ligne in 0 until taille)
            for (col in 0 until taille)
                if (ligne % 2 !== col % 2)
                    getCase(Coordonne(ligne, col)).updateCouleur(CouleurCase.COULEURCASE1)


    }

}