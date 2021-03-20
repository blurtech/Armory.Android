package tech.blur.armory.domain.commands

import tech.blur.armory.data.storages.UserStorage

class LogOutCommand(private var userStorage: UserStorage) {
    suspend fun logOut() {
        userStorage.clear()
    }
}