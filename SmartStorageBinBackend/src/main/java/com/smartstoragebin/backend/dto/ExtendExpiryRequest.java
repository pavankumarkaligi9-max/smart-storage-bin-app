package com.smartstoragebin.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtendExpiryRequest {
    private Integer additionalDays;
    private LocalDate newExpiryDate;
}
