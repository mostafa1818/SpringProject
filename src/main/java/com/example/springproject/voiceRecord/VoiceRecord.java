package com.example.springproject.voiceRecord;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VoiceRecord {
    @GetMapping("voiceRecord")
    public String voiceRecord()
    {
        return "voicerecord/voice-record";
    }
}
