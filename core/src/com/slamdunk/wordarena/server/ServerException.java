package com.slamdunk.wordarena.server;

public class ServerException extends Exception {

	private static final long serialVersionUID = 1204641162966125335L;

	private Exception rootException;
	
	public ServerException(Exception rootException) {
		this.rootException = rootException;
	}

	public Exception getRootException() {
		return rootException;
	}
}
