package com.pocketledger.api.controller;

import com.pocketledger.api.dto.ApiResponse;
import com.pocketledger.api.entity.Expense;
import com.pocketledger.api.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Expense>>> getAllExpenses(){
        List<Expense> expenses =  expenseService.getAllExpenses();
        ApiResponse<List<Expense>> response = new ApiResponse<>("Expenses fetched successfully", expenses);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Expense>> saveExpense(@Valid @RequestBody Expense expense){
        Expense newExpense =  expenseService.saveExpense(expense);
        ApiResponse<Expense> response = new ApiResponse<>("Expense Saved Successfully", newExpense);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
        ApiResponse<String> response = new ApiResponse<>("Expense Deleted Successfully",null);
        return ResponseEntity.ok(response);
    }

}
