package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.ui.game.allWordsList

class GameViewModel : ViewModel() {

    // Private mutable variables
    private var _score = 0
    val score: Int
        get() = _score

    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    private var _currentScrambledWord = ""
    val currentScrambledWord: Spannable
        get() {
            val spannable: Spannable = SpannableString(_currentScrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(_currentScrambledWord).build(),
                0,
                _currentScrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            return spannable
        }

    // Game-related variables
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String
    private var isGameOver: Boolean = false

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameViewModel", "GameViewModel destroyed!")
    }

    /*
     * Updates currentWord and currentScrambledWord with the next word.
     */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        // Ensure the scrambled word is not the same as the original
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }

        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }

    /*
     * Increases the game score if the player's word is correct.
     */
    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    /*
     * Returns true if the player word is correct and increases the score.
     */
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    /*
     * Returns true if the current word count is less than MAX_NO_OF_WORDS
     */
    fun nextWord(): Boolean {
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    fun isGameOver() = isGameOver
}
