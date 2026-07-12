package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ChatConversationEntity
import com.example.data.CommentEntity
import com.example.data.CommunityEntity
import com.example.data.MessageEntity
import com.example.data.NotificationEntity
import com.example.data.PostEntity
import com.example.data.UserEntity
import com.example.data.ZyloRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ZyloViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: ZyloRepository

  init {
    val dao = AppDatabase.getDatabase(application).zyloDao()
    repository = ZyloRepository(dao)
    viewModelScope.launch {
      repository.seedInitialDataIfNeeded()
    }
  }

  val currentUser: StateFlow<UserEntity?> = repository.currentUser
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allPosts: StateFlow<List<PostEntity>> = repository.allPosts
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allConversations: StateFlow<List<ChatConversationEntity>> = repository.allConversations
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allCommunities: StateFlow<List<CommunityEntity>> = repository.allCommunities
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun getCommentsForPost(postId: Int): Flow<List<CommentEntity>> = repository.getCommentsForPost(postId)
  fun getMessagesForChat(chatId: Int): Flow<List<MessageEntity>> = repository.getMessagesForChat(chatId)

  fun createPost(content: String, imageUrl: String? = null) {
    viewModelScope.launch {
      val user = currentUser.value
      repository.insertPost(
        PostEntity(
          authorName = user?.name ?: "Alex Silva",
          authorHandle = user?.handle ?: "@alexsilva",
          authorAvatar = user?.avatarUrl ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop",
          content = content,
          imageUrl = imageUrl,
          likesCount = 0,
          commentsCount = 0,
          sharesCount = 0
        )
      )
    }
  }

  fun toggleLike(post: PostEntity) {
    viewModelScope.launch {
      val newLiked = !post.isLiked
      val newCount = if (newLiked) post.likesCount + 1 else maxOf(0, post.likesCount - 1)
      repository.updatePost(post.copy(isLiked = newLiked, likesCount = newCount))
    }
  }

  fun addComment(postId: Int, content: String) {
    viewModelScope.launch {
      val user = currentUser.value
      repository.insertComment(
        CommentEntity(
          postId = postId,
          authorName = user?.name ?: "Alex Silva",
          authorAvatar = user?.avatarUrl ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop",
          content = content
        )
      )
      // Update comment count on post
      val post = allPosts.value.find { it.id == postId }
      if (post != null) {
        repository.updatePost(post.copy(commentsCount = post.commentsCount + 1))
      }
    }
  }

  fun sendMessage(chatId: Int, messageText: String) {
    viewModelScope.launch {
      repository.insertMessage(
        MessageEntity(
          chatId = chatId,
          senderName = "Alex Silva",
          message = messageText,
          isMe = true
        )
      )
    }
  }

  fun toggleCommunityJoin(community: CommunityEntity) {
    viewModelScope.launch {
      val newJoined = !community.isJoined
      val newCount = if (newJoined) community.memberCount + 1 else maxOf(0, community.memberCount - 1)
      repository.updateCommunity(community.copy(isJoined = newJoined, memberCount = newCount))
    }
  }

  fun markNotificationsRead() {
    viewModelScope.launch {
      repository.markAllNotificationsAsRead()
    }
  }

  fun updateProfile(name: String, bio: String, avatarUrl: String) {
    viewModelScope.launch {
      val user = currentUser.value
      if (user != null) {
        repository.updateUser(user.copy(name = name, bio = bio, avatarUrl = avatarUrl))
      }
    }
  }

  fun login(name: String, handle: String) {
    viewModelScope.launch {
      val user = currentUser.value
      if (user != null) {
        repository.updateUser(user.copy(name = name, handle = handle, isCurrent = true))
      } else {
        repository.insertUser(
          UserEntity(
            name = name,
            handle = handle,
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop",
            bio = "Membro do Zylo Hub",
            followersCount = 10,
            followingCount = 15,
            isCurrent = true
          )
        )
      }
    }
  }
}
