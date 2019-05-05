package Falcon.Service

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Model.UserDTO
import Falcon.Persist.Post
import Falcon.Repository.PostRepository

interface PostService extends  BaseService <Post, Long, PostRepository> {
    PostDTO createPost(PostDTO postDTO, UserDTO userDTO)
    PostDTO getPostDtoById(Long id)
    PostDTO updatePost(Long id, PostDTO postDTO)
    PostDTO addPostTags(Long postId, Set<TagsDTO> tagsDTOs)
    PostDTO updatePostTags(Long postId, Set<TagsDTO> tagsDTOs)
    boolean isDeletable(PostDTO postDTO, String username)
    boolean isEditable(PostDTO postDTO, String username)
    String getAuthorName(PostDTO postDTO)
}