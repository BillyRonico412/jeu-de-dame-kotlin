package billy.ronico.jeu_de_dame.models

data class InfoBouger(
    val coordonne: Coordonne,
    val coordonneApres: MutableList<Coordonne>
)
