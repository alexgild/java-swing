package FIT_16207_Gild_Init.ru.nsu;

import FIT_16207_Gild_Init.ru.nsu.model.Field;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.*;

public class InitMainWindow extends MainFrame {

    private Field field;
    private InitView initView;

    private Timer timer = new Timer(500, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            field.countCellsImpact();
            field.update();
            initView.update();
        }
    });

    public InitMainWindow()
    {
        super(900, 600, "FIT_16207_Gild_Init");
        try
        {
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/Save", "Save field and settings", KeyEvent.VK_S, "save");
            addMenuItem("File/Load", "Load field and settings from file", KeyEvent.VK_S, "load");
            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, "Exit.gif", "onExit");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Shows program version and copyright information", KeyEvent.VK_A, "About.gif", "onAbout");

            addSubMenu("Edit", KeyEvent.VK_E);
            addMenuItem("Edit/Mode", "Change mode replace/XOR", KeyEvent.VK_M, "XOR.png", "onModeChange");
            addMenuItem("Edit/Impact", "Show cell's impact", KeyEvent.VK_I, "ShowImpact.png", "showImpact");
            addMenuItem("Edit/Option", "Change parameters of the field", KeyEvent.VK_O, "Options.png", "openOptions");
            addMenuItem("Edit/Clear", "Clear the field", KeyEvent.VK_C, "Clear.png", "clear");

            addSubMenu("Run", KeyEvent.VK_R);
            addMenuItem("Run/Step", "Change the state of cells once", KeyEvent.VK_S, "OneStep.png", "makeStep");
            addMenuItem("Run/Run", "Change the state of cells continuous every 500 ms", KeyEvent.VK_R, "Run.png", "run");
            addMenuItem("Run/Stop", "Stop the process", KeyEvent.VK_S, "Stop.png", "stop");

            addToolBarButton("File/Exit");
            addToolBarButton("Help/About...");
            addToolBarButton("Edit/Mode");
            addToolBarButton("Edit/Impact");
            addToolBarButton("Edit/Clear");
            addToolBarButton("Edit/Option");
            addToolBarButton("Run/Step");
            addToolBarButton("Run/Run");
            addToolBarButton("Run/Stop");
            addToolBarSeparator();

            field = new Field(8, 20, 1, 72);
            initView = new InitView(field);
            initView.setPreferredSize(new Dimension(initView.getFieldWidth(), initView.getFieldHeight()));
            JScrollPane scrollPane = new JScrollPane(initView);
            scrollPane.setPreferredSize(new Dimension(900, 600));

            this.add(scrollPane);
            this.pack();
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

    public void onModeChange() {
        if(field.getMode().equals("Replace")) {
            field.setMode("XOR");
        } else if(field.getMode().equals("XOR")) {
            field.setMode("Replace");
        }
        initView.update();

    }

    public void showImpact() {
        if (!field.getImpact()) {
            field.setImpact(true);
        } else {
            field.setImpact(false);
        }
        initView.update();
    }

    public void makeStep() {
        if(timer.isRunning()) {
            return;
        }
        field.countCellsImpact();
        field.update();
        initView.update();
    }

    public void run() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void clear() {
        timer.stop();
        field.clear();
        initView.update();
    }

    public void save() {
        timer.stop();
        File f = FileUtils.getSaveFileName(this, "txt", "Text file");
        field.save(f.getPath());
    }

    public void load() {
        timer.stop();
        File f = FileUtils.getOpenFileName(this, "txt", "Text file");
        field = Field.load(f.getPath(), field);
        initView.changeField(field);
        initView.update();
    }

    public void openOptions() {
        OptionsWindow optionsWindow = new OptionsWindow(field);
        initView.update();
    }

    public static void main(String[] args)
    {
        InitMainWindow mainFrame = new InitMainWindow();
        mainFrame.setVisible(true);
    }
}