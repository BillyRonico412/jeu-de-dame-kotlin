package billy.ronico.jeu_de_dame.models

import billy.ronico.jeu_de_dame.controlers.EtatJeu

data class InfoArbreEtat(
    val etatJeu: EtatJeu,
    val poids: Int
)
