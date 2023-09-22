package fellowship.mealmaestro.models.neo4j;

import java.util.HashMap;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import lombok.Data;

@Node("View")
public class ViewModel {

    @Id
    @GeneratedValue
    private Long id;

    private HashMap<String, Double> ScoreMap = new HashMap<>();
    private HashMap<String, Double> nScoreMap = new HashMap<>();
    private Double max;
    private Double min;

    public ViewModel() {
    }

    public ViewModel(HashMap<String, Double> ScoreMap) {
        this.ScoreMap = ScoreMap;
    }

    public HashMap<String, Double> getScoreMap() {
        return this.ScoreMap;
    }

    public void setScoreMap(HashMap<String, Double> ScoreMap) {
        this.ScoreMap = ScoreMap;
    }

    public void updateScore(String ingredient, Double Score) {
        if (ScoreMap.containsKey(ingredient)) {
            Boolean changed = false;
            Double score = ScoreMap.get(ingredient);
            Double nScore = nScoreMap.get(ingredient);
            score += Score;
            if (Score > max || max == null) {
                max = Score;
                changed = true;
            }

            if (Score < min || min == null) {
                min = Score;
                changed = true;
            }
            nScore = normalise(Score);
            if (changed) {
                normalise();
            }

        } else {
            
            ScoreMap.put(ingredient, Score);
            nScoreMap.put(ingredient,normalise(Score));
        }
    }

    public Double normalise(Double Score) {
        return 2 * ((Score - min) / (max - min)) - 1;

    }

    public void normalise() {
        // return 2 * ((Score - min) / (max - min)) - 1;
        for (String ingredient : ScoreMap.keySet()) {
            nScoreMap.put(ingredient, 2 * ((ScoreMap.get(ingredient) - min) / (max - min)) - 1) ;
        }
    }

}
