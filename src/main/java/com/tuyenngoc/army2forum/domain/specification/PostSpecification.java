package com.tuyenngoc.army2forum.domain.specification;

import com.tuyenngoc.army2forum.domain.entity.Category_;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.domain.entity.Post_;
import com.tuyenngoc.army2forum.util.SpecificationsUtil;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {

    public static Specification<Post> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Post_.TITLE), title);
    }

    public static Specification<Post> filterPosts(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Post_.TITLE -> predicate = builder.and(predicate, builder.like(root.get(Post_.title), "%" + keyword + "%"));

                    case Post_.CATEGORY -> predicate = builder.and(predicate, builder.equal(root.get(Post_.category).get(Category_.id),
                            SpecificationsUtil.castToRequiredType(root.get(Post_.category).get(Category_.id).getJavaType(), keyword)));

                    case Post_.VIEW_COUNT -> predicate = builder.and(predicate, builder.equal(root.get(Post_.viewCount),
                            SpecificationsUtil.castToRequiredType(root.get(Post_.viewCount).getJavaType(), keyword)));
                }
            }
            return predicate;
        };
    }
}
