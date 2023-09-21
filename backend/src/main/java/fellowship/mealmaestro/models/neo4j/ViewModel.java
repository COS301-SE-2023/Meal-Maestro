package fellowship.mealmaestro.models.neo4j;

import java.util.HashMap;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.Data;

@Node("View")
public class ViewModel {
    @Data
    private class StatValues {
        public Double Score;
        public Double minScore;
        public Double maxScore;
    }

    private HashMap<String,StatValues> ScoreMap = new HashMap<>();

    public ViewModel() {
    }

    public ViewModel(HashMap<String,StatValues> ScoreMap) {
        this.ScoreMap = ScoreMap;
    }

    public HashMap<String,StatValues> getScoreMap() {
        return this.ScoreMap;
    }

    public void setScoreMap(HashMap<String,StatValues> ScoreMap) {
        this.ScoreMap = ScoreMap;
    }
    
    public void updateScore(String ingredient, Double Score){

    }

}
