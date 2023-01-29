package com.kekadoc.project.capybara.server.routing

import com.kekadoc.project.capybara.model.ScheduleSource
import com.kekadoc.project.capybara.server.routing.api.v1.auth.authorization
import com.kekadoc.project.capybara.server.routing.api.v1.notification.authNotification
import com.kekadoc.project.capybara.server.routing.api.v1.notification.sendNotification
import com.kekadoc.project.capybara.server.routing.api.v1.profile.deleteProfile
import com.kekadoc.project.capybara.server.routing.api.v1.profile.getProfile
import com.kekadoc.project.capybara.server.routing.api.v1.profile.updateProfile
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() = routing {
    get("/") { homeResponse() }
    get("/pnipu") { homePNIPUResponse() }
    get("/pnipu/fulltime") { parseScheduleResponse(ScheduleSource.CHF_PNIPU_FULLTIME) }
    get("/pnipu/extramural") { parseScheduleResponse(ScheduleSource.CHF_PNIPU_EXTRAMURAL) }
    get("/pnipu/evening") { parseScheduleResponse(ScheduleSource.CHF_PNIPU_EVENING) }
    post("/file") { parseScheduleResponse(ScheduleSource.FILE) }
    
    //Авторизация пользователя или админа
    post("/api/v1/auth") { authorization() }
    
    //Получение профиля
    get("/api/v1/profile") { getProfile() }
    //Обновление профиля
    patch("/api/v1/profile") { updateProfile() }
    //Удаление профиля
    delete("/api/v1/profile") { deleteProfile() }
    
    //Подписка на пуши
    post("/api/v1/notification/auth") { authNotification() }
    //Отправка пуша
    post("/api/v1/notification/send") { sendNotification() }
    
    //Обновление статуса нотификации от получателя
    post("/api/v1/notification/status") { TODO() }
}