package billy.ronico.jeu_de_dame.controlers

import billy.ronico.jeu_de_dame.enums.EtatCase
import billy.ronico.jeu_de_dame.enums.Tour
import billy.ronico.jeu_de_dame.enums.TypePiece
import billy.ronico.jeu_de_dame.models.*

class EtatJeu(
    val nbrePiece: Int,
    val taille: Int,
    val tour: Tour,
    var tabCase: List<List<EtatCase>> = List(taille) { ligne ->
        List(taille) { col ->
            when {
                ligne % 2 == col % 2 -> EtatCase.VIDE
                ligne in 0 until (nbrePiece / (taille / 2)) -> EtatCase.NOIR
                ligne in taille - (nbrePiece / (taille / 2)) until taille -> EtatCase.BLANC
                else -> EtatCase.VIDE
            }
        }
    }
) {

    init {
        changeDame()
    }

    override fun toString(): String {

        return "\n" + tabCase.foldRight("") { list, acc ->
            list.map {
                when (it) {
                    EtatCase.VIDE -> " "
                    EtatCase.NOIR -> "x"
                    EtatCase.BLANC -> "o"
                    EtatCase.NOIR_DAME -> "X"
                    EtatCase.BLANC_DAME -> "O"
                }
            }.toString() + "\n" + acc
        } + "\nTour : ${if (tour === Tour.BLANC) "blanc" else "noir"}"

    }

    fun changeDame() {
        val newTabCase: List<List<EtatCase>> = List(taille) { ligne ->
            List(taille) { col ->
                when {
                    getEtat(
                        Coordonne(
                            ligne,
                            col
                        )
                    ) === EtatCase.BLANC && ligne === 0 -> EtatCase.BLANC_DAME
                    getEtat(
                        Coordonne(
                            ligne,
                            col
                        )
                    ) === EtatCase.NOIR && ligne === taille - 1 -> EtatCase.NOIR_DAME
                    else -> getEtat(Coordonne(ligne, col))
                }
            }
        }

        tabCase = newTabCase

    }

    // Permet de retourner une liste contenant les 2 tableaux de joueurs
    fun etatsJoueur(): List<EtatCase> =
        if (tour === Tour.BLANC) listOf(EtatCase.BLANC, EtatCase.BLANC_DAME)
        else listOf(EtatCase.NOIR, EtatCase.NOIR_DAME)

    fun etatsNonJoueur(): List<EtatCase> =
        if (tour === Tour.NOIR) listOf(EtatCase.BLANC, EtatCase.BLANC_DAME)
        else listOf(EtatCase.NOIR, EtatCase.NOIR_DAME)

    // Permet de recuperer l'état d'une case à une coordonné
    fun getEtat(coordonne: Coordonne): EtatCase = tabCase[coordonne.x][coordonne.y]

    fun allPNJouer(): MutableList<Coordonne> {
        val result = mutableListOf<Coordonne>()

        when (tour) {

            Tour.BLANC -> for (ligne in 0 until taille)
                for (col in 0 until taille)
                    if (
                        getEtat(Coordonne(ligne, col)) === EtatCase.BLANC
                    ) result.add(Coordonne(ligne, col))

            Tour.NOIR -> for (ligne in 0 until taille)
                for (col in 0 until taille)
                    if (
                        getEtat(Coordonne(ligne, col)) === EtatCase.NOIR
                    ) result.add(Coordonne(ligne, col))

        }

        return result

    }

    fun allDameJouer(): MutableList<Coordonne> {

        val result = mutableListOf<Coordonne>()

        when (tour) {

            Tour.BLANC -> for (ligne in 0 until taille)
                for (col in 0 until taille)
                    if (
                        getEtat(Coordonne(ligne, col)) === EtatCase.BLANC_DAME
                    ) result.add(Coordonne(ligne, col))

            Tour.NOIR -> for (ligne in 0 until taille)
                for (col in 0 until taille)
                    if (
                        getEtat(Coordonne(ligne, col)) === EtatCase.NOIR_DAME
                    ) result.add(Coordonne(ligne, col))

        }

        return result

    }

    // Retourne tout les coordonnée du joueur courant
    fun allCoordonneJouer(): MutableList<Coordonne> =
        (allPNJouer() + allDameJouer()) as MutableList<Coordonne>

    fun allCoordonneNonJouer(): MutableList<Coordonne> {

        val result = mutableListOf<Coordonne>()

        when (tour) {

            Tour.BLANC -> for (ligne in 0 until taille)
                for (col in 0 until taille)
                    if (
                        getEtat(Coordonne(ligne, col)) === EtatCase.BLANC ||
                        getEtat(Coordonne(ligne, col)) === EtatCase.BLANC_DAME
                    ) result.add(Coordonne(ligne, col))

            Tour.NOIR -> for (ligne in 0 until taille)
                for (col in 0 until taille)
                    if (
                        getEtat(Coordonne(ligne, col)) === EtatCase.NOIR ||
                        getEtat(Coordonne(ligne, col)) === EtatCase.NOIR_DAME
                    ) result.add(Coordonne(ligne, col))

        }

        return result

    }

    fun diagonaleHautGauche(
        coord: Coordonne,
        result: MutableList<Coordonne> = mutableListOf()
    ): MutableList<Coordonne> {
        return if (
            !coord.hautGauche().estComprisDans(taille) ||
            getEtat(coord.hautGauche()) !== EtatCase.VIDE
        ) result
        else diagonaleHautGauche(
            coord.hautGauche(),
            (result + mutableListOf(coord.hautGauche())) as MutableList<Coordonne>
        )
    }

    fun diagonaleHautDroite(
        coord: Coordonne,
        result: MutableList<Coordonne> = mutableListOf()
    ): MutableList<Coordonne> {
        return if (
            !coord.hautDroite().estComprisDans(taille) ||
            getEtat(coord.hautDroite()) !== EtatCase.VIDE
        ) result
        else diagonaleHautDroite(
            coord.hautDroite(),
            (result + mutableListOf(coord.hautDroite())) as MutableList<Coordonne>
        )
    }

    fun diagonaleBasGauche(
        coord: Coordonne,
        result: MutableList<Coordonne> = mutableListOf()
    ): MutableList<Coordonne> {
        return if (
            !coord.basGauche().estComprisDans(taille) ||
            getEtat(coord.basGauche()) !== EtatCase.VIDE
        ) result
        else diagonaleBasGauche(
            coord.basGauche(),
            (result + mutableListOf(coord.basGauche())) as MutableList<Coordonne>
        )
    }

    fun diagonaleBasDroite(
        coord: Coordonne,
        result: MutableList<Coordonne> = mutableListOf()
    ): MutableList<Coordonne> {
        return if (
            !coord.basDroite().estComprisDans(taille) ||
            getEtat(coord.basDroite()) !== EtatCase.VIDE
        ) result
        else diagonaleBasDroite(
            coord.basDroite(),
            (result + mutableListOf(coord.basDroite())) as MutableList<Coordonne>
        )
    }

    fun caseEntre(a: Coordonne, b: Coordonne): MutableList<Coordonne> {
        return when {
            a.x < b.x && a.y < b.y -> diagonaleHautGauche(b)
            a.x < b.x && a.y > b.y -> diagonaleHautDroite(b)
            a.x > b.x && a.y < b.y -> diagonaleBasGauche(b)
            a.x > b.x && a.y > b.y -> diagonaleBasDroite(b)
            else -> mutableListOf()
        }
    }

    // Retourne tout les coordonnes de PieceNormal qu'on puissent bouger à partir de notre Etat
    fun caseBougeablePN(): MutableList<InfoBouger> {

        val result = mutableListOf<InfoBouger>()

        allPNJouer().forEach {

            val coordonneApres = mutableListOf<Coordonne>()

            if (tour === Tour.BLANC) {

                if (
                    it.hautGauche().estComprisDans(taille) &&
                    getEtat(it.hautGauche()) === EtatCase.VIDE
                ) coordonneApres.add(it.hautGauche())

                if (
                    it.hautDroite().estComprisDans(taille) &&
                    getEtat(it.hautDroite()) === EtatCase.VIDE
                ) coordonneApres.add(it.hautDroite())

            } else {

                if (
                    it.basGauche().estComprisDans(taille) &&
                    getEtat(it.basGauche()) === EtatCase.VIDE
                ) coordonneApres.add(it.basGauche())

                if (
                    it.basDroite().estComprisDans(taille) &&
                    getEtat(it.basDroite()) === EtatCase.VIDE
                ) coordonneApres.add(it.basDroite())
            }

            if (coordonneApres.size > 0) result.add(InfoBouger(it, coordonneApres))

        }

        return result

    }

    // Retourne tout les coordonnes de PieceDame qu'on puissent bouger à partir de notre Etat
    fun caseBougeableDame(): MutableList<InfoBouger> {

        val result = mutableListOf<InfoBouger>()

        allDameJouer().forEach {

            val coordonneApres = (
                    diagonaleHautGauche(it) +
                            diagonaleHautDroite(it) +
                            diagonaleBasGauche(it) +
                            diagonaleBasDroite(it)
                    ) as MutableList<Coordonne>

            if (coordonneApres.size > 0) result.add(InfoBouger(it, coordonneApres))

        }

        return result

    }

    // Retourne tout les coordonnes de PieceNormal qu'on puissent manger à partir de notre Etat
    fun caseMangeable(): List<MutableList<MutableList<InfoManger>>> {

        fun infoMangerPN(
            coordonne: Coordonne, coordonnesDejaManger: MutableList<Coordonne> = mutableListOf()
        ): MutableList<InfoManger> {

            val filsManger: MutableList<InfoManger> = mutableListOf()

            // Recuperer les infos manger HautGauche
            if (
                coordonne.hautGauche().estComprisDans(taille) &&
                etatsNonJoueur().contains(getEtat(coordonne.hautGauche())) &&
                coordonne.hautGauche().hautGauche().estComprisDans(taille) &&
                getEtat(coordonne.hautGauche().hautGauche()) === EtatCase.VIDE &&
                coordonnesDejaManger.all { !it.compare(coordonne.hautGauche()) }
            ) filsManger.add(
                InfoManger(
                    coordonneCourant = coordonne,
                    coordonneManger = coordonne.hautGauche(),
                    coordonneApres = mutableListOf(coordonne.hautGauche().hautGauche())
                )
            )

            // Recuperer les infos manger HautDroite
            if (
                coordonne.hautDroite().estComprisDans(taille) &&
                etatsNonJoueur().contains(getEtat(coordonne.hautDroite())) &&
                coordonne.hautDroite().hautDroite().estComprisDans(taille) &&
                getEtat(coordonne.hautDroite().hautDroite()) === EtatCase.VIDE &&
                coordonnesDejaManger.all { !it.compare(coordonne.hautDroite()) }
            ) filsManger.add(
                InfoManger(
                    coordonneCourant = coordonne,
                    coordonneManger = coordonne.hautDroite(),
                    coordonneApres = mutableListOf(coordonne.hautDroite().hautDroite())
                )
            )

            // Recuperer les infos manger basGauche
            if (
                coordonne.basGauche().estComprisDans(taille) &&
                etatsNonJoueur().contains(getEtat(coordonne.basGauche())) &&
                coordonne.basGauche().basGauche().estComprisDans(taille) &&
                getEtat(coordonne.basGauche().basGauche()) === EtatCase.VIDE &&
                coordonnesDejaManger.all { !it.compare(coordonne.basGauche()) }
            ) filsManger.add(
                InfoManger(
                    coordonneCourant = coordonne,
                    coordonneManger = coordonne.basGauche(),
                    coordonneApres = mutableListOf(coordonne.basGauche().basGauche())
                )
            )

            // Recuperer les infos manger BasDroite
            if (
                coordonne.basDroite().estComprisDans(taille) &&
                etatsNonJoueur().contains(getEtat(coordonne.basDroite())) &&
                coordonne.basDroite().basDroite().estComprisDans(taille) &&
                getEtat(coordonne.basDroite().basDroite()) === EtatCase.VIDE &&
                coordonnesDejaManger.all { !it.compare(coordonne.basDroite()) }
            ) filsManger.add(
                InfoManger(
                    coordonneCourant = coordonne,
                    coordonneManger = coordonne.basDroite(),
                    coordonneApres = mutableListOf(coordonne.basDroite().basDroite())
                )
            )

            return filsManger

        }

        fun infoMangerDame(
            coordonne: Coordonne, coordonnesDejaManger: MutableList<Coordonne> = mutableListOf()
        ): MutableList<InfoManger> {

            val result = mutableListOf<InfoManger>()

            val coordMangerHautGauche: Coordonne = if (diagonaleHautGauche(coordonne).size > 0)
                diagonaleHautGauche(coordonne).last().hautGauche()
            else coordonne.hautGauche()

            if (
                coordMangerHautGauche.estComprisDans(taille) &&
                coordMangerHautGauche.hautGauche()
                    .estComprisDans(taille) &&
                etatsNonJoueur().contains(
                    getEtat(
                        coordMangerHautGauche
                    )
                ) &&
                coordonnesDejaManger.all {
                    !it.compare(
                        coordMangerHautGauche
                    )
                } &&
                getEtat(
                    coordMangerHautGauche.hautGauche()
                ) === EtatCase.VIDE
            )
                result.add(
                    InfoManger(
                        coordonneCourant = coordonne,
                        coordonneManger = coordMangerHautGauche,
                        coordonneApres = diagonaleHautGauche(
                            coordMangerHautGauche
                        )
                    )
                )

            val coordMangerHautDroite: Coordonne = if (diagonaleHautDroite(coordonne).size > 0)
                diagonaleHautDroite(coordonne).last().hautDroite()
            else coordonne.hautDroite()

            if (
                coordMangerHautDroite.estComprisDans(taille) &&
                coordMangerHautDroite.hautDroite()
                    .estComprisDans(taille) &&
                etatsNonJoueur().contains(
                    getEtat(
                        coordMangerHautDroite
                    )
                ) &&
                coordonnesDejaManger.all {
                    !it.compare(
                        coordMangerHautDroite
                    )
                } &&
                getEtat(
                    coordMangerHautDroite.hautDroite()
                ) === EtatCase.VIDE
            )
                result.add(
                    InfoManger(
                        coordonneCourant = coordonne,
                        coordonneManger = coordMangerHautDroite,
                        coordonneApres = diagonaleHautDroite(
                            coordMangerHautDroite
                        )
                    )
                )

            val coordMangerBasGauche: Coordonne = if (diagonaleBasGauche(coordonne).size > 0)
                diagonaleBasGauche(coordonne).last().basGauche()
            else coordonne.basGauche()

            if (
                coordMangerBasGauche.estComprisDans(taille) &&
                coordMangerBasGauche.basGauche()
                    .estComprisDans(taille) &&
                etatsNonJoueur().contains(
                    getEtat(
                        coordMangerBasGauche
                    )
                ) &&
                coordonnesDejaManger.all {
                    !it.compare(
                        coordMangerBasGauche
                    )
                } &&
                getEtat(
                    coordMangerBasGauche.basGauche()
                ) === EtatCase.VIDE
            )
                result.add(
                    InfoManger(
                        coordonneCourant = coordonne,
                        coordonneManger = coordMangerBasGauche,
                        coordonneApres = diagonaleBasGauche(
                            coordMangerBasGauche
                        )
                    )
                )

            val coordMangerBasDroite: Coordonne = if (diagonaleBasDroite(coordonne).size > 0)
                diagonaleBasDroite(coordonne).last().basDroite()
            else coordonne.basDroite()

            if (
                coordMangerBasDroite.estComprisDans(taille) &&
                coordMangerBasDroite.basDroite()
                    .estComprisDans(taille) &&
                etatsNonJoueur().contains(
                    getEtat(
                        coordMangerBasDroite
                    )
                ) &&
                coordonnesDejaManger.all {
                    !it.compare(
                        coordMangerBasDroite
                    )
                } &&
                getEtat(
                    coordMangerBasDroite.basDroite()
                ) === EtatCase.VIDE
            )
                result.add(
                    InfoManger(
                        coordonneCourant = coordonne,
                        coordonneManger = coordMangerBasDroite,
                        coordonneApres = diagonaleBasDroite(
                            coordMangerBasDroite
                        )
                    )
                )

            return result

        }

        fun arbre(
            infoManger: InfoManger,
            typePiece: TypePiece,
            coordonneDejaManger: MutableList<Coordonne> = mutableListOf()
        ): ArbreManger {

            val filsMangerSuivant = mutableListOf<InfoManger>()

            filsMangerSuivant +=

                if (
                    typePiece === TypePiece.PN
                )
                    infoMangerPN(
                        coordonne = infoManger.coordonneApres[0],
                        coordonnesDejaManger = (coordonneDejaManger + infoManger.coordonneManger) as MutableList<Coordonne>
                    )
                else infoManger.coordonneApres.foldRight(mutableListOf()) { coord, acc ->
                    (infoMangerDame(
                        coordonne = coord,
                        coordonnesDejaManger = (coordonneDejaManger + infoManger.coordonneManger) as MutableList<Coordonne>
                    ) + acc) as MutableList<InfoManger>
                }


            return if (filsMangerSuivant.size === 0) Noeud(
                info = infoManger, fils = mutableListOf(ArbreVide)
            )
            else Noeud(
                info = infoManger,
                fils = filsMangerSuivant.map {
                    arbre(
                        infoManger = it,
                        coordonneDejaManger = (coordonneDejaManger + it.coordonneManger) as MutableList<Coordonne>,
                        typePiece = typePiece
                    )
                } as MutableList<ArbreManger>
            )
        }

        val casesMangerPN = allPNJouer().filter { infoMangerPN(it).size > 0 }

        val casesMangerDame = allDameJouer().filter { infoMangerDame(it).size > 0 }

        val infoCasesMangerPN = (casesMangerPN.map { infoMangerPN(it) })

        val infoCasesMangerDame = (casesMangerDame.map { infoMangerDame(it) })

        val infoCasesManger = infoCasesMangerPN + infoCasesMangerDame

        val toutLesArbres: MutableList<ArbreManger> = mutableListOf()

        infoCasesManger.forEach {

            it.forEach { infoManger ->
                toutLesArbres += if (
                    getEtat(infoManger.coordonneCourant) === EtatCase.BLANC ||
                    getEtat(infoManger.coordonneCourant) === EtatCase.NOIR
                )
                    arbre(infoManger, TypePiece.PN)
                else arbre(infoManger, TypePiece.DAME)
            }
        }

        var hauteurMaxArbre = 0

        toutLesArbres.forEach {
            if (it.hauteur() > hauteurMaxArbre) hauteurMaxArbre = it.hauteur()
        }

        val arbreMaxHauteur = toutLesArbres.filter {
            it.hauteur() === hauteurMaxArbre
        }

        // On doit recuperer la branche la plus longue

        return arbreMaxHauteur.map { it.aplatirArbre() }

    }

    fun caseBougeable(): List<InfoBouger> = caseBougeablePN() + caseBougeableDame()

    fun bouger(infoBouger: InfoBouger, coordonneBouger: Coordonne): EtatJeu {

        val newTabCase: List<List<EtatCase>> = List(taille) { ligne ->
            List(taille) { col ->
                when {
                    Coordonne(ligne, col).compare(infoBouger.coordonne) -> EtatCase.VIDE
                    Coordonne(
                        ligne,
                        col
                    ).compare(coordonneBouger) -> getEtat(infoBouger.coordonne)
                    else -> getEtat(Coordonne(ligne, col))
                }
            }
        }

        return EtatJeu(
            nbrePiece, taille, tour = if (tour === Tour.BLANC) Tour.NOIR else Tour.BLANC, newTabCase
        )

    }

    fun manger(chemin: MutableList<InfoManger>, coordonneApres: Coordonne): EtatJeu {

        // Faut enlever la piece qui a manger puis tout les autres pieces manger et mettre une piece qui
        // a manger la position coordonneApres

        val newTabCase: List<List<EtatCase>> = List(taille) { ligne ->
            List(taille) { col ->
                when {
                    Coordonne(ligne, col).compare(chemin[0].coordonneCourant) -> EtatCase.VIDE
                    chemin.any {
                        Coordonne(
                            ligne,
                            col
                        ).compare(it.coordonneManger)
                    } -> EtatCase.VIDE
                    Coordonne(
                        ligne,
                        col
                    ).compare(coordonneApres) -> getEtat(chemin[0].coordonneCourant)
                    else -> getEtat(Coordonne(ligne, col))
                }
            }
        }

        return EtatJeu(
            nbrePiece, taille, tour = if (tour === Tour.BLANC) Tour.NOIR else Tour.BLANC, newTabCase
        )

    }

    fun evolutionPossible(): Evolution {
        val allCaseMangeable = caseMangeable()
        return if (allCaseMangeable.isEmpty())
            Evolution(allCaseMangeable, caseBougeable())
        else Evolution(allCaseMangeable, mutableListOf())
    }

    // Retourne l'ensemble des EtatJeu à laquelle, on peut faire évoluer notre Jeu
    fun evolution(): MutableList<InfoArbreEtat> {

        val result = mutableListOf<InfoArbreEtat>()
        val evolutionPossible = evolutionPossible()

        // On peut donc manger et non bouger
        if (evolutionPossible.evolutionManger.isNotEmpty()) {
            evolutionPossible.evolutionManger.forEach { toutCheminMax ->
                toutCheminMax.forEach { chemin ->
                    chemin.last().coordonneApres.forEach {
                        result.add(InfoArbreEtat(manger(chemin, it), chemin.size))
                    }
                }
            }
        } else if (evolutionPossible.evolutionBouger.isNotEmpty()) {
            evolutionPossible.evolutionBouger.forEach { infoBouger ->
                infoBouger.coordonneApres.forEach {
                    result.add(InfoArbreEtat(bouger(infoBouger, it), 0))
                }
            }
        }

        return result

    }

}