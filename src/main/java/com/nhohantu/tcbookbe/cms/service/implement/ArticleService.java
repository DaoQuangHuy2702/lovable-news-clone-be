package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.cms.dto.request.ArticleRequest;
import com.nhohantu.tcbookbe.cms.dto.response.ArticleSummaryResponse;
import com.nhohantu.tcbookbe.common.model.entity.Article;
import com.nhohantu.tcbookbe.common.model.entity.Category;
import com.nhohantu.tcbookbe.common.model.enums.ArticleTypeEnum;
import com.nhohantu.tcbookbe.common.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;

    public Page<ArticleSummaryResponse> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(article -> ArticleSummaryResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .excerpt(article.getExcerpt())
                .views(article.getViews())
                .type(article.getType())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .category(article.getCategory() != null ? ArticleSummaryResponse.CategorySummary.builder()
                        .id(article.getCategory().getId())
                        .name(article.getCategory().getName())
                        .colorCode(article.getCategory().getColorCode())
                        .build() : null)
                .build());
    }

    public Article getArticle(String id) {
        return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
    }

    @Transactional
    public Article createArticle(ArticleRequest request) {
        Category category = null;
        if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
            category = categoryService.getCategory(request.getCategoryId());
        }

        Article article = Article.builder()
                .title(request.getTitle())
                .excerpt(request.getExcerpt())
                .content(request.getContent())
                .thumbnail(request.getThumbnail())
                .type(request.getType() != null ? ArticleTypeEnum.valueOf(request.getType()) : ArticleTypeEnum.NEWS)
                .category(category)
                .build();

        return articleRepository.save(article);
    }

    @Transactional
    public Article updateArticle(String id, ArticleRequest request) {
        Article article = getArticle(id);

        Category category = null;
        if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
            category = categoryService.getCategory(request.getCategoryId());
        }

        article.setTitle(request.getTitle());
        article.setExcerpt(request.getExcerpt());
        article.setContent(request.getContent());
        article.setThumbnail(request.getThumbnail());
        if (request.getType() != null) {
            article.setType(ArticleTypeEnum.valueOf(request.getType()));
        }
        article.setCategory(category);

        return articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(String id) {
        Article article = getArticle(id);
        articleRepository.delete(article);
    }
}
