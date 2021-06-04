package Model;

import java.util.List;

public class ReglaGLC {

    private String id;
    private List<List<ReglaGLC>> rule;

    public ReglaGLC(String id, List<List<ReglaGLC>> rule) {
        this.id = id;
        this.rule = rule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<List<ReglaGLC>> getRule() {
        return rule;
    }

    public void setRule(List<List<ReglaGLC>> rule) {
        this.rule = rule;
    }
}
