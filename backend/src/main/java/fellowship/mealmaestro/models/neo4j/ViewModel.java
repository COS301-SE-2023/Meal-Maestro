package fellowship.mealmaestro.models.neo4j;

import java.util.HashMap;
import org.springframework.data.neo4j.core.schema.Node;

@Node("View")
public class ViewModel {

    private HashMap<String,Double> ScoreMap = new HashMap<>();

    public ViewModel() {
    }

    public ViewModel(HashMap<String,Double> ScoreMap) {
        this.ScoreMap = ScoreMap;
    }

    public HashMap<String,Double> getScoreMap() {
        return this.ScoreMap;
    }

    public void setScoreMap(HashMap<String,Double> ScoreMap) {
        this.ScoreMap = ScoreMap;
    }

   
}
