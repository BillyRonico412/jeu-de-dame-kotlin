package billy.ronico.jeu_de_dame.models

import billy.ronico.jeu_de_dame.enums.CouleurPiece
import billy.ronico.jeu_de_dame.enums.Direction
import billy.ronico.jeu_de_dame.enums.EtatPiece

class PieceDame(
        couleurPiece: CouleurPiece, direction: Direction, coordonne: Coordonne, taille: Int
) : Piece(EtatPiece.values()[couleurPiece.ordinal], direction, coordonne, taille) {

    // On va definir une suite de methodes permettant de definir tout les coordonnes diagonales
    // Partant de la piece vers les extrémités

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
                coordonne.x <= taille - 1 + count && 0 <= coordonne.y - count
        ) result.add(Coordonne(coordonne.x - count, coordonne.y - count++))

        return result
    }

    fun diagonaleBasDroite(): MutableList<Coordonne> {
        val result: MutableList<Coordonne> = mutableListOf()
        var count = 1

        while (
                coordonne.x <= taille - 1 + count && coordonne.y + count <= taille - 1
        ) result.add(Coordonne(coordonne.x - count, coordonne.y + count++))

        return result
    }


}