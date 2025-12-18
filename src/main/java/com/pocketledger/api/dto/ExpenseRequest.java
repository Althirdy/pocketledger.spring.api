package com.pocketledger.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseRequest {

    @NotBlank(message = "Description is Required")
    @Size(max = 255 , message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMin(value= "0.01", message = "Amount must be greater than to 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid format (max 2 decimal places)")
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate date;

    @NotBlank(message = "Category is required")
    private String category;
}
