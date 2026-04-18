package com.core_aurora_performance.service;

import com.core_aurora_performance.model.Struttura;
import com.core_aurora_performance.model.StrutturaStaff;
import com.core_aurora_performance.model.User;
import com.core_aurora_performance.repository.StrutturaRepository;
import com.core_aurora_performance.repository.StrutturaStaffRepository;
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
public class StrutturaCoreService {

    private final StrutturaRepository strutturaRepository;
    private final StrutturaStaffRepository strutturaStaffRepository;
    private final UserRepository userRepository;

    public List<Struttura> findByCodiceIstat(String codiceIstat) {
        return strutturaRepository.findByCodiceIstatWithStaff(codiceIstat.trim());
    }

    public Optional<Struttura> findById(Integer id) {
        return strutturaRepository.findById(id);
    }

    public List<Struttura> findAllByResponsabile(Long responsabileId) {
        return strutturaRepository.findAllByIdResponsabile(responsabileId.intValue());
    }

    @Transactional
    public Struttura save(Struttura struttura) {
        return strutturaRepository.save(struttura);
    }

    @Transactional
    public void delete(Integer id) {
        strutturaRepository.deleteById(id);
    }

    @Transactional
    public StrutturaStaff addStaff(Integer strutturaId, Long userId, String ruoloStruttura, Integer ordine) {
        StrutturaStaff staff = StrutturaStaff.builder()
                .idStruttura(strutturaId)
                .idUser(Math.toIntExact(userId))
                .ruoloStruttura(ruoloStruttura)
                .ordine(ordine)
                .build();
        return strutturaStaffRepository.save(staff);
    }

    @Transactional
    public void removeStaff(Integer strutturaId, Long userId) {
        strutturaStaffRepository.deleteByIdStrutturaAndIdUser(strutturaId, Math.toIntExact(userId));
    }

    public List<User> findUtentiByStrutturaId(Integer strutturaId) {
        List<StrutturaStaff> staff = strutturaStaffRepository.findByIdStruttura(strutturaId);
        return staff.stream()
                .map(s -> userRepository.findById(Long.valueOf(s.getIdUser())).orElse(null))
                .filter(u -> u != null)
                .toList();
    }

    public boolean existsStaff(Integer strutturaId, Long userId) {
        return strutturaStaffRepository.existsByIdStrutturaAndIdUser(strutturaId, Math.toIntExact(userId));
    }

    public boolean hasChildren(Integer strutturaId) {
        return !strutturaRepository.findByIdParent(strutturaId).isEmpty();
    }

    public List<Struttura> findByUserId(Integer userId) {
        List<StrutturaStaff> staffEntries = strutturaStaffRepository.findByIdUser(userId);
        return staffEntries.stream()
                .map(s -> strutturaRepository.findById(s.getIdStruttura()).orElse(null))
                .filter(s -> s != null)
                .toList();
    }
}
