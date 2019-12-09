package com.esipe.pw.repository;

import com.esipe.pw.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.List;

/**
 * Custom Repository pour générer nos requêtes métiers plus évoluées que le simple CRUD
 */
public class CustomTweetRepositoryImpl implements CustomTweetRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Page<Document> findTweets(Criteria criteria, Pageable pageable) {
        Query query = new Query().addCriteria(criteria).with(pageable);
        List<Document> results = mongoTemplate.find(query, Document.class);
        Page<Document> tweetPage = PageableExecutionUtils.getPage(
                results,
                pageable,
                () -> mongoTemplate.count(query, Document.class));
        return tweetPage;
    }
}
