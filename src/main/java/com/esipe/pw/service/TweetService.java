package com.esipe.pw.service;

import com.esipe.pw.exception.NotFoundException;
import com.esipe.pw.model.Document;
import com.esipe.pw.repository.CustomTweetRepository;
import com.esipe.pw.repository.TweetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;

@Slf4j
@Service
/**
 * Cette classe contient la logique métier à appliquer à nos tweets
 */
public class TweetService {

    private QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private CustomTweetRepository customTweetRepository;


    public Page<Document> getTweets(String stringQuery, Pageable pageable) {
        Criteria criteria = convertQuery(stringQuery);
        Page<Document> results = customTweetRepository.findTweets(criteria, pageable);
        return results;
    }

    public Document getTweet(String id) {
        Document document = tweetRepository.findById(id).orElseThrow(()-> NotFoundException.DEFAULT);
        return document;
    }

    public Document createTweet(Document document){
        log.debug("tweedto {}", document.toString());
        Document insertedDocument = tweetRepository.insert(document);
        return insertedDocument;
    }

    /**
     * Mise à jour d'un tweet
     * @param updateDocument
     * @return
     */
    public Document updateTweet(Document updateDocument) {
        // on cherche d'abord le document si il est présent
        Document document = tweetRepository.findById(updateDocument.getId()).orElseThrow(() -> NotFoundException.DEFAULT);
        // on controle les champs que l'on veut mettre à jour
        document.setSource(updateDocument.getSource());
        document.setText(updateDocument.getText());
        document.setEditor(updateDocument.getEditor());
        // on le sauvegarde
        return tweetRepository.save(document);
    }

    public Page<Document> getTweetsByNickname(String nickname, Pageable pageable) {
        return tweetRepository.findByName(nickname, pageable);
    }

    /**
     * Convertit une requête RSQL en un objet Criteria compréhensible par la base
     * @param stringQuery
     * @return
     */
    private Criteria convertQuery(String stringQuery){
        Criteria criteria;
        if(!StringUtils.isEmpty(stringQuery)) {
            Condition<GeneralQueryBuilder> condition = pipeline.apply(stringQuery, Document.class);
            criteria = condition.query(new MongoVisitor());
        } else {
            criteria = new Criteria();
        }
        return criteria;
    }
}
