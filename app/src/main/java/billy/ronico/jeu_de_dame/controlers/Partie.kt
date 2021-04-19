package billy.ronico.jeu_de_dame.controlers

import android.content.Context
import billy.ronico.jeu_de_dame.enums.CouleurCase
import billy.ronico.jeu_de_dame.enums.CouleurPiece
import billy.ronico.jeu_de_dame.enums.Direction
import billy.ronico.jeu_de_dame.enums.EtatCase
import billy.ronico.jeu_de_dame.models.*
import billy.ronico.jeu_de_dame.views.Damier

// Classe permettant de representer une partie de jeu

class Partie(private val taille: Int, private val nbrePiece: Int, context: Context) {

    // On choisit aléatoirement le tour
    // Pour définir aussi les couleurs des joueurs

    // Le blanc commence toujours au premier

    // Le joueur1 a une direction vers le HAUT (commence d'en haut)
    // Le joueur2 a une direction vers le BAS (commence d'en bas)

    private var tour: Boolean = (Math.random() * 2).toInt() == 0

    // Joueur Blanc
    private val joueur1: Joueur = Joueur(
        taille, nbrePiece, CouleurPiece.BLANC, Direction.HAUT
    )

    // Joueur Noir
    private val joueur2: Joueur = Joueur(
        taille, nbrePiece, CouleurPiece.NOIR, Direction.BAS
    )

    private val damier: Damier = Damier(context, taille)

    init {
        partie()
    }

    fun getDamier(): Damier = damier

    fun joueurCourant(): Joueur = if (tour) joueur1 else joueur2

    fun joueurNonCourant(): Joueur = if (!tour) joueur1 else joueur2

    fun tourSuivant() {
        tour = !tour
    }

    // Permet de lier les pieces de nos joueurs sur le damier
    private fun liaisonJoueurDamier() {

        val allPiece: List<Piece> = joueur1.tabPiece + joueur2.tabPiece

        allPiece.forEach {
            damier.changeEtatCase(it.coordonne, it.etatCase())
        }

    }

    fun render() {
        damier.initDamier()
        liaisonJoueurDamier()
    }

    // Retire les couleurs verts
    fun initCouleurVert() = damier.initCouleur(CouleurCase.VERT)

    // Retire les couleurs oranges
    fun initCouleurOrange() = damier.initCouleur(CouleurCase.ORANGE)

    // Retourne une liste des pieces bougeables du joueur courant
    fun pieceBougeable(): List<Piece> {
        return joueurCourant().tabPiece.filter { piece ->
            when (piece) {
                is PieceNormal ->
                    piece.coordonneAccessible().any { damier.getCase(it).etatCase == EtatCase.VIDE }

                is PieceDame -> false

                else -> false
            }
        }
    }

    // Colorie les pieces bougeables
    fun colorPieceBougeable() {
        pieceBougeable().forEach { damier.changeCouleurCase(it.coordonne, CouleurCase.ORANGE) }
    }

    // Colorie les cases bougeables de la piece
    fun colorCaseBougeableParPiece(piece: Piece) {

        initCouleurVert()

        colorPieceBougeable()

        damier.getCase(piece.coordonne).updateCouleur(CouleurCase.VERT)

        if (piece is PieceNormal)
            piece.coordonneAccessible().forEach {
                if (damier.getCase(it).etatCase == EtatCase.VIDE)
                    damier.getCase(it).updateCouleur(CouleurCase.VERT)
            }
    }

    // Fonction permettatn de renvoyer les pieces avec lesquelles on pourra manger
    fun pieceMangeable(): List<Piece> {
        return this.joueurCourant().tabPiece.filter {
            if (it is PieceNormal)
                coordonneMangeablePN(it.coordonne, it.coordonne).size > 0
            else false
        }
    }

    // Permet de retourner tout les coordonneés mangeables d'une coordonne (On supposant une piece normal)
    fun coordonneMangeablePN(
        coordonne: Coordonne, oldCoordonne: Coordonne
    ): MutableList<Map<String, Coordonne>> {

        val result: MutableList<Map<String, Coordonne>> = mutableListOf()

        // Diagonale Haut Gauche
        if (

            !oldCoordonne.compare(Coordonne(coordonne.x - 2, coordonne.y - 2)) &&

            0 <= coordonne.x - 2 && 0 <= coordonne.y - 2 &&

            // Lorsqu'on va manger, on ne sortira pas du damier

            // On a une piece adverse en haut à gauche

            joueurNonCourant().tabPiece.any {
                it.coordonne.compare(Coordonne(coordonne.x - 1, coordonne.y - 1))
            }

            // La piece en haut gauche au bout de 2 saut est vide

            && damier.getCase(
                Coordonne(coordonne.x - 2, coordonne.y - 2)
            ).etatCase == EtatCase.VIDE

        )
            result.add(mapOf(
                "coordonneCourant" to coordonne,
                "coordonneCaseManger" to Coordonne(coordonne.x - 1, coordonne.y - 1),
                "coordonneApres" to Coordonne(coordonne.x - 2, coordonne.y - 2)
            ))

        // Diagonale Haut Droite
        if (

            !oldCoordonne.compare(Coordonne(coordonne.x - 2, coordonne.y + 2)) &&

            0 <= coordonne.x - 2 && coordonne.y + 2 <= taille - 1 &&

            joueurNonCourant().tabPiece.any {
                it.coordonne.compare(Coordonne(coordonne.x - 1, coordonne.y + 1))
            }

            && damier.getCase(
                Coordonne(coordonne.x - 2, coordonne.y + 2)
            ).etatCase == EtatCase.VIDE
        )
            result.add(mapOf(
                "coordonneCourant" to coordonne,
                "coordonneCaseManger" to Coordonne(coordonne.x - 1, coordonne.y + 1),
                "coordonneApres" to Coordonne(coordonne.x - 2, coordonne.y + 2)
            ))

        // Diagonale Bas Gauche
        if (

            !oldCoordonne.compare(Coordonne(coordonne.x + 2, coordonne.y - 2)) &&

            coordonne.x + 2 <= taille - 1 && 0 <= coordonne.y - 2 &&

            joueurNonCourant().tabPiece.any {
                it.coordonne.compare(Coordonne(coordonne.x + 1, coordonne.y - 1))
            }

            && damier.getCase(
                Coordonne(coordonne.x + 2, coordonne.y - 2)
            ).etatCase == EtatCase.VIDE
        )
            result.add(mapOf(
                "coordonneCourant" to coordonne,
                "coordonneCaseManger" to Coordonne(coordonne.x + 1, coordonne.y - 1),
                "coordonneApres" to Coordonne(coordonne.x + 2, coordonne.y - 2)
            ))

        // Diagonale Bas Droite
        if (

            !oldCoordonne.compare(Coordonne(coordonne.x + 2, coordonne.y + 2)) &&

            coordonne.x + 2 <= taille - 1 && coordonne.y + 2 <= taille - 1 &&

            joueurNonCourant().tabPiece.any {
                it.coordonne.compare(Coordonne(coordonne.x + 1, coordonne.y + 1))
            }

            && damier.getCase(
                Coordonne(coordonne.x + 2, coordonne.y + 2)
            ).etatCase == EtatCase.VIDE
        )
            result.add(mapOf(
                "coordonneCourant" to coordonne,
                "coordonneCaseManger" to Coordonne(coordonne.x + 1, coordonne.y + 1),
                "coordonneApres" to Coordonne(coordonne.x + 2, coordonne.y + 2)
            ))

        return result

    }

    fun getAllCheminPourMangerPN(piece: PieceNormal): MutableList<MutableList<Map<String, Coordonne>>> {

        var result: MutableList<MutableList<Map<String, Coordonne>>> = mutableListOf()

        // On recupere les informations apres manger de notre piece
        var coordonneApresManger = coordonneMangeablePN(piece.coordonne, piece.coordonne)

        // On rajoute dans notre resultat une liste contenant chacune des informations trouvés
        for (cam in coordonneApresManger)
            result.add(mutableListOf(cam))

        // Tant qu'on peut encore faire evoluer notre arbre
        while (
            result.any {
                coordonneMangeablePN(
                    it.last()["coordonneApres"] as Coordonne,
                    it.last()["coordonneCourant"] as Coordonne
                ).size > 0
            }
        ) {

            val newResult: MutableList<MutableList<Map<String, Coordonne>>> = mutableListOf()

            for (listCam in result) {

                coordonneApresManger = coordonneMangeablePN(
                    listCam.last()["coordonneApres"] as Coordonne,
                    listCam.last()["coordonneCourant"] as Coordonne
                )
                if (coordonneApresManger.size > 0)
                    for (cam in coordonneApresManger)
                        newResult.add((listCam + mutableListOf(cam)) as MutableList<Map<String, Coordonne>>)
                else newResult.add(listCam)
            }
            result = newResult
        }
        return result
    }

    // Permet de retourner les chemins les plus long pour manger du joueur courant
    fun getCheminsLesPlusLongPN(): MutableList<MutableList<Map<String, Coordonne>>> {

        // On recupere tout les chemins pour manger

        val allCheminPourManger: MutableList<MutableList<Map<String, Coordonne>>> = mutableListOf()

        pieceMangeable().forEach {
            if (it is PieceNormal)
                allCheminPourManger.addAll(getAllCheminPourMangerPN(it))
        }

        // On recupere tout les chemins les plus long

        val cheminsLesPlusLong: MutableList<MutableList<Map<String, Coordonne>>> = mutableListOf()

        var profondeurMax = 0

        allCheminPourManger.forEach { if (it.size > profondeurMax) profondeurMax = it.size }

        cheminsLesPlusLong += allCheminPourManger.filter { it.size == profondeurMax }

        return cheminsLesPlusLong

    }

    fun coloreCheminPN(chemin: MutableList<Map<String, Coordonne>>) {
        chemin.forEach {
            damier.changeCouleurCase(it["coordonneCourant"] as Coordonne, CouleurCase.VERT)
            damier.changeCouleurCase(it["coordonneCaseManger"] as Coordonne, CouleurCase.VERT)
            damier.changeCouleurCase(it["coordonneApres"] as Coordonne, CouleurCase.VERT)
        }
    }

    // Permet de fixer l'evenement de mouvement d'une piece
    fun fixeEventMouvPiecePN(piece: PieceNormal) {

        // Je remets les evenements
        fixeEventCanMouvPiece()
        // Je colore la case de la piece bougeable
        colorCaseBougeableParPiece(piece)
        // Je fixe les evenements de mouvement

        piece.coordonneAccessible().forEach {

            if (damier.getCase(it).etatCase == EtatCase.VIDE)

            // On fixe cette methode pour l'event
                damier.addEventCase(it) {

                    // On met a jour le mouvement
                    damier.changeEtatCase(piece.coordonne, EtatCase.VIDE)
                    piece.bougePiece(it)
                    damier.changeEtatCase(it, piece.etatCase())

                    // On continue la partie
                    partie()

                }
        }

    }

    fun fixeEventMangePiecePN(listCheminPourManger: MutableList<Map<String, Coordonne>>) {

        initCouleurVert()
        fixeEventCanMouvPiece()
        coloreCheminPN(listCheminPourManger)

        // Dernier coordonné, celui qu'on doit cliquer pour bouger

        val listCoordonneManger: List<Coordonne> = listCheminPourManger.map {
            it["coordonneCaseManger"] as Coordonne
        }

        println(listCoordonneManger)

        listCheminPourManger.forEachIndexed { i: Int, map: Map<String, Coordonne> ->
            if (i == listCheminPourManger.size - 1) {
                // On est sur le dernier chemin
                val lastCoordonne: Coordonne = map["coordonneApres"] as Coordonne
                damier.addEventCase(lastCoordonne) {
                    listCoordonneManger.forEach {
                        joueurNonCourant().mangerPiece(it)
                        damier.changeEtatCase(it, EtatCase.VIDE)
                    }
                    val pieceJoueur = joueurCourant().getPiece(listCheminPourManger[0]["coordonneCourant"] as Coordonne) as Piece
                    pieceJoueur.bougePiece(lastCoordonne)
                    partie()
                }
                damier.addEventCase(map["coordonneCourant"] as Coordonne) {}
                damier.addEventCase(map["coordonneCaseManger"] as Coordonne) {}
            } else {
                // On est dans les chemins intermédiaires
                damier.addEventCase(map["coordonneCourant"] as Coordonne) {}
                damier.addEventCase(map["coordonneCaseManger"] as Coordonne) {}
                damier.addEventCase(map["coordonneApres"] as Coordonne) {}
            }
        }

    }

    fun fixeEventMouvDame(piece: PieceDame) {

    }

    // Permet de fixer les evenements sur les pieces bougeables

    // Si on peut manger une case, on assigne un evenement sur les pieces qui mange le plus
    // de pice

    // Sinon:
    // * On doit les alumer en orange
    // * En cas de clique, on doit allumer les cases ou la piece pourra bouger
    // * On doit enfin assigner l'event des Cases ou elle pourra bouger


    fun fixeEventCanMouvPiece() {

        damier.initEvent()

        if (pieceMangeable().isNotEmpty()) {

            val cheminsLesPlusLongPN: MutableList<MutableList<Map<String, Coordonne>>> = getCheminsLesPlusLongPN()

            // On parcours les chemins les plus long et on assigne les evenements adéquats

            cheminsLesPlusLongPN.forEach {

                damier.changeCouleurCase(it[0]["coordonneCourant"] as Coordonne, CouleurCase.ORANGE)
                damier.addEventCase(it[0]["coordonneCourant"] as Coordonne) {

                    fixeEventMangePiecePN(it)

                }
            }



        } else {

            colorPieceBougeable()

            pieceBougeable().forEach {
                damier.addEventCase(it.coordonne) {
                    if (it is PieceNormal)
                    fixeEventMouvPiecePN(it)
                    else if (it is PieceDame) fixeEventMouvDame(it)
                }
            }
        }

    }

    fun partie() {
        render()
        tourSuivant()
        if (!finPartie())
            fixeEventCanMouvPiece()
        else {
            if (joueurCourant().couleurPiece == CouleurPiece.BLANC)
                println("Joueur Blanc à gagner")
            else println("Joueur Noir à gagner")
        }
    }

    // On arrête la partie si un des joueurs ne peut plus jouer
    fun finPartie(): Boolean = (
        pieceMangeable().isEmpty() && pieceBougeable().isEmpty()
        )

}