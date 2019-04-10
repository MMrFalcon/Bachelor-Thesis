package Falcon.Service.Implementations

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Persist.Post
import Falcon.Persist.Tags
import Falcon.Repository.TagsRepository
import Falcon.Service.PostService
import Falcon.Service.TagsService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class TagsServiceImplementation extends BaseServiceImplementation<Tags, Long, TagsRepository> implements  TagsService {

    private TagsRepository tagsRepository
    private PostService postService

    TagsServiceImplementation(TagsRepository tagsRepository, @Lazy PostService postService) {
        this.tagsRepository = tagsRepository
        this.postService = postService
    }

    @Override
    TagsRepository getRepository() {
        tagsRepository
    }

    @Override
    Tags saveAndFlush(Tags tagsEntity) {
        return tagsRepository.saveAndFlush(tagsEntity)
    }


    @Override
    Set<TagsDTO> generateTags(String tags, PostDTO postDTO) {
        Set<TagsDTO> tagsSet = new HashSet<>()
        
        def splittedTags = tags.split(" ")
        
        if (!splittedTags || splittedTags.size() == 0)
            throw new RuntimeException("Tags are empty")

        splittedTags.each {
                String tag ->
                    TagsDTO tagsDTO = new TagsDTO(tag)
                    tagsSet.add(tagsDTO)
        }

        return tagsSet
    }


    String getPostTagsAsString(Long postId) {
        Post post = postService.getOne(postId)
        def tagsString = ""

        post.getTags().each {
            Tags tags ->
                tagsString+= "${tags.getTag()} "
        }

        tagsString
    }

    @Override
    void removeTagsFromPost(PostDTO postDTO) {
        Post postEntity = postService.getOne(postDTO.getId())
        postEntity.getTags().each {
            Tags tag ->
                delete(tag.getId())
                saveAndFlush(tag)
        }
    }

    @Override
    TagsDTO getTagByName(String tag) {
        Optional<Tags> optionalTag = tagsRepository.findByTag(tag)
        if (optionalTag.isPresent())
            return Mapper.tagsToDTO(optionalTag.get())
        else
            return null
    }

    @Override
    Tags getTagById(Long id) {
        Optional<Tags> optionalTag = tagsRepository.findById(id)
        if (optionalTag.isPresent())
            return optionalTag.get()
        else
            return null
    }

}
