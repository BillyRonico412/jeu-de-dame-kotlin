package billy.ronico.jeu_de_dame.ia

import billy.ronico.jeu_de_dame.controlers.EtatJeu
import billy.ronico.jeu_de_dame.enums.Tour
import billy.ronico.jeu_de_dame.models.ArbreEtat
import billy.ronico.jeu_de_dame.models.ArbreVideEtat
import billy.ronico.jeu_de_dame.models.NoeudArbreEtat
import billy.ronico.jeu_de_dame.models.InfoArbreEtat

object IA {

    fun getArbreEtat(
        etatJeu: EtatJeu,
        profondeur: Int,
        poids: Int = 0,
        tourIa: Tour = etatJeu.tour
    ): ArbreEtat {

        return when {
            profondeur === 0 -> ArbreVideEtat
            etatJeu.evolution().isEmpty() -> NoeudArbreEtat(
                infoArbreEtat = InfoArbreEtat(etatJeu, poids), fils = mutableListOf(ArbreVideEtat)
            )
            else -> NoeudArbreEtat(
                infoArbreEtat = InfoArbreEtat(etatJeu, poids),
                fils = etatJeu.evolution().map {
                    getArbreEtat(
                        it.etatJeu,
                        profondeur - 1,
                        poids + if (tourIa === it.etatJeu.tour) -1 * it.poids else it.poids
                    )
                } as MutableList<ArbreEtat>
            )
        }

    }

    // Fonction permettant de construire l'arbre d'état
    fun getBranchesEtat(
        etatJeu: EtatJeu,
        profondeur: Int,
        tourIa: Tour
    ): MutableList<MutableList<InfoArbreEtat>> {

        var result = mutableListOf<MutableList<InfoArbreEtat>>()

        result.add(mutableListOf(InfoArbreEtat(etatJeu, 0)))

        for (i in 1..profondeur) {

            val newResult = mutableListOf<MutableList<InfoArbreEtat>>()

            result.forEach { branche ->

                branche.last().etatJeu.evolution().forEach {

                    val newNoeudArbreEtat = InfoArbreEtat(
                        etatJeu = it.etatJeu,
                        poids = branche.last().poids + if (it.etatJeu.tour === tourIa) -1 * it.poids else it.poids
                    )
                    newResult.add((branche + newNoeudArbreEtat) as MutableList<InfoArbreEtat>)

                }

            }

            result = newResult

        }

        return result

    }

    fun brancheMax(arbre: MutableList<MutableList<InfoArbreEtat>>): MutableList<InfoArbreEtat> {
        var brancheMax: MutableList<InfoArbreEtat> = mutableListOf()
        var maxPoids = 0
        arbre.forEach {
            if (it.last().poids > maxPoids) {
                brancheMax = it
                maxPoids = it.last().poids
            }
        }
        return brancheMax
    }

    fun resolve(etatJeu: EtatJeu, profondeur: Int): EtatJeu {

        val evolution = etatJeu.evolution()

        val minMaxAllEvolution = evolution.map {
            val arbre = getArbreEtat(it.etatJeu, profondeur, it.poids, etatJeu.tour)
            arbre.minMax(etatJeu.tour)
        }


        var valMax = minMaxAllEvolution[0]

        minMaxAllEvolution.forEach{
            if (valMax < it) valMax = it
        }

        val listIndexMax = mutableListOf<Int>()

        minMaxAllEvolution.forEachIndexed { index, value ->
            if (value == valMax) listIndexMax.add(index)
        }

        return evolution[listIndexMax[(Math.random() * listIndexMax.size).toInt()]].etatJeu

    }

}