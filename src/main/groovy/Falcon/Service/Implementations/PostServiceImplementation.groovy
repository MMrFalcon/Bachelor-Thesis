package Falcon.Service.Implementations

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Model.UserDTO
import Falcon.Persist.Post
import Falcon.Persist.Tags
import Falcon.Persist.User
import Falcon.Repository.PostRepository
import Falcon.Repository.UserRepository
import Falcon.Service.PostService
import Falcon.Service.TagsService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import javax.transaction.Transactional

@Service
class PostServiceImplementation extends BaseServiceImplementation<Post, Long, PostRepository> implements PostService {

    private PostRepository postRepository
    private UserRepository userRepository
    private TagsService tagsService

    PostServiceImplementation(PostRepository postRepository, UserRepository userRepository, TagsService tagsService) {
        this.postRepository = postRepository
        this.userRepository = userRepository
        this.tagsService = tagsService
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')") //FIXME
    PostRepository getRepository() { postRepository }

    @Override
    PostDTO createPost(PostDTO postDTO, UserDTO userDTO) {
        Post postEntity = Mapper.dtoToPost(postDTO)
        Optional<User> optionalUser = userRepository.findById(userDTO.getId())

        if (optionalUser.isPresent()) {
            postEntity.setUser(optionalUser.get())
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
            List<Tags> tags =  new ArrayList<>()
            tagsDTOs.each {
                TagsDTO tagsDTO ->
                        println("Saving tag \'${tagsDTO.getTag()}\'")
                        Tags tag = tagsService.saveAndFlush(Mapper.dtoToTags(tagsDTO))
                        println("Tag value after flashing: \'${tag.getTag()}\'")
                        tags.add(tag)

            }


            postEntity.setTags(tags)
            saveAndFlush(postEntity)
            println("List of tags size after flashing Post entity: ${postEntity.getTags().size()}")

            return Mapper.postToDto(postEntity)

        }

    }

    @Transactional
    @Override
    PostDTO updatePostTags(Long postId, Set<TagsDTO> tagsDTOs) {

        Post postEntity = getOne(postId)

        if (postId == null)
            throw new NullPointerException("Bad ID!")

        if (!tagsDTOs && tagsDTOs.size() == 0)
            return Mapper.postToDto(postEntity)

        List<Tags> tags =  new ArrayList<>()
        tagsDTOs.each {
            TagsDTO tagsDTO ->

                    println("Saving tag \'${tagsDTO.getTag()}\'")
                    Tags tag = tagsService.saveAndFlush(Mapper.dtoToTags(tagsDTO))
                    println("Tag value after flashing: \'${tag.getTag()}\'")
                    tags.add(tag)

        }

        postEntity.getTags().clear()
        postEntity.setTags(tags)
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


}
