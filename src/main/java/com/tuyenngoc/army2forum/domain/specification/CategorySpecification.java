package com.tuyenngoc.army2forum.domain.specification;

import com.tuyenngoc.army2forum.domain.entity.Category;
import com.tuyenngoc.army2forum.domain.entity.Category_;
import com.tuyenngoc.army2forum.util.SpecificationsUtil;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

    public static Specification<Category> filterCategories(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Category_.ID -> predicate = builder.and(predicate, builder.equal(root.get(Category_.id),
                            SpecificationsUtil.castToRequiredType(root.get(Category_.id).getJavaType(), keyword)));

                    case Category_.NAME -> predicate = builder.and(predicate, builder.like(root.get(Category_.name), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }

}
