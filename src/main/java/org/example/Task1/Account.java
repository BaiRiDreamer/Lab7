package org.example.Task1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
	private double balance;
	private final Lock lock = new ReentrantLock();

	Account (double balance) {
		this.balance = balance;
	}


	/**
	 * 使用 Syncronized 确保线程安全
	 * @param money 存款金额
	 */
	public synchronized void deposit(double money) {
		synchronized (this) {
			double newBalance = balance + money;
			try {
				Thread.sleep(10);   // 模拟服务处理时间
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			balance = newBalance;
		}
	}

	public double getBalance() {
		return balance;
	}
}