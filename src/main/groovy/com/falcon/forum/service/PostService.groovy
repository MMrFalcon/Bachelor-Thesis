package com.falcon.forum.service

import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.TagsDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.Post
import com.falcon.forum.repository.PostRepository

interface PostService extends  BaseService <Post, Long, PostRepository> {
    PostDTO createPost(PostDTO postDTO, UserDTO userDTO)
    PostDTO getPostDtoById(Long id)
    PostDTO getPostByAnswer(CommentsDTO commentsDTO)
    PostDTO updatePost(Long id, PostDTO postDTO)
    PostDTO addPostTags(Long postId, Set<TagsDTO> tagsDTOs)
    PostDTO updatePostTags(Long postId, Set<TagsDTO> tagsDTOs)
    boolean isDeletable(PostDTO postDTO, String username)
    boolean isEditable(PostDTO postDTO, String username)
    boolean isAuthor(PostDTO postDTO, String username)
    String getAuthorName(PostDTO postDTO)
}