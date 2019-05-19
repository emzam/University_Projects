import java.util.ArrayList;
import java.util.List;

public class Task {
    private int id, time, staff, slack;
    private String name;
    private int earliestStart, latestStart;
    private List<Task> outEdges;
    private List<Task> inEdges;
    private ArrayList<Integer> inEdgesInt;
    private int cntPredecessors;

// ***************************************************************************
// Konstruktoer
    public Task(int id, String name, int time, int staff,
    ArrayList<Integer> inEdgesInt) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.staff = staff;
        this.inEdgesInt = inEdgesInt;
        this.outEdges = new ArrayList<Task>();
        this.inEdges = new ArrayList<Task>();
        cntPredecessors = inEdgesInt.size();
        // Foreloepige startverider.
        earliestStart = 0;
        latestStart = Integer.MAX_VALUE;
    }

// ****************************************************************************
// Gettere og settere.
    public int getID() {
        return id;
    }
    public int getTime() {
        return time;
    }
    public int getStaff() {
        return staff;
    }
    public String getName() {
        return name;
    }
    public int getEarliestStart() {
        return earliestStart;
    }
    public int getLateststart() {
        return latestStart;
    }
    public int getCntpredecessors() {
        return cntPredecessors;
    }
    public List<Task> getOutedges() {
        return outEdges;
    }
    public List<Task> getInedges() {
        return inEdges;
    }
    public ArrayList<Integer> getInedgesInt() {
        return inEdgesInt;
    }
    public int getSlack() {
        if(slack > 0) {
            return slack;
        }
        else {
            return 0;
        }
    }
    public void setSlack(int i) {
        slack = i;
    }
    public void addOutedge(Task t) {
        outEdges.add(t);
    }
    public void addInedge(Task t) {
        inEdges.add(t);
    }
    public void setLatest(int i) {
        latestStart = i;
    }

// ****************************************************************************
// Metode som fikser earliest for alle noder
    public void setEarlieststart(int t) {
        if(t > earliestStart) {
            earliestStart = t;
        }

        for(Task task : outEdges) {
            task.setEarlieststart(earliestStart + time);
        }
    }
}
