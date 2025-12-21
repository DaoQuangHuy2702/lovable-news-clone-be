package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.request.ArticleRequest;
import com.nhohantu.tcbookbe.cms.dto.response.ArticleSummaryResponse;
import com.nhohantu.tcbookbe.cms.service.implement.ArticleService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.Article;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/articles")
@RequiredArgsConstructor
public class CmsArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<ArticleSummaryResponse>>> getAllArticles(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ArticleSummaryResponse> articles = articleService.getAllArticles(pageable);
        return ResponseBuilder.okResponse("Get articles success", articles, StatusCodeEnum.SUCCESS2000);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Article>> createArticle(@Valid @RequestBody ArticleRequest request) {
        return ResponseBuilder.okResponse("Create article success", articleService.createArticle(request),
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Article>> getArticle(@PathVariable String id) {
        return ResponseBuilder.okResponse("Get article success", articleService.getArticle(id),
                StatusCodeEnum.SUCCESS2000);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Article>> updateArticle(@PathVariable String id,
            @Valid @RequestBody ArticleRequest request) {
        return ResponseBuilder.okResponse("Update article success", articleService.updateArticle(id, request),
                StatusCodeEnum.SUCCESS2000);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteArticle(@PathVariable String id) {
        articleService.deleteArticle(id);
        return ResponseBuilder.okResponse("Delete article success", null, StatusCodeEnum.SUCCESS2000);
    }
}
