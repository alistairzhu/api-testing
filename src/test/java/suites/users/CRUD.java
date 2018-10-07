package suites.users;

import data.ServiceContract;
import data.User;

import io.restassured.http.*;
import io.restassured.specification.ResponseSpecification;
import io.restassured.RestAssured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Reporter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import common.TestBase;

import java.io.IOException;

public class CRUD extends TestBase {
    public static final String CookieValue = ".ASPXANONYMOUS=QczrAYbODPBvRI50YzJsoYkPYM_dmfORqN0m5P7aTXVYQKqcU-5bNQzVEisMz3rDDahHmNYoIQ7eTbsUuz7ibfruBcQ0Xd8bBafqGgnPK87G_8o26ZmDWuSK-YpHE5ee8pOleA2; lfSessionKey_.laserfiche.com=Bw9xW1%2fzwzPhwlVTx6QX31kBxC0f1DMp2jg0eW3mJgR%2fmH4ftAQnuggqrYV0CsaG; acsApiRootUrl_.laserfiche.com=https%3a%2f%2fv-tor-2016c-2.laserfiche.com%2fACS%2f; lfCloud_.laserfiche.com=N4IgkgdgzgLghhAxgUyiAXAbVGAIhgZgBoQAlZAEwEsAnZRGAVRoBsMQALGGABynQD0AuAFdq3GnCosAdDFQwZLOFGQ0AZlUQdkMxAHsAtgICCYqjAAqk6QJAlcqRDSo8YVfRHZnxAAmtSbCRgBhB47KLiMDZsAL5EOPjoAIwk5NR0DMxs6JzcfIICAO7IAEZwiChQUHIKSipqmtq6BsbKqhpaOgKlNPpFqjIqPAAeAPx0PPoAvDQAtABspeoAHADsyABMyPYgjlDOru6e7Lj6iCKGyBAwaMGh4bnq%2biwUanNQL1QUIPGJGAAWNKUWj0JisdhcXj8ITPGiGGryWD1DpNHR6IwCdRzZIUBYUAELAg7BxOFxuDxeXIAMX08Lu4AeSRAcIRvwS4CSK2BGTB2Uh%2bUKUA4KngKMaXRamNKPEMu32hwpJ1yAAU%2blUoL4zDAjHBjl57p5HiAZXK%2fpyMABOHmgrIQ9AQEQsFikg7k%2fXsFUiUosLS%2bFV0%2bAsXz7KgAcwgal2ISNzJ43t9iCmNCDbyg4cjNHZ%2f3QmxtmXBOTy0MK8yWqw221qyPaEuaGOMAHUyiYKIYqBA7K7FR7cuQpumdTQAJ5atsdqiwSQew1hZklcrjrzmx4ABnzfPtxZ4hQAbnMh3NNqvkgtEEfxZ1660BABhESwIxqAMpuAsUxLyfRPWUgDEwiXAQVgWAEAFY1k2UCVlAgQeGUA09jJI5KW8Sp9BEG5oyZCI0IwmBswtdBQI3O0iyhHchH3Q9j1Pc9NkvNEpWMe9HyuGgXyDD92wgL9pz%2fAQO3kGgIDfAQLlYtQGQVd0UNyFidTYhkYzndgwz6EQeAIx4FhIwsBV4PcDzpI8TzPC9ayvdEb3kp92MDUTW243if08f99F3NRdyoZAinlJClSpEAAHkPJoLyfKw2N2EQFh0J%2bFckjWXT%2bVyciYQEChzgYyUGwECzGJkTKLiuG4XM7a4w19YUMvOKABEcdRRBYRQuEMX9mxYVpkEsfQABkGks3RWr8t1kOVEAAAlkBYTTZ2NHQZt%2bABdWIgA%3d%3d%3d; BPMSTS=77u/PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz48U2VjdXJpdHlDb250ZXh0VG9rZW4gcDE6SWQ9Il81ZGMxZmY4Zi0wMDAxLTRhMTQtYTE1OC1kN2YyNzhlNGY0NDAtM0ExMUE1M0M4ODhCNTAwODkzQUQyODE2ODhGMDA4RDYiIHhtbG5zOnAxPSJodHRwOi8vZG9jcy5vYXNpcy1vcGVuLm9yZy93c3MvMjAwNC8wMS9vYXNpcy0yMDA0MDEtd3NzLXdzc2VjdXJpdHktdXRpbGl0eS0xLjAueHNkIiB4bWxucz0iaHR0cDovL2RvY3Mub2FzaXMtb3Blbi5vcmcvd3Mtc3gvd3Mtc2VjdXJlY29udmVyc2F0aW9uLzIwMDUxMiI+PElkZW50aWZpZXI+Qnc5eFcxL3p3elBod2xWVHg2UVgzMWtCeEMwZjFETXAyamcwZVczbUpnUi9tSDRmdEFRbnVnZ3FyWVYwQ3NhRzwvSWRlbnRpZmllcj48L1NlY3VyaXR5Q29udGV4dFRva2VuPg==; ASP.NET_SessionId=foomvdhh3kdgnwatyww0mr5m";

    private ServiceContract currentTestContract;
    private static String testContractId;

    @BeforeClass
    public void setUp() {
        RestAssured.basePath = "";
        Reporter.log("Set Up", true);
        currentTestContract = new ServiceContract("TestServContract-a-c17","descriptiondd");


    }

    @Test (priority = 1)
    public void createServiceContract() throws IOException {

        Reporter.log("Create ServiceContract",true);
        ResponseSpecification serviceContractSpec = currentTestContract.getContractSpec();

//        String jsonBody = generateStringFromResource("/Users/bmishra/Code_Center/stash/experiments/src/main/resources/Search.json");


        String serviceContractId =
        given().cookie("Cookie",CookieValue).
                accept(ContentType.JSON).
                contentType(ContentType.JSON).
                body(currentTestContract).
//                log request body
//                log().body().
        when().
                post().
        then().
//                log full response (headers + body)
//                log().all().
                statusCode(200).
                spec(serviceContractSpec).
        extract().
                path("data.id").toString();
        currentTestContract.setContractId(serviceContractId);
        testContractId = currentTestContract.getContractId();
        Reporter.log("ServiceContractId id = " + testContractId, true);

    }


    @Test (priority = 2, dependsOnMethods = "createServiceContract")
    public void readServiceContract() {
        Reporter.log("Read Service Contract",true);

        given().cookie("Cookie",CookieValue).
        when().
                get(testContractId + "/Versions").
        then().
                statusCode(200).
               //body("data.type", containsString("EventVersionInfo`1"));
               // body("data.props.version.major", containsString("[1]"));
               // body("data.props..data.data.", equalTo("[1]"));
                 body("links.self", containsString(testContractId));

    }
/*
    @DataProvider(name="seasonsAndNumberOfRaces")
    public Object[][] createTestDataRecords() {
        return new Object[][] {
                {"2017",20},
                {"2016",21},
                {"1966",9}
        };
    }
*/
/*
    @Test ( dependsOnMethods = "createServiceContract")
    public void updateServiceContract() throws Exception{

        testContractId = "a9490131-7633-4b90-8501-42d49301cf1a";

        Thread.sleep(4000);
        Reporter.log("Update updateServiceContract", true);

        currentTestContract = new ServiceContract("TestServContract-a-29e-dd","description-a");
        //currentTestContract.setName("TestEdi-b--2ted-a-01");
    //    ResponseSpecification userSpec = currentTestContract.getContractSpec();

        given().cookie("Cookie",CookieValue).
                accept(ContentType.JSON).
                contentType(ContentType.JSON).
                body(currentTestContract).
        when().
                put(testContractId).
        then().
                statusCode(200);
              //  spec(userSpec);
    }


    @Test ( dependsOnMethods = "createServiceContract")
    public void viewServiceContract() throws Exception{

     //   testContractId = "a9490131-7633-4b90-8501-42d49301cf1a";

        Thread.sleep(4000);
        Reporter.log("Get all versions ServiceContract", true);

        currentTestContract = new ServiceContract("TestServContract-a-29e-dd","description-a");
        //currentTestContract.setName("TestEdi-b--2ted-a-01");
        //    ResponseSpecification userSpec = currentTestContract.getContractSpec();

        given().cookie("Cookie",CookieValue).
                accept(ContentType.JSON).
                contentType(ContentType.JSON).
                body(currentTestContract).
                when().
                get(testContractId + "/Versions").
                then().
                statusCode(200);
        //  spec(userSpec);
    }
*/
/*
    @Test
    public void deleteUser() {
        Reporter.log("Delete User", true);
        ResponseSpecification userSpec = currentTestContract.getContractSpec();

        given().
        when().
                delete("/"+currentTestContract.getContractId()).
        then().
                statusCode(204);
    }
*/
}
