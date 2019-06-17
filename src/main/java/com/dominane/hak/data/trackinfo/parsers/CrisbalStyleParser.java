package com.dominane.hak.data.trackinfo.parsers;

import com.dominane.hak.data.trackinfo.TrackInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrisbalStyleParser extends StartTitleTrackInfoParser {

    public CrisbalStyleParser() {
        this.delimiter = " - ";
        this.startFirst = true;
    }

    public CrisbalStyleParser(String delimiter, boolean startFirst) {
        this.delimiter = delimiter;
        this.startFirst = startFirst;
    }

    @Override
    public ArrayList<TrackInfo> parse(String trackInfoString) {
        List<String> trackInfoLines = Arrays.asList(trackInfoString.split("\r\n"));
        return recordToTrackInfo(trackInfoLines);
    }
}
