package com.misrshoryanelataa.misr_shoryan_elataa.Services;

import com.misrshoryanelataa.misr_shoryan_elataa.DTO.ValidationResponse;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.MedicalValuesEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.MedicalValuesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MedicalValuesService {
    @Autowired
    private MedicalValuesRepo medRepo;

    private boolean shouldShow(MedicalValuesEntity q, Map<Integer, String> answers) {

        if (q.getDependsOnQuestionId() == null)
            return true;

        String parentAnswer = answers.get(q.getDependsOnQuestionId());

        return parentAnswer != null &&
                parentAnswer.equalsIgnoreCase("Yes");
    }

    private String convertArabicToWestern(String input) {
    if (input == null) return null;
    StringBuilder sb = new StringBuilder();
    for (char c : input.toCharArray()) {
        if (c >= '\u0660' && c <= '\u0669') {
            // Arabic-Indic digits ٠١٢٣٤٥٦٧٨٩
            sb.append((char) (c - '\u0660' + '0'));
        } else if (c >= '\u06F0' && c <= '\u06F9') {
            // Extended Persian digits ۰۱۲۳۴۵۶۷۸۹
            sb.append((char) (c - '\u06F0' + '0'));
        } else {
            sb.append(c);
        }
    }
    return sb.toString();
}
    public ValidationResponse validate(Map<Integer, String> answers) {

        List<MedicalValuesEntity> questions =
                medRepo.findAll(Sort.by("id"));

        for (MedicalValuesEntity q : questions) {

            if (!shouldShow(q, answers)) {
                continue;
            }

            String answer = answers.get(q.getId());
            if (answer == null) {
                return new ValidationResponse(false,
                        "Missing answer for: " + q.getTitle());
            }

            switch (q.getType()) {

              case NUMBER:
    try {
        String normalizedAnswer = convertArabicToWestern(answer);
        double value = Double.parseDouble(normalizedAnswer);
        if (q.getMinValue() != null && value < q.getMinValue()) {
            return new ValidationResponse(false,
                    "نأسف، لا يمكنك التبرع حالياً لأنك لا تستوفي شروط التبرع.");
        }
        if (q.getMaxValue() != null && value > q.getMaxValue()) {
            return new ValidationResponse(false,
                    "نأسف، لا يمكنك التبرع حالياً لأنك لا تستوفي شروط التبرع.");
        }
    } catch (NumberFormatException e) {
        return new ValidationResponse(false,
                q.getTitle() + " must be a number");
    }
    break;
                case YES_NO:


                    if (!answer.equalsIgnoreCase("Yes") &&
                            !answer.equalsIgnoreCase("No")) {

                        return new ValidationResponse(false,
                                q.getTitle() + " must be Yes or No");
                    }
                    if (answer.equalsIgnoreCase("Yes") &&
                            Boolean.TRUE.equals(q.getYesIsInvalid())) {

                        return new ValidationResponse(false,
                                "نأسف، لا يمكنك التبرع حالياً لأنك لا تستوفي شروط التبرع.");
                    }

                    break;
            }
        }

        return new ValidationResponse(true,
                "You are eligible to donate");
    }
    public List<String> getAllQuestionTitles() {

        return medRepo.findAll(Sort.by("id"))
                .stream()
                .map(MedicalValuesEntity::getTitle)
                .toList();
    }

    public Object getAllQuestion() {
             return medRepo.findAll(Sort.by("id"))
                .stream()
                .toList();
    }
}
