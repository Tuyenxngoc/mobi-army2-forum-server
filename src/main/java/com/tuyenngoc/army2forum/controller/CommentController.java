package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.CommentRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Comment")
public class CommentController {

    CommentService commentService;

    @Operation(summary = "API Create Comment")
    @PostMapping(UrlConstant.Comment.CREATE)
    public ResponseEntity<?> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails

    ) {
        return VsResponseUtil.success(commentService.createComment(userDetails.getPlayerId(), postId, requestDto));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Update Comment")
    @PutMapping(UrlConstant.Comment.UPDATE)
    public ResponseEntity<?> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
        return VsResponseUtil.success(commentService.updateComment(id, requestDto));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Delete Comment")
    @DeleteMapping(UrlConstant.Comment.DELETE)
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        return VsResponseUtil.success(commentService.deleteComment(id));
    }

    @Operation(summary = "API Get Comments by Post Id")
    @GetMapping(UrlConstant.Comment.GET_BY_POST_ID)
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId, @ParameterObject PaginationRequestDto requestDto) {
        return VsResponseUtil.success(commentService.getCommentsByPostId(postId, requestDto));
    }

}