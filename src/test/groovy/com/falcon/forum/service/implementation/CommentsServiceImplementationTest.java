package com.falcon.forum.service.implementation;

import com.falcon.forum.model.CommentsDTO;
import com.falcon.forum.model.PostDTO;
import com.falcon.forum.model.UserDTO;
import com.falcon.forum.persist.Comments;
import com.falcon.forum.persist.Post;
import com.falcon.forum.repository.CommentsRepository;
import com.falcon.forum.service.PostService;
import com.falcon.forum.service.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class CommentsServiceImplementationTest {

    private CommentsServiceImplementation commentsService;
    private UserDTO userDTO;
    private PostDTO postDTO;
    private CommentsDTO commentsDTO;

    @Mock
    CommentsRepository commentsRepository;

    @Mock
    UserService userService;

    @Mock
    PostService postService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        commentsService = new CommentsServiceImplementation(commentsRepository, userService, postService);

        userDTO = new UserDTO();
        userDTO.setUsername("Falcon");
        userDTO.setId(1L);


        commentsDTO = new CommentsDTO();
        commentsDTO.setCommentMessage("Comment Message");
        commentsDTO.setId(1L);
        commentsDTO.setActive(true);

        postDTO = new PostDTO();
        postDTO.setTitle("Title of post");
        postDTO.setId(1L);
    }

    @Test
    public void createComment() {

        when(userService.getOne(anyLong())).thenReturn(Mapper.dtoToUser(userDTO));
        when(postService.getOne(anyLong())).thenReturn(Mapper.dtoToPost(postDTO));
        when(commentsRepository.save(any())).thenReturn(Mapper.dtoToComments(commentsDTO));

        Comments savedComment = commentsRepository.save(Mapper.dtoToComments(commentsDTO));
        CommentsDTO savedCommentDTO = commentsService.createComment(commentsDTO, userDTO, postDTO);

        assertNotNull(savedCommentDTO);
        assertEquals(savedCommentDTO.getId(), savedComment.getId());
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createCommentWithNullAuthor() {
        final String exceptionMessage = "Cannot find user";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        when(userService.getOne(anyLong())).thenReturn(null);
        when(postService.getOne(anyLong())).thenReturn(Mapper.dtoToPost(postDTO));
        when(commentsRepository.save(any())).thenReturn(Mapper.dtoToComments(commentsDTO));

        commentsService.createComment(commentsDTO, userDTO, postDTO);

    }

    @Test
    public void createCommentWithNullPost() {
        final String exceptionMessage = "Cannot find post";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        when(userService.getOne(anyLong())).thenReturn(Mapper.dtoToUser(userDTO));
        when(postService.getOne(anyLong())).thenReturn(null);
        when(commentsRepository.save(any())).thenReturn(Mapper.dtoToComments(commentsDTO));

        commentsService.createComment(commentsDTO, userDTO, postDTO);
    }

    @Test
    public void updateComment() {
        Long commentId = 2L;
        String commentMessage = "New Comment Message";
        CommentsDTO commentsForUpdate = new CommentsDTO();
        commentsForUpdate.setId(commentId);
        commentsForUpdate.setCommentMessage(commentMessage);

        when(commentsRepository.getOne(commentId)).thenReturn(Mapper.dtoToComments(commentsDTO));
        //for service Method
        when(commentsRepository.save(any())).thenReturn(Mapper.dtoToComments(commentsForUpdate));
        CommentsDTO updateComment = commentsService.updateComment(commentsForUpdate, commentId);
        //for repository Method
        Comments updatedEntity = commentsRepository.save(Mapper.dtoToComments(commentsForUpdate));

        assertNotNull(updateComment);
        assertEquals(updateComment.getCommentMessage(), commentMessage);
        assertEquals(updatedEntity.getCommentMessage(), commentMessage);
    }

    @Test
    public void updateCommentWithBadId() {
        Long commentId = 2L;
        String commentMessage = "New Comment Message";
        CommentsDTO commentsForUpdate = new CommentsDTO();
        commentsForUpdate.setId(commentId);
        commentsForUpdate.setCommentMessage(commentMessage);

        final String exceptionMessage = "Cannot find comment with id 2";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        when(commentsRepository.getOne(commentId)).thenReturn(null);
        commentsService.updateComment(commentsForUpdate, commentId);
        //for repository Method
        commentsRepository.save(Mapper.dtoToComments(commentsForUpdate));
    }

    @Test
    public void getComments() {
        Post post = Mapper.dtoToPost(postDTO);
        post.getComments().add(Mapper.dtoToComments(commentsDTO));

        when(postService.getOne(any())).thenReturn(post);
        List<Comments> comments = commentsService.getComments(Mapper.postToDto(post));

        assertEquals(comments.size(), 1);
        assertEquals(comments.get(0).getCommentMessage(), "Comment Message");
    }

    @Test
    public void getCommentDtoById() {
        Long id = commentsDTO.getId();

        when(commentsRepository.getOne(id)).thenReturn(null);
        CommentsDTO comment = commentsService.getCommentDtoById(id);

        assertNotNull(comment);
        assertEquals(comment.getCommentMessage(), "Comment Message");
    }

    @Test
    public void getCommentDtoByIdWithBadId() {
        Long id = 2L;
        final String exceptionMessage = "Cannot find comment with id 2";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        when(commentsRepository.getOne(id)).thenReturn(null);
        commentsService.getCommentDtoById(id);
    }

    @Test
    public void getPostId() {
        Post post = Mapper.dtoToPost(postDTO);
        Comments comments = Mapper.dtoToComments(commentsDTO);
        comments.setPost(post);
        Long id = post.getId();

        when(commentsRepository.getOne(comments.getId())).thenReturn(comments);
        Long postId = commentsService.getPostId(comments.getId());

        assertEquals(postId, id);
    }
}