package com.insurance.claim.controller;

import com.insurance.claim.dto.ApiResponse;
import com.insurance.claim.dto.ThirdPartyCallbackRequest;
import com.insurance.claim.entity.ThirdPartyCallback;
import com.insurance.claim.service.ThirdPartyCallbackService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/callbacks")
public class CallbackController {

    private final ThirdPartyCallbackService callbackService;

    public CallbackController(ThirdPartyCallbackService callbackService) {
        this.callbackService = callbackService;
    }

    @GetMapping("/{callbackId}")
    public ApiResponse<ThirdPartyCallback> getCallback(@PathVariable String callbackId) {
        ThirdPartyCallback callback = callbackService.getCallbackById(callbackId);
        return ApiResponse.success(callback);
    }

    @GetMapping("/claim/{claimNumber}")
    public ApiResponse<List<ThirdPartyCallback>> getCallbacksByClaim(
            @PathVariable String claimNumber) {
        List<ThirdPartyCallback> callbacks = callbackService.getCallbacksByClaim(claimNumber);
        return ApiResponse.success(callbacks);
    }

    @GetMapping("/source/{sourceSystem}")
    public ApiResponse<List<ThirdPartyCallback>> getCallbacksBySource(
            @PathVariable String sourceSystem) {
        List<ThirdPartyCallback> callbacks = callbackService.getCallbacksBySource(sourceSystem);
        return ApiResponse.success(callbacks);
    }

    @PostMapping("/receive")
    public ApiResponse<ThirdPartyCallback> receiveCallback(
            @Valid @RequestBody ThirdPartyCallbackRequest request) {
        ThirdPartyCallback callback = callbackService.receiveCallback(request);
        return ApiResponse.success("回调已接收", callback);
    }

    @PostMapping("/{callbackId}/process")
    public ApiResponse<Void> processCallback(@PathVariable Long callbackId) {
        callbackService.processCallback(callbackId);
        return ApiResponse.success("回调处理完成", null);
    }
}
