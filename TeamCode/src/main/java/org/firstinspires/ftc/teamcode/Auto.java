package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Auto {
    Bogg robot;
    ElapsedTime timer;
    StartPosition startPosition;
    double iSP; //initialSlopePositivity
    //double t;

    Auto(Bogg robot)
    {
        this.robot = robot;
        robot.driveEngine.driveAtAngle(Math.PI);
    }

    private enum StartPosition
    {
        FrontBlue,
        BackBlue,
        FrontRed,
        BackRed
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
        double inchesMoved = robot.driveEngine.back.getCurrentPosition() * DriveEngine.inPerTicks;
        if(timer.seconds() < .3) //for an additional .3 seconds
        {
            robot.lift(.2); //drop a bit more
        }
        else if(inchesMoved < 4) //the back encoder has moved less than 4 inches
        {
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(.4,0); //drive to the side to unhook
        }
        else //if the robot has unhooked
            doneSliding = true;
    }

    boolean isDoneSliding(){return doneSliding;}


    void spin()
    {
        double t = timer.seconds();
        if(t/1.5 - Math.floor(t)/1.5 < .60)  //rotates for 60% of 1.5 seconds
            robot.driveEngine.rotate(.1);
        else
            robot.driveEngine.rotate(0);  //and stops so we can see the target

        //robot.driveEngine.rotate(t/1.5 - Math.floor(t/1.5) < .60 ? .1 : 0);

        if(robot.camera.targetVisible() != null) //if we see a camera (null = nothing)
        {
            double[] location = robot.camera.getLocation(); //get a location, looks like [5.65,-2.54]

            double angle = Math.atan2(location[0], location[1]); //get the angle looking down on the field, lander to robot

            if(angle < -Math.PI/2)                          //Quadrant 3
                startPosition = StartPosition.BackBlue;
            else if(angle < 0)                              //Quadrant 4
                startPosition = StartPosition.BackRed;
            else if(angle < Math.PI/2)                      //Quadrant 1
                startPosition = StartPosition.FrontRed;
            else // angle between pi/2 and pi               //Quadrant 2
                startPosition = StartPosition.FrontBlue;
            doneSpinning = true;
        }
    }

    boolean isDoneSpinning(){return doneSpinning;}

    void moveToWall()
    {
        if(robot.camera.targetVisible() == null){
            //robot.driveEngine.rotate(t/1.5 - Math.floor(t/1.5) < .60 ? .1 : 0);
        }

        double[] midTarget;
        double[] wallTarget;
        double targetAngle;

        switch(startPosition)
        {
            case FrontRed:
                midTarget = new double[]{0,3};
                wallTarget = new double[]{0,5.5};
                targetAngle = 90;
                iSP = 1;
                break;
            case FrontBlue:
                midTarget = new double[]{-3,0};
                wallTarget = new double[]{-5.5,0};
                targetAngle = 180;
                iSP = -1;
                break;
            case BackBlue:
                midTarget = new double[]{0,-3};
                wallTarget = new double[]{0,-5.5};
                targetAngle = 270;
                iSP = 1;
                break;
            default: //case BackRed:
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

        if(mobileDistance < 6) { //TODO: need new sensor somewhere or new servo
            doneMovingToDepot = true;
            robot.sensors.rotateMobileX(-90);
            timer.reset();
        }
    }

    boolean isDoneMovingToDepot(){return doneMovingToDepot;}

    void dropMarker()
    {
        robot.driveEngine.drive(0,0);
        if(timer.seconds() > 4)
            doneDroppingMarker = true;
    }

    boolean isDoneDroppingMarker(){return doneDroppingMarker;}

    void moveToCrater()
    {
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);

        robot.driveEngine.drive(iSP * .7,(6 - fixedDistance)/6.0);

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
