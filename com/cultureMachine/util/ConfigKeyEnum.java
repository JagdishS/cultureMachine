package com.cultureMachine.util;

public enum ConfigKeyEnum {
	
	RECORD_STARTS("recordStartChar") ,
	TUPLE_SEPARATOR ("tupleSeparator"),
	KV_SEPARATOR("keyValueSeparator") ,
	VALUE_SEPARATOR("valuesSeparator") ,
	RECORD_ENDS("recordEndChar") ,
	DEFAULT_FILE_LOCATION("fileLocation"),
	KEYWORDS("keywords");
	
	private String key ;
	
	private ConfigKeyEnum(String s) {
		this.key = s ;
	}
	
	public String key() {
		return key ;
	}
}
