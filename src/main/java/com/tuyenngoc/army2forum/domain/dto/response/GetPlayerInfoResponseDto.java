package com.tuyenngoc.army2forum.domain.dto.response;

import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.util.MaskingUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlayerInfoResponseDto {

    private long id;

    private boolean online;

    private int xu;

    private int luong;

    private String email;

    private String phoneNumber;

    private List<GetPlayerCharacterResponseDto> characters;

    public GetPlayerInfoResponseDto(Player player) {
        this.id = player.getId();
        this.online = player.getIsOnline();
        this.xu = player.getXu();
        this.luong = player.getLuong();
        this.email = MaskingUtils.maskEmail(player.getUser().getEmail());
        this.phoneNumber = MaskingUtils.maskPhoneNumber(player.getUser().getPhoneNumber());
        this.characters = player.getPlayerCharacters().stream()
                .map(GetPlayerCharacterResponseDto::new)
                .collect(Collectors.toList());
    }

}
