package com.pocketledger.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketledger.api.dto.ExpenseRequest;
import com.pocketledger.api.dto.ExpenseResponse;
import com.pocketledger.api.entity.Category;
import com.pocketledger.api.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseControllerTest.class)
@Import(ExpenseController.class)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createExpense_withValidCategory_shouldReturn201() throws Exception {
        ExpenseRequest request = new ExpenseRequest();
        request.setCategory(Category.TRANSPORT);
        request.setDate(LocalDate.now());
        request.setAmount(new BigDecimal("50.50"));
        request.setDescription("Lunch");

        ExpenseResponse mockResponse  = new ExpenseResponse();
        mockResponse.setId(1L);
        mockResponse.setCategory(Category.TRANSPORT);

        when(expenseService.saveExpense(any(ExpenseRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andDo(print())
                        .andExpect(status().isCreated());
    }

    @Test
    public void getAllExpense_shouldReturnListAnd200() throws Exception {
        ExpenseResponse expense1 = new ExpenseResponse();
        expense1.setId(1L);
        expense1.setDescription("Coffee");

        ExpenseResponse expense2 = new ExpenseResponse();
        expense2.setId(2L);
        expense2.setDescription("Uber");

        List<ExpenseResponse> mockList = Arrays.asList(expense1, expense2);

        when(expenseService.getAllExpenses()).thenReturn(mockList);

        mockMvc.perform(get("/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.data.size()").value(2))
                .andExpect(jsonPath("$.data[0].description").value("Coffee"));
    }


    @Test
    public void deleteExpense_shouldCallServiceAndReturn200() throws Exception {
        Long expenseId = 99L;

        doNothing().when(expenseService).deleteExpense(expenseId);

        mockMvc.perform(delete("/api/v1/expenses/{id}",expenseId).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.message")).value("Expense Deleted Successfully"));

        verify(expenseService, times(1)).deleteExpense(expenseId);
    }

    @Test
    public void createExpense_withInvalidCategory_shouldReturn400() throws  Exception {
        String invalidJson = """
                {
                    "description": "Gym Membership",
                    "amount": 50.00,
                    "date": "2026-01-02",
                    "category": "GYM"
                }
                """;
        mockMvc.perform(post("/api/v1/expenses").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Allowed values are"))));
    }

}
