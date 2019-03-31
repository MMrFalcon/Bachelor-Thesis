package Falcon.Service

import Falcon.Model.TagsDTO
import Falcon.Persist.Tags
import Falcon.Repository.TagsRepository

interface TagsService extends BaseService <Tags, Long, TagsRepository> {
    TagsDTO createTag(TagsDTO tagsDTO)
    void generateTags(def tags, Long postId)
    void updateTags(def oldTags, def newTags, Long postId)
    List<TagsDTO> getTagsByPostId(Long postId)
    void deleteTags(def tags)

}