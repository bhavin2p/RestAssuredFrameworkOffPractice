package main.java;

import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

public class ComplexJsonParse {
    public static void main(String[] args) {

        JsonPath js = new JsonPath(payload.CoursePrice());


        //1. Print No of courses returned by API
        int sizeOfCourse = js.getInt("courses.size()");
        System.out.println("Total Courses = " +sizeOfCourse);

        //2.Print Purchase Amount
        int purchaseAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println("Purchase Amount = " +purchaseAmount);

        //3. Print Title of the first course
        String firstCourseTitle = js.getString("courses[0].title");
        System.out.println("Title of First Course is = " +firstCourseTitle);

        //4. Print All course titles and their respective Prices
        for(int i=0; i<sizeOfCourse; i++){
            String title = js.getString("courses["+i+"].title");
            int price = js.getInt("courses["+i+"].price");
            System.out.println("Title of Course "+i+ " is " +title+ " and price is " +price);
        }


        //5. Print no of copies sold by RPA Course
        for(int i=0; i<sizeOfCourse; i++){
            String title = js.getString("courses["+i+"].title");
            if(title.equals("RPA")){
                int copies = js.getInt("courses["+i+"].copies");
                System.out.println("The total copies sold for RPA is " +copies);
            }
        }


        int total = 0;
        //6. Verify if Sum of all Course prices matches with Purchase Amount
        for(int i=0; i<sizeOfCourse; i++){
            int copies = js.getInt("courses["+i+"].copies");
            int price = js.getInt("courses["+i+"].price");
            total = (copies * price) + total;
            System.out.println("Total Amount = " +total);
            System.out.println("Purchase Amount = " +purchaseAmount);
        }

        Assert.assertEquals(purchaseAmount, total);
    }

}
