package com.lazyDroid.jetty;

class User {
	final String username;
	final String hashedPW;
	private int safePoint;
	User(String username, String hashedPW) {
		this.username = username;
		this.hashedPW = hashedPW;
		safePoint = 10;
	}
	
	int getSafePoint() {
		return this.safePoint;
	}
	
	void updateSafePoint(int newSafePoint) {
		this.safePoint = newSafePoint;
	}
}
