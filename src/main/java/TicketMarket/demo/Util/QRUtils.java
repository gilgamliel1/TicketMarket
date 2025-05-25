package TicketMarket.demo.Util;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class QRUtils {

    /** 
     * Scans every page of the PDF for a QR code.
     * @return the first QR payload found, or null if none.
     */
    public static String extractQRCodeFromPDF(Path pdfPath) {
        try (PDDocument document = PDDocument.load(pdfPath.toFile())) {
            PDFRenderer renderer = new PDFRenderer(document);
            int pageCount = document.getNumberOfPages();

            for (int page = 0; page < pageCount; page++) {
                // render at 300 DPI for good QR fidelity
                BufferedImage image = renderer.renderImageWithDPI(page, 300);
                String qrText = decodeQRCode(image);
                if (qrText != null) {
                    return qrText;
                }
            }
        } catch (IOException e) {
            // I/O error reading PDF
            e.printStackTrace();
        }
        // no QR found
        return null;
    }
    public static String extractQRCodeFromImage(Path imagePath) throws IOException {
    BufferedImage img = ImageIO.read(imagePath.toFile());
    LuminanceSource source = new BufferedImageLuminanceSource(img);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    try {
        return new MultiFormatReader().decode(bitmap).getText();
    } catch (NotFoundException e) {
        return null;
    }
}


    /** 
     * Uses ZXing to decode a single BufferedImage.
     */
    private static String decodeQRCode(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            // no QR code in this image
            return null;
        }
    }
}
