package com.kekadoc.project.capybara.server.data.source.converter.dto

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.source.network.model.ProfileDto

object ProfileDtoConverter : Converter.Bidirectional<Profile, ProfileDto> {

    override fun convert(source: ProfileDto): Profile {
        return Profile(
            type = TypeConverter.revert(source.type),
            name = source.name,
            surname = source.surname,
            patronymic = source.patronymic,
            avatar = source.avatar,
            role = source.role,
            about = source.about,
        )
    }

    override fun revert(target: Profile): ProfileDto {
        return ProfileDto(
            type = TypeConverter.convert(target.type),
            name = target.name,
            surname = target.surname,
            patronymic = target.patronymic,
            avatar = target.avatar,
            role = target.role,
            about = target.about,
        )
    }

    object TypeConverter : Converter.Bidirectional<ProfileDto.Type, Profile.Type> {

        override fun convert(source: Profile.Type): ProfileDto.Type {
            return when (source) {
                Profile.Type.USER -> ProfileDto.Type.USER
                Profile.Type.SPEAKER -> ProfileDto.Type.SPEAKER
                Profile.Type.ADMIN -> ProfileDto.Type.ADMIN
                Profile.Type.DEFAULT -> ProfileDto.Type.DEFAULT
            }
        }

        override fun revert(target: ProfileDto.Type): Profile.Type {
            return when (target) {
                ProfileDto.Type.USER -> Profile.Type.USER
                ProfileDto.Type.SPEAKER -> Profile.Type.SPEAKER
                ProfileDto.Type.ADMIN -> Profile.Type.ADMIN
                ProfileDto.Type.DEFAULT -> Profile.Type.DEFAULT
            }
        }

    }

}