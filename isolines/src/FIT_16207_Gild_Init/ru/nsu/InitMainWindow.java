package FIT_16207_Gild_Init.ru.nsu;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class InitMainWindow extends MainFrame {

    private InitView initView;

    private InitMainWindow()
    {
        super(850, 600, "Isolines");
        try
        {
            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Shows program version and copyright information", KeyEvent.VK_A, "About.gif", "onAbout");
            addToolBarButton("Help/About...");

            addSubMenu("Function", KeyEvent.VK_G);
            addMenuItem("Function/Parameters", "Change definition area", KeyEvent.VK_F, "FunctionParameters.png", "functionParameters");
            addMenuItem("Function/ColorOff", "Color off", KeyEvent.VK_C, "BW.png", "colorOff");
            addToolBarButton("Function/Parameters");
            addToolBarButton("Function/ColorOff");


            addSubMenu("Grid", KeyEvent.VK_G);
            addMenuItem("Grid/Grid", "Shows grid", KeyEvent.VK_G, "Grid.png", "grid");
            addMenuItem("Grid/Parameters", "Change grid parameters", KeyEvent.VK_P, "gridParameters.png", "gridParameters");
            addToolBarButton("Grid/Grid");
            addToolBarButton("Grid/Parameters");

            addSubMenu("Isolines", KeyEvent.VK_I);
            addMenuItem("Isolines/Isolines", "Show isolines by mouse click", KeyEvent.VK_I, "Isolines.png", "isolines");
            addMenuItem("Isolines/BasicIsolines", "Show basic n isolines", KeyEvent.VK_I, "BasicIsolines.png", "basicIsolines");
            addMenuItem("Isolines/DotsMapping", "Mapping dots on field", KeyEvent.VK_D, "DotsMap.png", "dotsMapping");
            addToolBarButton("Isolines/Isolines");
            addToolBarButton("Isolines/BasicIsolines");
            addToolBarButton("Isolines/DotsMapping");

            addSubMenu("Interpolation", KeyEvent.VK_L);
            addMenuItem("Interpolation/Interpolation", "Function's and legend's colors interpolate", KeyEvent.VK_L, "Interpolation.png", "interpolate");
            addToolBarButton("Interpolation/Interpolation");

            addToolBarSeparator();

            initView = new InitView();
            initView.setPreferredSize(new Dimension(830, 670));
            JScrollPane scrollPane = new JScrollPane(initView);
            scrollPane.setPreferredSize(new Dimension(830, 670));

            this.add(scrollPane);
            this.pack();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void grid() {
        initView.drawGrid();
        repaint();
    }

    public void gridParameters() {
        initView.gridParameters();
        repaint();
    }

    public void functionParameters() {
        initView.functionParameters();
    }

    public void onAbout()
    {
        JOptionPane.showMessageDialog(this, "Init, version 1.0\nCopyright © 2019 Alexandra Gild, FIT, group 16207", "About Init", JOptionPane.INFORMATION_MESSAGE);
    }

    public void isolines() {
        initView.isolines();
        repaint();
    }

    public void basicIsolines() {
        initView.basicIsolines();
        repaint();
    }

    public void interpolate() {
        initView.interpolate();
        repaint();
    }

    public void colorOff() {
        initView.colorOff();
    }

    public void dotsMapping() {
        initView.dotsMapping();
    }

    public void onExit()
    {
        System.exit(0);
    }

    public static void main(String[] args)
    {
        InitMainWindow mainFrame = new InitMainWindow();
        mainFrame.setVisible(true);
    }
}