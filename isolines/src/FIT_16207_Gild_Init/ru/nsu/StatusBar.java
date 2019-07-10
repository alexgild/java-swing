package FIT_16207_Gild_Init.ru.nsu;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
    private JLabel functionValue = new JLabel();
    private JLabel mouseCoordinate = new JLabel();

    StatusBar() {
        this.add(new JLabel("Mouse coordinate: "));
        this.add(mouseCoordinate);
        this.add(new JLabel("Function value: "));
        this.add(functionValue);
    }

    void setFunctionValue(String functionValue) {
        this.functionValue.setText(functionValue);
    }

    void setMouseCoordinate(String mouseCoordinate) {
        this.mouseCoordinate.setText(mouseCoordinate);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
