package com.cultureMachine.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import com.cultureMachine.api.Config;

public class ConfigReader implements Config {
	
	private static volatile ConfigReader instance = null ;
	private Properties prop = null ;
	
	private void init()  {
		
		if(prop == null) {
			prop =  new Properties() ;
			InputStream is = getClass().getClassLoader().getResourceAsStream("resources/config.properties") ;
		
			if (is != null) {
				try {
					prop.load(is);					
					
				} catch (IOException e) {
					throw new RuntimeException("Config file not found", e) ;
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						throw new RuntimeException("Exception while closing config file ", e) ;
					}
					
				}
			}
		}
				
	}
	
	private ConfigReader() {
		
	}
	
	public static final Config getInstance() {
		
		if(instance == null) {
			synchronized (ConfigReader.class) {
				if(instance == null) {
					instance = new ConfigReader() ;
					instance.init() ;					
				}
			}
		}
		
		return instance ;
	}
	
	public String getProperty(String key) {		
		return prop.getProperty(key);
	}
	
	public Set<Object> keySet() {		
		return prop.keySet();
	}
	
	public Collection<Object> values() {
		return prop.values() ;
	}

}
