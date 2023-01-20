package main.java;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.json.Json;
import org.testng.Assert;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
public class ECommerceAPITest {

    public static void main(String[] args) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail("bhavinpanchal143@gmail.com");
        loginRequest.setUserPassword("F@CEb88k");
        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

        RequestSpecification reqLogin = given().log().all().spec(req).body(loginRequest);

        LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login").then().log().all()
                .extract().response().as(LoginResponse.class);
        String token = loginResponse.getToken();
        String userId = loginResponse.getUserId();
        System.out.println(token);
        System.out.println(userId);


        //Add product
        RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token ).build();

        RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseReq).param("productName", "Laptop")
        .param("productAddedBy",userId)
                .param("productCategory", "technology")
                .param("productSubCategory", "Computers/Laptop")
                .param("productPrice", "25000")
                .param("productDescription", "Dell")
                .param("productFor", "men")
                .multiPart("productImage", new File("C:\\Users\\BhavinP\\Documents\\Unix Command.jpg"));

        String addProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product")
                .then().log().all().extract().response().asString();

        JsonPath js = new JsonPath(addProductResponse);
        String productId = js.get("productId");

        System.out.println(productId);

        //Create order
        RequestSpecification createProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token ).setContentType(ContentType.JSON)
                .build();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCountry("India");
        orderDetail.setProductOrderedId(productId);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(orderDetail);
        Orders orders = new Orders();
        orders.setOrders(orderDetailList);
        //.relaxedHTTPSValidation() //SSL validation when proxy enabled
        RequestSpecification createOrderReq = given().log().all().spec(createProductBaseReq).body(orders);
        String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
        System.out.println(responseAddOrder);

        //Delete product
        RequestSpecification deleteProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token ).setContentType(ContentType.JSON)
                .build();
        RequestSpecification deleteProdReq = given().log().all().spec(deleteProductBaseReq).pathParam("productId", productId);
        String deleteProductResponse = deleteProdReq.when().delete("/api/ecom/product/delete-product/{productId}").then()
                .log().all().extract().response().asString();

        JsonPath jsDelete = new JsonPath(deleteProductResponse);
        Assert.assertEquals("Product Deleted Successfully", jsDelete.get("message"));


    }

}
