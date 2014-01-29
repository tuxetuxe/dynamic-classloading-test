package com.eyefreight.classloader.core;

import java.io.File;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class Main {

	public static void main(String[] args) {

		printStats("## Initial");
		ContentProcessor processor = new ContentProcessor();

		printStats("1) Before Processor run");
		processor.process(new File("../classloader-transformer-hello/target/classes"), "A test string");
		printStats("1) After Processor run");

		printStats("2) Before Processor run");
		processor.process(new File("../classloader-transformer-appender/target/classes"), "A test string");
		printStats("2) After Processor run");

		printStats("3) Before Processor run");
		processor.process(new File("../classloader-transformer-new-appender/target/classes"), "A test string");
		printStats("3) After Processor run");

		printStats("4) Before Processor run");
		processor.process(new File("../classloader-transformer-appender/target/classes"), "A test string");
		printStats("4) After Processor run");

		processor = null;
		System.gc();
		printStats("## Final");
	}

	private static void printStats(String message) {

		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		long usedHeap = memoryMXBean.getHeapMemoryUsage().getUsed();
		long nonUsedHeap = memoryMXBean.getNonHeapMemoryUsage().getUsed();
		long objectsPendingFinalizationCount = memoryMXBean.getObjectPendingFinalizationCount();

		ClassLoadingMXBean classloadingMXBean = ManagementFactory.getClassLoadingMXBean();
		long loadedClassCount = classloadingMXBean.getLoadedClassCount();
		long unloadedClassCount = classloadingMXBean.getUnloadedClassCount();
		long totalLoadedClassCount = classloadingMXBean.getTotalLoadedClassCount();

		System.out.print(message);
		System.out.print("=> Heap [" + usedHeap + " - " + nonUsedHeap + " - " + objectsPendingFinalizationCount + "] ");
		System.out.print("=> ClassCount [" + loadedClassCount + " - " + unloadedClassCount + " - " + totalLoadedClassCount + "] ");
		System.out.println();

	}
}
