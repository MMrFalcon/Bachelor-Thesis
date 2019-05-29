package com.falcon.forum.service.implementation

import com.falcon.forum.exception.TagNotFoundException
import com.falcon.forum.model.TagsDTO
import com.falcon.forum.persist.Post
import com.falcon.forum.persist.Tags
import com.falcon.forum.repository.TagsRepository
import com.falcon.forum.service.PostService
import com.falcon.forum.service.TagsService
import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

import javax.persistence.EntityNotFoundException

@Slf4j
@Service
class TagsServiceImplementation extends BaseServiceImplementation<Tags, Long, TagsRepository> implements TagsService {

    private final TagsRepository tagsRepository
    private final PostService postService

    TagsServiceImplementation(TagsRepository tagsRepository, @Lazy PostService postService) {
        this.tagsRepository = tagsRepository
        this.postService = postService
        super.setRepository(tagsRepository)
    }

    @Override
    Set<TagsDTO> generateTags(String tags) {
        log.info("Generating tags...")
        Set<TagsDTO> tagsSet = new HashSet<>()

        def splittedTags = tags.split(" ")

        if (!splittedTags || splittedTags.size() == 0)
            throw new RuntimeException("Tags are empty")

        splittedTags.each {
            String tag ->
                TagsDTO tagsDTO = new TagsDTO(tag)
                tagsSet.add(tagsDTO)
        }
        log.info("${tagsSet.size()} tags after generation")
        return tagsSet
    }


    String getPostTagsAsString(Long postId) {
        log.info("Searching for post Tags...")
        Post post = postService.getOne(postId)
        def tagsString = ""

        post.getTags().toList().sort{a,b -> a.createdDate<=>b.createdDate}.each {
            Tags tags ->
                tagsString += "${tags.getTag()} "
        }
        log.info("${post.getTags().size()} tags was found")
        tagsString
    }


    @Override
    TagsDTO getTagByName(String tag) {
        log.info("Searching for tag ${tag}...")
        Optional<Tags> optionalTag = tagsRepository.findByTag(tag)
        if (optionalTag.isPresent())
            return Mapper.tagsToDTO(optionalTag.get())
        else
            throw new TagNotFoundException("Tag ${tag} is not present!")
    }

    @Override
    Tags getTagEntityByName(String tag) {
        log.info("Searching for tag entity with name ${tag}")
        Optional<Tags> optionalTag = tagsRepository.findByTag(tag)
        if (optionalTag.isPresent())
            return optionalTag.get()
        else
            throw new EntityNotFoundException("Tag ${tag} is not present!")
    }

    @Override
    boolean isPresent(String tag) {
        log.info("Checking tag ${tag}...")
        try {
            getTagByName(tag)
            log.info("Tag ${tag} was succesfully found")
            return true
        } catch (TagNotFoundException ex) {
            log.info(ex.getMessage())
            return false
        }
    }
}
