package org.swerverobotics.library;

import com.qualcomm.robotcore.robocol.Telemetry;

import org.swerverobotics.library.thunking.IThunk;
import org.swerverobotics.library.thunking.ThreadThunkContext;
import org.swerverobotics.library.thunking.ThunkedTelemetry;

import java.util.*;

/**
 *
 */
public class TelemetryDashboard
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private Telemetry        unthunkedTelemetry = null;
    private ThunkedTelemetry thunkedTelemetry = null;
    private Vector<Line>     lines = null;
    private long             nanoLastUpdate = 0;

    public  double           msUpdateInterval = 1000;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public TelemetryDashboard(Telemetry unthunkedTelemetry)
        {
        this.thunkedTelemetry = null;
        this.unthunkedTelemetry = unthunkedTelemetry;
        this.clear();
        }

    public TelemetryDashboard(ThunkedTelemetry thunkedTelemetry)
        {
        this.thunkedTelemetry = thunkedTelemetry;
        this.unthunkedTelemetry = this.thunkedTelemetry.getTarget();
        this.clear();
        }

    public void clear()
        {
        this.lines = new Vector<Line>();
        }

    //----------------------------------------------------------------------------------------------
    // Types
    //----------------------------------------------------------------------------------------------

    private interface IStringValue
        {
        String stringValue();
        }

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

    //----------------------------------------------------------------------------------------------
    // Items
    //----------------------------------------------------------------------------------------------

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

    //----------------------------------------------------------------------------------------------
    // Lines
    //----------------------------------------------------------------------------------------------

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
    public void line(List<Item> items)
        {
        Line line = new Line();
        line.items = items;
        this.lines.add(line);
        }

    //----------------------------------------------------------------------------------------------
    // Emitting
    //----------------------------------------------------------------------------------------------

    public void emit()
    // Called on either a synchronous thread or the loop() thread itself.
        {
        final Vector<String> keys = new Vector<String>();
        final Vector<String> values = new Vector<String>();

        int iLine = 0;
        for (Line line : this.lines)
            {
            keys.add("line" + iLine);
            values.add(this.lines.elementAt(iLine).compose());
            iLine++;
            }

        SynchronousOpMode.getThreadThunker().executeOnLoopThreadASAP(new IThunk()
            {
            @Override public void doLoopThreadWork()
                {
                for (int i=0; i < keys.size(); i++)
                    {
                    TelemetryDashboard.this.unthunkedTelemetry.addData(
                        keys.elementAt(i),
                        values.elementAt(i)
                        );
                    }
                }
            });
        }

    public synchronized void update()
        {
        long nanoNow = System.nanoTime();
        if (this.nanoLastUpdate == 0 || nanoNow > this.nanoLastUpdate + this.msUpdateInterval * SynchronousOpMode.NANO_TO_MILLI)
            {
            this.emit();
            this.nanoLastUpdate = nanoNow;
            }
        }
    }
