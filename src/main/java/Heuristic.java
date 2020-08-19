
public enum Heuristic {
	CLOSEST_GOAL("CLOSEST_GOAL"),
    CLOSEST_BOX("CLOSEST_BOX"),
    BOXES_REMAINING("BOXES_REMAINING"),
    MIX_1_2("MIX_1_2"),
    MIX_1_3("MIX_1_3"),
    MIX_2_3("MIX_2_3"),
    ALL_MIXED("ALL_MIXED"),;
	
    private String codename;

	Heuristic(String string) {
        this.codename = string;
    }

    public String getCodename() {
        return codename;
    }
}
