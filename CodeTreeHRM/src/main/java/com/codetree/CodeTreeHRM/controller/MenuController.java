package com.codetree.CodeTreeHRM.controller;

import com.codetree.CodeTreeHRM.repository.ComMenuRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    private final ComMenuRepository comMenuRepository;

    @GetMapping("/menus")
    public ResponseEntity<?> getMenus(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(comMenuRepository.findByUseYnOrderByMenuOrdr("Y"));
    }
}
