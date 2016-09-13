package edu.usrobotics.vrep;

import java.util.concurrent.locks.ReentrantReadWriteLock;


public abstract class DeviceData {
	public final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	protected String mName = null;

	public DeviceData() {
		mName = "";
	}

    // Do subclass level processing in this method
    protected abstract void construct();


	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}
}