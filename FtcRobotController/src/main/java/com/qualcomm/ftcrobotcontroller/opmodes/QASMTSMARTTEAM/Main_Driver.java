package com.qualcomm.ftcrobotcontroller.opmodes.QASMTSMARTTEAM;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

    /*
    *
    *
    * Controls are defined here
    *
    *
    * Y      = Toggle Driver Perspective
    * R1     = Right bumber of gamepad 1 toggles Traction Control
    * A      = Hold A for motor power synch
    * L1 + R1 + A = Reset initial heading to current
    *
    *
    * * Controller 2 * *
    * R1     = Right Bumper = Lower hand
    * L1     = Left Bumper  = Raise hand
    * ly     = left y axis  = Pivot
    * ry     = right y axis = Linear
    * */

public class Main_Driver extends OpMode implements SensorEventListener {

    //TODO look into vector products of axis for the implementation of vertical phone compass


    //Acceleration
    double ax, ay, az;
    double[] gravity = new double[3];
    double[] linear_acceleration = new double[3];

    //DcMotor objects
    DcMotor frontLeft,frontRight,backLeft,backRight, pivot, linear;

    // servos
    double hand;
    Servo servo;

    // Linbae


    //Controller 1
    //double lx, ly, rx, ry, l2, r2;
    double lx, ly, rx, ry = 0;
    //boolean a ,b ,x ,y , l1, r1, du, dd, dl, dr = false;
    boolean a  ,y , l1, r1 = false;
    boolean yp, r1p = false;

    //Controller 2
    //double lx2, ly2, rx2, ry2, l22, r22;
    double  ly2, ry2 = 0;
    //boolean a2 ,b2 ,x2 ,y2 , l12, r12, du2, dd2, dl2, dr2 = false;
    boolean  l12, r12 = false;



    float azimuth_angle;
    float pitch_angle;
    float roll_angle;

    //Motor powers
    /*
    double 	power_front_left,
            power_front_right,
            power_back_left,
            power_back_right = 0;
    */
    double[] motorPowers = new double[4];

    // Variables for normal drive
    double directMag, scale, biggest = 0;

    double pivot_power, linear_power = 0;
    //Specific to heading
    double heading = 0;
    double initialHeading = 0;
    boolean initial = false;


    // Driver Perspective Calculations
    double direction, magnitude, offset, resultant;
    //double stopwatch, v, vd, ad, am, pam,  vt; // lol what are these variables?

    //ArrayList<Double> list = new ArrayList<Double>();

    // Motors
    double frv, flv, blv, brv, frp, flp, blp, brp;
    double motorWatch;
    double[] reducer = new double[4];
    boolean dPerspective = false;
    boolean tc = false;
    public Main_Driver() {

    }


    @Override
    public void init() {

        try {
            SensorManager sensorManager;
            sensorManager =
                    (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
            sensorManager.registerListener(orientationListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(accelerationListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        }
        catch (Exception e)
        {

            telemetry.addData("Exception", e.toString());
        }

/*
		frontLeft = hardwareMap.dcMotor.get("front_left");
		frontRight = hardwareMap.dcMotor.get("front_right");
		backLeft = hardwareMap.dcMotor.get("back_left");
		backRight = hardwareMap.dcMotor.get("back_right");

		frontLeft.setDirection(DcMotor.Direction.REVERSE);
		backLeft.setDirection(DcMotor.Direction.REVERSE);

		frontLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        frontRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        backLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        backRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        */

        try {
            servo = hardwareMap.servo.get("hand");
            frontLeft = hardwareMap.dcMotor.get("front_left");
            frontRight = hardwareMap.dcMotor.get("front_right");
            backLeft = hardwareMap.dcMotor.get("back_left");
            backRight = hardwareMap.dcMotor.get("back_right");
            pivot = hardwareMap.dcMotor.get("Pivot");
            linear = hardwareMap.dcMotor.get("Linear");

            frontRight.setDirection(DcMotor.Direction.REVERSE);
            backRight.setDirection(DcMotor.Direction.REVERSE);

        }
        catch (Exception c)
        {
            telemetry.addData("robot is gonna die", 0);
        }

    }
    @Override
    public void start()
    {
        frontLeft.setPower(0.5);
        frontRight.setPower(0.5);
        backLeft.setPower(0.5);
        backRight.setPower(0.5);
        double straightT = System.currentTimeMillis();
        while(System.currentTimeMillis() < straightT + 1000)
        {}

    }
    @Override
    public void loop() {


        // Controller 1
        lx = gamepad1.left_stick_x;
        ly = -	gamepad1.left_stick_y;		//UP RETURNS -(NEGATIVE) CORRECTION
        rx = gamepad1.right_stick_x;
        ry = -	gamepad1.right_stick_y;		//UP RETURNS -(NEGATIVE) CORRECTION
        //l2 = gamepad1.left_trigger;
        //r2 = gamepad1.right_trigger;
        a = gamepad1.a;
        //b = gamepad1.b;
        //x = gamepad1.x;
        y = gamepad1.y;
        //l1 = gamepad1.left_bumper;
        r1 = gamepad1.right_bumper;
        //du = gamepad1.dpad_up;
        //dd = gamepad1.dpad_down;
        //dl = gamepad1.dpad_left;
        //dr = gamepad1.dpad_right;

        // Controller 2
        ly2 = gamepad2.left_stick_y;
        ry2 = gamepad2.right_stick_y;
        l12 = gamepad2.left_bumper;
        r12 = gamepad2.right_bumper;



        //Range.clip()

        /*power_front_right = ly - lx - rx;
        power_front_left  = ly + lx + rx;
        power_back_left   = ly - lx + rx;
        power_back_right  = ly + lx - rx;*/

        /*if(l1)
        {
            power_front_left = 0.5;
            frontLeft.setPower(power_front_left);
        }
        if(r1)
        {
            power_front_right = 0.5;
            frontRight.setPower(power_front_right);
        }
        if(l2>0)
        {
            power_back_left = 0.5;
            backLeft.setPower(power_back_left);

        }
        if(r2>0)
        {
            power_back_right = 0.5;
            backRight.setPower(power_back_right);
        }*/



       /* power_front_right = 0.5 * rx;
        power_front_left  = 0.5 * lx;
        power_back_left   = 0.5 * rx;
        power_back_right  = 0.5 * lx;*/

        // result is assigned the value 1.0

        //Ternary operator. 
        //float result = true ? 1.0f : 2.0f;

        try {

            // Servo hand control

            if(l12)
                hand += 0.02;
            if(r12)
                hand -= 0.02;
            // TODO: Figure out actual range of the hand
            hand = Range.clip(hand, 0, 1);
            servo.setPosition(hand);

            //power_front_right = (ly + lx - rx) * 0.9;   //use ternary operators for this logical
            //condition
            //if the left stick values add up to 1 or even -1, and the right stick is not ze ro
            //left sticks values * 0.5 + right stick * 0.5
            //power_front_left = (ly - lx + rx) * 0.9;
            //power_back_left = (ly + lx + rx) * 0.9;
            //power_back_right = (ly - lx - rx) * 0.9;

            /*
            power_front_right = (ly+lx)*(1-rx) * 0.9;
            power_front_left = (ly - lx + rx) * 0.9;
            power_back_left = (ly + lx + rx) * 0.9;
            power_back_right = (ly - lx)*(1-rx) * 0.9;


            power_front_right = (ly + lx - rx) *0.9;
            power_front_left = (ly - lx + rx) * 0.9;
            power_back_left = (ly + lx + rx) * 0.9;
            power_back_right = (ly - lx - rx) * 0.9;

            if(power_front_right > 1)
                power_front_right = 1;
            if(power_back_left > 1)
                power_back_left = 1;
            if(power_front_left > 1)
                power_front_left = 1;
            if(power_back_right > 1)
                power_back_right = 1;

            */

           // pivot.setPower(ly2*0.4);
            //linear.setPower(ry2);


        }catch(Exception e)
        {
            telemetry.addData("MINT", "Mit die");
        }


		/*
		*
		*
		*
		* CALCULATIONS GO HERE!!
		*
		*
		*
		* */

        /*
        if(r1) {
            if(ly!=0){
                frontLeft.setPower(0.5*ly);
                frontRight.setPower(0.5*ly);
                backLeft.setPower(0.5*ly);
                backRight.setPower(0.5*ly);
            }
        }
        if(l1)
        {

            if(lx<-0.01)
            {
                power_front_right = -0.5 * lx;
                power_front_left  = 0.5  * lx;
                power_back_left   = -0.5 * lx;
                power_back_right  = 0.5 * lx;
                frontLeft.setPower(power_front_left);
                frontRight.setPower(power_front_right);
                backLeft.setPower(power_back_left);
                backRight.setPower(power_back_right);

            }
            else if (lx > 0.01){

                power_front_right = 0.5 * lx;
                power_front_left  = -0.5 * lx;
                power_back_left   = 0.5 * lx;
                power_back_right  = -0.5 * lx;
                frontLeft.setPower(power_front_left);
                frontRight.setPower(power_front_right);
                backLeft.setPower(power_back_left);
                backRight.setPower(power_back_right);

            }
        }

        */


		if(!dPerspective){
		    if(!a)
		    {

                /*
                power_front_right = (ly + lx - rx)*0.9;
                power_front_left  = (ly - lx + rx)*0.9;
                power_back_left   = (ly + lx + rx)*0.9;
                power_back_right  = (ly - lx - rx)*0.9;
                */
                motorPowers[0] = (ly + lx - rx);
                motorPowers[1] = (ly - lx + rx);
                motorPowers[2] = (ly + lx + rx);
                motorPowers[3] = (ly - lx - rx);


                biggest = motorPowers[0];
                for(int i = 1; i < 3; i++)
                {
                    if(motorPowers[i] > biggest)
                        biggest = motorPowers[i];
                }
                directMag = Math.sqrt(ly*ly + lx*lx) * 0.9;
                scale = directMag / biggest;

                motorPowers[0] = motorPowers[0] * scale;
                motorPowers[1] = motorPowers[1] * scale;
                motorPowers[2] = motorPowers[2] * scale;
                motorPowers[3] = motorPowers[3] * scale;


            }
            else
            {
                /*
                power_front_right = ly ;
                power_front_left  = ly ;
                power_back_left   = ly ;
                power_back_right  = ly ;
                */
                motorPowers[0] = ly;
                motorPowers[1] = ly;
                motorPowers[2] = ly;
                motorPowers[3] = ly;
            }
        }
        else{
		//=================================================
          //  Driver perspective

            if(lx == 0 && ly != 0)
            {
                if(ly > 0)
                    direction = 1.570796;
                else
                    direction = 4.712389;
            }
            else if(ly == 0 && lx != 0)
            {
                if(lx > 0)
                    direction = 0;
                else
                    direction = 3.141593;

            }
            else
            {
                direction = Math.atan(ly/lx);
            }

            magnitude = Math.sqrt((ly * ly) + (lx * lx));
            offset = initialHeading - heading;
            //resultant = (double)6.283185 - (offset - direction);
            resultant = direction - offset;

            ly = Math.sin(resultant) * magnitude;
            lx =  Math.cos(resultant) * magnitude;


             if(!a)
		    {
                /*
                power_front_right = (ly + lx - rx)*0.9;
                power_front_left  = (ly - lx + rx)*0.9;
                power_back_left   = (ly + lx + rx)*0.9;
                power_back_right  = (ly - lx - rx)*0.9;
                */
                motorPowers[0] = (ly + lx - rx);
                motorPowers[1] = (ly - lx + rx);
                motorPowers[2] = (ly + lx + rx);
                motorPowers[3] = (ly - lx - rx);


                double biggest = motorPowers[0];
                for(int i = 1; i < 3; i++)
                {
                    if(motorPowers[i] > biggest)
                        biggest = motorPowers[i];
                }
                double directMag = Math.sqrt(ly*ly + lx*lx) * 0.9;
                double scale = directMag / biggest;

                motorPowers[0] = motorPowers[0] * scale;
                motorPowers[1] = motorPowers[1] * scale;
                motorPowers[2] = motorPowers[2] * scale;
                motorPowers[3] = motorPowers[3] * scale;

            }
            else
            {
                 /*
                power_front_right = ly ;
                power_front_left  = ly ;
                power_back_left   = ly ;
                power_back_right  = ly ;
                */
                motorPowers[0] = ly;
                motorPowers[1] = ly;
                motorPowers[2] = ly;
                motorPowers[3] = ly;
            }

        } // finished driver perspective and assigning powers
        double elapsed = (System.currentTimeMillis() - motorWatch);
        // Traction control code
        if(tc)
        {
            // Take the time elapsed since encoders were last reset

            // get the motor rpms
            flv = Math.abs(frontLeft.getCurrentPosition()) / elapsed;
            frv = Math.abs(frontRight.getCurrentPosition()) / elapsed;
            blv = Math.abs(backLeft.getCurrentPosition()) / elapsed;
            brv = Math.abs(backRight.getCurrentPosition()) / elapsed;



            if(flv > frv || flv >  blv || flv > brv)
            {
                reducer[1] += 0.05;
            }
            else if(flv < frv && flv <  blv && flv < brv)
                reducer[1] = -0.05;
            else
                reducer[1] = 0;

            if(frv > flv || frv >  blv || frv > brv)
            {
                reducer[0] += 0.05;
            }
            else if(frv < flv && frv <  blv && frv < brv)
                reducer[0] = -0.05;
            else
                reducer[0] = 0;

            if(blv > frv || blv >  flv || blv > brv)
            {
                reducer[2] += 0.05;
            }
            else if(blv < frv && blv <  flv && blv < brv)
                reducer[2] = -0.05;
            else
                reducer[2] = 0;

            if(brv > frv || brv >  blv || brv > flv)
            {
                reducer[3] += 0.05;
            }
            else  if(brv < frv && brv <  blv && brv < flv)
                reducer [3] = -0.05;
            else
                reducer[3] = 0;

            // factor in reducers to powers
            /*
            power_front_right -= reducer[0];
            power_front_left  -= reducer[1];
            power_back_left   -= reducer[2];
            power_back_right  -= reducer[3];
            */
            motorPowers[0] -= reducer[0];
            motorPowers[1] -= reducer[1];
            motorPowers[2] -= reducer[2];
            motorPowers[3] -= reducer[3];

        } // finished scaling powers with reducers

        // Get elapsed time
        elapsed = (System.currentTimeMillis() - motorWatch);

        // Pivot motor
        double pivotSpeed = pivot.getCurrentPosition() / (elapsed);
        if(pivotSpeed > ly2 * 0.5)
            pivot_power -= 0.05;
        else if(pivotSpeed < ly2 * 0.5)
            pivot_power += 0.05;

        if(ly2 < 0.02 && ly2 > 0.02)
            pivot_power = 0;


        // Linear motor
        double linearSpeed = linear.getCurrentPosition() / (elapsed);
        if(pivotSpeed > ry2 * 0.5)
            linear_power -= 0.05;
        else if(pivotSpeed < ry2 * 0.5)
            linear_power += 0.05;

        if(ry2 < 0.02 && ry2 > -0.02)
            linear_power = 0;




        // Reset encoders and timestamp
        // Always put this code after the Traction control block, but not inside
        motorWatch = System.currentTimeMillis();

        frontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        pivot.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        linear.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);


        // set powers to motors
        frontRight.setPower(motorPowers[0]);
		frontLeft.setPower(motorPowers[1]);
		backLeft.setPower(motorPowers[2]);
		backRight.setPower(motorPowers[3]);

        pivot.setPower(pivot_power);
        linear.setPower(linear_power);





        // Change modes from other buttons

        // Toggle of driver perspective
        if(y)
        {
            yp = true;
        }
        if(yp && !y)
        {
            dPerspective = !dPerspective;
            yp = false;
        }

        // Toggle Traction control
        if(r1)
        {
            r1p = true;

        }
        if(r1p && !r1)
        {
            tc = !tc;
            r1p = false;
        }


        //Reset initial heading to current
        if(l1&&r1&&a)
        {
            initial = false;
        }

        try {
            if(dPerspective) {

                //telemetry.addData("Azi, Pit, Rol", String.format("Azi %d, Pit %d, Roll %d", (int) azimuth_angle, (int) pitch_angle, (int) roll_angle));

                telemetry.addData("Initial heading", Math.round(initialHeading));
                telemetry.addData("Current heading", Math.round(heading));
                telemetry.addData("Difference", Math.round(heading - initialHeading));

                //telemetry.addData("Acceleration", "******Acceleration******");
                //telemetry.addData("Acceleration X ", ax);
                //telemetry.addData("Acceleration Y", ay);
                //telemetry.addData("Acceleration Z", az);
                telemetry.addData("Driver perspective", "Press B and R1 to change");
            }
            else
            {
                telemetry.addData("Not driver perspective", "Press Y to change");
            }
        }catch (Exception e)
        {
            telemetry.addData("Exception ", e.toString());
        }


        telemetry.addData("value", ly);
        telemetry.addData("pivotSpeed", pivotSpeed);
        telemetry.addData("Linear Speed", linearSpeed);

    } // End Loop


    @Override
    public void stop() {

    }


    private SensorEventListener accelerationListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            // In this example, alpha is calculated as t / (t + dT),
            // where t is the low-pass filter's time-constant and
            // dT is the event delivery rate.

            final double alpha = 0.8;

            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            /*ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];*/

            ax = linear_acceleration[0];
            ay = linear_acceleration[1];
            az = linear_acceleration[2];

            //list.add(ax);

            /* Not gonna bother with this for now, maybe later
            // Attempt to calculate - no idea what's happening
            double elapsed = (System.currentTimeMillis() - stopwatch)/1000;
            double vx, vy, pvx, pvy, tvx, tvy;
            ad = Math.atan(az/az);
            am = Math.sqrt(az*az + ax*ax);
            vt = elapsed*(Math.abs(pam + am))/2;
            vy = Math.sin(vt) * vt;
            vx = Math.cos(vt) * vt;
            pvy = Math.sin(vd) * v;
            pvx = Math.cos(vd) * v;
            tvy = vy + pvy;
            tvx = vx + pvx;
            v = Math.sqrt(tvy * tvy + tvx * tvx);
            vd = Math.atan(tvy/tvx);

            stopwatch = System.currentTimeMillis();
            pam = am;
            */
        }

    };

    private SensorEventListener orientationListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (dPerspective) {
                try {
                    if (!initial) {
                        initialHeading = event.values[0];
                        heading = event.values[0];
                        initial = true;
                    } else {
                        heading = event.values[0];
                        azimuth_angle = event.values[0];
                        pitch_angle = event.values[1];
                        roll_angle = event.values[2];
                    }
                } catch (Exception e) {
                    telemetry.addData("Exception", e.toString());
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // do not use this method
        }

    };

    @Override
    public void onSensorChanged(SensorEvent event)
    {}
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {}

}