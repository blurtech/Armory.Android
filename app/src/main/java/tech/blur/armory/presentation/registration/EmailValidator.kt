package tech.blur.armory.presentation.registration

import android.util.Patterns.EMAIL_ADDRESS


class EmailFormatValidator {
    companion object {
        fun validateEmailFormat(email: String): EmailValidationResult {
            return when {
                email == "" -> EmailValidationResult.EMPTY
                !Regex(EMAIL_ADDRESS.pattern()).matches(email) -> EmailValidationResult.INVALID_FORMAT
                else -> EmailValidationResult.VALID
            }
        }
    }

    enum class EmailValidationResult {
        VALID,
        INVALID_FORMAT,
        EMPTY
    }
}
