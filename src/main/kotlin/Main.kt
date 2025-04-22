import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var size by remember { mutableStateOf("8") }
    var mines by remember { mutableStateOf("10") }
    var game by remember { mutableStateOf<Buscaminas?>(null) }
    var texts by remember { mutableStateOf(Array(8) { Array(8) { "X" } }) }
    var info by remember { mutableStateOf("") }
    var sizeInt=0

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                TextField(
                    value = size,
                    onValueChange = { size = it },
                    label = { Text("Size") },
                    modifier = Modifier.padding(8.dp)
                )
                TextField(
                    value = mines,
                    onValueChange = { mines = it },
                    label = { Text("Mines") },
                    modifier = Modifier.padding(8.dp)
                )
            }
            Button(onClick = {
                sizeInt = size.toIntOrNull() ?: 8
                val minesInt = mines.toIntOrNull() ?: 10
                game = Buscaminas(sizeInt, minesInt)
                game?.creartablero()
                game?.ponerminas()
                texts = Array(sizeInt) { Array(sizeInt) { "" } }
                info = ""
            }) {
                Text("Start Game")
            }
            game?.let { game ->
                Text(info)
                for (element1 in 0 until sizeInt) {
                    Row(
                        horizontalArrangement = Arrangement.Start // Sin espacio entre botones
                    ) {
                        for (element2 in 0 until sizeInt) {
                            Button(
                                onClick = {
                                    if (!game.getvistabanderas()[element1][element2]) {
                                        game.destapar(element1, element2)
                                        texts = texts.mapIndexed { i, row ->
                                            row.mapIndexed { j, text ->
                                                if (game.getvistatablero()[i][j]) {
                                                    if (game.gettablero()[i][j] == 9) {
                                                        "*"
                                                    } else
                                                        game.gettablero()[i][j].toString()
                                                } else {
                                                    text
                                                }
                                            }.toTypedArray()
                                        }.toTypedArray()
                                        if (game.ganar()) {
                                            info = "Ganaste"
                                        }
                                        if (game.gettablero()[element1][element2] == 9) {
                                            info = "Perdiste"
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .size(50.dp) // Button size 50x50
                                    .padding(1.dp) // Space between buttons
                                    .pointerInput(Unit) {
                                        awaitPointerEventScope {
                                            while (true) {
                                                val event = awaitPointerEvent()
                                                if (event.buttons.isSecondaryPressed) { // Detect right-click
                                                    if (!game.getvistabanderas()[element1][element2]) {
                                                        game.ponerbandera(element1, element2)
                                                        texts[element1][element2] = "B"
                                                    }
                                                    else{
                                                        game.quitarbandera(element1, element2)
                                                        texts[element1][element2] = ""
                                                    }
                                                    info = game.vervistatablero()
                                                    info = ""
                                                }
                                            }
                                        }
                                    },
                                shape = RectangleShape, // Hace que el botón sea cuadrado
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.LightGray, // Cambia el color de fondo del botón
                                    contentColor = Color.Gray // Cambia el color del texto del botón
                                )
                            ) {
                                Text(texts[element1][element2])
                            }
                        }
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Buscaminas") {
        App()
    }
}