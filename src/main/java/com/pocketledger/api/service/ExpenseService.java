package com.pocketledger.api.service;


import com.pocketledger.api.dto.ExpenseRequest;
import com.pocketledger.api.dto.ExpenseResponse;
import com.pocketledger.api.entity.Expense;
import com.pocketledger.api.exception.ResourceNotFoundException;
import com.pocketledger.api.mapper.ExpenseMapper;
import com.pocketledger.api.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExpenseService {
    private final ExpenseRepository repository;
    private final ExpenseMapper expenseMapper;

    public ExpenseService(ExpenseRepository repository, ExpenseMapper expenseMapper){
        this.repository = repository;
        this.expenseMapper = expenseMapper;
    }

    public List<ExpenseResponse> getAllExpenses(){
        ArrayList<ExpenseResponse> allExpense = new ArrayList<>();
        repository.findAll().forEach((expense -> {
            allExpense.add(expenseMapper.toResponse(expense));
        }));
        return  allExpense;
    }

    public ExpenseResponse getExpenseById(Long id){
        Expense expense = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Expense with id " + id + " not found"));
        return expenseMapper.toResponse(expense);
    }

    public ExpenseResponse saveExpense(ExpenseRequest expenseRequest){
        Expense expense = expenseMapper.toEntity(expenseRequest);
        Expense savedExpense = repository.save(expense);
        return  expenseMapper.toResponse(savedExpense);
    }

    public Expense updateExpense(Long id, Expense expense){
        Expense existingExpense = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with ID " + id + " not found"));
        existingExpense.setAmount(expense.getAmount());
        existingExpense.setDate(expense.getDate());
        existingExpense.setCategory(expense.getCategory());
        existingExpense.setDescription(expense.getDescription());

        return repository.save(existingExpense);
    }

    public void deleteExpense(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Expense with ID " + id + " not found");
        }

        repository.deleteById(id);
    }

}
