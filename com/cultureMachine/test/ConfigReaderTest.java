package com.cultureMachine.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;

import com.cultureMachine.api.Config;
import com.cultureMachine.util.ConfigReader;
import com.cultureMachine.util.ConfigKeyEnum;

public class ConfigReaderTest {

	@Test
	public void test() {
		Config reader = ConfigReader.getInstance();
		assertNotNull(reader);
		
	}
	
	@Test
	public void testFileLoad() {
		Config reader = ConfigReader.getInstance();
//		System.out.println(reader.values());
		assertNotNull(reader.getProperty("recordStartChar")) ;
		Assert.assertTrue(reader.getProperty("recordStartChar").equals("{"));
		assertNotNull(reader.getProperty(ConfigKeyEnum.RECORD_STARTS.key())) ;
//		System.out.println("recordStartChar "+reader.getProperty("recordStartChar"));
		assertNotNull(reader.getProperty("fileLocation")) ;
//		System.out.println("fileLocation "+reader.getProperty("fileLocation"));
	}

}
