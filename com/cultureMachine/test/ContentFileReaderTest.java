package com.cultureMachine.test;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cultureMachine.objects.ContentFormat;
import com.cultureMachine.util.ContentFormatFileReader;

public class ContentFileReaderTest {
	
	private RandomAccessFile raf = null;

	@Before
	public void testFileRead() throws Exception {
		URL url = getClass().getClassLoader().getResource("resources/contentFormats.txt") ;
		File file = new File(url.toURI()) ;
		raf = new RandomAccessFile(file, "rw") ;
		 
	}
	
	@Test
	public void test() throws Exception {
		
		raf.writeBytes("How to") ;
		raf.writeBytes("\n") ;
		raf.writeBytes("How to cook") ;
		raf.writeBytes("\n") ;
		raf.writeBytes("How to eat") ;
		
		ContentFormatFileReader reader = ContentFormatFileReader.getInstance() ;
		assertNotNull(reader);
		Set<ContentFormat> set = reader.getContentFormats() ;
		assertNotNull(set);
		Assert.assertEquals(3, set.size()) ;
		ContentFormat s = new ContentFormat("How to") ;
		Assert.assertTrue(set.contains(s)) ;
		
	}
		
	@After
	public void close() throws Exception {
		
		raf.close() ;
	}

}
