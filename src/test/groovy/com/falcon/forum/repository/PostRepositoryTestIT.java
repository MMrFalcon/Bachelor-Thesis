package com.falcon.forum.repository;

import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTestIT {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    public void save() {

        User commentsAuthor = new User();
        commentsAuthor.setId(1L);
        commentsAuthor.setUsername("Falcon");
        commentsAuthor.setPassword("secret123");
        commentsAuthor.setEmail("mail@mail.com");

        User savedUser = userRepository.save(commentsAuthor);

        Post post = new Post();
        post.setId(2L);
        post.setTitle("Title");
        post.setContent("Content of the post");
        post.setUser(savedUser);

        Post savedPost = postRepository.save(post);

        assertNotNull(savedUser);
        assertEquals(savedPost.getUser(), savedUser);
        assertEquals(savedPost.getTitle(), post.getTitle());
    }

    @Test
    public void getOne() {
        User commentsAuthor = new User();
        commentsAuthor.setId(1L);
        commentsAuthor.setUsername("Falcon");
        commentsAuthor.setPassword("secret123");
        commentsAuthor.setEmail("mail@mail.com");

        User savedUser = userRepository.save(commentsAuthor);

        Post post = new Post();
        post.setId(2L);
        post.setTitle("Title");
        post.setContent("Content of the post");
        post.setUser(savedUser);

        Post savedPost = postRepository.save(post);
        Post postFromRepo = postRepository.getOne(savedPost.getId());

        assertNotNull(postFromRepo);
        assertEquals(postFromRepo.getUser(), savedUser);
        assertEquals(postFromRepo.getTitle(), post.getTitle());
    }

    @Test
    public void getAll() {
        User commentsAuthor = new User();
        commentsAuthor.setId(1L);
        commentsAuthor.setUsername("Falcon");
        commentsAuthor.setPassword("secret123");
        commentsAuthor.setEmail("mail@mail.com");

        User savedUser = userRepository.save(commentsAuthor);

        Post post = new Post();
        post.setId(2L);
        post.setTitle("Title");
        post.setContent("Content of the post");
        post.setUser(savedUser);


        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Title2");
        post2.setContent("Content of the post2");
        post2.setUser(savedUser);

        Post savedPost = postRepository.save(post);
        Post secondSavedPost = postRepository.save(post2);

        List<Post> postList = postRepository.findAll();

        assertEquals(postList.size(), 2);
        assertTrue(postList.contains(savedPost));
        assertTrue(postList.contains(secondSavedPost));
    }
}