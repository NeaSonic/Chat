package com.kekw.chatapi.registration;

import com.kekw.chatapi.email.EmailService;
import com.kekw.chatapi.registration.token.ConfirmationToken;
import com.kekw.chatapi.user.User;
import com.kekw.chatapi.user.UserRole;
import com.kekw.chatapi.user.UserService;
import com.kekw.chatapi.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    public String register(RegistrationRequest request) {
        if (request.getUsername().length()<5 || request.getUsername().length()>25){
            throw new IllegalStateException("Username must be between 5 and 25 characters!");
        }
        String pass = request.getPassword();
        if (pass.length()<8 || pass.length()>30){
            throw new IllegalStateException("Password must be between 8 and 25 characters!");
        }
        boolean sp=false,num=false,upp=false,low=false;
        int count=0;
        for (int i = 0; i<pass.length(); i++){
            char c = pass.charAt(i);
            if (c==32)
                throw new IllegalStateException("Password can not contain any spaces");
            if (!sp) {
                if ((c>=33&&c<=47) || (c>=58&&c<=64) || (c>=91&&c<=96) || (c>=123&&c<=126)) {sp=true; count++;}
            }
            if (!num) {
                if (c>=48&&c<=57) {num=true; count++;}
            }
            if (!low) {
                if (c>=97&&c<=122) {low=true; count++;}
            }
            if (!upp) {
                if (c>=65&&c<=90) {upp=true; count++;}
            }
            if (count==4) break;
        }
        if (count < 4)
            throw new IllegalStateException("Password must contain at least one upper case character, one lower case, one special character and one number");
        if (userService.checkUser(request.getEmail()))
            throw new IllegalStateException("Email already taken");
        String token = UUID.randomUUID().toString();
        String link = "http://localhost:3000/registration/confirm/"+token;
        emailService.send(request.getEmail(), buildEmail(request.getUsername(),link));
        userService.signUpUser(new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                UserRole.USER,
                false,
                false
        ),token);
        return token;
    }
    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt()!=null)
            throw new IllegalStateException("Email already confirmed");

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            String newToken = UUID.randomUUID().toString();
            userService.createNewToken(confirmationToken.getUser(),newToken);
            String link = "http://localhost:3000/registration/confirm/"+newToken;
            emailService.send(confirmationToken.getUser().getEmail(), buildEmail(confirmationToken.getUser().getEmail(),link));
            throw new IllegalStateException("Your token has expire. We've send you a new confirmation email. Please follow the link in the email to confirm your account");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        userService.enableUser(confirmationToken.getUser());
        return "confirmed";
    }
    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
