package billy.ronico.jeu_de_dame.models

class Coordonne(var x: Int, var y: Int) {

    fun compare(coordonne: Coordonne): Boolean = coordonne.x == x && coordonne.y == y

    fun hautGauche(): Coordonne = Coordonne(x - 1, y - 1)

    fun hautDroite(): Coordonne = Coordonne(x - 1, y + 1)

    fun basGauche(): Coordonne = Coordonne(x + 1, y - 1)

    fun basDroite(): Coordonne = Coordonne(x + 1, y + 1)

    // Permet de savoir si la valeur de x et y sont compris dans une plage de valeur

    fun estComprisDans(min: Int, max: Int): Boolean = (x in min until max && y in min until max)

    fun estComprisDans(max: Int): Boolean = (x in 0 until max && y in 0 until max)

    override fun toString(): String {
        return "($x, $y)"
    }

}