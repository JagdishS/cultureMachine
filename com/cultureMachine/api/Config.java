package com.cultureMachine.api;

import java.util.Collection;
import java.util.Set;

public interface Config {
	
	public String getProperty(String key) ;
	
	public Set<Object> keySet() ;
	
	public Collection<Object> values() ;

}
