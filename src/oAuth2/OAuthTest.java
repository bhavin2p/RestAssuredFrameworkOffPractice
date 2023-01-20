package oAuth2;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import pojo.API;
import pojo.GetCourse;
import pojo.WebAutomation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;

public class OAuthTest {

    public static void main(String[] args) throws InterruptedException {

        String courseTitles[] = {"Selenium Webdriver Java", "Cypress", "Protractor"};
        /*WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
        driver.findElement(By.xpath("//input[@type='email']")).sendKeys("emailId");
        driver.findElement(By.xpath("//input[@type='email']")).sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys("password");
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(Keys.ENTER);
        Thread.sleep(3000);*/
        String currentUrl = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AWgavdcgTuJvu0REWaIIy3gPcLnHCc-vjSifgLMiMJimAhYdhjFPBHvafa72_u5FIZ4tDg&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
        System.out.println("URL = " +currentUrl);
        String partialCode = currentUrl.split("code=")[1];
        String code = partialCode.split("&scope")[0];

        System.out.println("Code = " +code);

        String accessToken = given().queryParams("code", code).urlEncodingEnabled(false)
                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type", "authorization_code")
                .when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();

        JsonPath js = new JsonPath(accessToken);

        String tokenId = js.getString("access_token");

      /*String resp = given().queryParam("access_token", tokenId)
                .when().get("https://rahulshettyacademy.com/getCourse.php").asString();

        System.out.println("The get Response = " +resp);*/

        GetCourse response = given().queryParam("access_token", tokenId).expect().defaultParser(Parser.JSON)
                .when()
                .get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
        System.out.println(response.getLinkedIn());
        System.out.println(response.getCourses().getApi().get(1).getCourseTitle());
        System.out.println(response.getCourses().getWebAutomation().get(2).getPrice());

        List<API> apiCourses = response.getCourses().getApi();
        for(int i=0; i<apiCourses.size(); i++){
            System.out.println("Course of API is = " + response.getCourses().getApi().get(i).getCourseTitle());
            System.out.println("Price of API course is = " + response.getCourses().getApi().get(i).getPrice());
        }

        ArrayList<String> aList = new ArrayList<>();

        List<WebAutomation> webAutomationCourses = response.getCourses().getWebAutomation();
        for(int i=0; i<webAutomationCourses.size(); i++){
            aList.add(webAutomationCourses.get(i).getCourseTitle());
        }

        List<String> expectedCourses = Arrays.asList(courseTitles);

        Assert.assertTrue(aList.equals(expectedCourses));

    }


}
