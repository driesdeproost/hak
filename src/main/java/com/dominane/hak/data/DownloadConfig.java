package com.dominane.hak.data;

import java.util.ArrayList;
import java.util.Arrays;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DownloadConfig {

    private String url;
    private String artist;
    private String album;
    private String trackInfoString;

    public ArrayList<ArrayList<String>> getTrackInfo() {
        ArrayList<String> trackInfos = new ArrayList<>(Arrays.asList(getTrackInfoString().split(", ")));
        log.info(trackInfos.toString());
        ArrayList<ArrayList<String>> trackInfosDivided = new ArrayList<>();
        for (int i = 0; i < trackInfos.size(); i++) {
            String pair = trackInfos.get(i);
            trackInfosDivided.add(new ArrayList<>(Arrays.asList(pair.split(" - "))));
        }
        return trackInfosDivided;
    }

    public String toString() {
        return "Download for: \n" +
                "Url: " + getUrl() + "\n" +
                "Artist: " + getArtist() + "\n" +
                "Album name: " + getAlbum() + "\n" +
                "Tracks: " + getTrackInfoString();
    }

}
