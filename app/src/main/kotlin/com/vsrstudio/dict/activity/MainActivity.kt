package com.vsrstudio.dict.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.vsrstudio.dict.R
import com.vsrstudio.dict.WORDS

class MainActivity : BaseActivity() {

    override val layoutResourceId = R.layout.activity_main
    override val toolbarTitleId = R.string.app_name
    override val displayHomeAsUp = false
    override val homeButtonEnabled = false

    var number: Int = 0
    var winrate: Int = 0
    var count: Int = WORDS.size

    lateinit var numberTextView: TextView
    lateinit var winrateTextView: TextView
    lateinit var wordTextView: TextView
    lateinit var translationTextView: TextView
    lateinit var rememberButton: Button
    lateinit var answerButton: Button
    lateinit var randomOrderCheckBox: CheckBox
    lateinit var randomLangCheckBox: CheckBox
    lateinit var ruCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        numberTextView = findViewById(R.id.numberTextView) as TextView
        winrateTextView = findViewById(R.id.winrateTextView) as TextView
        wordTextView = findViewById(R.id.wordTextView) as TextView
        translationTextView = findViewById(R.id.translationTextView) as TextView
        rememberButton = findViewById(R.id.rememberButton) as Button
        answerButton = findViewById(R.id.answerButton) as Button
        randomOrderCheckBox = findViewById(R.id.randomOrderCheckBox) as CheckBox
        randomLangCheckBox = findViewById(R.id.randomLangCheckBox) as CheckBox
        ruCheckBox = findViewById(R.id.ruCheckBox) as CheckBox

        randomLangCheckBox.setOnCheckedChangeListener { compoundButton, checked -> ruCheckBox.isEnabled = !checked }
    }

    override fun onStart() {
        super.onStart()
        showNextWord(number)
    }

    fun showNextWord(num: Int) {
        val index = num % count

        val pair = currentWord(index)
        val word: String
        val translation: String
        if (randomLangCheckBox.isChecked) {
            if (Math.random() < 0.5) {
                word = pair.second
                translation = pair.first
            } else {
                word = pair.first
                translation = pair.second
            }
        } else {
            if (ruCheckBox.isChecked) {
                word = pair.second
                translation = pair.first
            } else {
                word = pair.first
                translation = pair.second
            }
        }

        numberTextView.text = "Number: ${number + 1}"
        winrateTextView.text = "Winrate: ${winratePercent(number, winrate).toInt()}%"
        rememberButton.text = "I remember"
        answerButton.text = "Answer"

        rememberButton.visibility = VISIBLE

        wordTextView.text = word
        translationTextView.text = ""

        rememberButton.setOnClickListener {
            winrate++
            intermediateState(translation)
        }

        answerButton.setOnClickListener {
            intermediateState(translation)
        }
    }

    private fun intermediateState(translation: String) {
        translationTextView.text = translation
        answerButton.text = "Next"
        rememberButton.visibility = GONE
        answerButton.setOnClickListener {
            showNextWord(newNumber())
        }
    }

    fun currentWord(number: Int): Pair<String, String> {
        return WORDS[number]
    }

    fun winratePercent(number: Int, winrate: Int): Float {
        return if (number == 0) {
            0.0F
        } else {
            winrate.toFloat() / number * 100
        }
    }

    fun newNumber(): Int {
        number++
        val num = if (randomOrderCheckBox.isChecked) {
            (Math.random() * count).toInt()
        } else {
            number
        }
        return num
    }

}