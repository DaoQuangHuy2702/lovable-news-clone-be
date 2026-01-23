package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.cms.dto.response.QuizResponse;
import com.nhohantu.tcbookbe.cms.service.implement.QuizService;
import com.nhohantu.tcbookbe.cms.service.implement.QuizResultService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.Option;
import com.nhohantu.tcbookbe.common.model.entity.Question;
import com.nhohantu.tcbookbe.common.model.entity.Quiz;
import com.nhohantu.tcbookbe.common.model.entity.QuizResult;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class PublicQuizController {
    private final QuizResultService quizResultService;
    private final QuizService quizService;

    @GetMapping("/active")
    public ResponseEntity<ResponseDTO<QuizResponse>> getActiveQuiz() {
        return quizService.getActiveQuiz()
                .map(quiz -> ResponseBuilder.okResponse("Lấy bộ câu hỏi hiện tại thành công",
                        convertToResponse(quiz), StatusCodeEnum.SUCCESS2000))
                .orElse(ResponseBuilder.badRequestResponse("Không có bộ câu hỏi nào đang hoạt động",
                        StatusCodeEnum.ERRORCODE4000));
    }

    @PostMapping("/submit")
    public ResponseEntity<ResponseDTO<QuizResult>> submitResult(@RequestBody QuizResult result) {
        return ResponseBuilder.okResponse("Nộp kết quả thành công", quizResultService.saveResult(result),
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<ResponseDTO<List<QuizResult>>> getLeaderboard(@RequestParam(required = false) String quizId) {
        List<QuizResult> leaderboard;
        if (quizId != null && !quizId.isEmpty()) {
            leaderboard = quizResultService.getLeaderboardByQuizId(quizId);
        } else {
            leaderboard = quizResultService.getLeaderboard();
        }
        return ResponseBuilder.okResponse("Lấy bảng xếp hạng thành công", leaderboard,
                StatusCodeEnum.SUCCESS2000);
    }

    private QuizResponse convertToResponse(Quiz quiz) {
        List<Question> questions = quizService.getQuestionsForQuiz(quiz.getId());

        List<QuizResponse.QuestionResponse> questionResponses = questions.stream().map(q -> {
            List<Option> options = quizService.getOptionsForQuestion(q.getId());
            return QuizResponse.QuestionResponse.builder()
                    .id(q.getId())
                    .content(q.getContent())
                    .options(options.stream().map(o -> QuizResponse.OptionResponse.builder()
                            .id(o.getId())
                            .content(o.getContent())
                            // .isCorrect(o.isCorrect()) // Should we hide correct answer in public GET?
                            // Actually, for the quiz to work in FE, we need to handle scoring.
                            // But if we want to be secure, scoring should be on BE.
                            // The current plan is simple, so I'll keep it as is, or maybe hide it if we do
                            // BE validation.
                            // Seeing Games.tsx, it does scoring on FE. So we NEED isCorrect.
                            .isCorrect(o.isCorrect())
                            .build()).collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());

        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .isActive(quiz.isActive())
                .questions(questionResponses)
                .build();
    }
}
