package com.cultureMachine.objects;

public class ContentFormat implements Comparable<ContentFormat> {
	
	private String name ;
	private long count ;

	public ContentFormat(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void incrementCount(long value) {
		this.count = this.count + value ;
	}

	public Long getCount() {
		return count;
	}

	@Override
	public int compareTo(ContentFormat o) {
		if(o == null ) {
			return -1 ;
		} else {
			if(this.getCount().longValue() < o.getCount().longValue()) {
				return -1;
			} else if (this.getCount().longValue() == o.getCount().longValue()){
				return 0 ;
			} else {
				return 1 ;
			}
		}
		
	}
	
	@Override
	public int hashCode() {		
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof ContentFormat)) {
			return false ;
		}
		ContentFormat cf = (ContentFormat) obj;
		return this.getName().equalsIgnoreCase(cf.getName());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder() ;
		sb.append("Name_");
		sb.append(this.getName()) ;
		sb.append("_Count_") ;
		sb.append(this.getCount()) ;
		return sb.toString() ;
	}
}
