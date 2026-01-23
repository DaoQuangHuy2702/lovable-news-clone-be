package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.cms.dto.request.QuizRequest;
import com.nhohantu.tcbookbe.common.exception.BadRequestException;
import com.nhohantu.tcbookbe.common.model.entity.Option;
import com.nhohantu.tcbookbe.common.model.entity.Question;
import com.nhohantu.tcbookbe.common.model.entity.Quiz;
import com.nhohantu.tcbookbe.common.repository.OptionRepository;
import com.nhohantu.tcbookbe.common.repository.QuestionRepository;
import com.nhohantu.tcbookbe.common.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public Page<Quiz> getAllQuizzes(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public Quiz getQuiz(String id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy bộ câu hỏi"));
    }

    @Transactional
    public Quiz createQuiz(QuizRequest request) {
        Quiz quiz = Quiz.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .isActive(request.isActive())
                .build();

        if (quiz.isActive()) {
            deactivateAllQuizzes();
        }

        quiz = quizRepository.save(quiz);
        saveQuestions(quiz, request.getQuestions());

        return quiz;
    }

    @Transactional
    public Quiz updateQuiz(String id, QuizRequest request) {
        Quiz quiz = getQuiz(id);

        if (quiz.isActive()) {
            throw new BadRequestException(
                    "Không thể chỉnh sửa bộ câu hỏi đang được kích hoạt. Vui lòng hủy kích hoạt trước khi sửa.");
        }

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());

        if (request.isActive() && !quiz.isActive()) {
            deactivateAllQuizzes();
        }
        quiz.setActive(request.isActive());

        quiz = quizRepository.save(quiz);

        List<Question> existingQuestions = questionRepository.findByQuizIdAndIsDeletedFalse(id);
        for (Question q : existingQuestions) {
            List<Option> existingOptions = optionRepository.findByQuestionIdAndIsDeletedFalse(q.getId());
            for (Option o : existingOptions) {
                o.setDeleted(true);
                optionRepository.save(o);
            }
            q.setDeleted(true);
            questionRepository.save(q);
        }

        saveQuestions(quiz, request.getQuestions());

        return quiz;
    }

    private void saveQuestions(Quiz quiz, List<QuizRequest.QuestionRequest> questionRequests) {
        if (questionRequests == null)
            return;

        for (QuizRequest.QuestionRequest qr : questionRequests) {
            Question question = Question.builder()
                    .content(qr.getContent())
                    .quiz(quiz)
                    .build();
            question = questionRepository.save(question);

            if (qr.getOptions() != null) {
                for (QuizRequest.OptionRequest or : qr.getOptions()) {
                    Option option = Option.builder()
                            .content(or.getContent())
                            .isCorrect(or.isCorrect())
                            .question(question)
                            .build();
                    optionRepository.save(option);
                }
            }
        }
    }

    @Transactional
    public void deleteQuiz(String id) {
        Quiz quiz = getQuiz(id);

        if (quiz.isActive()) {
            throw new BadRequestException(
                    "Không thể xóa bộ câu hỏi đang được kích hoạt. Vui lòng hủy kích hoạt trước khi xóa.");
        }

        quiz.setDeleted(true);
        quizRepository.save(quiz);
    }

    @Transactional
    public void setActive(String id) {
        deactivateAllQuizzes();
        Quiz quiz = getQuiz(id);
        quiz.setActive(true);
        quizRepository.save(quiz);
    }

    @Transactional
    public void deactivate(String id) {
        Quiz quiz = getQuiz(id);
        quiz.setActive(false);
        quizRepository.save(quiz);
    }

    private void deactivateAllQuizzes() {
        List<Quiz> activeQuizzes = quizRepository.findAllByIsActiveTrueAndIsDeletedFalse();
        for (Quiz q : activeQuizzes) {
            q.setActive(false);
            quizRepository.save(q);
        }
    }

    public Optional<Quiz> getActiveQuiz() {
        return quizRepository.findByIsActiveTrueAndIsDeletedFalse();
    }

    public List<Question> getQuestionsForQuiz(String quizId) {
        return questionRepository.findByQuizIdAndIsDeletedFalse(quizId);
    }

    public List<Option> getOptionsForQuestion(String questionId) {
        return optionRepository.findByQuestionIdAndIsDeletedFalse(questionId);
    }
}
