package com.dominane.hak.components.impl;

import com.dominane.hak.data.DownloadConfig;
import com.dominane.hak.data.trackinfo.TrackInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/albumdownload/")
@Api(description = "Endpoint to request a download and split of the full album on yt")
public class DownloadServiceController {
    private static final String TIME_FAR_AWAY = "99:59:59";

    @RequestMapping(method = RequestMethod.POST, value="download")
    @ApiOperation("Download video in parts")
    public void download(@RequestBody DownloadConfig downloadConfig)  {
        log.info(downloadConfig.toString());
        try {
            for (ArrayList<String> command: downloadExecutionList(downloadConfig)){
                log.info(command.toString());
                new ProcessBuilder(command).start().waitFor();
            }
        } catch (IOException e) {
            log.error("error starting processBuilder",e);
        } catch (InterruptedException e) {
            log.error("interrupted while running processes",e);
        }
    }

    private ArrayList<ArrayList<String>> downloadExecutionList(DownloadConfig downloadConfig) {
        String url = downloadConfig.getUrl();
        String id = downloadConfig.getId();
        String folderName = escapeSpecialChars(downloadConfig.getArtist() + " - " + downloadConfig.getAlbum());

        ArrayList<ArrayList<String>> toExecute = new ArrayList<>();
        toExecute.add(makeBashCommand(youtubeDLDownloadCommand(url,id)));
        toExecute.add(makeBashCommand("mkdir -p " + folderName));
        toExecute.addAll(splitCommandsList(downloadConfig,folderName));
        toExecute.add(makeBashCommand("rm " + id));
        toExecute.add(makeBashCommand("mv -n '" + folderName + "' /tmp/mounted/"));
        toExecute.add(makeBashCommand("echo \"succesfully downloaded: " + downloadConfig.getAlbum() + "\" >> log"));
        return toExecute;
    }

    private ArrayList<ArrayList<String>> splitCommandsList(DownloadConfig config, String outputFolderName) {
        ArrayList<ArrayList<String>> splitCommands = new ArrayList<>();

        ArrayList<TrackInfo> trackInfoList = config.getTrackInfos();

        for (TrackInfo trackInfo : trackInfoList) {
            splitCommands.add(makeBashCommand(ffmpegSplitCommand(
                    trackInfo.getStart(),
                    trackInfo.getEnd(),
                    config.getId(),
                    outputFolderName,
                    config.getArtist(),
                    config.getAlbum(),
                    trackInfo.getTitle(),
                    trackInfo.getTrackNumber()
            )));
        }

        return splitCommands;
    }

    private String escapeSpecialChars(String in) {
        //TODO improvement: write regex or use library
        return in
                .replace(" ", "\\ ")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("&","\\&");
    }

    private ArrayList<String> makeBashCommand(String command) {
        return new ArrayList<>(Arrays.asList(
                "bash",
                "-c",
                "cd /tmp/ && " + command));
    }

    private String youtubeDLDownloadCommand(String url, String output) {
        return "youtube-dl "
                + "-o " + output + ".mp3 "
                + "-x --audio-format mp3 " + url + " "
                + ">> log";
    }

    private String ffmpegSplitCommand(String from, String to, String input, String outputFolder, String artist, String album, String title, String trackNumber) {
        String output = Paths.get(StringUtils.uriDecode(outputFolder, Charset.defaultCharset()), escapeSpecialChars(title)).toString();
        return "ffmpeg "
                + "-i " + input + ".mp3 "
                + "-metadata artist=\"" + artist + "\" "
                + "-metadata album=\"" + album + "\" "
                + "-metadata title=\"" + title + "\" "
                + "-metadata track=\"" + trackNumber + "\" "
                + "-ss " + from + " "
                + "-to " + to + " "
                + output + ".mp3";
    }

}