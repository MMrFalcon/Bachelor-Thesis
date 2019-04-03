package Falcon.Service.Implementations

import Falcon.Exceptions.InactiveEntityException
import Falcon.Model.TagsDTO
import Falcon.Persist.Tags
import Falcon.Repository.TagsRepository
import Falcon.Service.TagsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagsServiceImplementation extends BaseServiceImplementation<Tags, Long, TagsRepository> implements  TagsService {

    @Autowired
    TagsRepository tagsRepository

    @Override
    TagsRepository getRepository() {
        tagsRepository
    }

    @Override
    TagsDTO createTag(TagsDTO tagsDTO) {
         return Mapper.tagsToDTO(saveAndFlush(Mapper.dtoToTags(tagsDTO)))
    }

    @Override
    void generateTags(Object tags, Long postId) {

        tags.each {
            tag ->
                try {
                    TagsDTO tagsDTO = new TagsDTO(
                            postId: postId,
                            tag: tag
                    )

                    createTag(tagsDTO)
                }catch(Exception ex) {
                    println(ex)
                }
        }

    }

    @Override
    void updateTags(Object oldTags, Object newTags, Long postId) {
       try {
           oldTags.each {
               TagsDTO oldTag ->
                   delete(oldTag.getId())
           }
       }catch(Exception ex) {
           println(ex)
       }


        try {
            generateTags(newTags, postId)
        }catch(NullPointerException nullPointer) {
            println(nullPointer)
        }catch(InactiveEntityException ex) {
            println(ex)
        }catch(Exception ex) {
            println(ex)
        }


    }

    @Override
    List<TagsDTO> getTagsByPostId(Long postId) {
        def tags = tagsRepository.findByPostId(postId) as List<Tags>

        return Mapper.transferTags(tags)
    }

    @Override
    void deleteTags(Object tags) {
            try {
                tags.each {
                    Tags tag ->
                        delete(tag.getId())
                }
            }catch(NullPointerException nullPointer) {
                println(nullPointer)
            }catch(InactiveEntityException inactiveEntity) {
                println(inactiveEntity)
            }catch(Exception ex) {
                println(ex)
            }

    }
}
