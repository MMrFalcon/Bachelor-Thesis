package Falcon.Service.Implementations

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Persist.Post
import Falcon.Persist.Tags
import Falcon.Repository.PostRepository
import Falcon.Repository.TagsRepository
import Falcon.Service.TagsService
import org.springframework.stereotype.Service

@Service
class TagsServiceImplementation extends BaseServiceImplementation<Tags, Long, TagsRepository> implements  TagsService {

    private TagsRepository tagsRepository
    private PostRepository postRepository

    TagsServiceImplementation(TagsRepository tagsRepository, PostRepository postRepository) {
        this.tagsRepository = tagsRepository
        this.postRepository = postRepository
    }

    @Override
    TagsRepository getRepository() {
        tagsRepository
    }

    @Override
    TagsDTO createTag(TagsDTO tagsDTO, PostDTO postDTO) {
        Tags tagsEntity = Mapper.dtoToTags(tagsDTO)
        println("New tag for save: ${tagsDTO.getTag()}")

        tagsEntity.getPosts().add(Mapper.dtoToPost(postDTO))

        println("Tag Posts list size after adding: ${tagsEntity.getPosts().size()}")
        println("*************************")

        return Mapper.tagsToDTO(save(tagsEntity))
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
        Post post = postRepository.getOne(postId)
        def tagsString = ""

        post.getTags().each {
            Tags tags ->
                tagsString+= "${tags.getTag()} "
        }

        tagsString
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
    boolean isTagPresent(TagsDTO tagsDTO) {
        if (getTagByName(tagsDTO.getTag()))
            return true
        else
            return false
    }
}
