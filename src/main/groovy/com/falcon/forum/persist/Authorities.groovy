package com.falcon.forum.persist

enum Authorities {
    READ_AUTHORITY {
        @Override
       String toString(){
            return "READ_AUTHORITY"
        }
    },

    WRITE_AUTHORITY {
        @Override
        String toString(){
            return "WRITE_AUTHORITY"
        }
    },

    ADMIN_AUTHORITY {
        @Override
        String toString(){
            return "ADMIN_AUTHORITY"
        }
    }
}