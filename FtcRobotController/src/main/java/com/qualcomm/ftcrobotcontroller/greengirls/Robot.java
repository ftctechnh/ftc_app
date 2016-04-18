package com.greengirls;

/**
 *  Represents the state of our Robot.
 *
 *  @author The Green Girls
 *  @since 9/20/2015.
 */
public class Robot {
   private boolean isDeflectorUp = false;
   private int compassHeading = 0;
    private drivingMotor front;
    private drivingMotor rear;
    public static void main(String args[])  {
        Motor rightFront = new Motor();
        rightFront.stopMotor();
    }

    /**
     * Determines whether the deflector
     * is currently up
     *
     * @return {@code true} if the deflector is
     * up, {@code false} otherwise
      */
    public boolean isDeflectorUp(){
        return isDeflectorUp;
    }

    /**
     * Sets the deflector state
     *
     * @param state {@code true} for up,
     *              {@code false} for down.
     */
    public void setDeflectorUp(boolean state){
        isDeflectorUp = state;
    }
}
