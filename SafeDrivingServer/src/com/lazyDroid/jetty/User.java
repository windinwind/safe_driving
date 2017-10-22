package com.lazyDroid.jetty;

/**
 * The class that represents the user in the safe driving project.
 * 
 * @author Kyrin Feng
 *
 */
class User {
	final String username;
	final String hashedPW;
	private int safePoint;

	/**
	 * The constructor of the user class.
	 * 
	 * @param username
	 *            - The user name for a user.
	 * @param hashedPW
	 *            - The hashed password for the user.
	 */
	User(String username, String hashedPW) {
		this.username = username;
		this.hashedPW = hashedPW;
		safePoint = 10; // Initialize the safe point to 10
	}

	/**
	 * Get the safe point for this user.
	 * 
	 * @return The safe point of the current user.
	 */
	int getSafePoint() {
		return this.safePoint;
	}

	/**
	 * Update the safe point for this user.
	 * 
	 * @param newSafePoint
	 *            - The updated safe point.
	 */
	void updateSafePoint(int newSafePoint) {
		this.safePoint = newSafePoint;
	}
}
