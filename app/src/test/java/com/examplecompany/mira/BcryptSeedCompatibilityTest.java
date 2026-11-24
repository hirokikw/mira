package com.examplecompany.mira;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class BcryptSeedCompatibilityTest {

    @Test
    void adminPasswordMatchesPgcryptoHashFromSeed() {
        final String hash = "$2a$10$7vzaA27JMv8hIOEtTYjnG.HCtbokYdSrLfG33d.yxGc9mBCr/zBh6";
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("admin", hash));
    }
}