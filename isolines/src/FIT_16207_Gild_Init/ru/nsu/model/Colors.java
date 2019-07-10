package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.*;
import java.util.ArrayList;

public class Colors {
    private ArrayList<Float> bounds;
    private ArrayList<Color> colors;
    private float boundsStep;

    public Colors(ParametersReader reader, ArrayList<Float> functionValue) {
        this.colors = reader.getColors();
        bounds = new ArrayList<>();
        calculateColorsBounds(functionValue);
    }

    public void calculateColorsBounds(ArrayList<Float> functionValue) {
        //ArrayList<Float> colorsBounds = new ArrayList<>();
        float currentValue = minValue(functionValue);
        float step = (maxValue(functionValue) - minValue(functionValue))/(float)(colors.size());
        this.boundsStep = step;

        currentValue += step;

        for(int i = 0; i < colors.size() - 1; i++) {
            bounds.add(currentValue);
            currentValue += step;
        }
        //return colorsBounds;
    }

    private float minValue(ArrayList<Float> functionValue) {
        float min = functionValue.get(0);
        for(Float current : functionValue) {
            if(current < min) {
                min = current;
            }
        }

        return min;
    }

    private float maxValue(ArrayList<Float> functionValue) {
        float max = functionValue.get(0);
        for(Float current : functionValue) {
            if (current > max) {
                max = current;
            }
        }

        return max;
    }

    public ArrayList<Float> getBounds() {
        return bounds;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    Float getStep() {
        return  this.boundsStep;
    }
}
