package edu.usrobotics.vrep;

public class NullDeviceData extends DeviceData {

	public NullDeviceData() {
		super();
		construct();
	}

    @Override
    protected void construct() {
        //System.out.println("Building Null DeviceData");
    }
}
