package com.hunterjdev.jarfind;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 
 * @author hunterjp
 *
 */
public class JarInspector extends SimpleFileVisitor<Path> {
    
    private final JarEntryMatcher entryMatcher;
    private List<Path> matchedJars;
    
    
    public JarInspector(JarEntryMatcher entryMatcher) {
        this.entryMatcher = entryMatcher;
        this.matchedJars = new ArrayList<>();
    }
    
    
    public List<Path> getMatchedJars() {
        return this.matchedJars;
    }

       
    protected List<JarEntry> checkForMatchedEntries(JarFile jar) {
        List<JarEntry> matches = new LinkedList<>();
        Enumeration<JarEntry> entries = jar.entries();
        JarEntry current = null;
        
        while (entries.hasMoreElements()) {
            current = entries.nextElement();
            if (entryMatcher.match(current)) {
                matches.add(current);
            }
        }
        return matches;
    }
    

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {

        if (attrs.isRegularFile()) {
            // String filePath = "";
            File pFile = path.toFile();
            if (!pFile.canRead()) {
                System.out.println(pFile.toString().concat(" is not readable"));
                return FileVisitResult.CONTINUE;
            }
            String filePathStr = pFile.getCanonicalPath();
            // Unfortunately this is the only(?) way to check for the file type
            if (filePathStr.endsWith(".jar")) {
                try (JarFile jar = new JarFile(filePathStr)) {
                    List<JarEntry> matches = this.checkForMatchedEntries(jar);
                    if (! matches.isEmpty()) {
                        matchedJars.add(path);
                        System.out.println(filePathStr);
                        for (JarEntry entry : matches) {
                            System.out.println("\t".concat(entry.getName()));
                        }
                    }
                }catch(IOException e) {
                	System.err.printf("Warning: File: '%s' Message: '%s'\n", filePathStr, e.getMessage() );
                }
            }
        }
        return FileVisitResult.CONTINUE;
    }
    

    /**
     * Overriding this method allows capturing of the read error.
     */
    @Override
    public FileVisitResult visitFileFailed(Path path, IOException e) {
        //log the IOException in real life
        e.printStackTrace(System.err);
        return FileVisitResult.CONTINUE;
    }

}