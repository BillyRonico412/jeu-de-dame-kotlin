package billy.ronico.jeu_de_dame.models

data class InfoDameManger(
    val coordonneCourant: Coordonne, val coordonneCaseManger: Coordonne, val coordonneApres: MutableList<Coordonne>
)
