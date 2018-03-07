package org.firstinspires.ftc.teamcode.libraries.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.util.Range;

/**
 * Created by Noah on 3/6/2018.
 * Interface to abscract the control code of underglow blinking effects
 */

//abstraction for a blinky effect
public abstract class BlinkyEffect {
    private DcMotor blinker;
    public void setMotor(DcMotor blinker) {
        this.blinker = blinker;
    }

    //brightness from 0 to 1
    //scale brightness based on sqrt function
    protected void setBrightness(float bright){
        blinker.setPower(Math.pow(Math.abs(bright), 2));
    }

    protected float getBrightness(){
        return (float)Math.sqrt(blinker.getPower());
    }

    abstract int loop(int deltaTime); //takes the milliseconds since last call, returns milliseconds till next call
    //return 0 or negative when done

    //some blinky effects
    public static class OnOff extends BlinkyEffect {
        private final int time;
        private int count;
        private final float onBright;
        private boolean on = false;

        public OnOff(int millisTime, float onBright){
            this(millisTime, onBright,-1);
        }

        public OnOff(int millisTime, float onBright, int count) {
            this.time = millisTime;
            this.count = count;
            this.onBright = onBright;
        }

        public int loop(int deltaTime) {
            //first call
            if(deltaTime == 0) {
                //if it started on, turn it off
                if(getBrightness() > 0) setBrightness(0);
                return time;
            }
            if(count > 0 || count < 0) { //if count is negative, go on forever
                if(!on) setBrightness(onBright);
                else {
                    setBrightness(0);
                    if(count > 0) count--;
                }
                on = !on;
                return 2*time - deltaTime;
            }
            //else dunzoned
            return 0;
        }
    }

    public static class Pulse extends BlinkyEffect {
        private static final float PULSE_STEPS = 16;

        private final int time;
        private int count;
        private final float onBright;
        private final float pulseStep;
        private boolean on = false;
        private float currentPulse = 0;

        public Pulse(int millisTime, float onBright){
            this(millisTime, onBright,-1);
        }

        public Pulse(int millisTime, float onBright, int count) {
            this.time = Math.round((float) millisTime / PULSE_STEPS);
            this.count = count;
            this.onBright = onBright;
            this.pulseStep = onBright / PULSE_STEPS;
        }

        public int loop(int deltaTime) {
            //first call
            if(deltaTime == 0) {
                //if it started on, turn it off
                if(getBrightness() > 0) setBrightness(0);
                return time;
            }

            if(count > 0 || count < 0) { //if count is negative, go on forever
                if(!on) {
                    currentPulse += pulseStep;
                    if(currentPulse >= onBright) {
                        currentPulse = onBright;
                        on = true;
                    }
                    setBrightness(currentPulse);
                }
                else {
                    currentPulse -= pulseStep;
                    if(currentPulse <= 0) {
                        currentPulse = 0;
                        on = false;
                        if(count > 0) count--;
                    }
                    setBrightness(currentPulse);
                }
                return time;
            }
            //else dunzoned
            return 0;
        }
    }
}
