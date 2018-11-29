package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Auto {
    Bogg robot = null;
    ElapsedTime timer = null;
    StartPosition startPosition = null;
    Telemetry telemetry = null;
    double iSP; //initialSlopePositivity

    Auto(Bogg robot, Telemetry telemetry)
    {
        this.robot = robot;
        robot.driveEngine.driveAtAngle(Math.PI);
        this.telemetry = telemetry;
        robot.camera = new Camera(robot.hardwareMap, telemetry);
        telemetry.addLine("Made it to Point X");
        telemetry.update();
    }

    private enum StartPosition
    {
        HighBlue,
        LowBlue,
        HighRed,
        LowRed
    }

    private boolean doneDropping = false;
    private boolean doneSliding = false;
    private boolean doneSpinning = false;
    private boolean midTargetAchieved = false;
    private boolean doneMovingToWall = false;
    private boolean doneMovingToDepot = false;
    private boolean doneDroppingMarker = false;
    private boolean doneMovingToCrater = false;

    void drop()
    {
        if(timer == null)
        {
            timer = new ElapsedTime();
        }
        if (timer.seconds() < 4) //for the first 4 seconds
        {
            robot.lift(-1); //pull while we
            robot.setBrake(false); //disengage the brake
        } else if (!robot.sensors.touchTop.isPressed()) //if the robot is still off the ground
        {
            robot.lift(.2); //push up, which drops the robot
        }
        else {
            timer.reset();
            doneDropping = true;
        }
    }

    boolean isDoneDropping(){return doneDropping;}

    void slide()
    {
        double inchesMoved = Math.abs(robot.driveEngine.back.getCurrentPosition() * DriveEngine.inPerTicks);
        if(timer.seconds() < .3) //for an additional .3 seconds
        {
            robot.lift(.2); //drop a bit more
        }
        else if(inchesMoved < 4.5) //the back encoder has moved less than 4 inches
        {
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(.4,0); //drive to the side to unhook
        }
        else if(inchesMoved < 6) //the back encoder has moved less than 4 inches
        {
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(.4,.4); //drive to the side to unhook
        }
        else  //if the robot has unhooked
        {
            robot.driveEngine.drive(0,0); //initializing camera interrupts code, so we have to stop the robot.
            doneSliding = true;
            timer.reset(); //Needed for DropPark
        }
    }

    boolean isDoneSliding(){return doneSliding;}

    int count = 0;
    void spin()
    {
        count += 1;
        telemetry.addLine("Made it to point A");
        telemetry.update();

        double t = timer.seconds();
        if(t/1.5 - Math.floor(t)/1.5 < .60)  //rotates for 60% of 1.5 seconds
            robot.driveEngine.rotate(.2);
        else
            robot.driveEngine.rotate(0);  //and stops so we can see the target

        telemetry.addLine("Made it to point B");
        telemetry.update();

        double[] location = robot.camera.getLocation();//get a location, looks like [5.65,-2.54]
        if(!(null == location)) {
            telemetry.addLine("Made it to point Q");
            telemetry.update();

            double angle = Math.atan2(location[0], location[1]); //get the angle looking down on the field, lander to robot
            telemetry.addLine("Made it to point 2");
            telemetry.update();

            if (angle < -Math.PI / 2)                          //Quadrant 3
                startPosition = StartPosition.LowBlue;
            else if (angle < 0)                              //Quadrant 4
                startPosition = StartPosition.LowRed;
            else if (angle < Math.PI / 2)                      //Quadrant 1
                startPosition = StartPosition.HighRed;
            else // angle between pi/2 and pi               //Quadrant 2
                startPosition = StartPosition.HighBlue;

            doneSpinning = true;
            telemetry.addLine("Made it to point 3");
            telemetry.update();
        }

        telemetry.addLine("Made it to point C");
        telemetry.update();
    }

    boolean isDoneSpinning(){return doneSpinning;}

    void moveToWall()
    {
        telemetry.addLine("Made it to point 4");
        if(robot.camera.targetVisible()){
            double t = timer.seconds();
            robot.driveEngine.rotate(t/1.5 - Math.floor(t/1.5) < .60 ? .2 : 0);
        }

        double[] midTarget;
        double[] wallTarget;
        double targetAngle;

        switch(startPosition)
        {
            case HighRed:
                midTarget = new double[]{0,3};
                wallTarget = new double[]{0,5.5};
                targetAngle = 90;
                iSP = 1;
                break;
            case HighBlue:
                midTarget = new double[]{-3,0};
                wallTarget = new double[]{-5.5,0};
                targetAngle = 180;
                iSP = -1;
                break;
            case LowBlue:
                midTarget = new double[]{0,-3};
                wallTarget = new double[]{0,-5.5};
                targetAngle = 270;
                iSP = 1;
                break;
            default: //case LowRed:
                midTarget = new double[]{3,0};
                wallTarget = new double[]{5.5,0};
                targetAngle = 0;
                iSP = -1;
                break;
        }
        if(!midTargetAchieved) { //if we haven't achieved the midtarget
            if(robot.driveToTarget(midTarget[0], midTarget[0], .6, 4))  //keep driving to the midtarget, also returns if we have, if so
                midTargetAchieved = true; //say that we have acheived the midtarget
        }
        else
        if(robot.driveToTarget(wallTarget[0], wallTarget[1], .6, 4)) //drive to the wall
        {
            if(robot.rotateToTargetAngle(targetAngle, 5)) //align to the wall; if we're good then
                doneMovingToWall = true;                           //move to the depot
        }
    }

    boolean isDoneMovingToWall(){return doneMovingToWall;}

    void moveToDepot()
    {
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
        double mobileDistance = robot.sensors.dMobile.getDistance(DistanceUnit.INCH);

        robot.driveEngine.drive(iSP * .7,(6 - fixedDistance)/6.0);

        if(mobileDistance < 6) {
            doneMovingToDepot = true;
            robot.sensors.rotateMobile(-90);
            timer.reset();
        }
    }

    boolean isDoneMovingToDepot(){return doneMovingToDepot;}

    void dropMarker()
    {
        robot.driveEngine.drive(0,0);
        robot.push(true);
        if(timer.seconds() > 3)
            doneDroppingMarker = true;
    }

    boolean isDoneDroppingMarker(){return doneDroppingMarker;}

    void moveToCrater()
    {
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
        robot.push(false);
        robot.driveEngine.drive(iSP * .7,(6 - fixedDistance)/6.0);
        if(robot.sensors.isTilted())
        {
            doneMovingToCrater = true;
        }
    }

    boolean isDoneMovingToCrater(){return doneMovingToCrater;}

    void stop()
    {
        robot.driveEngine.drive(0,0);
        robot.lift(0);
    }

    double getTime()
    {
        return timer.seconds();
    }
}
