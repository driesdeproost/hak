package com.dominane.hak.components.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;

public class DownloadServiceControllerTest {

    private static Map<String, String> escapes = new HashMap<String, String>(){{
        put("abc","abc"); //don't escape
        put("'","\\'");
        put("\"","\\\"");
        put("|","\\|");
        put("&","\\&");
        put(" ","\\ ");
        put("(","\\(");
        put(")","\\)");
        put(";","\\;");
        put("<","\\<");
        put(">","\\>");
        put("$","\\$");
        put("`","\\`");
        put("\\","\\\\");
        put("\t","\\\t");
//        put("\n","\\\n"); // ok, no newlines then
        put("*","\\*");
        put("?","\\?");
        put("[","\\[");
        put("#","\\#");
        put("~","\\~");
        put("=","\\=");
        put("%","\\%");
    }};

    @Test
    public void escapeSpecialChars() {
        for (Entry<String,String> entry : escapes.entrySet()){
            assertThat(
                    "String with special character " + entry.getKey() + " not properly escaped",
                    DownloadServiceController.escapeSpecialChars(entry.getKey()),
                    is(entry.getValue())
            );
        }
    }
}