package com.mentoai.mentoaiapi.meeting.presentation.rest.controller;

import com.mentoai.mentoaiapi.meeting.application.service.TranscricaoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transcricoes")
public class TranscricaoController {

    private final TranscricaoService transcricaoService;

    public TranscricaoController(TranscricaoService transcricaoService) {
        this.transcricaoService = transcricaoService;
    }
}
