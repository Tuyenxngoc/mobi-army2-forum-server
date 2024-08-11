package com.tuyenngoc.army2forum.domain.specification;

import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Player_;
import com.tuyenngoc.army2forum.domain.entity.User;
import com.tuyenngoc.army2forum.domain.entity.User_;
import com.tuyenngoc.army2forum.util.SpecificationsUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class PlayerSpecification {

    public static Specification<Player> filterPlayers(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case "id" -> predicate = builder.and(predicate, builder.equal(root.get(Player_.id),
                            SpecificationsUtil.castToRequiredType(root.get(Player_.id).getJavaType(), keyword)));

                    case "username" -> {
                        Join<Player, User> playerUserJoin = root.join(Player_.user);
                        predicate = builder.and(predicate, builder.like(playerUserJoin.get(User_.username), "%" + keyword + "%"));
                    }
                }
            }
            return predicate;
        };
    }

}
