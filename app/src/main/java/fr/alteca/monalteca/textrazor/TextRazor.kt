package fr.alteca.monalteca.textrazor

import android.content.Context
import fr.alteca.monalteca.textrazor.model.Entity
import fr.alteca.monalteca.textrazor.model.TextRazorResponse

class TextRazor(val context: Context) {
    companion object {

        fun responseTreatment(textRazorResponse: TextRazorResponse): List<Entity> {
            return if (textRazorResponse.ok) {
                textRazorResponse.response.entities

            } else emptyList()
        }


        enum class EntityType {
            Company,
            Person,
            Place,
            PopulatedPlace,
            Settlement,
            Number

        }

        class NerCouple(val type: String, val text: String)

    }
}