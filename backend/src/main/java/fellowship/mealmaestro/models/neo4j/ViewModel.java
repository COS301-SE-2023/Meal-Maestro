package fellowship.mealmaestro.models.neo4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("View")
public class ViewModel {

    @Id
    @GeneratedValue
    private Long id;

    private String[] Keys;
    private List<Double> scoreValues;
    private List<Double> nScoreValues;
   
    private Double max = Double.MIN_VALUE;
    private Double min = Double.MAX_VALUE;

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
        this.max = Double.MIN_VALUE;
        this.min = Double.MAX_VALUE;
        for (Double score : scoreValues) {
            if(score < this.min){
                this.min = score;
            }
            if(score > this.max){
                this.max = score;
            }
        }
        HashMap<String, Double> ScoreMap = new HashMap<>();
        HashMap<String, Double> nScoreMap = new HashMap<>();
        for (String ingredient : ScoreMap.keySet()) {
            nScoreMap.put(ingredient, 2 * ((ScoreMap.get(ingredient) - min) / (max - min)) - 1);
        }
        Keys = hashMapKeysToArray(nScoreMap);
        scoreValues = hashMapValuesToArray(ScoreMap);
        nScoreValues = hashMapValuesToArray(nScoreMap);
    }

    public static List<Double> hashMapValuesToArray(HashMap<String, Double> hashMap) {
        List<Double> listFromHashMap = new ArrayList<>();
        for (Map.Entry<String, Double> entry : hashMap.entrySet()) {
            Double value = entry.getValue();
            listFromHashMap.add(value);
        }
        return listFromHashMap;
    }

    public static String[] hashMapKeysToArray(HashMap<String, Double> hashMap) {
        return hashMap.keySet().toArray(new String[0]);
    }

    public static HashMap<String, Double> arraysToHashMap(String[] keys, List<Double> nScoreValues2) {
        if (keys.length != nScoreValues2.size()) {
            throw new IllegalArgumentException("Keys and values arrays must have the same length.");
        }

        HashMap<String, Double> hashMap = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            hashMap.put(keys[i], nScoreValues2.get(i));
        }
        return hashMap;
    }

    public List<String> getPositiveNScores(Double minNValue) throws Exception{
        List<String>Keys = new ArrayList<>();
        if(this.Keys != null && this.Keys.length > 0){
            HashMap<String, Double> nScoreMap = arraysToHashMap(this.Keys, this.nScoreValues);

            for (Map.Entry<String, Double> entry : nScoreMap.entrySet()) {
                if (entry.getValue() >= minNValue) {
                    Keys.add(entry.getKey());
                }
            }

            return Keys;
        }
        else return null;
    }
}
