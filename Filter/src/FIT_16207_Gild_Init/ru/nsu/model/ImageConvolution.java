package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.image.*;

class ImageConvolution
{
    private BufferedImage inputImage;
    private BufferedImage outputImage;
    private int kernelHeight;
    private int kernelWidth;
    private int kernelDivisior;
    private ArrayData kernel;

    ImageConvolution(BufferedImage inputImage, int kernelHeight, int kernelWidth, int kernelDivision, int[] kernelData) {
        this.inputImage = inputImage;
        this.kernelHeight = kernelHeight;
        this.kernelWidth = kernelWidth;
        this.kernelDivisior = kernelDivision;
        outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.kernel = new ArrayData(kernelData, kernelWidth, kernelHeight);
        filter();
    }

    public static class ArrayData
    {
        final int[] dataArray;
        final int width;
        final int height;

        ArrayData(int width, int height) {
            this(new int[width * height], width, height);
        }

        ArrayData(int[] dataArray, int width, int height) {
            this.dataArray = dataArray;
            this.width = width;
            this.height = height;
        }

        int get(int x, int y) {
            return dataArray[y * width + x];
        }

        void set(int x, int y, int value) {
            dataArray[y * width + x] = value;
        }
    }

    private static int bound(int value, int endIndex) {
        if (value < 0)
            return 0;
        if (value < endIndex)
            return value;
        return endIndex - 1;
    }

    private ArrayData convolve(ArrayData inputData, ArrayData kernel, int kernelDivisor) {
        int inputWidth = inputData.width;
        int inputHeight = inputData.height;
        int kernelWidthRadius = kernelWidth >>> 1;
        int kernelHeightRadius = kernelHeight >>> 1;

        ArrayData outputData = new ArrayData(inputWidth, inputHeight);
        for (int i = inputWidth - 1; i >= 0; i--) {
            for (int j = inputHeight - 1; j >= 0; j--) {
                double newValue = 0.0;
                for (int kw = kernelWidth - 1; kw >= 0; kw--) {
                    for (int kh = kernelHeight - 1; kh >= 0; kh--) {
                        newValue += kernel.get(kw, kh) * inputData.get(
                                bound(i + kw - kernelWidthRadius, inputWidth),
                                bound(j + kh - kernelHeightRadius, inputHeight));
                    }
                }
                outputData.set(i, j, (int)Math.round(newValue / kernelDivisor));
            }
        }
        return outputData;
    }

    private ArrayData[] getArrayDataFromImage(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] rgbData = inputImage.getRGB(0, 0, width, height, null, 0, width);
        ArrayData reds = new ArrayData(width, height);
        ArrayData greens = new ArrayData(width, height);
        ArrayData blues = new ArrayData(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgbValue = rgbData[y * width + x];
                reds.set(x, y, (rgbValue >>> 16) & 0xFF);
                greens.set(x, y, (rgbValue >>> 8) & 0xFF);
                blues.set(x, y, rgbValue & 0xFF);
            }
        }
        return new ArrayData[] { reds, greens, blues };
    }

    private void writeOutputImage(ArrayData[] redGreenBlue) {
        ArrayData reds = redGreenBlue[0];
        ArrayData greens = redGreenBlue[1];
        ArrayData blues = redGreenBlue[2];
        for (int y = 0; y < reds.height; y++)
        {
            for (int x = 0; x < reds.width; x++)
            {
                int red = bound(reds.get(x, y), 256);
                int green = bound(greens.get(x, y), 256);
                int blue = bound(blues.get(x, y), 256);
                outputImage.setRGB(x, y, (red << 16) | (green << 8) | blue | -0x01000000);
            }
        }
    }

    private void filter() {
        ArrayData[] dataArrays = getArrayDataFromImage(inputImage);
        for (int i = 0; i < dataArrays.length; i++) {
            dataArrays[i] = convolve(dataArrays[i], kernel, kernelDivisior);
        }
        writeOutputImage(dataArrays);
    }

    BufferedImage getOutputImage() {
        return outputImage;
    }
}