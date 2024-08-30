package com.tuyenngoc.army2forum.domain.specification;

import com.tuyenngoc.army2forum.domain.entity.GiftCode;
import com.tuyenngoc.army2forum.domain.entity.GiftCode_;
import com.tuyenngoc.army2forum.util.SpecificationsUtil;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class GiftCodeSpecification {

    public static Specification<GiftCode> filterGiftCodes(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case GiftCode_.ID -> predicate = builder.and(predicate, builder.equal(root.get(GiftCode_.id),
                            SpecificationsUtil.castToRequiredType(root.get(GiftCode_.id).getJavaType(), keyword)));

                    case GiftCode_.CODE ->
                            predicate = builder.and(predicate, builder.like(root.get(GiftCode_.code), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }

}
