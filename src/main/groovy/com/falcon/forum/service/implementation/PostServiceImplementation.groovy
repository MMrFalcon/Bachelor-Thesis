package com.falcon.forum.service.implementation

import com.falcon.forum.exception.PostNotFoundException
import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.TagsDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.Comments
import com.falcon.forum.persist.Post
import com.falcon.forum.persist.Tags
import com.falcon.forum.persist.User
import com.falcon.forum.repository.PostRepository
import com.falcon.forum.service.CommentsService
import com.falcon.forum.service.PostService
import com.falcon.forum.service.TagsService
import com.falcon.forum.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import java.time.LocalDate

@Slf4j
@Service
class PostServiceImplementation extends BaseServiceImplementation<Post, Long, PostRepository> implements PostService {

    private final PostRepository postRepository
    private final UserService userService
    private final TagsService tagsService
    private final CommentsService commentsService

    protected PostServiceImplementation(UserService userService, @Lazy TagsService tagsService,
                                        PostRepository postRepository, @Lazy CommentsService commentsService) {
        this.userService = userService
        this.tagsService = tagsService
        this.postRepository = postRepository
        this.commentsService = commentsService
        super.setRepository(postRepository)
    }


    @Override
    PostDTO createPost(PostDTO postDTO, UserDTO userDTO) {
        Post postEntity = Mapper.dtoToPost(postDTO)
        User user = userService.getOne(userDTO.getId())
        if (user) {
            log.info("User ${user.getUsername()} starts creation of new Post")
            postEntity.setUser(user)
        } else {
            throw new UsernameNotFoundException("User name is missing")
        }
        log.info("Post entity is ready for Tagging - ${postEntity}")
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
                        log.info("Adding Post ${postEntity} to existing tag ${tag}")
                        postEntity.getTags().add(tag)
                        saveAndFlush(postEntity)
                    } else {
                        Tags tag = tagsService.saveAndFlush(Mapper.dtoToTags(tagsDTO))
                        log.info("Adding tag - ${tag} to post: ${postEntity}")
                        postEntity.getTags().add(tag)
                        saveAndFlush(postEntity)
                    }


            }

            saveAndFlush(postEntity)
            log.info("Tags list size after flashing Post entity: ${postEntity.getTags().size()}")

            return Mapper.postToDto(postEntity)

        }

    }

    @Override
    PostDTO updatePostTags(Long postId, Set<TagsDTO> tagsDTOs) {

        Post postEntity = getOne(postId)

        if (postEntity == null)
            throw new PostNotFoundException("Cannot find post with id ${postId}")

        if (!tagsDTOs && tagsDTOs.size() == 0)
            return Mapper.postToDto(postEntity)

        log.info("Tags set size before update post: ${postEntity.getTags().size()}")
        postEntity.getTags().clear()
        saveAndFlush(postEntity)
        tagsDTOs.each {
            TagsDTO tagsDTO ->
                if (tagsService.isPresent(tagsDTO.getTag())) {
                    log.info("Adding Post to existing tag")
                    Tags tag = tagsService.getTagEntityByName(tagsDTO.getTag())
                    postEntity.getTags().add(tag)
                    saveAndFlush(postEntity)
                } else {
                    Tags tag = tagsService.saveAndFlush(Mapper.dtoToTags(tagsDTO))
                    log.info("Adding tag to post: ${postEntity}")
                    postEntity.getTags().add(tag)
                    saveAndFlush(postEntity)
                }

        }

        saveAndFlush(postEntity)
        log.info("Tags set size after flashing Post entity: ${postEntity.getTags().size()}")

        return Mapper.postToDto(postEntity)
    }

    @Override
    boolean isDeletable(PostDTO postDTO, String username) {
        Post post = postRepository.getOne(postDTO.getId())
        if (post.getUser().getUsername() != username || post.getComments().size() > 0) {
            return false
        }
        return true
    }

    @Override
    boolean isEditable(PostDTO postDTO, String username) {
        Post post = postRepository.getOne(postDTO.getId())
        if (post.getUser().getUsername() != username) {
            return false
        }
        return true
    }

    @Override
    boolean isAuthor(PostDTO postDTO, String username) {
        Post post = postRepository.getOne(postDTO.getId())
        if (post.getUser().getUsername() == username) {
            return true
        }
        return false
    }

    @Override
    String getAuthorName(PostDTO postDTO) {
        String authorName = postRepository.getOne(postDTO.getId()).getUser().getUsername()
        return authorName
    }

    @Override
    PostDTO getPostDtoById(Long id) {
        log.info("Searching for post with id ${id}")
        Post post = getOne(id)
        return Mapper.postToDto(post)
    }

    @Override
    PostDTO getPostByAnswer(CommentsDTO commentsDTO) {
        Comments comments = commentsService.getOne(commentsDTO.getId())
        PostDTO post = Mapper.postToDto(comments.getPost())
        return post
    }

    @Override
    PostDTO updatePost(Long id, PostDTO postDTO) {

        log.info("Searching for post with id ${id}")
        Post postEntity = getOne(id)

        if (postEntity == null)
            throw new PostNotFoundException("Cannot find post!")

        if (postDTO == null)
            throw new PostNotFoundException("Cannot find post!")

        postEntity.setTitle(postDTO.getTitle())
        postEntity.setContent(postDTO.getContent())
        postEntity.setUpdatedDate(LocalDate.now())

        Post savedPost = save(postEntity)
        log.info("Post with id ${id} successfully updated")
        return Mapper.postToDto(savedPost)
    }

}
