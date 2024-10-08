package com.tuyenngoc.army2forum.domain.dto.response.clan;

import com.tuyenngoc.army2forum.domain.dto.common.DateAuditingDto;
import com.tuyenngoc.army2forum.domain.entity.ClanApproval;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPendingApprovalsResponseDto extends DateAuditingDto {

    private long id;

    private String username;

    public GetPendingApprovalsResponseDto(ClanApproval clanApproval) {
        this.setCreatedDate(clanApproval.getCreatedDate());
        this.setLastModifiedDate(clanApproval.getLastModifiedDate());

        this.id = clanApproval.getId();
        this.username = clanApproval.getPlayer().getUser().getUsername();
    }

}
