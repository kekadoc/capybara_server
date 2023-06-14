package com.kekadoc.project.capybara.server.routing.api.auth.model.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequestInfo
import com.kekadoc.project.capybara.server.routing.api.auth.model.RegistrationRequestInfoDto
import com.kekadoc.project.capybara.server.routing.api.auth.model.RegistrationStatusDto

object RegistrationRequestInfoDtoFactory :
    Factory.Single<RegistrationRequestInfo, RegistrationRequestInfoDto> {

    override fun create(
        value: RegistrationRequestInfo,
    ): RegistrationRequestInfoDto = RegistrationRequestInfoDto(
        id = value.id,
        status = RegistrationStatusDto.valueOf(value.status.name),
        name = value.name,
        surname = value.surname,
        patronymic = value.patronymic,
        email = value.email,
        isStudent = value.isStudent,
        groupId = value.groupId,
    )

}