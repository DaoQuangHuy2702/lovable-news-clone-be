package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.cms.dto.request.ArticleRequest;
import com.nhohantu.tcbookbe.cms.dto.response.ArticleSummaryResponse;
import com.nhohantu.tcbookbe.common.model.entity.Article;
import com.nhohantu.tcbookbe.common.model.entity.Category;
import com.nhohantu.tcbookbe.common.model.enums.ArticleTypeEnum;
import com.nhohantu.tcbookbe.common.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;
    private final com.nhohantu.tcbookbe.common.service.UploadService uploadService;

    public Page<ArticleSummaryResponse> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(this::mapToSummaryResponse);
    }

    public Page<ArticleSummaryResponse> getArticlesByType(String type, Pageable pageable) {
        ArticleTypeEnum articleType = ArticleTypeEnum.valueOf(type);
        return articleRepository.findByType(articleType, pageable).map(this::mapToSummaryResponse);
    }

    public Page<ArticleSummaryResponse> getArticlesByTypeAndCategory(String type, String categoryId,
            Pageable pageable) {
        ArticleTypeEnum articleType = ArticleTypeEnum.valueOf(type);
        return articleRepository.findByTypeAndCategoryId(articleType, categoryId, pageable)
                .map(this::mapToSummaryResponse);
    }

    private ArticleSummaryResponse mapToSummaryResponse(Article article) {
        return ArticleSummaryResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .excerpt(article.getExcerpt())
                .views(article.getViews())
                .type(article.getType())
                .thumbnail(article.getThumbnail())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .category(article.getCategory() != null ? ArticleSummaryResponse.CategorySummary.builder()
                        .id(article.getCategory().getId())
                        .name(article.getCategory().getName())
                        .colorCode(article.getCategory().getColorCode())
                        .build() : null)
                .build();
    }

    public Article getArticle(String id) {
        return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
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

        // Delete old image if thumbnail changed
        if (article.getThumbnail() != null && !article.getThumbnail().equals(request.getThumbnail())) {
            uploadService.deleteFile(article.getThumbnail());
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

        // Delete image from Cloudinary
        if (article.getThumbnail() != null) {
            uploadService.deleteFile(article.getThumbnail());
        }

        articleRepository.delete(article);
    }

    @Transactional
    public void increaseView(String id) {
        articleRepository.incrementViews(id);
    }

    public ArticleSummaryResponse getFeaturedArticle(String type, String categoryId) {
        ArticleTypeEnum articleType = ArticleTypeEnum.valueOf(type);
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("views"), Sort.Order.desc("createdAt")));

        Page<Article> page;
        if (categoryId != null && !categoryId.isEmpty()) {
            page = articleRepository.findByTypeAndCategoryId(articleType, categoryId, pageable);
        } else {
            page = articleRepository.findByType(articleType, pageable);
        }

        if (page.hasContent()) {
            return mapToSummaryResponse(page.getContent().get(0));
        }
        return null;
    }
}
