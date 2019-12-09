package com.esipe.pw.dto;

import com.esipe.pw.model.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TweetDto {

    public static final String ZONE_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private String id;
    @NotBlank(message = "text must not be blank")
    //@Size(max = 256, message = "tweet is max 256")
    private String body;
    private Source source;
    @NotNull
    private EditorDto editor;
    @NotNull
    private CreatorDto creator;
    @JsonFormat(pattern = ZONE_DATE_TIME_FORMAT)
    private ZonedDateTime created;
    @JsonFormat(pattern = ZONE_DATE_TIME_FORMAT)
    private ZonedDateTime modified;


    public Document toEntity() {
        return Document.builder()
                .id(id)
                .body(body)
                .source(source)
                .creator(creator.toEntity())
                .editor(editor.toEntity())
                .build();
    }
}

