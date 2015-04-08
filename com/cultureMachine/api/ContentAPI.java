package com.cultureMachine.api;

import java.io.IOException;
import java.util.List;

/**
 * Client facing API for fetching top content formats
 * @author jagdish
 *
 * @param <T>
 */
public interface ContentAPI<T> {
	
	/**
	 * Fetch top n content formats 
	 * @param n Top "n" content formats
	 * @return
	 * @throws IOException
	 */
	List<T> getTopContentFormats(int n) throws IOException;

}
