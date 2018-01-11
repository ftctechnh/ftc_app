package org.firstinspires.ftc.teamcode.libraries;

/**
 * Created by Noah on 1/10/2018.
 * Sensor library for APDS9930 prox sensor
 */

public class APDS9930 {
    //unsigned...?
    public static final byte ADDR = 0x39;

    //Register enums
    //I love java
    private enum Regs {
        ENALLE (0x00),
        PTIME (0x02),
        WTIME (0x03),
        CONFIG (0x0D),
        PPULSE (0x0E),
        CONTROL (0x0F),
        ID (0x12),
        STATUS (0x13),
        PDATAL (0x18),
        PDATAH (0x19),
        POFFSET (0x1E);

        public final byte REG;
        Regs(int reg) {
            this.REG = (byte)reg;
        }
    }

    public static class Config {

        public enum LEDStrength {
            STREN_100MA (0),
            STREN_50MA (1),
            STREN_25MA (2),
            STREN_12_5MA (3);

            private static final byte SHIFT = 6;
            private final byte bVal;
            LEDStrength(int val) { this.bVal = (byte)val; }
            byte getVal() { return (byte)(this.bVal << SHIFT); }
        }

        public static final byte PDIODE = 2 << 4;

        public enum ProxGain {
            GAIN_1X (0),
            GAIN_2X (1),
            GAIN_4X (2),
            GAIN_8X (3);

            private static final byte SHIFT = 2;
            private final byte bVal;
            ProxGain(int val) { this.bVal = (byte)val; }
            byte getVal() { return (byte)(this.bVal << SHIFT); }
        }

        private byte PPULSE;

        private void setPPULSE(byte pulse) {
            this.PPULSE = pulse;
        }
    }
}
