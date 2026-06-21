package com.insurance.claim.controller;

import com.insurance.claim.dto.ApiResponse;
import com.insurance.claim.dto.SurveySubmitRequest;
import com.insurance.claim.entity.SurveyRecord;
import com.insurance.claim.service.SurveyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/{surveyNumber}")
    public ApiResponse<SurveyRecord> getSurvey(@PathVariable String surveyNumber) {
        SurveyRecord survey = surveyService.getSurveyByNumber(surveyNumber);
        return ApiResponse.success(survey);
    }

    @GetMapping("/claim/{claimNumber}")
    public ApiResponse<List<SurveyRecord>> getSurveysByClaim(@PathVariable String claimNumber) {
        List<SurveyRecord> surveys = surveyService.getSurveysByClaimNumber(claimNumber);
        return ApiResponse.success(surveys);
    }

    @GetMapping("/claim/{claimNumber}/latest")
    public ApiResponse<SurveyRecord> getLatestSurvey(@PathVariable String claimNumber) {
        SurveyRecord survey = surveyService.getLatestSurveyByClaimNumber(claimNumber);
        return ApiResponse.success(survey);
    }

    @GetMapping("/surveyor/{surveyorId}")
    public ApiResponse<List<SurveyRecord>> getSurveysBySurveyor(@PathVariable String surveyorId) {
        List<SurveyRecord> surveys = surveyService.getSurveysBySurveyor(surveyorId);
        return ApiResponse.success(surveys);
    }

    @GetMapping("/fraud-suspected")
    public ApiResponse<List<SurveyRecord>> getFraudSuspectedSurveys() {
        List<SurveyRecord> surveys = surveyService.getFraudSuspectedSurveys();
        return ApiResponse.success(surveys);
    }

    @PostMapping("/submit")
    public ApiResponse<SurveyRecord> submitSurvey(@Valid @RequestBody SurveySubmitRequest request) {
        SurveyRecord survey = surveyService.submitSurvey(request);
        return ApiResponse.success("查勘记录提交成功", survey);
    }

    @PutMapping("/{surveyNumber}/verify")
    public ApiResponse<SurveyRecord> verifySurvey(
            @PathVariable String surveyNumber,
            @RequestParam(required = false) String verifierRemark) {
        SurveyRecord survey = surveyService.verifySurvey(surveyNumber, verifierRemark);
        return ApiResponse.success("查勘记录已验证", survey);
    }
}
