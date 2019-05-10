package com.falcon.forum.service.implementation


import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.TagsDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.Comments
import com.falcon.forum.persist.Post
import com.falcon.forum.persist.Tags
import com.falcon.forum.persist.User
import org.modelmapper.ModelMapper

class Mapper {

    static UserDTO userToDTO(User user) {
        ModelMapper modelMapper = new ModelMapper()
        return modelMapper.map(user, UserDTO)
    }

    static User dtoToUser(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper()
        return modelMapper.map(userDTO, User)
    }

    static PostDTO postToDto(Post post) {
        ModelMapper modelMapper = new ModelMapper()
        return modelMapper.map(post, PostDTO)
    }

    static Post dtoToPost(PostDTO postDTO) {
        ModelMapper modelMapper = new ModelMapper()
        return modelMapper.map(postDTO, Post)
    }

    static TagsDTO tagsToDTO(Tags tags) {
        ModelMapper modelMapper = new ModelMapper()
        return modelMapper.map(tags, TagsDTO)
    }

    static Tags dtoToTags(TagsDTO tagsDTO) {
        ModelMapper modelMapper = new ModelMapper()
        return modelMapper.map(tagsDTO, Tags)
    }

    static Comments dtoToComments(CommentsDTO commentsDTO) {
        ModelMapper modelMapper = new ModelMapper()
        return modelMapper.map(commentsDTO, Comments)
    }

    static CommentsDTO commentsToDto(Comments comments) {
        ModelMapper modelMapper = new ModelMapper()
        return modelMapper.map(comments, CommentsDTO)
    }

}
