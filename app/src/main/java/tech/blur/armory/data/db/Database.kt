package tech.blur.armory.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.blur.armory.data.db.dao.UserDao
import tech.blur.armory.data.db.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun userDao(): UserDao
}