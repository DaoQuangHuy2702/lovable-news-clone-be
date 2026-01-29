package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.common.model.entity.QuizResult;
import com.nhohantu.tcbookbe.common.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        return getTop3Results(null);
    }

    public List<QuizResult> getLeaderboardByQuizId(String quizId) {
        return getTop3Results(quizId);
    }

    private List<QuizResult> getTop3Results(String quizId) {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "score")
                .and(Sort.by(Sort.Direction.ASC, "completionTime"))
                .and(Sort.by(Sort.Direction.ASC, "createdAt")));
        return quizResultRepository.findLeaderboard(quizId, pageable).getContent();
    }

    public Page<QuizResult> getAllResults(Pageable pageable) {
        return quizResultRepository.findAllByIsDeletedFalse(pageable);
    }

    public Page<QuizResult> getAllResultsByQuizId(String quizId, Pageable pageable) {
        return quizResultRepository.findByQuizIdAndIsDeletedFalse(quizId, pageable);
    }

    @Transactional
    public void deleteResult(String id) {
        QuizResult result = quizResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kết quả"));
        result.setDeleted(true);
        quizResultRepository.save(result);
    }
}
