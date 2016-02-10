package org.whuims.easynlp.scolartermdist;

public class ScholarDistanceCal {
	private String modelPath;

	public ScholarDistanceCal(String modelPath) {
		super();
		this.modelPath = modelPath;
	}

	public ScholarDistanceCal() {

	}

	public double calDistance(String termA, String termB) {
		return 0.0d;
	}

	public boolean canBeSeenSame(String termA, String termB) {
		return false;
	}
	
	

}
