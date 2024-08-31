package com.tuyenngoc.army2forum.domain.specification;

import com.tuyenngoc.army2forum.domain.entity.*;
import com.tuyenngoc.army2forum.util.SpecificationsUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class PlayerGiftCodeSpecification {

    public static Specification<PlayerGiftCode> filterByGiftCodeId(Long giftCodeId) {
        return (root, query, builder) -> {
            query.distinct(true);
            Join<PlayerGiftCode, GiftCode> giftCodeJoin = root.join(PlayerGiftCode_.giftCode);
            return builder.equal(giftCodeJoin.get(GiftCode_.id), giftCodeId);
        };
    }

    public static Specification<PlayerGiftCode> filterPlayerGiftCodes(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case "playerId" -> {
                        Join<PlayerGiftCode, Player> playerJoin = root.join(PlayerGiftCode_.player);

                        predicate = builder.and(predicate, builder.equal(playerJoin.get(Player_.id),
                                SpecificationsUtil.castToRequiredType(playerJoin.get(Player_.id).getJavaType(), keyword)));
                    }

                    case "username" -> {
                        Join<PlayerGiftCode, Player> playerJoin = root.join(PlayerGiftCode_.player);
                        Join<Player, User> userJoin = playerJoin.join(Player_.user);

                        predicate = builder.and(predicate, builder.like(userJoin.get(User_.username), "%" + keyword + "%"));
                    }
                }
            }
            return predicate;
        };
    }

}
