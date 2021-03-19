package tech.blur.armory.domain.commands

import android.content.Context
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.CalendarScopes
import tech.blur.armory.data.providers.ResourceProvider
import java.util.*


class GoogleCalendarCommand(
    private val resourceProvider: ResourceProvider,
    private val context: Context
) {

    private val jsonFactory = GsonFactory()

    private val scopes: List<String> = Collections.singletonList(CalendarScopes.CALENDAR_READONLY)

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private fun getCredentials(token: String): Credential? {
        return GoogleCredential().setAccessToken(token)
    }

//    /**
//     * Creates an authorized Credential object.
//     * @param HTTP_TRANSPORT The network HTTP Transport.
//     * @return An authorized Credential object.
//     * @throws IOException If the credentials.json file cannot be found.
//     */
//    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential? {
//
//    }

    fun authorize(token: String) {
        val credential = getCredentials(token)
    }

    companion object {
        private const val APPLICATION_NAME = "Google Calendar API Java Quickstart"
        private const val TOKENS_DIRECTORY_PATH = "google-credentials"
    }
}