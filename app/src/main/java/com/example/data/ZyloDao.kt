package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ZyloDao {
  // Users
  @Query("SELECT * FROM users")
  fun getAllUsers(): Flow<List<UserEntity>>

  @Query("SELECT * FROM users WHERE isCurrent = 1 LIMIT 1")
  fun getCurrentUser(): Flow<UserEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserEntity)

  @Update
  suspend fun updateUser(user: UserEntity)

  // Posts
  @Query("SELECT * FROM posts ORDER BY timestamp DESC")
  fun getAllPosts(): Flow<List<PostEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPost(post: PostEntity)

  @Update
  suspend fun updatePost(post: PostEntity)

  @Query("DELETE FROM posts WHERE id = :postId")
  suspend fun deletePost(postId: Int)

  // Comments
  @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
  fun getCommentsForPost(postId: Int): Flow<List<CommentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertComment(comment: CommentEntity)

  // Conversations
  @Query("SELECT * FROM conversations ORDER BY timestamp DESC")
  fun getAllConversations(): Flow<List<ChatConversationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertConversation(conversation: ChatConversationEntity)

  // Messages
  @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
  fun getMessagesForChat(chatId: Int): Flow<List<MessageEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessage(message: MessageEntity)

  // Communities
  @Query("SELECT * FROM communities")
  fun getAllCommunities(): Flow<List<CommunityEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCommunity(community: CommunityEntity)

  @Update
  suspend fun updateCommunity(community: CommunityEntity)

  // Notifications
  @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
  fun getAllNotifications(): Flow<List<NotificationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotification(notification: NotificationEntity)

  @Query("UPDATE notifications SET isRead = 1")
  suspend fun markAllNotificationsAsRead()
}
