In order to run TRP, you need a `jar` file for it. There are two ways to get it.

## Downloading a Release

Unfortunately, at the time of this writing, there are no releases. You have to compile the source. If there were releases, I would list them here.

## Compiling the Source

To compile the source, you need to install the [Java 8 JDK][1] and [Maven][2]. When you've done that, clone this repository, open a shell/command prompt in the repo directory, and type:

    mvn install

You'll find several `jar` files under the `target` directory. 

# Running the JAR

Open a command prompt in the directory with the `jar` and type:

    java -jar <jar file name>

It runs!!!

[1]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[2]: http://maven.apache.org/download.cgi