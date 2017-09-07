package org.firstinspires.ftc.teamcode._Libs;

/**
 * A utility class of some useful filters.
 * Created by phanau on 1/1/16.
 */
public class FilterLib {

    public static interface Filter {
        public double currentValue();
        public void appendValue(double v);
    }

    public static class LowPassFilter implements Filter {
        private double m_value;
        private double m_const;
        public LowPassFilter() { m_const = 0.5; m_value = 0.0; }
        public LowPassFilter(double a, double v) { m_const = a; m_value = v; }
        public double currentValue() { return m_value; }
        public void appendValue(double v) { m_value = (1.0-m_const)*m_value + m_const*v; }
    }

    public static class MovingWindowFilter implements Filter {
        protected double m_buffer[];
        protected int m_wsize;
        protected int m_index;

        public MovingWindowFilter() { m_wsize = 10; initialize(0); }
        public MovingWindowFilter(int wsize) { m_wsize = wsize; initialize(0); }
        public MovingWindowFilter(int wsize, double ivalue) { m_wsize = wsize; initialize(ivalue); }
        private void initialize(double ival) {
            m_buffer = new double[m_wsize];
            m_index = 0;
            for (int i=0; i<m_wsize; i++) m_buffer[i] = ival;
        }
        public double currentValue() {
            double sum = 0;
            for (int i=0; i<m_wsize; i++) sum += m_buffer[i];
            return sum/m_wsize;
        }
        public void appendValue(double v) {
            m_buffer[m_index++] = v;                    // fill current entry
            if (m_index >= m_wsize) m_index = 0;        // wrap around
        }
    }

    // specialized MWF for angles in degrees in the range (-180:+180)--
    // since angles wrap around, you can't just "average" them -
    // the average of -179 and +179 is +-180, not 0.
    // this averaging method was taken from
    // http://stackoverflow.com/questions/4699417/android-compass-orientation-on-unreliable-low-pass-filter
    public static class MovingWindowAngleFilter extends MovingWindowFilter {
        public MovingWindowAngleFilter() {
            super();
        }
        public MovingWindowAngleFilter(int wsize) {
            super(wsize);
        }
        public double currentValue() {
            int difference = 0;
            for(int i= 1;i <m_wsize;i++){
                difference += ( (m_buffer[i]- m_buffer[0] + 180 + 360 ) % 360 ) - 180;
            }
            double avg = (360 + m_buffer[0] + ( difference / m_wsize ) ) % 360;
            if (avg > 180)
                avg -= 360;
            return avg;
        }
    }
}
