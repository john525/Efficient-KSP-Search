package edu.cuny.hunter.compsci.ents;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Creates random graphs to test the algorithm's speed/memory use.
 */
public class TestDataGenerator {
	
	public static final int[] sizes = {10000, 50000, 250000, 1000000};
	public static final int[] avgDegrees = {5, 10, 20};
	public static final Random rand = new Random();
	
	void randomize(File f, int size, int deg) throws IOException {
		//true = autoflush
		PrintStream output = new PrintStream(new FileOutputStream(f), true);
		
		String header = size + " 0 1";
		output.println(header);

		ArrayList<Integer>[] edges = new ArrayList[size];
		for(int i=0; i<edges.length; i++) {
			edges[i] = new ArrayList<Integer>();
		}
		long totalNumEdges = 0;
		String x;
		while (((float)2*totalNumEdges)/((float)size) < deg) {
			int a = rand.nextInt(size);
			int b = rand.nextInt(size-1);
			if(b >= a) b++;
			if(edges[a].contains(b)) {
				continue;
			}
			else {
				edges[a].add(b);
				x = a + " " + b + " " + (1.0-Math.random());
				output.println(x);
				totalNumEdges++;
			}
		}
		
		output.close();
	}
	
	void deleteFolder(File f) {
		if(f.isDirectory()) {
			for(File subFile : f.listFiles()) {
				deleteFolder(subFile);
			}
			f.delete();
		}
		else {
			f.delete();
		}
	}
	
	public void go() {
		
		//heapA = ExecuteInfo.memoryUsed();
		
		File testFolder = new File("test_data");
		if(testFolder.exists()) {
			deleteFolder(testFolder);
		}
		testFolder.mkdir();
				
		int count = 0;
		
		for(int size: sizes) {
			for(int deg : avgDegrees) {
				File graphFolder = new File(testFolder, "n="+size+", deg="+deg);
				if(!graphFolder.exists()) {
					graphFolder.mkdir();
				}
				File geneList = new File(graphFolder, "allProteins");
				File graphList = new File(graphFolder, "allgraphs_all");
				File unknownCausal = new File(graphFolder, "G0_G1");
				File unknownTarget = new File(graphFolder, "G0_G1_inv");
				BufferedWriter writer;
				
				try {
					writer = new BufferedWriter(new FileWriter(geneList));
					for(int geneNum=0; geneNum<size; geneNum++) {
						writer.write("G"+geneNum+"\n");
					}
					writer.flush();
					writer.close();
					
					writer = new BufferedWriter(new FileWriter(graphList));
					writer.write("Target_gene Target_gene_num Causal_gene Causal_gene_num\n");
					writer.write("G0 0 G1 1");
					writer.flush();
					writer.close();
					
					randomize(unknownCausal, size, deg);
					randomize(unknownTarget, size, deg);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				count++;
				System.out.println(count+"/"+sizes.length*avgDegrees.length+" graphs generated.");
			}
		}
		System.out.println("Done making files!");
	}		
}