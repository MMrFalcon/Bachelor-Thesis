package com.falcon.forum.repository


import com.falcon.forum.persist.Post
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends BaseRepository<Post, Long>{

}