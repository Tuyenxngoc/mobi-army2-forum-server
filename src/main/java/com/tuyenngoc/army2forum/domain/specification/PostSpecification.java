package com.tuyenngoc.army2forum.domain.specification;

import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.domain.entity.Post_;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {

    public static Specification<Post> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Post_.TITLE), title);
    }

}
