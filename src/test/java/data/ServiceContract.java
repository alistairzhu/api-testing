package data;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.*;

public class ServiceContract {

    private String name;
    private String description;
    private String contractId;

    public ServiceContract(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getContractId() {
        return this.contractId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public ResponseSpecification getContractSpec(){
        ResponseSpecBuilder builder = new ResponseSpecBuilder ();
        builder.expectContentType(ContentType.JSON);
        builder.expectBody("data.name", equalTo(this.getName()));
        builder.expectBody("data.description", equalTo(this.getDescription()));
        builder.expectBody("data.iprops.Version.Major", equalTo(1));
        return builder.build ();
    }

}
