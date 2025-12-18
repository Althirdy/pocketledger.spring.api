package com.pocketledger.api.mapper;

import com.pocketledger.api.dto.ExpenseRequest;
import com.pocketledger.api.dto.ExpenseResponse;
import com.pocketledger.api.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "id",ignore = true)
    Expense toEntity(ExpenseRequest expense);

    ExpenseResponse toResponse(Expense expense);
}
