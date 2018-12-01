package org.firstinspires.ftc.teamcode.systems.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides a String Formatter similar to that of the C# string formatting
 */
public class StringFormatter
{
    private static final String VariableRegex = "\\{\\d+\\}";
    private static final Pattern VariablePattern = Pattern.compile(VariableRegex);
    public static final int MinimumMatchLength = 3;

    /**
     * Replaces the arguments in the string and returns the string formatted
     * @param data raw string to be formatted
     * @param args the arguments to be placed in the formatted string
     * @return Returns the formatted string
     */
    public static String format(String data, Object... args) {
        List<String> matches = getAllMatches(data);
        for (String match : matches) {
            int index = getIndex(match);
            ensureArgumentsExists(index, args);
            Object arg = args[index];
            data = data.replace(match, arg.toString());
        }
        return data;
    }

    /**
     * Gets all the matches of arguments in the raw string
     * @param data raw string to be formatted
     * @return Returns a List of String containing the matches of the raw string arguments
     */
    private static List<String> getAllMatches(String data) {
        List<String> allMatches = new ArrayList<String>();
        Matcher variableMatcher = VariablePattern.matcher(data);
        while (variableMatcher.find()) {
            allMatches.add(variableMatcher.group());
        }
        return allMatches;
    }

    /**
     * Gets the index of a match
     * @param match the match in the regular expression
     * @return Returns the index of the match
     */
    private static int getIndex(String match) {
        if (isValidMatch(match))
            return parseIndex(match);
        else
            throw new IllegalArgumentException("Invalid match");
    }

    /**
     * Checks if the match is a valid match
     * @param match The match of the argument variable pattern
     * @return Returns true if the match is valid
     */
    private static boolean isValidMatch(String match) {
        return match != null && match.length() >= MinimumMatchLength;
    }

    /**
     * Parses the index of the argument variable referenced in the match
     * @param match The match of the argument variable pattern
     * @return Returns the index of the argument variable
     */
    private static int parseIndex(String match) {
        try {
            return Integer.parseInt(
                    match.replace("{", "")
                            .replace("}", "")
            );
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Ensures that the argument exists in the string formatter
     * @param index the index of the argument variable
     * @param args the arguments to be placed into the formatted string
     */
    private static void ensureArgumentsExists(int index, Object... args) {
        if (index >= args.length) {
            throw new IllegalArgumentException("Argument at index: " + index + " does not exist");
        }
    }
}
