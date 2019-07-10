package FIT_16207_Gild_Init.ru.nsu;

import FIT_16207_Gild_Init.ru.nsu.model.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.swing.*;

public class InitView extends JPanel {

    private BufferedImage imgLoad;
    private BufferedImage imgDetails;
    private BufferedImage imgFilter;
    private BufferedImage imgLoadCopy;
    private Point latestSquareCenter = new Point();

    InitView() {
        imgLoad = new BufferedImage(Constants.ZONE_SIZE, Constants.ZONE_SIZE, BufferedImage.TYPE_INT_ARGB);
        imgDetails = new BufferedImage(Constants.ZONE_SIZE, Constants.ZONE_SIZE, BufferedImage.TYPE_INT_ARGB);
        imgFilter = new BufferedImage(Constants.ZONE_SIZE, Constants.ZONE_SIZE, BufferedImage.TYPE_INT_ARGB);
        imgLoadCopy = new BufferedImage(Constants.ZONE_SIZE, Constants.ZONE_SIZE, BufferedImage.TYPE_INT_ARGB);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(imgLoad != null) {
                    getAndDrawZone(e.getX(), e.getY());
                    update();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (imgLoad != null) {
                    imageBackup();
                    update();
                }
            }

        });


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                imageBackup();
                if(latestSquareCenter.x != e.getX() && latestSquareCenter.y != e.getY()) {
                    getAndDrawZone(e.getX(), e.getY());
                    latestSquareCenter = new Point(e.getX(), e.getY());
                }
                update();
            }

        });
    }


    private void imageBackup() {
        ColorModel cm = imgLoadCopy.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = imgLoadCopy.copyData(null);
        imgLoadCopy = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        imgLoadCopy.copyData(imgLoad.getRaster());
    }

    private void getAndDrawZone(int x, int y) {
        if (y >= Constants.ZONE_Y_START && y <= Constants.ZONE_Y_END &&
                x >= Constants.ZONE_LOAD_LEFT && x <= Constants.ZONE_FILTER_RIGHT) {
            int xStart = ImageZone.calculateLeftFrameCornerX(x, imgLoad.getWidth(), imgLoad.getHeight());
            int yStart = ImageZone.calculateLeftFrameCornerY(y, imgLoad.getHeight(), imgLoad.getWidth());
            imgDetails = ImageZone.getZone(imgLoadCopy, xStart, yStart);
            if(imgLoad.getHeight() == Constants.ZONE_SIZE && imgLoad.getWidth() == Constants.ZONE_SIZE) {
                xStart = 0;
                yStart = 0;
            }
            ImageZone.drawSquare(imgLoad, xStart, yStart);
        }
    }


    private void update(){
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        drawZones(g);
        printInLoadZone(g);
        printInDetailsZone(g);
        printInFilterZone(g);
    }

    private void drawZones(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(Constants.ZONE_LOAD_LEFT, Constants.ZONE_Y_START, Constants.ZONE_LOAD_RIGHT, Constants.ZONE_Y_START);
        g.drawLine(Constants.ZONE_LOAD_RIGHT, Constants.ZONE_Y_START, Constants.ZONE_LOAD_RIGHT, Constants.ZONE_Y_END);
        g.drawLine(Constants.ZONE_LOAD_RIGHT, Constants.ZONE_Y_END, Constants.ZONE_LOAD_LEFT, Constants.ZONE_Y_END);
        g.drawLine(Constants.ZONE_LOAD_LEFT, Constants.ZONE_Y_END, Constants.ZONE_LOAD_LEFT, Constants.ZONE_Y_START);

        g.drawLine(Constants.ZONE_DETAILS_LEFT, Constants.ZONE_Y_START, Constants.ZONE_DETAILS_RIGHT, Constants.ZONE_Y_START);
        g.drawLine(Constants.ZONE_DETAILS_RIGHT, Constants.ZONE_Y_START, Constants.ZONE_DETAILS_RIGHT, Constants.ZONE_Y_END);
        g.drawLine(Constants.ZONE_DETAILS_RIGHT, Constants.ZONE_Y_END, Constants.ZONE_DETAILS_LEFT, Constants.ZONE_Y_END);
        g.drawLine(Constants.ZONE_DETAILS_LEFT, Constants.ZONE_Y_END, Constants.ZONE_DETAILS_LEFT, Constants.ZONE_Y_START);

        g.drawLine(Constants.ZONE_FILTER_LEFT, Constants.ZONE_Y_START, Constants.ZONE_FILTER_RIGHT, Constants.ZONE_Y_START);
        g.drawLine(Constants.ZONE_FILTER_RIGHT, Constants.ZONE_Y_START, Constants.ZONE_FILTER_RIGHT, Constants.ZONE_Y_END);
        g.drawLine(Constants.ZONE_FILTER_RIGHT, Constants.ZONE_Y_END, Constants.ZONE_FILTER_LEFT, Constants.ZONE_Y_END);
        g.drawLine(Constants.ZONE_FILTER_LEFT, Constants.ZONE_Y_END, Constants.ZONE_FILTER_LEFT, Constants.ZONE_Y_START);
    }

    void loadImage(File file) {
        try {
            imgLoad = ImageIO.read(file);
            imgLoadCopy = ImageIO.read(file);
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveImage(File file) {
        try {
            ImageIO.write(imgFilter, "bmp", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printInLoadZone(Graphics g) {
        if(imgLoad != null) {
            if(imgLoad.getWidth() <= Constants.ZONE_SIZE && imgLoad.getHeight() <= Constants.ZONE_SIZE) {
                g.drawImage(imgLoad, Constants.ZONE_LOAD_LEFT, Constants.ZONE_Y_START, Constants.ZONE_SIZE, Constants.ZONE_SIZE, this);
                return;
            }
            if((imgLoad.getWidth() >= Constants.ZONE_SIZE && imgLoad.getHeight() < Constants.ZONE_SIZE) ||
                    (imgLoad.getHeight() >= Constants.ZONE_SIZE && imgLoad.getWidth() < Constants.ZONE_SIZE) ||
                    (imgLoad.getWidth() >= Constants.ZONE_SIZE && imgLoad.getWidth() >= Constants.ZONE_SIZE)){
                BufferedImage resized = Resizer.resize(imgLoad);
                g.drawImage(imgLoad, Constants.ZONE_LOAD_LEFT, Constants.ZONE_Y_START, resized.getWidth(), resized.getHeight(), this);
            }
        }
    }

    private void printInDetailsZone(Graphics g) {
        if(imgDetails != null) {
            g.drawImage(imgDetails, Constants.ZONE_DETAILS_LEFT, Constants.ZONE_Y_START, Constants.ZONE_SIZE, Constants.ZONE_SIZE, this);
        }
    }

    private void printInFilterZone(Graphics g) {
        if(imgFilter != null) {
            g.drawImage(imgFilter, Constants.ZONE_FILTER_LEFT, Constants.ZONE_Y_START, Constants.ZONE_SIZE, Constants.ZONE_SIZE, this);
        }
    }

    void blackAndWhite() {
        if (imgDetails != null) {
            Filters filters = new Filters(imgDetails);
            imgFilter = filters.blackAndWhite();
            this.repaint();
        }
    }

    void negative() {
        if (imgDetails != null) {
            Filters filters = new Filters(imgDetails);
            imgFilter = filters.negative();
            this.repaint();
        }
    }

    void blur() {
        if (imgDetails != null) {
            Filters filters = new Filters(imgDetails);
            imgFilter = filters.blur();
            this.repaint();
        }
    }

    void sharpen() {
        if (imgDetails != null) {
            Filters filters = new Filters(imgDetails);
            imgFilter = filters.sharpen();
            this.repaint();
        }
    }

    void border() {
        if (imgDetails != null) {
            JTextField borderField = new JTextField("70", 17);

            JPanel options = new JPanel();
            options.add(new JLabel("border: "));
            options.add(borderField);

            int result = JOptionPane.showConfirmDialog(null, options, "Enter border", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String userInput = borderField.getText();
                if (userInput != null && userInput.length() > 0) {
                    try {
                        int border = Integer.parseInt(userInput);
                        imgFilter = BorderFilter.sobelOperator(imgDetails, border);
                        this.repaint();
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Please enter integer number", "Failure", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    void roberts() {
        if (imgDetails != null) {
            JTextField borderField = new JTextField("20", 17);

            JPanel options = new JPanel();
            options.add(new JLabel("border: "));
            options.add(borderField);

            int result = JOptionPane.showConfirmDialog(null, options, "Enter border", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String userInput = borderField.getText();
                if (userInput != null && userInput.length() > 0) {
                    try {
                        int border = Integer.parseInt(userInput);
                        imgFilter = BorderFilter.robertsOperatr(imgDetails, border);
                        this.repaint();
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Please enter integer number", "Failure", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }


    void stamping() {
        if (imgDetails != null) {
            Filters filters = new Filters(imgDetails);
            imgFilter = filters.stamping();
            this.repaint();
        }
    }

    void copyCtoB() {
        imgDetails = imgFilter;
        this.repaint();
    }

    void gamma() {
        if (imgDetails != null) {
            String userInput = JOptionPane.showInputDialog("Please input gamma != 0: ");
            if (userInput != null && userInput.length() >0) {
                try {
                    double gamma = Double.parseDouble(userInput);
                    Filters filters = new Filters(imgDetails);
                    imgFilter = filters.gammaCorrection(gamma);
                    this.repaint();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog( null, "Please enter double number", "Failure", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    void increase() {
        if (imgDetails != null) {
            imgFilter = Resizer.increase(imgDetails);
            this.repaint();
        }
    }

    void watercolor() {
        if(imgDetails != null) {
            imgFilter = Watercolor.watercolor(imgDetails);
            this.repaint();
        }
    }

    void rotate() {
        if(imgDetails != null) {
            String userInput = JOptionPane.showInputDialog("Please input angle >= 0: ");
            if (userInput != null && userInput.length() >0) {
                try {
                    int angle = Integer.parseInt(userInput);
                    imgFilter = Resizer.rotate(imgDetails, angle);
                    this.repaint();
                } catch (NumberFormatException e){
                    JOptionPane.showMessageDialog( null, "Please enter integer number", "Failure", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    void orderedDithering() {
        if(imgDetails != null) {
            imgFilter = OrderedDithering.dither(imgDetails);
            this.repaint();
        }
    }

    void floydSteinberg() {
        if(imgDetails != null) {
            JTextField rColors = new JTextField("2", 5);
            JTextField gColors = new JTextField("2", 5);
            JTextField bColors = new JTextField("2", 5);

            JPanel options = new JPanel();
            options.add(new JLabel("r: "));
            options.add(rColors);
            options.add(new JLabel("g: "));
            options.add(gColors);
            options.add(new JLabel("b: "));
            options.add(bColors);

            int result = JOptionPane.showConfirmDialog(null, options, "Enter r, g, b colors number", JOptionPane.OK_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION) {
                try {
                    int r = Integer.parseInt(rColors.getText());
                    int g = Integer.parseInt(gColors.getText());
                    int b = Integer.parseInt(bColors.getText());
                    if(r <= 1 || g <= 1 || b <= 1 ||
                        r > 255 || g > 255 || b > 255) {
                        throw new NumberFormatException();
                    }
                    FloydSteinberg floydSteinberg = new FloydSteinberg(imgDetails, r, g, b);
                    imgFilter = floydSteinberg.dither();
                    this.repaint();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog( null, "Please enter integer numbers", "Failure", JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    }
}
