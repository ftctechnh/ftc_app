package org.usfirst.FTC5866.library;

/**
 * Created by Olavi Kamppari on 10/7/2015.
 * 
 * Added to Github on 11/16/2015 (https://github.com/OliviliK/FTC_Library/edit/master/ArrayQueue.java)
 */
public class ArrayQueue<AnyType> {
    private AnyType[]   queue;
    private int         queueSize;
    private int         head;
    private int         tail;

    private static final int DEFAULT_SIZE = 4;

    @SuppressWarnings("unchecked")              // The array casting is OK
    private AnyType[] newQueue(int size) {
        return (AnyType[]) new Object[size];
    }

    public ArrayQueue() {
        queue       = newQueue(DEFAULT_SIZE);
        queueSize   = queue.length;
        head        = 0;
        tail        = 0;
    }

    public void close(){
        while (!isEmpty()) remove();// Discard all elements
    }

    public boolean isEmpty() {return head == tail;}

    public int length() {return (tail + queueSize - head) % queueSize;}

    public void add(AnyType element) {
        int nextTail    = (tail +1) % queueSize;
        if (nextTail == head) { // The queue is full
            int i;              // Double the queue size
            AnyType[] nextQueue = newQueue(2 * queueSize);
                                // Copy the elements from the original queue
            for (i = 0; head != tail; i++, head = (head + 1) % queueSize) {
                nextQueue[i]    = queue[head];
                queue[head]     = null;         // Support garbage collection
            }
            queue       = nextQueue;
            queueSize   = queue.length;
            head        = 0;
            tail        = i;
            nextTail    = (tail +1) % queueSize;
        }
        queue[tail] = element;
        tail        = nextTail;
    }

    public AnyType remove() {
        AnyType element;
        if (isEmpty()) return null;
        element     = queue[head];
        queue[head] = null;         // Enable garbage collection
        head        = (head + 1) % queueSize;
        return element;
    }
}
