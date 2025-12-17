package com.pocketledger.api.service;


import com.pocketledger.api.entity.Expense;
import com.pocketledger.api.exception.ResourceNotFoundException;
import com.pocketledger.api.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ExpenseService {
    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository){
        this.repository = repository;
    }

    public List<Expense> getAllExpenses(){
        return repository.findAll();
    }

    public Expense saveExpense(Expense expense){
        return  repository.save(expense);
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
