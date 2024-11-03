package org.example.Task1;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class Test {

    @State(Scope.Benchmark)
    public static class AccountState {
        public Account accountSync = new Account(0); // 使用 synchronized 的账户
        public AccountLock accountLock = new AccountLock(0); // 使用 ReentrantLock 的账户
    }

    @Benchmark
    @Threads(2) // 使用2个线程
    public void testAccountSync(AccountState state) throws InterruptedException {
        for (int i = 0; i < 50; i++) { // 每个线程操作50次
            state.accountSync.deposit(100);
        }
    }

    @Benchmark
    @Threads(2) // 使用2个线程
    public void testAccountLock(AccountState state) throws InterruptedException {
        for (int i = 0; i < 50; i++) { // 每个线程操作50次
            state.accountLock.deposit(100);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Test.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(5)
                .warmupTime(TimeValue.seconds(3))
                .measurementTime(TimeValue.seconds(7))
                .build();

        new Runner(options).run();
    }
}
