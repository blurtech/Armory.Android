package tech.blur.armory.domain.commands

import android.content.SharedPreferences
import tech.blur.armory.data.storages.UserStorage

class LogOutCommand(
    private var userStorage: UserStorage, private val sharedPreferences: SharedPreferences,
) {
    suspend fun logOut() {
        userStorage.clear()
        sharedPreferences.edit()
            .remove("token")
            .apply()
    }
}