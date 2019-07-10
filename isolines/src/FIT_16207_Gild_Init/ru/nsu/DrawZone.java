package FIT_16207_Gild_Init.ru.nsu;

import FIT_16207_Gild_Init.ru.nsu.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class DrawZone extends JPanel {

    private ParametersReader reader;
    private BufferedImage drawField;
    private BufferedImage drawFieldCopy;//reserve
    private FunctionDraw functionDraw;
    private Isolines isolines;
    private Grid grid;
    private Colors colors;

    private boolean gridIsOn = false;
    private boolean basicIsolinesIsOn = false;
    private boolean isolinesIsOn = false;
    private boolean dotsAreOn = false;
    private boolean interpolationIsOn = false;
    private boolean colorsAreOn = true;


    DrawZone(ParametersReader reader, StatusBar statusBar) {
        this.reader = reader;
        this.setPreferredSize(new Dimension(800, 500));
        this.setSize(new Dimension(800, 500));
        drawField = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        drawFieldCopy = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.functionDraw = new FunctionDraw(reader, drawField, this.getWidth(), this.getHeight(), -10, 10, -10, 10);
        functionDraw.drawFunction();
        grid = new Grid(drawField, 20, 20);
        this.colors = new Colors(reader, functionDraw.getFunctionValue());
        isolines = new Isolines(grid, functionDraw.getFunction(), this, reader.getIsolinesColor(), colors);
        imageSave();
        this.setVisible(true);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(isolinesIsOn) {
                    isolines.drawIsolines(e.getX(), e.getY());
                    update();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(isolinesIsOn) {
                    isolines.drawIsolines(e.getX(), e.getY());
                    update();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                float xCoord = CoordinateConvert.pixelToCoordinate(e.getX(), functionDraw.getFunction().getXDefinitionArea().x,
                        functionDraw.getFunction().getXDefinitionArea().y, 0, drawField.getWidth());

                xCoord  = (float) Math.round(xCoord * 100) / 100;
                float yCoord = CoordinateConvert.pixelToCoordinate(e.getY(), functionDraw.getFunction().getYDefinitionArea().x,
                        functionDraw.getFunction().getYDefinitionArea().y, 0, drawField.getHeight());

                yCoord  = (float) Math.round(yCoord * 100) / 100;
                statusBar.setMouseCoordinate("(" + xCoord + "; " + yCoord + ")");
                statusBar.setFunctionValue(String.valueOf((float) Math.round(functionDraw.getFunction().getValue(xCoord, yCoord) * 100)/100));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(colorsAreOn) {
            g.drawImage(drawField, 0, 0, this);
        }
        g.drawImage(grid.getGrid(), 0, 0, this);
        g.drawImage(isolines.getBasicIsolines(), 0, 0, this);
        g.drawImage(isolines.getUserIsolines(), 0, 0, this);
        g.drawImage(isolines.getDots(), 0, 0, this);
        functionDraw.updateImage(drawField);
    }

    FunctionDraw getFunctionDraw() {
        return this.functionDraw;
    }

    private void update() {
        repaint();
    }

    void gridParameters() {
        JTextField xCount = new JTextField(grid == null ? "20" : String.valueOf(grid.getxCount()), 5);
        JTextField yCount = new JTextField(grid == null ? "20" : String.valueOf(grid.getyCount()), 5);

        JPanel options = new JPanel();
        options.add(new JLabel("horizontal count: "));
        options.add(xCount);
        options.add(new JLabel("vertical count: "));
        options.add(yCount);

        int result = JOptionPane.showConfirmDialog(null, options, "Enter x and y grid's line count", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int x = Integer.parseInt(xCount.getText());
                int y = Integer.parseInt(yCount.getText());
                grid = new Grid(drawField, x, y);
                isolines = new Isolines(grid, functionDraw.getFunction(), this, reader.getIsolinesColor(), colors);
                clear();
                update();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter integer numbers", "Failure", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void drawGrid() {
        if (!gridIsOn) {
            gridIsOn = true;
            grid = new Grid(drawField, grid.getxCount(), grid.getyCount());
            grid.drawGrid();
            isolines.setGrid(grid);
            update();
        } else {
            gridIsOn = false;
            grid.off();
            update();
        }
    }

    void functionParameters() {
        JTextField xStart = new JTextField(String.valueOf(functionDraw.getFunction().getXDefinitionArea().x), 5);
        JTextField xEnd = new JTextField(String.valueOf(functionDraw.getFunction().getXDefinitionArea().y), 5);

        JTextField yStart = new JTextField(String.valueOf(functionDraw.getFunction().getYDefinitionArea().x), 5);
        JTextField yEnd = new JTextField(String.valueOf(functionDraw.getFunction().getYDefinitionArea().y), 5);

        JPanel options = new JPanel();
        options.add(new JLabel("x0: "));
        options.add(xStart);
        options.add(new JLabel("x1: "));
        options.add(xEnd);
        options.add(new JLabel("y0: "));
        options.add(yStart);
        options.add(new JLabel("y1: "));
        options.add(yEnd);

        int result = JOptionPane.showConfirmDialog(null, options, "Enter function's definition area", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int x0 = Integer.parseInt(xStart.getText());
                int x1 = Integer.parseInt(xEnd.getText());
                int y0 = Integer.parseInt(yStart.getText());
                int y1 = Integer.parseInt(yEnd.getText());


                this.functionDraw = new FunctionDraw(reader, drawField, this.getWidth(), this.getHeight(), x0, x1, y0, y1);
                functionDraw.drawFunction();

                //functionDraw.setxDefinitionArea(new Point(x0, x1));
                //functionDraw.setyDefinitionArea(new Point(y0, y1));
                //functionDraw.updateData();
                //functionDraw.drawFunction();
                imageSave();
                //functionDraw.setNewField(drawField);

                isolines = new Isolines(grid, functionDraw.getFunction(), this, reader.getIsolinesColor(), colors);
                clear();
                update();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter integer numbers", "Failure", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void drawIsolines() {
        isolinesIsOn = !isolinesIsOn;
        if(!isolinesIsOn) {
            isolines.isolinesOff();
            update();
        }
    }

    BufferedImage getDrawField() {
        return drawField;
    }

    void basicIsolines() {
        if (!basicIsolinesIsOn) {
            basicIsolinesIsOn = true;
            isolines.drawBasicIsolines();
            update();
        } else {
            basicIsolinesIsOn = false;
            isolines.basicISolinesOff();
            update();
        }
    }

    private void imageBackup() {
       drawField = imageCopy(drawFieldCopy, drawField);
    }

    private void imageSave() {
        drawFieldCopy = imageCopy(drawField, drawFieldCopy);
    }

    private BufferedImage imageCopy(BufferedImage oldImage, BufferedImage newImage) {
        ColorModel cm = newImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = newImage.copyData(null);
        newImage = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        oldImage.copyData(newImage.getRaster());
        return newImage;
    }

    void drawDots() {
        if(!dotsAreOn) {
            if(isolinesIsOn || basicIsolinesIsOn) {
                dotsAreOn = true;
                isolines.dotsMapping();
                update();
            }
        } else {
            dotsAreOn = false;
            isolines.dotsOff();
            update();
        }
    }

    void interpolate() {
        if(!interpolationIsOn) {
            interpolationIsOn = true;
            imageSave();
            update();
        } else {
            interpolationIsOn = false;
            imageBackup();
            update();
        }
    }

    void setDrawField(BufferedImage interpolatedField) {
        this.drawField = interpolatedField;
        update();
    }

    void colorOff() {
        if(colorsAreOn) {
            colorsAreOn = false;
            update();
        } else {
            colorsAreOn = true;

            imageBackup();
            update();
        }
    }

    private void clear() {
        isolines.isolinesOff();
        isolines.basicISolinesOff();
        isolines.dotsOff();
        grid.off();
        gridIsOn = false;
        isolinesIsOn = false;
        basicIsolinesIsOn = false;
        dotsAreOn = false;
    }
}