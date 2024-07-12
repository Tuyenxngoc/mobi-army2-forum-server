package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.request.NewCommentRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "Comment")
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @Operation(summary = "API Create Comment")
    @PostMapping(UrlConstant.Comment.CREATE)
    public ResponseEntity<?> createComment(
            @Valid @RequestBody NewCommentRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(commentService.createComment(userDetails.getPlayerId(), requestDto));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @Operation(summary = "API Update Comment")
    @PutMapping(UrlConstant.Comment.UPDATE)
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        return VsResponseUtil.success(commentService.updateComment(id, comment));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Delete Comment")
    @DeleteMapping(UrlConstant.Comment.DELETE)
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return VsResponseUtil.success(null);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @Operation(summary = "API Get Comment by Id")
    @GetMapping(UrlConstant.Comment.GET_BY_ID)
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        return VsResponseUtil.success(commentService.getCommentById(id));
    }

    @Operation(summary = "API Get All Comments")
    @GetMapping(UrlConstant.Comment.GET_ALL)
    public ResponseEntity<?> getAllComments() {
        return VsResponseUtil.success(commentService.getAllComments());
    }

    @Operation(summary = "API Get Comments by Post Id")
    @GetMapping(UrlConstant.Comment.GET_BY_POST_ID)
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
        return VsResponseUtil.success(commentService.getCommentsByPostId(postId));
    }
}