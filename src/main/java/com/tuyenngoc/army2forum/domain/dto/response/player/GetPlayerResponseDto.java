package com.tuyenngoc.army2forum.domain.dto.response.player;

import com.tuyenngoc.army2forum.domain.dto.common.DateAuditingDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlayerResponseDto extends DateAuditingDto {

    private long id;

    private String username;

    private String roleName;

    private int xu;

    private int luong;

    private int cup;

    private int posts;

    private int comments;

    public GetPlayerResponseDto(Player player) {
        this.setCreatedDate(player.getCreatedDate());
        this.setLastModifiedDate(player.getLastModifiedDate());

        this.id = player.getId();
        this.username = player.getUser().getUsername();
        this.roleName = player.getUser().getRole().getName();
        this.xu = player.getXu();
        this.luong = player.getLuong();
        this.cup = player.getCup();
        this.posts = player.getPosts().size();
        this.comments = player.getComments().size();
    }

}
