package com.pocketledger.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Description is required")
    @Size(max = 255 , message = "Description cannot exceed 255 characters")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Amount is required")
    @DecimalMin(value= "0.01", message = "Amount must be greater than to 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid format (max 2 decimal places)")
    private BigDecimal amount;

    private String category;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate date;

    @CreationTimestamp
    @Column(updatable = false,name = "created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
