package net.sourceforge.hunterj.jarfind;

import java.util.jar.JarEntry;
import java.util.regex.Pattern;

/**
 * Defines the default jar entry matching criteria. A jar entry will
 * match if it simply ends with the search expression concatenated
 * with ".class". 
 * 
 * @author hunterjp
 *
 */
public class DefaultJarEntryMatcher implements JarEntryMatcher {
    private final Pattern searchPattern;
    StringBuilder searchTerm;
    
    public DefaultJarEntryMatcher(String className) {
        // clean up the user input to get the format '[java/some/package]/SomeClass'
        className = className.replaceFirst("\\.+class$", "")
                .replaceAll("\\.+|\\\\+|/{2,}+", "/");
        
        searchTerm = new StringBuilder();
        
        if (className.indexOf('/') == -1) {
            searchTerm.append('/');
        }
        searchTerm.append(className).append("\\.class$");
//        System.out.println("\n (REGEX => " + searchTerm + ")\n");
        searchPattern = Pattern.compile(searchTerm.toString());
    }

    @Override
    public boolean match(JarEntry entry) {
        return searchPattern.matcher(entry.getName()).find();
    }

    @Override
    public void setSearchTarget(String target) {
        // Do nothing here because the String-dependent Pattern is
        // declared final (which is preferable) and so cannot be 
        // altered at this point.
    }
    
}
