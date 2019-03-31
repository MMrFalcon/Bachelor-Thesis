package Falcon.Service

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Model.UserDTO
import Falcon.Persist.Post
import Falcon.Persist.Tags
import Falcon.Persist.User

class Mapper {

    static userToDTO(User user) {
        UserDTO userDTO = new UserDTO(
                id: user.getId(),
                username: user.getProperty("username"),
                password: user.getProperty("password"),
                email: user.getProperty("email"),
                points: user.getPoints(),
                active: user.getProperty("active")
        )

        return userDTO
    }

    static User dtoToUser(UserDTO userDTO) {
        User user = new User(
                email: userDTO.getEmail(),
                username: userDTO.getUsername(),
                password: userDTO.getPassword(),
                points: userDTO.getPoints(),
                active: userDTO.isActive()
        )

        return user
    }

    static PostDTO postToDto(Post post) {
        PostDTO postDTO = new PostDTO(
                id: post.getId(),
                title: post.getTitle(),
                content: post.getContent(),
                authorName: post.getAuthorName()
        )

        return postDTO
    }

    static Post dtoToPost(PostDTO postDTO) {
        Post post = new Post(
                title: postDTO.getTitle(),
                content: postDTO.getContent(),
                authorName: postDTO.getAuthorName()
        )

        return post
    }

    static TagsDTO tagsToDTO(Tags tags) {
        TagsDTO tagsDTO  = new TagsDTO(
                id: tags.getId(), postId: tags.getPostId(),
                tag: tags.getTag()
        )

        return tagsDTO
    }

    static Tags dtoToTags(TagsDTO tagsDTO) {
        Tags tags = new Tags(postId: tagsDTO.getPostId(), tag: tagsDTO.getTag())
        return tags
    }

    static List<TagsDTO> transferTags(List<Tags> tags) {
        def tagsDTOList = [] as List<TagsDTO>

        tags.each {
            Tags tag ->
                tagsDTOList << tagsToDTO(tag)
        }

        return tagsDTOList
    }



}
