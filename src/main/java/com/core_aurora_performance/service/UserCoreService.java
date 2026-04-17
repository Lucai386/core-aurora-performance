package com.core_aurora_performance.service;

import com.core_aurora_performance.model.User;
import com.core_aurora_performance.model.UserLog;
import com.core_aurora_performance.repository.UserLogRepository;
import com.core_aurora_performance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCoreService {

    private final UserRepository userRepository;
    private final UserLogRepository userLogRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId);
    }

    public List<User> findByCodiceIstat(String codiceIstat) {
        return userRepository.findByCodiceIstat(codiceIstat);
    }

    public List<User> findByCodiceIstatAndRuolo(String codiceIstat, String ruolo) {
        return userRepository.findByCodiceIstatAndRuolo(codiceIstat, ruolo);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public UserLog saveLog(UserLog log) {
        return userLogRepository.save(log);
    }
}
