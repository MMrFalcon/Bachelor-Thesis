package Falcon.Repository

import Falcon.Persist.Tags
import org.springframework.stereotype.Repository

@Repository
interface TagsRepository extends BaseRepository<Tags, Long> {
    Optional<Tags> findByTag(String tag)
}