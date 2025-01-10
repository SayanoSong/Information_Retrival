import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryImp {
    private int id;
    private String title;
    private String description;
    private String narrative;
    private ArrayList<String> tokens;
    public HashMap<String, Double> tokenSet;
    public Map<String, Double> rank;
    public double length;

    public QueryImp() {
        this.id = -1;
        this.title = "";
        this.description = "";
        this.tokens = new ArrayList<String>();
        tokenSet = new HashMap<String, Double>();
        rank = new HashMap<String, Double>();
        length = 0.0;
    }

    public QueryImp(int id, String title, String description, String narrative, ArrayList<String> tokens) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.narrative = narrative;
        this.tokens = tokens;
        tokenSet = new HashMap<String, Double>();
        rank = new HashMap<String, Double>();
        length = 0.0;
    }

    public int getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return description;
    }

    public String getNarrative() {
        return narrative;
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String description) {
        this.description = description;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }
}
