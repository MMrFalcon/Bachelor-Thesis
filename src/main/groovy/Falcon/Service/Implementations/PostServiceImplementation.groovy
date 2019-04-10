package Falcon.Service.Implementations

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Model.UserDTO
import Falcon.Persist.BaseEntity
import Falcon.Persist.Post
import Falcon.Persist.User
import Falcon.Repository.PostRepository
import Falcon.Service.PostService
import Falcon.Service.TagsService
import Falcon.Service.UserService
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import javax.transaction.Transactional

@Service
class PostServiceImplementation extends BaseServiceImplementation<Post, Long, PostRepository> implements PostService {

    private PostRepository postRepository
    private UserService userService
    private TagsService tagsService

    PostServiceImplementation(UserService userService, @Lazy TagsService tagsService, PostRepository postRepository) {
        this.userService = userService
        this.tagsService = tagsService
        this.postRepository = postRepository
    }


    @Override
    PostDTO createPost(PostDTO postDTO, UserDTO userDTO) {
        Post postEntity = Mapper.dtoToPost(postDTO)
        User user = userService.getOne(userDTO.getId())

        if (user) {
            postEntity.setUser(user)
        }else {
            throw new UsernameNotFoundException("User name is missing")
        }

        return Mapper.postToDto(save(postEntity))
    }


    @Override
    PostDTO addPostTags(Long postId, Set<TagsDTO> tagsDTOs) {
        Post postEntity = getOne(postId)

        if (!tagsDTOs || tagsDTOs.size() == 0) {
            postRepository.delete(postEntity)
            postRepository.flush()
            throw new NullPointerException("Tags are empty")
        } else {

            tagsDTOs.each {
                TagsDTO tagsDTO ->
                        BaseEntity baseEntity = tagsService.saveAndFlush(Mapper.dtoToTags(tagsDTO))
                        println("Adding tag to post: ${tagsService.getTagById(baseEntity.getId())}")
                        postEntity.getTags().add(tagsService.getTagById(baseEntity.getId()))
                        saveAndFlush(postEntity)
            }

            saveAndFlush(postEntity)
            println("List of tags size after flashing Post entity: ${postEntity.getTags().size()}")

            return Mapper.postToDto(postEntity)

        }

    }

//    @Transactional
    @Override
    PostDTO updatePostTags(Long postId, Set<TagsDTO> tagsDTOs) {

        Post postEntity = getOne(postId)

        if (postId == null)
            throw new NullPointerException("Bad ID!")

        if (!tagsDTOs && tagsDTOs.size() == 0)
            return Mapper.postToDto(postEntity)

        tagsService.removeTagsFromPost(Mapper.postToDto(getOne(postId)))
        postEntity.getTags().clear()
        saveAndFlush(postEntity)
        tagsDTOs.each {
            TagsDTO tagsDTO ->
                BaseEntity baseEntity = tagsService.saveAndFlush(Mapper.dtoToTags(tagsDTO))
                println("Adding tag to post: ${tagsService.getTagById(baseEntity.getId())}")
                postEntity.getTags().add(tagsService.getTagById(baseEntity.getId()))
                saveAndFlush(postEntity)
        }

        saveAndFlush(postEntity)
        println("List of tags size after flashing Post entity: ${postEntity.getTags().size()}")

        return Mapper.postToDto(postEntity)
    }

    @Override
    PostDTO getPostDtoById(Long id) {
        return Mapper.postToDto(getOne(id))
    }

    @Transactional
    @Override
    PostDTO updatePost(Long id, PostDTO postDTO) {

        if (id == null)
            throw new NullPointerException("Bad ID!")

        Post postEntity = getOne(id)

        if (PostDTO == null)
            throw new NullPointerException("Empty model!")

        postEntity.setTitle(postDTO.getTitle())
        postEntity.setContent(postDTO.getContent())
        postEntity.setUpdatedDate(new Date())

        return Mapper.postToDto(save(postEntity))
    }

    @Override
    PostRepository getRepository() {
        return this.postRepository
    }
}
