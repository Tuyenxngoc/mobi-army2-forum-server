package com.tuyenngoc.army2forum.constant;

public class UrlConstant {

    public static final String ADMIN_URL = "/admin";

    public static class Auth {
        private static final String PRE_FIX = "/auth";

        public static final String LOGIN = PRE_FIX + "/login";
        public static final String LOGOUT = PRE_FIX + "/logout";
        public static final String REGISTER = PRE_FIX + "/register";
        public static final String FORGET_PASSWORD = PRE_FIX + "/forget-password";
        public static final String CHANGE_PASSWORD = PRE_FIX + "/change-password";
        public static final String REFRESH_TOKEN = PRE_FIX + "/refresh-token";
        public static final String CONFIRM = PRE_FIX + "/confirm";
        public static final String RESEND_CONFIRMATION_EMAIL = PRE_FIX + "/resend-code";
        public static final String CHECK_EMAIL_CONFIRMED = PRE_FIX + "/check-email";
        public static final String OAUTH2_CALLBACK = "/login/oauth2/code/google";
    }

    public static class User {
        private static final String PRE_FIX = "/user";

        public static final String GET_CURRENT_USER = PRE_FIX + "/current";
        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }

    public static class Category {
        private static final String PRE_FIX = "/categories";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";

        public static final String ADMIN_GET_ALL = ADMIN_URL + PRE_FIX;
        public static final String ADMIN_GET_BY_ID = ADMIN_URL + PRE_FIX + "/{id}";
    }

    public static class Notification {
        private static final String PRE_FIX = "/notifications";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }

    public static class Comment {
        private static final String PRE_FIX = "/comments";

        public static final String CREATE = "/posts/{postId}" + PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String GET_BY_POST_ID = "/posts/{postId}" + PRE_FIX;

    }

    public static class Follow {
        private static final String PRE_FIX = "/follows";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }

    public static class Like {
        private static final String PRE_FIX = "/likes";

        public static final String TOGGLE = PRE_FIX + "/{postId}";
    }

    public static class Player {
        private static final String PRE_FIX = "/players";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";

        public static final String UPDATE_ROLE = PRE_FIX + "/{id}/role/{roleId}";
        public static final String UPDATE_SCHEDULE = PRE_FIX + "/{id}/schedule";
        public static final String GET_FOLLOWING = PRE_FIX + "/following-post";

        public static final String GET_PLAYER_INFO = PRE_FIX + "/info";

        public static final String TOGGLE_CHEST_LOCK = PRE_FIX + "/toggle-chest-lock";
        public static final String TOGGLE_INVITATION_LOCK = PRE_FIX + "/toggle-invitation-lock";
    }

    public static class PlayerNotification {
        private static final String PRE_FIX = "/player-notifications";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }

    public static class Post {
        private static final String PRE_FIX = "/posts";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";

        public static final String APPROVE = PRE_FIX + "/{id}/approve";
        public static final String LOCK = PRE_FIX + "/{id}/lock";
        public static final String FOLLOW = PRE_FIX + "/{id}/follow";

        public static final String ADMIN_GET_ALL = ADMIN_URL + PRE_FIX;
        public static final String ADMIN_GET_BY_ID = ADMIN_URL + PRE_FIX + "/{id}";

    }

    public static class Role {
        private static final String PRE_FIX = "/roles";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }

    public static class Clan {
        private static final String PRE_FIX = "/clans";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String GET_ICONS = PRE_FIX + "/icons";
        public static final String JOIN = PRE_FIX + "/{id}/join";
        public static final String LEAVE = PRE_FIX + "/{id}/leave";
        public static final String GET_MEMBERS = PRE_FIX + "/{id}/members";
    }

}
