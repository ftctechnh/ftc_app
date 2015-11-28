package com.qualcomm.ftcrobotcontroller.opmodes.QASMTSMARTTEAM;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;


public class Main_Driver extends OpMode implements SensorEventListener {

    //TODO look into vector products of axis for the implementation of vertical phone compass


    //Acceleration
    double ax, ay, az;
    double[] gravity = new double[3];
    double[] linear_acceleration = new double[3];

    //DcMotor objects
    DcMotor frontLeft,frontRight,backLeft,backRight;

    //Controller 1
    double lx, ly, rx, ry, l2, r2;
    boolean a ,b ,x ,y , l1, r1, du, dd, dl, dr;

    //Controller 2
    double lx2, ly2, rx2, ry2, l22, r22;
    boolean a2 ,b2 ,x2 ,y2 , l12, r12, du2, dd2, dl2, dr2;

    float azimuth_angle;
    float pitch_angle;
    float roll_angle;

    //Motor powers
    double 	power_front_left,
            power_front_right,
            power_back_left,
            power_back_right;

    //Specific to heading
    double heading = 0;
    double initialHeading = 0;
    boolean initial = false;


    //Calculation
    double direction, magnitude, offset, resultant;
    double stopwatch, v, vd, ad, am, pam,  vt;

    // Motors
    double frv, flv, blv, brv, frp, flp, blp, brp;
    double motorWatch;
    double[] reducer = new double[4];
    boolean dPerspective = true;
    boolean tc = false;
    boolean sync = false;
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
    }


    @Override
    public void loop() {



        lx = gamepad1.left_stick_x;
        ly = -	gamepad1.left_stick_y;		//UP RETURNS -(NEGATIVE) CORRECTION
        rx = gamepad1.right_stick_x;
        ry = -	gamepad1.right_stick_y;		//UP RETURNS -(NEGATIVE) CORRECTION
        l2 = gamepad1.left_trigger;
        r2 = gamepad1.right_trigger;
        a = gamepad1.a;
        b = gamepad1.b;
        x = gamepad1.x;
        y = gamepad1.y;
        l1 = gamepad1.left_bumper;
        r1 = gamepad1.right_bumper;
        du = gamepad1.dpad_up;
        dd = gamepad1.dpad_down;
        dl = gamepad1.dpad_left;
        dr = gamepad1.dpad_right;

        //TODO where is the position nevin?
        //frontLeft.getController().setMotorTargetPosition();


		/*
		*
		*
		*
		* CALCULATIONS GO HERE!!
		*
		*
		*
		* */
        if(!dPerspective){
            if(!sync)
            {
                power_front_right = ly - lx - rx;
                power_front_left  = ly + lx + rx;
                power_back_left   = ly - lx + rx;
                power_back_right  = ly + lx - rx;
                if(power_front_right > 1)
                    power_front_right = 1;
                if(power_front_left > 1)
                    power_front_left = 1;
                if(power_back_left > 1)
                    power_back_left = 1;
                if(power_back_right > 1)
                    power_back_left = 1;

            }
            else
            {
                power_front_right = ly ;
                power_front_left  = ly ;
                power_back_left   = ly ;
                power_back_right  = ly ;
            }
        }
        else{
            //=================================================
            //    Driver perspective
            // *************************************************

            if(lx == 0 && ly == 0)
            {
                direction = 0;
            }
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
                direction = (double) Math.atan(ly/lx);
            }



            magnitude = (double) Math.sqrt((ly * ly) + (lx * lx));
            offset = initialHeading - heading;
            //resultant = (double)6.283185 - (offset - direction);
            resultant = direction - offset;

            ly = (double) Math.sin(resultant) * magnitude;
            lx = (double) Math.cos(resultant) * magnitude;


            if(!sync)
            {
                power_front_right = ly - lx - rx;
                power_front_left  = ly + lx + rx;
                power_back_left   = ly - lx + rx;
                power_back_right  = ly + lx - rx;
            }
            else
            {
                power_front_right = ly ;
                power_front_left  = ly ;
                power_back_left   = ly ;
                power_back_right  = ly ;
            }

        } // finished assigning power values to variables

        // Traction control code
        if(tc)
        {
            // Take the time elapsed since encoders were last reset
            double elapsed = (System.currentTimeMillis() - motorWatch);
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
            power_front_right -= reducer[0];
            power_front_left  -= reducer[1];
            power_back_left   -= reducer[2];
            power_back_right  -= reducer[3];
        } // finished scaling powers with reducers

        // Reset encoders and timestamp
        // Always put this code after the Traction control block, but not inside
        motorWatch = System.currentTimeMillis();
        frontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);


        // Adjust for any powers over 1
        if(power_front_right > 1)
            power_front_right = 1;
        if(power_front_left > 1)
            power_front_left = 1;
        if(power_back_left > 1)
            power_back_left = 1;
        if(power_back_right > 1)
            power_back_left = 1;
        // set powers to motors
        frontLeft.setPower(power_front_left);
        frontRight.setPower(power_front_right);
        backLeft.setPower(power_back_left);
        backRight.setPower(power_back_right);





        //Change modes - I'm changing this, checked with Rowan, to Y toggle
        /*
        if(b&&r1)
        {
            dPerspective = false;
        }
        if(b&&l1)
        {
            dPerspective = true;
            initial = false;
        }
        */

        if(y)
        {
            dPerspective = !dPerspective;
            //DONT USE THE SAME TO TOGGLE
            /*
            * the loop runs many times in a while, so you cannot accurately tell if
            * it is switching modes
            * and trust me it won't work as intended
            *
            * have y to change to driver perspective
            * have x to change to non-driver perspective
            *
            * toggles won't work mate
            * */
        }
        // Toggle Traction control
        if(r1)
        {
            tc = !tc;
        }
        // Toggle motor sync
        if(a)
        {
            sync = true;
        }
        //Reset initial heading to current
        if(l1&&r1&&a)
        {
            initial = false;
        }

        try {
            if(dPerspective) {

                telemetry.addData("Azi, Pit, Rol", String.format("Azi %d, Pit %d, Roll %d", (int) azimuth_angle, (int) pitch_angle, (int) roll_angle));

                telemetry.addData("Initial heading", Math.round(initialHeading));
                telemetry.addData("Current heading", Math.round(heading));
                telemetry.addData("Difference", Math.round(heading - initialHeading));

                //telemetry.addData("Acceleration", "******Acceleration******");
                telemetry.addData("Acceleration X ", ax);
                telemetry.addData("Acceleration Y", ay);
                telemetry.addData("Acceleration Z", az);
                telemetry.addData("Driver perspective", "Press B and R1 to change");
            }
            else
            {
                telemetry.addData("Not driver perspective", "Press B and L1 to change");
            }
        }catch (Exception e)
        {
            telemetry.addData("Exception ", e.toString());
        }





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






    /*
    *
    *
    * Controls are defined here
    *
    * B + R1 = Non driver perspective (erase)
    * B + L1 = Driver perspective (erase)
    * Y      = Toggle Driver Perspective
    * R1     = Right bumber of gamepad 1 toggles Traction Control
    *
    * L1 + R1 + A = Reset initial heading to current
    *
    *
    *
    *
    * */





}