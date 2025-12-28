package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.cms.service.implement.QuizResultService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.QuizResult;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class PublicQuizController {
    private final QuizResultService quizResultService;

    @PostMapping("/submit")
    public ResponseEntity<ResponseDTO<QuizResult>> submitResult(@RequestBody QuizResult result) {
        return ResponseBuilder.okResponse("Submit result success", quizResultService.saveResult(result),
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<ResponseDTO<List<QuizResult>>> getLeaderboard() {
        return ResponseBuilder.okResponse("Get leaderboard success", quizResultService.getLeaderboard(),
                StatusCodeEnum.SUCCESS2000);
    }
}
