package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 2/20/18.
 */

public enum CryptoboxColumn {
    LEFT (- 1.0),
    CENTER (0.0),
    RIGHT (+ 1.0);
    public final double value;
    CryptoboxColumn(double value) {
        this.value = value;
    }
}