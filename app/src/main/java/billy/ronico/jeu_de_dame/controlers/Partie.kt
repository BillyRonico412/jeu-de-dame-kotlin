package billy.ronico.jeu_de_dame.controlers

import android.content.Context
import billy.ronico.jeu_de_dame.FinPartieDialog
import billy.ronico.jeu_de_dame.enums.CouleurCase
import billy.ronico.jeu_de_dame.enums.Tour
import billy.ronico.jeu_de_dame.ia.IA
import billy.ronico.jeu_de_dame.models.Coordonne
import billy.ronico.jeu_de_dame.models.Evolution
import billy.ronico.jeu_de_dame.models.InfoManger
import billy.ronico.jeu_de_dame.views.Damier

open class Partie(
    val nbrePiece: Int,
    val taille: Int,
    var etatJeu: EtatJeu = EtatJeu(nbrePiece, taille, Tour.BLANC)
) {

    val allEtatJeu: MutableList<EtatJeu> = mutableListOf()

    // Les cases ou ont a assigné un event de mouvement ou de mangement
    // Cela nous permettra de remettre à jour les evenements
    var casesAvecEvent: MutableList<Coordonne> = mutableListOf()

    lateinit var damier: Damier

    lateinit var funAfficheDialog: (Boolean) -> Unit

    fun initDamier(_damier: Damier) {
        damier = _damier
        damier.updateEtatJeu(etatJeu)
        partie()
    }

    fun initFunAfficheDialog(_funAfficheDialog: (Boolean) -> Unit) {
        funAfficheDialog = _funAfficheDialog
    }

    fun updateJeu() {
        damier.updateEtatJeu(etatJeu)
    }

    fun restart() {
        allEtatJeu.clear()
        etatJeu = EtatJeu(nbrePiece, taille, Tour.BLANC)
        partie()
    }

    fun precedent() {
        if (allEtatJeu.size > 0) {
            etatJeu = allEtatJeu.last()
            allEtatJeu.removeLast()
            partie()
        }
    }

    open fun partie() {

        updateJeu()

        damier.initEvent()

        val evolutionPossible: Evolution = etatJeu.evolutionPossible()

        if (
            evolutionPossible.evolutionManger.size !== 0 ||
            evolutionPossible.evolutionBouger.size !== 0
        )
            if (evolutionPossible.evolutionManger.size === 0) {

                evolutionPossible.evolutionBouger.forEach { caseBougeable ->

                    damier.changeCouleurCase(caseBougeable.coordonne, CouleurCase.ORANGE)

                    damier.addEventCase(caseBougeable.coordonne) {

                        casesAvecEvent.forEach { damier.initEvent(it) }

                        damier.initCouleur(CouleurCase.VERT)

                        evolutionPossible.evolutionBouger.forEach {
                            if (!(it.coordonne.compare(caseBougeable.coordonne)))
                                damier.changeCouleurCase(it.coordonne, CouleurCase.ORANGE)
                            else damier.changeCouleurCase(it.coordonne, CouleurCase.VERT)
                        }

                        casesAvecEvent = caseBougeable.coordonneApres

                        caseBougeable.coordonneApres.forEach {
                            damier.changeCouleurCase(it, CouleurCase.VERT)
                            damier.addEventCase(it) {
                                allEtatJeu.add(etatJeu)
                                etatJeu = etatJeu.bouger(caseBougeable, it)
                                partie()

                            }
                        }
                    }
                }

            } else {

                val coordonneCourantDejaTraiter = mutableListOf<Coordonne>()

                evolutionPossible.evolutionManger.forEach { allCheminManger ->

                    val coordonneCourant: Coordonne = allCheminManger[0][0].coordonneCourant

                    if (!coordonneCourantDejaTraiter.any { it.compare(coordonneCourant) }) {

                        val allCheminAvecMemeCoordonneCourant: MutableList<MutableList<MutableList<InfoManger>>> =
                            evolutionPossible.evolutionManger.filter {
                                it[0][0].coordonneCourant === coordonneCourant
                            } as MutableList<MutableList<MutableList<InfoManger>>>

                        damier.changeCouleurCase(coordonneCourant, CouleurCase.ORANGE)

                        damier.addEventCase(coordonneCourant) {

                            casesAvecEvent.forEach { damier.initEvent(it) }
                            casesAvecEvent = mutableListOf()

                            damier.initCouleur(CouleurCase.VERT)

                            evolutionPossible.evolutionManger.forEach { allCheminManger2 ->
                                if (!(allCheminManger2[0][0].coordonneCourant.compare(coordonneCourant)))
                                    damier.changeCouleurCase(
                                        allCheminManger2[0][0].coordonneCourant,
                                        CouleurCase.ORANGE
                                    )
                                else damier.changeCouleurCase(
                                    allCheminManger2[0][0].coordonneCourant,
                                    CouleurCase.VERT
                                )
                            }

                            allCheminAvecMemeCoordonneCourant.forEach { allCheminMangerUnique ->

                                allCheminMangerUnique.forEach { chemin ->

                                    chemin.forEachIndexed { indexInfoManger, infoManger ->

                                        damier.changeCouleurCase(
                                            infoManger.coordonneManger,
                                            CouleurCase.VERT
                                        )
                                        damier.addEventCase(infoManger.coordonneManger) {}
                                        casesAvecEvent.add(infoManger.coordonneManger)

                                        etatJeu.caseEntre(
                                            infoManger.coordonneManger,
                                            infoManger.coordonneCourant
                                        ).forEach {
                                            damier.changeCouleurCase(it, CouleurCase.VERT)
                                            damier.addEventCase(it) {}
                                            casesAvecEvent.add(it)
                                        }

                                        if (indexInfoManger !== 0) {

                                            damier.changeCouleurCase(
                                                infoManger.coordonneCourant,
                                                CouleurCase.VERT
                                            )
                                            damier.addEventCase(infoManger.coordonneCourant) {}

                                            etatJeu.caseEntre(
                                                chemin[indexInfoManger - 1].coordonneManger,
                                                infoManger.coordonneCourant
                                            ).forEach {
                                                damier.changeCouleurCase(it, CouleurCase.VERT)
                                                damier.addEventCase(it) {}
                                                casesAvecEvent.add(it)
                                            }

                                            casesAvecEvent.add(infoManger.coordonneCourant)
                                        }

                                        if (indexInfoManger === chemin.size - 1)
                                            infoManger.coordonneApres.forEach {
                                                damier.changeCouleurCase(it, CouleurCase.VERT)
                                                damier.addEventCase(it) {
                                                    allEtatJeu.add(etatJeu)
                                                    etatJeu = etatJeu.manger(chemin, it)
                                                    partie()
                                                }
                                            }
                                    }

                                }

                            }

                        }

                    }

                    coordonneCourantDejaTraiter.add(coordonneCourant)

                }

            }

        else {
            funAfficheDialog(etatJeu.tour === Tour.NOIR)
            restart()
            partie()
        }

    }

}