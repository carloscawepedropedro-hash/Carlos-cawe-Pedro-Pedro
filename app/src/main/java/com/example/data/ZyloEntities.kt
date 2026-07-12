package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val name: String,
  val handle: String,
  val avatarUrl: String,
  val bio: String,
  val followersCount: Int,
  val followingCount: Int,
  val isCurrent: Boolean = false
)

@Entity(tableName = "posts")
data class PostEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val authorName: String,
  val authorHandle: String,
  val authorAvatar: String,
  val content: String,
  val imageUrl: String? = null,
  val videoUrl: String? = null,
  val timestamp: Long = System.currentTimeMillis(),
  val likesCount: Int = 0,
  val commentsCount: Int = 0,
  val sharesCount: Int = 0,
  val isLiked: Boolean = false
)

@Entity(tableName = "comments")
data class CommentEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val postId: Int,
  val authorName: String,
  val authorAvatar: String,
  val content: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "conversations")
data class ChatConversationEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val participantName: String,
  val participantAvatar: String,
  val lastMessage: String,
  val timestamp: Long = System.currentTimeMillis(),
  val unreadCount: Int = 0
)

@Entity(tableName = "messages")
data class MessageEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val chatId: Int,
  val senderName: String,
  val message: String,
  val timestamp: Long = System.currentTimeMillis(),
  val isMe: Boolean = false
)

@Entity(tableName = "communities")
data class CommunityEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val name: String,
  val description: String,
  val memberCount: Int,
  val imageUrl: String,
  val isJoined: Boolean = false
)

@Entity(tableName = "notifications")
data class NotificationEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val title: String,
  val message: String,
  val timestamp: Long = System.currentTimeMillis(),
  val type: String, // "like", "comment", "follow", "chat"
  val isRead: Boolean = false
)
