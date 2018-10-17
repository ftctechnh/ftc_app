package org.firstinspires.ftc.teamcode.systems.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatter
{
    private static final String VariableRegex = "\\{\\d+\\}";
    private static final Pattern VariablePattern = Pattern.compile(VariableRegex);
    public static final int MinimumMatchLength = 3;


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

    private static List<String> getAllMatches(String data) {
        List<String> allMatches = new ArrayList<String>();
        Matcher variableMatcher = VariablePattern.matcher(data);
        while (variableMatcher.find()) {
            allMatches.add(variableMatcher.group());
        }
        return allMatches;
    }

    private static int getIndex(String match) {
        if (isValidMatch(match))
            return parseIndex(match);
        else
            throw new IllegalArgumentException("Invalid match");
    }

    private static boolean isValidMatch(String match) {
        return match != null && match.length() >= MinimumMatchLength;
    }

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

    private static void ensureArgumentsExists(int index, Object... args) {
        if (index >= args.length) {
            throw new IllegalArgumentException("Argument at index: " + index + " does not exist");
        }
    }
}
