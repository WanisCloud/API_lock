package com.esipe.pw.model;

import com.esipe.pw.dto.Source;
import com.esipe.pw.dto.TweetDto;
import com.esipe.pw.utils.RestUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


//à coordonner avec DocumentSummary; e
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@org.springframework.data.mongodb.core.mapping.Document
public class Document {

    @Id
    private String id;
    @CreatedDate
    private LocalDateTime created;
    @LastModifiedDate
    private LocalDateTime updated;
    @NotNull
    private Source source;
    @NotNull
    private String editor;
    // private User user;
    @NotNull
    private String creator;
    @NotBlank(message = "text must not be blank")
    private String body;
    // private String text;
    @Transient
    private String etag;
    /**
     * Constructeur utilisé par Spring data
     * On doit le définir car l'attribut etag ne fait pas partie du modèle
     * @param id
     * @param body
     * @param editor
     * @param source
     * @param created
     * @param updated
     * @param creator
     */
    @PersistenceConstructor
    public Document(String id, String body, String editor, String creator,
                    Source source, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.body = body;
        this.source = source;
        this.editor = editor;
        this.creator = creator;
        this.created = created;
        this.updated = updated;
    }

    public TweetDto toDto() {
        return TweetDto.builder()
                .id(id)
                .body(body)
                .created(RestUtils.convertToZoneDateTime(created))
                .modified(RestUtils.convertToZoneDateTime(updated))
                .editor(editor.toDto())
                .creator(creator.toDto())
                .source(source)
                .build();
    }
}
