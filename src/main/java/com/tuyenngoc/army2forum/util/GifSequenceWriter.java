package com.tuyenngoc.army2forum.util;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GifSequenceWriter {

    private final ImageWriter gifWriter;
    private final ImageWriteParam imageWriteParam;
    private final IIOMetadata imageMetaData;

    public GifSequenceWriter(
            ImageOutputStream outputStream,
            int imageType,
            int timeBetweenFramesMS,
            boolean loopContinuously) throws IOException {

        gifWriter = ImageIO.getImageWritersBySuffix("gif").next();
        imageWriteParam = gifWriter.getDefaultWriteParam();

        ImageTypeSpecifier imageTypeSpecifier = new ImageTypeSpecifier(new BufferedImage(1, 1, imageType));
        imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);

        String metaFormatName = imageMetaData.getNativeMetadataFormatName();

        IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);

        IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");

        graphicsControlExtensionNode.setAttribute("disposalMethod", "restoreToBackgroundColor");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(timeBetweenFramesMS / 10));
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

        IIOMetadataNode applicationExtensionsNode = getNode(root, "ApplicationExtensions");
        IIOMetadataNode applicationExtension = new IIOMetadataNode("ApplicationExtension");

        applicationExtension.setAttribute("applicationID", "NETSCAPE");
        applicationExtension.setAttribute("authenticationCode", "2.0");

        byte[] loop = new byte[]{0x1, (byte) (loopContinuously ? 0x0 : 0x1), 0x0};
        applicationExtension.setUserObject(loop);
        applicationExtensionsNode.appendChild(applicationExtension);
        imageMetaData.setFromTree(metaFormatName, root);

        gifWriter.setOutput(outputStream);
        gifWriter.prepareWriteSequence(null);
    }

    public void writeToSequence(BufferedImage img) throws IOException {
        gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam);
    }

    public void close() throws IOException {
        gifWriter.endWriteSequence();
    }

    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) {
            if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                return (IIOMetadataNode) rootNode.item(i);
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return node;
    }

}
