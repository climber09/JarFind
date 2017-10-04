package net.sourceforge.hunterj.jarfind;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Searches for jar files which contain a specified Java class. For example:
 * <blockquote><pre>
 * java net.sourceforge.hunterj.jarfind.JarFind D:/repository Logger
 * </pre></blockquote>
 * looks for jar files containing a Logger.class file, under the D:\repository directory.
 * It would output the matched jar files and jar file entries - something like:
 * <blockquote><pre>
 * D:\repository\log4j\log4j\1.2.16\log4j-1.2.16.jar
        org/apache/log4j/Logger.class
 * </pre></blockquote>
 * 
 * The option exists for the user to customize the search criterion by implementing 
 * a {@link hunterjp.test.jdk7.newFeatures.JarEntryMatcher}, and supplying 
 * the fully qualified class name as a system property at the command line. 
 * Using the '-D' option the custom {@link hunterjp.test.jdk7.newFeatures.JarEntryMatcher} 
 * is assigned to the '<code>jarFind.jarEntryMatcher</code>' property. For example:
 * <blockquote><pre>
 * java -DjarFind.jarEntryMatcher=my.custom.Matcher ...
 * </pre></blockquote>
 * 
 * @author hunterjp
 *
 */
public class JarFind {
    
    public static final String JAR_ENTRY_MATCHER = "jarFind.jarEntryMatcher";
    
    public static java.util.List<Path> find(String startPath, String className) 
            throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{

        String customMatcherClass = System.getProperty(JAR_ENTRY_MATCHER);
        JarEntryMatcher entryMatcher;

        if ( customMatcherClass != null) {
            entryMatcher = (JarEntryMatcher)Class.forName(customMatcherClass).newInstance();
            entryMatcher.setSearchTarget(className);
        }
        else {
            entryMatcher = new DefaultJarEntryMatcher(className);
        }
        JarInspector jarInspector = new JarInspector(entryMatcher);
        Files.walkFileTree(Paths.get(startPath), jarInspector);
        return jarInspector.getMatchedJars();
    }
    
    private static final String USAGE = 
        "\nUSAGE: java [-DjarFind.jarEntryMatcher=my.custom.Matcher] " 
        		+ JarFind.class.getCanonicalName() 
        		+ " <starting_dir>  <class_name_exp>";

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        if (args.length < 2 || args.length > 3) {
            System.out.println(USAGE);
            System.exit(1);
        }
        
        String path = args[args.length-2];
        String className = args[args.length-1];        
        System.out.printf("\nLooking for %s.class under %s...\n\n", className, path);
        
        try {
            find(path, className);
        } 
        catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}

