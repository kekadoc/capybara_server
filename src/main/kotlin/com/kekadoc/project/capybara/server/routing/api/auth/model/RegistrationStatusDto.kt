package com.kekadoc.project.capybara.server.routing.api.auth.model

enum class RegistrationStatusDto {

    WAIT_EMAIL_CONFIRMING,
    WAIT_APPROVING,
    COMPLETED,
    REJECTED,
    CANCELLED,

}