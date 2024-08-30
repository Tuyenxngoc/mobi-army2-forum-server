package com.tuyenngoc.army2forum.constant;

public enum SortByDataConstant implements SortByInterface {

    USER {
        @Override
        public String getSortBy(String sortBy) {
            return "createdDate";
        }
    },

    PLAYER {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "id" -> "id";
                case "xu" -> "xu";
                case "luong" -> "luong";
                case "cup" -> "cup";
                case "lastModifiedDate" -> "lastModifiedDate";
                default -> "createdDate";
            };
        }
    },

    POST {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "id" -> "id";
                case "title" -> "title";
                case "viewCount" -> "viewCount";
                case "isLocked" -> "isLocked";
                case "category" -> "category";
                case "createdBy" -> "createdBy";
                case "lastModifiedDate" -> "lastModifiedDate";
                default -> "createdDate";
            };
        }
    },

    CATEGORY {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "id" -> "id";
                case "name" -> "name";
                case "createdBy" -> "createdBy";
                case "lastModifiedDate" -> "lastModifiedDate";
                default -> "createdDate";
            };
        }
    },

    NOTIFICATION {
        @Override
        public String getSortBy(String sortBy) {
            return "createdDate";
        }
    },

    CLAN {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "id" -> "id";
                case "name" -> "name";
                case "lastModifiedDate" -> "lastModifiedDate";
                case "createdDate" -> "createdDate";
                default -> "xp";
            };
        }
    },

    CLAN_MEMBER {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "id" -> "id";
                case "rights" -> "rights";
                case "xu" -> "xu";
                case "luong" -> "luong";
                case "clan_point" -> "clan_point";
                case "contribute_count" -> "contribute_count";
                case "contribute_time" -> "contribute_time";
                case "join_time" -> "join_time";
                default -> "xp";
            };
        }
    },
    GIFT_CODES {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "id" -> "id";
                case "code" -> "code";
                case "usageLimit" -> "usageLimit";
                case "xu" -> "xu";
                case "luong" -> "luong";
                case "exp" -> "exp";
                case "createdBy" -> "createdBy";
                case "lastModifiedBy" -> "lastModifiedBy";
                case "lastModifiedDate" -> "lastModifiedDate";
                default -> "createdDate";
            };
        }
    },
}
