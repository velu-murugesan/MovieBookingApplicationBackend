package com.velu.MovieBookingApplication.dtoresponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {

   private List<T> content;
   private int pageSize;
   private int totalSize;
   private int totalElements;
   private int totalPages;
   private boolean last;
}
