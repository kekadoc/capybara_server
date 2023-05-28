package com.kekadoc.project.capybara.server.routing.api.groups

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.authToken
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.intercator.groups.GroupsInteractor
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.routing.api.groups.model.CreateGroupRequest
import com.kekadoc.project.capybara.server.routing.api.groups.model.GetGroupListRequestDto
import com.kekadoc.project.capybara.server.routing.api.groups.model.UpdateGroupMembersRequest
import com.kekadoc.project.capybara.server.routing.api.groups.model.UpdateGroupNameRequest
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.execution.delete
import com.kekadoc.project.capybara.server.routing.util.execution.get
import com.kekadoc.project.capybara.server.routing.util.execution.patch
import com.kekadoc.project.capybara.server.routing.util.execution.post
import com.kekadoc.project.capybara.server.routing.util.requirePathId
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.get
import java.util.*

fun Route.groups() = route("/groups") {

    get { getAllGroups() }

    //Создание группы
    post<CreateGroupRequest>(ApiKeyVerifier, AuthorizationVerifier) { request -> createGroup(request) }

    //Создание группы
    post<GetGroupListRequestDto>("/list") { request -> getGroupList(request) }

    route("/{id}") {

        //Получить детализацию группы
        get(ApiKeyVerifier, AuthorizationVerifier) { getContact(requirePathId()) }

        //Обновление данных группы
        patch<UpdateGroupNameRequest>("/name", ApiKeyVerifier, AuthorizationVerifier) { request ->
            updateGroupName(requirePathId(), request)
        }

        //Добавление участников группы
        patch<UpdateGroupMembersRequest>("/member/add", ApiKeyVerifier, AuthorizationVerifier) { request ->
            addMembersToGroup(requirePathId(), request)
        }

        //Удаление участников группы
        patch<UpdateGroupMembersRequest>("/member/delete", ApiKeyVerifier, AuthorizationVerifier) { request ->
            removeMembersFromGroup(requirePathId(), request)
        }

        //Удаление группы
        delete(ApiKeyVerifier, AuthorizationVerifier) { deleteGroup(requirePathId()) }

        get("/members") {
            getGroupMembers(groupId = requirePathId())
        }

    }

}

private suspend fun PipelineContext.getAllGroups() {
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.getAllGroups()
    call.respond(result)
}

private suspend fun PipelineContext.createGroup(
    request: CreateGroupRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.createGroup(
        authToken = authToken,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getGroupList(
    request: GetGroupListRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.getGroups(
        authToken = authToken,
        groupIds = request.ids.map(UUID::fromString),
    )
    call.respond(result)
}

private suspend fun PipelineContext.getContact(
    groupId: Identifier,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.getGroup(
        authToken = authToken,
        groupId = groupId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateGroupName(
    groupId: Identifier,
    request: UpdateGroupNameRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.updateGroupName(
        authToken = authToken,
        groupId = groupId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.addMembersToGroup(
    groupId: Identifier,
    request: UpdateGroupMembersRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.addMembersToGroup(
        authToken = authToken,
        groupId = groupId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.removeMembersFromGroup(
    groupId: Identifier,
    request: UpdateGroupMembersRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.removeMembersFromGroup(
        authToken = authToken,
        groupId = groupId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deleteGroup(
    groupId: Identifier,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.deleteGroup(
        authToken = authToken,
        groupId = groupId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getGroupMembers(
    groupId: Identifier,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<GroupsInteractor>().getGroupMembers(
        authToken = authToken,
        groupId = groupId,
    )
}