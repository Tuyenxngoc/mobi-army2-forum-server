package com.tuyenngoc.army2forum.constant;

public enum SortByDataConstant implements SortByInterface {

    POST {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "id" -> "id";
                case "fullName" -> "fullName";
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
    }
}
