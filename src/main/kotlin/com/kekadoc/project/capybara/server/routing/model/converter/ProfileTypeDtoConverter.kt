package com.kekadoc.project.capybara.server.routing.model.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.domain.model.Profile
import com.kekadoc.project.capybara.server.routing.model.profile.ProfileTypeDto

object ProfileTypeDtoConverter : Converter.Bidirectional<ProfileTypeDto, Profile.Type> {

    override fun convert(value: ProfileTypeDto): Profile.Type = when (value) {
        ProfileTypeDto.USER -> Profile.Type.USER
        ProfileTypeDto.SPEAKER -> Profile.Type.SPEAKER
        ProfileTypeDto.ADMIN -> Profile.Type.ADMIN
    }

    override fun revert(value: Profile.Type): ProfileTypeDto = when (value) {
        Profile.Type.USER -> ProfileTypeDto.USER
        Profile.Type.SPEAKER -> ProfileTypeDto.SPEAKER
        Profile.Type.ADMIN -> ProfileTypeDto.ADMIN
        Profile.Type.DEFAULT -> ProfileTypeDto.USER
    }

}