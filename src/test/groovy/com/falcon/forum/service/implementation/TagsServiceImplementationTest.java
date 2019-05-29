package com.falcon.forum.service.implementation;

import com.falcon.forum.model.PostDTO;
import com.falcon.forum.model.TagsDTO;
import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.Tags;
import com.falcon.forum.repository.TagsRepository;
import com.falcon.forum.service.PostService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TagsServiceImplementationTest {

    private TagsServiceImplementation tagsService;
    private PostDTO postDTO;

    @Mock
    PostService postService;

    @Mock
    TagsRepository tagsRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tagsService = new TagsServiceImplementation(tagsRepository, postService);

        postDTO = new PostDTO();
        postDTO.setTitle("Title of post");
        postDTO.setId(1L);
        postDTO.setActive(true);

    }

    @Test
    public void generateTags() {
        String tags = "Java Groovy";

        Set<TagsDTO> tagsDTOS = tagsService.generateTags(tags);

        assertEquals(tagsDTOS.size(), 2);
    }

    @Test
    public void getPostTagsAsString() {
        Post post = new Post();
        post.setId(postDTO.getId());

        Tags tags = new Tags();
        tags.setId(1L);
        tags.setTag("first");
        tags.setCreatedDate(LocalDate.now());

        Tags tags2 = new Tags();
        tags2.setId(2L);
        tags2.setTag("second");
        tags.setCreatedDate(LocalDate.now().minusDays(1));

        post.getTags().add(tags);
        post.getTags().add(tags2);

        when(postService.getOne(postDTO.getId())).thenReturn(post);
        String tagsString = tagsService.getPostTagsAsString(postDTO.getId());

        assertNotNull(tagsString);
        assertEquals(tagsString, "second first ");
    }

    @Test
    public void getTagByName() {
        String tagName = "Test";
        Optional<Tags> tag = Optional.of(new Tags());
        tag.get().setTag(tagName);

        when(tagsRepository.findByTag(tagName)).thenReturn(tag);
        TagsDTO tagsDTO = tagsService.getTagByName(tagName);

        assertEquals(tagsDTO.getTag(), tagName);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getTagByNameIsNotPresent() {
        String tagName = "Test";
        Optional<Tags> tag = Optional.of(new Tags());
        tag.get().setTag(tagName);

        final String exceptionMessage = "Tag Other is not present!";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        tagsService.getTagByName("Other");
    }


    @Test
    public void getTagEntityByName() {
        String tagName = "Test";
        Optional<Tags> tag = Optional.of(new Tags());
        tag.get().setTag(tagName);

        when(tagsRepository.findByTag(tagName)).thenReturn(tag);
        Tags tags = tagsService.getTagEntityByName(tagName);

        assertEquals(tags.getTag(), tagName);
    }

    @Test
    public void getTagEntityByNameIsNotPresent() {
        String tagName = "Test";
        Optional<Tags> tag = Optional.of(new Tags());
        tag.get().setTag(tagName);

        final String exceptionMessage = "Tag Other is not present!";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(exceptionMessage);

        tagsService.getTagEntityByName("Other");
    }

    @Test
    public void isPresent() {
        String tagName = "Test";
        Optional<Tags> tag = Optional.of(new Tags());
        tag.get().setTag(tagName);

        when(tagsRepository.findByTag(tagName)).thenReturn(tag);
        boolean isTrue = tagsService.isPresent(tagName);

        assertTrue(isTrue);
    }

    @Test
    public void isNotPresent() {
        String tagName = "Test";
        Optional<Tags> tag = Optional.of(new Tags());
        tag.get().setTag(tagName);

        when(tagsRepository.findByTag(tagName)).thenReturn(tag);
        boolean isTrue = tagsService.isPresent("Other");

        assertFalse(isTrue);
    }
}