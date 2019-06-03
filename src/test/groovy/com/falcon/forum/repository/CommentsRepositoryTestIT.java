package com.falcon.forum.repository;

import com.falcon.forum.persist.Comments;
import com.falcon.forum.persist.Post;
import com.falcon.forum.persist.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentsRepositoryTestIT {

    @Autowired
    CommentsRepository commentsRepository;

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

        Post savedPost =  postRepository.save(post);

        Comments comments = new Comments();
        comments.setId(1L);
        comments.setPost(savedPost);
        comments.setUser(savedUser);
        comments.setCommentMessage("Answer message");

        Comments savedComment = commentsRepository.save(comments);

        assertEquals(savedComment.getCommentMessage(), comments.getCommentMessage());
        assertEquals(savedComment.getUser().getUsername(), commentsAuthor.getUsername());
        assertEquals(savedComment.getPost().getTitle(), post.getTitle());
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

        Post savedPost =  postRepository.save(post);

        Comments comments = new Comments();
        comments.setId(1L);
        comments.setPost(savedPost);
        comments.setUser(savedUser);
        comments.setCommentMessage("Answer message");

        Comments savedComment = commentsRepository.save(comments);
        Comments comment = commentsRepository.getOne(savedComment.getId());

        assertEquals(comment.getPost().getTitle(), post.getTitle());
        assertEquals(comment.getUser().getUsername(), savedUser.getUsername());
        assertEquals(comment.getCommentMessage(), comments.getCommentMessage());
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

        Post savedPost =  postRepository.save(post);

        Comments comments = new Comments();
        comments.setId(1L);
        comments.setPost(savedPost);
        comments.setUser(savedUser);
        comments.setCommentMessage("Answer message");

        Comments comments2 = new Comments();
        comments2.setId(2L);
        comments2.setPost(savedPost);
        comments2.setUser(savedUser);
        comments2.setCommentMessage("Answer message2");

        Comments savedComment = commentsRepository.save(comments);
        Comments secondSavedComment = commentsRepository.save(comments2);

       List<Comments> commentsList = commentsRepository.findAll();

       assertEquals(commentsList.size(), 2);
       assertTrue(commentsList.contains(savedComment));
       assertTrue(commentsList.contains(secondSavedComment));

    }
}