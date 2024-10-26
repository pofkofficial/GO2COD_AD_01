package com.example.quizzie

import Question
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var nextButton: Button
    private lateinit var timerText: TextView

    private var currentQuestionIndex = 0
    private var score = 0
    private val timerDuration: Long = 30000 // 30 seconds for each question
    private lateinit var timer: CountDownTimer

    private val questions = arrayListOf(
        Question("What is the capital of France?", listOf("Berlin", "Paris", "Rome", "Madrid"), 1),
        Question("Which planet is known as the Red Planet?", listOf("Earth", "Mars", "Venus", "Jupiter"), 1),
        Question("Who wrote 'Hamlet'?", listOf("Shakespeare", "Dickens", "Tolstoy", "Homer"), 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionText = findViewById(R.id.questionText)
        radioGroup = findViewById(R.id.radioGroup)
        nextButton = findViewById(R.id.nextButton)
        timerText = findViewById(R.id.timerText)

        loadQuestion()

        nextButton.setOnClickListener {
            checkAnswer()
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                loadQuestion()
            } else {
                showResultDialog()
            }
        }
    }

    private fun loadQuestion() {
        // Cancel the timer if it has been initialized
        if (::timer.isInitialized) {
            timer.cancel()
        }
        startTimer(timerDuration)

        // Clear radio buttons
        radioGroup.removeAllViews()

        // Load the question
        val currentQuestion = questions[currentQuestionIndex]
        questionText.text = currentQuestion.questionText

        // Dynamically add radio buttons for each answer option
        for ((index, answer) in currentQuestion.answers.withIndex()) {
            val radioButton = RadioButton(this)
            radioButton.text = answer
            radioButton.id = index
            radioGroup.addView(radioButton)
        }

        // Change button text to "Done" on the last question
        if (currentQuestionIndex == questions.size - 1) {
            nextButton.text = "Done"
        } else {
            nextButton.text = "Next Question"
        }
    }



    private fun checkAnswer() {
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        if (selectedRadioButtonId != -1) {
            val correctAnswerIndex = questions[currentQuestionIndex].correctAnswerIndex
            if (selectedRadioButtonId == correctAnswerIndex) {
                score++
            }
        } else {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer(duration: Long) {
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "Time: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                checkAnswer()
                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    loadQuestion()
                } else {
                    showResultDialog()
                }
            }
        }.start()
    }

    private fun showResultDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Quiz Finished!")
        builder.setMessage("Your Score: $score/${questions.size}")

        builder.setPositiveButton("Try Again") { _, _ ->
            resetQuiz()
        }

        builder.setNegativeButton("Quit") { _, _ ->
            finish() // Ends the activity
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun resetQuiz() {
        currentQuestionIndex = 0
        score = 0
        loadQuestion()
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
}
