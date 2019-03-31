package Falcon.Service

import Falcon.Model.PostDTO
import Falcon.Persist.Post
import Falcon.Repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class PostServiceImplementation extends BaseServiceImplementation<Post, Long, PostRepository> implements PostService {

    @Autowired
    private PostRepository postRepository

    @Override
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')") //FIXME
    PostRepository getRepository() { postRepository }

    @Override
    PostDTO createPost(PostDTO postDTO) {
        Post postEntity = save(Mapper.dtoToPost(postDTO))

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

        return Mapper.postToDto(saveAndFlush(postEntity))
    }

}
