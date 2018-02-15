package org.firstinspires.ftc.teamcode.libraries;

/**
 * Created by Noah on 2/15/2018.
 * Step class designed to throw an exception on error
 * And terminate gracefully afterwards
 */

public abstract class FailableStep extends AutoLib.Step {
    //this method should be overriden in the baby class
    //it can throw an exception on failure
    public abstract boolean _loop() throws Exception;
    //this function should also be overriden, as it will be called to have the step gracefully terminate
    public abstract boolean _terminate();
    private boolean failed = false;
    //the regular loop method will catch and ignore all exceptions
    //only called by sequences who don't understand how to handle failures
    public final boolean loop() {
        super.loop();
        if(!failed) {
            try {
                return failed = this._loop();
            }
            catch (Exception e) {
                return this._terminate();
            }
        }
        else return this._terminate();
    }
}
