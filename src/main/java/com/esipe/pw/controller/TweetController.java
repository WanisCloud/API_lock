package com.esipe.pw.controller;


import com.esipe.pw.dto.PageData;
import com.esipe.pw.dto.TweetDto;
import com.esipe.pw.model.Document;
import com.esipe.pw.service.TweetService;
import com.esipe.pw.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * Rest controller pour l'url /api/v1/tweets
 */
@Slf4j
@Validated
@RestController
@EnableWebMvc
@RequiredArgsConstructor
@RequestMapping(TweetController.PATH)
@SuppressWarnings("unused")
public class TweetController {

    public static final String PATH = "/api/v1/tweets";

    @Autowired
    private TweetService tweetService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD}, path = "/{id}")
    @ResponseBody
    public ResponseEntity<TweetDto> getTweet(@PathVariable("id") String tweetId) {

        Document document = tweetService.getTweet(tweetId);

        TweetDto tweetDto = document.toDto();

        return ResponseEntity
                .status(HttpStatus.OK)
                .eTag(document.getEtag())
                .cacheControl(CacheControl.maxAge(10, TimeUnit.HOURS))
                .lastModified(tweetDto.getModified())
                .body(tweetDto);
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD})
    @ResponseBody
    public ResponseEntity<PageData<TweetDto>> getTweets(@RequestParam(required = false) String query,
                                                        @PageableDefault(page = 0, size = 20) Pageable pageable,
                                                        UriComponentsBuilder uriComponentsBuilder) {

        Page<Document> results = tweetService.getTweets(query, pageable);
        PageData<TweetDto> pageResult = PageData.fromPage(results.map(Document::toDto));
        if (RestUtils.hasNext(results, pageable)) {
            pageResult.setNext(RestUtils.buildNextUri(uriComponentsBuilder, pageable));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(CacheControl.noCache())
                .body(pageResult);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TweetDto>
    createTweet(@Valid @RequestBody TweetDto tweet, UriComponentsBuilder uriComponentsBuilder) {

        Document createdDocument = tweetService.createTweet(tweet.toEntity());
        UriComponents uriComponents = uriComponentsBuilder.path(TweetController.PATH.concat("/{id}"))
                .buildAndExpand(createdDocument.getId());

        TweetDto createdTweetDto = createdDocument.toDto();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .eTag(createdDocument.getEtag())
                .lastModified(createdTweetDto.getModified())
                .location(uriComponents.toUri())
                .body(createdTweetDto);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<TweetDto>
    updateTweet(@PathVariable("id") String tweetId, @Valid @RequestBody TweetDto tweetDto) {

        Document document = tweetDto.toEntity();
        document.setId(tweetId);
        Document updatedDocument = tweetService.updateTweet(document);

        TweetDto updatedTweetDto = updatedDocument.toDto();

        return ResponseEntity
                .status(HttpStatus.OK)
                .lastModified(updatedTweetDto.getModified())
                .eTag(updatedDocument.getEtag())
                .body(updatedTweetDto);
    }
}

