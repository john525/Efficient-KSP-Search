package edu.cuny.hunter.compsci.ents;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
//import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;

/**
 * These methods were used to test the algorithms in Graph.
 * Graph.java contains the actual KDSP algorithm.
 */
public class Main {
	
	public static Set<String> PROT_IGNORE;
	
	static {
		if(new File("D:\\ENTS_results_logged_edges").exists()) PROT_IGNORE = proteinsToIgnore();
	}
	
	/**
	 * 
	 * @param args
	 * <ul>
	 * <li>args[0] is the graph file.</li>
	 * <li>args[1] is the name of the source node.</li>
	 * <li>args[2] is k, the number of paths.</li>
	 * <li>args[3] is lambda, the diversity threshold.</li>
	 * <li>args[4] is h, the maximum path length.</li>
	 * <li>args[5] is the result file, to which the results of graph search will be written.</li>
	 * <li>args[6] is the edge score desired -- either the RLS or NLS of the similarity value found in the file.</li>
	 * <li>args[7] is the importance function used, either "Imp1," "Imp2," or "Imp3."</li>
	 * </ul>args[8] is remaining options: P to print full paths in the result file, S to perform statistical analysis of the algorithm's time/space usage, SP for both or blank for neither.</li>
	 */
	public static void main(String[] args) {
		if(args.length > 0) {
			File graph = new File(args[0]);
			Graph g;
			try {
				g = makeGraph(graph, args[6]);
				if(!g.name2Number().containsKey(args[1])) {
					System.out.println("Invalid source node; the name is not listed in the graph file.");
					System.exit(1);
				}
				int source = g.name2Number().get(args[1]);
				int k = Integer.parseInt(args[2]);
				float lambda = Float.parseFloat(args[3]);
				int h = Integer.parseInt(args[4]);
				File resultFile = new File(args[5]);
				ImpFunc func = null;
				if(args[7].toUpperCase().equals("IMP1")) {
					func = ImpFunc.IMP1;
				}
				else if(args[7].toUpperCase().equals("IMP2")) {
					func = ImpFunc.IMP2;
				}
				else if(args[7].toUpperCase().equals("IMP3")) {
					func = ImpFunc.IMP3;
				}
				else {
					System.out.println("Invalid importance function input. Type Imp1, Imp2, or Imp3.");
				}
				g.KSPSearch(source, k, lambda, h, func, args.length>=9 ? args[8] : "", resultFile);
				
				System.exit(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("File error, most likely due to invalid graph file.");
				e.printStackTrace();
				System.exit(1);
			}
		}
		
//		System.out.println("JL-KSP-Implementation");
//		runOnSmallGraph();
//		new TestDataGenerator().go();
		
//		checkHMM();
//		ENTS(Integer.parseInt(args[0]), Float.parseFloat(args[1]), Integer.parseInt(args[2]));
		
//		ENTS(4, 1.0f, 23);
		
//		//Benchmark maxes
//		benchmarkMax(new File("D:\\ENTS_results_logged_edges"), "logged");
//		benchmarkMax(new File("D:\\ENTS_results_logged+1_edges"), "logged+1");
//		benchmarkMax(new File("D:\\ENTS_results_logged_edges_prob"), "logged_prob");
//		benchmarkMax(new File("D:\\ENTS_results_logged+1_edges_prob"), "logged+1_prob");
//		
//		//Benchmark probs
//		benchmarkProb(new File("D:\\ENTS_results_logged+1_edges_prob"), "logged+1_prob");
//		benchmarkProb(new File("D:\\ENTS_results_logged_edges_prob"), "logged_prob");
		
//		//Benchmark prob all
//		benchmarkProbAll(new File("D:\\ENTS_results_logged+1_edges_prob"), "logged+1_prob_all_95");
//		benchmarkProbAll(new File("D:\\ENTS_results_logged_edges_prob"), "logged_prob_all");

//		//Benchmark ents
//		benchmarkENTS(new File("D:\\ENTS_results_logged+1_edges"), "logged+1_cutoff_avg+3std");
//		benchmarkENTS(new File("D:\\ENTS_results_logged_edges"), "logged");
//		benchmarkENTS(new File("D:\\ENTS_results_logged_edges_prob"), "logged_prob");
//		benchmarkENTS(new File("D:\\ENTS_results_logged+1_edges_prob"), "logged+1_prob");
//		
//		//Compute Ranks
//		int[] rankValues = {1,3,10,20};
//		calculateRanks(new File("D:\\ENTS_results_logged_edges"), rankValues);
//		calculateRanks(new File("D:\\ENTS_results_logged+1_edges"), rankValues);
//		calculateRanks(new File("D:\\ENTS_results_logged_edges_prob"), rankValues);
//		calculateRanks(new File("D:\\ENTS_results_logged+1_edges_prob"), rankValues);
		
		System.out.println("Main method is done.");
	}
	
	public static void checkHMM() {
		try {
			HashMap<String, String> classifications = loadClassifications();
			HashMap<String, List<Double>> similarities = new HashMap<String, List<Double>>();
			BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\hh_graphs\\d3ryce_.pair")));
			while(reader.ready()) {
				String[] edge = reader.readLine().split("\\s");
				if(edge[0].equals("d3ryce_")) {
					if(similarities.containsKey(classifications.get(edge[1]))) {
						similarities.get(classifications.get(edge[1])).add(Double.parseDouble(edge[2]));
					}
					else {
						LinkedList<Double> l = new LinkedList<Double>();
						l.add(Double.parseDouble(edge[2]));
						similarities.put(classifications.get(edge[1]), l);
					}
				}
				else if(edge[1].equals("d3ryce_")) {
					if(similarities.containsKey(classifications.get(edge[0]))) {
						similarities.get(classifications.get(edge[0])).add(Double.parseDouble(edge[2]));
					}
					else {
						LinkedList<Double> l = new LinkedList<Double>();
						l.add(Double.parseDouble(edge[2]));
						similarities.put(classifications.get(edge[0]), l);
					}
				}
			}
			reader.close();
			for(String fold : similarities.keySet()) {
				System.out.println(similarities.get(fold));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Set<String> proteinsToIgnore() {
		Set<String> proteins = new HashSet<String>();
		
		File where = new File("D:\\ENTS_results_logged_edges", "K=4_lambda=0.5");
		for(File resultFile : where.listFiles()) {
			try {
				String query = resultFile.getName().replace(".txt", "");
				BufferedReader reader = new BufferedReader(new FileReader(resultFile));
				while(reader.ready()) {
					String line = reader.readLine();
					if(line.contains("path") || line.isEmpty()) continue;

					String[] summary = line.split("\\s");
					String proteinName = summary[0];

					if(summary[1].equals("Infinity")) {
						proteins.add(query + "\t" + proteinName);
					}
					else break;
				}
				reader.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		return proteins;
	}
	
	/**
	 * Calculates the ranks in a given directory.
	 * @param where
	 * @param k
	 */
	public static void calculateRanks(File where, int[] k) {
		Map<String, String> classifications = loadClassifications();
		
		System.out.println("Algorithm ranks for " + where.getName());
		for(File paramSet : where.listFiles()) {
			System.out.println(paramSet.getName());
			for(int i=0; i<k.length; i++) {
				if(paramSet.listFiles().length >= 885) {
					System.out.println("Top " + k[i] + ": " + (100.0*calculateRank(paramSet, classifications, k[i])) + "%");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Calculates the ranks for a single run.
	 * @param folder
	 * @param classifications
	 * @param k
	 * @return
	 */
	public static double calculateRank(File folder, Map<String, String> classifications, int k) {
		double proteinsCorrectlyPredictedInTop = 0;
		double total = 0;
		
		for(File resultFile : folder.listFiles()) {
			if(resultFile.isDirectory()) continue;
			
			String query = resultFile.getName().replace(".txt", "");
			String trueFold = classifications.get(query);
			
			//Check if protein is correctly predicted in top k
			try {
				int numChecked = 0;
				BufferedReader reader = new BufferedReader(new FileReader(resultFile));
				while(reader.ready()) {
					String line = reader.readLine();
					if(line.contains("path") || line.isEmpty()) continue;

					String[] summary = line.split("\\s");
					String proteinName = summary[0];

					if(!PROT_IGNORE.contains(query + "\t" + proteinName) && classifications.containsKey(proteinName)) {
						String foldGuess = classifications.get(proteinName);
						if(trueFold.equals(foldGuess)) {
							proteinsCorrectlyPredictedInTop += 1.0;
							break;
						}

						numChecked++;
						if(numChecked == k) break;
					}
				}
				reader.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			//Increment total to find number of proteins
			total += 1.0;
		}
		
		return proteinsCorrectlyPredictedInTop / total;
	}
	
	public static void reformatData(String folderPath) {
		File folder = new File(folderPath);
		for(File resultFile : folder.listFiles()) {
			if(resultFile.isDirectory()) continue;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(resultFile));
				File reformatted = new File(resultFile.getName());
				reformatted.createNewFile();
				PrintStream output = new PrintStream(new FileOutputStream(reformatted));
				while(reader.ready()) {
					String ln = reader.readLine();
					if(ln.isEmpty() || ln.contains("path") || ln.contains("Path") || ln.contains(" = ")) {
						continue;
					}
					else {
						String[] info = ln.split(" ");
						output.println(info[0] + "\t" + info[1]);
					}
				}
				reader.close();
				output.close();
				
				/*Files.move(java.nio.file.Paths.get(reformatted.getPath()),
						java.nio.file.Paths.get(resultFile.getPath()),
						java.nio.file.StandardCopyOption.REPLACE_EXISTING);*/
				resultFile.delete();
				reformatted.renameTo(resultFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void ENTS(int k, float lambda, int h) {
		System.out.println("John Lhota ENTS.");
		
		ArrayList<File> graphFiles = null;
		int num = 0, tot=0;
		
		File graphs = new File("/storage/db/SCOP/RANKPROP/fatcat_hhblist_e0.0");
		if(!graphs.exists()) {
			graphs = new File("/work/db/SCOP/RANKPROP/fatcat_hhblist_e0.0");
		}
		if(!graphs.exists()) {
			graphs = new File("D:\\hh_graphs");
		}
		if(!graphs.exists()) {
			graphs = new File("hh_graphs");
		}
		
		try {
			graphFiles = new ArrayList<File>();
			BufferedReader reader = new BufferedReader(new FileReader(new File(graphs, "edge_files.list")));
			while(reader.ready()) {
				File file = new File(graphs, reader.readLine());
				graphFiles.add(file);
			}
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				
		for(int i = 0; i<graphFiles.size(); ) {
			File f = graphFiles.get(i);
			long startTime = System.currentTimeMillis();
			try {
				runProteinFile(k, lambda, h, f);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Program failed on " + format.format(new Date()));
			}
			i++;
			System.out.print(i + "/" + graphFiles.size());
			System.out.print(" ("+f.getName()+", "+(System.currentTimeMillis()-startTime)/1000F+"s)");
			System.out.println();
		}
		System.out.println("Fast ENTS is finished.");
		System.out.println("Program ended successfully on " + format.format(new Date()));
	}
	
	public static Graph makeGraph(File f, String edgeScore) throws IOException {
		HashMap<Integer, String> number2Name = new HashMap<Integer, String>();
		HashMap<String, Integer> name2Number = new HashMap<String, Integer>();
		BufferedReader reader = new BufferedReader(new FileReader(f));

		int n = 0;
		
		int line = 1;
		while(reader.ready()) {
			String[] info = reader.readLine().split("\\s");
			if(info.length != 3) {
				System.out.println("Invalid input on line " + line + " of graph file");
				System.out.println("Line should contain three whitespace-delimited values:");
				System.out.println("name of node 1, name of node 2, and similarity score between 0 and 1.");
			}
			if(!name2Number.containsKey(info[0])) {
				name2Number.put(info[0], n);
				number2Name.put(n, info[0]);
				n++;
			}
			if(!name2Number.containsKey(info[1])) {
				name2Number.put(info[1], n);
				number2Name.put(n, info[1]);
				n++;
			}
			line++;
		}

		Graph g = new Graph(number2Name);
		g.setName2Number(name2Number);

		reader = new BufferedReader(new FileReader(f));

		while(reader.ready()) {
			String[] info = reader.readLine().split("\\s");
			double score = 1.0-Math.log(Double.valueOf(info[2]))/Math.log(10.0);
			if(edgeScore.equals("NLS")) {
				score = -Math.log(Double.valueOf(info[2]))/Math.log(10.0);
			}
			else if(edgeScore.equals("RLS")) {
				score = 1.0-Math.log(Double.valueOf(info[2]))/Math.log(10.0);
			}
			g.addUndirectedEdge(name2Number.get(info[0].trim()), name2Number.get(info[1].trim()), score);
		}
		
		return g;
	}
		
	public static void runProteinFile(int k, float lambda, int h, File f) {
		HashMap<Integer, String> number2Name = new HashMap<Integer, String>();
		HashMap<String, Integer> name2Number = new HashMap<String, Integer>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f));
			
			int n = 0;

			while(reader.ready()) {
				String[] info = reader.readLine().split(" ");
				if(!name2Number.containsKey(info[0])) {
					name2Number.put(info[0], n);
					number2Name.put(n, info[0]);
					n++;
				}
				if(!name2Number.containsKey(info[1])) {
					name2Number.put(info[1], n);
					number2Name.put(n, info[1]);
					n++;
				}
			}
			
			Graph g = new Graph(number2Name);
			
			reader = new BufferedReader(new FileReader(f));
			
			while(reader.ready()) {
				String[] info = reader.readLine().split(" ");
				g.addUndirectedEdge(name2Number.get(info[0]), name2Number.get(info[1]), 1.0-Math.log(Double.valueOf(info[2]))/Math.log(10.0));
			}
			
			int source = name2Number.get(f.getName().replace(".pair", ""));
			name2Number = null;
			
			File superDir = new File("D:\\ENTS_results_logged+1_edges_prob");
			if(!superDir.exists()) {
				superDir = new File("ENTS_results_logged+1_edges_prob");
			}
			File resultDir = new File(superDir, "K="+k+"_"+"lambda="+lambda+"_h="+h);
			resultDir.mkdir();
			File finalResult = new File(resultDir, number2Name.get(source)+".txt");
			g.KSPSearch(source, k, lambda, h, "", finalResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void benchmarkENTS(File resultDir, String outputName) {
		System.out.println("Benchmark ENTS, " + outputName);

		HashMap<String, String> classifications = loadClassifications();	
		TreeSet<Hit> hits = new TreeSet<Hit>();
		
		Workbook workbook = null;
		WritableWorkbook temp = null;
		try {
			File spreadsheet = new File("algorithm_evaluation", "empty.xls");
			workbook = Workbook.getWorkbook(spreadsheet);
			temp = Workbook.createWorkbook(new File("algorithm_evaluation", outputName+"_ents.xls"), workbook);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//File[] folders = resultDir.listFiles();
		File[] folders = {/*new File(resultDir, "K=4_lambda=0.5_h=23"), */new File(resultDir, "K=4_lambda=0.5_h=50")/*, new File(resultDir, "K=4_lambda=0.75_h=50"), new File(resultDir, "K=4_lambda=1.0_h=50")*/};
		int paramSetID = 0;
		for(File folder : folders) {			
			System.out.println("Working on " + (paramSetID+1) + "/" + folders.length);
			
			HashMap<String, Hit> predictions = new HashMap<String, Hit>();
			
			try {				
				File[] resultFiles = folder.listFiles();
				if(resultFiles.length < 885) continue;
				
				for(File file : resultFiles) {
					if(file.isDirectory()) continue;
					
					String query = file.getName().replace(".txt", "");
					
					File revised = new File(file.getPath().replace(".txt", "$$$.txt"));
					
					List<Double> imps = new LinkedList<Double>();
					
					BufferedReader copier = new BufferedReader(new FileReader(file));
					PrintStream output = new PrintStream(new FileOutputStream(revised));
					while(copier.ready()) {
						String ln = copier.readLine();
						//System.out.println(ln);
						String[] info = ln.split("\t");
						if(!PROT_IGNORE.contains(query + "\t" + info[0])) {
							output.println(ln);
							imps.add(Double.parseDouble(info[1]));
						}
					}
					copier.close();
					output.close();
					
					double averageImp = 0;
					for(Double x : imps) {
						averageImp += x;
					}
					averageImp /= (double) imps.size();
					double std = 0;
					for(Double x : imps) {
						std += Math.pow(x-averageImp, 2);
					}
					std /= (double) imps.size();
					std = Math.sqrt(std);

					Process ents = Runtime.getRuntime().exec("perl protENTS.pl D:\\dir.scop_PDP.txt " + revised.getPath() + " " + (averageImp+1*std));
					BufferedReader reader = new BufferedReader(new InputStreamReader(ents.getInputStream()));
					String ln;
					while((ln=reader.readLine()) != null) {
						String[] res = ln.split("\t");
						String fold = res[0];
						double z = Double.parseDouble(res[1]);
						hits.add(new Hit(query, fold, z));
						if(!predictions.containsKey(query) || z > predictions.get(query).importance) {
							predictions.put(query, new Hit(query, fold, z));
						}
					}
					reader.close();
					
					revised.delete();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Hits: " + hits.size());
			
			int total = predictions.size();
			System.out.println("Total preds: " + total);
			int x = 0;
			for(String query : predictions.keySet()) {
				if(classifications.get(query).equals(predictions.get(query).foldGuess)) {
					x++;
				}
			}
			System.out.println("x="+x);
			
			makeSpreadsheet(folder, temp, paramSetID, classifications, hits);
			paramSetID++;
		}
		
		try {
			temp.write();
			temp.close();
			workbook.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	/**
	 * Tests the algorithm using the probabilistic importance functions (Imp2 and Imp3).
	 * @param resultDir
	 * @param outputName
	 */
	public static void benchmarkProb(File resultDir, String outputName) {
		System.out.println("Benchmark Prob, " + outputName);
		HashMap<String, String> classifications = loadClassifications();	
		TreeSet<Hit> hits = new TreeSet<Hit>();
		
		Workbook workbook = null;
		WritableWorkbook temp = null;
		try {
			File spreadsheet = new File("algorithm_evaluation", "empty.xls");
			workbook = Workbook.getWorkbook(spreadsheet);
			temp = Workbook.createWorkbook(new File("algorithm_evaluation", outputName+"_prob.xls"), workbook);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		File[] folders = resultDir.listFiles();
		int paramSetID = 0;
		for(File folder : folders) {
			System.out.println("Working on " + (paramSetID+1) + "/" + folders.length);
			
			try {				
				File[] resultFiles = folder.listFiles();
				if(resultFiles.length < 885) continue;
				
				for(File file : resultFiles) {
					if(file.isDirectory()) continue;
					
					//System.out.println(paramSetID + "/" + folders.length + " - Working on " + file.getName());
					
					String query = file.getName().replace(".txt", "");
					
					BufferedReader reader = new BufferedReader(new FileReader(file));
					Map<String, List<Double>> queryHits = new HashMap<String, List<Double>>();
					
					while(reader.ready()) {
						String line = reader.readLine();
						if(line.contains("path") || line.isEmpty()) continue;
						
						String[] summary = line.split("\\s");
						String proteinName = summary[0];

						double probCorrect = Double.valueOf(summary[1]);
						String foldGuess = classifications.get(proteinName);

						if(classifications.containsKey(proteinName) && !PROT_IGNORE.contains(query + "\t" + proteinName)) {
							if(!queryHits.containsKey(foldGuess)) {//We start with the highest importance values so no need to check that.
								List<Double> stub = new LinkedList<Double>();
								stub.add(probCorrect);
								queryHits.put(foldGuess, stub);
							}
							else {
								queryHits.get(foldGuess).add(probCorrect);
							}
						}
					}
					reader.close();

					//Add all queryHits to total hits
					for(String fold : queryHits.keySet()) {
						hits.add(new Hit(query, fold, queryHits.get(fold)));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Hits: " + hits.size());
			
			makeSpreadsheet(folder, temp, paramSetID, classifications, hits);
			paramSetID++;
		}
		
		try {
			temp.write();
			temp.close();
			workbook.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	/**
	 * Tests the algorithm using Imp2/Imp3 and the Prob fold score.
	 * @param resultDir
	 * @param outputName
	 */
	public static void benchmarkProbAll(File resultDir, String outputName) {
		System.out.println("Benchmark Prob, " + outputName);
		HashMap<String, String> classifications = loadClassifications();	
		TreeSet<Hit> hits = new TreeSet<Hit>();
		
		Workbook workbook = null;
		WritableWorkbook temp = null;
		try {
			File spreadsheet = new File("algorithm_evaluation", "empty.xls");
			workbook = Workbook.getWorkbook(spreadsheet);
			temp = Workbook.createWorkbook(new File("algorithm_evaluation", outputName+"_prob.xls"), workbook);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		File[] folders = resultDir.listFiles();
		int paramSetID = 0;
		for(File folder : folders) {
			System.out.println("Working on " + (paramSetID+1) + "/" + folders.length);
			
			try {				
				File[] resultFiles = folder.listFiles();
				if(resultFiles.length < 885) continue;
				
				for(File file : resultFiles) {
					if(file.isDirectory()) continue;
					
					//System.out.println(paramSetID + "/" + folders.length + " - Working on " + file.getName());
					
					String query = file.getName().replace(".txt", "");
					
					BufferedReader reader = new BufferedReader(new FileReader(file));
					Map<String, List<Double>> queryHits = new HashMap<String, List<Double>>();
					
					while(reader.ready()) {
						String line = reader.readLine();
						if(line.contains("path") || line.isEmpty()) continue;
						
						String[] summary = line.split("\\s");
						String proteinName = summary[0];

						double probCorrect = Double.valueOf(summary[1]);
						String foldGuess = classifications.get(proteinName);

						if(classifications.containsKey(proteinName) && !PROT_IGNORE.contains(query + "\t" + proteinName)) {
							if(!queryHits.containsKey(foldGuess)) {//We start with the highest importance values so no need to check that.
								List<Double> stub = new LinkedList<Double>();
								stub.add(probCorrect);
								queryHits.put(foldGuess, stub);
							}
							else {
								queryHits.get(foldGuess).add(probCorrect);
							}
						}
					}
					reader.close();

					//Add all queryHits to total hits
					for(String fold : queryHits.keySet()) {
						double probAllRight = 1.0;
						for(double p : queryHits.get(fold)) {
							if(p > 0.95) probAllRight *= p;
						}
						hits.add(new Hit(query, fold, probAllRight));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Hits: " + hits.size());
			
			makeSpreadsheet(folder, temp, paramSetID, classifications, hits);
			paramSetID++;
		}
		
		try {
			temp.write();
			temp.close();
			workbook.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * Tests the algorithm using the Max fold score.
	 * @param resultDir
	 * @param outputName
	 */
	public static void benchmarkMax(File resultDir, String outputName) {
		System.out.println("Benchmark Max, " + outputName);

		HashMap<String, String> classifications = loadClassifications();	
		TreeSet<Hit> hits = new TreeSet<Hit>();
		
		Workbook workbook = null;
		WritableWorkbook temp = null;
		try {
			File spreadsheet = new File("algorithm_evaluation", "empty.xls");
			workbook = Workbook.getWorkbook(spreadsheet);
			temp = Workbook.createWorkbook(new File("algorithm_evaluation", outputName+"_max.xls"), workbook);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		File[] folders = resultDir.listFiles();
		int paramSetID = 0;
		for(File folder : folders) {
			System.out.println("Working on " + (paramSetID+1) + "/" + folders.length);
			
			try {				
				File[] resultFiles = folder.listFiles();
				if(resultFiles.length < 885) continue;
				for(File file : resultFiles) {
					if(file.isDirectory()) continue;
					
					//System.out.println(paramSetID + "/" + folders.length + " - Working on " + file.getName());
					
					String query = file.getName().replace(".txt", "");
					
					BufferedReader reader = new BufferedReader(new FileReader(file));
					HashSet<String> foldsAdded = new HashSet<String>();
					while(reader.ready()) {
						if(foldsAdded.size() >= 2000) break;
						
						String line = reader.readLine();
						if(line.contains("path") || line.isEmpty()) continue;
						
						String[] summary = line.split("\\s");
						String proteinName = summary[0];
						
						if(!PROT_IGNORE.contains(query + "\t" + proteinName)) {
							double importance = Double.valueOf(summary[1]);
							String foldGuess = classifications.get(proteinName);
							
							if(classifications.containsKey(proteinName)) {
								if(!foldsAdded.contains(foldGuess)) {//We start with the highest importance values so no need to check that.
									hits.add(new Hit(query, foldGuess, importance));
									foldsAdded.add(foldGuess);
								}
							}
							else {
								//System.out.println("Highest noninfinite ranking is unclassified.");
							}
						}
					}
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Hits: " + hits.size());
			
			makeSpreadsheet(folder, temp, paramSetID, classifications, hits);
			paramSetID++;
		}
		
		try {
			temp.write();
			temp.close();
			workbook.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	/**
	 * Loads the list of proteins and their corresponding folds.
	 * @return
	 */
	public static HashMap<String,String> loadClassifications() {
		HashMap<String, String> classifications = new HashMap<String, String>(25000);
		try {
			File scopFile = new File("/storage/db/SCOP/dir.scop_PDP.txt");
			if(!scopFile.exists()) {
				scopFile = new File("D:\\dir.scop_PDP.txt");
			}
			BufferedReader reader = new BufferedReader(new FileReader(scopFile));
			while(reader.ready()) {
				String[] classification = reader.readLine().split("\\s");
//				for(String s : classification) {
//					System.out.print(s + "___");
//				}
//				System.out.println();
				String[] scop = classification[2].split("\\.");
				if(!classification[3].equals("-")) {
					classifications.put(classification[3], scop[0] + "." + scop[1]);
				}
			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return classifications;
	}

	/**
	 * Create an Excel(TM) spreadsheet to save the modified ROC curves to.
	 * @param folder
	 * @param temp
	 * @param paramSetID
	 * @param classifications
	 * @param hits
	 */
	public static void makeSpreadsheet(File folder, WritableWorkbook temp, int paramSetID, HashMap<String,String> classifications, TreeSet<Hit> hits) {
		WritableSheet sheet = null;
		try {			
			sheet = temp.getSheet(0);
			
			Label topN = new Label(0, 0, "Top N Hits");
			sheet.addCell(topN);
			
			for(int i = 0; i <= 2000; i+=10) {
				Number n = new Number(0, i/10 + 1, i);
				sheet.addCell(n);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		try {				
			Label top = new Label(paramSetID+1, 0, folder.getName());
			sheet.addCell(top);
			
			int checked = 0, correct = 0;
			for(int i = 0; i <= Math.min(hits.size(), 2000); i++) {
				if(checked%10 == 0) {
					Number fraction = new Number(paramSetID+1, checked/10 + 1, ((double)correct)/885.0);
					sheet.addCell(fraction);
				}
				
				Hit hit = hits.pollLast();
				System.out.println(hit.importance);
				if(hit.foldGuess.equals(classifications.get(hit.proteinName))) {
					correct++;
				}
				checked++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Used to test speed/memory usage of the algorithm.
	 * @param newData If this is true, create new random graphs (this takes a while).
	 */
	public static void doTesting(boolean newData) {
		if(newData) new TestDataGenerator().go();
		
		for(int i=0; i<TestDataGenerator.sizes.length; i++) {
			for(int j=0; j<TestDataGenerator.avgDegrees.length; j++) {
				int size = TestDataGenerator.sizes[i];
				int deg = TestDataGenerator.avgDegrees[j];

				long heapStart = Graph.memoryUsed();
				
				HashMap<Integer, String> nameMapping = new HashMap<Integer, String>();
				try {
					File nodeList = new File(new File("test_data", "n="+size+", deg="+deg), "allProteins");
					Scanner nodeListReader = new Scanner(new FileInputStream(nodeList), "UTF-8");
					int z=0;

					while(nodeListReader.hasNextLine()){
						String geneName = nodeListReader.nextLine();
						nameMapping.put(z , geneName);
						z++;
					}
					nodeListReader.close();
				} catch(IOException e) {
					e.printStackTrace();
				}

//				for(int node=0; node<size; node++) {
//					nameMapping.put(node, "G"+node);
//				}
				
				Graph g = new Graph(nameMapping);
				try {
					File edgeList = new File(new File("test_data", "n="+size+", deg="+deg), "G0_G1");
					Scanner edgeListReader = new Scanner(new FileInputStream(edgeList), "UTF-8");
					edgeListReader.nextLine();//throw away header
					while(edgeListReader.hasNextLine()) {
						String[] edge = edgeListReader.nextLine().split(" ");
						g.addDirectedEdge(Integer.parseInt(edge[0]), Integer.parseInt(edge[1]), Double.parseDouble(edge[2]));
					}
					edgeListReader.close();
				} catch(IOException e) {
					e.printStackTrace();
				}

				//			for(int node=0; node<g.edges.length; node++) {
				//				g.edges[i] = new ArrayList<Edge>();
				//			}
				//			long totalNumEdges = 0;
				//			while (((float)2*totalNumEdges)/((float)size) < deg) {
				//				int a = rand.nextInt(size);
				//				int b = rand.nextInt(size-1);
				//				if(b >= a) b++;
				//				if(g.edges[a].contains(b)) {
				//					continue;
				//				}
				//				else {
				//					g.addDirectedEdge(a, b, Math.random());
				//					totalNumEdges++;
				//				}
				//			}

				System.out.println("Running n="+size+" and deg="+deg);
				Statistics s = g.KSPSearch(0, 5, 0.5F, 50, "P", new File("res2.txt"));
				System.out.println("Memory: " + (s.maxHeap - heapStart)/1e6 + "MB");
				System.out.println("Time: " + s.time + "s");
				System.out.println();
			}
		}
	}
	
	/**
	 * Used for the programmer to hand-test the paths produced by the algorithm.
	 */
	public static void runOnSmallGraph() {
		HashMap<Integer, String> nameMapping = new HashMap<Integer, String>();
		nameMapping.put(0, "A");
		nameMapping.put(1, "B");
		nameMapping.put(2, "C");
		nameMapping.put(3, "D");
		nameMapping.put(4, "E");
		nameMapping.put(5, "F");
		nameMapping.put(6, "G");
		nameMapping.put(7, "H");
		nameMapping.put(8, "I");
		nameMapping.put(9, "J");
		Graph g = new Graph(nameMapping);
		g.addUndirectedEdge(0, 2, 3);
		g.addUndirectedEdge(0, 3, 6);
		g.addUndirectedEdge(2, 4, 9);
		g.addUndirectedEdge(1, 2, 7);
		g.addUndirectedEdge(3, 5, 2);
		g.addUndirectedEdge(5, 4, 3);
		g.addUndirectedEdge(1, 6, 2);
		g.addUndirectedEdge(5, 6, 1);
		g.addUndirectedEdge(4, 6, 7);
		g.addUndirectedEdge(4, 9, 3);
		g.addUndirectedEdge(6, 8, 5);
		g.addUndirectedEdge(6, 7, 3);
		g.addUndirectedEdge(7, 8, 0.5);
		g.KSPSearch(0, 3, 0.5F, 23, "P", new File("res_smalltest2.txt"));
		System.out.println("done");
	}

}
