package com.company;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class getLaplac {
    static public int get(byte[] arr, int h, int w) {
        int l = CvType.CV_8UC1;
        Mat matImageGrey = new Mat(h, w, l);
        matImageGrey.put(0, 0, arr);
        Mat dst2 = new Mat();
        Mat laplacianImage = new Mat();
        dst2.convertTo(laplacianImage, l);
        Imgproc.Laplacian(matImageGrey, laplacianImage, CvType.CV_8U);
        Mat laplacianImage8bit = new Mat();
        laplacianImage.convertTo(laplacianImage8bit, l);

        byte[] pixels = new byte[laplacianImage8bit.width() * laplacianImage8bit.height()];
        laplacianImage8bit.get(0, 0, pixels);

        int maxLap = -16777216;

        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] > maxLap)
                maxLap = pixels[i];
        }

        return maxLap;
    }
}
