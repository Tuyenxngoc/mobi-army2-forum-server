package com.tuyenngoc.army2forum.util;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GifCreator {

    public static String createGif(BufferedImage image1, BufferedImage image2, String outputGifPath) {
        try {
            // Mở luồng đầu ra cho file GIF
            ImageOutputStream output = new FileImageOutputStream(new File(outputGifPath));

            // Khởi tạo GifSequenceWriter
            GifSequenceWriter writer = new GifSequenceWriter(output, image1.getType(), 500, true);

            // Thêm các khung hình (frames) vào GIF
            writer.writeToSequence(image1);
            writer.writeToSequence(image2);

            // Kết thúc quá trình ghi GIF
            writer.close();
            output.close();

            // Trả về đường dẫn GIF đã tạo
            return outputGifPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
