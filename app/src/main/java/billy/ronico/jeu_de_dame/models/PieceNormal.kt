package billy.ronico.jeu_de_dame.models

import billy.ronico.jeu_de_dame.enums.CouleurPiece
import billy.ronico.jeu_de_dame.enums.Direction
import billy.ronico.jeu_de_dame.enums.EtatPiece

class PieceNormal(
        couleurPiece: CouleurPiece, direction: Direction, coordonne: Coordonne, taille: Int
) : Piece(EtatPiece.values()[couleurPiece.ordinal], direction, coordonne, taille) {


    // Fonction permettant de retourne tout les cases accéssibles pour une piece normal
    // En effet, on a accès au 2 cases sur les diagonales (en fonction de la direction)
    // Sauf lorsqu'on est tout à gauche ou tout a droite

    fun coordonneAccessible(): Array<Coordonne> {

        if (direction == Direction.HAUT && 0 < coordonne.x && coordonne.x <= taille - 1) {

            // On pars vers le haut et qu'on est pas tout en haut

            return if (0 < coordonne.y && coordonne.y < taille - 1)
            // On est ni tout à gauche, ni tout à droite du damier
                arrayOf(
                        Coordonne(coordonne.x - 1, coordonne.y - 1),
                        Coordonne(coordonne.x - 1, coordonne.y + 1)
                )
            else if (coordonne.y == 0)
            // On est tout a gauche
                arrayOf(Coordonne(coordonne.x - 1, coordonne.y + 1))

            // On est tout à droite
            else arrayOf(Coordonne(coordonne.x - 1, coordonne.y - 1))

        } else if (direction == Direction.BAS && 0 <= coordonne.x && coordonne.x < taille - 1) {

            // On pars vers le bas et qu'on est pas tout en bas

            return if (0 < coordonne.y && coordonne.y < taille - 1)
            // On est ni tout à gauche, ni tout à droite du damier
                arrayOf(
                        Coordonne(coordonne.x + 1, coordonne.y - 1),
                        Coordonne(coordonne.x + 1, coordonne.y + 1)
                )
            else if (coordonne.y == 0)
            // On est tout a gauche
                arrayOf(Coordonne(coordonne.x + 1, coordonne.y + 1))

            // On est tout à droite
            else arrayOf(Coordonne(coordonne.x + 1, coordonne.y - 1))

        }

        else return arrayOf()

    }


}