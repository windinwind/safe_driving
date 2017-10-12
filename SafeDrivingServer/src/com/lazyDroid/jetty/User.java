package com.lazyDroid.jetty;

class User {
	final String username;
	final String hashedPW;
	User(String username, String hashedPW) {
		this.username = username;
		this.hashedPW = hashedPW;
	}
}
