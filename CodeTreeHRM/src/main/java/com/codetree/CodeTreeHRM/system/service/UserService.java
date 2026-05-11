package com.codetree.CodeTreeHRM.system.service;

import com.codetree.CodeTreeHRM.hr.dto.PageResponse;
import com.codetree.CodeTreeHRM.system.dto.UserDto;
import com.codetree.CodeTreeHRM.system.dto.UserSearchDto;
import com.codetree.CodeTreeHRM.system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper       userMapper;
    private final PasswordEncoder  passwordEncoder;

    /** 사용자 목록 (검색 + 페이징) */
    @Transactional(readOnly = true)
    public PageResponse<UserDto> searchUsers(UserSearchDto search) {
        List<UserDto> list  = userMapper.searchUsers(search);
        long          total = userMapper.countUsers(search);
        return PageResponse.of(list, total, search.getPage(), search.getSize());
    }

    /** 사용자 단건 조회 */
    @Transactional(readOnly = true)
    public UserDto findByUserId(String userId) {
        return userMapper.findByUserId(userId);
    }

    /**
     * 사용자 저장/수정 (Upsert)
     * - userId 미존재 → 신규 등록 (비밀번호 BCrypt 인코딩)
     * - userId 존재   → 정보 수정 (useYn, acctLockYn)
     */
    @Transactional
    public void saveUser(UserDto dto) {
        if (userMapper.countByUserId(dto.getUserId()) == 0) {
            // ── 신규 등록 ──
            validateForInsert(dto);
            dto.setUserPwd(passwordEncoder.encode(dto.getUserPwd()));
            userMapper.insertUser(dto);
        } else {
            // ── 정보 수정 ──
            validateForUpdate(dto);
            userMapper.updateUser(dto);
        }
    }

    /**
     * 비밀번호 변경/초기화
     * - 새 비밀번호를 BCrypt 인코딩 후 저장
     * - 실패 횟수 초기화 및 계정 잠금 해제도 함께 처리
     */
    @Transactional
    public void changePassword(String userId, String newPassword) {
        if (userMapper.countByUserId(userId) == 0) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + userId);
        }
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
        String encoded = passwordEncoder.encode(newPassword);
        userMapper.updatePassword(userId, encoded);
    }

    // ── 유효성 검사 ──────────────────────────────────────────

    private void validateForInsert(UserDto dto) {
        if (dto.getUserId() == null || dto.getUserId().isBlank()) {
            throw new IllegalArgumentException("사용자 아이디는 필수입니다.");
        }
        if (dto.getUserPwd() == null || dto.getUserPwd().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
        if (dto.getEmpNo() == null || dto.getEmpNo().isBlank()) {
            throw new IllegalArgumentException("사번은 필수입니다.");
        }
        if (userMapper.countEmpByEmpNo(dto.getEmpNo()) == 0) {
            throw new IllegalArgumentException("존재하지 않는 사번입니다: " + dto.getEmpNo());
        }
    }

    private void validateForUpdate(UserDto dto) {
        if (dto.getUseYn() == null || (!dto.getUseYn().equals("Y") && !dto.getUseYn().equals("N"))) {
            throw new IllegalArgumentException("사용여부 값은 'Y' 또는 'N' 이어야 합니다.");
        }
    }
}
