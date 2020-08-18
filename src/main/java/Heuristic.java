
public enum Heuristic {
	CLOSEST_GOAL("CLOSEST_GOAL"), CLOSEST_BOX("CLOSEST_BOX"), BOXES_REMAINING("BOXES_REMAINING");
	
    private String codename;

	Heuristic(String string) {
        this.codename = string;
    }

    public String getCodename() {
        return codename;
    }
}
