package at.ac.tuwien.dsg.cloud.salsa.knowledge.model.impl;

public enum InstrumentType {
	apt("apt"),
	script("script"),
	chefsolo("chefsolo");
	
	String type;
	
	private InstrumentType(String type) {
		this.type = type;
	}
}
