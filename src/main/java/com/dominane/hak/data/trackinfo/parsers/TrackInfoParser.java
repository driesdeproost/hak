package com.dominane.hak.data.trackinfo.parsers;

import com.dominane.hak.data.trackinfo.TrackInfo;
import java.util.ArrayList;

public abstract class TrackInfoParser {

    static String TIME_FAR_WAY = "99:59:59";

    public abstract ArrayList<TrackInfo> parse(String trackInfoString);



}
