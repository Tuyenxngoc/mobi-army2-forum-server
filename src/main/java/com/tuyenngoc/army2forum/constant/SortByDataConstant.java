package com.tuyenngoc.army2forum.constant;

public enum SortByDataConstant implements SortByInterface {

    USER {
        @Override
        public String getSortBy(String sortBy) {
            return "createdDate";
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
                case "lastModifiedDate" -> "lastModifiedDate";
                case "createdDate" -> "createdDate";
                default -> "xp";
            };
        }
    },
}
