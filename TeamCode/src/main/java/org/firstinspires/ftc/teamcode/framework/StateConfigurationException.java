package org.firstinspires.ftc.teamcode.framework;

public class StateConfigurationException extends RuntimeException{
    public StateConfigurationException(){

    }

    public StateConfigurationException(String message){
        super(message);
    }

    public StateConfigurationException(String state, String previousState){
        super("Previous state: "+previousState+" couldn't be found by: "+state+" state");
    }
}
