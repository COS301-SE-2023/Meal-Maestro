package fellowship.mealmaestro.models.neo4j;

import java.util.HashMap;
import org.springframework.data.neo4j.core.schema.Node;
import lombok.Data;

@Node("View")
public class ViewModel {
    @Data
    private class Scores {
        public Double score;
        public Double nScore;
    }

    private HashMap<String, Scores> ScoreMap = new HashMap<>();
    private Double max;
    private Double min;

    public ViewModel() {
    }

    public ViewModel(HashMap<String, Scores> ScoreMap) {
        this.ScoreMap = ScoreMap;
    }

    public HashMap<String, Scores> getScoreMap() {
        return this.ScoreMap;
    }

    public void setScoreMap(HashMap<String, Scores> ScoreMap) {
        this.ScoreMap = ScoreMap;
    }

    public void updateScore(String ingredient, Double Score) {
        if (ScoreMap.containsKey(ingredient)) {
            Boolean changed = false;
            Scores scores = ScoreMap.get(ingredient);
            scores.score += Score;
            if (Score > max) {
                max = Score;
                changed = true;
            }

            if (Score < min) {
                min = Score;
                changed = true;
            }
            scores.nScore = normalise(scores.score);
            if (changed) {
                normalise();
            }

        } else {
            Scores scores = new Scores();
            scores.score = Score;
            scores.nScore = 0.0;
            ScoreMap.put(ingredient, scores);
        }
    }

    public Double normalise(Double Score) {
        return 2 * ((Score - min) / (max - min)) - 1;

    }

    public void normalise() {
        // return 2 * ((Score - min) / (max - min)) - 1;
        for (Scores scores : ScoreMap.values()) {
            scores.nScore = 2 * ((scores.score - min) / (max - min)) - 1;
        }
    }

}
