package com.cultureMachine.api;

/**
 * Parser API for parsing a line of data from file
 * @author jagdish
 *
 * @param <T>
 */
public interface Parser<T> {

	/**
	 * Receives a line read from file or any other source and parses it
	 * @param s
	 * @return parsed data
	 */
	T parse(String s) ;
}
