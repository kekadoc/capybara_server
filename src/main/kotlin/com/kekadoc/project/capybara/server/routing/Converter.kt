package com.kekadoc.project.capybara.server.routing

import com.kekadoc.project.capybara.model.Schedule
import com.kekadoc.project.capybara.model.ScheduleBoard
import com.kekadoc.project.capybara.model.ScheduleMeta
import com.kekadoc.project.capybara.model.ScheduleSource
import com.kekadoc.project.capybara.server.Server

internal suspend fun Schedule.toBoard(source: ScheduleSource): ScheduleBoard {
    val meta = ScheduleMeta(source = source, timeOfCreation = Server.getTime())
    return ScheduleBoard(meta = meta, schedule = this)
}