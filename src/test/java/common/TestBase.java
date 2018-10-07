package common;

import io.restassured.RestAssured;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.ITestContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestBase {

    public String runId = String.valueOf(System.currentTimeMillis()).substring(5, 12);
    public String uname;
    public String pwd;


    @BeforeClass
    public void beforeClass(ITestContext context) {
        RestAssured.baseURI = context.getCurrentXmlTest().getParameter("BaseURI");
        uname = context.getCurrentXmlTest().getParameter("UserName");
        pwd = context.getCurrentXmlTest().getParameter("UserPass");
        Reporter.log("Username="+uname+" Password="+pwd, true);
    }

    public String generateStringFromResource(String path) throws IOException {

        return new String(Files.readAllBytes(Paths.get(path)));

    }
}
