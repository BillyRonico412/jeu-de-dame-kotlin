package billy.ronico.jeu_de_dame.models

data class Evolution(
    val evolutionManger: List<MutableList<MutableList<InfoManger>>>,
    val evolutionBouger: List<InfoBouger> = mutableListOf()
)
/*
    evolutionManger est une matrice a 3 dimensions
    1ere Dim: Ce sont les evolutions manger de chacune des pieces pouvant manger
    2eme Dim: Les differents chemins maximal pour manger a partir d'une meme piece
    3eme Dim: Ce sont les infos manger
 */
