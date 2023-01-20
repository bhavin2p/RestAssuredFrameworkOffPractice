package main.java;

import files.payload;
import io.restassured.RestAssured;
import pojo.AddPlace;
import pojo.Location;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SerializationTest {

    public static void main(String[] args) {

        AddPlace ap = new AddPlace();
        Location loc = new Location();

        ap.setAccuracy(50);
        ap.setAddress("A201, ahmedabad");
        ap.setLanguage("English");
        ap.setPhone_number("(+91) 983 893 3937");
        ap.setWebsite("www.google.com");
        ap.setName("Poonam");
        List<String> myList = new ArrayList<>();
        myList.add("shoe park");
        myList.add("shop");
        ap.setTypes(myList);

        loc.setLat(-38.383494);
        loc.setLng(33.427362);
        ap.setLocation(loc);

        RestAssured.baseURI = "http://rahulshettyacademy.com";

        String response = given().log().all().
                queryParam("key", "qaclick123").header("Content-Type", "application/json").
                body(ap).when().post("maps/api/place/add/json").
                then().assertThat().statusCode(200).extract().response().asString();

        System.out.println("Response == " + response);
    }
}
