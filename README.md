<h1>bitsPlease</h1>
<h3>bitsPlease is a chat & scribble application created in Java, utilizing the Swing library.</h3>
It consists of a simple server application, along with a client application. The user has a chat area for sending text messages and images and a drawing area where users can simultaneously draw together in a real-time manner.

Preview:

![Screenshot 2023-11-09 at 12 25 00](https://github.com/hermanolvik/bitsPlease/assets/72079200/d1dabcb3-578f-4e88-8e2d-893b5cf61935)

Running the program directly from the source code:

Before you proceed with the following steps, please ensure that the Java Development Kit (JDK) is installed and updated to version 20.0.1 or newer.

1. Build and Start the Server
- Open a terminal window in the 'src' folder of the repository.
- Build the server program using the command: javac server/MainServer.java
- Note that this command must be executed while standing in the 'src' folder to ensure that all the repository's packages are visible during compilation, unless the classpath has already been set to the 'src' folder.
- Run the server program with the command: java server/MainServer.
2. Build and Start the Client
- Open a terminal window in the 'src' folder of the repository.
- Build the client program with the command: javac Main.java.
- Run the client program using the command: java Main.
