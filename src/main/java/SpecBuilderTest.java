package main.java;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SpecBuilderTest {

    public static void main(String[] args) {

        //RequestSpecBuilder


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

        //RestAssured.baseURI = "http://rahulshettyacademy.com";

        RequestSpecification reqSpec =new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key", "qaclick123")
                .setContentType(ContentType.JSON).build();

        ResponseSpecification resspec =new ResponseSpecBuilder().expectStatusCode(200)
                .expectContentType(ContentType.JSON).build();

        RequestSpecification res=given().spec(reqSpec)
                .body(ap);

        Response response =res.when().post("/maps/api/place/add/json").
                then().spec(resspec).extract().response();

        String responseString=response.asString();
        System.out.println(responseString);

    }
}
