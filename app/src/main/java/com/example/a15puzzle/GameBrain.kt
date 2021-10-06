package com.example.a15puzzle

import org.json.JSONArray
import java.util.*

class GameBrain {
    private var _previousMoves = Stack<String>()
    private var _blankTileCurrentId = "imageGameButton33"
    private var _moves = 0

    private var _gameBoard = arrayOf(
        arrayOf("00", "01", "02", "03"),
        arrayOf("10", "11", "12", "13"),
        arrayOf("20", "21", "22", "23"),
        arrayOf("30", "31", "32", "X"),
    )

    fun getGameBoard(): Array<Array<String>> {
        return _gameBoard
    }

    fun getBoardJson(): String{
        val jsonArray = JSONArray(_gameBoard);
        return jsonArray.toString();
    }

    fun restoreBoardFromJson(boardJson: String){
        val jsonArray = JSONArray(boardJson)
        for (x in 0 until jsonArray.length()){
            for (y in 0 until (jsonArray[x] as JSONArray).length()) {
                _gameBoard[x][y] = (jsonArray[x] as JSONArray)[y] as String
            }
        }
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
        _gameBoard = arrayOf(
            arrayOf("00", "01", "02", "03"),
            arrayOf("10", "11", "12", "13"),
            arrayOf("20", "21", "22", "23"),
            arrayOf("30", "31", "32", "X"),
        )
        _blankTileCurrentId = "imageGameButton33"
        shuffle()
        _moves = 0
        _previousMoves = Stack<String>()
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
    }

    private fun getGameButtonRow(buttonId: String): Int {
        return Character.getNumericValue(buttonId.split("Button")[1][0])
    }

    private fun getGameButtonCol(buttonId: String): Int {
        return Character.getNumericValue(buttonId.split("Button")[1][1])
    }
}