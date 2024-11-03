package org.example.Task2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<E> {
    private Queue<E> queue;
    private int capacity;

    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public MyBlockingQueue (int capacity){
        // Initialize queue and set capacity
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }

    public void put(E e){
        lock.lock();
        try {
            // Wait if the queue is full
            while (queue.size() == capacity) {
                notFull.await();
            }
            // Add the element to the queue
            queue.offer(e);
            System.out.println(Thread.currentThread().getName() + " Produced: " + e + ", Queue: " + queue);
            // Signal the consumer that the queue is not empty
            notEmpty.signal();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public E take() {
        lock.lock();
        try {
            // Wait if the queue is empty
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            // Remove the element from the queue
            E item = queue.poll();
            System.out.println(Thread.currentThread().getName() + " Consumed: " + item + ", Queue: " + queue);
            // Signal the producer that the queue is not full
            notFull.signal();
            return item;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {

        int CAPACITY = 200;
        int PRODUCER_WORK = 21;
        int PRODUCER_CNT = 100;
        int PRODUCER_OFF = 1000;
        int CONSUMER_WORK = 20;
        int CONSUMER_CNT = 100;
        int CONSUMER_OFF = 10;

        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(CAPACITY);

        Runnable producer = () -> {
            for(int i = 0; i < PRODUCER_WORK; i++) {
                queue.put(i);
                try {
                    Thread.sleep(PRODUCER_OFF);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable consumer = () -> {
            for(int i = 0; i < CONSUMER_WORK; i++) {
                queue.take();
                try {
                    Thread.sleep(CONSUMER_OFF);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        for (int i = 0; i < PRODUCER_CNT; i++) {
            new Thread(producer).start();
        }
        for (int i = 0; i < CONSUMER_CNT; i++) {
            new Thread(consumer).start();
        }
    }
}
