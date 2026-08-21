package com.kanchan.studentmanagementsystem.util;

import java.io.File;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRGenerator {

    public static String generateQRCode(String text, String fileName) {

        try {

            String folder = System.getProperty("user.dir")
                    + File.separator + "uploads"
                    + File.separator + "qr";

            File dir = new File(folder);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = folder + File.separator + fileName + ".png";

            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix matrix = writer.encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    200,
                    200);

            MatrixToImageWriter.writeToPath(
                    matrix,
                    "PNG",
                    new File(filePath).toPath());

            return fileName + ".png";

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}