package billy.ronico.jeu_de_dame.controlers

import android.content.Context
import billy.ronico.jeu_de_dame.enums.CouleurCase
import billy.ronico.jeu_de_dame.enums.CouleurPiece
import billy.ronico.jeu_de_dame.enums.Direction
import billy.ronico.jeu_de_dame.enums.EtatCase
import billy.ronico.jeu_de_dame.models.*
import billy.ronico.jeu_de_dame.views.Damier

// Classe permettant de representer une partie de jeu

class Partie(
    private val taille: Int,
    private val nbrePiece: Int,
    val actionFinPartie: (textMessage: String) -> Unit
) {

    // On choisit aléatoirement le tour
    // Pour définir aussi les couleurs des joueurs

    // Le blanc commence toujours au premier

    // Le joueur1 a une direction vers le HAUT (commence d'en haut)
    // Le joueur2 a une direction vers le BAS (commence d'en bas)

    private var tour: Boolean = (Math.random() * 2).toInt() == 0

    // Joueur Blanc
    private var joueur1: Joueur = Joueur(
        taille, nbrePiece, CouleurPiece.BLANC, Direction.HAUT
    )

    // Joueur Noir
    private var joueur2: Joueur = Joueur(

        taille, nbrePiece, CouleurPiece.NOIR, Direction.BAS
    )

    private lateinit var damier: Damier

    fun initDamier(ctx: Context) {
        damier = Damier(ctx, taille)
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

    // Permet de retourner tout les coordonnés accéssible via un super Dame
    fun dameCoordonneAccessible(piece: PieceDame): MutableList<Coordonne> {

        val result = mutableListOf<Coordonne>()

        for (i in 0 until piece.diagonaleHautGauche().size) {
            if (damier.getCase(piece.diagonaleHautGauche()[i]).etatCase === EtatCase.VIDE)
                result.add(piece.diagonaleHautGauche()[i])
            else break
        }

        for (i in 0 until piece.diagonaleHautDroite().size) {
            if (damier.getCase(piece.diagonaleHautDroite()[i]).etatCase === EtatCase.VIDE)
                result.add(piece.diagonaleHautDroite()[i])
            else break
        }

        for (i in 0 until piece.diagonaleBasGauche().size) {
            if (damier.getCase(piece.diagonaleBasGauche()[i]).etatCase === EtatCase.VIDE)
                result.add(piece.diagonaleBasGauche()[i])
            else break
        }

        for (i in 0 until piece.diagonaleBasDroite().size) {
            if (damier.getCase(piece.diagonaleBasDroite()[i]).etatCase === EtatCase.VIDE)
                result.add(piece.diagonaleBasDroite()[i])
            else break
        }


        return result

    }

    // Retourne une liste des pieces bougeables du joueur courant
    fun pieceBougeable(): List<Piece> {
        return joueurCourant().tabPiece.filter { piece ->

            when (piece) {

                is PieceNormal ->
                    piece.coordonneAccessible().any { damier.getCase(it).etatCase == EtatCase.VIDE }

                is PieceDame ->
                    dameCoordonneAccessible(piece).size > 0

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
        else if (piece is PieceDame)
            dameCoordonneAccessible(piece).forEach {
                damier.getCase(it).updateCouleur(CouleurCase.VERT)
            }
    }

    // Fonction permettatn de renvoyer les pieces avec lesquelles on pourra manger
    fun pieceMangeable(): List<Piece> {
        return this.joueurCourant().tabPiece.filter {
            when (it) {
                is PieceNormal -> coordonneMangeablePN(it.coordonne).size > 0
                is PieceDame -> coordonneMangeableDame(it.coordonne).size > 0
                else -> false
            }
        }
    }

    // Permet de retourner tout les coordonneés mangeables d'une coordonne (En supposant une piece normal)
    fun coordonneMangeablePN(
        coordonne: Coordonne, oldCoordonneManger: Coordonne = coordonne
    ): MutableList<InfoPieceManger> {

        val result: MutableList<InfoPieceManger> = mutableListOf()

        // Diagonale Haut Gauche
        if (

            !oldCoordonneManger.compare(Coordonne(coordonne.x - 1, coordonne.y - 1)) &&

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
            result.add(
                InfoPieceManger(
                    coordonne,
                    Coordonne(coordonne.x - 1, coordonne.y - 1),
                    Coordonne(coordonne.x - 2, coordonne.y - 2)
                )
            )

        // Diagonale Haut Droite
        if (

            !oldCoordonneManger.compare(Coordonne(coordonne.x - 1, coordonne.y + 1)) &&

            0 <= coordonne.x - 2 && coordonne.y + 2 <= taille - 1 &&

            joueurNonCourant().tabPiece.any {
                it.coordonne.compare(Coordonne(coordonne.x - 1, coordonne.y + 1))
            }

            && damier.getCase(
                Coordonne(coordonne.x - 2, coordonne.y + 2)
            ).etatCase == EtatCase.VIDE
        )
            result.add(
                InfoPieceManger(
                    coordonne,
                    Coordonne(coordonne.x - 1, coordonne.y + 1),
                    Coordonne(coordonne.x - 2, coordonne.y + 2)
                )
            )

        // Diagonale Bas Gauche
        if (

            !oldCoordonneManger.compare(Coordonne(coordonne.x + 1, coordonne.y - 1)) &&

            coordonne.x + 2 <= taille - 1 && 0 <= coordonne.y - 2 &&

            joueurNonCourant().tabPiece.any {
                it.coordonne.compare(Coordonne(coordonne.x + 1, coordonne.y - 1))
            }

            && damier.getCase(
                Coordonne(coordonne.x + 2, coordonne.y - 2)
            ).etatCase == EtatCase.VIDE
        )
            result.add(
                InfoPieceManger(
                    coordonne,
                    Coordonne(coordonne.x + 1, coordonne.y - 1),
                    Coordonne(coordonne.x + 2, coordonne.y - 2)
                )
            )

        // Diagonale Bas Droite
        if (

            !oldCoordonneManger.compare(Coordonne(coordonne.x + 1, coordonne.y + 1)) &&

            coordonne.x + 2 <= taille - 1 && coordonne.y + 2 <= taille - 1 &&

            joueurNonCourant().tabPiece.any {
                it.coordonne.compare(Coordonne(coordonne.x + 1, coordonne.y + 1))
            }

            && damier.getCase(
                Coordonne(coordonne.x + 2, coordonne.y + 2)
            ).etatCase == EtatCase.VIDE
        )
            result.add(
                InfoPieceManger(
                    coordonne,
                    Coordonne(coordonne.x + 1, coordonne.y + 1),
                    Coordonne(coordonne.x + 2, coordonne.y + 2)
                )
            )

        return result

    }

    fun getAllCheminPourMangerPN(piece: PieceNormal): MutableList<MutableList<InfoPieceManger>> {

        var result: MutableList<MutableList<InfoPieceManger>> = mutableListOf()

        // On recupere les informations apres manger de notre piece
        var infoCoordonneManger = coordonneMangeablePN(piece.coordonne)

        // On rajoute dans notre resultat une liste contenant chacune des informations trouvés
        for (infoManger in infoCoordonneManger)
            result.add(mutableListOf(infoManger))

        // Tant qu'on peut encore faire evoluer notre arbre
        while (
            result.any {
                coordonneMangeablePN(
                    it.last().coordonneApres,
                    it.last().coordonneCaseManger
                ).size > 0
            }
        ) {

            val newResult: MutableList<MutableList<InfoPieceManger>> = mutableListOf()

            for (cheminInfoManger in result) {

                infoCoordonneManger = coordonneMangeablePN(
                    cheminInfoManger.last().coordonneApres,
                    cheminInfoManger.last().coordonneCaseManger
                )

                if (infoCoordonneManger.size > 0)
                    for (infoManger in infoCoordonneManger)
                        newResult.add((cheminInfoManger + mutableListOf(infoManger)) as MutableList<InfoPieceManger>)
                else newResult.add(cheminInfoManger)
            }

            result = newResult

        }
        return result
    }

    // Permet de retourner les chemins les plus long pour manger du joueur courant
    fun getCheminsLesPlusLongPN(): MutableList<MutableList<InfoPieceManger>> {

        // On recupere tout les chemins pour manger

        val allCheminPourManger: MutableList<MutableList<InfoPieceManger>> = mutableListOf()

        pieceMangeable().forEach {
            if (it is PieceNormal)
                allCheminPourManger.addAll(getAllCheminPourMangerPN(it))
        }

        // On recupere tout les chemins les plus long

        val cheminsLesPlusLong: MutableList<MutableList<InfoPieceManger>> = mutableListOf()

        var profondeurMax = 0

        allCheminPourManger.forEach { if (it.size > profondeurMax) profondeurMax = it.size }

        cheminsLesPlusLong += allCheminPourManger.filter { it.size == profondeurMax }

        return cheminsLesPlusLong

    }

    // Permet de regarder les pieces dames avec lesquelles, on pourra manger
    fun coordonneMangeableDame(
        coordonne: Coordonne, oldCoordonneManger: Coordonne = coordonne
    ): MutableList<InfoDameManger> {

        // On recupere les diagonales de notre coodonne
        val diagonaleHautGauche = PieceDame.diagonaleHautGauche(coordonne, taille)
        val diagonaleHautDroite = PieceDame.diagonaleHautDroite(coordonne, taille)
        val diagonaleBasGauche = PieceDame.diagonaleBasGauche(coordonne, taille)
        val diagonaleBasDroite = PieceDame.diagonaleBasDroite(coordonne, taille)

        val result: MutableList<InfoDameManger> = mutableListOf()

        for (i in 0 until diagonaleHautGauche.size) {

            // On parcours la premières diagonale

            if (
            // On est pas sur l'ancienne coordonnée
                !oldCoordonneManger.compare(diagonaleHautGauche[i]) &&
                // On a une piece adverse sur cette coordonnée
                joueurNonCourant().tabPiece.any { it.coordonne.compare(diagonaleHautGauche[i]) } &&
                // On a regarde si on peut au moins manger une fois la piece
                0 <= diagonaleHautGauche[i].x - 1 && 0 <= diagonaleHautGauche[i].y - 1
            ) {

                if (
                // On regarde si la case suivante est vide
                    damier.getCase(
                        Coordonne(diagonaleHautGauche[i].x - 1, diagonaleHautGauche[i].y - 1)
                    ).etatCase === EtatCase.VIDE
                ) {
                    // On recupere tout les case où on pourra se mettre après avoir manger la pièce
                    val caseApresManger: MutableList<Coordonne> = mutableListOf()
                    val diagonaleHautGaucheManger =
                        PieceDame.diagonaleHautGauche(diagonaleHautGauche[i], taille)

                    for (j in 0 until diagonaleHautGaucheManger.size) {
                        if (
                            damier.getCase(diagonaleHautGaucheManger[j]).etatCase === EtatCase.VIDE
                        ) caseApresManger.add(diagonaleHautGaucheManger[j])
                        else break
                    }

                    // On ajoute tout cela à notre resultat
                    result.add(InfoDameManger(coordonne, diagonaleHautGauche[i], caseApresManger))

                    // On break car on peut avoir qu'une seul manger sur une diagonal
                    break
                } else break

            }
            // Si on trouve une piece à nous, on peut plus aller plus loin
            else if (joueurCourant().tabPiece.any { it.coordonne.compare(diagonaleHautGauche[i]) }) break
        }

        for (i in 0 until diagonaleHautDroite.size) {

            if (
                !oldCoordonneManger.compare(diagonaleHautDroite[i]) &&
                joueurNonCourant().tabPiece.any { it.coordonne.compare(diagonaleHautDroite[i]) } &&
                0 <= diagonaleHautDroite[i].x - 1 && diagonaleHautDroite[i].y + 1 <= taille - 1
            ) {

                if (
                    damier.getCase(
                        Coordonne(diagonaleHautDroite[i].x - 1, diagonaleHautDroite[i].y + 1)
                    ).etatCase === EtatCase.VIDE
                ) {
                    val caseApresManger: MutableList<Coordonne> = mutableListOf()
                    val diagonaleHautDroiteManger =
                        PieceDame.diagonaleHautDroite(diagonaleHautDroite[i], taille)

                    for (j in 0 until diagonaleHautDroiteManger.size) {
                        if (
                            damier.getCase(diagonaleHautDroiteManger[j]).etatCase === EtatCase.VIDE
                        ) caseApresManger.add(diagonaleHautDroiteManger[j])
                        else break
                    }

                    result.add(InfoDameManger(coordonne, diagonaleHautDroite[i], caseApresManger))

                    break
                } else break

            } else if (joueurCourant().tabPiece.any { it.coordonne.compare(diagonaleHautDroite[i]) }) break
        }

        for (i in 0 until diagonaleBasGauche.size) {

            if (
                !oldCoordonneManger.compare(diagonaleBasGauche[i]) &&
                joueurNonCourant().tabPiece.any { it.coordonne.compare(diagonaleBasGauche[i]) } &&
                diagonaleBasGauche[i].x + 1 <= taille - 1 && 0 <= diagonaleBasGauche[i].y - 1
            ) {

                if (
                    damier.getCase(
                        Coordonne(diagonaleBasGauche[i].x + 1, diagonaleBasGauche[i].y - 1)
                    ).etatCase === EtatCase.VIDE
                ) {
                    val caseApresManger: MutableList<Coordonne> = mutableListOf()
                    val diagonaleBasGaucheManger =
                        PieceDame.diagonaleBasGauche(diagonaleBasGauche[i], taille)

                    for (j in 0 until diagonaleBasGaucheManger.size) {
                        if (
                            damier.getCase(diagonaleBasGaucheManger[j]).etatCase === EtatCase.VIDE
                        ) caseApresManger.add(diagonaleBasGaucheManger[j])
                        else break
                    }

                    result.add(InfoDameManger(coordonne, diagonaleBasGauche[i], caseApresManger))

                    break
                } else break


            } else if (joueurCourant().tabPiece.any { it.coordonne.compare(diagonaleBasGauche[i]) }) break
        }

        for (i in 0 until diagonaleBasDroite.size) {

            if (
                !oldCoordonneManger.compare(diagonaleBasDroite[i]) &&
                joueurNonCourant().tabPiece.any { it.coordonne.compare(diagonaleBasDroite[i]) } &&
                diagonaleBasDroite[i].x + 1 <= taille - 1 && diagonaleBasDroite[i].y + 1 <= taille - 1
            ) {

                if (
                    damier.getCase(
                        Coordonne(diagonaleBasDroite[i].x + 1, diagonaleBasDroite[i].y + 1)
                    ).etatCase === EtatCase.VIDE
                ) {
                    val caseApresManger: MutableList<Coordonne> = mutableListOf()
                    val diagonaleBasDroiteManger =
                        PieceDame.diagonaleBasDroite(diagonaleBasDroite[i], taille)

                    for (j in 0 until diagonaleBasDroiteManger.size) {
                        if (
                            damier.getCase(diagonaleBasDroiteManger[j]).etatCase === EtatCase.VIDE
                        ) caseApresManger.add(diagonaleBasDroiteManger[j])
                        else break
                    }

                    result.add(InfoDameManger(coordonne, diagonaleBasDroite[i], caseApresManger))

                    break

                } else break

            } else if (joueurCourant().tabPiece.any { it.coordonne.compare(diagonaleBasDroite[i]) }) break

        }

        return result

    }

    fun getAllCheminMangerDame(piece: PieceDame): MutableList<MutableList<InfoDameManger>> {

        var result: MutableList<MutableList<InfoDameManger>> = mutableListOf()

        // On recupere les informations des coordonnées que l'on pourrait manger
        val infoDameManger = coordonneMangeableDame(piece.coordonne)

        // Result est une liste de liste où la première dim corréspond à un chemin pour manger
        // Et la deuxieme dimension correspond aux arrêtes

        for (info in infoDameManger)
            result.add(mutableListOf(info))

        while (
            result.any {
                it.last().coordonneApres.any { coordonneApres ->
                    coordonneMangeableDame(
                        coordonneApres,
                        it.last().coordonneCaseManger
                    ).size > 0
                }
            }
        ) {

            //On stocke reconstruit le tableau, en rajoutant à la fin tout les elements qui peuvent suivre

            val newResult: MutableList<MutableList<InfoDameManger>> = mutableListOf()

            for (cheminInfoManger in result) {

                val infoCoordonneManger: MutableList<InfoDameManger> = mutableListOf()

                cheminInfoManger.last().coordonneApres.forEach {
                    infoCoordonneManger.addAll(
                        coordonneMangeableDame(
                            it, cheminInfoManger.last().coordonneCaseManger
                        )
                    )
                }

                if (infoCoordonneManger.size > 0)
                    for (infoManger in infoCoordonneManger)
                        newResult.add((cheminInfoManger + mutableListOf(infoManger)) as MutableList<InfoDameManger>)
                else newResult.add(cheminInfoManger)

            }

            result = newResult

            println(result)

        }

        return result

    }

    fun getCheminsLesPlusLongDame(): MutableList<MutableList<InfoDameManger>> {

        // On recupere tout les chemins pour manger

        val allCheminPourManger: MutableList<MutableList<InfoDameManger>> = mutableListOf()

        pieceMangeable().forEach {
            if (it is PieceDame)
                allCheminPourManger.addAll(getAllCheminMangerDame(it))
        }

        // On recupere tout les chemins les plus long

        val cheminsLesPlusLong: MutableList<MutableList<InfoDameManger>> = mutableListOf()

        var profondeurMax = 0

        allCheminPourManger.forEach { if (it.size > profondeurMax) profondeurMax = it.size }

        cheminsLesPlusLong += allCheminPourManger.filter { it.size == profondeurMax }

        return cheminsLesPlusLong

    }

    fun coloreCheminPN(chemin: MutableList<InfoPieceManger>) {
        chemin.forEach {
            damier.changeCouleurCase(it.coordonneCourant, CouleurCase.VERT)
            damier.changeCouleurCase(it.coordonneCaseManger, CouleurCase.VERT)
            damier.changeCouleurCase(it.coordonneApres, CouleurCase.VERT)
        }
    }

    fun coloreCheminDame(chemin: MutableList<InfoDameManger>) {
        chemin.forEach {

            PieceDame.diagonaleEntre2Coordonne(it.coordonneCourant, it.coordonneCaseManger, taille)
                .forEach { coord ->
                    damier.changeCouleurCase(coord, CouleurCase.VERT)
                }

            damier.changeCouleurCase(it.coordonneCourant, CouleurCase.VERT)
            damier.changeCouleurCase(it.coordonneCaseManger, CouleurCase.VERT)
            it.coordonneApres.forEach { coordonneApres ->
                damier.changeCouleurCase(coordonneApres, CouleurCase.VERT)
            }
        }
    }

    // Permet de fixer l'evenement de mouvement d'une piece
    fun fixeEventMouvPiecePN(piece: PieceNormal) {

        // Je remets les evenements
        fixeEventCanMouvPiece()

        // Je colore la case de la piece bougeable
        colorCaseBougeableParPiece(piece)

        // Je fixe les evenements de mouvement sur chacune des cases ou on pourra manger
        piece.coordonneAccessible().forEach {

            if (damier.getCase(it).etatCase == EtatCase.VIDE)

            // On fixe cette methode pour l'event
                damier.addEventCase(it) {

                    piece.bougePiece(it)


                    // Permet de changer en dame
                    if (
                        joueurCourant().direction === Direction.HAUT &&
                        it.x === 0
                    ) joueurCourant().changeDame(it)

                    if (
                        joueurCourant().direction === Direction.BAS &&
                        it.x === taille - 1
                    ) joueurCourant().changeDame(it)

                    // On continue la partie
                    partie()

                }
        }

    }

    fun fixeEventMangePiecePN(listCheminPourManger: MutableList<InfoPieceManger>) {

        initCouleurVert()
        fixeEventCanMouvPiece()
        coloreCheminPN(listCheminPourManger)

        // Dernier coordonné, celui qu'on doit cliquer pour bouger

        val listCoordonneManger: List<Coordonne> = listCheminPourManger.map {
            it.coordonneCaseManger
        }

        listCheminPourManger.forEachIndexed { i: Int, map: InfoPieceManger ->
            if (i === listCheminPourManger.size - 1) {
                // On est sur le dernier chemin
                val lastCoordonne: Coordonne = map.coordonneApres
                damier.addEventCase(lastCoordonne) {
                    listCoordonneManger.forEach {
                        joueurNonCourant().mangerPiece(it)
                        damier.changeEtatCase(it, EtatCase.VIDE)
                    }
                    val pieceJoueur =
                        joueurCourant().getPiece(listCheminPourManger[0].coordonneCourant) as Piece
                    pieceJoueur.bougePiece(lastCoordonne)

                    if (
                        joueurCourant().direction === Direction.HAUT &&
                        lastCoordonne.x === 0
                    ) joueurCourant().changeDame(lastCoordonne)

                    if (
                        joueurCourant().direction === Direction.BAS &&
                        lastCoordonne.x === taille - 1
                    ) joueurCourant().changeDame(lastCoordonne)

                    partie()
                }
                damier.addEventCase(map.coordonneCourant) {}
                damier.addEventCase(map.coordonneCaseManger) {}
            } else {
                // On est dans les chemins intermédiaires
                damier.addEventCase(map.coordonneCourant) {}
                damier.addEventCase(map.coordonneCaseManger) {}
                damier.addEventCase(map.coordonneApres) {}
            }
        }

    }

    fun fixeEventMouvDame(piece: PieceDame) {

        // On remet les evenements sur les autres Pieces
        fixeEventCanMouvPiece()

        // On colore les cases où on pourra se déplacer
        colorCaseBougeableParPiece(piece)

        println(getAllCheminMangerDame(piece))

        // On fixe les evenements sur les cases où, on pourra se déplacer
        dameCoordonneAccessible(piece).forEach {
            damier.addEventCase(it) {
                piece.bougePiece(it)
                // On continue la partie
                partie()
            }
        }

    }

    fun fixeEventMangeDame(listInfoDameManger: MutableList<InfoDameManger>) {

        initCouleurVert()
        fixeEventCanMouvPiece()

        coloreCheminDame(listInfoDameManger)

        // Les derniers coordonnés, ceux qu'on doivent cliquer

        val listCoordonneManger: List<Coordonne> = listInfoDameManger.map {
            it.coordonneCaseManger
        }

        listInfoDameManger.forEachIndexed { i: Int, infoDameManger ->

            PieceDame.diagonaleEntre2Coordonne(
                infoDameManger.coordonneCourant, infoDameManger.coordonneCaseManger, taille
            ).forEach {
                damier.addEventCase(it) {}
            }

            if (i === listInfoDameManger.size - 1) {
                // On est sur le dernier chemin

                infoDameManger.coordonneApres.forEach {
                    damier.addEventCase(it) {
                        listCoordonneManger.forEach { coordonneManger ->
                            joueurNonCourant().mangerPiece(coordonneManger)
                            damier.changeEtatCase(coordonneManger, EtatCase.VIDE)
                        }
                        val pieceJoueur =
                            joueurCourant().getPiece(listInfoDameManger[0].coordonneCourant) as Piece

                        pieceJoueur.bougePiece(it)

                        partie()
                    }
                    damier.addEventCase(infoDameManger.coordonneCourant) {}
                    damier.addEventCase(infoDameManger.coordonneCaseManger) {}
                }
            } else {

                damier.addEventCase(infoDameManger.coordonneCourant) {}
                damier.addEventCase(infoDameManger.coordonneCaseManger) {}
                infoDameManger.coordonneApres.forEach {
                    damier.addEventCase(it) {}
                }

            }

        }


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

            val cheminsLesPlusLongPN: MutableList<MutableList<InfoPieceManger>> =
                getCheminsLesPlusLongPN()

            val cheminsLesPlusLongDame: MutableList<MutableList<InfoDameManger>> =
                getCheminsLesPlusLongDame()

            val longueurMaxCheminsPN =
                if (cheminsLesPlusLongPN.size === 0) 0 else cheminsLesPlusLongPN[0].size

            val longueurMaxCheminsDame =
                if (cheminsLesPlusLongDame.size === 0) 0 else cheminsLesPlusLongDame[0].size

            when {
                longueurMaxCheminsPN > longueurMaxCheminsDame -> {
                    cheminsLesPlusLongPN.forEach {

                        damier.changeCouleurCase(it[0].coordonneCourant, CouleurCase.ORANGE)
                        damier.addEventCase(it[0].coordonneCourant) {

                            fixeEventMangePiecePN(it)

                        }
                    }
                }
                longueurMaxCheminsPN < longueurMaxCheminsDame -> {
                    cheminsLesPlusLongDame.forEach {

                        damier.changeCouleurCase(it[0].coordonneCourant, CouleurCase.ORANGE)
                        damier.addEventCase(it[0].coordonneCourant) {

                            fixeEventMangeDame(it)

                        }
                    }
                }
                else -> {

                    cheminsLesPlusLongPN.forEach {

                        damier.changeCouleurCase(it[0].coordonneCourant, CouleurCase.ORANGE)
                        damier.addEventCase(it[0].coordonneCourant) {

                            fixeEventMangePiecePN(it)

                        }
                    }

                    cheminsLesPlusLongDame.forEach {

                        damier.changeCouleurCase(it[0].coordonneCourant, CouleurCase.ORANGE)
                        damier.addEventCase(it[0].coordonneCourant) {

                            fixeEventMangeDame(it)

                        }
                    }

                }
            }

            // On parcours les chemins les plus long et on assigne les evenements adéquats


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
            if (joueurCourant().couleurPiece === CouleurPiece.BLANC)
                actionFinPartie("Joueur Noir a gagné")
            else actionFinPartie("Joueur Blanc a gagné")
            restartGame()
        }
    }

    // On arrête la partie si un des joueurs ne peut plus jouer
    fun finPartie(): Boolean = pieceMangeable().isEmpty() && pieceBougeable().isEmpty()

    fun restartGame() {

        tour = (Math.random() * 2).toInt() == 0

        // Joueur Blanc
        joueur1 = Joueur(
            taille, nbrePiece, CouleurPiece.BLANC, Direction.HAUT
        )

        // Joueur Noir
        joueur2 = Joueur(
            taille, nbrePiece, CouleurPiece.NOIR, Direction.BAS
        )

        render()

        partie()

    }

}