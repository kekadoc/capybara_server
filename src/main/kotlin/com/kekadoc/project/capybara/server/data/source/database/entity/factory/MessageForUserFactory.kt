package com.kekadoc.project.capybara.server.data.source.database.entity.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.data.source.database.entity.MessageForUserEntity
import com.kekadoc.project.capybara.server.domain.model.message.MessageForUser

object MessageForUserFactory : Factory.Single<MessageForUserEntity, MessageForUser> {

    override fun create(value: MessageForUserEntity): MessageForUser = MessageForUser(
        id = value.id.value,
        messageId = value.message.id.value,
        userId = value.userId.id.value,
        received = value.received,
        read = value.read,
        answerIds = value.answerIds?.map(String::toLong),
        fromGroup = value.asGroupMember != null,
    )

}