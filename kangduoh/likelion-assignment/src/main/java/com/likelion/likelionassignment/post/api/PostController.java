package com.likelion.likelionassignment.post.api;

import com.likelion.likelionassignment.global.template.ApiResponseTemplate;
import com.likelion.likelionassignment.post.api.dto.PostDto;
import com.likelion.likelionassignment.post.api.dto.request.PostCreateReqDto;
import com.likelion.likelionassignment.post.api.dto.request.PostUpdateReqDto;
import com.likelion.likelionassignment.post.api.dto.response.PostCreateResDto;
import com.likelion.likelionassignment.post.application.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseTemplate<PostCreateResDto>> createPost(
            @RequestPart("reqDto") PostCreateReqDto reqDto,
            @RequestPart("image") MultipartFile image,
            Principal principal) throws IOException {

        ApiResponseTemplate<PostCreateResDto> data = postService.createPost(principal, reqDto, image);

        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping
    public ResponseEntity<ApiResponseTemplate<List<PostDto>>> getAllPosts() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        List<PostDto> posts = postService.getAllPosts(principal).getData();
        ApiResponseTemplate<List<PostDto>> data = ApiResponseTemplate.<List<PostDto>>builder()
                .status(200)
                .success(true)
                .message("모든 게시글 조회 성공")
                .data(posts)
                .build();
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponseTemplate<PostDto>> getPostById(@PathVariable Long postId, Principal principal) {
        PostDto post = postService.getPostById(postId, principal).getData();
        ApiResponseTemplate<PostDto> data = ApiResponseTemplate.<PostDto>builder()
                .status(200)
                .success(true)
                .message("특정 게시글 조회 성공")
                .data(post)
                .build();
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponseTemplate<Void>> updatePost(
            @PathVariable Long postId,
            @RequestPart("reqDto") PostUpdateReqDto reqDto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Principal principal) throws IOException {

        ApiResponseTemplate<Void> data = postService.updatePost(principal, postId, reqDto, image);

        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponseTemplate<Void>> deletePost(
            @PathVariable Long postId,
            Principal principal) {

        ApiResponseTemplate<Void> data = postService.deletePost(principal, postId);

        return ResponseEntity.status(data.getStatus()).body(data);
    }
}