package com.jm.db;

public class PatternFile {
	private long id;
	private String name;
	private byte[] value;
	private boolean isWritable;
	
	public PatternFile(long id, String name, byte[] value, boolean isWritable){
		this.id = id;
		this.name = name;
		this.value = value;
		this.isWritable = isWritable;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public byte[] getValue() {
		return value;
	}
	
	public boolean isWritable(){
		return isWritable;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
