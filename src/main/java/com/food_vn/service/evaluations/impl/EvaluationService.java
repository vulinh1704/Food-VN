package com.food_vn.service.evaluations.impl;

import com.food_vn.lib.base_serivce.BaseService;
import com.food_vn.lib.error_message.ERROR_MESSAGE;
import com.food_vn.model.evaluates.Evaluation;
import com.food_vn.model.products.Product;
import com.food_vn.repository.evaluation.EvaluationRepository;
import com.food_vn.repository.products.ProductRepository;
import com.food_vn.service.evaluations.IEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService extends BaseService implements IEvaluationService {
    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Evaluation save(Evaluation evaluation) throws Exception {
        this.isAssert(evaluation.getUser() != null, ERROR_MESSAGE.DATA_NOT_FOUND);
        this.isAssert(evaluation.getScore() >= 0 && evaluation.getScore() <= 5, ERROR_MESSAGE.INVALID_SCORE);
        Evaluation output = this.evaluationRepository.save(evaluation);
        Product product = productRepository.findById(output.getProduct().getId())
                .orElseThrow(() -> new Exception(ERROR_MESSAGE.DATA_NOT_FOUND));
        double averageScore = this.getAverageScoreByProductId(output);
        product.setScore(averageScore);
        productRepository.save(product);
        return output;
    }

    @Override
    public List<Evaluation> findAllByProduct_Id(Long productId) {
        this.isAssert(this.productRepository.existsById(productId), ERROR_MESSAGE.DATA_NOT_FOUND);
        return this.evaluationRepository.findAllByProduct_Id(productId);
    }

    public List<Evaluation> findTop10Newest() {
        return evaluationRepository.findTop10ByOrderByIdDesc();
    }

    private double getAverageScoreByProductId(Evaluation evaluation) {
        Long productId = evaluation.getProduct().getId();
        this.isAssert(this.productRepository.existsById(productId), ERROR_MESSAGE.DATA_NOT_FOUND);
        List<Evaluation> evaluations = this.evaluationRepository.findAllByProduct_Id(productId);
        if (evaluations.isEmpty()) return 5.0;
        double sum = evaluations.stream().mapToDouble(Evaluation::getScore).sum() + 5;
        return sum / (evaluations.size() + 1);
    }
}
