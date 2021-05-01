package billy.ronico.jeu_de_dame.models

data class InfoManger(
    val coordonneCourant: Coordonne, val coordonneManger: Coordonne, val coordonneApres: MutableList<Coordonne>
)
