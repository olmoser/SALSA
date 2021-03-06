/**
 * Copyright 2013 Technische Universitaet Wien (TUW), Distributed Systems Group
 * E184
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package at.ac.tuwien.dsg.cloud.salsa.knowledge.cloudinformation.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @Author Daniel Moldovan
 * @E-mail: d.moldovan@dsg.tuwien.ac.at
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ServiceUnit")
public class ServiceUnit extends Entity {

    @XmlAttribute(name = "category", required = true)
    private String category;
    @XmlAttribute(name = "subcategory", required = true)
    private String subcategory;
    @XmlElement(name = "CostFunction", required = false)
    private List<CostFunction> costFunctions;
    @XmlElement(name = "Resource", required = false)
    private List<Resource> resourceProperties;
    @XmlElement(name = "Quality", required = false)
    private List<Quality> qualityProperties;
    @XmlElement(name = "ElasticityCapability", required = false)
    private List<ElasticityCapability> elasticityCapabilities;

//    
    //from here onwards associations )optional or mandatory) are seen as ElasticityCapabilities
//    private List<serviceUnit> mandatoryAssociations;
//    private List<serviceUnit> optionalAssociations;
    {
        costFunctions = new ArrayList<CostFunction>();
        qualityProperties = new ArrayList<Quality>();
        resourceProperties = new ArrayList<Resource>();
//        optionalAssociations = new ArrayList<serviceUnit>();
//        mandatoryAssociations = new ArrayList<serviceUnit>();
        elasticityCapabilities = new ArrayList<ElasticityCapability>();
    }

    public ServiceUnit() {
    }

    public ServiceUnit(String category, String subcategory, String name) {
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
    }

    public void setCostFunctions(List<CostFunction> costFunctions) {
        this.costFunctions = costFunctions;
    }

    public void setResourceProperties(List<Resource> resourceProperties) {
        this.resourceProperties = resourceProperties;
    }

    public void setQualityProperties(List<Quality> qualityProperties) {
        this.qualityProperties = qualityProperties;
    }

    public void addCostFunction(CostFunction cf) {
        costFunctions.add(cf);
    }

    public void removeCostFunction(CostFunction cf) {
        costFunctions.remove(cf);
    }

    public void addResourceProperty(Resource resource) {
        resourceProperties.add(resource);
    }

    public void removeResourceProperty(Resource resource) {
        resourceProperties.remove(resource);
    }

    public void addQualityProperty(Quality quality) {
        qualityProperties.add(quality);
    }

    public void removeQualityProperty(Quality quality) {
        qualityProperties.remove(quality);
    }

//    public void addMandatoryAssociation(serviceUnit serviceUnit){
//        mandatoryAssociations.add(serviceUnit);
//    }
//    
//    public void removeMandatoryAssociation(serviceUnit serviceUnit){
//        mandatoryAssociations.remove(serviceUnit);
//    }
//    
//    
//    public void addOptionalAssociation(serviceUnit serviceUnit){
//        optionalAssociations.add(serviceUnit);
//    }
//    
//    public void removeOptionalAssociation(serviceUnit serviceUnit){
//        optionalAssociations.remove(serviceUnit);
//    }
//
//    public List<serviceUnit> getMandatoryAssociations() {
//        return mandatoryAssociations;
//    }
//
//    public List<serviceUnit> getOptionalAssociations() {
//        return optionalAssociations;
//    }
//       
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public List<CostFunction> getCostFunctions() {
        return costFunctions;
    }

    public List<Resource> getResourceProperties() {
        return resourceProperties;
    }

    public List<Quality> getQualityProperties() {
        return qualityProperties;
    }

    public List<ElasticityCapability> getElasticityCapabilities() {
        return elasticityCapabilities;
    }

    public void setElasticityCapabilities(List<ElasticityCapability> elasticityCapabilities) {
        this.elasticityCapabilities = elasticityCapabilities;
    }

    public void addElasticityCapability(ElasticityCapability characteristic) {
        this.elasticityCapabilities.add(characteristic);
    }

    public void removeElasticityCapability(ElasticityCapability characteristic) {
        this.elasticityCapabilities.remove(characteristic);
    }

    public List<ElasticityCapability> getMandatoryServiceUnitAssociations() {

        List<ElasticityCapability> mandatoryAssociations = new ArrayList<ElasticityCapability>();

        for (ElasticityCapability capability : getElasticityCapabilities()) {
            //only extract mandatory associations
            if (!capability.getType().equals(ElasticityCapability.Type.MANDATORY_ASSOCIATION)) {
                continue;
            }

            if (!capability.getTargetType().equals(ServiceUnit.class)) {
                continue;
            }

            mandatoryAssociations.add(capability);

        }

        return mandatoryAssociations;
    }

    public List<ElasticityCapability> getOptionalServiceUnitAssociations() {

        List<ElasticityCapability> mandatoryAssociations = new ArrayList<ElasticityCapability>();

        for (ElasticityCapability capability : getElasticityCapabilities()) {
            //only extract mandatory associations
            if (!capability.getType().equals(ElasticityCapability.Type.OPTIONAL_ASSOCIATION)) {
                continue;
            }
            //only optional associations towards ServiceUnit
            if (!capability.getTargetType().equals(ServiceUnit.class)) {
                continue;
            }

            mandatoryAssociations.add(capability);
        }

        return mandatoryAssociations;
    }

    public List<ElasticityCapability> getOptionalResourceAssociations() {

        List<ElasticityCapability> optionalAssociations = new ArrayList<ElasticityCapability>();

        for (ElasticityCapability capability : getElasticityCapabilities()) {
            //only extract mandatory associations
            if (!capability.getType().equals(ElasticityCapability.Type.OPTIONAL_ASSOCIATION)) {
                continue;
            }
            //only optional associations towards ServiceUnit
            if (!capability.getTargetType().equals(Resource.class)) {
                continue;
            }

            optionalAssociations.add(capability);
        }

        return optionalAssociations;
    }

    public List<ElasticityCapability> getOptionalQualityAssociations() {


        List<ElasticityCapability> optionalAssociations = new ArrayList<ElasticityCapability>();

        for (ElasticityCapability capability : getElasticityCapabilities()) {
            //only extract mandatory associations
            if (!capability.getType().equals(ElasticityCapability.Type.OPTIONAL_ASSOCIATION)) {
                continue;
            }
            //only optional associations towards ServiceUnit
            if (!capability.getTargetType().equals(Quality.class)) {
                continue;
            }

            optionalAssociations.add(capability);
        }

        return optionalAssociations;
    }

    public List<ElasticityCapability> getOptionalCostAssociations() {


        List<ElasticityCapability> optionalAssociations = new ArrayList<ElasticityCapability>();

        for (ElasticityCapability capability : getElasticityCapabilities()) {
            //only extract mandatory associations
            if (!capability.getType().equals(ElasticityCapability.Type.OPTIONAL_ASSOCIATION)) {
                continue;
            }
            //only optional associations towards ServiceUnit
            if (!capability.getTargetType().equals(CostFunction.class)) {
                continue;
            }

            optionalAssociations.add(capability);
        }

        return optionalAssociations;
    }

    public interface Category {

        String SOFTWARE = "SOFTWARE";
        String MANAGEMENT = "MANAGEMENT";
        String VIRTUAL_INFRASTRUCTURE = "VIRTUAL_INFRASTRUCTURE";
    }

    @Override
    public String toString() {
        return "ServiceUnit{" + "name=" + name + " category=" + category + ", subcategory=" + subcategory + ", costFunctions=" + costFunctions + ", resourceProperties=" + resourceProperties + ", qualityProperties=" + qualityProperties + ", elasticityCapabilities=" + elasticityCapabilities + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.category != null ? this.category.hashCode() : 0);
        hash = 89 * hash + (this.subcategory != null ? this.subcategory.hashCode() : 0);
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServiceUnit other = (ServiceUnit) obj;
        if ((this.category == null) ? (other.category != null) : !this.category.equals(other.category)) {
            return false;
        }
        if ((this.subcategory == null) ? (other.subcategory != null) : !this.subcategory.equals(other.subcategory)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
