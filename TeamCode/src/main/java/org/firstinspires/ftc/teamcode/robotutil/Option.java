package org.firstinspires.ftc.teamcode.robotutil;

public class Option {

    private boolean quantitative;
    private String name;

    // Quantitative
    private double min;
    private double max;
    private double step;

    private double value;

    // Categorical
    private String[] choices;

    private int index;

    public Option(String name, double min, double max, double step) {
        this.quantitative = true;
        this.name = name;
        this.min = min;
        this.max = max;
        this.step = step;
        this.value = min;
    }

    public Option(String name, String[] choices) {
        this.quantitative = false;
        this.name = name;
        this.choices = choices;
    }

    public void increment(boolean increase) {
        if (quantitative) {
            if (increase && value < max) {
                value += step;
            } else if (!increase && value > min) {
                value -= step;
            }
        } else {
            if (increase && index < choices.length - 1) {
                index++;
            } else if (!increase && index > 0) {
                index--;
            }
        }
    }

    public boolean getQuantitative() {
        return quantitative;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        if (quantitative) {
            return value;
        } else {
            return -1;
        }
    }

    public String getChoice() {
        if (!quantitative) {
            return choices[index];
        } else {
            return "NA";
        }
    }

}
