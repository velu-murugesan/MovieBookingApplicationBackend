package com.velu.MovieBookingApplication.dtoresponse;
import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PaginationResponse<T> {

   private List<T> content;
   private int pageSize;
   private int totalSize;
   private int totalElements;
   private int totalPages;
   private boolean last;

}
