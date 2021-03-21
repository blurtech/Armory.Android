package tech.blur.armory.presentation.book

import tech.blur.armory.presentation.common.Action
import tech.blur.armory.presentation.common.ViewState

class BookViewState : ViewState {
    val onBookCompleted = Action<Unit>()
    val requestToken = Action<Unit>()
    val onInviteFailed = Action<Unit>()
    val onInviteSucceed = Action<Person>()
    val onError = Action<Exception>()
}