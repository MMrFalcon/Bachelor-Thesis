package com.falcon.forum.repository;

import com.falcon.forum.persist.Tags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TagsRepositoryTestIT {

    @Autowired
    TagsRepository tagsRepository;

    @Test
    public void save() {
        Tags tags = new Tags();
        tags.setId(1L);
        tags.setTag("TagHere");

        Tags savedTag = tagsRepository.save(tags);

        assertNotNull(savedTag);
        assertEquals(tags.getTag(), savedTag.getTag());
    }

    @Test
    public void getOne() {
        Tags tags = new Tags();
        tags.setId(1L);
        tags.setTag("TagHere");

        Tags savedTag = tagsRepository.save(tags);
        Tags tagFromRepo = tagsRepository.getOne(savedTag.getId());

        assertNotNull(tagFromRepo);
        assertEquals(savedTag.getId(), tagFromRepo.getId());
        assertEquals(savedTag.getTag(), tagFromRepo.getTag());
    }

    @Test
    public void getAll() {

        Tags tags = new Tags();
        tags.setId(1L);
        tags.setTag("TagHere");

        Tags tags2 = new Tags();
        tags2.setId(2L);
        tags2.setTag("TagHere2");

        Tags savedTag = tagsRepository.save(tags);
        Tags secondSavedTag = tagsRepository.save(tags2);

        List<Tags> tagsList = tagsRepository.findAll();

        assertEquals(tagsList.size(), 2);
        assertTrue(tagsList.contains(savedTag));
        assertTrue(tagsList.contains(secondSavedTag));
    }

    @Test
    public void findByTag() {
        Tags tags = new Tags();
        tags.setId(1L);
        tags.setTag("TagHere");

        Tags tags2 = new Tags();
        tags2.setId(2L);
        tags2.setTag("TagHere2");

        tagsRepository.save(tags);
       Tags savedTag = tagsRepository.save(tags2);

       Optional<Tags> tagFromRepo = tagsRepository.findByTag(tags2.getTag());

       assertTrue(tagFromRepo.isPresent());
       assertEquals(tagFromRepo.get().getTag(), tags2.getTag());
       assertNotEquals(tagFromRepo.get().getTag(), tags.getTag());
       assertEquals(tagFromRepo.get(), savedTag);

    }
}