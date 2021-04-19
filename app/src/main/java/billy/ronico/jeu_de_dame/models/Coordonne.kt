package billy.ronico.jeu_de_dame.models

class Coordonne(var x: Int, var y: Int) {
    fun compare(coordonne: Coordonne): Boolean = coordonne.x == x && coordonne.y == y
    override fun toString(): String {
        return "($x, $y)"
    }
}