package com.kekw.chatapi.registration.token;

import com.kekw.chatapi.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime localDateTime;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Column(nullable = true)
    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(nullable = false,name="app_user_id")
    private User user;

    public ConfirmationToken(String token, LocalDateTime localDateTime, LocalDateTime expiredAt, User user) {
        this.token = token;
        this.localDateTime = localDateTime;
        this.expiresAt = expiredAt;
        this.user = user;
    }
}
