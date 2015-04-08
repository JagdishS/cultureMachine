package com.cultureMachine.test;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cultureMachine.api.Parser;
import com.cultureMachine.api.impl.StringParser;
import com.cultureMachine.util.ConfigReader;


public class ParserTest {

	private Parser<Map<String, String[]>> stringParser ;
	
	@Before
	public void init() {
		
		this.stringParser = new StringParser(ConfigReader.getInstance());
	}	
	
	@Test
	public void testParse() {
		
		String s = "{title: Cook rice easy and tasty, category: video category, keywords: cooking | Tasty recipie  }";
		Map<String, String[]> map = this.stringParser.parse(s);
		assertTrue(map.containsKey("title")) ;
		assertTrue(map.containsKey("category")) ;
		assertTrue(map.containsKey("keywords")) ;
		assertTrue(map.get("keywords").length == 2) ;
		s= "{title: Cook rice easy and tasty, category: video category, keywords: cooking  }";
		map = this.stringParser.parse(s);
		assertTrue(map.get("keywords").length == 1) ;
		s= "{title: Cook rice easy and tasty, category: video category, keywords: cooking |  }";
		map = this.stringParser.parse(s);
		assertTrue(map.get("keywords").length == 1) ;
	}
		
}
