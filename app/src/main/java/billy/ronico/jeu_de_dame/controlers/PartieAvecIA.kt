package billy.ronico.jeu_de_dame.controlers

import android.content.Context
import billy.ronico.jeu_de_dame.FinPartieDialog
import billy.ronico.jeu_de_dame.enums.CouleurCase
import billy.ronico.jeu_de_dame.enums.EtatCase
import billy.ronico.jeu_de_dame.enums.Tour
import billy.ronico.jeu_de_dame.ia.IA
import billy.ronico.jeu_de_dame.models.Coordonne
import billy.ronico.jeu_de_dame.models.Evolution
import billy.ronico.jeu_de_dame.views.Damier

class PartieAvecIA(
    nbrePiece: Int,
    taille: Int,
    val profondeur: Int,
    val tourIa: Tour = Tour.NOIR
) : Partie(nbrePiece, taille) {

    override fun partie() {

        updateJeu()
        damier.initEvent()


        val evolutionPossible: Evolution = etatJeu.evolutionPossible()

        if (
            evolutionPossible.evolutionManger.size !== 0 ||
            evolutionPossible.evolutionBouger.size !== 0
        ) {
            if (tourIa === etatJeu.tour) {

                val resolveEtatJeu = IA.resolve(etatJeu, profondeur)

                var oldCoordonne: Coordonne = Coordonne(0, 0)
                var newCoordonne: Coordonne = Coordonne(0, 0)
                var coordonneManger: MutableList<Coordonne> = mutableListOf()

                for (i in 0 until etatJeu.taille)
                    for (j in 0 until etatJeu.taille) {
                        if (
                            etatJeu.etatsJoueur().contains(etatJeu.tabCase[i][j]) &&
                            resolveEtatJeu.tabCase[i][j] === EtatCase.VIDE
                        )
                            oldCoordonne = Coordonne(i, j)

                        if (
                            etatJeu.etatsJoueur().contains(resolveEtatJeu.tabCase[i][j]) &&
                            etatJeu.tabCase[i][j] === EtatCase.VIDE
                        )
                            newCoordonne = Coordonne(i, j)

                        if (
                            resolveEtatJeu.tabCase[i][j] === EtatCase.VIDE &&
                            etatJeu.etatsNonJoueur().contains(
                                etatJeu.tabCase[i][j]
                            )
                        )
                            coordonneManger.add(Coordonne(i, j))
                    }

                android.os.Handler().postDelayed ({
                    damier.changeCouleurCase(oldCoordonne, CouleurCase.VERT)

                    coordonneManger.forEach {
                        damier.changeCouleurCase(it, CouleurCase.ROUGE)
                    }

                    android.os.Handler().postDelayed({

                        damier.changeCouleurCase(oldCoordonne, CouleurCase.COULEURCASE1)

                        coordonneManger.forEach {
                            damier.changeCouleurCase(it, CouleurCase.COULEURCASE1)
                        }

                        damier.changeCouleurCase(newCoordonne, CouleurCase.VERT)

                        android.os.Handler().postDelayed({
                            damier.changeCouleurCase(newCoordonne, CouleurCase.COULEURCASE1)
                            etatJeu = resolveEtatJeu
                            partie()
                        }, 500)
                    }, 500)
                }, 500)


            } else {

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

                    evolutionPossible.evolutionManger.forEach { allCheminManger ->

                        val coordonneCourant: Coordonne = allCheminManger[0][0].coordonneCourant

                        damier.changeCouleurCase(coordonneCourant, CouleurCase.ORANGE)

                        damier.addEventCase(coordonneCourant) {

                            casesAvecEvent.forEach { damier.initEvent(it) }
                            casesAvecEvent = mutableListOf()

                            damier.initCouleur(CouleurCase.VERT)

                            evolutionPossible.evolutionManger.forEach { allCheminManger2 ->
                                if (!(allCheminManger2[0][0].coordonneCourant.compare(
                                        coordonneCourant
                                    ))
                                )
                                    damier.changeCouleurCase(
                                        allCheminManger2[0][0].coordonneCourant,
                                        CouleurCase.ORANGE
                                    )
                                else damier.changeCouleurCase(
                                    allCheminManger2[0][0].coordonneCourant,
                                    CouleurCase.VERT
                                )
                            }

                            allCheminManger.forEach { chemin ->

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

            }
        } else {
            funAfficheDialog(etatJeu.tour === Tour.NOIR)
            restart()
            partie()
        }

    }

}