package com.hunterjdev.jarfind;


import java.util.jar.JarEntry;

/**
 * Defines the matching criteria for individual jar file entries. Allows a 
 * user defined matching strategy to be used by supplying the fully 
 * qualified Java class name as a command line argument with the -D option
 * (e.g., java -DjarFind.jarEntryMatcher=my.custom.Matcher ...).
 * 
 * @author hunterjp
 *
 */
public interface JarEntryMatcher {
    
    public boolean match(JarEntry entry);
    
    public void setSearchTarget(String target);

}
