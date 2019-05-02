package Falcon.Service.Implementations

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Model.UserDTO
import Falcon.Persist.Post
import Falcon.Persist.Tags
import Falcon.Persist.User
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

}
