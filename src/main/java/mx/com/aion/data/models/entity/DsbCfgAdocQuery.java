package mx.com.aion.data.models.entity.entity;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "\"DSB_CFG_ADOC_QUERY\"")
@Table(name = "\"DSB_CFG_ADOC_QUERY\"")
public class DsbCfgAdocQuery implements Serializable {//Ocupa hibernate

    @Id
    @Column(name = "VC_QUERY_ID")
    private String vcQueryId;
    @Column(name = "N_QUERY_ACTIVE")
    private long nQueryActive;
    @Column(name = "VC_QUERY_DESCRIPTION")
    private String vcQueryDescription;
    @Column(name = "VC_QUERY_VERSION")
    private String vcQueryVersion;
    @Column(name = "VC_QUERY_STATEMENT")
    private String vcQueryStatement;
    @Column(name = "VC_QUERY_DATE")
    private String vcQueryDate;
    @Column(name = "VC_QUERY_AUTHOR")
    private String vcQueryAuthor;
    @Column(name = "VC_MODULE_ID")
    private String vcModuleId;

    public String getVcQueryId() {
        return vcQueryId;
    }

    public void setVcQueryId(String vcQueryId) {
        this.vcQueryId = vcQueryId;
    }


    public long getNQueryActive() {
        return nQueryActive;
    }

    public void setNQueryActive(long nQueryActive) {
        this.nQueryActive = nQueryActive;
    }


    public String getVcQueryDescription() {
        return vcQueryDescription;
    }

    public void setVcQueryDescription(String vcQueryDescription) {
        this.vcQueryDescription = vcQueryDescription;
    }


    public String getVcQueryVersion() {
        return vcQueryVersion;
    }

    public void setVcQueryVersion(String vcQueryVersion) {
        this.vcQueryVersion = vcQueryVersion;
    }


    public String getVcQueryStatement() {
        return vcQueryStatement;
    }

    public void setVcQueryStatement(String vcQueryStatement) {
        this.vcQueryStatement = vcQueryStatement;
    }


    public String getVcQueryDate() {
        return vcQueryDate;
    }

    public void setVcQueryDate(String vcQueryDate) {
        this.vcQueryDate = vcQueryDate;
    }


    public String getVcQueryAuthor() {
        return vcQueryAuthor;
    }

    public void setVcQueryAuthor(String vcQueryAuthor) {
        this.vcQueryAuthor = vcQueryAuthor;
    }


    public String getVcModuleId() {
        return vcModuleId;
    }

    public void setVcModuleId(String vcModuleId) {
        this.vcModuleId = vcModuleId;
    }

}
