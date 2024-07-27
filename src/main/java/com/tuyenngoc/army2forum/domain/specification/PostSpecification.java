package com.tuyenngoc.army2forum.domain.specification;

import com.tuyenngoc.army2forum.domain.entity.*;
import com.tuyenngoc.army2forum.util.SpecificationsUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {

    public static Specification<Post> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Post_.title), title);
    }

    public static Specification<Post> isApprovedTrue() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get(Post_.isApproved));
    }

    public static Specification<Post> filterPosts(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Post_.ID -> predicate = builder.and(predicate, builder.equal(root.get(Post_.id),
                            SpecificationsUtil.castToRequiredType(root.get(Post_.id).getJavaType(), keyword)));

                    case Post_.TITLE -> predicate = builder.and(predicate, builder.like(root.get(Post_.title), "%" + keyword + "%"));

                    case Post_.PLAYER -> {
                        Join<Post, Player> postPlayerJoin = root.join(Post_.player);
                        Join<Player, User> playerUserJoin = postPlayerJoin.join(Player_.user);
                        Predicate likeUsername = builder.like(playerUserJoin.get(User_.username), "%" + keyword + "%");
                        predicate = builder.and(predicate, likeUsername);
                    }

                    case Post_.APPROVED_BY -> {
                        Join<Post, Player> postPlayerJoin = root.join(Post_.approvedBy);
                        Join<Player, User> playerUserJoin = postPlayerJoin.join(Player_.user);
                        Predicate likeUsername = builder.like(playerUserJoin.get(User_.username), "%" + keyword + "%");
                        predicate = builder.and(predicate, likeUsername);
                    }

                    case "categoryId" -> {
                        Join<Post, Category> postCategoryJoin = root.join(Post_.category);
                        predicate = builder.and(predicate, builder.equal(postCategoryJoin.get(Category_.id),
                                SpecificationsUtil.castToRequiredType(postCategoryJoin.get(Category_.id).getJavaType(), keyword)));
                    }

                    case "categoryName" -> {
                        Join<Post, Category> postCategoryJoin = root.join(Post_.category);
                        predicate = builder.and(predicate, builder.like(postCategoryJoin.get(Category_.name), "%" + keyword + "%"));
                    }

                    case Post_.IS_APPROVED -> {
                        predicate = builder.and(predicate, builder.equal(root.get(Post_.isApproved),
                                SpecificationsUtil.castToRequiredType(root.get(Post_.isApproved).getJavaType(), keyword)));
                    }

                    case Post_.IS_LOCKED -> {
                        predicate = builder.and(predicate, builder.equal(root.get(Post_.isLocked),
                                SpecificationsUtil.castToRequiredType(root.get(Post_.isLocked).getJavaType(), keyword)));
                    }

                    case Post_.VIEW_COUNT -> predicate = builder.and(predicate, builder.equal(root.get(Post_.viewCount),
                            SpecificationsUtil.castToRequiredType(root.get(Post_.viewCount).getJavaType(), keyword)));
                }
            }
            return predicate;
        };
    }
}
