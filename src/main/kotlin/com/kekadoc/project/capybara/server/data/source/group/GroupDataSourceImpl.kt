package com.kekadoc.project.capybara.server.data.source.group

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
class GroupDataSourceImpl : GroupDataSource {
    
    private val database = FirebaseDatabase.getInstance()
    private val groups = database.getReference("/groups")
    
}