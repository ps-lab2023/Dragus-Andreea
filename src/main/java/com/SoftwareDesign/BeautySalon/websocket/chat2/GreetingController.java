package com.SoftwareDesign.BeautySalon.websocket.chat2;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class GreetingController {
    private List<String> announcements = new ArrayList<>();

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        announcements.add(HtmlUtils.htmlEscape(message.getName()));
        return new Greeting(HtmlUtils.htmlEscape(message.getName()));
    }

    @GetMapping("/announcements")
    public ResponseEntity<List<String>> getMessages() {
        return ResponseEntity.ok(this.announcements);
    }
}
