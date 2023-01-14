package main.java;

import files.ReusableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Basics {
    public static void main(String[] args) {
        //validate add place API is working as expected

        //given-all input details
        //when-submit the api -- resources and http methods
        //then-validate the response

        RestAssured.baseURI = "http://rahulshettyacademy.com";

        //Post method
        String response = given().log().all().
                queryParam("key", "qaclick123").header("Content-Type", "application/json").
                body(payload.AddPlace()).when().post("maps/api/place/add/json").
                then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();

        System.out.println("Response == " + response);

        // for parsing json
        JsonPath js = new JsonPath(response);

        String placeId = js.getString("place_id");
        System.out.println("Place Id = " + placeId);

        String newAddress = "Summer Walk, Africa";

        //Update place - Put method
        String updateResponse = given().log().all().queryParam("key", "qaclick123").
                header("Content-Type", "application/json").
                body("{\r\n" +
                        "\"place_id\":\"" + placeId + "\",\r\n" +
                        "\"address\":\"" + newAddress + "\",\r\n" +
                        "\"key\":\"qaclick123\"\r\n" +
                        "}").
                when().put("maps/api/place/update/json").
                then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated")).
                extract().response().asString();

        System.out.println("Update Response = " + updateResponse);

        //Get Place -->
        String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").
                queryParam("place_id", placeId).
                when().get("maps/api/place/get/json").
                then().assertThat().log().all().statusCode(200).extract().response().asString();

        JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
        //JsonPath js1 = new JsonPath(getPlaceResponse);

        String actualAddress = js1.getString("address");

        System.out.println("Actual Address = " + actualAddress);
        Assert.assertEquals(actualAddress, newAddress);


        //Add place -->
        //update place with new address -->
        //get place to validate if new address is present in response
    }
}
