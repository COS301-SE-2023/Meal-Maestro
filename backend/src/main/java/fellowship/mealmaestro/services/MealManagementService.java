package fellowship.mealmaestro.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import fellowship.mealmaestro.models.MealModel;

@Service
public class MealManagementService {

    @Autowired
    private OpenaiApiService openaiApiService;
    @Autowired
    private ObjectMapper objectMapper;

    public MealModel generateMeal(String mealType, String token) {
        try {
            JsonNode mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse(mealType, token));
            int i = 0;
            if (!validate(mealJson)) {
                for (i = 0; i < 4; i++) {
                    mealJson = objectMapper.readTree(openaiApiService.fetchMealResponse(mealType, token));
                    if (validate(mealJson))
                        break;
                }
                return null;
            }
            ObjectNode mealObject = objectMapper.valueToTree(mealJson);
            mealObject.put("type", mealType);
            mealObject.put("image", ""); // TODO: Add image

            MealModel mealModel = objectMapper.treeToValue(mealObject, MealModel.class);
            return mealModel;
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean validate(JsonNode data) {
        try {
            File schemaFile = new File("src\\main\\resources\\MealSchema.json");
            JsonNode schemaNode = objectMapper.readTree(schemaFile);

            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonSchema schema = factory.getJsonSchema(schemaNode);

            ProcessingReport report = schema.validate(data);

            return report.isSuccess();

        } catch (ProcessingException e) {
            System.out.println("Error validating meal schema");
            return false;
        } catch (IOException e) {
            System.out.println("Error reading meal schema file");
            System.out.println(e.getMessage());
            return false;
        }
    }

}
