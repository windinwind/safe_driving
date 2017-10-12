# Safe Driving

# Server Info
* Server IP: 34.210.113.123
* Port Number: 80
* Language: Java
* The server-side is implemented based on Jetty and RESTful web services

# User login pass format
* hash(first character of the password + username + last character of the password + user password + lazyDroid)
* example: if username = "usrabc", user password = "abcdef", the userpass should be hash("ausrabcfabcdeflazyDroid")
* The preset user in the server is:
  * username: lazyDroid
  * user password: Cpen321SD#
