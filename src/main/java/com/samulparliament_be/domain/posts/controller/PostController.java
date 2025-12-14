package com.samulparliament_be.domain.posts.controller;

import com.samulparliament_be.domain.posts.dto.OrderType;
import com.samulparliament_be.domain.posts.dto.PostCreateRequest;
import com.samulparliament_be.domain.posts.dto.PostResponse;
import com.samulparliament_be.domain.posts.dto.PostUpdateRequest;
import com.samulparliament_be.domain.posts.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostResponse create(
            @Valid @RequestBody PostCreateRequest request
    ) {
        return PostResponse.from(
                postService.create(request.authorId(), request)
        );
    }

    @GetMapping
    public Page<PostResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") OrderType order
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        order == OrderType.ASC
                                ? Sort.Direction.ASC
                                : Sort.Direction.DESC, "createdAt"
                )
        );

        return postService.getAll(pageable)
                .map(PostResponse::from);
        //      .map(post -> PostResponse.from(post))
    }

    @GetMapping("/{id}")
    public PostResponse get(@PathVariable Long id) {
        return PostResponse.from(
                postService.get(id)
        );
    }

    @PutMapping("/{id}")
    public PostResponse update(
            @PathVariable Long id,
            @RequestBody PostUpdateRequest request
    ) {
        return PostResponse.from(
                postService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public void delete(Long id) {
        postService.delete(id);
    }
}
