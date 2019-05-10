package com.falcon.forum.service

import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.TagsDTO
import com.falcon.forum.persist.Tags
import com.falcon.forum.repository.TagsRepository

interface TagsService extends BaseService <Tags, Long, TagsRepository> {
        Set<TagsDTO> generateTags(String tags, PostDTO postDTO)
        String getPostTagsAsString(Long postId)
        void removeTagsFromPost(PostDTO postDTO)
        TagsDTO getTagByName(String tag)
        TagsDTO getTagById(Long id)
        Tags getTagEntityByName(String tag)
        boolean isPresent(String tag)
}