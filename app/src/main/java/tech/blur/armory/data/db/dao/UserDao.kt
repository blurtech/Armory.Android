package tech.blur.armory.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import tech.blur.armory.data.db.entities.UserEntity
import tech.blur.armory.domain.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM userentity LIMIT 1")
    suspend fun getUser(): User

    @Query("SELECT token FROM userentity LIMIT 1")
    suspend fun getAccessToken(): String

    @Query("SELECT COUNT(id) <> 0 FROM userentity")
    suspend fun isUserExists(): Boolean

    @Insert
    suspend fun insert(user: UserEntity)

    @Query("DELETE FROM userentity")
    suspend fun delete()
}