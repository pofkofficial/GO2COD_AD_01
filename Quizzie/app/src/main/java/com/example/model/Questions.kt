data class Question(
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val isMultipleChoice: Boolean = false
)
