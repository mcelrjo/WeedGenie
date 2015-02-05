package edu.auburn.augdd;

/**
 * Created by zachary on 2/5/15.
 * <p/>
 * This is a shell for the information obtained from forecast.io. This will be used in a ListView in MainActivity
 */
public class ListItem {
    private String name;
    private double threshold, gdd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getGdd() {
        return gdd;
    }

    public void setGdd(double gdd) {
        this.gdd = gdd;
    }
}
