package com.telusko.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageGenController {

    private ChatClient chatClient;

    private OpenAiImageModel openAiImageModel;

    public ImageGenController(OpenAiImageModel openAiImageModel,ChatClient.Builder builder){
        this.openAiImageModel=openAiImageModel;
        this.chatClient=builder.build();
    }

    @GetMapping("/api/image/{query}")
    public String genImage(@PathVariable String query){
        ImagePrompt prompt=new ImagePrompt(query, OpenAiImageOptions.builder()
                .quality("hd")
                .height(1024)
                .width(1024)
                .style("natural")
                .build());

        ImageResponse resposne = openAiImageModel.call(prompt);

        return resposne.getResult().getOutput().getUrl();
    }

    @PostMapping("/api/desc-img")
    public String descImage(@RequestParam String query, @RequestParam MultipartFile file) {

        System.out.println(file.getOriginalFilename());
        return chatClient.prompt()
                .user(us -> us.text(query)
                        .media(MimeTypeUtils.IMAGE_JPEG, file.getResource()))
                .call()
                .content();
    }
}
