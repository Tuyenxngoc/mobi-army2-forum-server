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
        public static final String POST_DELETE_DETAIL = "success.notification.post.delete-detail";
        public static final String POST_APPROVED = "success.notification.post.approved";
        public static final String POST_APPROVE_DETAIL = "success.notification.post.approved-detail";
    }

    public static class Post {
        public static final String APPROVED = "success.post.approved";
        public static final String LOCKED = "success.post.lock";
        public static final String UNLOCKED = "success.post.unlock";
        public static final String UNFOLLOWED = "success.post.unfollowed";
        public static final String FOLLOWED = "success.post.followed";
        public static final String CREATE = "success.post.create";
        public static final String ADMIN_CREATE = "success.post.admin-create";
    }

    public static class Auth {
        public static final String LOGOUT = "success.user.logout";
    }

    public static class Clan {
        public static final String REQUEST_SUBMITTED = "success.clan.request.submitted";
        public static final String JOINED_SUCCESSFULLY = "success.clan.joined.successfully";
        public static final String LEAVE_SUCCESS = "success.clan.leave.successfully";
    }
}
