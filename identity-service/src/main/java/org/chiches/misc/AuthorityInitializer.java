package org.chiches.misc;

import org.chiches.entity.AuthorityEntity;
import org.chiches.entity.UserEntity;
import org.chiches.repository.AuthorityRepository;
import org.chiches.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AuthorityInitializer implements CommandLineRunner {
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final String[] authorities = {
            "user::read",
            "user::write",
            "user::delete",
            "staff::read",
            "staff::write",
            "staff::delete",
            "admin::read",
            "admin::write",
            "admin::delete"
    };
    public AuthorityInitializer(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (String authority : authorities) {
            if (!authorityRepository.existsByAuthority(authority)) {
                AuthorityEntity authorityEntity = new AuthorityEntity();
                authorityEntity.setAuthority(authority);
                authorityRepository.save(authorityEntity);
            }
        }



    }
}
