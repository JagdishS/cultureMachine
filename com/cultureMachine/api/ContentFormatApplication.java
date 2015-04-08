package com.cultureMachine.api;

import java.util.List;
import java.util.Set;


/**
 * Interface for applying content formats to given list of keywords
 * Class implementing this interface will put custom strategy on how content formats should be
 * applied to video
 * @author jagdish
 *
 * @param <T>
 */
public interface ContentFormatApplication<T> {
	
	/**
	 * Apply content formats to given list of keywords
	 * @param keywords
	 * @return Set of contents formats that can be applied
	 */
	Set<T> applyContentFormats(List<T> keywords) ;

}
