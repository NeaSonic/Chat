package com.kekw.chatapi.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class RegistrationRequest {
    private final String email;
    private final String password;
    private final String username;
}
