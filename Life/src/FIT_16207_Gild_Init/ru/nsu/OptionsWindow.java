package FIT_16207_Gild_Init.ru.nsu;

import FIT_16207_Gild_Init.ru.nsu.model.Constants;
import FIT_16207_Gild_Init.ru.nsu.model.Field;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;

public class OptionsWindow extends JFrame {

    private Field field;
    private JTextField heightField;
    private JTextField widthField;
    private JSlider widthSlider;
    private JTextField cellWidthText;
    private JSlider cellSizeSlider;
    private JTextField cellSizeField;
    private JTextField liveBeginField;
    private JTextField liveEndField;
    private JTextField birthBeginField;
    private JTextField birthEndField;
    private JTextField firstImpactField;
    private JTextField secondImpactField;

    public OptionsWindow(Field field) {
        this.field = field;
        JOptionPane optionsPane = new JOptionPane();
        optionsPane.setBorder(BorderFactory.createBevelBorder(2));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JPanel left = new JPanel();

        JPanel leftUp = new JPanel();
        leftUp.setLayout(new BoxLayout(leftUp, BoxLayout.X_AXIS));

        JPanel modePanel = new JPanel();
        JRadioButton replace = new JRadioButton("Replace");
        replace.setMnemonic(KeyEvent.VK_R);
        replace.setBounds(15, 15, 30, 25);
        replace.setActionCommand("ReplaceMode");
        if(field.getMode().equals("Replace")) {
            replace.setSelected(true);
        }

        replace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.setMode("Replace");
            }
        });

        JRadioButton XOR = new JRadioButton("XOR");
        XOR.setBounds(15, 40, 30, 25);
        XOR.setMnemonic(KeyEvent.VK_X);
        XOR.setActionCommand("XORMode");
        if(field.getMode().equals("XOR")) {
            XOR.setSelected(true);
        }

        XOR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.setMode("XOR");
            }
        });


        ButtonGroup modeChange = new ButtonGroup();
        modeChange.add(XOR);
        modeChange.add(replace);

        modePanel.add(replace);
        modePanel.add(XOR);

        modePanel.setLayout(new BoxLayout(modePanel, BoxLayout.Y_AXIS));
        modePanel.setBorder(BorderFactory.createTitledBorder("Modes"));

        JPanel fieldSizePanel = new JPanel(new GridBagLayout());

        heightField = new JTextField();
        heightField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyNumbersAllowed(e);
            }
        });

        heightField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = Integer.parseInt(heightField.getText());
                heightField.setText(String.valueOf(newSize(input, 1, 100)));
            }
        });
        addPanelToParameters(fieldSizePanel, heightField, String.valueOf(field.getHeight()), "Height  ");

        widthField = new JTextField();
        addPanelToParameters(fieldSizePanel, widthField, String.valueOf(field.getWidth()), "Width   ");

        widthField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyNumbersAllowed(e);
            }
        });

        widthField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = Integer.parseInt(widthField.getText());
                widthField.setText(String.valueOf(newSize(input, 1, 100)));
            }
        });


        fieldSizePanel.setLayout(new BoxLayout(fieldSizePanel, BoxLayout.Y_AXIS));
        fieldSizePanel.setBorder(BorderFactory.createTitledBorder("Field size"));

        leftUp.add(modePanel, new GridBagConstraints());
        leftUp.add(fieldSizePanel, new GridBagConstraints());

        JPanel lineWidth = new JPanel();
        lineWidth.setLayout(new BoxLayout(lineWidth, BoxLayout.Y_AXIS));

        cellWidthText = new JTextField(String.valueOf(field.getLineThickness()));
        widthSlider = new JSlider(JSlider.HORIZONTAL,1, 10, Integer.parseInt(cellWidthText.getText()));
        cellWidthText.setPreferredSize(new Dimension(25, 20));

        widthSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cellWidthText.setText(String.valueOf(widthSlider.getValue()));
            }
        });

        cellWidthText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = Integer.parseInt(cellWidthText.getText());
                cellWidthText.setText(String.valueOf(newSize(input, 1, 10)));
                widthSlider.setValue(input);
            }
        });

        lineWidth.add(widthSlider, new GridBagLayout());
        lineWidth.add(cellWidthText, new GridBagLayout());
        lineWidth.setBorder(BorderFactory.createTitledBorder("Line width"));

        JPanel cellSize = new JPanel();
        cellSize.setLayout(new BoxLayout(cellSize, BoxLayout.Y_AXIS));

        cellSizeField = new JTextField(String.valueOf(field.getCellSize()));
        cellSizeSlider = new JSlider(JSlider.HORIZONTAL,20, 100, Integer.parseInt(cellSizeField.getText()));
        cellSizeField.setPreferredSize(new Dimension(25, 20));

        cellSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cellSizeField.setText(String.valueOf(cellSizeSlider.getValue()));
            }
        });

        cellSizeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = Integer.parseInt(cellSizeField.getText());
                cellSizeField.setText(String.valueOf(newSize(input, 20, 100)));
                cellSizeSlider.setValue(input);
            }
        });

        cellSize.add(cellSizeSlider, new GridBagLayout());
        cellSize.add(cellSizeField, new GridBagLayout());
        cellSize.setBorder(BorderFactory.createTitledBorder("Cell size"));

        left.add(leftUp, new GridBagConstraints());
        left.add(lineWidth, new GridBagConstraints());
        left.add(cellSize, new GridBagConstraints());
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JPanel right = new JPanel(new GridBagLayout());

        JPanel liveParameters = new JPanel();
        liveParameters.setLayout(new BoxLayout(liveParameters, BoxLayout.Y_AXIS));
        liveParameters.setBorder(BorderFactory.createTitledBorder("Live parameters"));

        liveBeginField = new JTextField();
        addPanelToParameters(liveParameters, liveBeginField, String.valueOf(Constants.LIVE_BEGIN), "Live begin        ");
        liveBeginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyFloatsAllowed(e);
            }
        });

        liveEndField = new JTextField();
        addPanelToParameters(liveParameters, liveEndField, String.valueOf(Constants.LIVE_END), "Live end          ");
        liveEndField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyFloatsAllowed(e);
            }
        });

        birthBeginField = new JTextField();
        addPanelToParameters(liveParameters, birthBeginField, String.valueOf(Constants.BIRTH_BEGIN), "Birth begin       ");
        birthBeginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyFloatsAllowed(e);
            }
        });

        birthEndField = new JTextField();
        addPanelToParameters(liveParameters, birthEndField, String.valueOf(Constants.BIRTH_END), "Birth end         ");
        birthEndField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyFloatsAllowed(e);
            }
        });

        firstImpactField = new JTextField();
        addPanelToParameters(liveParameters, firstImpactField, String.valueOf(Constants.FST_IMPACT), "First impact     ");
        firstImpactField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyFloatsAllowed(e);
            }
        });

        secondImpactField = new JTextField();
        addPanelToParameters(liveParameters, secondImpactField, String.valueOf(Constants.SND_IMPACT), "Second impact ");
        secondImpactField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                onlyFloatsAllowed(e);
            }
        });

        right.add(liveParameters);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        mainPanel.add(left);
        mainPanel.add(right);
        optionsPane.setMessage(new Object[]{mainPanel});

        JDialog dialog = optionsPane.createDialog(this, "Options");
        dialog.setVisible(true);
        //field change after 'ok' pressed
        saveChanges(dialog);
    }

    private void onlyNumbersAllowed(KeyEvent e) {
        char c = e.getKeyChar();
        if(!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_BACK_SPACE))) {
            getToolkit().beep();
            e.consume();
        }
    }

    private void onlyFloatsAllowed(KeyEvent e) {
        char c = e.getKeyChar();
        if(!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_PERIOD))) {
            getToolkit().beep();
            e.consume();
        }
    }

    private void saveChanges(Dialog dialog) {
        int oldWidth = field.getWidth();
        int oldHeight = field.getHeight();

        field.setHeight(newSize(Integer.parseInt(heightField.getText()), 1, 100));
        field.setWidth(newSize(Integer.parseInt(widthField.getText()), 1, 100));
        field.setLineThickness(newSize(Integer.parseInt(cellWidthText.getText()), 1, 10));
        field.setCellSize(newSize(Integer.parseInt(cellSizeField.getText()), 20, 100));
        field.resize(oldHeight, oldWidth);

        try {
            Constants.setLiveBegin(Float.parseFloat(liveBeginField.getText()));
            Constants.setLiveEnd(Float.parseFloat(liveEndField.getText()));
            Constants.setBirthBegin(Float.parseFloat(birthBeginField.getText()));
            Constants.setBirthEnd(Float.parseFloat(birthEndField.getText()));
            Constants.setFstImpact(Float.parseFloat(firstImpactField.getText()));
            Constants.setSndImpact(Float.parseFloat(secondImpactField.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            dialog.setVisible(true);
        }
    }

    private static int newSize(int inputValue, int minValue,int maxValue) {
        if(inputValue < minValue) {
            inputValue = minValue;
        } if (inputValue > maxValue) {
            inputValue = maxValue;
        }
        return inputValue;
    }

    private static void addPanelToParameters(JPanel parametersPanel, JTextField textField, String fieldValue, String labelText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel label = new JLabel(labelText);
        textField.setText(fieldValue);
        textField.setPreferredSize(new Dimension(25, 20));
        panel.add(label, new GridBagConstraints());
        panel.add(textField, new GridBagConstraints());
        parametersPanel.add(panel, new GridBagConstraints());
    }
}
