package billy.ronico.jeu_de_dame.models

import billy.ronico.jeu_de_dame.enums.CouleurPiece
import billy.ronico.jeu_de_dame.enums.Direction
import billy.ronico.jeu_de_dame.enums.EtatPiece

// Classe permettant de modeliser un joueur Humain Ou Ordi dans le Jeu

class Joueur(

        val taille: Int, val nbrePiece: Int, val couleurPiece: CouleurPiece,
        val direction: Direction

) {

    val tabPiece: MutableList<Piece> = mutableListOf()

    val etatPiece: EtatPiece = EtatPiece.values()[couleurPiece.ordinal]

    init {
        initPiece()
    }

    fun nombrePiece(): Int = tabPiece.size

    // Fonction permettant d'initialiser tout les pieces de notre joueur et les placés à la
    // bonne position

    fun initPiece() {

        val nbrePieceLigne: Int = taille / 2
        val nbreLigne: Int = nbrePiece / nbrePieceLigne


        tabPiece.clear()

        if (direction == Direction.BAS) {

            for (ligne in 0 until nbreLigne)
                for (col in 0 until nbrePieceLigne)
                    tabPiece.add(PieceNormal(couleurPiece, Direction.BAS, Coordonne(
                            ligne, if (ligne % 2 == 0) col * 2 + 1 else col * 2
                    ), taille))

        } else {

            for (ligne in 0 until nbreLigne)
                for (col in 0 until nbrePieceLigne)
                    tabPiece.add(PieceNormal(couleurPiece, Direction.HAUT, Coordonne(
                            taille - ligne - 1, if (ligne % 2 == 0) col * 2 else col * 2 + 1
                    ), taille))

        }
    }

    fun mangerPiece(coordonne: Coordonne) {

        var indexPieceManger = -1

        tabPiece.forEachIndexed { index, it ->
            if (it.coordonne.compare(coordonne)) {
                indexPieceManger = index
            }
        }

        if (indexPieceManger > -1)
            tabPiece.removeAt(indexPieceManger)

    }

    fun getPiece(coordonne: Coordonne): Piece? {
        return tabPiece.find {
            it.coordonne.compare(coordonne)
        }
    }

}