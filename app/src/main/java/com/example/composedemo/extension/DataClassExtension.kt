package com.example.composedemo.extension

import com.example.composedemo.model.GitResponse
import com.example.composedemo.model.LocalData
fun List<GitResponse.Item>.toLocalList():List<LocalData>{

    return map {item ->
        LocalData(
            id = item.id,
            name = item.name+"",
            image = item.owner.avatar_url+"",
            description = item.description+"",
            projectLink = item.owner.repos_url+"",
            contributors = item.collaborators_url+""
        )

    }
}
