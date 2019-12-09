package com.esipe.pw.controller;


import com.esipe.pw.dto.PageData;
import com.esipe.pw.model.Document;
import com.esipe.pw.service.TweetService;
import com.esipe.pw.utils.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Rest controller pour l'url /api/v1/users
 * Cette interface utilise le même service que le TweetController mais l'expose
 * sous une autre URL
 */
@Slf4j
@RestController
@RequestMapping(UserController.PATH)
public class UserController {

    public static final String PATH = "/api/v1/users";

    @Autowired
    TweetService tweetService;

    @GetMapping(path = "/{nickname}/tweets")
    @ResponseBody
    public ResponseEntity<PageData<URI>> getUserTweets(@PathVariable(name = "nickname") String nickname,
                                                       @PageableDefault(page = 0, size = 20) Pageable pageable,
                                                       UriComponentsBuilder uriComponentsBuilder) {

        Page<Document> results = tweetService.getTweetsByNickname(nickname, pageable);
        Page<URI> uriPage = results.map(tweet -> {
            log.debug("my tweet id: {}", tweet.getId());
            UriComponentsBuilder localBuilder = uriComponentsBuilder.cloneBuilder();
            return localBuilder.path(TweetController.PATH.concat("/{id}"))
                    .buildAndExpand(tweet.getId()).toUri();
        });

        PageData<URI> resultPage = PageData.fromPage(uriPage);
        if (RestUtils.hasNext(uriPage, pageable)) {
            resultPage.setNext(RestUtils.buildNextUri(uriComponentsBuilder, pageable));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resultPage);
    }
}

