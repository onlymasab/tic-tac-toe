package org.example

fun main() {
    val playerX = Player('X')
    val playerO = Player('O')

    val ticTacToeGame = TicTacToeGame(playerX, playerO)
    ticTacToeGame.startGame()
}

// Data class representing a player
data class Player(val symbol: Char)

// Data class representing the Tic-Tac-Toe board
data class Board(val grid: Array<CharArray> = Array(3) { CharArray(3) { ' ' } }) {
    // Function to print the current board
    fun printBoard() {
        println("Current board:")
        for (row in grid) {
            println(row.joinToString(" | "))
            println("-".repeat(15))
        }
    }

    // Check if the board is full
    fun isFull(): Boolean {
        return grid.all { row -> row.all { it != ' ' } }
    }

    // Place a move on the board
    fun placeMove(row: Int, col: Int, player: Player): Boolean {
        return if (grid[row][col] == ' ') {
            grid[row][col] = player.symbol
            true
        } else {
            false
        }
    }

    // Function to check if the given player has won
    fun hasPlayerWon(player: Player): Boolean {
        // Check rows, columns, and diagonals
        for (i in 0..2) {
            if (grid[i].all { it == player.symbol } || grid.all { it[i] == player.symbol }) return true
        }
        if ((grid[0][0] == player.symbol && grid[1][1] == player.symbol && grid[2][2] == player.symbol) ||
            (grid[0][2] == player.symbol && grid[1][1] == player.symbol && grid[2][0] == player.symbol)
        ) return true
        return false
    }
}

// Sealed class representing the different states of the game
sealed class GameState {
    object Ongoing : GameState()
    data class Win(val player: Player) : GameState()
    object Draw : GameState()
}

// Interface representing basic game behavior
interface Game {
    fun playMove(row: Int, col: Int)
    fun checkGameState(): GameState
}

// Tic-Tac-Toe game class that implements the Game interface
class TicTacToeGame(private val playerX: Player, private val playerO: Player) : Game {
    private val board = Board()
    private var currentPlayer = playerX

    // Start the game loop
    fun startGame() {
        while (true) {
            board.printBoard()
            println("Player ${currentPlayer.symbol}'s turn. Enter your move (row and column): ")

            val (row, col) = readMove()

            if (board.placeMove(row, col, currentPlayer)) {
                when (val gameState = checkGameState()) {
                    is GameState.Win -> {
                        board.printBoard()
                        println("Player ${gameState.player.symbol} wins!")
                        return
                    }
                    GameState.Draw -> {
                        board.printBoard()
                        println("It's a draw!")
                        return
                    }
                    else -> {
                        switchPlayer()
                    }
                }
            } else {
                println("Invalid move, the cell is already occupied. Try again.")
            }
        }
    }

    // Function to read the player's move from the console
    private fun readMove(): Pair<Int, Int> {
        while (true) {
            try {
                print("Enter row (0-2): ")
                val row = readLine()!!.toInt()

                print("Enter column (0-2): ")
                val col = readLine()!!.toInt()

                if (row in 0..2 && col in 0..2) {
                    return Pair(row, col)
                } else {
                    println("Invalid input. Please enter numbers between 0 and 2.")
                }
            } catch (e: Exception) {
                println("Invalid input. Please enter valid numbers.")
            }
        }
    }

    // Function to switch the current player
    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == playerX) playerO else playerX
    }

    // Function to check the current state of the game
    override fun checkGameState(): GameState {
        if (board.hasPlayerWon(currentPlayer)) {
            return GameState.Win(currentPlayer)
        }
        if (board.isFull()) {
            return GameState.Draw
        }
        return GameState.Ongoing
    }

    // Function to play a move (not used directly in this example)
    override fun playMove(row: Int, col: Int) {
        board.placeMove(row, col, currentPlayer)
    }
}