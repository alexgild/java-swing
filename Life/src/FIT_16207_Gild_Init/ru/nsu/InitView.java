package FIT_16207_Gild_Init.ru.nsu;

import FIT_16207_Gild_Init.ru.nsu.model.Field;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class InitView extends JPanel {

    private BufferedImage imgField;
    private Field field;

    public InitView(Field newField)
    {
        this.field = newField;
        int width = field.getWidth()*field.getCellSize() + field.getCellSize();
        int height = field.getHeight()*field.getCellSize() + field.getCellSize();
        imgField = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(imgField.getRGB(e.getX(), e.getY()) == Color.WHITE.getRGB() ||
                        imgField.getRGB(e.getX(), e.getY()) == Color.BLACK.getRGB()) {
                    return;
                }
                Point coordinates = field.getCellCoordinate(e.getX(), e.getY(), imgField.getRGB(e.getX(), e.getY()));
                if(field.getMode().equals("Replace") && !field.isAlive(coordinates.x, coordinates.y)) {
                    field.setState(true, coordinates.x, coordinates.y);
                } else if(field.getMode().equals("XOR")) {
                    if(field.isAlive(coordinates.x, coordinates.y)) {
                        field.setState(false, coordinates.x, coordinates.y);
                    } else {
                        field.setState(true, coordinates.x, coordinates.y);
                    }
                }
                field.countCellsImpact();
                update();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                return;
            }

        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(mouseIsOut(e.getX(), e.getY())) {
                    return;
                } else if(imgField.getRGB(e.getX(), e.getY()) == Color.WHITE.getRGB() ||
                        imgField.getRGB(e.getX(), e.getY()) == Color.BLACK.getRGB() || mouseIsOut(e.getX(), e.getY())){
                    return;
                }

                Point coordinates = field.getCellCoordinate(e.getX(), e.getY(), imgField.getRGB(e.getX(), e.getY()));

                if(field.getMode().equals("Replace") && !field.isAlive(coordinates.x, coordinates.y)) {
                    field.setState(true, coordinates.x, coordinates.y);
                } else if(field.getMode().equals("XOR")) {
                    if(field.isAlive(coordinates.x, coordinates.y) && !coordinates.equals(field.getLastChanged())) {
                        field.setState(false, coordinates.x, coordinates.y);
                        field.setLastChanged(coordinates);
                    } else if(!field.isAlive(coordinates.x, coordinates.y) && !coordinates.equals(field.getLastChanged())){
                        field.setState(true, coordinates.x, coordinates.y);
                        field.setLastChanged(coordinates);
                    }
                }

                field.countCellsImpact();
                update();
            }

            private boolean mouseIsOut(int x, int y) {
                if(x < 0 || x > imgField.getWidth()
                || y < 0 || y > imgField.getHeight()) {
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = imgField.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imgField.getWidth(), imgField.getHeight());
        g2d.dispose();
        FieldDraw.drawField(imgField, field);
        g.drawImage(imgField, 0, 0, this);
        this.setSize(imgField.getWidth(), imgField.getHeight());
    }

    public void update() {
        removeAll();
        int width = field.getWidth()*field.getCellSize() + field.getCellSize();
        int height = field.getHeight()*field.getCellSize() + field.getCellSize();
        imgField = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //this.setSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        FieldDraw.drawField(imgField, field);
        repaint();
    }

    public int getFieldWidth() {
        return field.getWidth() * field.getCellSize() ;
    }

    public int getFieldHeight() {
        return field.getHeight() * field.getCellSize();
    }


    public void changeField(Field newField) {
        this.field = newField;
        int width = field.getWidth()*field.getCellSize() + field.getCellSize();
        int height = field.getHeight()*field.getCellSize() + field.getCellSize();
        //this.setSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        imgField = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        field.countCellsImpact();
    }
}
