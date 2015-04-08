package com.cultureMachine.api.impl;

import java.util.HashMap;
import java.util.Map;

import com.cultureMachine.api.Config;
import com.cultureMachine.api.Parser;
import com.cultureMachine.util.ConfigKeyEnum;

public class StringParser implements Parser<Map<String, String[]> >{

	Config config ;
	
	public StringParser(Config config) throws IllegalArgumentException {
		if (config == null) {
			throw new IllegalArgumentException("No config for record parsing") ;
		}
		this.config = config ;
	}
		
	@Override
	public Map<String,String[]> parse(String s) {
		
		Map<String, String[]> map = new HashMap<>() ;		
	
		if (s == null || s.isEmpty()
				|| !s.startsWith(config.getProperty(ConfigKeyEnum.RECORD_STARTS.key()))
				|| !s.endsWith(config.getProperty(ConfigKeyEnum.RECORD_ENDS.key()))) {

			return null ;
		}
		s= new String(s.trim());
		s = s.replace(config.getProperty(ConfigKeyEnum.RECORD_STARTS.key()), "") ;
		s = s.replace(config.getProperty(ConfigKeyEnum.RECORD_ENDS.key()), "") ;
		
		String[] tuples = s.split(config.getProperty(ConfigKeyEnum.TUPLE_SEPARATOR.key())) ;
		String[] kv = new String[2] ;
		for (String tuple : tuples) {
			kv = tuple.trim().split(config.getProperty(ConfigKeyEnum.KV_SEPARATOR.key()));
			map.put(kv[0], (kv[1].split(config.getProperty(ConfigKeyEnum.VALUE_SEPARATOR.key()))));

		}			
 		
		return map;
	}
	
}
