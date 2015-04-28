package edu.auburn.weedgenie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zachary on 2/5/15.
 * <p/>
 * This is a shell for the information obtained from forecast.io. This will be used in a ListView in MainActivity
 */
public class ListItem implements Serializable {
    private String name;
    private double threshold, gdd, startGDD;
    private Date endDate = null;
    private ArrayList<Double> pastData = new ArrayList<>();

    public ListItem(String name) {
        this.name = name;
    }

    public double getBase() {
        return base;
    }

    public void setBase(double base) {
        this.base = base;
    }

    private double base;

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
        if (gdd < 0)
            this.gdd = 0;
        else
            this.gdd = gdd;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setStartGDD(double startGDD) {
        this.startGDD = startGDD;
    }

    public double getStartGDD() {
        return startGDD;
    }

    public void addPastGDD(double gdd) {
        pastData.add(gdd);
    }

    public ArrayList<Double> getPastGDD() {
        return pastData;
    }

}
