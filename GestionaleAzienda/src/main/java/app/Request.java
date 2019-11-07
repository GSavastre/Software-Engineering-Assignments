package app;

import java.io.Serializable;

public class Request implements Serializable{
	private static final long serialVersiounUID = 1L;
	
	private final int value;
	
	public Request(final int v) {
		this.value = v;
	}
	
	public int GetValue() {
		return this.value;
	}
}
