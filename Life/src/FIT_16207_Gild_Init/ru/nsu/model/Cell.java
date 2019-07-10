package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.*;

public class Cell {
    private boolean state;
    private int firstCount;
    private int secondCount;
    private float impact;
    private Point center;
    private boolean isEarlyChanged = false;

    public Cell(boolean state) {
        state = false;
        firstCount = 0;
        secondCount = 0;
        impact = 0;
    }

    public void checkState() {
        if (Constants.BIRTH_BEGIN <= impact && impact <= Constants.BIRTH_END) {
            this.state = true;
        }
        if (Constants.LIVE_BEGIN <= impact && impact <= Constants.LIVE_END) {
            this.state = true;
        }
        if (impact < Constants.LIVE_BEGIN) {
            this.state = false;
        }
        if (impact > Constants.LIVE_END) {
            this.state = false;
        }
    }
    public void countImpact() {
        this.impact = firstCount*Constants.FST_IMPACT + secondCount*Constants.SND_IMPACT;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void increaseFirstCount() {
        this.firstCount++;
    }

    public void decreaseFirstCount() {
        this.firstCount--;
    }

    public void increaseSecondCount() {
        this.secondCount++;
    }

    public void decreaseSecondCount() {
        this.secondCount--;
    }

    public void setImpact(float impact) {
        this.impact = impact;
    }

    public boolean isState() {
        return state;
    }

    public int getFirstCount() {
        return firstCount;
    }

    public int getSecondCount() {
        return secondCount;
    }

    public float getImpact() {
        return impact;
    }

    public void deleteNeighbors() {
        firstCount = 0;
        secondCount = 0;
    }
    public void setCenter(Point center) {
        this.center = center;
    }

    public boolean isEarlyChanged() {
        return isEarlyChanged;
    }

    public void setEarlyChanged(boolean nearlyChanged) {
        isEarlyChanged = nearlyChanged;
    }

    public Point getCenter() {
        return this.center;
    }
}
