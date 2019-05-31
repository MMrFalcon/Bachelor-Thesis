package com.falcon.forum.service.implementation;

import com.falcon.forum.exception.VoteAlreadyAddedException;
import com.falcon.forum.model.CommentsDTO;
import com.falcon.forum.model.PostDTO;
import com.falcon.forum.model.UserDTO;
import com.falcon.forum.persist.Comments;
import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.User;
import com.falcon.forum.service.CommentsService;
import com.falcon.forum.service.PostService;
import com.falcon.forum.service.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VoteServiceImplTest {

    private VoteServiceImpl voteService;
    private UserDTO userDTO;
    private User user;
    private PostDTO postDTO;
    private Post post;
    private CommentsDTO commentsDTO;
    private Comments comments;

    @Mock
    UserService userService;

    @Mock
    PostService postService;

    @Mock
    CommentsService commentsService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        voteService = new VoteServiceImpl(userService, postService, commentsService);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("Falcon");
        userDTO.setActive(true);
        userDTO.setEmail("mail@mail.com");
        userDTO.setPoints(20L);
        userDTO.setMinusPoints(2L);
        userDTO.setPlusPoints(22L);

        user = new User();
        user.setId(1L);
        user.setUsername("Falcon");
        user.setActive(true);
        user.setEmail("mail@mail.com");
        user.setPoints(20L);
        user.setMinusPoints(2L);
        user.setPlusPoints(22L);

        postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setActive(true);
        postDTO.setPoints(11L);
        postDTO.setResolved(false);

        post = new Post();
        post.setId(1L);
        post.setActive(true);
        post.setPoints(11L);
        post.setResolved(false);

        commentsDTO = new CommentsDTO();
        commentsDTO.setId(1L);
        commentsDTO.setPoints(13L);
        commentsDTO.setActive(true);

        comments = new Comments();
        comments.setId(1L);
        comments.setPoints(13L);
        comments.setActive(true);
        comments.setPost(post);
        comments.setIsCorrect(false);

    }

    @Test
    public void addVoteToPost() {
        int postUserVotesSize = 1;

        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(postService.getOne(postDTO.getId())).thenReturn(post);
        when(postService.saveAndFlush(post)).thenReturn(post);
        voteService.addVoteToPost(userDTO, postDTO);

        assertEquals(post.getPostUsersVotes().size(),postUserVotesSize);
        verify(userService, times(1)).getOne(userDTO.getId());
        verify(postService, times(1)).getOne(postDTO.getId());
        verify(postService, times(1)).saveAndFlush(post);
    }


    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Test
    public void addVoteToPostAlreadyAdded() {
        post.getPostUsersVotes().add(user);
        final String exceptionMessage = "Vote already added";
        expectedException.expect(VoteAlreadyAddedException.class);
        expectedException.expectMessage(exceptionMessage);

        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(postService.getOne(postDTO.getId())).thenReturn(post);
        when(postService.saveAndFlush(post)).thenReturn(post);
        voteService.addVoteToPost(userDTO, postDTO);


    }

    @Test
    public void addVoteToComment() {
        int commentUserVotesSize = 1;

        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(commentsService.getOne(commentsDTO.getId())).thenReturn(comments);
        when(commentsService.saveAndFlush(comments)).thenReturn(comments);
        voteService.addVoteToComment(userDTO, commentsDTO);

        assertEquals(comments.getAnswerUsersVotes().size(), commentUserVotesSize);
        verify(userService, times(1)).getOne(userDTO.getId());
        verify(commentsService, times(1)).getOne(commentsDTO.getId());
        verify(commentsService, times(1)).saveAndFlush(comments);
    }

    @Test
    public void addVoteToCommentAlreadyExist() {
        comments.getAnswerUsersVotes().add(user);
        final String exceptionMessage = "Vote already added";
        expectedException.expect(VoteAlreadyAddedException.class);
        expectedException.expectMessage(exceptionMessage);

        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(commentsService.getOne(commentsDTO.getId())).thenReturn(comments);
        when(commentsService.saveAndFlush(comments)).thenReturn(comments);
        voteService.addVoteToComment(userDTO, commentsDTO);
    }

    @Test
    public void isPostAuthor() {
        post.getPostUsersVotes().add(user);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(postService.getOne(postDTO.getId())).thenReturn(post);
        boolean shouldBeTrue = voteService.isPostAuthor("Falcon", 1L);

        assertTrue(shouldBeTrue);
    }

    @Test
    public void isNotPostAuthor() {
        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(postService.getOne(postDTO.getId())).thenReturn(post);
        boolean shouldBeFalse = voteService.isPostAuthor("Falcon", 1L);

        assertFalse(shouldBeFalse);
    }

    @Test
    public void isAnswerAuthor() {
        comments.getAnswerUsersVotes().add(user);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(commentsService.getOne(commentsDTO.getId())).thenReturn(comments);
        boolean shouldBeTrue = voteService.isAnswerAuthor("Falcon", 1L);

        assertTrue(shouldBeTrue);
    }

    @Test
    public void isNotAnswerAuthor() {
        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(commentsService.getOne(commentsDTO.getId())).thenReturn(comments);
        boolean shouldBeFalse = voteService.isAnswerAuthor("Falcon", 1L);

        assertFalse(shouldBeFalse);
    }
}