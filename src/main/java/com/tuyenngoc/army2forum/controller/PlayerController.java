package com.tuyenngoc.army2forum.controller;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

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
