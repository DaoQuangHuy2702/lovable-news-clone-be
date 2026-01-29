package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.QuizResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, String> {

        @Query("SELECT q FROM QuizResult q WHERE q.isDeleted = false AND (:quizId IS NULL OR q.quiz.id = :quizId)")
        Page<QuizResult> findLeaderboard(@Param("quizId") String quizId, Pageable pageable);

        Page<QuizResult> findByQuizIdAndIsDeletedFalse(String quizId, Pageable pageable);

        Page<QuizResult> findAllByIsDeletedFalse(Pageable pageable);
}
