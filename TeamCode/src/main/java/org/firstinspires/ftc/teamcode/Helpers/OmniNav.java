package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Helpers.HW;

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
    public static void setMotors(float x, float y, float rot, DcMotor ne, DcMotor se, DcMotor sw, DcMotor nw) {
//        double theta = Math.atan(x/y) - Math.PI/4;
        double scaler = 0.8;
        double drive = (double) -y;
        double strafe = (double) x;
        double spin = (double) rot;


        /*rot *= .5;
        double magnitude = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        magnitude = magnitude*(100/127) - rot;
        magnitude = (scaler*Math.pow(magnitude, 3) + (1 - scaler) * magnitude);
        double newX = Math.cos(theta)*magnitude;
        double newY = Math.sin(theta)*magnitude;
        double nePower, nwPower, sePower, swPower;
        nePower = Range.clip(rot + newX, -1, 1);
        nwPower = Range.clip(rot + newY, -1, 1);
        sePower = Range.clip(rot - newX, -1, 1);
        swPower = Range.clip(rot - newY, -1, 1);
*/
        double nePower, nwPower, sePower, swPower;
        nwPower = Range.clip(drive + strafe + spin, -1, 1);
        swPower = Range.clip(drive - strafe + spin, -1, 1);
        nePower = Range.clip(drive - strafe - spin, -1, 1);
        sePower = Range.clip(drive + strafe - spin, -1, 1);
        nwPower = (scaler*Math.pow(nwPower, 3) + ( 1 - scaler) * nwPower);
        nePower = -(scaler*Math.pow(nePower, 3) + ( 1 - scaler) * nePower);
        swPower = (scaler*Math.pow(swPower, 3) + ( 1 - scaler) * swPower);
        sePower = -(scaler*Math.pow(sePower, 3) + ( 1 - scaler) * sePower);

        //from here on is just setting motor values
        ne.setPower(nePower);
        se.setPower(sePower);
        sw.setPower(swPower);
        nw.setPower(nwPower);
    }

    public void turn(double degrees) {
    }

}
