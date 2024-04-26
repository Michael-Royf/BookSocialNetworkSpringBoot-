package com.michael.book_social_network.constant;


public class PaginationConstants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
}


//    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
//            Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//
//    Pageable pageable = PageRequest.of(pageNo, pageSiZe, sort);
