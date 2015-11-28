package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by tdoylend on 2015-10-08.
 *
 * This class provides a way to print and compare version numbers.
 *
 * Version 1.0.0
 *
 * CHANGELOG:
 * 1.0.0 - First version.
 */
public class VersionNumber {

    long major;
    long minor;
    long build;

    public VersionNumber(long major, long minor, long build){
        this.major = major;
        this.minor = minor;
        this.build = build;
    }
    public boolean isNewerThan(VersionNumber otherversion) {
        if (this.major > otherversion.major) { return true; }
        if (this.minor > otherversion.minor) { return true; }
        if (this.build > otherversion.build) { return true; }
        return false;
    }
    public String string() {
        return (Long.toString(this.major) + "." +
                Long.toString(this.minor) + "." +
                Long.toString(this.build));
    }
}
