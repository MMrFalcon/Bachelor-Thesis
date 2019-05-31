package com.falcon.forum.service.implementation;

import com.falcon.forum.model.CommentsDTO;
import com.falcon.forum.model.PostDTO;
import com.falcon.forum.model.UserDTO;
import com.falcon.forum.persist.Comments;
import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.User;
import com.falcon.forum.service.CommentsService;
import com.falcon.forum.service.PostService;
import com.falcon.forum.service.UserService;
import com.falcon.forum.service.VoteService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PointsServiceImplementationTest {

    private PointsServiceImplementation pointsServiceImplementation;
    private UserDTO userDTO;
    private User user;
    private PostDTO postDTO;
    private Post post;
    private CommentsDTO commentsDTO;
    private Comments comments;
    private Comments correctComments;
    private User postAuthor;


    @Mock
    UserService userService;

    @Mock
    PostService postService;

    @Mock
    CommentsService commentsService;

    @Mock
    VoteService voteService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pointsServiceImplementation = new PointsServiceImplementation(userService, commentsService, postService, voteService);

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

        postAuthor = new User();
        postAuthor.setId(2L);
        postAuthor.setUsername("Alberta");
        postAuthor.setActive(true);
        postAuthor.setEmail("mail@mail.com");
        postAuthor.setPoints(20L);
        postAuthor.setMinusPoints(2L);
        postAuthor.setPlusPoints(22L);

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
        post.setUser(postAuthor);

        commentsDTO = new CommentsDTO();
        commentsDTO.setId(1L);
        commentsDTO.setPoints(13L);
        commentsDTO.setActive(true);

        comments = new Comments();
        comments.setId(1L);
        comments.setPoints(13L);
        comments.setActive(true);
        comments.setUser(postAuthor);
        comments.setPost(post);
        comments.setIsCorrect(false);

        correctComments = new Comments();
        correctComments.setId(2L);
        correctComments.setPoints(15L);
        correctComments.setActive(true);
        correctComments.setUser(postAuthor);
        correctComments.setPost(post);
        correctComments.setIsCorrect(true);

    }

    @Test
    public void addPointsToUser() {
        Long pointsForAdd = 2L;
        Long pointsAfterAdd = userDTO.getPoints() + pointsForAdd;

        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        pointsServiceImplementation.addPointsToUser(pointsForAdd, userDTO);

        verify(userService, times(2)).getOne(userDTO.getId());
        verify(userService, times(2)).save(user);
        assertEquals(userService.getOne(userDTO.getId()).getPoints(), pointsAfterAdd);
    }

    @Test
    public void addPlusPointsToUser() {
        Long plusPointsForAdd = 1L;
        Long pointsAfterAdd = userDTO.getPlusPoints() + plusPointsForAdd;

        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        pointsServiceImplementation.addPointsToUser(plusPointsForAdd, userDTO);

        verify(userService, times(2)).getOne(userDTO.getId());
        verify(userService, times(2)).save(user);
        assertEquals(userService.getOne(userDTO.getId()).getPlusPoints(), pointsAfterAdd);
    }

    @Test
    public void addMinusPointsToUser() {
        Long minusPointsForAdd = 1L;
        Long pointsAfterAdd = userDTO.getPlusPoints() + minusPointsForAdd;

        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        pointsServiceImplementation.addPointsToUser(minusPointsForAdd, userDTO);

        verify(userService, times(2)).getOne(userDTO.getId());
        verify(userService, times(2)).save(user);
        assertEquals(userService.getOne(userDTO.getId()).getPlusPoints(), pointsAfterAdd);
    }

    @Test
    public void addPointsToPost() {
        Long pointsForAdd = 2L;
        Long pointsAfterAdd = postDTO.getPoints() + pointsForAdd;
        String voteAuthor = "Jack";

        when(postService.getOne(postDTO.getId())).thenReturn(post);
        when(postService.save(post)).thenReturn(post);
        when(userService.getUserByName(voteAuthor)).thenReturn(any());
        when(userService.getOne(postAuthor.getId())).thenReturn(postAuthor);
        when(userService.save(postAuthor)).thenReturn(postAuthor);
        pointsServiceImplementation.addPointsToPost(pointsForAdd, postDTO, voteAuthor);

        verify(postService, times(1)).getOne(postDTO.getId());
        verify(postService, times(1)).save(post);
        verify(userService, times(1)).getUserByName(voteAuthor);
        verify(userService, times(2)).getOne(postAuthor.getId());
        verify(userService, times(2)).save(postAuthor);
        assertEquals(postService.getOne(postDTO.getId()).getPoints(), pointsAfterAdd);
    }

    @Test
    public void subtractPointsFromUser() {
        Long pointsForSubtract = 2L;
        Long pointsAfterAdd = userDTO.getPoints() - pointsForSubtract;

        when(userService.getOne(userDTO.getId())).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        pointsServiceImplementation.subtractPointsFromUser(pointsForSubtract, userDTO);

        verify(userService, times(2)).getOne(userDTO.getId());
        verify(userService, times(2)).save(user);
        assertEquals(userService.getOne(userDTO.getId()).getPoints(), pointsAfterAdd);

    }

    @Test
    public void subtractPointsFromPost() {
        Long pointsForSubtract = 2L;
        Long pointsAfterAdd = postDTO.getPoints()  - pointsForSubtract;
        String voteAuthor = "Jack";

        when(postService.getOne(postDTO.getId())).thenReturn(post);
        when(postService.save(post)).thenReturn(post);
        when(userService.getUserByName(voteAuthor)).thenReturn(any());
        when(userService.getOne(postAuthor.getId())).thenReturn(postAuthor);
        when(userService.save(postAuthor)).thenReturn(postAuthor);
        pointsServiceImplementation.subtractPointsFromPost(pointsForSubtract, postDTO, voteAuthor);

        verify(postService, times(1)).getOne(postDTO.getId());
        verify(postService, times(1)).save(post);
        verify(userService, times(1)).getUserByName(voteAuthor);
        verify(userService, times(2)).getOne(postAuthor.getId());
        verify(userService, times(2)).save(postAuthor);
        assertEquals(postService.getOne(postDTO.getId()).getPoints(), pointsAfterAdd);
    }

    @Test
    public void addPointsToAnswer() {
        Long pointsForAdd = 2L;
        Long pointsAfterAdd = commentsDTO.getPoints() + pointsForAdd;
        String voteAuthor = "Jack";

        when(commentsService.getOne(postDTO.getId())).thenReturn(comments);
        when(commentsService.save(comments)).thenReturn(comments);
        when(userService.getUserByName(voteAuthor)).thenReturn(any());
        when(userService.getOne(postAuthor.getId())).thenReturn(postAuthor);
        when(userService.save(postAuthor)).thenReturn(postAuthor);
        pointsServiceImplementation.addPointsToAnswer(pointsForAdd, commentsDTO, voteAuthor);

        verify(commentsService, times(1)).getOne(postDTO.getId());
        verify(commentsService, times(1)).save(comments);
        verify(userService, times(1)).getUserByName(voteAuthor);
        verify(userService, times(2)).getOne(postAuthor.getId());
        verify(userService, times(2)).save(postAuthor);
        assertEquals(commentsService.getOne(postDTO.getId()).getPoints(), pointsAfterAdd);
    }

    @Test
    public void subtractPointsFromAnswer() {
        Long pointsForAdd = 2L;
        Long pointsAfterAdd = commentsDTO.getPoints() - pointsForAdd;
        String voteAuthor = "Jack";

        when(commentsService.getOne(postDTO.getId())).thenReturn(comments);
        when(commentsService.save(comments)).thenReturn(comments);
        when(userService.getUserByName(voteAuthor)).thenReturn(any());
        when(userService.getOne(postAuthor.getId())).thenReturn(postAuthor);
        when(userService.save(postAuthor)).thenReturn(postAuthor);
        pointsServiceImplementation.subtractPointsFromAnswer(pointsForAdd, commentsDTO, voteAuthor);

        verify(commentsService, times(1)).getOne(postDTO.getId());
        verify(commentsService, times(1)).save(comments);
        verify(userService, times(1)).getUserByName(voteAuthor);
        verify(userService, times(2)).getOne(postAuthor.getId());
        verify(userService, times(2)).save(postAuthor);
        assertEquals(commentsService.getOne(postDTO.getId()).getPoints(), pointsAfterAdd);
    }

    @Test
    public void markAnswerAsCorrect() {
        Long userPointsAfterAdd = 2L + user.getPoints();
        Long userPlusPointsAfterAdd = 2L + user.getPlusPoints();

        when(commentsService.getOne(commentsDTO.getId())).thenReturn(comments);
        when(commentsService.save(comments)).thenReturn(correctComments);
        when(postService.getOne(comments.getPost().getId())).thenReturn(post);
        when(postService.save(comments.getPost())).thenReturn(post);
        when(userService.getOne(anyLong())).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        pointsServiceImplementation.markAnswerAsCorrect(commentsDTO);

        assertEquals(userPointsAfterAdd, user.getPoints());
        assertEquals(userPlusPointsAfterAdd, user.getPlusPoints());
        verify(userService, times(2)).getOne(anyLong());
        verify(userService, times(2)).save(user);
        verify(commentsService, times(1)).getOne(commentsDTO.getId());
        verify(commentsService, times(1)).save(comments);
        verify(postService, times(1)).save(comments.getPost());
        verify(postService, times(1)).getOne(comments.getPost().getId());

    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Test
    public void markAnswerAsCorrectAlreadyCorrect() {
        final String exceptionMessage = "Comment was already marked as correct";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        when(commentsService.getOne(commentsDTO.getId())).thenReturn(correctComments);
        pointsServiceImplementation.markAnswerAsCorrect(commentsDTO );
    }
}