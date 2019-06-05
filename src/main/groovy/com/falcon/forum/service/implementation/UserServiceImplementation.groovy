package com.falcon.forum.service.implementation

import com.falcon.forum.exception.DuplicateEmailException
import com.falcon.forum.exception.DuplicateUsernameException
import com.falcon.forum.exception.PasswordsException
import com.falcon.forum.exception.UserNotFoundException
import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.Comments
import com.falcon.forum.persist.Post
import com.falcon.forum.persist.User
import com.falcon.forum.repository.UserRepository
import com.falcon.forum.service.UserService
import groovy.util.logging.Slf4j
import org.postgresql.util.PSQLState
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

@Slf4j
@Service
class UserServiceImplementation extends BaseServiceImplementation<User, Long, UserRepository> implements UserService {

    private final UserRepository userRepository

    UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository
        super.setRepository(userRepository)
    }

    @Transactional
    @Override
    UserDTO createUser(UserDTO userDTO) {
        log.info("Transaction for user (creating): ${TransactionSynchronizationManager.isActualTransactionActive()} - " +
                "${TransactionSynchronizationManager.getCurrentTransactionName()}")
        if (userDTO == null)
            throw new NullPointerException("You are trying to save an empty Object!")
        else {

            try {
                UserDTO user = getUserByName(userDTO.getUsername())
                throw new DuplicateUsernameException("User ${user.username} already exist", PSQLState.DATA_ERROR)
            } catch (UserNotFoundException exception) {
                log.info("Adding usernem: ${userDTO.username} to new user")
            }

            try {
                getUserByEmail(userDTO.getEmail())
                throw new DuplicateEmailException("Email already exist", PSQLState.DATA_ERROR)
            } catch (UserNotFoundException exception) {
                log.info("Adding email ${userDTO.email} to new user")
            }

            if (passwordsEquals(userDTO.getPassword(), userDTO.getPasswordConfirmation())) {
                User user = save(Mapper.dtoToUser(userDTO))
                log.info("New user with id ${userRepository.getOne(user.getId())} was created")
                return Mapper.userToDTO(user)
            } else {
                throw new PasswordsException("Passwords are not the same")
            }

        }
    }

    @Override
    UserDTO getUserByName(String name) {
        log.info("Transaction for user (get user by name): ${TransactionSynchronizationManager.isActualTransactionActive()} - " +
                "${TransactionSynchronizationManager.getCurrentTransactionName()}")
        User userEntity = userRepository.findByUsername(name)
        try {
            UserDTO user = Mapper.userToDTO(userEntity)
            return user
        } catch (IllegalArgumentException exception) {
            throw new UserNotFoundException("Cannot find user with name ${name}")
        }
    }

    @Override
    UserDTO getUserByEmail(String email) {
        log.info("Transaction for user (get User by email): ${TransactionSynchronizationManager.isActualTransactionActive()} - " +
                "${TransactionSynchronizationManager.getCurrentTransactionName()}")
        try {
            User userEntity = userRepository.findByEmail(email)
            return Mapper.userToDTO(userEntity)
        } catch (IllegalArgumentException exception) {
            throw new UserNotFoundException("Cannot find user with email ${email}")
        }
    }

    @Override
    Long getCountOfCorrectAnswers(UserDTO userDTO) {
        User user = getOne(userDTO.getId())
        Long score = 0L
        user.getComments().each {
            Comments comments ->
                if (comments.isCorrect)
                    score += 1L
        }
        return score
    }


    @Override
    Long getAnswersPoints(UserDTO userDTO) {
        User user = getOne(userDTO.getId())
        Long score = 0L
        user.getComments().each {
            Comments comments ->
                    score += comments.getPoints()
        }
        return score
    }

    @Override
    Long getPostsPoints(UserDTO userDTO) {
        User user = getOne(userDTO.getId())
        Long score = 0L
        user.getPosts().each {
            Post post ->
                score += post.getPoints()
        }
        return score
    }

    @Override
    Long getNumberOfAddedPosts(UserDTO userDTO) {
        return getOne(userDTO.getId()).getPosts().size()
    }

    @Override
    Long getNumberOfAddedAnswers(UserDTO userDTO) {
        return getOne(userDTO.getId()).getComments().size()
    }

    @Override
    List<CommentsDTO> getCommentsDto(UserDTO userDTO) {
        def comments  = [] as List<CommentsDTO>
        User user = getOne(userDTO.getId())
        log.info("Searching for comments...")
        user.getComments().each {
            Comments commentsEntity ->
                comments.add(Mapper.commentsToDto(commentsEntity))
        }
        log.info("${comments.size()} comments found")
        comments.sort {a,b -> a.created<=>b.created}
        return comments
    }

    @Override
    List<PostDTO> getPostsDto(UserDTO userDTO) {
        def posts = [] as List<PostDTO>
        User user = getOne(userDTO.getId())
        log.info("Searching for posts")
        user.getPosts().each {
            Post post ->
                posts.add(Mapper.postToDto(post))
        }
        log.info("${posts.size()} posts found")
        posts.sort {a,b -> a.created<=>b.created}
        return posts
    }

    @Override
    boolean passwordsEquals(String password, String confPassword) {
        return password == confPassword
    }
}
