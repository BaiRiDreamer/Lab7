package org.example.Task1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountLock {
    private double balance;
    private final Lock lock = new ReentrantLock();

    AccountLock (double balance) {
        this.balance = balance;
    }


    /**
     * 使用 ReentrantLock 确保线程安全
     * @param money 存款金额
     */
    public void deposit(double money) {
        lock.lock(); // 加锁
        try {
            double newBalance = balance + money;
            try {
                Thread.sleep(10);   // 模拟服务处理时间
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            balance = newBalance;
        } finally {
            lock.unlock(); // 确保释放锁
        }
    }

    public double getBalance() {
        return balance;
    }
}