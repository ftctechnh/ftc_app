package edu.usrobotics.client;

import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 * Created by Max on 9/10/2016.
 */
public class RemoteDeviceDiscovery {
    public RemoteDevice device;

    public RemoteDevice getDevice(final String deviceName) {
        try {
            final Object inquiryCompletedEvent = new Object();

            final DiscoveryListener listener = new DiscoveryListener() {

                public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                    try {
                        if (btDevice.getFriendlyName(false).equals(deviceName)) {
                            device = btDevice;
                            LocalDevice.getLocalDevice().getDiscoveryAgent().cancelInquiry(this);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                public void inquiryCompleted(int discType) {
                    /* Notify thread when inquiry completed */
                    synchronized (inquiryCompletedEvent) {
                        inquiryCompletedEvent.notifyAll();
                    }
                }

                /* To find service on bluetooth */
                public void serviceSearchCompleted(int transID, int respCode) {
                }

                /* To find service on bluetooth */
                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                }
            };

            synchronized (inquiryCompletedEvent) {
                boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
                if (started) {
                    System.out.println("Waiting for device inquiry to complete...");
                    inquiryCompletedEvent.wait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return device;
    }
}
