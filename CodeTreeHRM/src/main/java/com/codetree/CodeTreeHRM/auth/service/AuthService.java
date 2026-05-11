package com.codetree.CodeTreeHRM.auth.service;

import com.codetree.CodeTreeHRM.auth.dto.LoginRequest;
import com.codetree.CodeTreeHRM.auth.dto.LoginResponse;
import com.codetree.CodeTreeHRM.auth.dto.UserLoginInfo;
import com.codetree.CodeTreeHRM.auth.dto.UserRoleInfo;
import com.codetree.CodeTreeHRM.auth.mapper.AuthMapper;
import com.codetree.CodeTreeHRM.common.exception.AccountLockedException;
import com.codetree.CodeTreeHRM.common.exception.UnauthorizedException;
import com.codetree.CodeTreeHRM.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private static final String ERR_INVALID_CREDENTIALS = "아이디 또는 비밀번호가 올바르지 않습니다.";

    @Transactional
    public LoginResponse login(LoginRequest request, String ipAddr, String userAgent) {
        UserLoginInfo user = authMapper.findByUserId(request.getUserId());

        if (user == null) {
            log(request.getUserId(), null, "F", "ID_NOT_FOUND", ipAddr, userAgent);
            throw new UnauthorizedException(ERR_INVALID_CREDENTIALS);
        }

        if ("N".equals(user.getUseYn())) {
            log(request.getUserId(), user.getEmpNo(), "F", "ACCOUNT_DISABLED", ipAddr, userAgent);
            throw new UnauthorizedException(ERR_INVALID_CREDENTIALS);
        }

        if ("Y".equals(user.getAcctLockYn())) {
            log(request.getUserId(), user.getEmpNo(), "F", "ACCOUNT_LOCKED", ipAddr, userAgent);
            throw new AccountLockedException("잠긴 계정입니다. 관리자에게 문의하세요.");
        }

        if ("RESIGNED".equals(user.getEmpSttsCd())) {
            log(request.getUserId(), user.getEmpNo(), "F", "RESIGNED", ipAddr, userAgent);
            throw new UnauthorizedException(ERR_INVALID_CREDENTIALS);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getUserPwd())) {
            int newCount = user.getLoginFailAtmtCnt() + 1;
            if (newCount >= 5) {
                authMapper.lockAccount(user.getUserNo());
                log(request.getUserId(), user.getEmpNo(), "F", "WRONG_PWD", ipAddr, userAgent);
                throw new AccountLockedException("로그인 5회 실패로 계정이 잠겼습니다. 관리자에게 문의하세요.");
            }
            authMapper.incrementFailCount(user.getUserNo());
            log(request.getUserId(), user.getEmpNo(), "F", "WRONG_PWD", ipAddr, userAgent);
            throw new UnauthorizedException(ERR_INVALID_CREDENTIALS);
        }

        authMapper.resetFailCountAndUpdateLastLogin(user.getUserNo());

        List<UserRoleInfo> roles = authMapper.findActiveRolesByEmpNo(user.getEmpNo());
        List<String> roleCodes = roles.stream().map(UserRoleInfo::getRoleCd).collect(Collectors.toList());
        int minRoleLevel = roles.stream().mapToInt(UserRoleInfo::getRoleLevel).min().orElse(99);

        String token = jwtTokenProvider.generateToken(user.getUserId(), user.getEmpNo(), roleCodes, minRoleLevel);
        log(request.getUserId(), user.getEmpNo(), "S", null, ipAddr, userAgent);

        return LoginResponse.builder()
                .accessToken(token)
                .userId(user.getUserId())
                .empNo(user.getEmpNo())
                .empNm(user.getEmpNm())
                .roleLevel(minRoleLevel)
                .build();
    }

    private void log(String loginId, String empNo, String rsltCd, String failRsnCd, String ipAddr, String userAgent) {
        authMapper.insertLoginHistory(loginId, empNo, rsltCd, failRsnCd, ipAddr, userAgent);
    }
}
