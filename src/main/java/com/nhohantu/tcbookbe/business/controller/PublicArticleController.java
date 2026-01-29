package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.cms.dto.response.ArticleSummaryResponse;
import com.nhohantu.tcbookbe.cms.service.implement.ArticleService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nhohantu.tcbookbe.common.model.entity.Article;

@RestController
@RequestMapping("/public/articles")
@RequiredArgsConstructor
public class PublicArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<ArticleSummaryResponse>>> getArticles(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String categoryId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ArticleSummaryResponse> articles;
        if (type != null && !type.isEmpty()) {
            if (categoryId != null && !categoryId.isEmpty()) {
                articles = articleService.getArticlesByTypeAndCategory(type, categoryId, pageable);
            } else {
                articles = articleService.getArticlesByType(type, pageable);
            }
        } else {
            articles = articleService.getAllArticles(pageable);
        }
        return ResponseBuilder.okResponse("Get articles success", articles, StatusCodeEnum.SUCCESS2000);
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/view")
    public ResponseEntity<ResponseDTO<Void>> incrementView(
            @org.springframework.web.bind.annotation.PathVariable String id) {
        articleService.increaseView(id);
        return ResponseBuilder.okResponse("Increment view success", null, StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/featured")
    public ResponseEntity<ResponseDTO<ArticleSummaryResponse>> getFeaturedArticle(
            @RequestParam String type,
            @RequestParam(required = false) String categoryId) {
        ArticleSummaryResponse article = articleService.getFeaturedArticle(type, categoryId);
        return ResponseBuilder.okResponse("Get featured article success", article, StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Article>> getArticle(@PathVariable String id) {
        return ResponseBuilder.okResponse("Lấy chi tiết bài viết thành công", articleService.getArticle(id),
                StatusCodeEnum.SUCCESS2000);
    }
}
