package com.dominane.hak.data.trackinfo.parsers;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import com.dominane.hak.data.trackinfo.TrackInfo;
import java.util.ArrayList;
import org.junit.Test;

public class CrisbalStyleParserTest {

    private String toParse =
            "00:00 - First Track\n"
                    + "01:00 - Second Track\n"
                    + "02:00 - Third Track";

    private String reversedStyle =
            "First Track - 00:00\n"
                    + "Second Track - 01:00\n"
                    + "Third Track - 02:00";

    @Test
    public void parseTest() {
        CrisbalStyleParser parser = new CrisbalStyleParser();
        ArrayList<TrackInfo> actual = parser.parse(toParse);
        assertThat("",actual.get(0).getStart(), is("00:00"));
        assertThat("",actual.get(1).getEnd(), is("02:00"));
        assertThat("",actual.get(2).getTitle(), is("Third Track"));
    }

    @Test
    public void parseTestReverse() {
        CrisbalStyleParser parser = new CrisbalStyleParser(" - ", false);
        ArrayList<TrackInfo> actual = parser.parse(reversedStyle);
        assertThat("",actual.get(0).getStart(), is("00:00"));
        assertThat("",actual.get(1).getEnd(), is("02:00"));
        assertThat("",actual.get(2).getTitle(), is("Third Track"));
    }



}