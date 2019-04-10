package Falcon.Service

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Persist.Tags
import Falcon.Repository.TagsRepository

interface TagsService extends BaseService <Tags, Long, TagsRepository> {
        Set<TagsDTO> generateTags(String tags, PostDTO postDTO)
        String getPostTagsAsString(Long postId)
        void removeTagsFromPost(PostDTO postDTO)
        TagsDTO getTagByName(String tag)
        Tags getTagById(Long id)
}