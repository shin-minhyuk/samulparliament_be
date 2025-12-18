package com.samulparliament_be.domain.posts.controller;

import com.samulparliament_be.domain.posts.service.PostService;
import com.samulparliament_be.global.auth.annotation.ADMIN;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;

    @ADMIN
    @DeleteMapping("/{id}")
    public void forceDelete(@PathVariable Long id) {
        postService.forceDelete(id);
    }
}
