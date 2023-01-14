package files;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class payload {

    public static String AddPlace(){
        return "{\n" +
                "    \"location\": {\n" +
                "        \"lat\": -38.383494,\n" +
                "        \"lng\": 33.427362\n" +
                "    },\n" +
                "    \"accuracy\": 50,\n" +
                "    \"name\": \"Poonam Test Ahmedabad1\",\n" +
                "    \"phone_number\": \"(+91) 983 893 3937\",\n" +
                "    \"address\": \"29, side layout, cohen 09\",\n" +
                "    \"types\": [\n" +
                "        \"shoe park\",\n" +
                "        \"shop\"\n" +
                "    ],\n" +
                "    \"website\": \"http://google.com\",\n" +
                "    \"language\": \"French-IN\"\n" +
                "}";
    }

    public static String CoursePrice(){

        return "{\n" +
                "\"dashboard\": {\"purchaseAmount\": 1162,\n" +
                "\"website\": \"rahulshettyacademy.com\"\n" +
                "},\n" +
                "\"courses\": [\n" +
                "{\n" +
                "\"title\": \"Selenium Python\",\n" +
                "\"price\": 50,\n" +
                "\"copies\": 6\n" +
                "},\n" +
                "{\n" +
                "\"title\": \"Cypress\",\n" +
                "\"price\": 40,\n" +
                "\"copies\": 4\n" +
                "},\n" +
                "{\n" +
                "\"title\": \"RPA\",\n" +
                "\"price\": 45,\n" +
                "\"copies\": 10\n" +
                "},\n" +
                "{\n" +
                "\"title\": \"Appium\",\n" +
                "\"price\": 36,\n" +
                "\"copies\": 7\n" +
                "}]\n" +
                "}";

    }

    public static String Addbook(String isbn, String aisle){
        String payload = "{\n" +
                "\"name\":\"Learn Appium Automation with Java\",\n" +
                "\"isbn\":\""+isbn+"\",\n" +
                "\"aisle\":\""+aisle+"\",\n" +
                "\"author\":\"John foer\"\n" +
                "}\n";
        return payload;
    }


    @Test(dataProvider = "BooksData")
    public void addBook(String isbn, String aisle){
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().header("Content-Type", "application/json").
                body(payload.Addbook(isbn, aisle))
                .when().post("Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js = ReusableMethods.rawToJson(response);
        String id = js.get("ID");
        System.out.println(id);
    }
}
