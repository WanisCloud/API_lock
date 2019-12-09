package com.esipe.pw.repository;

import com.esipe.pw.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public interface CustomTweetRepository {

    Page<Document> findTweets(Criteria query, Pageable pageable);

}
