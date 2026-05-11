package com.codetree.CodeTreeHRM.system.controller;

import com.codetree.CodeTreeHRM.common.dto.ApiResponse;
import com.codetree.CodeTreeHRM.hr.dto.PageResponse;
import com.codetree.CodeTreeHRM.system.dto.UserDto;
import com.codetree.CodeTreeHRM.system.dto.UserSearchDto;
import com.codetree.CodeTreeHRM.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/system/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 사용자 목록 조회 (검색 + 페이징) */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserDto>>> searchUsers(UserSearchDto search) {
        return ResponseEntity.ok(ApiResponse.success(userService.searchUsers(search)));
    }

    /** 사용자 단건 상세 조회 */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable String userId) {
        UserDto user = userService.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /** 사용자 저장/수정 (Upsert) */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveUser(@RequestBody UserDto dto) {
        try {
            userService.saveUser(dto);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /** 비밀번호 변경/초기화 */
    @PatchMapping("/{userId}/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable String userId,
            @RequestBody Map<String, String> body) {
        try {
            String newPassword = body.get("newPassword");
            userService.changePassword(userId, newPassword);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
