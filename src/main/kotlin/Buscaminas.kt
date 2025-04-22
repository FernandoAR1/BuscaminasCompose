class MiException(mensaje:String):Exception(mensaje)
class Buscaminas(private var size:Int,private var numminas:Int){
    init {
        if (size<=1){
            throw MiException("tamaño del buscaminas no valido")
        }
        if ((numminas>(size*size))||numminas<=0){
            throw MiException("numero de minas no valido")
        }
    }
    private var tablero = Array(size) { Array(size) { 0 } }
    private var vistatablero = Array(size) { Array(size) { false } }
    private var vistabanderas = Array(size) { Array(size) { false } }
    fun creartablero(){
        for (i in 0..size-1){
            for (j in 0..size-1){
                tablero[i][j] = 0
            }
        }
    }
    fun vervistatablero():String{
        var tablerostring=StringBuilder()
        for (i in 0..size-1){
            for (j in 0..size-1){
                if (vistatablero[i][j]){
                    if (tablero[i][j]==9){
                        tablerostring.append("* ")
                    }
                    else {
                        tablerostring.append("${tablero[i][j]} ")
                    }
                }
                else if (vistabanderas[i][j]){
                    tablerostring.append("B ")
                }
                else{
                    tablerostring.append("X ")
                }
            }
            tablerostring.append("\n")
        }
        return tablerostring.toString()
    }

    fun gettablero():Array<Array<Int>>{
        return tablero
    }
    fun getvistatablero():Array<Array<Boolean>>{
        return vistatablero
    }
    fun getvistabanderas():Array<Array<Boolean>>{
        return vistabanderas
    }

    fun ponerminas(){
        var minas = 0
        while (minas < numminas){
            var x = (0..size-1).random()
            var y = (0..size-1).random()
            if (tablero[x][y] == 0){
                tablero[x][y] = 9
                minas++
            }
        }
        for (i in 0..size-1){
            for (j in 0..size-1){
                if (tablero[i][j] == 0){
                    tablero[i][j] = contarminas(i, j)
                }
            }
        }

    }
    fun contarminas(x:Int, y:Int):Int {
        var minas = 0
        for (i in -1..1) {
            for (j in -1..1) {
                if (x + i >= 0 && x + i < size && y + j >= 0 && y + j < size) {
                    if (tablero[x + i][y + j] == 9) {
                        minas++
                    }
                }
            }
        }
        return minas
    }
    fun destapar(x:Int, y:Int):Boolean{
        if (vistabanderas[x][y]) {
            return false
        }
        if (tablero[x][y] == 0){
            vistatablero[x][y] = true
            for (i in -1..1) {
                for (j in -1..1) {
                    if (x + i >= 0 && x + i < size && y + j >= 0 && y + j < size) {
                        if (!vistatablero[x + i][y + j]){
                            destapar(x + i, y + j)
                        }
                    }
                }
            }
        }
        else{
            vistatablero[x][y] = true
        }
        return tablero[x][y] == 9
    }
    fun ponerbandera(x:Int, y:Int){
        vistabanderas[x][y] = true
    }
    fun quitarbandera(x:Int, y:Int){
        vistabanderas[x][y] = false
    }
    fun ganar():Boolean{
        for (i in 0..size-1){
            for (j in 0..size-1){
                if (!vistatablero[i][j] && tablero[i][j] != 9){
                    return false
                }
            }
        }
        return true
    }
}