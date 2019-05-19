// Node klassen.
public class Node {
    private Node childLeft = null;
    private Node childRight = null;
    private String value;
    private int depth;

    // Konstruktoer, get'ere og set'ere.
    public Node(String v) {
         value = v;
    }
    public void setLeft(Node l) {
        childLeft = l;
    }
    public void setRight(Node r) {
        childRight = r;
    }
    public Node getLeft() {
        return childLeft;
    }
    public Node getRight() {
        return childRight;
    }
    public String getValue() {
        return value;
    }
    public void setDepth(int i) {
        depth = i;
    }
    public int getDepth() {
        return depth;
    }

    // Setter inn i et tre.
    public void insert(Node n) {
        // Sammenligner this med n. Hvis this<n, er n stoerre enn this.
        if(this.getValue().compareToIgnoreCase(n.getValue()) < 0) {
            // Hvis this sin hoeyere er null, er den ledig. Settes inn.
            if(this.getRight() == null) {
                n.setDepth(this.getDepth() + 1);
                this.setRight(n);
                return;
            }
            // .. hvis ikke, fortsetter rekursjonen.
            this.getRight().insert(n);
        }
        // Sammenligner this med n. Hvis this>n, er n mindre enn this.
        if(this.getValue().compareToIgnoreCase(n.getValue()) > 0) {
            // Hvis this sin left er null, er den ledig. Settes inn.
            if(this.getLeft() == null) {
                n.setDepth(this.getDepth() + 1);
                this.setLeft(n);
                return;
            }
            // .. hvis ikke, fortsetter rekursjonen.
            this.getLeft().insert(n);
        }
        // Hvis alt over failer, er de like. Da skjer det ingenting.
        return;
    }

    // Soek etter ord.
    public boolean search(String s) {
        if(this.getValue().compareToIgnoreCase(s) < 0) {
            if(this.getRight() == null) {
                return false;
            }
            else {
                return this.getRight().search(s);
            }
        }
        else if(this.getValue().compareToIgnoreCase(s) > 0) {
            if(this.getLeft() == null) {
                return false;
            }
            else {
                return this.getLeft().search(s);
            }
        }
        else {
            return true;
        }
    }
}
