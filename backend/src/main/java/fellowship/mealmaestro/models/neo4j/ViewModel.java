package fellowship.mealmaestro.models.neo4j;

import java.util.HashMap;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("View")
public class ViewModel {

    @Id
    @GeneratedValue
    private Long id;

    String[] Keys;
    Double[] scoreValues;
    Double[] nScoreValues;
   
    private Double max =0.0;
    private Double min =0.0;

    public ViewModel() {
    }


    public HashMap<String, Double> getScoreMap() {
        return arraysToHashMap(Keys, nScoreValues);
    }


    public void updateScore(String ingredient, Double Score) {
        HashMap<String, Double> ScoreMap = new HashMap<>();
        HashMap<String, Double> nScoreMap = new HashMap<>();
        
        if(Keys != null){
         ScoreMap = arraysToHashMap(Keys, scoreValues);
         nScoreMap = arraysToHashMap(Keys, nScoreValues);
        }
       
        
        if (ScoreMap.containsKey(ingredient)) {
            Boolean changed = false;
            Double score = ScoreMap.get(ingredient);
            Double nScore = nScoreMap.get(ingredient);
            score += Score;
            if (score > max || max == null) {
                max = score;
                changed = true;
            }

            if (score < min || min == null) {
                min = score;
                changed = true;
            }
            nScore = normalise(Score);
            if (changed) {
                normalise();
            }

            ScoreMap.put(ingredient, score);
            nScoreMap.put(ingredient, nScore);

        } else {

            ScoreMap.put(ingredient, Score);
            nScoreMap.put(ingredient, normalise(Score));
        }
        Keys = hashMapKeysToArray(nScoreMap);
        scoreValues = hashMapValuesToArray(ScoreMap);
        nScoreValues = hashMapValuesToArray(nScoreMap);
    }

    public Double normalise(Double Score) {
        return 2 * ((Score - min) / (max - min)) - 1;

    }

    public void normalise() {
        // return 2 * ((Score - min) / (max - min)) - 1;
        HashMap<String, Double> ScoreMap = new HashMap<>();
        HashMap<String, Double> nScoreMap = new HashMap<>();
        for (String ingredient : ScoreMap.keySet()) {
            nScoreMap.put(ingredient, 2 * ((ScoreMap.get(ingredient) - min) / (max - min)) - 1);
        }
        Keys = hashMapKeysToArray(nScoreMap);
        scoreValues = hashMapValuesToArray(ScoreMap);
        nScoreValues = hashMapValuesToArray(nScoreMap);
    }

    public static Double[] hashMapValuesToArray(HashMap<String, Double> hashMap) {
        return hashMap.values().toArray(new Double[0]);
    }

    public static String[] hashMapKeysToArray(HashMap<String, Double> hashMap) {
        return hashMap.keySet().toArray(new String[0]);
    }

    public static HashMap<String, Double> arraysToHashMap(String[] keys, Double[] values) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Keys and values arrays must have the same length.");
        }

        HashMap<String, Double> hashMap = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            hashMap.put(keys[i], values[i]);
        }
        return hashMap;
    }

   
}
