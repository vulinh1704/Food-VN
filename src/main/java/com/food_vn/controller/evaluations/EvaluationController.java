package com.food_vn.controller.evaluations;
import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.model.evaluates.Evaluation;
import com.food_vn.service.evaluations.IEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private IEvaluationService evaluationService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Evaluation evaluation) throws Exception {
        Evaluation output = evaluationService.save(evaluation);
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                output,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getByProduct(@PathVariable Long productId) {
        List<Evaluation> evaluations = evaluationService.findAllByProduct_Id(productId);
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                evaluations,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestEvaluations() {
        List<Evaluation> evaluations = evaluationService.findTop10Newest();
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                evaluations,
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
