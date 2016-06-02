/*
 * Entity class for service category
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries({
    //select queries for service category search
    @NamedQuery(name = "findAllServiceCategory", query = "select s from ServiceCategory s"),
    @NamedQuery(name = "findAllFrontendServiceCategory", query = "select s from ServiceCategory s WHERE UPPER(s.status) LIKE :status"),
    @NamedQuery(name = "findServiceCategoryByCategoryName", query = "select s from ServiceCategory s where UPPER(s.serviceCategoryName) LIKE :CategoryName"),
    @NamedQuery(name = "findServiceCategoryByStatus", query = "select s from ServiceCategory s where UPPER(s.status) LIKE :Status"),
    
    //update query to update service category
    @NamedQuery(name = "updateServiceCategory", 
            query = "update ServiceCategory a Set "
                    + "a.longDescription = :serCatLongDescription, "
                    + "a.serviceCategoryName = :serCatServiceCategoryName, "
                    + "a.shortDescription = :serCatShortDescription, "
                    + "a.status = :serCatStatus  "
                    + "where a.serviceCategoryId = :serCatId")
})
public class ServiceCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long serviceCategoryId;
    private String serviceCategoryName;
    private String shortDescription;
    private String longDescription;
    private String status;
    
    //constructor
    public ServiceCategory() {
    }
    
    //getter and setter funtions
    
    //get and set function for category id
    public Long getServiceCategoryId() {
        return serviceCategoryId;
    }
    public void setServiceCategoryId(Long id) {
        this.serviceCategoryId = id;
    }

    //get and set function for service category name
    public String getServiceCategoryName() {
        return serviceCategoryName;
    }
    public void setServiceCategoryName(String serviceCategoryName) {
        this.serviceCategoryName = serviceCategoryName;
    }

    //get and set function for service category short description
    public String getShortDescription() {
        return shortDescription;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    //get and set function for service category long description
    public String getLongDescription() {
        return longDescription;
    }
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    //get and set function for service category status
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    //auto generated codes
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviceCategoryId != null ? serviceCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceCategory)) {
            return false;
        }
        ServiceCategory other = (ServiceCategory) object;
        if ((this.serviceCategoryId == null && other.serviceCategoryId != null) || (this.serviceCategoryId != null && !this.serviceCategoryId.equals(other.serviceCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ServiceCategory[ id=" + serviceCategoryId + " ]";
    }

    
}
