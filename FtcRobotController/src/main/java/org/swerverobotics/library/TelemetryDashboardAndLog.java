package org.swerverobotics.library;

import com.qualcomm.robotcore.robocol.Telemetry;

import org.swerverobotics.library.thunking.ThunkedTelemetry;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 *
 */
public class TelemetryDashboardAndLog
    {
    //----------------------------------------------------------------------------------------------
    // Types
    //----------------------------------------------------------------------------------------------

    private interface IStringValue
        {
        String stringValue();
        }

    //==============================================================================================

    public class Dashboard
        {
        //------------------------------------------------------------------------------------------
        // State
        //------------------------------------------------------------------------------------------

        private Vector<Dashboard.Line>  lines = null;
        private Vector<String>          values;

        //------------------------------------------------------------------------------------------
        // Types
        //------------------------------------------------------------------------------------------

        public class Item
            {
            String       caption;
            IStringValue value;

            public void composeTo(StringBuilder builder)
                {
                builder.append(this.caption);
                builder.append(this.value.stringValue());
                }
            }

        public class Line
            {
            List<Item> items;

            public String compose()
                {
                StringBuilder result = new StringBuilder();
                boolean first = true;
                for (Item item : this.items)
                    {
                    if (!first)
                        {
                        result.append(" | ");
                        }
                    item.composeTo(result);
                    first = false;
                    }
                return result.toString();
                }
            }

        //------------------------------------------------------------------------------------------
        // Evaluation
        //------------------------------------------------------------------------------------------

        public synchronized void clear()
            {
            this.lines = new Vector<Dashboard.Line>();
            }

        //------------------------------------------------------------------------------------------
        // Items
        //------------------------------------------------------------------------------------------

        public Item item(final String itemCaption, final ITelemetryValue itemValue)
            {
            Item result = new Item();
            result.caption = itemCaption;
            result.value = new IStringValue()
            {
            @Override public String stringValue()
                {
                return itemValue.value().toString();
                }
            };
            return result;
            }

        //------------------------------------------------------------------------------------------
        // Lines
        //------------------------------------------------------------------------------------------

        public void line()
            {
            this.line(new ArrayList<Item>());
            }
        public void line(Item item)
            {
            ArrayList<Item> items = new ArrayList<Item>();
            items.add(item);
            this.line(items);
            }
        public void line(Item item0, Item item1)
            {
            ArrayList<Item> items = new ArrayList<Item>();
            items.add(item0);
            items.add(item1);
            this.line(items);
            }
        public void line(Item item0, Item item1, Item item2)
            {
            ArrayList<Item> items = new ArrayList<Item>();
            items.add(item0);
            items.add(item1);
            items.add(item2);
            this.line(items);
            }
        public synchronized void line(List<Item> items)
            {
            Line line = new Line();
            line.items = items;
            this.lines.add(line);
            }

        //------------------------------------------------------------------------------------------
        // Emitting
        //------------------------------------------------------------------------------------------

        public void update()
            {
            TelemetryDashboardAndLog.this.update();
            }
        }

    //==============================================================================================

    public class Log
        {
        //------------------------------------------------------------------------------------------
        // State
        //------------------------------------------------------------------------------------------

        private Queue<String> logQueue = new LinkedList<>();
        private boolean dirty = false;

        public int capacity = 5;

        //------------------------------------------------------------------------------------------
        // Operations
        //------------------------------------------------------------------------------------------

        /**
         * Add a new log message to be emitted to the telemetry as soon as we can
         */
        public void add(String msg)
            {
            synchronized (this)
                {
                this.logQueue.add(msg);

                while (this.logQueue.size() > this.capacity)
                    {
                    this.logQueue.remove();
                    }

                this.dirty = true;
                }

            TelemetryDashboardAndLog.this.update();
            }
        }

    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private Telemetry               unthunkedTelemetry = null;
    private ThunkedTelemetry        thunkedTelemetry = null;
    private long                    nanoLastUpdate = 0;
    private int                     singletonKey = SynchronousOpMode.getNewExecuteSingletonKey();

    /**
     * msUpdateInterval is the interval in milliseconds at which updates are in fact transmitted
     * to the driver station.
     */
    public double                   msUpdateInterval = 1000;

    public final Dashboard          dashboard = new Dashboard();
    public final Log                log = new Log();

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public TelemetryDashboardAndLog(Telemetry unthunkedTelemetry)
        {
        this.thunkedTelemetry = null;
        this.unthunkedTelemetry = unthunkedTelemetry;
        this.dashboard.clear();
        }

    public TelemetryDashboardAndLog(ThunkedTelemetry thunkedTelemetry)
        {
        this.thunkedTelemetry = thunkedTelemetry;
        this.unthunkedTelemetry = this.thunkedTelemetry.getTarget();
        this.dashboard.clear();
        }

    //----------------------------------------------------------------------------------------------
    // Emitting
    //----------------------------------------------------------------------------------------------

    /**
     * If you want to acquire both the dashboard and the log lock, then this method
     * does it in the right order.
     */
    private void synchronizeDashboardAndLog(IAction action)
        {
        synchronized (this.dashboard)
            {
            synchronized (this.log)
                {
                action.doAction();
                }
            }
        }

    private static String getKey(int iLine)
        {
        return "line" + String.format("%04d", iLine);
        }

    /**
     * At the next available loop() thread call, update the telemetry to reflect the state
     * of the dashboard and log.
     */
    private void update()
        {
        // We need both the dashboard and log locked while we copy their contents
        this.synchronizeDashboardAndLog(new IAction()
            {
            @Override public void doAction()
                {
                // Don't actually put out updates too often in order to avoid flooding. So,
                // log additions flow as soon as we can, but otherwise, we only update things
                // at periodic intervals.
                long nanoNow = System.nanoTime();
                if (nanoLastUpdate == 0
                        || nanoNow > nanoLastUpdate + msUpdateInterval * SynchronousOpMode.NANO_TO_MILLI
                        || log.dirty
                        )
                    {
                    // We're going to update the telemetry. Get a copy of all the data to output.
                    // We only use strings as values. Keys we make up in alphabetical order so as
                    // to maintaining the ordering in which they are created.
                    final Vector<String> keys = new Vector<String>();
                    final Vector<String> values = new Vector<String>();
                    int iLine = 0;

                    for (Dashboard.Line line : dashboard.lines)
                        {
                        keys.add(getKey(iLine));
                        values.add(dashboard.lines.elementAt(iLine).compose());
                        iLine++;
                        }

                    for (String s : log.logQueue)
                        {
                        keys.add(getKey(iLine));
                        values.add(s);
                        iLine++;
                        }

                    // Head on over to the loop() thread and add these messages to the
                    // (unthunked) telemetry. However, we only do that once per loop() call;
                    // if we attempt two of these within one loop() quantum (by, e.g., issuing
                    // a bunch of log.add() calls), then only the last one will actually manifest
                    // itself and thus get back to the driver station.
                    SynchronousOpMode.getThreadThunker().executeSingletonOnLoopThread(singletonKey, new IAction()
                        {
                        @Override public void doAction()
                            {
                            for (int i = 0; i < keys.size(); i++)
                                {
                                TelemetryDashboardAndLog.this.unthunkedTelemetry.addData(
                                    keys.elementAt(i),
                                    values.elementAt(i));
                                }
                            }
                        });

                    nanoLastUpdate = nanoNow;
                    log.dirty = false;
                    }
                }
            });
        }
    }
