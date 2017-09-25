## JarFind
*JarFind* is a Java development tool. It's purpose is to quickly find jar files which contain a particular Java .class file. Sooner or later as a Java developer you get stymied by the ClassNotFoundException, which is usually thrown when the jvm cannot load a particular class which has been referenced within some running thread. So, if you don't know which jar file contains that particular class, and where that jar file is located, then you have a quest ahead of you.

*JarFind* will find the jar for you. You just have to point it in the right direction. *JarFind* takes two arguments: a directory (in which to search) and a Java class name.  

### Install
Ok, let's say you compile the source and create an executable jar named jarfind.jar and put that somewhere convienient (like /opt/java/jarfind.jar or ~/bin/jarfind.jar). So, assuming you have the compiled code under ./build, you could do something like:

    jar -cvfe ~/bin/jarfind.jar net/sourceforge/hunterj/jarfind/*JarFind* -C ./build net

### Usage

For convenience, I like to invoke *JarFind* with a simple shell script like this:

    #!/usr/bin/env bash

    JARFIND_HOME=$(dirname $(readlink -f $0))
    JARFIND_JAR="jarfind.jar"

    if [ "$#" -eq 3 ]; then
        java $1 -jar $JARFIND_HOME/$JARFIND_JAR $2 $3
    else
        java -jar $JARFIND_HOME/$JARFIND_JAR $1 $2
    fi
    exit 0

So if you save this shell script in the same directory as jarfind.jar just created (say, ~/bin/jarfind.sh), you have a handy way of invoking *JarFind*. Better still, you can create a symbolic link to the script - something like:

    $ sudo ln -s ~/bin/jarfind.sh /usr/local/bin/jarfind

Then simply invoke the link:

    $ jarfind
    USAGE: java [-DjarFind.jarEntryMatcher=my.custom.Matcher] net.sourceforge.hunterj.jarfind.JarFind <starting_dir>  <class_name_exp>
    $ jarfind ~/workspace TestCase

    Looking for TestCase.class under /home/jim/workspace...

    /home/jim/workspace/JavaTest/lib/junit-4.12.jar
    	junit/framework/TestCase.class
