package com.tuyenngoc.army2forum.domain.dto;

import com.tuyenngoc.army2forum.domain.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeDto {

    private int likeCount;

    private String latestLiker;

    public LikeDto(List<Like> likeList) {
        this.likeCount = likeList.size();
        if (likeCount > 0) {
            this.latestLiker = likeList.get(likeCount - 1).getPlayer().getUser().getUsername();
        }
    }

}
