package edu.usrobotics.vrep;

public class MotorDeviceData extends DeviceData {

	float mMotorSpeed=0;
	boolean mMotorFloatMode=false;

	public MotorDeviceData() {
		super();
		construct();
	}

    @Override
    protected void construct() {
        //System.out.println("Building Legacy Motor DeviceData");
    }

	public float getMotorSpeed() {
		lock.readLock().lock();
		try {
			return mMotorSpeed;
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean getMotorFloatMode() {
		return mMotorFloatMode;
	}

	public void setMotorSpeed(byte speedByte) {
		lock.writeLock().lock();
		try {
			if (speedByte == (byte)0x80) {
				mMotorFloatMode = true;
				mMotorSpeed=0.0f;
			} else {
				mMotorSpeed = (float)speedByte/100.0f;
			}
		} finally {
			lock.writeLock().unlock();
		}
	}
}
