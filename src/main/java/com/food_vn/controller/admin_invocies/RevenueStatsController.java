package com.food_vn.controller.admin_invocies;

import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.service.order_details.impl.OrderDetailService;
import com.food_vn.service.revenue.IRevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RestController
@RequestMapping("/admin/revenue")
@CrossOrigin("*")
public class RevenueStatsController {
    @Autowired
    private IRevenueService revenueService;

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/stats")
    public ResponseEntity<?> getRevenueStats(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Map<String, Object> result = new HashMap<>();
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based

        Date queryDate = now;
        if (date != null) {
            cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);
            queryDate = cal.getTime();
            year = date.getYear();
            month = date.getMonthValue();
        }

        Double dayRevenue = revenueService.getTotalRevenueByDay(queryDate);
        Double monthRevenue = revenueService.getTotalRevenueByMonth(year, month);
        Double yearRevenue = revenueService.getTotalRevenueByYear(year);

        // Calculate previous periods
        Calendar prevCal = Calendar.getInstance();
        prevCal.setTime(queryDate);
        // Previous day
        prevCal.add(Calendar.DATE, -1);
        Date prevDay = prevCal.getTime();
        Double prevDayRevenue = revenueService.getTotalRevenueByDay(prevDay);
        // Previous month
        prevCal.setTime(queryDate);
        prevCal.add(Calendar.MONTH, -1);
        int prevMonth = prevCal.get(Calendar.MONTH) + 1;
        int prevMonthYear = prevCal.get(Calendar.YEAR);
        Double prevMonthRevenue = revenueService.getTotalRevenueByMonth(prevMonthYear, prevMonth);
        // Previous year
        prevCal.setTime(queryDate);
        prevCal.add(Calendar.YEAR, -1);
        int prevYear = prevCal.get(Calendar.YEAR);
        Double prevYearRevenue = revenueService.getTotalRevenueByYear(prevYear);

        // Calculate percent changes
        double dayPercent = (prevDayRevenue != 0) ? ((dayRevenue - prevDayRevenue) / prevDayRevenue) * 100 : (dayRevenue > 0 ? 100 : 0);
        double monthPercent = (prevMonthRevenue != 0) ? ((monthRevenue - prevMonthRevenue) / prevMonthRevenue) * 100 : (monthRevenue > 0 ? 100 : 0);
        double yearPercent = (prevYearRevenue != 0) ? ((yearRevenue - prevYearRevenue) / prevYearRevenue) * 100 : (yearRevenue > 0 ? 100 : 0);

        result.put("day", dayRevenue);
        result.put("dayPercent", dayPercent);
        result.put("month", monthRevenue);
        result.put("monthPercent", monthPercent);
        result.put("year", yearRevenue);
        result.put("yearPercent", yearPercent);
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                result,
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chart")
    public ResponseEntity<?> getRevenueChart(
            @RequestParam("type") String type,
            @RequestParam("year") int year,
            @RequestParam(value = "month", required = false) Integer month
    ) {
        Map<String, Object> result = new HashMap<>();
        List<Double> data = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        if ("year".equalsIgnoreCase(type)) {
            for (int m = 1; m <= 12; m++) {
                double total = revenueService.getTotalRevenueByMonth(year, m);
                data.add(total);
                labels.add("Month " + m);
            }
        } else if ("month".equalsIgnoreCase(type) && month != null) {
            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();
            Calendar cal = Calendar.getInstance();
            for (int d = 1; d <= daysInMonth; d++) {
                cal.set(year, month - 1, d, 0, 0, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date date = cal.getTime();
                double total = revenueService.getTotalRevenueByDay(date);
                data.add(total);
                labels.add(String.format("%02d/%02d", d, month));
            }
        }
        result.put("labels", labels);
        result.put("data", data);
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                result,
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chart/category-percent")
    public ResponseEntity<?> getProductCategorySalePercent(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        List<Object> result = orderDetailService.getProductCategorySalePercent(startDate, endDate);
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                result,
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.ok(response);
    }
}

