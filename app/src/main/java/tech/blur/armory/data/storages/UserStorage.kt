package tech.blur.armory.data.storages

import tech.blur.armory.data.db.dao.UserDao
import tech.blur.armory.data.db.entities.UserEntity
import tech.blur.armory.domain.models.MyUser

class UserStorage(private val userDao: UserDao) {
    suspend fun insert(user: MyUser) {
        with(user) {
            userDao.insert(
                UserEntity(id, email, name, surname, token)
            )
        }
    }

    suspend fun getUser(): MyUser {
        return userDao.getUser()
    }

    suspend fun getAccessToken(): String {
        return userDao.getAccessToken()
    }

    suspend fun clear() {
        userDao.delete()
    }

    suspend fun isUserExists(): Boolean {
        return userDao.isUserExists()
    }
}