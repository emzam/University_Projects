// Jeg har valgt aa lage en helt egen klasse for noder.
public class Node<T> {
	private Node<T> neste;
	private Node<T> forrige;
	private T nodeInnhold;

	// To Konsruktoerer for Node. En for Node med innhold, og en for uten.
	Node(T nodeInnhold){
		this.nodeInnhold = nodeInnhold;
	}

	Node() {
		this(null);
	}

	// Setter neste Node.
	public void settNeste(Node<T> neste){
		this.neste = neste;
	}

	// Setter forrige Node.
	public void settForrige(Node<T> forrige){
		this.forrige = forrige;
	}

	// Returnerer neste Node.
	public Node<T> hentNeste(){
		return neste;
	}

	// Returnerer forrige Node. (Innholdet at den er sagt)
	public Node<T> hentForrige(){
		return forrige;
	}

	// Returnerer denne noden sitt innhold.
	public T hentInnhold(){
		return nodeInnhold;
	}

	// Setter en Node inn mellom to andre noder.
	public void settInnMellom(Node<T> venstre, Node<T> hoyre){
		venstre.settNeste(this);
		hoyre.settForrige(this);

		settNeste(hoyre);
		settForrige(venstre);
	}

	// Koble ut "sletter" en node.
	public void kobleUt(){
		this.neste.settForrige(this.forrige);
		this.forrige.settNeste(this.neste);

		this.neste = null;
		this.forrige = null;
	}
}
