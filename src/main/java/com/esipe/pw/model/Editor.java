package com.esipe.pw.model;

import com.esipe.pw.dto.UserDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Editor {
    @NotBlank
    private String nickname;
    @Email
    private String mail;

    public UserDto toDto(){
        return UserDto.builder()
                .mail(mail)
                .nickname(nickname)
                .build();
    }
}