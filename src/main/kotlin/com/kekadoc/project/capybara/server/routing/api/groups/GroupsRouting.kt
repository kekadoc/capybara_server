package com.kekadoc.project.capybara.server.routing.api.groups

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.authToken
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.intercator.groups.GroupsInteractor
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.routing.api.groups.model.*
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

    get("/students") { getStudentGroups() }

    get("/all") { getAllWithMembersGroups() }

    //Создание группы
    post<CreateGroupRequestDto> { request -> createGroup(request) }

    //Получить список всех групп
    post<GetGroupListRequestDto>("/list") { request -> getGroupList(request) }

    //Получить список всех групп
    post<GetGroupWithMembersListRequestDto>("/list/extended") { request -> getGroupWithMembersList(request) }

    route("/{id}") {

        //Получить детализацию группы
        get { getContact(requirePathId()) }

        //Обновление данных группы
        patch<UpdateGroupNameRequestDto>("/name") { request ->
            updateGroupName(requirePathId(), request)
        }

        //Добавление участников группы
        patch<UpdateGroupMembersRequestDto>("/member/add") { request ->
            addMembersToGroup(requirePathId(), request)
        }

        //Удаление участников группы
        patch<UpdateGroupMembersRequestDto>("/member/delete") { request ->
            removeMembersFromGroup(requirePathId(), request)
        }

        //Удаление группы
        delete { deleteGroup(requirePathId()) }

        get("/members") { getGroupMembers(groupId = requirePathId()) }

    }

}

private suspend fun PipelineContext.getStudentGroups(

) = execute(ApiKeyVerifier) {
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.getStudentGroups()
    call.respond(result)
}

private suspend fun PipelineContext.getAllWithMembersGroups(

) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.getAllGroupsWithMembers(authToken)
    call.respond(result)
}

private suspend fun PipelineContext.createGroup(
    request: CreateGroupRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
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

private suspend fun PipelineContext.getGroupWithMembersList(
    request: GetGroupWithMembersListRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val interactor = Di.get<GroupsInteractor>()
    val result = interactor.getGroupsWithMembers(
        authToken = authToken,
        groupIds = request.ids.map(UUID::fromString),
    )
    call.respond(result)
}

private suspend fun PipelineContext.getContact(
    groupId: Identifier,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
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
    request: UpdateGroupNameRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
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
    request: UpdateGroupMembersRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
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
    request: UpdateGroupMembersRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
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
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
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