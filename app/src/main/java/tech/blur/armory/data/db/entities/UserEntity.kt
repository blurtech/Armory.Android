package tech.blur.armory.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val email: String,
    val name: String,
    val surname: String,
    val token: String
)