package com.food_vn.service.revenue.iml;

import com.food_vn.model.revenue.Revenue;
import com.food_vn.repository.orders.OrdersRepository;
import com.food_vn.repository.revenue.RevenueRepository;
import com.food_vn.service.revenue.IRevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class RevenueService implements IRevenueService {
    @Autowired
    private RevenueRepository revenueRepository;
    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public void saveRevenueForDate(Date date, Double orderTotal) {
        revenueRepository.findByDate(date)
                .ifPresentOrElse(
                        revenue -> {
                            revenue.setTotal(revenue.getTotal() + orderTotal);
                            revenueRepository.save(revenue);
                        },
                        () -> revenueRepository.save(new Revenue(date, orderTotal))
                );
    }

    @Override
    public Double getTotalRevenueByDay(Date date) {
        return revenueRepository.getTotalRevenueByDay(date);
    }

    @Override
    public Double getTotalRevenueByMonth(int year, int month) {
        return revenueRepository.getTotalRevenueByMonth(year, month);
    }

    @Override
    public Double getTotalRevenueByYear(int year) {
        return revenueRepository.getTotalRevenueByYear(year);
    }

}
