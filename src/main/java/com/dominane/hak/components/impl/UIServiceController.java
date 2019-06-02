package com.dominane.hak.components.impl;

import com.dominane.hak.data.DownloadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UIServiceController {

    @Autowired
    DownloadServiceController downloadServiceController;

    @GetMapping("/")
    public String downloadForm(Model model) {
        model.addAttribute("downloadConfig", new DownloadConfig());
        return "form";
    }

    @PostMapping("/")
    public String downloadSubmit(@ModelAttribute DownloadConfig downloadConfig) {
        new Thread(() -> downloadServiceController.download(downloadConfig)).start();
        return "submit";
    }

}
