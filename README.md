# Efficient-KSP-Search
Hunter College implementation of Single Source K Diverse Shortest Paths Algorithm.
Sample command:
"java -jar KSP.jar sample_graph.txt a 2 0.0 10 result.txt RLS imp1 P".
This loads the graph from sample_graph.txt, sets the source node to the node labeled "a" in the file, sets k=2, lambda=0.0, and h=10, and uses the "RLS" edge scoring function. The command will output the results of the search to "result.txt." Node importance values will be calculated using the built-in "imp1" score. The "P" at the end tells the program to print the actual k paths to the result file, as opposed to only showing the final importance value. (It could be left blank, replaced with "S" to keep track of the algorithm's time and memory usage, or written "PS" to do both.)
