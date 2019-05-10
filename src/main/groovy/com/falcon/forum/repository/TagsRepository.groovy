package com.falcon.forum.repository


import com.falcon.forum.persist.Tags
import org.springframework.stereotype.Repository

@Repository
interface TagsRepository extends BaseRepository<Tags, Long> {
    Optional<Tags> findByTag(String tag)
}