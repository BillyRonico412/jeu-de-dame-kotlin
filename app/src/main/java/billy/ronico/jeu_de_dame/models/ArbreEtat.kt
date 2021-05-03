package billy.ronico.jeu_de_dame.models

import billy.ronico.jeu_de_dame.enums.Tour

interface ArbreEtat {

    fun min(tabNumber: MutableList<Int>): Int {

        var result = tabNumber[0]

        tabNumber.forEach {
            if (it < result) result = it
        }

        return result

    }

    fun max(tabNumber: MutableList<Int>): Int {

        var result = tabNumber[0]

        tabNumber.forEach {
            if (it > result) result = it
        }

        return result

    }

    fun minMax(tourIA: Tour): Int {

        return when(this) {

            is ArbreVideEtat -> 0
            is NoeudArbreEtat ->

                if ( fils.all { it is ArbreVideEtat } ) infoArbreEtat.poids
                else {

                    if (infoArbreEtat.etatJeu.tour === tourIA)
                        max(fils.map { it.minMax(tourIA) } as MutableList<Int>)
                    else min(fils.map { it.minMax(tourIA) } as MutableList<Int>)

                }
            else -> 0
        }

    }
}

object ArbreVideEtat : ArbreEtat {
    override fun toString(): String {
        return "Arbre Vide"
    }
}

data class NoeudArbreEtat(
    val infoArbreEtat: InfoArbreEtat,
    val fils: MutableList<ArbreEtat>
): ArbreEtat