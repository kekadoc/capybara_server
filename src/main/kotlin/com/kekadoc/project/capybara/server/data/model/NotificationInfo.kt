package com.kekadoc.project.capybara.server.data.model

data class NotificationInfo(
    val notification: Notification,
    val addresseeGroups: List<Group>,
    val addresseeUsers: List<FromUserInfo>,
    val status: Status,
) {

    data class FromUserInfo(
        val user: User,
        val received: Boolean,
        val read: Boolean,
        val answer: String,
    )

    enum class Status {
        UNDEFINED,
        /**
         * Принято сервером
         */
        RECEIVED,
        /**
         * Отправлено
         */
        SENT,
        /**
         * Отменено
         */
        CANCELED,
        /**
         * Заблокировано сервером
         */
        REJECTED,
        /**
         * Ошибка отправки
         */
        FAILED
    }

}