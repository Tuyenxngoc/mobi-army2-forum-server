package com.tuyenngoc.army2forum.domain.specification;

import com.tuyenngoc.army2forum.domain.entity.Clan;
import com.tuyenngoc.army2forum.domain.entity.ClanMember;
import com.tuyenngoc.army2forum.domain.entity.ClanMember_;
import com.tuyenngoc.army2forum.domain.entity.Clan_;
import com.tuyenngoc.army2forum.util.SpecificationsUtil;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ClanSpecification {

    public static Specification<Clan> filterClans(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Clan_.ID -> predicate = builder.and(predicate, builder.equal(root.get(Clan_.id),
                            SpecificationsUtil.castToRequiredType(root.get(Clan_.id).getJavaType(), keyword)));

                    case Clan_.NAME -> predicate = builder.and(predicate, builder.like(root.get(Clan_.name), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }

    public static Specification<ClanMember> filterClanMembers(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case ClanMember_.ID -> predicate = builder.and(predicate, builder.equal(root.get(ClanMember_.id),
                            SpecificationsUtil.castToRequiredType(root.get(ClanMember_.id).getJavaType(), keyword)));
                }
            }
            return predicate;
        };
    }

}
