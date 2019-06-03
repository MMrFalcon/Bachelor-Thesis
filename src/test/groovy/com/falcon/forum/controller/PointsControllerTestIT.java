package com.falcon.forum.controller;

import com.falcon.forum.model.CommentsDTO;
import com.falcon.forum.model.PostDTO;
import com.falcon.forum.persist.Comments;
import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.User;
import com.falcon.forum.service.CommentsService;
import com.falcon.forum.service.PointsService;
import com.falcon.forum.service.PostService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PointsController.class)
public class PointsControllerTestIT {

    private User userEntity;

    private Post postEntity;

    private Comments commentsEntity;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    PointsService pointsService;

    @MockBean
    CommentsService commentsService;

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

        commentsEntity = new Comments();
        commentsEntity.setId(2L);
        commentsEntity.setPoints(15L);
        commentsEntity.setActive(true);
        commentsEntity.setUser(userEntity);
        commentsEntity.setPost(postEntity);
        commentsEntity.setIsCorrect(true);
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void addPointsToAnswer() throws Exception{
        PostDTO postDTO = Mapper.postToDto(postEntity);
        CommentsDTO commentsDTO = Mapper.commentsToDto(commentsEntity);

        when(postService.getPostByAnswer(any())).thenReturn(postDTO);
        when(commentsService.getCommentDtoById(anyLong())).thenReturn(commentsDTO);
        mockMvc.perform(get("/voteup/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts/post/1"));
        verify(pointsService, times(1)).addPointsToAnswer(anyLong(),
               eq(commentsDTO), eq("Falcon"));
    }

    @Test
    public void addPointsToAnswerUnauthorized() throws Exception{
        mockMvc.perform(get("/voteup/1"))
                .andExpect(status().isUnauthorized());
    }
    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void subtractPointsFromAnswer() throws Exception {
        PostDTO postDTO = Mapper.postToDto(postEntity);
        CommentsDTO commentsDTO = Mapper.commentsToDto(commentsEntity);

        when(postService.getPostByAnswer(any())).thenReturn(postDTO);
        when(commentsService.getCommentDtoById(anyLong())).thenReturn(commentsDTO);
        mockMvc.perform(get("/votedown/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts/post/1"));

        verify(pointsService, times(1)).subtractPointsFromAnswer(anyLong(),
                eq(commentsDTO), eq("Falcon"));
    }

    @Test
    public void subtractPointsFromAnswerUnauthorized() throws Exception {
        mockMvc.perform(get("/votedown/1"))
                .andExpect(status().isUnauthorized());
    }


    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void markAnswerAsCorrect() throws Exception {
        PostDTO postDTO = Mapper.postToDto(postEntity);
        CommentsDTO commentsDTO = Mapper.commentsToDto(commentsEntity);

        when(postService.getPostByAnswer(any())).thenReturn(postDTO);
        when(commentsService.getCommentDtoById(anyLong())).thenReturn(commentsDTO);
        mockMvc.perform(get("/correctAnswer/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts/post/1"));

        verify(pointsService, times(1)).markAnswerAsCorrect(commentsDTO);
    }

    @Test
    public void markAnswerAsCorrectUnauthorized() throws Exception {
        mockMvc.perform(get("/correctAnswer/1"))
                .andExpect(status().isUnauthorized());
    }


    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void addPointsToPost() throws Exception {
        PostDTO postDTO = Mapper.postToDto(postEntity);

        when(postService.getPostDtoById(anyLong())).thenReturn(postDTO);
        when(commentsService.getCommentDtoById(anyLong())).thenReturn(Mapper.commentsToDto(commentsEntity));
        mockMvc.perform(get("/voteupPost/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts/post/1"));

        verify(pointsService, times(1)).addPointsToPost(anyLong(),
                eq(postDTO), eq("Falcon"));
    }

    @Test
    public void addPointsToPostUnauthorized() throws Exception {
        mockMvc.perform(get("/voteupPost/1"))
                .andExpect(status().isUnauthorized());
    }


    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void subtractPointsFromPost() throws Exception {
        PostDTO postDTO = Mapper.postToDto(postEntity);

        when(postService.getPostDtoById(anyLong())).thenReturn(postDTO);
        when(commentsService.getCommentDtoById(anyLong())).thenReturn(Mapper.commentsToDto(commentsEntity));
        mockMvc.perform(get("/votedownPost/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts/post/1"));

        verify(pointsService, times(1)).subtractPointsFromPost(anyLong(),
                eq(postDTO), eq("Falcon"));
    }

    @Test
    public void subtractPointsFromPostUnauthorized() throws Exception {
        mockMvc.perform(get("/votedownPost/1"))
                .andExpect(status().isUnauthorized());
    }
}