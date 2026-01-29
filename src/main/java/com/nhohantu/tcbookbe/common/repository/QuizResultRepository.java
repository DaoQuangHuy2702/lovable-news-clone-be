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

        @Query(value = "SELECT * FROM quiz_results q WHERE q.is_deleted = false AND (:quizId IS NULL OR q.quiz_id = :quizId) ORDER BY q.score DESC, q.completion_time ASC, q.created_at ASC LIMIT 3", nativeQuery = true)
        List<QuizResult> findTop3Rankings(@Param("quizId") String quizId);

        Page<QuizResult> findByQuizIdAndIsDeletedFalse(String quizId, Pageable pageable);

        Page<QuizResult> findAllByIsDeletedFalse(Pageable pageable);
}
