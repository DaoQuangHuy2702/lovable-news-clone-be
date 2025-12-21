package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import com.nhohantu.tcbookbe.common.model.enums.ArticleTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "articles")
public class Article extends BaseModel {

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "excerpt", length = 500)
    private String excerpt;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "thumbnail", length = 500)
    private String thumbnail;

    @Column(name = "views")
    @Builder.Default
    private Long views = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private ArticleTypeEnum type;
}
