package com.cultureMachine.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cultureMachine.api.ContentAPI;
import com.cultureMachine.api.impl.ContentApiImpl;
import com.cultureMachine.objects.ContentFormat;

public class ContentApiTest {
	
	private final String fileLocation = "/tmp/data.txt";

	@Before
	public void createFile() {
		try {
			System.out.println("File Creation starts");
			long startTime = System.currentTimeMillis() ;
			
			File f = new File(fileLocation) ;
			
			RandomAccessFile file = new RandomAccessFile(f, "rw") ;
			
			for (int i = 0; i < 100_000; i++) {
				StringBuilder sb = new StringBuilder();
				sb.append("{");
				sb.append("title:");
				sb.append("Video Title " + (int) ( Math.random() * i) );
				sb.append(",");
				sb.append("category:");
				sb.append("Video category");
				sb.append(",");
				sb.append("keywords:");
				sb.append("Keyword " + (int) (Math.random() * i/1000) );
				sb.append("|");
				sb.append("Keyword " + (int) (Math.random() * i/(1000*i)) );
				sb.append("}");
				sb.append("\n");
				file.writeBytes(sb.toString());
				
				sb = null;
			}
			file.close();
			
			System.out.println("Time taken to create file "+(System.currentTimeMillis() - startTime));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Test
	public void testTaskExecution() {	
		
		try {
		ContentAPI<ContentFormat> api = ContentApiImpl.getInstance(this.fileLocation) ;
			assertNotNull(api);
			List<ContentFormat> list = api.getTopContentFormats(20) ;
			assertTrue(list.size() < 21) ;
			System.out.println(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@After
	public void removeFile(){
		File f= new File(fileLocation);
		if(f.exists()) {
			f.delete() ;
		}
	}


	
}
