package org.upacreekrobotics.dashboard;

/**
 * Class filter that describes the behavior of {@link ClasspathScanner} objects.
 */
public interface ClassFilter {
    /**
     * Returns whether the given class should be loaded for processing. Preliminary filtering in
     * this step can greatly improve scanning time.
     * @param className class name
     * @return true if the class should be processed
     */
    boolean shouldProcessClass(String className);

    /**
     * Processes the class (for example, checks the presence of an annotation).
     * @param klass class
     */
    void processClass(Class klass);
}

