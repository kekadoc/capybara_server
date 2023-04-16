package com.kekadoc.project.capybara.server.routing.api.schedule

import com.kekadoc.project.capybara.model.ScheduleSource
import io.ktor.server.routing.*

fun Routing.schedule() {
    get("/") { homeResponse() }
    get("/pnipu") { homePNIPUResponse() }
    get("/pnipu/fulltime") { parseScheduleResponse(ScheduleSource.CHF_PNIPU_FULLTIME) }
    get("/pnipu/extramural") { parseScheduleResponse(ScheduleSource.CHF_PNIPU_EXTRAMURAL) }
    get("/pnipu/evening") { parseScheduleResponse(ScheduleSource.CHF_PNIPU_EVENING) }
    post("/file") { parseScheduleResponse(ScheduleSource.FILE) }
}