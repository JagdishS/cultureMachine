package com.cultureMachine.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.cultureMachine.objects.ContentFormat;

public class ContentFormatFileReader {
	
	private Set<ContentFormat> contentFormats ;
	private static volatile ContentFormatFileReader INSTANCE ;
	
	public final Set<ContentFormat> getContentFormats() {
		return contentFormats ;
	}
	
	private final Set<ContentFormat> reload(){
		if (contentFormats == null) {
			contentFormats = new HashSet<>();
		} else {
			contentFormats.clear() ;
		}
		
		String s = null ;
		RandomAccessFile raf = null ;
		try {
			URL url = getClass().getClassLoader().getResource("resources/contentFormats.txt") ;
			File file = new File(url.toURI()) ;
			raf = new RandomAccessFile(file, "r") ;
			long length = raf.length() ;
			raf.seek(0);			
			while (raf.getFilePointer() < length) {
				s = new String(raf.readLine().trim()) ;
				
				ContentFormat cf = new ContentFormat(s);			
				contentFormats.add(cf) ;
			}
		
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to read content format file ", e) ;
		} finally {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to read content format file ", e) ;
			}
		}
		return contentFormats ;
	}

	private ContentFormatFileReader() {
		
	}
	
	public static ContentFormatFileReader getInstance() {
		if (INSTANCE == null) {
			synchronized (ContentFormatFileReader.class) {
				if (INSTANCE == null) {
					INSTANCE = new ContentFormatFileReader() ;
					INSTANCE.reload() ;
				}
			}
		}
		return INSTANCE ;
	}
}
