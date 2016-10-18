package org.steelhead.ftc;

/**
 * Created by alecmatthews on 10/18/16.
 */
public class ArrayQueue<AnyType> {
    private AnyType[] queue;
    private int queueSize;
    private int head;
    private int tail;

    private static final int DEAFAULT_SIZE = 4;

    @SuppressWarnings("unchecked")
    private AnyType[] newQueue(int size) {
        return (AnyType[]) new Object[size];
    }

    public ArrayQueue() {
        queue = newQueue(DEAFAULT_SIZE);
        queueSize = queue.length;
        head = 0;
        tail = 0;
    }

    public void close() {
        while (!isEmpty()) remove();
    }

    public boolean isEmpty() {
        return head == tail;
    }

    public int length() {
        return (tail + queueSize - head) % queueSize;
    }

    public void add(AnyType element) {
        int nextTail = (tail + 1) % queueSize;
        if(nextTail == head) {
            int i;
            AnyType[] nextQueue = newQueue(2 * queueSize);

            for (i = 0; head != tail; i++, head = (head + 1) % queueSize) {
                nextQueue[i] = queue[head];
                queue[head] = null;
            }
            queue = nextQueue;
            queueSize = queue.length;
            head = 0;
            tail = i;
            nextTail = (tail + 1) % queueSize;
        }

        queue[tail] = element;
        tail = nextTail;
    }

    public AnyType remove() {
        AnyType element;

        if(isEmpty()) return null;
        element = queue[head];
        queue[head] = null;
        head = (head + 1) % queueSize;
        return element;
    }
}
