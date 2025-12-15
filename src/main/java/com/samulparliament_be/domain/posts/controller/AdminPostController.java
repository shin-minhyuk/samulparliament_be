package com.samulparliament_be.domain.posts.controller;

import com.samulparliament_be.domain.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPostController {

    private final PostService postService;

    @DeleteMapping("/{id}")
    public void forceDelete(@PathVariable Long id) {
        postService.forceDelete(id);
    }
}