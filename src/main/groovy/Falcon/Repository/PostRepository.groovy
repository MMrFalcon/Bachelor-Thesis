package Falcon.Repository

import Falcon.Persist.Post
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends BaseRepository<Post, Long>{

}