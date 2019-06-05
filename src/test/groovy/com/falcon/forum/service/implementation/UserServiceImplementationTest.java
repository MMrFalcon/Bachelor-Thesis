package com.falcon.forum.service.implementation;

import com.falcon.forum.exception.DuplicateEmailException;
import com.falcon.forum.exception.DuplicateUsernameException;
import com.falcon.forum.exception.PasswordsException;
import com.falcon.forum.exception.UserNotFoundException;
import com.falcon.forum.model.CommentsDTO;
import com.falcon.forum.model.PostDTO;
import com.falcon.forum.model.UserDTO;
import com.falcon.forum.persist.Comments;
import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.User;
import com.falcon.forum.repository.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceImplementationTest {

    private UserServiceImplementation userService;
    private UserDTO userDTO;

    @Mock
    UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImplementation(userRepository);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("Falcon");
        userDTO.setActive(true);
        userDTO.setEmail("mail@mail.com");
        userDTO.setPoints(20L);
        userDTO.setMinusPoints(2L);
        userDTO.setPlusPoints(22L);
    }

    @Test
    public void createUser() {

        User user = new User();
        user.setId(1L);

        User mappedUser = Mapper.dtoToUser(userDTO);

        when(userRepository.getOne(any())).thenReturn(mappedUser);
        when(userRepository.save(any())).thenReturn(mappedUser);
        UserDTO createdUserDto = userService.createUser(userDTO);

        assertNotNull(createdUserDto);
        assertEquals(createdUserDto.getUsername(), userDTO.getUsername());
    }


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createUserPasswordsAreNotTheSame() {

        final String exceptionMessage = "Passwords are not the same";
        expectedException.expect(PasswordsException.class);
        expectedException.expectMessage(exceptionMessage);

        User user = new User();
        user.setId(1L);

        userDTO.setPassword("first321");
        userDTO.setPassword("second123");

        User mappedUser = Mapper.dtoToUser(userDTO);

        when(userRepository.getOne(any())).thenReturn(mappedUser);
        userService.createUser(userDTO);

    }


    @Test
    public void createUserUsernameAlreadyExist() {
        final String exceptionMessage = "User " + userDTO.getUsername() + " already exist";
        expectedException.expect(DuplicateUsernameException.class);
        expectedException.expectMessage(exceptionMessage);

        User user = new User();
        user.setId(1L);

        User mappedUser = Mapper.dtoToUser(userDTO);

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(mappedUser);
        userService.createUser(userDTO);

    }

    @Test
    public void createUserEmailAlreadyExist() {
        final String exceptionMessage = "Email already exist";
        expectedException.expect(DuplicateEmailException.class);
        expectedException.expectMessage(exceptionMessage);

        User user = new User();
        user.setId(1L);

        User mappedUser = Mapper.dtoToUser(userDTO);

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(mappedUser);
        userService.createUser(userDTO);

    }

    @Test
    public void createUserFromNullDTO() {
        final String exceptionMessage = "You are trying to save an empty Object!";
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(exceptionMessage);

        userService.createUser(null);
    }

    @Test
    public void getUserByName() {
        User mappedUser = Mapper.dtoToUser(userDTO);

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(mappedUser);
        UserDTO user = userService.getUserByName(userDTO.getUsername());

        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @Test
    public void getUserByNameDoesNotExist() {
        final String exceptionMessage = "Cannot find user with name " + userDTO.getUsername();
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage(exceptionMessage);

        userService.getUserByName(userDTO.getUsername());

    }

    @Test
    public void getUserByEmail() {
        User mappedUser = Mapper.dtoToUser(userDTO);

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(mappedUser);
        UserDTO user = userService.getUserByEmail(userDTO.getEmail());

        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    public void getUserByEmailDoesNotExist() {
        final String exceptionMessage = "Cannot find user with email " + userDTO.getEmail();
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage(exceptionMessage);

        userService.getUserByEmail(userDTO.getEmail());
    }

    @Test
    public void getCountOfCorrectAnswers() {
        Comments comments = new Comments();
        comments.setIsCorrect(true);

        Comments notCorrectComment = new Comments();
        notCorrectComment.setIsCorrect(false);

        User user = new User();
        user.setActive(true);
        user.getComments().add(comments);
        user.getComments().add(notCorrectComment);

        when(userRepository.getOne(any())).thenReturn(user);

        Long userScore = userService.getCountOfCorrectAnswers(userDTO);
        Long score = 1L;

        assertEquals(userScore, score);
    }

    @Test
    public void getAnswersPoints() {
        Comments comments = new Comments();
        comments.setPoints(4L);

        Comments notCorrectComment = new Comments();
        notCorrectComment.setPoints(2L);

        User user = new User();
        user.setActive(true);
        user.getComments().add(comments);
        user.getComments().add(notCorrectComment);

        when(userRepository.getOne(any())).thenReturn(user);

        Long userScore = userService.getAnswersPoints(userDTO);
        Long score = 6L;

        assertEquals(userScore, score);
    }

    @Test
    public void getPostsPoints() {
        Post post = new Post();
        post.setPoints(2L);

        Post secondPost = new Post();
        secondPost.setPoints(1L);

        User user = new User();
        user.setActive(true);
        user.getPosts().add(post);
        user.getPosts().add(secondPost);

        when(userRepository.getOne(any())).thenReturn(user);

        Long userScore = userService.getPostsPoints(userDTO);
        Long score = 3L;

        assertEquals(userScore, score);
    }

    @Test
    public void getNumberOfAddedPosts() {
        User user = new User();
        user.setActive(true);
        user.getPosts().add(new Post());
        user.getPosts().add(new Post());

        when(userRepository.getOne(any())).thenReturn(user);

        Long userPostsSize = userService.getNumberOfAddedPosts(userDTO);
        Long score = 2L;

        assertEquals(userPostsSize, score);
    }

    @Test
    public void getNumberOfAddedAnswers() {
        User user = new User();
        user.setActive(true);
        user.getComments().add(new Comments());

        when(userRepository.getOne(any())).thenReturn(user);
        Long userCommentsSize = userService.getNumberOfAddedAnswers(userDTO);
        Long score = 1L;

        assertEquals(userCommentsSize, score);
        verify(userRepository, times(1)).getOne(any());
    }

    @Test
    public void getCommentsDto() {
        User user = new User();
        user.setActive(true);
        user.setId(1L);

        Comments comments = new Comments();
        comments.setId(1L);
        comments.setCommentMessage("Mess1");
        comments.setCreatedDate(LocalDate.now().minusDays(1));

        Comments secondComments = new Comments();
        secondComments.setId(2L);
        secondComments.setCommentMessage("Mess2");
        comments.setCreatedDate(LocalDate.now());

        user.getComments().add(comments);
        user.getComments().add(secondComments);

        when(userRepository.getOne(userDTO.getId())).thenReturn(user);
        List<CommentsDTO> commentsDTOS = userService.getCommentsDto(userDTO);

        assertEquals(commentsDTOS.size(), 2);
        assertEquals(commentsDTOS.get(1).getCommentMessage(), comments.getCommentMessage());
        verify(userRepository, times(1)).getOne(userDTO.getId());
    }

    @Test
    public void getPostsDto() {
        User user = new User();
        user.setId(1L);
        user.setActive(true);

        Post post = new Post();
        post.setTitle("first");
        post.setId(1L);
        post.setCreatedDate(LocalDate.now());

        Post secondPost = new Post();
        post.setTitle("second");
        post.setId(2L);
        post.setCreatedDate(LocalDate.now().minusDays(1));

        user.getPosts().add(post);
        user.getPosts().add(secondPost);

        when(userRepository.getOne(userDTO.getId())).thenReturn(user);
        List<PostDTO> postDTOS = userService.getPostsDto(userDTO);

        assertEquals(postDTOS.size(), 2);
        assertEquals(postDTOS.get(0).getTitle(), secondPost.getTitle());
        verify(userRepository, times(1)).getOne(userDTO.getId());
    }

    @Test
    public void passwordsEquals() {
        String firstPass = "pass123";
        String secondPass = "pass123";
        String thirdPass = "pass321";

        boolean shouldBeTrue = userService.passwordsEquals(firstPass, secondPass);
        boolean shouldBeFalse = userService.passwordsEquals(firstPass, thirdPass);

        assertTrue(shouldBeTrue);
        assertFalse(shouldBeFalse);
    }
}