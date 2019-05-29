package com.falcon.forum.service.implementation;

import com.falcon.forum.model.PostDTO;
import com.falcon.forum.model.TagsDTO;
import com.falcon.forum.model.UserDTO;
import com.falcon.forum.persist.Comments;
import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.Tags;
import com.falcon.forum.repository.PostRepository;
import com.falcon.forum.service.CommentsService;
import com.falcon.forum.service.TagsService;
import com.falcon.forum.service.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class PostServiceImplementationTest {

    private PostServiceImplementation postService;
    private UserDTO userDTO;
    private PostDTO postDTO;

    @Mock
    PostRepository postRepository;

    @Mock
    UserService userService;

    @Mock
    TagsService tagsService;

    @Mock
    CommentsService commentsService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        postService = new PostServiceImplementation(userService, tagsService, postRepository, commentsService);

        userDTO = new UserDTO();
        userDTO.setUsername("Falcon");
        userDTO.setId(1L);

        postDTO = new PostDTO();
        postDTO.setTitle("Title of post");
        postDTO.setId(1L);
        postDTO.setActive(true);
    }

    @Test
    public void createPost() {

        when(userService.getOne(any())).thenReturn(Mapper.dtoToUser(userDTO));
        when(postRepository.save(any())).thenReturn(Mapper.dtoToPost(postDTO));
        PostDTO savedPost = postService.createPost(postDTO, userDTO);
        Post savedPostEntity = postRepository.save(Mapper.dtoToPost(postDTO));

        assertNotNull(savedPost);
        assertEquals(savedPost.getTitle(), savedPostEntity.getTitle());
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createPostWithoutUser() {
        final String exceptionMessage = "User name is missing";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        when(userService.getOne(any())).thenReturn(null);
        when(postRepository.save(any())).thenReturn(Mapper.dtoToPost(postDTO));
        postService.createPost(postDTO, userDTO);
        postRepository.save(Mapper.dtoToPost(postDTO));

    }


    @Test
    public void addPostNewTags() {
        Set<TagsDTO> tagsDTOS = new HashSet<>();

        TagsDTO firstTag = new TagsDTO();
        firstTag.setId(1L);
        firstTag.setTag("Tag1");
        tagsDTOS.add(firstTag);

        TagsDTO secondTag = new TagsDTO();
        secondTag.setId(2L);
        secondTag.setTag("Tag2");
        tagsDTOS.add(secondTag);

        Post post = new Post();
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setActive(true);

        when(postRepository.getOne(postDTO.getId())).thenReturn(post);
        PostDTO taggedPost = postService.addPostTags(postDTO.getId(), tagsDTOS);
        Post savedPost = postRepository.getOne(postDTO.getId());

        assertNotNull(taggedPost);
        //One because they are equal and Tags is a Set
        assertEquals(savedPost.getTags().size(), 1);
    }

    @Test
    public void addPostExistingTags() {
        Set<TagsDTO> tagsDTOS = new HashSet<>();
        TagsDTO firstTag = new TagsDTO();
        firstTag.setId(1L);
        firstTag.setTag("Tag1");
        tagsDTOS.add(firstTag);

        Post post = new Post();
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setActive(true);

        when(postRepository.getOne(postDTO.getId())).thenReturn(post);
        when(tagsService.isPresent(firstTag.getTag())).thenReturn(true);
        PostDTO taggedPost = postService.addPostTags(postDTO.getId(), tagsDTOS);
        Post savedPost = postRepository.getOne(postDTO.getId());

        assertNotNull(taggedPost);
        assertEquals(savedPost.getTags().size(), 1);
    }

    @Test
    public void addPostTagsWithNullTagsSet() {

        final String exceptionMessage = "Tags are empty";
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(exceptionMessage);

        Post post = new Post();
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setActive(true);

        when(postRepository.getOne(postDTO.getId())).thenReturn(post);
        PostDTO taggedPost = postService.addPostTags(postDTO.getId(), null);
        Post savedPost = postRepository.getOne(postDTO.getId());

        assertNotNull(taggedPost);
        assertEquals(savedPost.getTags().size(), 1);
    }

    @Test
    public void updatePostTags() {

        Tags tag = new Tags();
        tag.setId(1L);
        tag.setTag("Tag1");

        Post post = new Post();
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setActive(true);
        post.getTags().add(tag);
        post.getTags().add(new Tags());

        Set<TagsDTO> tagsDTOS = new HashSet<>();
        TagsDTO updated = new TagsDTO();
        updated.setId(2L);
        updated.setTag("Tag2");
        tagsDTOS.add(updated);

        when(postRepository.getOne(postDTO.getId())).thenReturn(post);
        PostDTO taggedPost = postService.updatePostTags(postDTO.getId(), tagsDTOS);
        Post savedPost = postRepository.getOne(postDTO.getId());

        assertNotNull(taggedPost);
        assertEquals(savedPost.getTags().size(), 1);

    }

    @Test
    public void updatePostTagsWithPostNull() {
        Set<TagsDTO> tagsDTOS = new HashSet<>();
        final String exceptionMessage = "Cannot find post with id "+postDTO.getId();
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        when(postRepository.getOne(postDTO.getId())).thenReturn(null);
        postService.updatePostTags(postDTO.getId(), tagsDTOS);
    }

    @Test
    public void updatePost() {
        Post oldPost = new Post();
        oldPost.setId(1L);
        oldPost.setTitle("Title old");
        oldPost.setContent("Old Content");
        oldPost.setActive(true);

        PostDTO newPost = new PostDTO();
        newPost.setId(1L);
        newPost.setTitle("New title");
        newPost.setContent("New Content");

        when(postRepository.getOne(1L)).thenReturn(oldPost);
        when(postRepository.save(oldPost)).thenReturn(Mapper.dtoToPost(newPost));
        PostDTO updatedPost = postService.updatePost(1L, newPost);

        assertEquals(updatedPost.getTitle(), newPost.getTitle());
        assertEquals(updatedPost.getContent(), newPost.getContent());

    }

    @Test
    public void updatePostWithNullParent() {
        final String exceptionMessage = "Cannot find post!";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        PostDTO newPost = new PostDTO();
        newPost.setId(1L);
        newPost.setTitle("New title");
        newPost.setContent("New Content");

        when(postRepository.getOne(1L)).thenReturn(null);
        postService.updatePost(1L, newPost);

    }

    @Test
    public void updatePostWithNullChild() {
        final String exceptionMessage = "Cannot find post!";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        Post oldPost = new Post();
        oldPost.setId(1L);
        oldPost.setTitle("Title old");
        oldPost.setContent("Old Content");
        oldPost.setActive(true);

        when(postRepository.getOne(1L)).thenReturn(oldPost);
        postService.updatePost(1L, null);

    }

    @Test
    public void isDeletable() {
        Post postEntity = new Post();
        postEntity.setUser(Mapper.dtoToUser(userDTO));
        postEntity.setId(postDTO.getId());

        when(postRepository.getOne(postDTO.getId())).thenReturn(postEntity);
        boolean shouldByTrue = postService.isDeletable(Mapper.postToDto(postEntity), userDTO.getUsername());

        assertTrue(shouldByTrue);

    }

    @Test
    public void isNotDeletableUsernameIsDifferent() {
        Post postEntity = new Post();
        postEntity.setUser(Mapper.dtoToUser(userDTO));
        postEntity.setId(postDTO.getId());

        when(postRepository.getOne(postDTO.getId())).thenReturn(postEntity);
        boolean shouldByFalse = postService.isDeletable(Mapper.postToDto(postEntity), "OtherName");

        assertFalse(shouldByFalse);
    }

    @Test
    public void isNotDeletablePostHasComments() {
        Post postEntity = new Post();
        postEntity.setUser(Mapper.dtoToUser(userDTO));
        postEntity.setId(postDTO.getId());
        postEntity.getComments().add(new Comments());

        when(postRepository.getOne(postDTO.getId())).thenReturn(postEntity);
        boolean shouldByFalse = postService.isDeletable(Mapper.postToDto(postEntity), userDTO.getUsername());

        assertFalse(shouldByFalse);
    }

    @Test
    public void isEditable() {
        Post postEntity = new Post();
        postEntity.setUser(Mapper.dtoToUser(userDTO));
        postEntity.setId(postDTO.getId());

        when(postRepository.getOne(postDTO.getId())).thenReturn(postEntity);
        boolean shouldByTrue = postService.isDeletable(Mapper.postToDto(postEntity), userDTO.getUsername());

        assertTrue(shouldByTrue);
    }

    @Test
    public void isNotEditable() {
        Post postEntity = new Post();
        postEntity.setUser(Mapper.dtoToUser(userDTO));
        postEntity.setId(postDTO.getId());

        when(postRepository.getOne(postDTO.getId())).thenReturn(postEntity);
        boolean shouldByFalse = postService.isDeletable(Mapper.postToDto(postEntity), "Other");

        assertFalse(shouldByFalse);
    }

    @Test
    public void isAuthor() {
        Post postEntity = new Post();
        postEntity.setUser(Mapper.dtoToUser(userDTO));
        postEntity.setId(postDTO.getId());

        when(postRepository.getOne(postDTO.getId())).thenReturn(postEntity);
        boolean isAuthor =  postService.isAuthor(Mapper.postToDto(postEntity), userDTO.getUsername());

        assertTrue(isAuthor);
    }

    @Test
    public void isNotAuthor() {
        Post postEntity = new Post();
        postEntity.setUser(Mapper.dtoToUser(userDTO));
        postEntity.setId(postDTO.getId());

        when(postRepository.getOne(postDTO.getId())).thenReturn(postEntity);
        boolean isNotAuthor =  postService.isAuthor(Mapper.postToDto(postEntity), "Other");

        assertFalse(isNotAuthor);
    }

    @Test
    public void getAuthorName() {
        Post postEntity = new Post();
        postEntity.setUser(Mapper.dtoToUser(userDTO));
        postEntity.setId(postDTO.getId());

        when(postRepository.getOne(postDTO.getId())).thenReturn(postEntity);
        String authorName = postService.getAuthorName(Mapper.postToDto(postEntity));

        assertEquals(userDTO.getUsername(), authorName);
    }

    @Test
    public void getPostDtoById() {
        Post postEntity = new Post();
        postEntity.setId(postDTO.getId());
        postEntity.setActive(true);

        when(postRepository.getOne(postDTO.getId())).thenReturn(postEntity);
        PostDTO post = postService.getPostDtoById(postDTO.getId());

        assertNotNull(post);
    }

    @Test
    public void getPostByAnswer() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Title");

        Comments comments = new Comments();
        comments.setPost(post);
        comments.setId(1L);

        when(commentsService.getOne(comments.getId())).thenReturn(comments);
        PostDTO postDTO = postService.getPostByAnswer(Mapper.commentsToDto(comments));

        assertNotNull(postDTO);
        assertEquals(postDTO.getTitle(), post.getTitle());
    }

}