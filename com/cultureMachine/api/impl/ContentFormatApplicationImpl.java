package com.cultureMachine.api.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cultureMachine.api.ContentFormatApplication;
import com.cultureMachine.objects.ContentFormat;

public class ContentFormatApplicationImpl implements ContentFormatApplication<String> {
	
	private Set<ContentFormat> cfList = null ;

	@Override
	public Set<String> applyContentFormats(List<String> keywords) {
		// TODO Auto-generated method stub
		// Not sure how to apply content formats to keywords, hence returning keywords
		return new HashSet<>(keywords);
	}
	
	public ContentFormatApplicationImpl(final Set<ContentFormat> cfList) throws IllegalArgumentException {
		if(cfList == null) {
			throw new IllegalArgumentException() ;
		}
		this.cfList = cfList;
	}

}
