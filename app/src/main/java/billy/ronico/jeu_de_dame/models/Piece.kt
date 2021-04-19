package billy.ronico.jeu_de_dame.models

import billy.ronico.jeu_de_dame.enums.Direction
import billy.ronico.jeu_de_dame.enums.EtatCase
import billy.ronico.jeu_de_dame.enums.EtatPiece
import java.lang.Exception

// Une piece est composé de son état, sa direction et ses coordonnées
// Cette classe va nous permettre de definir des comportements propres au piece,
// comme le fait de bouger,

// Cette classe est abstraite et doit avoir deux enfants, PieceNormal et PieceDame

abstract class Piece(
        val etatPiece: EtatPiece, val direction: Direction, var coordonne: Coordonne, val taille: Int
) {

    init {
        // Genere une exception si une piece n'est plus dans le damier
        if (!testCoordonne())
            throw Exception("On ne peut pas placer un pion a cette coordonne : ${coordonne.x}, ${coordonne.y}")
    }

    fun etatCase() = EtatCase.values()[etatPiece.ordinal]

    // Permet de tester si une piece est dans le damier ou pas
    fun testCoordonne(): Boolean = (
            coordonne.x in 0 until taille && coordonne.y in 0 until taille
            )

    // Permet de bouger une piece
    fun bougePiece(_coordonne: Coordonne) {
        if (_coordonne.x in 0 until taille && coordonne.y in 0 until taille)
            coordonne = _coordonne
        else throw Exception("On ne peut pas placer un pion a cette coordonne : ${coordonne.x}, ${coordonne.y}")
    }
}