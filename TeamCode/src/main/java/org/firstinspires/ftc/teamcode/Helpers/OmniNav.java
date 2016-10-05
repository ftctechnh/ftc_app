package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Driving functions for a Mecanum drive train in autonomous.
 */
public class OmniNav {
    private final HardwareMap hardwareMap;
    public DcMotor ne, se, sw, nw;

    public static enum Direction {NORTH, NORTH_EAST, EAST, SOUTH_EAST,
        SOUTH, SOUTH_WEST,  WEST, NORTH_WEST};

    /**
     * Constructor
     *
     * @param hardwareMap from the OpMode. Used to access the hardware.
     */
    public OmniNav(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.ne = this.hardwareMap.dcMotor.get("ne");
        this.se = this.hardwareMap.dcMotor.get("se");
        this.sw = this.hardwareMap.dcMotor.get("sw");
        this.nw = this.hardwareMap.dcMotor.get("nw");
    }

//    public void drive()

    /**
     * Drives north relative to the front robot.
     * @param speed of the motors. On a scale of 0-1 from stopped to full speed.
     */
    public void north(double speed) {
        ne.setPower(speed);
        se.setPower(speed);
        sw.setPower(speed);
        nw.setPower(speed);
    }

    public void east(double speed) {
        ne.setPower(-speed);
        se.setPower(speed);
        sw.setPower(-speed);
        nw.setPower(speed);
    }

    public void south(double speed) {
        ne.setPower(-speed);
        se.setPower(-speed);
        sw.setPower(-speed);
        nw.setPower(-speed);
    }

    public void west(double speed) {
        ne.setPower(speed);
        se.setPower(-speed);
        sw.setPower(speed);
        nw.setPower(-speed);
    }

    public void stop() {
        ne.setPower(0);
        se.setPower(0);
        sw.setPower(0);
        nw.setPower(0);
    }

    private void setPowers(double ne_speed, double se_speed, double sw_speed, double nw_speed) {
        ne.setPower(ne_speed);
        se.setPower(se_speed);
        sw.setPower(sw_speed);
        nw.setPower(nw_speed);
    }

    public void turn(double degrees) {
    }

}