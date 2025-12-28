package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhohantu.tcbookbe.common.model.enums.ArticleTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {
    Page<Article> findByType(ArticleTypeEnum type, Pageable pageable);

    Page<Article> findByTypeAndCategoryId(ArticleTypeEnum type, String categoryId, Pageable pageable);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE Article a SET a.views = a.views + 1 WHERE a.id = :id")
    void incrementViews(@org.springframework.web.bind.annotation.PathVariable("id") String id);
}
