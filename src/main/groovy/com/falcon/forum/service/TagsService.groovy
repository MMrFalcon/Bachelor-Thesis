package com.falcon.forum.service


import com.falcon.forum.model.TagsDTO
import com.falcon.forum.persist.Tags
import com.falcon.forum.repository.TagsRepository

interface TagsService extends BaseService <Tags, Long, TagsRepository> {
        Set<TagsDTO> generateTags(String tags)
        String getPostTagsAsString(Long postId)
        TagsDTO getTagByName(String tag)
        Tags getTagEntityByName(String tag)
        boolean isPresent(String tag)
}