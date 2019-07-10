package FIT_16207_Gild_Init.ru.nsu;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.*;

public class InitMainWindow extends MainFrame {

    private InitView initView;

    public InitMainWindow()
    {
        super(1125, 470, "FIT_16207_Gild_Init");
        try
        {
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/Save", "Save field and settings", KeyEvent.VK_S, "save");
            addMenuItem("File/Load", "Load field and settings from file", KeyEvent.VK_S, "load");
            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, "Exit.gif", "onExit");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Shows program version and copyright information", KeyEvent.VK_A, "About.gif", "onAbout");

            addSubMenu("Filter", KeyEvent.VK_F);
            addMenuItem("Filter/B&W", "Black&White filter", KeyEvent.VK_W, "B&W.png", "blackAndWhite");
            addMenuItem("Filter/Negative", "Negative filter", KeyEvent.VK_N, "Negative.png", "negative");
            addMenuItem("Filter/Blur", "Blur filter", KeyEvent.VK_B, "Blur.png", "blur");
            addMenuItem("Filter/Sharpen", "Sharpen filter", KeyEvent.VK_S, "Sharpen.png", "sharpen");
            addMenuItem("Filter/Border", "Sobel border filter", KeyEvent.VK_B, "Border.png", "border");
            addMenuItem("Filter/Roberts", "Roberts border filter", KeyEvent.VK_R, "Roberts.png", "roberts");
            addMenuItem("Filter/Stamping", "Stamping filter", KeyEvent.VK_S, "Stamping.png", "stamping");
            addMenuItem("Filter/Copy", "Copy C to B", KeyEvent.VK_C, "Copy.png", "copy");
            addMenuItem("Filter/Gamma", "Gamma correction", KeyEvent.VK_G, "Gamma.png", "gamma");
            addMenuItem("Filter/x2", "Make an image 2 times bigger", KeyEvent.VK_G, "x2.png", "increase");
            addMenuItem("Filter/Watercolor", "Watercolor filter", KeyEvent.VK_W, "Watercolor.png", "watercolor");
            addMenuItem("Filter/Rotate", "Rotate image", KeyEvent.VK_R, "Rotate.png", "rotate");
            addMenuItem("Filter/OrderedDithering", "Ordered Dithering", KeyEvent.VK_O, "OrderedDithering.png", "orderedDithering");
            addMenuItem("Filter/FloydSteinberg", "Floyd Steinberg Dithering", KeyEvent.VK_F, "FloydSteinberg.png", "floydSteinberg");

            addToolBarButton("File/Exit");
            addToolBarButton("Help/About...");
            addToolBarButton("Filter/B&W");
            addToolBarButton("Filter/Negative");
            addToolBarButton("Filter/Blur");
            addToolBarButton("Filter/Sharpen");
            addToolBarButton("Filter/Border");
            addToolBarButton("Filter/Roberts");
            addToolBarButton("Filter/Stamping");
            addToolBarButton("Filter/Copy");
            addToolBarButton("Filter/Gamma");
            addToolBarButton("Filter/x2");
            addToolBarButton("Filter/Watercolor");
            addToolBarButton("Filter/Rotate");
            addToolBarButton("Filter/OrderedDithering");
            addToolBarButton("Filter/FloydSteinberg");

            addToolBarSeparator();

            initView = new InitView();
            initView.setPreferredSize(new Dimension(1100, 800));

            this.add(initView);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void onAbout()
    {
        JOptionPane.showMessageDialog(this, "Init, version 1.0\nCopyright © 2019 Alexandra Gild, FIT, group 16207", "About Init", JOptionPane.INFORMATION_MESSAGE);
    }

    public void onExit()
    {
        System.exit(0);
    }


    public void save() {
        File f = FileUtils.getSaveFileName(this, "bmp", "Image file");
        initView.saveImage(f);
    }

    public void load() {
        File f = FileUtils.getOpenFileName(this, "bmp", "Image file");
        initView.loadImage(f);
    }

    public void blackAndWhite() {
        initView.blackAndWhite();
    }

    public void negative() {
        initView.negative();
    }

    public void blur() {
        initView.blur();
    }

    public void sharpen() {
        initView.sharpen();
    }

    public void border() {
        initView.border();
    }

    public void roberts(){initView.roberts();}

    public void stamping() {
        initView.stamping();
    }

    public void copy() {
        initView.copyCtoB();
    }

    public void gamma() {initView.gamma();}

    public void increase() {initView.increase();}

    public void watercolor() {initView.watercolor();}

    public void rotate() {initView.rotate();}

    public void orderedDithering() {initView.orderedDithering();}

    public void floydSteinberg() {initView.floydSteinberg();}

    public static void main(String[] args)
    {
        InitMainWindow mainFrame = new InitMainWindow();
        mainFrame.setVisible(true);
    }
}