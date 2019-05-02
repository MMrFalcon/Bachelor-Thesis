package Falcon.Repository


import Falcon.Persist.User

interface UserRepository extends BaseRepository<User, Long> {
    User findByUsername(String username)
    User findByEmail(String email)
}