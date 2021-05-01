package billy.ronico.jeu_de_dame.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Setting(

    var colorCase: Int,
    var tailleDamier: Int,
    var profondeur: Int,
    var nbrePiece: Int = when (tailleDamier) {
        6 -> 6
        8 -> 12
        10 -> 20
        else -> 12
    }

) : Parcelable, Serializable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(colorCase)
        parcel.writeInt(tailleDamier)
        parcel.writeInt(profondeur)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Setting> {
        private val seralVersionUid: Long = 12323465
        override fun createFromParcel(parcel: Parcel): Setting {
            return Setting(parcel)
        }

        override fun newArray(size: Int): Array<Setting?> {
            return arrayOfNulls(size)
        }
    }
}
