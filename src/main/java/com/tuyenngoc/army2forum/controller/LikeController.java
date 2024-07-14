package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "Like")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "API Toggle Like")
    @PostMapping(UrlConstant.Like.TOGGLE)
    public ResponseEntity<?> toggleLike(
            @PathVariable Long postId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(likeService.toggleLike(postId, userDetails.getPlayerId()));
    }

}
