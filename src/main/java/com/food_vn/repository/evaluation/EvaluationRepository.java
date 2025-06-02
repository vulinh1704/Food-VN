package com.food_vn.repository.evaluation;
import com.food_vn.model.evaluates.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findAllByProduct_Id(Long productId);
    List<Evaluation> findTop10ByOrderByIdDesc();
    int countByUserIdAndProductId(Long userId, Long productId);
}

