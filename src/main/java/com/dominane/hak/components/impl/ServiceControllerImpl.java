package com.dominane.hak.components.impl;

import com.dominane.hak.components.ServiceController;
import com.dominane.hak.data.DownloadConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/albumdownload/")
@Api(description = "Endpoint to request a download and split of the full album on yt")
public class ServiceControllerImpl implements ServiceController {
    private static final int ID_LENGTH = 11;
    private static final String TIME_FAR_AWAY = "99:59:59";

    @RequestMapping(method = RequestMethod.POST, value="download" /*, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}*/)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "emailInfo", value = "Description of the download attributes data", required = true,
                    paramType = "application:json", type = "string"),
    })
    @ApiOperation("Download video in parts")
    @Override public void download( @RequestBody DownloadConfig downloadConfig)  {
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
        String id = url.substring(url.length()-ID_LENGTH);
        String folderName = escapeSpaces(downloadConfig.getArtist() + " - " + downloadConfig.getAlbum());

        ArrayList<ArrayList<String>> toExecute = new ArrayList<>();
        toExecute.add(makeBashCommand(youtubeDLDownloadCommand(url,id)));
        toExecute.add(makeBashCommand("mkdir -p " + folderName));
        toExecute.addAll(splitCommandsList(downloadConfig.getTrackInfo(),id,folderName));
        toExecute.add(makeBashCommand("rm " + id));
        toExecute.add(makeBashCommand("mv -n " + folderName + " /tmp/mounted/"));
        toExecute.add(makeBashCommand("echo \"succesfully downloaded: " + downloadConfig.getAlbum() + "\" >> log"));
        return toExecute;
    }

    private String escapeSpaces(String in) {
        return in.replace(" ", "\\ ");
    }

    private ArrayList<String> makeBashCommand(String command) {
        return new ArrayList<>(Arrays.asList(
                "bash",
                "-c",
                "cd /tmp/ && " + command));
    }

    private ArrayList<ArrayList<String>> splitCommandsList(ArrayList<ArrayList<String>> trackInfo, String input, String outputFolderName) {
        ArrayList<ArrayList<String>> splitCommands = new ArrayList<>();

        int lastIndex = trackInfo.size()-1;
        for (int i = 0; i < lastIndex; i++) {
            splitCommands.add(makeBashCommand(ffmpegSplitCommand(
                    trackInfo.get(i).get(0),
                    trackInfo.get(i+1).get(0),
                    input,
                    Paths.get(outputFolderName, trackInfo.get(i).get(1)).toString())
                    )
            );
        }
        splitCommands.add(makeBashCommand(ffmpegSplitCommand(
                trackInfo.get(lastIndex).get(0),
                TIME_FAR_AWAY,
                input,
                Paths.get(outputFolderName, trackInfo.get(lastIndex).get(1)).toString())
                )
        );
        return splitCommands;
    }

    private String youtubeDLDownloadCommand(String url, String output) {
        return "youtube-dl "
                + "-o " + output + ".mp3 "
                + "-x --audio-format mp3 " + url + " "
                + ">> log";
    }

    private String ffmpegSplitCommand(String from, String to, String input, String output) {
        return "ffmpeg "
                + "-i " + input + ".mp3 "
                + "-ss " + from + " "
                + "-to " + to + " "
                + output + ".mp3";
    }



}