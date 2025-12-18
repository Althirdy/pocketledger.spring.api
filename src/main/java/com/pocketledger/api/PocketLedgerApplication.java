package com.pocketledger.api;

import com.pocketledger.api.entity.Expense;
import com.pocketledger.api.repository.ExpenseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class PocketLedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocketLedgerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ExpenseRepository expenseRepository){
		return  args -> {
			Expense expense = new Expense();
			expense.setDescription("Transportation Expense");
			expense.setAmount(BigDecimal.valueOf(20.50));
			expense.setDate(LocalDate.now());
			expense.setCategory("TRANSPORTATION");
			expenseRepository.save(expense);
		};
	}
}
