## JarFind
*JarFind* is a Java development tool. It's purpose is to quickly find jar files which contain a particular Java .class file. Sooner or later as a Java developer you get stymied by the ClassNotFoundException, which is usually thrown when the jvm cannot load a particular class which has been referenced within some running thread. So, if you don't know which jar file contains that particular class, and where that jar file is located, then you have a quest ahead of you.

*JarFind* will find the jar for you. You just have to point it in the right direction. *JarFind* takes at least two arguments: a directory under which to search and a Java class name to look for - not unlike the Unix/Linux *find* command.

*JarFind* is completely unrelated to the *findjar.com* website. *JarFind* locates jar files accessible on your local file system only. I developed *JarFind* long before I came across *findjar.com*.

### Install
The rest of these instructions are geared for Linux/Unix based systems. If you're working on Windows, you're on your own.  Ok, let's say you want to compile the source and create an executable jar named jarfind.jar and put that somewhere convenient (like /opt/java/jarfind.jar or ~/bin/jarfind.jar). So, assuming you have the compiled code under ./build, you could do something like:

    $ jar -cvfe ~/bin/jarfind.jar net/sourceforge/hunterj/jarfind/JarFind -C ./build net

This command should create an executable ~/bin/jarfind.jar file. Alternatively, you can just run the ant build script (build.xml) included in this repository.

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

So if you save this shell script in the same directory as jarfind.jar - say, ~/bin/jarfind.sh - you have a handy way of invoking *JarFind*. Better still, you can create a symbolic link to the script - something like:

    $ sudo ln -s ~/bin/jarfind.sh /usr/local/bin/jarfind

Then simply invoke the link.

### Usage

    $ jarfind
    USAGE: java [-DjarFind.jarEntryMatcher=my.custom.Matcher] net.sourceforge.hunterj.jarfind.JarFind <starting_dir>  <class_name_exp>
    $ jarfind ~/workspace TestCase

    Looking for TestCase.class under /home/jim/workspace...

    /home/jim/workspace/JavaTest/lib/junit-4.12.jar
    	junit/framework/TestCase.class

*JarFind* will display the located jar file(s) as well as the .class file found within that jar.

Sometimes you get multiple results, in which case you will need to manually select the right jar.

    $ jarfind ~/workspace Document

    Looking for Document.class under /home/jim/workspace...

    /home/jim/workspace/lib/lucened.jar
    	org/apache/lucene/document/Document.class
    /home/jim/workspace/lib/Tidy.jar
    	org/w3c/dom/Document.class
    /home/jim/workspace/lib/jdom.jar
    	org/jdom/Document.class


You can also customize the search by implementing your own *net.sourceforge.hunterj.jarfind.JarEntryMatcher*. You then provide the fully qualified Java class name of your custom matcher as a jvm system property using the standard "-D" option of the *java* command. The system property *JarFind* looks for is *"jarFind.jarEntryMatcher"*. So, for example if you have created an executable jar file, your command would look something like:

    $ java -DjarFind.jarEntryMatcher=my.custom.Matcher -jar ~/bin/jarfind.jar ~/workspace TestCase

If you use the script file above, and create a link to it, then your command would like something like:

    $ jarfind -DjarFind.jarEntryMatcher=my.brilliant.custom.Matcher ~/workspace TestCase

You just need to make sure that your custom *matcher* class is on your Java classpath.
