package com.cultureMachine.util;

import java.util.Comparator;

import com.cultureMachine.objects.ContentFormat;

public class ContentFormatComparator implements Comparator<ContentFormat> {

	@Override
	public int compare(ContentFormat obj1, ContentFormat obj2) {
		if(obj1.getCount() == obj2.getCount()) {
			return 0;
		} else if (obj1.getCount() > obj2.getCount()){
			return -1;
		} else {
			return 1;
		}
	}
	
	

}
