package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.common.model.entity.QuizResult;
import com.nhohantu.tcbookbe.common.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizResultService {
    private final QuizResultRepository quizResultRepository;

    @Transactional
    public QuizResult saveResult(QuizResult result) {
        return quizResultRepository.save(result);
    }

    public List<QuizResult> getLeaderboard() {
        return quizResultRepository.findTopRankings();
    }

    public Page<QuizResult> getAllResults(Pageable pageable) {
        return quizResultRepository.findAll(pageable);
    }

    @Transactional
    public void deleteResult(String id) {
        QuizResult result = quizResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found"));
        result.setDeleted(true);
        quizResultRepository.save(result);
    }
}
