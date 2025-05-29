package com.food_vn.repository.revenue;

import com.food_vn.model.revenue.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.Optional;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    @Query("SELECT r FROM Revenue r WHERE FUNCTION('DATE', r.date) = FUNCTION('DATE', :date)")
    Optional<Revenue> findByDate(Date date);

    @Query("SELECT COALESCE(SUM(r.total), 0) FROM Revenue r WHERE FUNCTION('DATE', r.date) = FUNCTION('DATE', :date)")
    Double getTotalRevenueByDay(Date date);

    @Query("SELECT COALESCE(SUM(r.total), 0) FROM Revenue r WHERE FUNCTION('YEAR', r.date) = :year AND FUNCTION('MONTH', r.date) = :month")
    Double getTotalRevenueByMonth(int year, int month);

    @Query("SELECT COALESCE(SUM(r.total), 0) FROM Revenue r WHERE FUNCTION('YEAR', r.date) = :year")
    Double getTotalRevenueByYear(int year);
}
