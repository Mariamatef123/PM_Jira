package com.misrshoryanelataa.misr_shoryan_elataa.Controllers;

import com.misrshoryanelataa.misr_shoryan_elataa.DTO.MedicalRequest;
import com.misrshoryanelataa.misr_shoryan_elataa.DTO.ValidationResponse;
import com.misrshoryanelataa.misr_shoryan_elataa.Services.MedicalValuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MedicalValuesController {
    @Autowired
    private MedicalValuesService medService;

    @PostMapping("/validate")
    public ValidationResponse validate(@RequestBody MedicalRequest request) {
        return medService.validate(request.getAnswers());
    }
    @GetMapping("/questions/titles")
    public List<String> getQuestionTitles() {
        return medService.getAllQuestionTitles();
    }

        @GetMapping("/questions")
    public Object getQuestion() {
        return medService.getAllQuestion();
    }
}
