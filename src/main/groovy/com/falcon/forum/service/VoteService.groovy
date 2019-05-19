package com.falcon.forum.service

import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO

interface VoteService {
    void addVoteToPost(UserDTO voteAuthor, PostDTO post)
    void addVoteToComment(UserDTO voteAuthor, CommentsDTO comment)
}