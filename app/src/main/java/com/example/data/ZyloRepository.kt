package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ZyloRepository(private val dao: ZyloDao) {
  val currentUser: Flow<UserEntity?> = dao.getCurrentUser()
  val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()
  val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()
  val allConversations: Flow<List<ChatConversationEntity>> = dao.getAllConversations()
  val allCommunities: Flow<List<CommunityEntity>> = dao.getAllCommunities()
  val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()

  fun getCommentsForPost(postId: Int): Flow<List<CommentEntity>> = dao.getCommentsForPost(postId)
  fun getMessagesForChat(chatId: Int): Flow<List<MessageEntity>> = dao.getMessagesForChat(chatId)

  suspend fun insertUser(user: UserEntity) = dao.insertUser(user)
  suspend fun updateUser(user: UserEntity) = dao.updateUser(user)

  suspend fun insertPost(post: PostEntity) = dao.insertPost(post)
  suspend fun updatePost(post: PostEntity) = dao.updatePost(post)
  suspend fun deletePost(postId: Int) = dao.deletePost(postId)

  suspend fun insertComment(comment: CommentEntity) = dao.insertComment(comment)
  suspend fun insertConversation(conversation: ChatConversationEntity) = dao.insertConversation(conversation)
  suspend fun insertMessage(message: MessageEntity) = dao.insertMessage(message)
  suspend fun updateCommunity(community: CommunityEntity) = dao.updateCommunity(community)
  suspend fun insertNotification(notification: NotificationEntity) = dao.insertNotification(notification)
  suspend fun markAllNotificationsAsRead() = dao.markAllNotificationsAsRead()

  suspend fun seedInitialDataIfNeeded() {
    val users = dao.getAllUsers().first()
    if (users.isEmpty()) {
      dao.insertUser(
        UserEntity(
          name = "Alex Silva",
          handle = "@alexsilva",
          avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop",
          bio = "Tech explorer, mobile developer & photography enthusiast 🚀✨",
          followersCount = 1420,
          followingCount = 380,
          isCurrent = true
        )
      )
      dao.insertUser(
        UserEntity(
          name = "Beatriz Costa",
          handle = "@biacosta",
          avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=500&auto=format&fit=crop",
          bio = "UI/UX Designer | Coffee addict ☕ | Lisbon & São Paulo",
          followersCount = 3210,
          followingCount = 512
        )
      )
      dao.insertUser(
        UserEntity(
          name = "Carlos Mendes",
          handle = "@carlosm",
          avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&auto=format&fit=crop",
          bio = "Startup founder & AI researcher.",
          followersCount = 890,
          followingCount = 190
        )
      )
    }

    val posts = dao.getAllPosts().first()
    if (posts.isEmpty()) {
      dao.insertPost(
        PostEntity(
          authorName = "Beatriz Costa",
          authorHandle = "@biacosta",
          authorAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=500&auto=format&fit=crop",
          content = "Explorando novos conceitos de design Material 3 para o Zylo Hub. O que acham dessa combinação de cores em tom índigo e violeta? 🎨✨",
          imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop",
          likesCount = 248,
          commentsCount = 35,
          sharesCount = 12,
          timestamp = System.currentTimeMillis() - 3600000 * 2
        )
      )
      dao.insertPost(
        PostEntity(
          authorName = "Carlos Mendes",
          authorHandle = "@carlosm",
          authorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&auto=format&fit=crop",
          content = "O futuro da IA no desenvolvimento mobile é impressionante! Cada vez mais rápido criar aplicativos completos direto na nuvem. 🚀🤖",
          imageUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=800&auto=format&fit=crop",
          likesCount = 512,
          commentsCount = 64,
          sharesCount = 41,
          timestamp = System.currentTimeMillis() - 3600000 * 5
        )
      )
      dao.insertPost(
        PostEntity(
          authorName = "Alex Silva",
          authorHandle = "@alexsilva",
          authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop",
          content = "Manhã perfeita para escrever código e tomar um bom café. Bom dia a todos os membros do Zylo Hub! ☕☀️",
          imageUrl = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=800&auto=format&fit=crop",
          likesCount = 189,
          commentsCount = 14,
          sharesCount = 5,
          timestamp = System.currentTimeMillis() - 3600000 * 10
        )
      )
    }

    val convos = dao.getAllConversations().first()
    if (convos.isEmpty()) {
      dao.insertConversation(
        ChatConversationEntity(
          id = 1,
          participantName = "Beatriz Costa",
          participantAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=500&auto=format&fit=crop",
          lastMessage = "Adorei a nova atualização do app! Ficou incrível.",
          timestamp = System.currentTimeMillis() - 100000,
          unreadCount = 2
        )
      )
      dao.insertConversation(
        ChatConversationEntity(
          id = 2,
          participantName = "Carlos Mendes",
          participantAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&auto=format&fit=crop",
          lastMessage = "Vamos marcar aquela reunião amanhã às 14h?",
          timestamp = System.currentTimeMillis() - 3600000,
          unreadCount = 0
        )
      )
      dao.insertConversation(
        ChatConversationEntity(
          id = 3,
          participantName = "Equipe Zylo Hub",
          participantAvatar = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=500&auto=format&fit=crop",
          lastMessage = "Bem-vindo ao Zylo Hub! Explore comunidades e compartilhe momentos.",
          timestamp = System.currentTimeMillis() - 86400000,
          unreadCount = 0
        )
      )
    }

    val messages = dao.getMessagesForChat(1).first()
    if (messages.isEmpty()) {
      dao.insertMessage(MessageEntity(chatId = 1, senderName = "Beatriz Costa", message = "Oi Alex! Tudo bem?", timestamp = System.currentTimeMillis() - 200000, isMe = false))
      dao.insertMessage(MessageEntity(chatId = 1, senderName = "Alex Silva", message = "Tudo ótimo por aqui e com você?", timestamp = System.currentTimeMillis() - 150000, isMe = true))
      dao.insertMessage(MessageEntity(chatId = 1, senderName = "Beatriz Costa", message = "Adorei a nova atualização do app! Ficou incrível.", timestamp = System.currentTimeMillis() - 100000, isMe = false))
    }

    val communities = dao.getAllCommunities().first()
    if (communities.isEmpty()) {
      dao.insertCommunity(CommunityEntity(name = "Tech & Inovação", description = "Comunidade para entusiastas de tecnologia, IA e desenvolvimento.", memberCount = 14500, imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=500&auto=format&fit=crop", isJoined = true))
      dao.insertCommunity(CommunityEntity(name = "Fotografia Urbana", description = "Compartilhe seus melhores cliques da cidade, arquitetura e estilo de vida.", memberCount = 8200, imageUrl = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=500&auto=format&fit=crop", isJoined = false))
      dao.insertCommunity(CommunityEntity(name = "Designers & Criativos", description = "Dicas de UI/UX, Figma, paletas de cores e inspiração visual.", memberCount = 11300, imageUrl = "https://images.unsplash.com/photo-1561070791-2526d30994b5?w=500&auto=format&fit=crop", isJoined = true))
    }

    val notifs = dao.getAllNotifications().first()
    if (notifs.isEmpty()) {
      dao.insertNotification(NotificationEntity(title = "Nova curtida", message = "Beatriz Costa curtiu sua publicação.", type = "like", isRead = false, timestamp = System.currentTimeMillis() - 1200000))
      dao.insertNotification(NotificationEntity(title = "Novo seguidor", message = "Carlos Mendes começou a seguir você.", type = "follow", isRead = false, timestamp = System.currentTimeMillis() - 7200000))
      dao.insertNotification(NotificationEntity(title = "Comentário", message = "Beatriz Costa comentou: 'Adorei!'", type = "comment", isRead = true, timestamp = System.currentTimeMillis() - 14400000))
    }
  }
}
