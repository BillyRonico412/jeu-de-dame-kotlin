package billy.ronico.jeu_de_dame.models

data class CaseMangeable(
    val coordonne: Coordonne,
    val listCheminManger: MutableList<MutableList<InfoManger>>
)
