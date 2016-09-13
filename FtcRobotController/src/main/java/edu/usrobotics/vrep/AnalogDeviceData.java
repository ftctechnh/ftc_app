package edu.usrobotics.vrep;

public class AnalogDeviceData extends DeviceData {

	float mAnalogValue=0.0f;

	public AnalogDeviceData() {
		super();
		construct();
	}

    @Override
    protected void construct() {
    }

	public float getAnalogValue() {
		lock.readLock().lock();
		try {
			return mAnalogValue;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void setAnalogValue(float a) {
		lock.writeLock().lock();
		try {
			mAnalogValue = a;
		} finally {
			lock.writeLock().unlock();
		}
	}
}