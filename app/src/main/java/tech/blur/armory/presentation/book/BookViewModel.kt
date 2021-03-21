package tech.blur.armory.presentation.book

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.soywiz.klock.*
import kotlinx.coroutines.launch
import tech.blur.armory.data.services.booking.BookRequest
import tech.blur.armory.data.services.booking.BookingApi
import tech.blur.armory.data.services.user.UserApi
import tech.blur.armory.presentation.common.StateViewModel

class BookViewModel(
    private val sharedPreferences: SharedPreferences,
    private val userApi: UserApi,
    private val bookingApi: BookingApi,
    private val meetingRoomId: Int
) : StateViewModel<BookViewState>(BookViewState()) {
    private val persons = mutableListOf<Person>()
    var token: String?
        get() = sharedPreferences.getString("token", null)
        set(value) {
            sharedPreferences.edit()
                .putString("token", value)
                .apply()
        }


    init {
        if (token == null) {
            stateModel = requireStateModel().apply {
                requestToken.set(Unit)
            }
        }
    }

    fun book(
        name: String,
        startTime: DateTimeTz,
        startDate: Date,
        duration: Int
    ) {
        val start = DateTime(startDate, startTime.utc.time).toOffset(0.0.milliseconds)
        val end = start + duration.toDouble().minutes
        viewModelScope.launch {
            bookingApi.book(
                BookRequest(
                    name,
                    start,
                    end,
                    meetingRoomId,
                    persons.map { it.email }
                ),
                token!!
            ).onSuccess {
                stateModel = requireStateModel().apply {
                    onBookCompleted.set(Unit)
                }

            }.onFailure {
                stateModel = requireStateModel().apply {
                    onError.set(it)
                }
            }
        }

    }

    fun inviteUser(email: String) {
        viewModelScope.launch {
            userApi.getUserByEmail(email).onSuccess {
                stateModel = requireStateModel().apply {
                    onInviteSucceed.set(it.let {
                        Person(
                            "${it.lastName} ${it.firstName.first()}.",
                            it.email
                        )
                    }.also {
                        persons.add(it)
                    })
                }
            }.onFailure {
                stateModel = requireStateModel().apply {
                    onInviteFailed.set(Unit)
                }
            }
        }
    }

    fun cancelInvite(person: Person) {
        persons.remove(person)
    }
}