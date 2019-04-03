package Falcon.Service.Implementations

import Falcon.Model.PostDTO
import Falcon.Model.UserDTO
import Falcon.Persist.Post
import Falcon.Persist.User
import Falcon.Repository.PostRepository
import Falcon.Repository.UserRepository
import Falcon.Service.PostService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class PostServiceImplementation extends BaseServiceImplementation<Post, Long, PostRepository> implements PostService {

    private PostRepository postRepository
    private UserRepository userRepository

    PostServiceImplementation(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository
        this.userRepository = userRepository
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
            return Mapper.postToDto(save(postEntity))
        }else {
            throw new UsernameNotFoundException("User name is missing")
        }



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

        return Mapper.postToDto(saveAndFlush(postEntity))
    }

}
