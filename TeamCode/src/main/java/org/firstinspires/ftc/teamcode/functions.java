//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//
//@Disabled
//
/////These are all the functions from last year that we may want to use this year
//
//    /* This is method uses the front right wheel and its encoder to give a number of ticks that the robot should move */
//public void headingPowerPositionFR(int heading,double power,int position,double lightVal){
//        /* declare values*/
//        double headingRads;
//        double x;
//        double y;
//        double r;
//        double light;
//        /* define values*/
//        headingRads=Math.toRadians(heading);
//        x=power*Math.cos(headingRads);
//        y=power*Math.sin(headingRads);
//        r=0;
//
//        robot.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        sleep(100);
//        robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//        /* apply power using the setWheelPower method*/
//        setWheelPower(x,y,r);
//
//        /* Using the light value given, run the robot using given parameters until light value is found*/
//        while(opModeIsActive()&&robot.frontRightMotor.getCurrentPosition()<position &&!robot.touchSensor.isPressed()){
//        if(lightVal>0){
//        light=robot.opticalDistanceSensor.getLightDetected();
//        telemetry.addData("light",light);
//        telemetry.update();
//        if(light>lightVal)
//        break;
//        }
//        }
//        wheelsOff();
//        }
//
//    /* This is method uses the back right wheel and its encoder to give a number of ticks that the robot should move */
//public void headingPowerPositionBR(int heading,double power,int position,double lightVal){
//        /* declare values*/
//        double headingRads;
//        double x;
//        double y;
//        double r;
//        double light;
//        /* define values*/
//        headingRads=Math.toRadians(heading);
//        x=power*Math.cos(headingRads);
//        y=power*Math.sin(headingRads);
//        r=0;
//
//        robot.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        sleep(100);
//        robot.backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//        /* apply power using the setWheelPower method*/
//        setWheelPower(x,y,r);
//
//        /* Using the light value given, run the robot using given parameters until light value is found*/
//        while(opModeIsActive()&&robot.backRightMotor.getCurrentPosition()<position &&!robot.touchSensor.isPressed()){
//        if(lightVal>0){
//        light=robot.opticalDistanceSensor.getLightDetected();
//        telemetry.addData("light",light);
//        telemetry.update();
//        if(light>lightVal)
//        break;
//        }
//        }
//        wheelsOff();
//        }
//    /* This method is used to have the robot rotate to a desired heading that is defined throughout the code*/
//private void RotateTo(int targetHeading)throws InterruptedException{
//        /* declare values and define constant values*/
//        int robotHeading=0;
//        int headingError;
//        double r=0;
//        int opModeLoopCount=0;
//
//        headingError=1;
//
//        // keep looping while we are still active, and not on heading.
//        while(opModeIsActive()&&(headingError!=0)){
//        // get the heading info.
//        // the Modern Robotics' gyro sensor keeps
//        // track of the current heading for the Z axis only.
//        robotHeading=robot.gyro.getHeading();
//        // adjust heading to match unit circle
//        //       Modern Robotics gyro heading increases CW
//        //       unit circle increases CCW
//        if(robotHeading!=0){
//        robotHeading=360-robotHeading;
//        }
//
//        // if heading not desired heading rotate left or right until they match
//        headingError=targetHeading-robotHeading;
//
//        if(headingError!=0){
//        if(headingError< -180){
//        headingError=headingError+360;
//        }else if(headingError>180){
//        headingError=headingError-360;
//        }
//        // avoid overflow to motors
//        //    headingError is -180 to 180
//        //    divide by 180 to make -1 to 1
//        r=(double)headingError/180.0;
//
//        // ensure minimal power to move robot
//        if((r< .07)&&(r>0)){
//        r=.07;
//        }else if((r>-.07)&&(r< 0)){
//        r=-.07;
//        }
//
//        // Set power on each wheel
//        robot.frontLeftMotor.setPower(r);
//        robot.frontRightMotor.setPower(r);
//        robot.backLeftMotor.setPower(r);
//        robot.backRightMotor.setPower(r);
//        }else{
//        wheelsOff();
//        }
//        opModeLoopCount=opModeLoopCount+1;
//        }
//        }
//    /* This method simply sets all motor to zero power*/
//public void wheelsOff(){
//        robot.frontLeftMotor.setPower(0);
//        robot.frontRightMotor.setPower(0);
//        robot.backLeftMotor.setPower(0);
//        robot.backRightMotor.setPower(0);
//        }