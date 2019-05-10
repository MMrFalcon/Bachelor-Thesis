package com.falcon.forum.repository


import com.falcon.forum.persist.User

interface UserRepository extends BaseRepository<User, Long> {
    User findByUsername(String username)
    User findByEmail(String email)
}