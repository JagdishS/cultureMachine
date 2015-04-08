package com.cultureMachine.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
	
	private static ExecutorService service  ;
	private static final int THREAD_COUNT = 1;
	
	public static ExecutorService getDataFileReaderThreadPool() {
		if(service == null ) {
			synchronized (ThreadPoolUtil.class) {
				if(service == null) {
					
					service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * THREAD_COUNT);
				}
			}
		}
		return service ;
	}
		
	private ThreadPoolUtil(){
		
	}
}
