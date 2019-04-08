package Falcon.Service

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Persist.Tags
import Falcon.Repository.TagsRepository

interface TagsService extends BaseService <Tags, Long, TagsRepository> {
        TagsDTO createTag(TagsDTO tagsDTO, PostDTO postDTO)
        Set<TagsDTO> generateTags(String tags, PostDTO postDTO)
        String getPostTagsAsString(Long postId)
        TagsDTO getTagByName(String tag)
        boolean isTagPresent(TagsDTO tag)
}