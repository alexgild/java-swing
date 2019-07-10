package FIT_16207_Gild_Init.ru.nsu;

import FIT_16207_Gild_Init.ru.nsu.model.Colors;
import FIT_16207_Gild_Init.ru.nsu.model.ParametersReader;

import java.awt.*;
import javax.swing.*;

public class InitView extends JPanel {

    private Legend legend;
    private DrawZone drawZone;

    InitView() {
        ParametersReader parametersReader = new ParametersReader("parameters.txt");
        StatusBar statusBar = new StatusBar();
        drawZone = new DrawZone(parametersReader, statusBar);
        Colors colors = new Colors(parametersReader, drawZone.getFunctionDraw().getFunctionValue());
        legend = new Legend(colors, drawZone.getFunctionDraw().getFunctionValue(), drawZone);
        statusBar.setSize(new Dimension(850, 30));
        statusBar.setPreferredSize(new Dimension(850, 30));

        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(drawZone);
        this.add(legend);
        this.add(statusBar);
        this.setSize(850, 650);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, drawZone.getWidth() + 10, drawZone.getHeight() + legend.getHeight() + 10);
    }

    void drawGrid() {
        drawZone.drawGrid();
        repaint();
    }

    public void gridParameters() {
        drawZone.gridParameters();
        repaint();
    }

    void interpolate() {
        drawZone.interpolate();
        legend.setDrawField(drawZone.getDrawField());
        legend.setNewDrawFieldValue(drawZone.getFunctionDraw().getFunctionValue());
        legend.interpolate();
        repaint();
    }

    void basicIsolines() {
        drawZone.basicIsolines();
    }

    void functionParameters() {
        drawZone.functionParameters();
        legend.setDrawZone(drawZone);
        repaint();
    }

    void isolines() {
        drawZone.drawIsolines();
        repaint();
    }

    void dotsMapping() {
        drawZone.drawDots();
        repaint();
    }

    void colorOff() {
        drawZone.colorOff();
        repaint();
    }
}
