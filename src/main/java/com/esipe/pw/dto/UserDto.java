package com.esipe.pw.dto;

import com.esipe.pw.model.Editor;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @NotBlank
    private String nickname;
    @Email
    private String mail;

    public Editor toEntity() {
        return Editor.builder()
                .mail(mail)
                .nickname(nickname)
                .build();
    }
}
