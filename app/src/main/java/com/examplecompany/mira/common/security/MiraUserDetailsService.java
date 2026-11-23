package com.examplecompany.mira.common.security;

import com.examplecompany.mira.common.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiraUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MiraUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByLoginId(username)
                .map(MiraUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));
    }
}