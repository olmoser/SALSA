package at.ac.tuwien.dsg.cloud.salsa.tosca.extension;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import at.ac.tuwien.dsg.cloud.salsa.common.cloudservice.model.enums.SalsaFuzzyValues;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "SalsaInstanceDescriptionFuzzy")
public class SalsaInstanceDescriptionFuzzy {
	
	@XmlElement(name = "os")
	String os;
	
	@XmlElement(name = "cpu")
	String cpu;
	
	@XmlElement(name = "memory")
	String memory;
	
	@XmlElement(name = "storage")
	String storage;
	
//	@XmlElement(name = "Packages")
//	ToscaVMNodeTemplatePropertiesEntend.PackagesDependencies packagesDependencies;

	@Override
	public String toString() {
		return "SalsaInstanceDescriptionFuzzy [os=" + os + ", cpu=" + cpu
				+ ", memory=" + memory + ", storage=" + storage
				+ "]";
	}


}
