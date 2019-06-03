package com.falcon.forum.controller;

import com.falcon.forum.model.PostDTO;
import com.falcon.forum.model.UserDTO;
import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.User;
import com.falcon.forum.service.PointsService;
import com.falcon.forum.service.PostService;
import com.falcon.forum.service.TagsService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TagsController.class)
public class TagsControllerTestIT {

    private User userEntity;
    private Post postEntity;

    @MockBean
    UserService userService;

    @MockBean
    PostService postService;

    @MockBean
    TagsService tagsService;

    @MockBean
    PointsService pointsService;

    @Autowired
    MockMvc mockMvc;

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
    public void getTagsForm() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        mockMvc.perform(get("/tags/creator/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/tagsForm"))
                .andExpect(model().attributeExists("tags", "username", "userId", "postId"));
    }

    @Test
    public void getTagsFormShouldFailWith401() throws Exception {
        mockMvc.perform(get("/tags/creator/1"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void sendPostedTags() throws Exception {
        PostDTO postDTO = Mapper.postToDto(postEntity);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("tag", "Java");

        when(postService.addPostTags(anyLong(), any())).thenReturn(postDTO);
        mockMvc.perform(post("/tags/creator/1")
                .with(csrf())
                .params(params)
        )
                .andExpect(redirectedUrl("/posts/post/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(status().isFound());
    }

    @Test
    public void sendPostedTagsShouldFailWith401() throws Exception {
        mockMvc.perform(post("/tags/creator/1")
                .with(csrf())
        )
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void editTags() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        mockMvc.perform(get("/tags/creator/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/editTags"))
                .andExpect(model().attributeExists("tags", "username", "userId", "tags"));
    }

    @Test
    public void editTagsShouldFailWith401() throws Exception {
        mockMvc.perform(get("/tags/creator/edit/1"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void updateTags() throws Exception {
        PostDTO postDTO = Mapper.postToDto(postEntity);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("tag", "Java");

        when(postService.addPostTags(anyLong(), any())).thenReturn(postDTO);
        mockMvc.perform(post("/tags/creator/edit/1")
                .with(csrf())
                .params(params)
        )
                .andExpect(redirectedUrl("/posts/post/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(status().isFound());
    }

    @Test
    public void updateTagsShouldFailWith401() throws Exception {
        mockMvc.perform(post("/tags/creator/edit/1")
                .with(csrf())
        )
                .andExpect(status().isUnauthorized());
    }
}