
public enum Algorithm {
	DFS("DFS"), BFS("BFS"), IDDFS("IDDFS"), GGS("GGS"), A_STAR("A*"), IDA_STAR("IDA*");
	
    private String codename;

    Algorithm(String string) {
        this.codename = string;
    }

    public String getCodename() {
        return codename;
    }
}
