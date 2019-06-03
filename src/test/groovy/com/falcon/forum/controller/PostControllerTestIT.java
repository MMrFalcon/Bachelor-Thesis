package com.falcon.forum.controller;

import com.falcon.forum.model.PostDTO;
import com.falcon.forum.model.UserDTO;
import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.User;
import com.falcon.forum.service.*;
import com.falcon.forum.service.implementation.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTestIT {

    private User userEntity;
    private Post postEntity;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    UserService userService;

    @MockBean
    TagsService tagsService;

    @MockBean
    CommentsService commentsService;

    @MockBean
    VoteService voteService;

    @MockBean
    PointsService pointsService;


    @Before
    public void setUp() throws Exception {
        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("Falcon");
        userEntity.setActive(true);
        userEntity.setEmail("mail@mail.com");
        userEntity.setPoints(20L);
        userEntity.setMinusPoints(2L);
        userEntity.setPlusPoints(22L);

        postEntity = new Post();
        postEntity.setId(1L);
        postEntity.setActive(true);
        postEntity.setPoints(11L);
        postEntity.setResolved(false);
        postEntity.setUser(userEntity);

    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void getForm() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        mockMvc.perform(get("/creator"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/postsForm"));
    }

    @Test
    public void getFormShouldFailWith401() throws Exception {
        mockMvc.perform(get("/creator"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void submitPost() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);
        PostDTO postDTO = Mapper.postToDto(postEntity);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        when(postService.createPost(any(), eq(userDTO))).thenReturn(postDTO);
        mockMvc.perform(post("/creator")
                .with(csrf())
        )
                .andExpect(redirectedUrl("/tags/creator/1"))
                .andExpect(status().isFound())
                .andExpect(status().is3xxRedirection());

        verify(postService, times(1)).createPost(any(), eq(userDTO));
    }

    @Test
    public void submitPostShouldFailWith401() throws Exception {

        mockMvc.perform(post("/creator")
                .with(csrf())
        )
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void getPosts() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/posts"))
                .andExpect(model().attributeExists("posts", "username", "userId", "tags"));
    }

    @Test
    public void getPostsShouldFailWith401() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void showPost() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);
        PostDTO postDTO = Mapper.postToDto(postEntity);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        when(postService.getPostDtoById(1L)).thenReturn(postDTO);
        when(postService.getAuthorName(postDTO)).thenReturn("Falcon");
        mockMvc.perform(get("/posts/post/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"))
                .andExpect(model().attributeExists("userId", "post", "username", "author", "delete", "edit",
                        "postId", "answers", "voteService", "isAuthor", "isPostVoteAuthor"));
    }

    @Test
    public void showPostShouldFailWith401() throws Exception {
        mockMvc.perform(get("/posts/post/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void editPostShouldFailWith401() throws Exception {
        mockMvc.perform(get("/edit/1"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void editPost() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);
        PostDTO postDTO = Mapper.postToDto(postEntity);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        when(postService.getPostDtoById(1L)).thenReturn(postDTO);
        mockMvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/editPost"))
                .andExpect(model().attributeExists("userId", "post", "username"));
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void createEditedPost() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);
        PostDTO postDTO = Mapper.postToDto(postEntity);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        when(postService.updatePost(1L, postDTO)).thenReturn(postDTO);
        mockMvc.perform(post("/editPost/1")
                .with(csrf())
        )
                .andExpect(redirectedUrl("/tags/creator/edit/1"))
                .andExpect(status().isFound())
                .andExpect(status().is3xxRedirection());

        verify(postService, times(1)).updatePost(eq(1L), any());
    }

    @Test
    public void createEditedPostShouldFailWith401() throws Exception {
        mockMvc.perform(post("/editPost/1")
                .with(csrf())
        ).andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void deletePost() throws Exception {
        mockMvc.perform(get("/delete/1"))
                .andExpect(status().isFound())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(postService, times(1)).delete(1L);
    }

    @Test
    public void deletePostShouldFailWith401() throws Exception {
        mockMvc.perform(get("/delete/1"))
                .andExpect(status().isUnauthorized());
    }
}