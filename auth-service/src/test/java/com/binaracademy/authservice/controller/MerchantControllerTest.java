package com.binaracademy.authservice.controller;

import com.binaracademy.authservice.dto.response.base.APIResponse;
import com.binaracademy.authservice.dto.response.base.APIResultResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class MerchantControllerTest {
    @InjectMocks
    private MerchantController merchantController;
    @Mock
    private MerchantService merchantService;
    @Test
    void testUpdateStatusMerchant() {
        UpdateStatusMerchantRequest request = new UpdateStatusMerchantRequest(true);

        doNothing().when(merchantService).updateStatusMerchant(request);

        ResponseEntity<APIResponse> result = merchantController.updateStatusMerchant(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Merchant successfully updated", Objects.requireNonNull(result.getBody()).getMessage());

        verify(merchantService).updateStatusMerchant(request);
    }

    @Test
    void testGetAllMerchantByStatus() {
        Boolean open = true;

        Page<MerchantResponse> response = Page.empty();

        when(merchantService.getAllMerchantByOpen(open, PageRequest.of(0, 5))).thenReturn(response);

        ResponseEntity<APIResultResponse<Page<MerchantResponse>>> result = merchantController.getAllMerchantByStatus(open, 0);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Merchant successfully retrieved", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(response, result.getBody().getResults());

        verify(merchantService).getAllMerchantByOpen(open, PageRequest.of(0, 5));
    }

}
