package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.service.implement.QuizResultService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.QuizResult;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/quiz-results")
@RequiredArgsConstructor
public class AdminQuizResultController {
    private final QuizResultService quizResultService;

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<QuizResult>>> getAllResults(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseBuilder.okResponse("Get all results success", quizResultService.getAllResults(pageable),
                StatusCodeEnum.SUCCESS2000);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteResult(@PathVariable String id) {
        quizResultService.deleteResult(id);
        return ResponseBuilder.okResponse("Delete result success", null, StatusCodeEnum.SUCCESS2000);
    }
}
