package com.food_vn.service.evaluations;

import com.food_vn.model.evaluates.Evaluation;
import java.util.List;

public interface IEvaluationService {
    Evaluation save(Evaluation evaluation) throws Exception;

    List<Evaluation> findAllByProduct_Id(Long productId);

    List<Evaluation> findTop10Newest();
}
