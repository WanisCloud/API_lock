package com.esipe.pw.repository;

import com.esipe.pw.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TweetRepository extends MongoRepository<Document, String> {
    @Query("{ 'editor.nickname' : ?0 }")
    /**
     * déclare une méthode avec la query, le reste est généré automatiquement par Spring Data
     */
    Page<Document> findByName(String nickname, Pageable pageable);

}
