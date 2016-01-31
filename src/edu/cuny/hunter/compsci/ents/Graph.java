package edu.cuny.hunter.compsci.ents;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;


public class Graph {

	public ArrayList<Edge>[] edges;
	private Map<Integer, String> number2Name;
	private Map<String, Integer> name2Number;
	private int numEdges;
	private int numNodesCompleted;
	
	public static final int numReadings = 4;//number of memory readings
		
	/**
	 * Creates a graph with the specifided nodes.
	 * @param nameMapping A map associating the integer IDs of each node in the graph with a human-readable name in the form of a string.
	 */
	public Graph(Map<Integer, String> nameMapping) {
		number2Name = nameMapping;
		edges = new ArrayList[nameMapping.size()];
		numEdges = 0;
		for(int i=0; i<edges.length; i++) {
			edges[i] = new ArrayList<Edge>();
		}
	}
	
	public Map<Integer, String> number2Name() {
		return number2Name;
	}
	
	public void setName2Number(Map<String, Integer> n) {
		name2Number = n;
	}
	
	public Map<String, Integer> name2Number() {
		return name2Number;
	}
	
	/**
	 * Calls the garbage collector, then measures how much memory is being used by the program.
	 * @return the number of bytes currently in use.
	 */
	public static long memoryUsed() {
		Runtime rt = Runtime.getRuntime();
		rt.gc();
		return rt.totalMemory() - rt.freeMemory();
	}
	
	/**
	 * Runs the nondiverse algorithm.
	 * @param source Where the algorithm starts from.
	 * @param k The number of paths to find.
	 * @param func The importance function to use. ImpFunc.IMP1, ImpFunc.IMP2, and ImpFunc.IMP3 contain Imp1-3 from the manuscript.
	 * @param opt Options. "P" to write full paths (instead of just their lengths) to the output file; "S" to create and return a statistics object.
	 * @param resultFile Text file to output the results to.
	 * @return An object of type Statistics containing information on the algorithm's time and memory usage.
	 */
	public Statistics KSPSearch(int source, int k, ImpFunc func, String opt, File resultFile) {
		return KSPSearch(source, k, 0f, func, opt, resultFile);
	}
	
	/**
	 * Runs the nondiverse algorithm using Imp1.
	 * @param source Where the algorithm starts from.
	 * @param k The number of paths to find.
	 * @param func The importance function to use. ImpFunc.IMP1, ImpFunc.IMP2, and ImpFunc.IMP3 contain Imp1-3 from the manuscript.
	 * @param opt Options. "P" to write full paths (instead of just their lengths) to the output file; "S" to create and return a statistics object.
	 * @param resultFile Text file to output the results to.
	 * @return An object of type Statistics containing information on the algorithm's time and memory usage.
	 */
	public Statistics KSPSearch(int source, int k, String opt, File resultFile) {
		return KSPSearch(source, k, 0f, ImpFunc.IMP1, opt, resultFile);
	}
	
	/**
	 * Runs the algorithm with h=23, the empirically determined maximum length of a gene regulatory pathway (Shih et al, 2012).
	 * (Gene regulatory pathway discovery was this algorithm's initial application.)
	 * @param source Where the algorithm starts from.
	 * @param k The number of paths to find.
	 * @param lambda Diversity threshold.
	 * @param func The importance function to use. ImpFunc.IMP1, ImpFunc.IMP2, and ImpFunc.IMP3 contain Imp1-3 from the manuscript.
	 * @param opt Options. "P" to write full paths (instead of just their lengths) to the output file; "S" to create and return a statistics object.
	 * @param resultFile Text file to output the results to.
	 * @return An object of type Statistics containing information on the algorithm's time and memory usage.
	 */
	public Statistics KSPSearch(int source, int k, float lambda, ImpFunc func, String opt, File resultFile) {
		return KSPSearch(source, k, lambda, 23, func, opt, resultFile);
	}
	
	/**
	 * Runs the algorithm with h=23 and the sum-of-reciprocals (Imp1) importance function, the parameters used by Shih et al.
	 * (Gene regulatory pathway discovery was this algorithm's initial application.)
	 * @param source Where the algorithm starts from.
	 * @param k The number of paths to find.
	 * @param lambda Diversity threshold.
	 * @param func The importance function to use. ImpFunc.IMP1, ImpFunc.IMP2, and ImpFunc.IMP3 contain Imp1-3 from the manuscript.
	 * @param opt Options. "P" to write full paths (instead of just their lengths) to the output file; "S" to create and return a statistics object.
	 * @param resultFile Text file to output the results to.
	 * @return An object of type Statistics containing information on the algorithm's time and memory usage.
	 */
	public Statistics KSPSearch(int source, int k, float lambda, String opt, File resultFile) {
		return KSPSearch(source, k, lambda, 23, ImpFunc.IMP1, opt, resultFile);
	}
	
	/**
	 * Runs the algorithm using Imp1.
	 * (Gene regulatory pathway discovery was this algorithm's initial application.)
	 * @param source Where the algorithm starts from.
	 * @param k The number of paths to find.
	 * @param lambda Diversity threshold.
	 * @param func The importance function to use. ImpFunc.IMP1, ImpFunc.IMP2, and ImpFunc.IMP3 contain Imp1-3 from the manuscript.
	 * @param opt Options. "P" to write full paths (instead of just their lengths) to the output file; "S" to create and return a statistics object.
	 * @param resultFile Text file to output the results to.
	 * @return An object of type Statistics containing information on the algorithm's time and memory usage.
	 */
	public Statistics KSPSearch(int source, int k, float lambda, int h, String opt, File resultFile) {
		return KSPSearch(source, k, lambda, h, ImpFunc.IMP1, opt, resultFile);
	}
	
	/**
	 * Run the single source k diverse short paths algorithm.
	 * @param source Where the algorithm starts from.
	 * @param k The number of paths to find.
	 * @param lambda Diversity threshold.
	 * @param h Max number of nodes in a path.
	 * @param func The importance function to use. ImpFunc.IMP1, ImpFunc.IMP2, and ImpFunc.IMP3 contain Imp1-3 from the manuscript.
	 * @param opt Options. "P" to write full paths (instead of just their lengths) to the output file; "S" to create and return a statistics object.
	 * @param resultFile Text file to output the results to.
	 * @return An object of type Statistics containing information on the algorithm's time and memory usage.
	 */
	public Statistics KSPSearch(int source, int k, float lambda, int h, ImpFunc func, String opt, File resultFile) {
		Statistics s = new Statistics();
		
		boolean outputPaths = opt.contains("P") || opt.contains("p");
		boolean statistics = opt.contains("S") || opt.contains("s");
		
		numNodesCompleted = 0;

		NodeData[] data = new NodeData[number2Name.size()];
		for(int i=0; i<data.length; i++) {
			data[i] = new NodeData(k, h, i);
		}
		int[] count = new int[number2Name.size()];
		
		//Statistical things
		int itr = 0;
		long timeToReadMem = 0;
		long heapMax = 0;
		if(statistics) {//Technically, should add this to timeToReadMem, but that would change an independent variable.
			heapMax = memoryUsed();
		}
		long timeStart = System.currentTimeMillis();
		
		do {
			for(int i=0; i<edges.length; i++) {
				for(Edge e : edges[i]) {
					e.edgeCount = 0;
				}
			}
			
			for(int i=0; i<data.length; i++) {
				count[i] = 0;
			}
			
			PriorityQueue<Path> pq = new PriorityQueue<Path>();
			
			Path stub = new Path(null, new Edge(source, source, 0));
			data[source].addToEnd(stub, k);
			pq.add(stub);
			
			int pqIdx = 0;
			int modValue = Math.round( ((float) number2Name.size()*k) / ((float) numReadings) );
			
			while(!pq.isEmpty()) {
				Path path = pq.remove();
//				System.out.println(path);
				//System.out.println(pq.size());
								
				for(Edge edge : edges[path.getFinalHead()]) {
					Path newPath = new Path(path == stub ? null : path, edge);
					if(newPath.numNodes() >= h) break;
					
//					System.out.println("+" + edge + " = " + newPath);
					
					double distance = newPath.getDistance();
					
					if(!path.contains(edge.head) && distance < data[edge.head].longestPathDistance(k)) {
						if(count[edge.head] < k) {
							count[edge.head]++;
						}
						else {
							//pq.remove(data[edge.head].paths[k-1]);
						}

						/*if(!data[edge.head].complete(k))*/ data[edge.head].addToEnd(newPath, k);
						
						pq.add(newPath);
						
						edge.edgeCount++; //Will decrement some edge counts later.
					}
				}
				
				if(statistics) {
					pqIdx++;
					long x = System.currentTimeMillis();
					if(pqIdx % modValue == 0) {
						//System.out.println("reading");
						heapMax = Math.max(heapMax, memoryUsed());
					}
					long y = System.currentTimeMillis();
					timeToReadMem += y-x;
				}
//				System.out.println();
			}

			for(Edge e : edges[source]) {
				e.edgeCount--;
			}
			
			for(int i=0; i<data.length; i++) {
				data[i].diverseFlush(k, lambda);
			}
			
			itr++;
			if(numNodesCompleted >= data.length) {
				break;
			}
			
			int numDeleted = 0;
			for(int i=0; i<edges.length; i++) {
				Iterator<Edge> edgeIt = edges[i].iterator();
				while(edgeIt.hasNext()) {
					Edge e = edgeIt.next();
					if(e.edgeCount >= 0.5*((float)k)) {
						float prob = ((float)e.edgeCount) / ((float)k);
						if(Math.random() < prob) {
//							System.out.println(e + " deleted");
							edgeIt.remove();
							numDeleted++;
							numEdges--;
						}
					}
				}
			}
//			System.out.println("num deleted: "+ numDeleted);
			if(numDeleted < ((float)numEdges) / ((float)number2Name.size())) {
//				System.out.println("Terminating due to m/n clause.");
				break;
			}
		} while(numNodesCompleted < number2Name.size());
		
		for(NodeData node : data) {
			node.calculateImportance(func);
		}
		
		s.time = (System.currentTimeMillis()-timeStart-timeToReadMem)/1000f;
		
		Arrays.sort(data);
						
		try {
			PrintStream output = new PrintStream(new FileOutputStream(resultFile));
			for(int i=data.length-1; i>=0; i--) {
				NodeData node = data[i];
				if(node.getNumber() == source) continue;
				output.println(number2Name.get(node.getNumber()) + "\t" + node.importance());
				if(outputPaths) {
					int j = 1;
					for(Path p : node.diversePaths) {
						output.println(j + ". " + p.toString(number2Name) + " = " + p.getDistance());
						j++;
					}
					output.println();
				}
			}
			output.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
			
		s.maxHeap = heapMax;
		
		numNodesCompleted = 0; //Take only photos, leave only footprints.
		
		return s;
	}
	
	/**
	 * Create a directed edge between two nodes in the graph.
	 * @param start The ID of the node at which the edge will start (the "tail").
	 * @param end The ID of the node to which the edge will point (the "head").
	 * @param length The length of the edge.
	 */
	public void addDirectedEdge(int start, int end, double length) {
		int head = end;
		int tail = start;
		edges[tail].add(new Edge(head, tail, length));
		numEdges++;
	}
	
	/**
	 * Create an undirected edge (represented as two antiparallel directed edges).
	 * @param a An ID of either of the two nodes this edge will connect.
	 * @param b The other node ID.
	 * @param length The length of the edge.
	 */
	public void addUndirectedEdge(int a, int b, double length) {
		addDirectedEdge(a, b, length);
		addDirectedEdge(b, a, length);
	}

	/**
	 * A class that can store the paths from the source node to a given node in the graph.
	 */
	private class NodeData implements Comparable<NodeData> {
		private List<Path> diversePaths;
		private Set<Edge> diverseEdges;
		
		private LinkedList<Path> paths;
		private int node;
		
		private double importance;
		
		public static final boolean STORAGE = false;
		
		/**
		 * Create a NodeData object for a specifided node.
		 * @param k "k" from the algorithm.
		 * @param h "h" from the algorithm.
		 * @param node The ID of the terminal node of the paths this object will store.
		 */
		public NodeData(int k, int h, int node) {
			this.node = node;
			paths = new LinkedList<Path>();
			diversePaths = new ArrayList<Path>();
			if(STORAGE) {
				diverseEdges = new HashSet<Edge>(k*h);
			}
			importance = -1;
		}
		
		/**
		 * @return The node ID.
		 */
		public int getNumber() {
			return node;
		}
		
		/**
		 * Eliminate paths that are not diverse enough.
		 * @param k The number of paths "k" from the algorithm.
		 * @param lambda The diversity threshold.
		 */
		public void diverseFlush(int k, float lambda) {
			if(diversePaths.size() < paths.size() && paths.size() > 0 /*We should have at least one path*/) {
				Set<Edge> visitedEdges;
				if(STORAGE) {
					visitedEdges = diverseEdges;
				}
				else {
					visitedEdges = new TreeSet<Edge>();
					for(Path p : diversePaths) {
						visitedEdges.addAll(p.toArrayList());
					}
				}
				
				for(Path p : paths) {			
					int numDivEdgesRequired = (int) Math.ceil(lambda * p.numEdges());
					int numDivEdgesFound = 0;
					
					boolean addPath = false;
					
					List<Edge> edgeList = p.toArrayList();
					List<Edge> newEdgesSoFar = new LinkedList<Edge>();
					int lastCheckedIndex = -1;
					for(Edge e : edgeList) {
						lastCheckedIndex++;
						if(!visitedEdges.contains(e)) {
							numDivEdgesFound++;
							newEdgesSoFar.add(e);
							
							if(numDivEdgesFound >= numDivEdgesRequired) {
								addPath = true;
								break;
							}
						}
					}
					
					if(addPath) {
						diversePaths.add(p);
						visitedEdges.addAll(newEdgesSoFar);
						for(int i = lastCheckedIndex + 1; i < edgeList.size(); i++) {
							visitedEdges.add(edgeList.get(i));
						}
					}
					
					if(diversePaths.size() == k) {//paths.length is just where we store the k value
						numNodesCompleted++;
						break;
					}
				}
			}
			
			paths.clear();
		}
		
		/**
		 * Finds the longest of the paths to the node.
		 * @param k "k" from the algorithm.
		 * @return the longest path to the node, or infinity if k paths have not yet been found
		 */
		public double longestPathDistance(int k) {
			if(paths.size() < k) return Double.MAX_VALUE;
			else return ((LinkedList<Path>)paths).getLast().getDistance();
		}
		
		/**
		 * @param p The path to be added.
		 * @param lambda The diversity threshold it must fulfill.
		 * @return Whether or not the path was added.
		 */
		public boolean addToEnd(Path p, int k) {
			if(paths.size() == k && p.getDistance() < longestPathDistance(k)) {
				paths.remove(paths.size()-1);
			}
			else if(paths.size() == k && p.getDistance() >= longestPathDistance(k)) {
				return false;
			}
			
			ListIterator<Path> li = paths.listIterator(paths.size());
			do {
				if(!li.hasPrevious()) {
					li.add(p);
					return true;
				}
			} while(li.previous().getDistance() > p.getDistance());
			li.next();
			li.add(p);
			return true;
		}
		
		/**
		 * Determines and stores the importance value of the node based on the path lengths.
		 */
		public void calculateImportance(ImpFunc func) {
			importance = func.calculate(diversePaths);
		}
		
		/**
		 * Returns the importance.
		 * @return The importance value of the node.
		 */
		public double importance() {
			return importance;
		}
		
		/**
		 * Determines which of the two nodes is more important.
		 */
		@Override
		public int compareTo(NodeData other) {
			return new Double(importance()).compareTo(other.importance());
		}
	}
}
