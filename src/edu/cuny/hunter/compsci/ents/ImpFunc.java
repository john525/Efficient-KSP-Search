package edu.cuny.hunter.compsci.ents;
import java.util.List;

/**
 * An abstract class that can be implemented to compute a given node importance function.
 */
public abstract class ImpFunc {
	public static final ImpFunc IMP1;
	public static final ImpFunc IMP2;
	public static final ImpFunc IMP3;
	
	static {
		IMP1 = new ImpFunc() {
			public double calculate(List<Path> paths) {
				double importance = 0;
				for(Path p : paths) {
					importance += 1.0/p.getDistance();
				}
				return importance;
			}
		};
		
		IMP2 = new ImpFunc() {
			public double calculate(List<Path> paths) {
				double probAllWrong = 1.0;
				for(Path p : paths) {
					double pathImp = p.getDistance();
					double probRight = Math.pow(10.0, -pathImp);
					probAllWrong *= (1.0 - probRight);
				}
				double probAnyRight = 1.0 - probAllWrong;
				return probAnyRight;			
			}
		};

		IMP3 = new ImpFunc() {
			public double calculate(List<Path> paths) {
				double probAllWrong = 1.0;
				for(Path p : paths) {
					double pathImp = p.getDistance() - p.numEdges();
					double probRight = Math.pow(10.0, -pathImp);
					probAllWrong *= (1.0 - probRight);
				}
				double probAnyRight = 1.0 - probAllWrong;
				return probAnyRight;			
			}
		};

	}

	/**
	 * Calculates a user-specified importance function.
	 * @param paths A list of Path objects terminating at the same vertex.
	 * @return The importance score of the path.
	 */
	public abstract double calculate(List<Path> paths);
}
