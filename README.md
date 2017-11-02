## JarFind
*JarFind* is a Java development tool. It's purpose is to quickly find jar files which contain a particular Java .class file. Sooner or later as a Java developer you get stymied by the ClassNotFoundException, which is usually thrown when the jvm cannot load a particular class which has been referenced within some running thread. So, if you don't know which jar file contains that particular class, and where that jar file is located, then you have a quest ahead of you.

*JarFind* will find the jar for you. You just have to point it in the right direction. *JarFind* takes at least two arguments: a directory under which to search and a Java class name to look for - not unlike the Unix/Linux *find* command.

*JarFind* is completely unrelated to the *findjar.com* website. *JarFind* locates jar files accessible on your local file system only. I developed *JarFind* long before I came across *findjar.com*.

### Install
Ok, let's say you compile the source and create an executable jar named jarfind.jar and put that somewhere convenient (like /opt/java/jarfind.jar or ~/bin/jarfind.jar). So, assuming you have the compiled code under ./build, you could do something like:

    $ jar -cvfe ~/bin/jarfind.jar com/hunterjdev/jarfind/JarFind -C ./build com

This command should create an executable ~/bin/jarfind.jar file. Alternatively, you can just run the ant build script (build.xml) included in this repository.

For convenience, I like to invoke *JarFind* with a simple shell script like this:

```bash
#!/usr/bin/env bash

JAR_HOME=$(dirname $(readlink -f $0))
JAR_NAME="jarfind.jar"
MATCHER=
SEARCH_ARGS=
EXEC_CMD=

while [ "$1" != "" ]; do
    if [[ $1 =~ ^-D ]]; then
        MATCHER=$1
    elif [[ $1 =~ ^-exec ]]; then
        EXEC_CMD=`echo $1 | sed "s/^-exec=\(.*\)$/-exec='\1'/"`
    else
        SEARCH_ARGS="$SEARCH_ARGS $1"
    fi
    shift
done

eval java $MATCHER -jar $JAR_HOME/$JAR_NAME $SEARCH_ARGS $EXEC_CMD
exit 0
```
So if you save this shell script in the same directory as jarfind.jar - say, ~/bin/jarfind.sh - you have a handy way of invoking *JarFind*. Better still, you can create a symbolic link to the script - something like:

    $ sudo ln -s ~/bin/jarfind.sh /usr/local/bin/jarfind

Then simply invoke the link.

### Usage

    $ jarfind
    USAGE: java [-DjarFind.jarEntryMatcher=my.custom.Matcher] com.hunterjdev.jarfind.JarFind <starting_dir>  <class_name_exp> [-exec='<system_command>']

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

The <em>-exec</em> option is a useful feature. It works much like the Unix/Linux find command option of the same name. It enables a user supplied command to be executed with each search hit. So to copy the located jar files to some new directory, just do something like (note: the command portion needs to be quoted):

    $ jarfind ~/workspace TestCase -exec='cp {} ~/workspace/NewLib'

You can also customize the search by implementing your own *com.hunterjdev.jarfind.JarEntryMatcher*. You then provide the fully qualified Java class name as a system property using the "-D" switch assigned to the "jarFind.jarEntryMatcher" property:


    $ jarfind -DjarFind.jarEntryMatcher=my.brilliant.custom.Matcher ~/workspace TestCase

You just need to make sure that your custom *JarEntryMatcher* class is on your Java classpath.

<div style="font-size: 0.85em;text-align:center;">Copyright &copy; 2017 James P Hunter</div>
