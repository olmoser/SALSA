package at.ac.tuwien.dsg.cloud.salsa.common.processes;

import generated.oasis.tosca.TDefinitions;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;

import at.ac.tuwien.dsg.cloud.salsa.common.model.SalsaCloudServiceData;
import at.ac.tuwien.dsg.cloud.salsa.common.model.SalsaComponentReplicaData;
import at.ac.tuwien.dsg.cloud.salsa.common.model.SalsaComponentReplicaData.Capabilities;
import at.ac.tuwien.dsg.cloud.salsa.common.model.SalsaReplicaRelationship;
import at.ac.tuwien.dsg.cloud.salsa.common.model.data.SalsaCapabilityString;
import at.ac.tuwien.dsg.cloud.salsa.common.model.data.SalsaInstanceDescription;
import at.ac.tuwien.dsg.cloud.salsa.common.model.enums.SalsaEntityState;
import at.ac.tuwien.dsg.cloud.salsa.tosca.ToscaXmlProcess;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * This class is for connecting to the SalsaCenter. Each of this instance target
 * to a specific service Id, then the serviceId must be provide to the construction
 * to ensure that the serviceId is available.
 * 
 * This class is referred to the ControlService of Salsa-center-services
 * 
 * @author Le Duc Hung
 *
 */
public class SalsaCenterConnector {
	Logger logger;
	String centerServiceEndpoint;
	String serviceId;
	String workingDir;

	/**
	 * Create a connector to Salsa service
	 * @param centerServiceEndpoint The endpoint. E.g: <ip>:<port>/<path>
	 * @param serviceId The deployment ID which connected to
	 * @param storageFolder temporary folder to storage service files
	 * @param logger Logger
	 */
	public SalsaCenterConnector(String centerServiceEndpoint, String serviceId, String workingDir, Logger logger) {
		this.centerServiceEndpoint = centerServiceEndpoint+"/rest";
		this.logger = logger;
		this.serviceId = serviceId;
		this.workingDir = workingDir;
	}

	/**
	 * Submit a SalsaCloudService to SalsaCenter. The input is serviceFile of the serviceId
	 * @param serviceFile
	 */
	public void submitService(String serviceFile) {

		String url = centerServiceEndpoint + "/submit";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		FileBody uploadfile = new FileBody(new File(serviceFile));
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("file", uploadfile);
		post.setEntity(reqEntity);
		try {
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				logger.error("Server failed to register service: "
						+ new File(serviceFile).getName());
			}
		} catch (Exception e) {
			logger.error("Error to submit service: "
					+ new File(serviceFile).getName());
		}
	}
	
	/**
	 * Set the Capability for a Replica instance.
	 * @param topologyId
	 * @param nodeId
	 * @param replica
	 * @param capaId
	 * @param value
	 */
	public void setCapability(String topologyId, String nodeId, int replica, String capaId, String value) {
		// Send the Capa to Salsa center
		Client client = Client.create();
		String serverURL = centerServiceEndpoint + "/update/capability/"
				+ serviceId + "/" + topologyId + "/" + nodeId + "/" + replica
				+ "/" + capaId + "/" + value;
		logger.debug("Querrying: "+serverURL);
		WebResource webRes = client.resource(serverURL);
		ClientResponse response = webRes.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		if (response.getStatus()!=200){
			logger.error("Error when setting capability");
			return;
		}
		logger.debug(response.getEntity(String.class));
		// Get the update
		//updateTopology();
	}
	
	/**
	 * Set the state of a node instances (replcia).
	 * @param topologyId The topology of node
	 * @param nodeId The node
	 * @param replica The instance of node
	 * @param state The state
	 */
	public void setNodeState(String topologyId, String nodeId, int replica, SalsaEntityState state) {
		// send the update command to Salsa center
		Client client = Client.create();
		String serverURL = centerServiceEndpoint + "/update/nodestate/"
				+ serviceId + "/" + topologyId + "/" + nodeId + "/" + replica
				+ "/" + state.getNodeStateString();
		logger.debug("Querrying: "+serverURL);
		WebResource webRes = client.resource(serverURL);
		ClientResponse response = webRes.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		if (response.getStatus()!=200){
			logger.error("Error when setting node state");
			return;
		}
		logger.debug("Set node "+nodeId +" state to "+state.getNodeStateString()+". "+response.getEntity(String.class));
		// get the update
		//updateTopology();
	}
	
	
	/**
	 * Get capability value of a Replica instance.
	 * @param topoId
	 * @param nodeId
	 * @param replica
	 * @param capaId
	 * @return
	 * TODO: Change the replica hierarchy
	 */
	public String getCapabilityValue(String topoId, String nodeId, int replica, String capaId) {		
		SalsaCloudServiceData service = getUpdateCloudServiceRuntime();
		SalsaComponentReplicaData rep = service.getReplicaById(topoId, nodeId, replica);
		Capabilities capas = rep.getCapabilities();
		if (capas != null){
			List<SalsaCapabilityString> capaLst = capas.getCapability();
			for (SalsaCapabilityString capa : capaLst) {
				if (capa.getId().equals(capaId)){
					return capa.getValue();
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Download latest Tosca.
	 * @return Tosca object
	 */
	public TDefinitions getToscaDescription() {
		try {
			String url= centerServiceEndpoint
					+ "/getservice/" + serviceId;					
			String toscaFile = workingDir + "/"	+ serviceId;			
			FileUtils.copyURLToFile(new URL(url), new File(toscaFile));		
			TDefinitions def = ToscaXmlProcess.readToscaFile(toscaFile);
			return def;
		} catch (IOException e) {
			logger.error("Error when update service description: "+ e.toString());			
		} catch (JAXBException e1){
			logger.error(e1.toString());
		}
		return null;
	}
	
	/**
	 * Update the component data on Cloud Service. Use when deploy an addition component
	 * on service.
	 * @param serviceId The service Id which component belong to
	 * @param topologyId The topology which component belong to
	 * @param data The component object
	 */
	public void addComponentData(String serviceId, String topologyId, String nodeId, SalsaComponentReplicaData data){
		String url=centerServiceEndpoint
				+ "/addcomponent"
				+ "/"+serviceId
				+ "/"+topologyId
				+ "/"+nodeId;		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			
			JAXBContext jaxbContext = JAXBContext.newInstance(SalsaComponentReplicaData.class, SalsaInstanceDescription.class);	// don't need Topology or Service
			Marshaller msl = jaxbContext.createMarshaller();
			msl.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter result = new StringWriter();
			msl.marshal(data, result);			
			
			StringEntity input = new StringEntity(result.toString());
			input.setContentType("application/xml");
			post.setEntity(input);
			
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				logger.error("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode());
			} else {
				logger.debug("Added component successfully: " + data.getId());
			}
		} catch (JAXBException e){
			logger.error("Error when marshalling Component data: "+ data.getId());
			logger.error(e.toString());
		} catch (Exception e){
			logger.error("Some error when sending component's data");
		}
		
	}
	
	public void addRelationship(String topologyId, SalsaReplicaRelationship rela){
		String url=centerServiceEndpoint
				+ "/addrelationship"
				+ "/"+serviceId
				+ "/"+topologyId;		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			
			JAXBContext jaxbContext = JAXBContext.newInstance(SalsaReplicaRelationship.class);
			Marshaller msl = jaxbContext.createMarshaller();
			msl.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter result = new StringWriter();
			msl.marshal(rela, result);			
			
			StringEntity input = new StringEntity(result.toString());
			input.setContentType("application/xml");
			post.setEntity(input);
			
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				logger.error("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode());
			} else {
				logger.debug("Added relationship successfully: " + rela.toString());
			}
		} catch (JAXBException e){
			logger.error("Error when marshalling Component data: "+ rela.toString());
			logger.error(e.toString());
		} catch (Exception e){
			logger.error("Some error when sending component's data");
		}
				
	}
	
	/**
	 * Query the Cloud Service Object, contain all runtime replicas of the service.
	 * @return the CloudService instance.
	 */
	public SalsaCloudServiceData getUpdateCloudServiceRuntime(){
		String url=centerServiceEndpoint
				+ "/getserviceruntimexml"
				+ "/"+serviceId;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);
			if (response.getStatus() != 200){
				logger.error("Fail to get CloudService info. Http error code: "+response.getStatus());
				return null;
			}
			String xml = response.getEntity(String.class);
			return SalsaXmlDataProcess.readSalsaServiceXml(xml);
			
		} catch (Exception e){
			e.printStackTrace();			
		}				
		return null;
	}
	
	/**
	 * Update the topology for a replica. As the property is AnyType, the property can be any Jaxb object  
	 * @param topologyId
	 * @param nodeId
	 * @param replica
	 * @param property
	 */
	public void updateReplicaProperty(String topologyId, String nodeId, int replica, Object property){
		
	}
	
	/**
	 * Update the capability for a node replica.
	 * @param topologyId
	 * @param nodeId
	 * @param replica
	 * @param capaId
	 * @param value
	 */
	public void updateReplicaCapability(String topologyId, String nodeId, int replica, String capaId, String value){
		
	}
}
