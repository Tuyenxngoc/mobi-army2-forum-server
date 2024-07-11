package com.tuyenngoc.army2forum.controller;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePlayerScheduleRequestDto;
import com.tuyenngoc.army2forum.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "Player")
public class PlayerController {

    private final PlayerService playerService;

    @Operation(summary = "Update player roles")
    @PostMapping(UrlConstant.Player.UPDATE_ROLE)
    public ResponseEntity<?> updatePlayerRoles(@PathVariable Long id, @PathVariable Long roleId) {
        return VsResponseUtil.success(playerService.updatePlayerRoles(id, roleId));
    }

    @Operation(summary = "Update player schedule")
    @PostMapping(UrlConstant.Player.UPDATE_SCHEDULE)
    public ResponseEntity<?> updatePlayerSchedule(@PathVariable Long id, @RequestBody UpdatePlayerScheduleRequestDto requestDto) {
        playerService.updatePlayerSchedule(id, requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/create-gif")
    public ResponseEntity<InputStreamResource> createGif() throws IOException {
        // Tạo ảnh GIF từ hai ảnh
        BufferedImage bigImage = ImageIO.read(new File("src/main/resources/static/res/bigImage/bigImage0.png"));

        List<int[]> cropIndices = List.of(
                new int[]{0, 0, 100, 100},
                new int[]{100, 0, 100, 100}
        );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(byteArrayOutputStream);
        encoder.setRepeat(0);
        encoder.setDelay(500);

        for (int[] indices : cropIndices) {
            BufferedImage subImage = bigImage.getSubimage(indices[0], indices[1], indices[2], indices[3]);
            encoder.addFrame(subImage);
        }

        encoder.finish();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_GIF);
        headers.setContentDispositionFormData("attachment", "output.gif");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(byteArrayOutputStream.size())
                .body(inputStreamResource);
    }

}
