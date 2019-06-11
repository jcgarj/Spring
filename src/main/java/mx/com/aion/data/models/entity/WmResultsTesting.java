package mx.com.aion.data.models.entity;


import java.io.Serializable;

public class WmResultsTesting implements Serializable {

  private long caseId;
  private long serviceId;
  private String serviceUrl;
  private String request;
  private String dataValidate;
  private String expectedResult;
  private String response;
  private String testingResults;
  private java.sql.Timestamp testDate;


  public long getCaseId() {
    return caseId;
  }

  public void setCaseId(long caseId) {
    this.caseId = caseId;
  }


  public long getServiceId() {
    return serviceId;
  }

  public void setServiceId(long serviceId) {
    this.serviceId = serviceId;
  }


  public String getServiceUrl() {
    return serviceUrl;
  }

  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }


  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }


  public String getDataValidate() {
    return dataValidate;
  }

  public void setDataValidate(String dataValidate) {
    this.dataValidate = dataValidate;
  }


  public String getExpectedResult() {
    return expectedResult;
  }

  public void setExpectedResult(String expectedResult) {
    this.expectedResult = expectedResult;
  }


  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }


  public String getTestingResults() {
    return testingResults;
  }

  public void setTestingResults(String testingResults) {
    this.testingResults = testingResults;
  }


  public java.sql.Timestamp getTestDate() {
    return testDate;
  }

  public void setTestDate(java.sql.Timestamp testDate) {
    this.testDate = testDate;
  }

}
