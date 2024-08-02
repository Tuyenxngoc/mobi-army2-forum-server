package com.tuyenngoc.army2forum.domain.specification;

import com.tuyenngoc.army2forum.domain.entity.*;
import com.tuyenngoc.army2forum.util.SpecificationsUtil;
import jakarta.persistence.criteria.Join;
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

                    case Clan_.MASTER -> {
                        Join<Clan, Player> clanMasterJoin = root.join(Clan_.master);
                        Join<Player, User> playerUserJoin = clanMasterJoin.join(Player_.user);

                        predicate = builder.and(predicate, builder.like(playerUserJoin.get(User_.username), "%" + keyword + "%"));
                    }
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
