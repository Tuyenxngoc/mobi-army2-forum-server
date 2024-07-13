package com.tuyenngoc.army2forum.domain.dto.response;

import com.tuyenngoc.army2forum.domain.dto.PlayerDto;
import com.tuyenngoc.army2forum.domain.dto.common.UserDateAuditingDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentResponseDto extends UserDateAuditingDto {

    private PlayerDto player;

    private Long id;

    private String content;

    public GetCommentResponseDto(Comment comment) {
        this.player = new PlayerDto(comment.getPlayer());
        this.id = comment.getId();
        this.content = comment.getContent();
        this.setCreatedDate(comment.getCreatedDate());
        this.setLastModifiedDate(comment.getLastModifiedDate());
        this.setCreatedBy(comment.getCreatedBy());
        this.setLastModifiedBy(comment.getLastModifiedBy());
    }
}
