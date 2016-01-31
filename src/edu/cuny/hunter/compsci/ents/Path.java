package edu.cuny.hunter.compsci.ents;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Path implements Comparable<Path> {

	Path prefix;
	Edge edge;

	int size;
	double distance;
	
//	int maxNodeID, minNodeID;
	
	/**
	 * Creates a new path.
	 * @param prefix The path from which this is based.
	 * @param edge One additional edge to add to the end of the new path.
	 */
	public Path(Path prefix, Edge edge) {
		this.prefix = prefix;
		this.edge = edge;
		size = -1;
		
		if(prefix != null) {
			size = prefix.size + 1;
		}
		
//		maxNodeID = -1;
//		minNodeID = Integer.MAX_VALUE;
//		if(prefix != null) {
//			maxNodeID = Math.max(maxNodeID, prefix.maxNodeID);
//			minNodeID = Math.min(minNodeID, prefix.minNodeID);
//		}
//		if(edge != null) {
//			maxNodeID = Math.max(maxNodeID, Math.max(edge.head, edge.tail));
//			minNodeID = Math.min(minNodeID, Math.min(edge.head, edge.tail));
//		}
//		
		distance = -1;
		if(prefix != null && edge != null) {
			distance = prefix.distance + edge.length;
		}
	}

	//empty path constructor
	public Path() {
		this(null, null);
	}

	public double getDistance() {
		if(distance != -1) return distance;

		if(prefix == null && edge == null) {
			return Double.MAX_VALUE;
		}

		double result = 0;
		if(prefix != null) result += prefix.getDistance();
		if(edge != null) result += edge.length;
		distance = result;
		return result;
	}
	
	/**
	 * Returns all the edges in the path.
	 * @return An ordered list of edges in the path.
	 */
	public ArrayList<Edge> toArrayList() {
		ArrayList<Edge> result = new ArrayList<Edge>();
		if(prefix != null) {
			result.addAll(prefix.toArrayList());
		}
		if(edge != null) {
			result.add(edge);
		}
		return result;
	}
	
	/**
	 * Tells you how large the path is.
	 * @return The number of edges in the path.
	 */
	public int numEdges() {
		return numNodes() - 1;
	}
	
	/**
	 * Determines if the path is empty.
	 * @return True if the path is empty, false if it has any edges.
	 */
	public boolean emptyPath() {
		return numNodes() == 0;
	}

	/**
	 * Determines how many nodes are in the path. Normally, this is stored
	 * and computed in the constructor, but if this is not the case, the value
	 * will be computed recursively.
	 * @return The number of nodes in the path (the number of edges + 1).
	 */
	public int numNodes() {
		if(size != -1) return size;

		int result = 0;
		if(prefix != null) {
			result += prefix.numNodes();
		}
		else if(prefix == null && edge != null) {
			result++;//add on the tail
		}

		if(edge != null) {
			if(edge.head == edge.tail) {
				return 1; //stub (see main algorithm)
			}
			else {
				result++;//add on this head.
			}
		}
		size = result;
		return result;
	}
	
	/**
	 * Tells you whether the path passes through a given node.
	 * @param node The specified node.
	 * @return True if the path passes through the node, false otherwise.
	 */
	public boolean contains(int node) {
//		if(node > maxNodeID || node < minNodeID) return false;
		
		if(prefix == null) {
			return edge.head == node || edge.tail == node;//Base case.
		}
		else if(edge != null) {
			if(edge.head == node) {
				return true;
			}
		}

		//We know the prefix isn't null because of the first if clause.
		return prefix.contains(node);
	}

//	public boolean contains(Edge e) {
//		if(e.length > maxEdgeLength) return false;
//		
//		if(edge != null) {
//			if(edge.equals(e)) {
//				return true;
//			}
//		}
//		if(prefix != null) {
//			return prefix.contains(e);
//		}
//		return false;
//	}
	
	/**
	 * Creates a human-readable representation of the path.
	 * @param number2Name A map from the integer codes representing individual nodes and the
	 * actual names associated with those nodes.
	 * @return The ordered list of names of nodes in the path, separated by the character ">".
	 */
	public String toString(Map<Integer, String> number2Name) {
		if(edge != null && prefix == null) {
			return number2Name.get(edge.tail) + ">" + number2Name.get(edge.head);
		}
		else if(edge != null && prefix != null) {
			return prefix.toString(number2Name) + ">" + number2Name.get(edge.head);
		}
		else if(edge == null && prefix != null) {
			return prefix.toString(number2Name);
		}
		else {
			return "[empty path]";
		}
	}
	
	/**
	 * Determines which of the two paths is shorter.
	 * @return +1 if this path is longer, -1 if it is shorter, 0 if they are the same length.
	 */
	@Override
	public int compareTo(Path p) {
		return new Double(this.getDistance()).compareTo(new Double(p.getDistance()));
	}
	
	/**
	 * @return The integer code representing the node at the very end of the list.
	 */
	public int getFinalHead() {
		if(edge != null) {
			return edge.head;
		}
		else {
			return prefix.getFinalHead();
		}
	}
	
	/**
	 * Determines if two paths are identical.
	 * @return True if they are the same.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof Path) {
			Path p = (Path) o;
			
			if(size != p.size || distance != p.distance) return false;
			
			if(edge != null && p.edge != null) {
				if(edge.head != p.edge.head) return false;
			}
			if(prefix == null && p.prefix == null) {
				if(edge != null && p.edge != null) {
					if(edge.equals(p.edge)) return true;
					else return false;
				}
				else if (edge == null && p.edge == null) return true;
				else if(edge == null || p.edge == null) return false;
			}
			else if(prefix == null || p.prefix == null) return false;//They're not both null, so if either is null we've found a discrepancy.
			else if(!prefix.equals(p.prefix)) return false;
			return true;
		}
		else return false;
	}

}