package com.lasarobotics.library.drive;

import com.lasarobotics.library.sensor.legacy.hitechnic.Gyroscope;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;

/**
 * Methods for the Swerve drive train
 * <p/>
 * WARNING: This class is entirely theoretical and has never, ever been tested.
 * If you test it, let us know!
 */
public class Swerve {
    /**
     * Implements the Swerve drive train with four motors and four lifting servos
     * Requires gyro input
     *
     * @param y           The y-axis of the controller, forward/rev
     * @param x           The x-axis of the controller, strafe
     * @param rot         The spin axis of the controller
     * @param gyroheading The current normalized gyro heading (between 0 and 360)
     * @param leftFront   The motor on the front left
     * @param rightFront  The motor on the front right
     * @param leftBack    The motor on the back left
     * @param rightBack   The motor on the back right
     * @param lf          The servo on the front left
     * @param rf          The servo on the front right
     * @param lb          The servo on the back left
     * @param rb          The servo on the back right
     */
    public static void standard(double y, double x, double rot, double gyroheading,
                                DcMotor leftFront, DcMotor rightFront, DcMotor leftBack, DcMotor rightBack,
                                Servo lf, Servo rf, Servo lb, Servo rb) {
        //Makes commands field-centric
        double cosA = Math.cos(Math.toRadians(Gyroscope.normalize(gyroheading)));
        double sinA = Math.sin(Math.toRadians(Gyroscope.normalize(gyroheading)));
        double xOut = x * cosA - y * sinA;
        double yOut = x * sinA + y * cosA;

        //Assuming a square robot for now
        //These variables are vectors of wheel motion that take into account both translational and rotational motion
        //The x and y components of motion add or subtract the linear equivalent of rotational motion based on wheel position
        //a and b are x components, and c and d are y components
        double a = xOut - rot / 2;
        double b = xOut + rot / 2;
        double c = yOut - rot / 2;
        double d = yOut + rot / 2;

        //Calculates magnitude of motor power needed by using the Pythagorean Theorem on the x and y vectors
        double wrf = Math.sqrt(b * b + c * c);
        double wlf = Math.sqrt(b * b + d * d);
        double wlb = Math.sqrt(a * a + d * d);
        double wrb = Math.sqrt(a * a + c * c);

        //Calculates direction the wheel needs to face by converting rectangular coordinates into polar and spitting out theta
        //Since Math.atan2(a, b)'s result is in the range of -pi to +pi, and the Servo takes values between 0 and +1,
        //we must scale the values
        double arf = Math.atan2(b, c) / Math.PI;
        double alf = Math.atan2(b, d) / Math.PI;
        double alb = Math.atan2(a, d) / Math.PI;
        double arb = Math.atan2(a, c) / Math.PI;

        //Move range to between 0 and +1, if not already
        double[] wheelspeed = {wrf, wlf, wlb, wrb};
        Arrays.sort(wheelspeed);
        if (wheelspeed[3] > 1) {
            wlf /= wheelspeed[3];
            wrf /= wheelspeed[3];
            wrb /= wheelspeed[3];
            wlb /= wheelspeed[3];
        }

        //So I have to type less in the next set of code
        wheelspeed[0] = wrf;
        wheelspeed[1] = wlf;
        wheelspeed[2] = wlb;
        wheelspeed[3] = wrb;

        //The servos we are planning on using only have 180 degrees of rotation, so if the angle is negative,
        //make it move backwards at the positive angle
        double[] angles = {arf, alf, alb, arb};
        for (int i = 0; i < angles.length; i++) {
            if (angles[i] < 0) {
                //The angles are between -1 and 0 here, so adding 1 is effectively adding pi in radians, or making it positive
                angles[i] += 1;
                wheelspeed[i] *= -1;
            }
        }

        //Set motor power (can move forward and backward)
        rightFront.setPower(wheelspeed[0]);
        leftFront.setPower(wheelspeed[1]);
        leftBack.setPower(wheelspeed[2]);
        rightBack.setPower(wheelspeed[3]);

        //Set the direction each wheel faces (only positive angles for now due to servo ranges)
        //This currently ignores the soft limits of 0.1 to 0.9, so if this becomes a problem and we have to obey these soft limits,
        //the scaling needs to be changed, but also our logic because of the discontinuity in possible wheel angles
        rf.setPosition(angles[0]);
        lf.setPosition(angles[1]);
        lb.setPosition(angles[2]);
        rb.setPosition(angles[3]);
    }
}
