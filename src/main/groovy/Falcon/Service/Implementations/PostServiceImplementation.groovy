package Falcon.Service.Implementations

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Model.UserDTO
import Falcon.Persist.Post
import Falcon.Persist.Tags
import Falcon.Persist.User
import Falcon.Repository.PostRepository
import Falcon.Service.PostService
import Falcon.Service.TagsService
import Falcon.Service.UserService
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class PostServiceImplementation extends BaseServiceImplementation<Post, Long, PostRepository> implements PostService {

    private final PostRepository postRepository
    private final UserService userService
    private final TagsService tagsService

    protected PostServiceImplementation(UserService userService, @Lazy TagsService tagsService, PostRepository postRepository) {
        this.userService = userService
        this.tagsService = tagsService
        this.postRepository = postRepository
        super.setRepository(postRepository)
    }


    @Override
    PostDTO createPost(PostDTO postDTO, UserDTO userDTO) {
        Post postEntity = Mapper.dtoToPost(postDTO)
        User user = userService.getOne(userDTO.getId())
        println("User ${user} starts creation of new Post")
        if (user) {
            postEntity.setUser(user)
        }else {
            throw new UsernameNotFoundException("User name is missing")
        }
        println("Post entity is ready for Tagging - ${postEntity}")
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
                    if (tagsService.isPresent(tagsDTO.getTag())) {
                        Tags tag = tagsService.getTagEntityByName(tagsDTO.getTag())
                        println("Adding Post ${postEntity} to existing tag ${tag}")
                        postEntity.getTags().add(tag)
                        saveAndFlush(postEntity)
                        tag.getPosts().add(postEntity)
                        tagsService.saveAndFlush(tag)
                    }else {
                        Tags tag = tagsService.saveAndFlush(Mapper.dtoToTags(tagsDTO))
                        println("Adding tag - ${tag} to post: ${postEntity}")
                        postEntity.getTags().add(tag)
                        saveAndFlush(postEntity)
                        tag.getPosts().add(postEntity)
                        tagsService.saveAndFlush(tag)
                    }


            }

            saveAndFlush(postEntity)
            println("List of tags size after flashing Post entity: ${postEntity.getTags().size()}")

            return Mapper.postToDto(postEntity)

        }

    }

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
                if (tagsService.isPresent(tagsDTO.getTag())) {
                    println("Adding Post to existing tag")
                    Tags tag = tagsService.getTagEntityByName(tagsDTO.getTag())
                    postEntity.getTags().add(tag)
                    saveAndFlush(postEntity)
                    tag.getPosts().add(postEntity)
                    tagsService.saveAndFlush(tag)
                }else {
                    Tags tag = tagsService.saveAndFlush(Mapper.dtoToTags(tagsDTO))
                    println("Adding tag to post: ${postEntity}")
                    postEntity.getTags().add(tag)
                    saveAndFlush(postEntity)
                    tag.getPosts().add(postEntity)
                    tagsService.saveAndFlush(tag)
                }

        }

        saveAndFlush(postEntity)
        println("List of tags size after flashing update Post entity: ${postEntity.getTags().size()}")

        return Mapper.postToDto(postEntity)
    }

    @Override
    PostDTO getPostDtoById(Long id) {
        return Mapper.postToDto(getOne(id))
    }

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

}
