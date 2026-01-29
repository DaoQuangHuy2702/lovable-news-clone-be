package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, String> {

    @Query("SELECT q FROM QuizResult q WHERE q.isDeleted = false ORDER BY q.score DESC, q.completionTime ASC, q.createdAt ASC")
    List<QuizResult> findTop3Rankings();

    @Query("SELECT q FROM QuizResult q WHERE q.quiz.id = :quizId AND q.isDeleted = false ORDER BY q.score DESC, q.completionTime ASC, q.createdAt ASC")
    List<QuizResult> findTop3RankingsByQuizId(String quizId);

    org.springframework.data.domain.Page<QuizResult> findByQuizIdAndIsDeletedFalse(String quizId,
            org.springframework.data.domain.Pageable pageable);
}
