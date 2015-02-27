package edu.auburn.augdd;

import java.io.Serializable;

/**
 * Created by zachary on 2/5/15.
 */
public class WeatherItem implements Serializable {
    private int time;
    private double max, min;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }
}
