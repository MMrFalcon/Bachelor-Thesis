package com.falcon.forum.controller;

import com.falcon.forum.persist.Comments;
import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.User;
import com.falcon.forum.service.CommentsService;
import com.falcon.forum.service.PostService;
import com.falcon.forum.service.UserService;
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

;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentsController.class)
public class CommentsControllerTestIT {

    private User userEntity;
    private Post postEntity;
    private Comments comments;


    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    PostService postService;

    @MockBean
    CommentsService commentsService;

    @Before
    public void setUp() throws Exception {
        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("Falcon");
        userEntity.setActive(true);

        postEntity = new Post();
        postEntity.setId(1L);
        postEntity.setActive(true);
        postEntity.setTitle("Title");
        postEntity.setUser(userEntity);

        comments = new Comments();
        comments.setId(1L);
        comments.setCommentMessage("Answer");
    }

    @Test
    public void getAnswerFormShouldFailWith401() throws Exception {
        mockMvc.perform(get("/posts/post/1/answer"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void getAnswerForm() throws Exception {
        when(userService.getUserByName(userEntity.getUsername())).thenReturn(Mapper.userToDTO(userEntity));
        when(postService.getPostDtoById(postEntity.getId())).thenReturn(Mapper.postToDto(postEntity));
        when(postService.getAuthorName(any())).thenReturn(postEntity.getUser().getUsername());

        mockMvc.perform(get("/posts/post/1/answer"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/answerForm"))
                .andExpect(model().attributeExists("userId", "post", "username", "author", "delete", "edit",
                        "comment", "postId", "answers"));
    }

    @Test
    public void postAnswerShouldFailWith403() throws Exception {
        mockMvc.perform(post("/posts/post/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void postAnswerShouldFailWith401() throws Exception {
        mockMvc.perform(post("/posts/post/1").with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void postAnswer() throws Exception {

        when(userService.getUserByName(userEntity.getUsername())).thenReturn(Mapper.userToDTO(userEntity));
        when(postService.getPostDtoById(postEntity.getId())).thenReturn(Mapper.postToDto(postEntity));

        mockMvc.perform(post("/posts/post/1/answer")
                .with(csrf())
        )
                .andExpect(redirectedUrl("/posts/post/1"))
                .andExpect(status().isFound())
                .andExpect(status().is3xxRedirection());
    }


    @Test
    public void editAnswerShouldFailWith401() throws Exception {
        mockMvc.perform(get("/posts/post/1/answer/1/edit"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void editAnswer() throws Exception {
        when(userService.getUserByName(userEntity.getUsername())).thenReturn(Mapper.userToDTO(userEntity));
        when(postService.getPostDtoById(postEntity.getId())).thenReturn(Mapper.postToDto(postEntity));
        when(postService.getAuthorName(any())).thenReturn("PostAuthorName");
        when(commentsService.getCommentDtoById(anyLong())).thenReturn(Mapper.commentsToDto(comments));

        mockMvc.perform(get("/posts/post/1/answer/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/editAnswerForm"))
                .andExpect(model().attributeExists("userId", "post", "username", "author", "delete", "edit",
                        "comment", "postId", "answers", "answerId"));
    }

    @Test
    public void postEditedAnswerShouldFailWith403() throws Exception {
        mockMvc.perform(post("/posts/post/1/answer/1/edit"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void postEditedAnswerShouldFailWith401() throws Exception {
        mockMvc.perform(post("/posts/post/1/answer/1/edit").with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void postEditedAnswer() throws Exception {
        when(userService.getUserByName(userEntity.getUsername())).thenReturn(Mapper.userToDTO(userEntity));
        when(postService.getPostDtoById(postEntity.getId())).thenReturn(Mapper.postToDto(postEntity));

        mockMvc.perform(post("/posts/post/1/answer/1/edit")
                .with(csrf())
        )
                .andExpect(redirectedUrl("/posts/post/1"))
                .andExpect(status().isFound())
                .andExpect(status().is3xxRedirection());
    }


    @Test
    public void redirectToPostShouldFailWith401() throws Exception {
        mockMvc.perform(get("/posts/answer/1"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void redirectToPost() throws Exception {

        when(commentsService.getPostId(anyLong())).thenReturn(1L);
        mockMvc.perform(get("/posts/answer/1"))
                .andExpect(redirectedUrl("/posts/post/1"))
                .andExpect(status().isFound())
                .andExpect(status().is3xxRedirection());

    }
}