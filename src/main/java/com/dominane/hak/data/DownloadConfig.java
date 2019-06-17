package com.dominane.hak.data;

import com.dominane.hak.data.trackinfo.TrackInfo;
import com.dominane.hak.data.trackinfo.parsers.CrisbalStyleParser;
import com.dominane.hak.data.trackinfo.parsers.TrackInfoParser;
import java.util.ArrayList;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DownloadConfig {

    private static final int ID_LENGTH = 11;

    private String url;
    private String artist;
    private String album;
    private String trackInfoString;
    private boolean startFirst;

    //TODO make list of parsers to pick from
    private static final CrisbalStyleParser defaultParser = new CrisbalStyleParser(" - ", false);
    private static final CrisbalStyleParser reverseDefaultParser = new CrisbalStyleParser(" - ", true);

    public ArrayList<TrackInfo> getTrackInfos() {
        return getParser().parse(trackInfoString);
    }

    private TrackInfoParser getParser(){
        return isStartFirst()? reverseDefaultParser : defaultParser;
    }

    public String getId(){
        return url.substring(url.length()-ID_LENGTH);
    }

    public String toString() {
        return "Download for: \n" +
                "Url: " + getUrl() + "\n" +
                "Artist: " + getArtist() + "\n" +
                "Album name: " + getAlbum() + "\n" +
                "Tracks: " + getTrackInfoString() + "\n" +
                "StartFirst: " + isStartFirst();
    }

}
