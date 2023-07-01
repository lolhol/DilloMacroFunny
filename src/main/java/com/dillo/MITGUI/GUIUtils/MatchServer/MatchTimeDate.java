package com.dillo.MITGUI.GUIUtils.MatchServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchTimeDate {
    public static String matchServer(String original) {
        Pattern pattern = Pattern.compile("\\d+/\\d+/\\d+ (.*)");
        Matcher matcher = pattern.matcher(original);
        return matcher.find() ? matcher.group(1) : null;
    }
}
