package com.cultureMachine.concurrent;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.cultureMachine.api.ContentFormatApplication;
import com.cultureMachine.api.Parser;
import com.cultureMachine.objects.ContentFormat;
import com.cultureMachine.util.ConfigKeyEnum;
import com.cultureMachine.util.ContentFormatComparator;

public class VideoDataFileReader implements Callable<List<ContentFormat>> {

	private long startPosition ;
	private long endPosition ;
	private Parser<Map<String, String[]>> parser ;
	private int topNContentFormats ;
	private String fileLocation;
	ContentFormatApplication<String> cfApplyStrategy ;
	
	/*
	 * Map that holds count for each content format
	 */
	private ConcurrentMap<String, ContentFormat> map = new ConcurrentHashMap<>() ;
	 
	public VideoDataFileReader(String fileLocation, long startPosition,
			long endPosition, Parser<Map<String, String[]>> parser,
			int topNContentFormats, ContentFormatApplication<String> cfApplyStrategy) throws IllegalArgumentException, IOException {
		
		if(fileLocation == null || fileLocation.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid file to read") ;
		}
		
		if (startPosition < 0 || endPosition < 0 || (startPosition >= endPosition)) {
			throw new IllegalArgumentException("Invalid file reading positions") ;
		}
		
		if(topNContentFormats < 0) {
			throw new IllegalArgumentException("topContentFormats invalid ") ;
		}
		
		if(parser == null) {
			throw new IllegalArgumentException("Invalid parser") ;
		}
		
		if(cfApplyStrategy == null) {
			throw new IllegalArgumentException("Invalid Strategy") ;
		}
		this.fileLocation = fileLocation;
		this.parser = parser ;
		this.startPosition = startPosition ;
		this.endPosition = endPosition ;
		this.topNContentFormats = topNContentFormats ;
		this.cfApplyStrategy = cfApplyStrategy ;
	}
	
	public long getStartPosition() {
		return startPosition;
	}

	public long getEndPosition() {
		return endPosition;
	}
	
	@Override
	public List<ContentFormat> call() throws Exception {	
		System.out.println(Thread.currentThread().getName() + " Inside call() method of FileReader with start "+getStartPosition() +" end "+getEndPosition());
		RandomAccessFile raf = null ;
		Map<String, String[]> lineContents = null ;

		try {			
			raf = new RandomAccessFile(fileLocation, "r") ;
			this.endPosition = (this.endPosition > raf.length()) ? raf.length() : this.endPosition ;
			raf.seek(startPosition) ;
			Set<String> contentsFormats = null ;
			
			while ((raf.getFilePointer() < endPosition)) {

				String s = new String(raf.readLine()) ;
				lineContents = parser.parse(s) ;
				
				if(lineContents == null ){
					continue ;
				}
				contentsFormats = cfApplyStrategy.applyContentFormats(Arrays.asList(lineContents.get(ConfigKeyEnum.KEYWORDS.key())));
				for(String contentFormat : contentsFormats) {
					map.putIfAbsent(contentFormat.trim(), new ContentFormat(contentFormat.trim()) );
					map.get(contentFormat.trim()).incrementCount(1L);
				}
				
			}			
		} catch (Exception e) {
			throw new RuntimeException("Error while reading file", e) ;
		} finally {
			raf.close();
		}
		List<ContentFormat> list = new ArrayList<>();
		list.addAll(map.values());
		
		return getTopList() ;
	}
	
	private List<ContentFormat> getTopList() {
		List<ContentFormat> list = new ArrayList<>();
		list.addAll(map.values()) ;

		Collections.sort(list, new ContentFormatComparator()) ;
		int size = list.size() /10 ;
		 
		return list.subList(0, (size > this.topNContentFormats) ? size : ( (list.size() > this.topNContentFormats) ? this.topNContentFormats : list.size()) );	
		
	}
		
}
