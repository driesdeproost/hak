package com.dominane.hak.data.trackinfo.parsers;

import com.dominane.hak.data.trackinfo.TrackInfo;
import java.util.ArrayList;
import java.util.List;

abstract class StartTitleTrackInfoParser extends TrackInfoParser{

    String delimiter;
    boolean startFirst;

    ArrayList<TrackInfo> recordToTrackInfo(List<String> trackInfoLines) {
        ArrayList<TrackInfo> ret = new ArrayList<>();

        String[] firstParsedLine = trackInfoLines.get(0).split(delimiter);
        TrackInfo first = new TrackInfo(getStart(firstParsedLine).trim(),null,getTitle(firstParsedLine).trim(),"1");
        ret.add(first);

        for (int i = 1; i < trackInfoLines.size(); i++) {
            String[] parsedLine = trackInfoLines.get(i).split(delimiter);
            String start = getStart(parsedLine).trim();
            String title = getTitle(parsedLine).trim();
            TrackInfo toAdd = new TrackInfo(start,null,title,String.valueOf(i+1));
            ret.add(toAdd);
            ret.get(i-1).setEnd(start);
        }

        ret.get(ret.size()-1).setEnd(TIME_FAR_WAY);

        return ret;
    }

    private String getStart(String[] pair) {
        return startFirst? pair[0] : pair[1];
    }

    private String getTitle(String[] pair) {
        return startFirst? pair[1] : pair[0];
    }
}
