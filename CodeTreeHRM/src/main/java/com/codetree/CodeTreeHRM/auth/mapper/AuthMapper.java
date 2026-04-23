package com.codetree.CodeTreeHRM.auth.mapper;

import com.codetree.CodeTreeHRM.auth.dto.UserLoginInfo;
import com.codetree.CodeTreeHRM.auth.dto.UserRoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthMapper {

    UserLoginInfo findByUserId(@Param("userId") String userId);

    List<UserRoleInfo> findActiveRolesByEmpNo(@Param("empNo") String empNo);

    void incrementFailCount(@Param("userNo") Long userNo);

    void lockAccount(@Param("userNo") Long userNo);

    void resetFailCountAndUpdateLastLogin(@Param("userNo") Long userNo);

    void insertLoginHistory(@Param("loginId") String loginId,
                            @Param("empNo") String empNo,
                            @Param("rsltCd") String rsltCd,
                            @Param("failRsnCd") String failRsnCd,
                            @Param("ipAddr") String ipAddr,
                            @Param("userAgent") String userAgent);
}
