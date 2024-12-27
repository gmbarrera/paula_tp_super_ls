package Persistance;

import Business.Stats.Stats;
import org.json.JSONException;

import java.util.List;

public interface StatsDAO {
    List<Stats> getStats();
    void saveStats(List<Stats> statsList) throws JSONException;
}
