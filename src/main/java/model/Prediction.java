package model;

import org.postgresql.util.PGInterval;

public class Prediction {
    private String state;
    private PGInterval interval;
    private int index;

    public Prediction(String state, PGInterval interval, int index) {
        this.state = state;
        this.interval = interval;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public PGInterval getInterval() {
        return interval;
    }

    public void setInterval(PGInterval interval) {
        this.interval = interval;
    }
}
