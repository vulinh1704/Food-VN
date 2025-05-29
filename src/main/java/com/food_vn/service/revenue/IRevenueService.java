package com.food_vn.service.revenue;

import java.util.Date;

public interface IRevenueService {
    void saveRevenueForDate(Date date, Double orderTotal);

    Double getTotalRevenueByDay(Date date);

    Double getTotalRevenueByMonth(int year, int month);

    Double getTotalRevenueByYear(int year);
}
