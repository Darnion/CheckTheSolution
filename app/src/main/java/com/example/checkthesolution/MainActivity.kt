package com.example.checkthesolution

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var firstOperand = 0
    private var secondOperand = 0
    private var operationSign = ""
    private var totalCounter = 0
    private var correctCounter = 0
    private var wrongCounter = 0
    private var correctAnswer = ""
    private var isAnswerCorrect = false
    private var isAppRunning = false
    private var totalTime = 0.0
    private lateinit var firstOperandLabel: TextView
    private lateinit var secondOperandLabel: TextView
    private lateinit var operationLabel: TextView
    private lateinit var totalAmountLabel: TextView
    private lateinit var percentageCorrectLabel: TextView
    private lateinit var correctAmountLabel: TextView
    private lateinit var wrongAmountLabel: TextView
    private lateinit var timerLabel: TextView
    private lateinit var minTimeCounter: TextView
    private lateinit var maxTimeCounter: TextView
    private lateinit var avgTimeCounter: TextView
    private lateinit var answerLabel: TextView
    private lateinit var btnCorrect: Button
    private lateinit var btnWrong: Button
    private lateinit var btnStart: Button
    private lateinit var exampleLayout: LinearLayout
    private lateinit var chronometer: Chronometer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firstOperandLabel = findViewById(R.id.textFirstOperand)
        secondOperandLabel = findViewById(R.id.textSecondOperand)
        operationLabel = findViewById(R.id.textOperation)
        totalAmountLabel = findViewById(R.id.textTotalCounter)
        percentageCorrectLabel = findViewById(R.id.textPercentageCorrect)
        correctAmountLabel = findViewById(R.id.textRightCounter)
        wrongAmountLabel = findViewById(R.id.textWrongCounter)
        timerLabel = findViewById(R.id.textTimer)
        minTimeCounter = findViewById(R.id.textMinTimeCounter)
        maxTimeCounter = findViewById(R.id.textMaxTimeCounter)
        avgTimeCounter = findViewById(R.id.textAvgTimeCounter)
        answerLabel = findViewById(R.id.textAnswer)
        btnCorrect = findViewById(R.id.buttonCorrect)
        btnWrong = findViewById(R.id.buttonWrong)
        btnStart = findViewById(R.id.btnStart)
        exampleLayout = findViewById(R.id.linearLayoutExample)
        chronometer = findViewById(R.id.chronometer)
    }

    fun btnStartClick(view: View) {
        if (!isAppRunning)
        {
            createExample()
            btnCorrect.isEnabled = true
            btnWrong.isEnabled = true
            btnStart.text = "СТОП"
            isAppRunning = true
        }
        else
        {
            chronometer.stop()
            btnCorrect.isEnabled = false
            btnWrong.isEnabled = false
            btnStart.text = "СТАРТ"
            isAppRunning = false
        }
    }

    @SuppressLint("SetTextI18n")
    fun btnCheckClick(view: View) {
        chronometer.stop()

        totalCounter++
        totalAmountLabel.text = totalCounter.toString()

        val elapsedSecs = (SystemClock.elapsedRealtime() - chronometer.base) / 1000.0
        totalTime += elapsedSecs

        if (minTimeCounter.text.toString().toDouble() > elapsedSecs || minTimeCounter.text.toString().toDouble() == 0.0)
        {
            minTimeCounter.text = String.format("%.2f", elapsedSecs)
        }

        if (maxTimeCounter.text.toString().toDouble() < elapsedSecs)
        {
            maxTimeCounter.text = String.format("%.2f", elapsedSecs)
        }

        avgTimeCounter.text = String.format("%.2f", totalTime / totalCounter)

        if (!(((view as Button).text == btnCorrect.text) xor(isAnswerCorrect)))
        {
            correctCounter++
            correctAmountLabel.text = correctCounter.toString()
            exampleLayout.background = Color.rgb(0, 255, 0).toDrawable()
        }
        else
        {
            wrongCounter++
            wrongAmountLabel.text = wrongCounter.toString()
            exampleLayout.background = Color.rgb(255, 0, 0).toDrawable()
        }

        percentageCorrectLabel.text = String.format("%.2f", correctCounter.toDouble() / totalCounter.toDouble() * 100) + "%"

        val handler = Handler()
        handler.postDelayed({
            createExample()
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
        }, 100)
    }

    private fun createExample()
    {
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        exampleLayout.background = Color.rgb(255, 255, 255).toDrawable()

        firstOperand = Random.nextInt(10, 100)
        secondOperand = Random.nextInt(10, 100)

        firstOperandLabel.text  = firstOperand.toString()
        secondOperandLabel.text = secondOperand.toString()

        when (Random.nextInt(1, 5))
        {
            1 -> { operationSign = "+"
                correctAnswer = (firstOperand + secondOperand).toString()
            }
            2 -> { operationSign = "-"
                correctAnswer = (firstOperand - secondOperand).toString()
            }
            3 -> { operationSign = "*"
                correctAnswer = (firstOperand * secondOperand).toString()
            }
            4 -> { operationSign = "/"
                correctAnswer = String.format("%.2f" ,firstOperand.toDouble() / secondOperand.toDouble())
            }
        }

        operationLabel.text = operationSign

        val isCorrectAnswerGenerated = Random.nextBoolean()

        if (isCorrectAnswerGenerated)
        {
            answerLabel.text = correctAnswer
            isAnswerCorrect = true
        }
        else
        {
            do {
                if (operationSign == "/")
                {
                    answerLabel.text = String.format("%.2f", Random.nextDouble(100.0))
                }
                else
                {
                    answerLabel.text = Random.nextInt(1000).toString()
                }
            } while (answerLabel.text == correctAnswer)

            isAnswerCorrect = false
        }
    }
}