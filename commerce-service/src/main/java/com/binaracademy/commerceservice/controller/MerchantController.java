package com.binaracademy.commerceservice.controller;

import com.binaracademy.commerceservice.dto.request.UpdateStatusMerchantRequest;
import com.binaracademy.commerceservice.dto.response.base.APIResponse;
import com.binaracademy.commerceservice.dto.response.base.APIResultResponse;
import com.binaracademy.commerceservice.dto.response.MerchantResponse;
import com.binaracademy.commerceservice.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/merchants", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Merchant", description = "Merchant API")
public class MerchantController {
    private final MerchantService merchantService;

    @PutMapping("/status")
    @Schema(name = "UpdateStatusMerchantRequest", description = "Update status merchant request body")
    @Operation(summary = "Endpoint to handle update merchant status")
    public ResponseEntity<APIResponse> updateStatusMerchant(@RequestBody @Valid UpdateStatusMerchantRequest request){
        merchantService.updateStatusMerchant(request);
        APIResponse responseDTO =  new APIResponse(
                HttpStatus.OK,
                "Merchant successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/")
    @Schema(name = "GetAllMerchantRequest", description = "Get all merchant request body")
    @Operation(summary = "Endpoint to handle get all merchant")
    public ResponseEntity<APIResultResponse<Page<MerchantResponse>>> getAllMerchantByStatus(
            @RequestParam("open") Boolean open,
            @RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<MerchantResponse> merchantResponse = merchantService.getAllMerchantByOpen(open, pageable);
        APIResultResponse<Page<MerchantResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Merchant successfully retrieved",
                merchantResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
