package billy.ronico.jeu_de_dame.models

data class Noeud(val info: InfoManger, val fils: MutableList<ArbreManger>) : ArbreManger

object ArbreVide : ArbreManger

interface ArbreManger {

    companion object {
        fun max(listInt: List<Int>): Int {
            var result = 0
            listInt.forEach {
                if (result < it) result = it
            }
            return result
        }
    }

    fun hauteur(): Int = when (this) {
        is ArbreVide -> 0
        is Noeud -> 1 + max(this.fils.map { it.hauteur() })
        else -> 0
    }

    fun aplatirArbre(): MutableList<MutableList<InfoManger>> {

        return when(this) {

            is ArbreVide -> mutableListOf(mutableListOf())

            is Noeud -> {
                val result = mutableListOf<MutableList<InfoManger>>()
                fils.forEach { arbreMangerEnfant ->
                    arbreMangerEnfant.aplatirArbre().forEach {
                        result.add((mutableListOf(info) + it) as MutableList<InfoManger>)
                    }
                }
                result
            }

            else -> mutableListOf(mutableListOf())

        }

    }

}