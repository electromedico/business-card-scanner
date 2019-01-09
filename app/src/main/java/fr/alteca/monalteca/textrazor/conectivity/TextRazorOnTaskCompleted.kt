package fr.alteca.monalteca.textrazor.conectivity

import fr.alteca.monalteca.textrazor.model.TextRazorResponse

interface TextRazorOnTaskCompleted {
    fun onTaskCompletedGet(textRazorResponse: TextRazorResponse)
}