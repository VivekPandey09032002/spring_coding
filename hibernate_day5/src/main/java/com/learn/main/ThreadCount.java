package com.learn.main;


import java.lang.management.ManagementFactory;

public class ThreadCount {
    public static void main(String[] args) {
        System.out.println("Thread.activeCount() = " + Thread.activeCount());
        System.out.println("Thread.getAllStackTraces() = " + Thread.getAllStackTraces());
        System.out.println("ManagementFactory.getThreadMXBean().getDaemonThreadCount() = " + ManagementFactory.getThreadMXBean().getDaemonThreadCount());

        System.out.println("ManagementFactory.getThreadMXBean().getThreadCount() = " + ManagementFactory.getThreadMXBean().getThreadCount());
        System.out.println("ManagementFactory.getThreadMXBean().getTotalStartedThreadCount() = " + ManagementFactory.getThreadMXBean().getTotalStartedThreadCount());
    }
}
