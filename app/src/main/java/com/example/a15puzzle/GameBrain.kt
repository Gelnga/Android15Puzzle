package com.example.a15puzzle

import org.json.JSONArray
import java.util.*

class GameBrain {
    private var _previousMoves = Stack<String>()
    private var _blankTileCurrentId = "imageGameButton33"
    private var _moves = 0
    private var _time = 0
    private var _win = false

    private var _completedGameBoard = createCompletedGameBoard()
    private var _gameBoard = createCompletedGameBoard()

    fun getWin(): Boolean {
        return _win
    }

    fun getGameBoard(): Array<Array<String>> {
        return _gameBoard
    }

    fun getGameJson(): String {
        val jsonArrayGameBoard = JSONArray(_gameBoard)
        val jsonPreviousMoves = JSONArray(_previousMoves)
        return jsonArrayGameBoard.toString() + ";" +
                jsonPreviousMoves.toString() + ";" +
                _blankTileCurrentId + ";" +
                _moves.toString() + ";" +
                _time + ";" +
                _win
    }

    fun getTime(): String {
        return _time.toString()
    }

    fun incrementTime() {
        _time++
    }

    fun restoreGameFromJson(gameJson: String) {
        val splitJson = gameJson.split(";")

        var jsonObject = JSONArray(splitJson[0])
        for (x in 0 until jsonObject.length()){
            for (y in 0 until (jsonObject[x] as JSONArray).length()) {
                _gameBoard[x][y] = (jsonObject[x] as JSONArray)[y] as String
            }
        }

        jsonObject = JSONArray(splitJson[1])
        if (jsonObject.length() != 0) {
            for (x in 0 until jsonObject.length()) {
                _previousMoves.add(jsonObject[x] as String)
            }
        }

        _blankTileCurrentId = splitJson[2]
        _moves = Integer.parseInt(splitJson[3])
        _time = Integer.parseInt(splitJson[4])
        _win = splitJson[5] == "true"
    }

    fun getMoves(): Int {
        return _moves
    }

    fun validateMove(idStr: String): Boolean {

        val row = getGameButtonRow(idStr)
        val col = getGameButtonCol(idStr)

        val rowBlankTile = getGameButtonRow(_blankTileCurrentId)
        val colBlankTile = getGameButtonCol(_blankTileCurrentId)

        if ((rowBlankTile - row == 1 && colBlankTile == col) ||
            (rowBlankTile - row == -1 && colBlankTile == col) ||
            (rowBlankTile == row && colBlankTile - col == 1) ||
            (rowBlankTile == row && colBlankTile - col == -1)) {

                _previousMoves.add(_blankTileCurrentId)
                swapButton(row, col, rowBlankTile, colBlankTile)
                _moves++
                return true
        }

        return false
    }

    fun undo() {
        if (_previousMoves.empty()) {
            return
        }
        _moves--
        val rowPrevious = getGameButtonRow(_previousMoves.peek())
        val colPrevious = getGameButtonCol(_previousMoves.peek())

        swapButton(rowPrevious, colPrevious,
            getGameButtonRow(_blankTileCurrentId), getGameButtonCol(_blankTileCurrentId))

        _previousMoves.pop()
    }

    fun newGame() {
        _gameBoard = createCompletedGameBoard()
        _blankTileCurrentId = "imageGameButton33"
        _moves = 0
        _time = 0
        _previousMoves = Stack<String>()
        _win = false
        shuffle()
    }

    private fun shuffle() {
        var i = 100
        while (i > 0) {
            randomMove()
            i--
        }
    }


    private fun randomMove() {
        val random = Random()
        val rowOrCol = random.nextInt(2)

        val change = if (rowOrCol == 0) {
            getGameButtonRow(_blankTileCurrentId)
        } else {
            getGameButtonCol(_blankTileCurrentId)
        }

        var copyOfChange: Int
        while(true) {
            copyOfChange = change
            if (random.nextInt(2) == 0) {
                copyOfChange++
            } else {
                copyOfChange--
            }

            if (copyOfChange in 0..3) {
                break
            }
        }

        if (rowOrCol == 0) {
            swapButton(copyOfChange, getGameButtonCol(_blankTileCurrentId),
                getGameButtonRow(_blankTileCurrentId), getGameButtonCol(_blankTileCurrentId))
        } else {
            swapButton(getGameButtonRow(_blankTileCurrentId), copyOfChange,
                getGameButtonRow(_blankTileCurrentId), getGameButtonCol(_blankTileCurrentId))
        }

    }

    private fun swapButton(rowPressed: Int, colPressed: Int, rowBlankTile: Int, colBlankTile: Int) {

        _gameBoard[rowBlankTile][colBlankTile] = _gameBoard[rowPressed][colPressed]
        _gameBoard[rowPressed][colPressed] = "X"

        _blankTileCurrentId = "imageGameButton$rowPressed$colPressed"

        if (_gameBoard.contentDeepEquals(_completedGameBoard) && !_previousMoves.empty()) {
            _win = true
        }
    }

    private fun getGameButtonRow(buttonId: String): Int {
        return Character.getNumericValue(buttonId.split("Button")[1][0])
    }

    private fun getGameButtonCol(buttonId: String): Int {
        return Character.getNumericValue(buttonId.split("Button")[1][1])
    }

    private fun createCompletedGameBoard(): Array<Array<String>> {
        val completedBoard = arrayOf(
            arrayOf("00", "01", "02", "03"),
            arrayOf("10", "11", "12", "13"),
            arrayOf("20", "21", "22", "23"),
            arrayOf("30", "31", "32", "X"),
        )
        val completedBoardCopy: Array<Array<String>?> = arrayOfNulls(4)
        for (x in completedBoard.indices) {
            completedBoardCopy[x] = completedBoard[x].copyOf()
        }
        return completedBoard.copyOf()
    }
}