package FIT_16207_Gild_Init.ru.nsu;

import FIT_16207_Gild_Init.ru.nsu.model.Colors;
import FIT_16207_Gild_Init.ru.nsu.model.Interpolation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Legend extends JPanel {
    private Colors colors;
    private BufferedImage legendField;

    public BufferedImage getLegend() {
        return legendField;
    }

    private BufferedImage legendFieldCopy;
    private ArrayList<Float> functionValue;
    private int offset = 20;
    private int xCellSize;
    private Interpolation interpolation;
    private boolean interpolationIsOn = false;
    private BufferedImage drawField;
    private DrawZone drawZone;

    Legend(Colors colors, ArrayList<Float> functionValue, DrawZone drawZone) {
        this.setPreferredSize(new Dimension(800, 100));
        this.setSize(new Dimension(800, 100));
        this.colors = colors;
        this.functionValue = functionValue;
        this.interpolation = new Interpolation(this);
        int size = colors.getColors().size();
        this.xCellSize = (this.getWidth() - 2* offset)/size;
        this.legendField = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.legendFieldCopy = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.drawZone = drawZone;
        this.drawField = drawZone.getDrawField();
        this.legendField = drawLegend(legendField);
        this.setVisible(true);
    }

    private BufferedImage drawLegend(BufferedImage image) {
        Graphics2D g2 = image.createGraphics();
        int boundsSize = colors.getBounds().size();
        int size = colors.getColors().size();
        int xStep = (this.getWidth() - 2 * offset)/size;

        g2.setColor(Color.BLACK);
        g2.drawRect(offset, offset, this.getWidth() - 2*offset - 1, this.getHeight() - offset);

        int xRectStart = offset + 1;

        for(int i = 0; i < size - 1; i++) {
            g2.setColor(colors.getColors().get(i));
            g2.fillRect(xRectStart, offset + 1, xStep, this.getHeight() - 2);
            xRectStart += xStep;
        }
        g2.setColor(colors.getColors().get(size-1));
        g2.fillRect(xRectStart, offset + 1, this.getWidth() - xStep *(size-1) - 2*offset, this.getHeight() - 2);
        int lastLineX = this.getWidth() - offset + 1;
        g2.setColor(Color.BLACK);
        g2.drawLine(lastLineX, offset + 1, lastLineX, this.getHeight() - 1);

        int current = offset + xStep;
        g2.setColor(Color.black);
        for(int i = 0; i < boundsSize; i++) {
            g2.drawString(String.valueOf(new DecimalFormat("####.##").format(colors.getBounds().get(i))), current, offset/2);
            current += xStep;
        }
        g2.dispose();
        return image;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.LIGHT_GRAY);
        g.drawImage(legendField, 0, 0, this);
    }

    private void update() {
        repaint();
    }

    void interpolate() {
        if(!interpolationIsOn) {
            interpolationIsOn = true;
            imageSave();
            legendField = interpolation.interpolate(offset, colors);
            drawZone.setDrawField(interpolation.interpolateField(drawField, functionValue, offset, colors));
            update();
        } else {
            imageBackup();
            interpolationIsOn = false;
            update();
        }
    }
    public int getxCellSize() {
        return xCellSize;
    }

    private void imageBackup() {
        legendField = imageCopy(legendFieldCopy, legendField);
    }

    private void imageSave() {
        legendFieldCopy = imageCopy(legendField, legendFieldCopy);
    }

    public void setDrawZone(DrawZone drawZone) {
        this.drawZone = drawZone;
    }

    public void setDrawField(BufferedImage drawField) {
        this.drawField = drawField;
    }

    public void setNewDrawFieldValue(ArrayList<Float> functionValue) {
        this.functionValue = functionValue;
    }

    private BufferedImage imageCopy(BufferedImage oldImage, BufferedImage newImage) {
        ColorModel cm = newImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = newImage.copyData(null);
        newImage = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        oldImage.copyData(newImage.getRaster());
        return newImage;
    }
}
