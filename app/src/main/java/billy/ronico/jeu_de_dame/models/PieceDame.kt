package billy.ronico.jeu_de_dame.models

import billy.ronico.jeu_de_dame.enums.CouleurPiece
import billy.ronico.jeu_de_dame.enums.Direction
import billy.ronico.jeu_de_dame.enums.EtatPiece

class PieceDame(
        couleurPiece: CouleurPiece, direction: Direction, coordonne: Coordonne, taille: Int
) : Piece(EtatPiece.values()[couleurPiece.ordinal + 2], direction, coordonne, taille) {

    // On va definir une suite de methodes permettant de definir tout les coordonnes diagonales
    // Partant de la piece vers les extrémités

    companion object {

        fun diagonaleHautGauche(coordonne: Coordonne, taille: Int): MutableList<Coordonne> {
            val result: MutableList<Coordonne> = mutableListOf()
            var count = 1

            while (
                0 <= coordonne.x - count && 0 <= coordonne.y - count
            ) result.add(Coordonne(coordonne.x - count, coordonne.y - count++))

            return result
        }

        fun diagonaleHautDroite(coordonne: Coordonne, taille: Int): MutableList<Coordonne> {
            val result: MutableList<Coordonne> = mutableListOf()
            var count = 1

            while (
                0 <= coordonne.x - count && coordonne.y + count <= taille - 1
            ) result.add(Coordonne(coordonne.x - count, coordonne.y + count++))

            return result
        }

        fun diagonaleBasGauche(coordonne: Coordonne, taille: Int): MutableList<Coordonne> {
            val result: MutableList<Coordonne> = mutableListOf()
            var count = 1

            while (
                coordonne.x + count <= taille - 1 && 0 <= coordonne.y - count
            ) result.add(Coordonne(coordonne.x + count, coordonne.y - count++))

            return result
        }

        fun diagonaleBasDroite(coordonne: Coordonne, taille: Int): MutableList<Coordonne> {
            val result: MutableList<Coordonne> = mutableListOf()
            var count = 1

            while (
                coordonne.x + count <= taille - 1 && coordonne.y + count <= taille - 1
            ) result.add(Coordonne(coordonne.x + count, coordonne.y + count++))

            return result
        }

        enum class DirectionDiagonale {
            HautGauche, HautDroite, BasGauche, BasDroite, Aucune
        }

        fun diagonaleEntre2Coordonne(
            _coordonne1: Coordonne, coordonne2: Coordonne, taille: Int
        ): MutableList<Coordonne> {

            val result: MutableList<Coordonne> = mutableListOf()

            if (_coordonne1.compare(coordonne2)) result

            var coordonne1 = _coordonne1

            val direction: DirectionDiagonale = when {

                (coordonne1.x < coordonne2.x) && (coordonne1.y < coordonne2.y) ->
                    DirectionDiagonale.BasDroite

                (coordonne1.x < coordonne2.x) && (coordonne1.y > coordonne2.y) ->
                    DirectionDiagonale.BasGauche

                (coordonne1.x > coordonne2.x) && (coordonne1.y < coordonne2.y) ->
                    DirectionDiagonale.HautDroite

                (coordonne1.x > coordonne2.x) && (coordonne1.y > coordonne2.y) ->
                    DirectionDiagonale.HautGauche

                else -> DirectionDiagonale.Aucune
            }

            println(direction)

            while(
                0 <= coordonne1.x && coordonne1.x <= taille - 1 &&
                0 <= coordonne1.y && coordonne1.y <= taille - 1
            ) {
                coordonne1 = when (direction) {
                    DirectionDiagonale.HautGauche -> Coordonne(coordonne1.x - 1, coordonne1.y - 1)
                    DirectionDiagonale.HautDroite -> Coordonne(coordonne1.x - 1, coordonne1.y + 1)
                    DirectionDiagonale.BasGauche -> Coordonne(coordonne1.x + 1, coordonne1.y - 1)
                    DirectionDiagonale.BasDroite -> Coordonne(coordonne1.x + 1, coordonne1.y + 1)
                    DirectionDiagonale.Aucune -> coordonne2
                }

                if (coordonne1.compare(coordonne2)) return result
                else result.add(coordonne1)
            }

            return mutableListOf()

        }

    }

    fun diagonaleHautGauche(): MutableList<Coordonne> {
        val result: MutableList<Coordonne> = mutableListOf()
        var count = 1

        while (
                0 <= coordonne.x - count && 0 <= coordonne.y - count
        ) result.add(Coordonne(coordonne.x - count, coordonne.y - count++))

        return result
    }

    fun diagonaleHautDroite(): MutableList<Coordonne> {
        val result: MutableList<Coordonne> = mutableListOf()
        var count = 1

        while (
                0 <= coordonne.x - count && coordonne.y + count <= taille - 1
        ) result.add(Coordonne(coordonne.x - count, coordonne.y + count++))

        return result
    }

    fun diagonaleBasGauche(): MutableList<Coordonne> {
        val result: MutableList<Coordonne> = mutableListOf()
        var count = 1

        while (
                coordonne.x + count <= taille - 1 && 0 <= coordonne.y - count
        ) result.add(Coordonne(coordonne.x + count, coordonne.y - count++))

        return result
    }

    fun diagonaleBasDroite(): MutableList<Coordonne> {
        val result: MutableList<Coordonne> = mutableListOf()
        var count = 1

        while (
                coordonne.x + count <= taille - 1 && coordonne.y + count <= taille - 1
        ) result.add(Coordonne(coordonne.x + count, coordonne.y + count++))

        return result
    }

}