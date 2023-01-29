package com.kekadoc.project.capybara.server.routing

import com.kekadoc.project.capybara.model.Schedule
import com.kekadoc.project.capybara.model.ScheduleBoard
import com.kekadoc.project.capybara.model.ScheduleSource
import com.kekadoc.project.capybara.parser.api.ScheduleApi
import com.kekadoc.project.capybara.parser.api.model.ScheduleFile
import com.kekadoc.project.capybara.parser.api.model.ScheduleOptions
import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.extensions.SYMBOL_DOT
import com.kekadoc.project.capybara.server.common.extensions.createTempFileSuspend
import com.kekadoc.project.capybara.server.common.extensions.getFileExtension
import com.kekadoc.project.capybara.server.di.Di
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.koin.core.component.get

internal suspend fun PipelineContext.parseScheduleResponse(
    source: ScheduleSource
) = execute {
    val scheduleApi = Di.get<ScheduleApi>()
    verificationApiKey()
    checkIsSourceAvailable(source)
    when (source) {
        ScheduleSource.FILE -> parseScheduleFile(scheduleApi)
        ScheduleSource.CHF_PNIPU_FULLTIME -> loadFromChFPNIPU(scheduleApi, source)
        ScheduleSource.CHF_PNIPU_EXTRAMURAL -> loadFromChFPNIPU(scheduleApi, source)
        ScheduleSource.CHF_PNIPU_EVENING -> loadFromChFPNIPU(scheduleApi, source)
    }
}


private suspend fun PipelineContext.loadFromChFPNIPU(
    api: ScheduleApi,
    source: ScheduleSource
) = execute {
    val schedule: ScheduleBoard = api.load(ScheduleOptions(source)).toBoard(source)
    call.respond(HttpStatusCode.OK, schedule)
}

private suspend fun PipelineContext.parseScheduleFile(api: ScheduleApi) = execute {
    val multipart = call.receiveMultipart()
    multipart.forEachPart { part ->
        when (part) {
            is PartData.BinaryItem -> { }
            is PartData.FormItem -> { }
            is PartData.FileItem -> {
                try {
                    val result = getScheduleFromPartDataFileItem(api, part).toBoard(ScheduleSource.FILE)
                    call.respond(HttpStatusCode.OK, result)
                } catch (e: Throwable) {
                    throw RuntimeException("Failed to process file: ${e.message}")
                }
            }
            else -> call.respond(HttpStatusCode.BadRequest)
        }
        part.dispose()
    }
}

private suspend fun getScheduleFromPartDataFileItem(api: ScheduleApi, fileItem: PartData.FileItem): Schedule {
    val fileName = fileItem.originalFileName as String
    val fileBytes = fileItem.streamProvider().readBytes()
    val scheduleFile = ScheduleFile.parse(fileName.getFileExtension())
    if (scheduleFile !in api.supportedFileTypes) {
        throw RuntimeException("Unsupported file: $fileName")
    }
    val file = createTempFileSuspend(fileName, "$SYMBOL_DOT${scheduleFile.extension}")
    file.writeBytes(fileBytes)
    return api.parseFromFile(file)
}