package com.cultureMachine.api.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.cultureMachine.api.ContentAPI;
import com.cultureMachine.concurrent.ThreadPoolUtil;
import com.cultureMachine.concurrent.VideoDataFileReader;
import com.cultureMachine.objects.ContentFormat;
import com.cultureMachine.util.ConfigKeyEnum;
import com.cultureMachine.util.ConfigReader;
import com.cultureMachine.util.ContentFormatComparator;
import com.cultureMachine.util.ContentFormatFileReader;

public class ContentApiImpl implements ContentAPI<ContentFormat> {

	private String fileLocation ;
	private static final int TOP_N_CONTENT_FORMATS = 20 ;
	private final RandomAccessFile raf ;
	private long bytesPerThread = 1024 * 1024 * 20; 
	private static final int TOP_COUNT_MULTIPLIER = 100 ;
	private static final int TASKS_PER_RUN = 50 ;
	
	@Override
	public synchronized List<ContentFormat> getTopContentFormats(int n) {
		
		List<VideoDataFileReader> tasks = this.getTasks() ;
		
		if(tasks.size() == 0) {
			throw new RuntimeException("No data file to read from");
		}
		
		List<ContentFormat> list = this.execute(tasks);
				
		Collections.sort(list, new ContentFormatComparator()) ;
	
		return list.subList(0, (n > list.size()) ? list.size() : n) ;
	}
	
	private ContentApiImpl(String fileLocation) {
		this.fileLocation = fileLocation ;
		try {
			this.raf = new RandomAccessFile(new File(fileLocation), "r") ;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("No file found to read ", e);
		}
	}

	public String getFileLocation() {
		return fileLocation;
	}
	
	public static final ContentApiImpl getInstance() throws RuntimeException {
		return new ContentApiImpl(ConfigReader.getInstance().getProperty(ConfigKeyEnum.DEFAULT_FILE_LOCATION.key())) ;
	}

	public static final ContentAPI<ContentFormat> getInstance(String fileLocation) throws RuntimeException {
		if(fileLocation == null || fileLocation.trim().isEmpty()) {
			fileLocation = ConfigReader.getInstance().getProperty(ConfigKeyEnum.DEFAULT_FILE_LOCATION.key()) ;
		}		
		return new ContentApiImpl(fileLocation) ;		
	}
	
	private List<ContentFormat> execute(List<VideoDataFileReader> tasks) {
		
		ExecutorService service = ThreadPoolUtil.getDataFileReaderThreadPool() ;
		
		List<Future<List<ContentFormat>>> result = null ;
		Map<String, ContentFormat> map = new HashMap<>() ;		
		
		for(int i = 0; i < tasks.size(); i=(i+TASKS_PER_RUN) ) {
			try {
				result = service.invokeAll(tasks.subList(i, ((i+TASKS_PER_RUN-1) < tasks.size() ? (i+TASKS_PER_RUN-1) : tasks.size())));
				Runtime.getRuntime().gc();
				for (Future<List<ContentFormat>> future : result) {
					try {
						List<ContentFormat> tmpList = future.get();
						for(ContentFormat cf : tmpList) {
							if(map.containsKey(cf.getName())) {
								map.get(cf.getName()).incrementCount(cf.getCount()) ;
							} else {
								map.put(cf.getName(), cf) ;
							}
				
						}
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
						throw new RuntimeException(e) ;
					}								
				}				
				result = null;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				throw new RuntimeException(e1);
			}
		}
		List<ContentFormat> tmp = new ArrayList<>();
		tmp.addAll(map.values()) ;
		return tmp ;
	}
	
	private List<VideoDataFileReader> getTasks() {
		
		long fileSize = 0l;
		
		try {
			fileSize = raf.length();
		} catch (IOException e2) {
			e2.printStackTrace();
			throw new RuntimeException("Could not read file "+fileLocation, e2) ;
		}
		
		final List<VideoDataFileReader> tasks = new ArrayList<>() ;
		Set<ContentFormat> cfList = ContentFormatFileReader.getInstance().getContentFormats() ;
		VideoDataFileReader reader = null;
		for(long i = 0 ; i< fileSize; ) {
			try {
				reader = new VideoDataFileReader(fileLocation, i,
						(bytesPerThread + i), new StringParser(
								ConfigReader.getInstance()),
						TOP_N_CONTENT_FORMATS * TOP_COUNT_MULTIPLIER, new ContentFormatApplicationImpl(cfList));
				
				tasks.add(reader) ;				
			} catch (IllegalArgumentException | IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not read file "+fileLocation, e) ;				
			}
			i = i + bytesPerThread+1 ;
		}
		try {
			raf.close() ;
		} catch (IOException e2) {
			e2.printStackTrace();
			throw new RuntimeException("Could not close file "+fileLocation, e2) ;
		}
		return tasks ;	
	}
	
}
