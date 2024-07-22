package com.tuyenngoc.army2forum.constant;

public class SuccessMessage {

    public static final String CREATE = "success.create";
    public static final String UPDATE = "success.update";
    public static final String DELETE = "success.delete";

    public static class User {
        public static final String CHANGE_PASSWORD = "success.user.change-password";
        public static final String FORGET_PASSWORD = "success.user.send.password";
        public static final String VERIFIED_SUCCESS = "success.user.verified";
        public static final String RESEND_CONFIRMATION = "success.user.resend";
    }

    public static class Notification {
        public static final String POST_DELETE = "success.notification.post.delete";
        public static final String POST_APPROVED = "success.notification.post.approved";
    }

    public static class Post {
        public static final String APPROVED = "success.post.approved";
        public static final String LOCKED = "success.post.lock";
        public static final String UNLOCKED = "success.post.unlock";
        public static final String UNFOLLOWED = "success.post.unfollowed";
        public static final String FOLLOWED = "success.post.followed";
    }

    public static class Auth {
        public static final String LOGOUT = "success.user.logout";
    }

}
