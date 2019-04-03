package Falcon.Service

import Falcon.Model.PostDTO
import Falcon.Model.UserDTO
import Falcon.Persist.Post
import Falcon.Repository.PostRepository

interface PostService extends  BaseService <Post, Long, PostRepository> {
    PostDTO createPost(PostDTO postDTO, UserDTO userDTO)
    PostDTO getPostDtoById(Long id)
    PostDTO updatePost(Long id, PostDTO postDTO)

}