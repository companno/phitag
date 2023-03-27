package de.garrafao.phitag.application.user.data;

import java.util.Set;

import org.apache.commons.lang3.Validate;
import lombok.Getter;

@Getter
public final class CreateUserCommand {

    private final String username;
    private final String email;
    private final String password;
    private final Set<String> languages;

    private final boolean privacypolicyAccepted;
    private final boolean ofLegalAge;

    public CreateUserCommand(final String username, final String email, final String password, final Set<String> languages, final boolean privacypolicyAccepted, final boolean ofLegalAge) {
        Validate.notBlank(username, "username must not be blank");
        Validate.notBlank(email, "email must not be blank");
        Validate.notBlank(password, "password must not be blank");
        Validate.notNull(languages, "languages must not be null");
        Validate.notEmpty(languages, "languages must not be empty");

        this.username = username;
        this.email = email;
        this.password = password;
        this.languages = languages;

        this.privacypolicyAccepted = privacypolicyAccepted;
        this.ofLegalAge = ofLegalAge;
    }
}
