package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.request.QuizRequest;
import com.nhohantu.tcbookbe.cms.dto.response.QuizResponse;
import com.nhohantu.tcbookbe.cms.service.implement.QuizService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.Option;
import com.nhohantu.tcbookbe.common.model.entity.Question;
import com.nhohantu.tcbookbe.common.model.entity.Quiz;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cms/quizzes")
@RequiredArgsConstructor
public class AdminQuizController {

    private final QuizService quizService;

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<Quiz>>> getAllQuizzes(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseBuilder.okResponse("Lấy danh sách bộ câu hỏi thành công",
                quizService.getAllQuizzes(pageable), StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<QuizResponse>> getQuiz(@PathVariable String id) {
        Quiz quiz = quizService.getQuiz(id);
        return ResponseBuilder.okResponse("Lấy thông tin bộ câu hỏi thành công",
                convertToResponse(quiz), StatusCodeEnum.SUCCESS2000);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Quiz>> createQuiz(@RequestBody QuizRequest request) {
        return ResponseBuilder.okResponse("Tạo bộ câu hỏi thành công",
                quizService.createQuiz(request), StatusCodeEnum.SUCCESS2000);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Quiz>> updateQuiz(@PathVariable String id, @RequestBody QuizRequest request) {
        return ResponseBuilder.okResponse("Cập nhật bộ câu hỏi thành công",
                quizService.updateQuiz(id, request), StatusCodeEnum.SUCCESS2000);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteQuiz(@PathVariable String id) {
        quizService.deleteQuiz(id);
        return ResponseBuilder.okResponse("Xóa bộ câu hỏi thành công", null, StatusCodeEnum.SUCCESS2000);
    }

    @PostMapping("/{id}/active")
    public ResponseEntity<ResponseDTO<Void>> setActive(@PathVariable String id) {
        quizService.setActive(id);
        return ResponseBuilder.okResponse("Kích hoạt bộ câu hỏi thành công", null, StatusCodeEnum.SUCCESS2000);
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ResponseDTO<Void>> deactivate(@PathVariable String id) {
        quizService.deactivate(id);
        return ResponseBuilder.okResponse("Bỏ kích hoạt bộ câu hỏi thành công", null, StatusCodeEnum.SUCCESS2000);
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
