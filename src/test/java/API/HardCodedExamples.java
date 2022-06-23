package API;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HardCodedExamples {

   String baseURI = RestAssured.baseURI = "http://hrm.syntaxtechs.net/syntaxapi/api";
   String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NTU1NjEzMDIsImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTY1NTYwNDUwMiwidXNlcklkIjoiMzgwNCJ9.QYIZTTpyIZoC_wZ7OrV04886Sh2RYjBPzVr2tlKSKkM";
   static String employee_id;

   @Test
   public void acreateEmployee(){
       RequestSpecification request= given().header("Content-Type","application/json").
                                             header("Authorization", token).
                                             body("{\n" +
               "  \"emp_firstname\": \"Laura\",\n" +
               "  \"emp_lastname\": \"Moore\",\n" +
               "  \"emp_middle_name\": \"Saly\",\n" +
               "  \"emp_gender\": \"F\",\n" +
               "  \"emp_birthday\": \"1975-08-19\",\n" +
               "  \"emp_status\": \"Waiting \",\n" +
               "  \"emp_job_title\": \"QA\"\n" +
               "}");

      Response response= request.when().post("/createEmployee.php");
      response.prettyPrint();

      response.then().assertThat().statusCode(201);

      //Hamcrest matchers
      response.then().assertThat().body("Message", equalTo("Employee Created"));
      response.then().assertThat().body("Employee.emp_firstname", equalTo("Laura"));

      //using jsonPath(), to specify the key in the body so that it returns the value against it
      employee_id = response.jsonPath().getString("Employee.employee_id");
       System.out.println(employee_id);

   }
 @Test
   public void bgetCreatedEmployee(){
      RequestSpecification preparedRequest= given().header("Content-Type","application/json").
              header("Authorization", token).queryParam("employee_id",employee_id);

     Response response= preparedRequest.when().get("/getOneEmployee.php");
     response.prettyPrint();

     response.then().assertThat().statusCode(200);

    String tempId =  response.jsonPath().getString("employee.employee_id.");
     System.out.println(tempId);
      Assert.assertEquals(tempId,employee_id);
   }

   @Test
   public void cupdateEmployee(){
       RequestSpecification prepareRequest = given().header("Content-Type","application/json").
               header("Authorization",token).body("{\n" +
                       "  \"employee_id\": \"" + employee_id + "\",\n" +
                       "  \"emp_firstname\": \"Lora\",\n" +
                       "  \"emp_lastname\": \"Smiths\",\n" +
                       "  \"emp_middle_name\": \"Adams\",\n" +
                       "  \"emp_gender\": \"F\",\n" +
                       "  \"emp_birthday\": \"1974-06-19\",\n" +
                       "  \"emp_status\": \"Not on Probation \",\n" +
                       "  \"emp_job_title\": \"PA Manager\"\n" +
                       "}");

       Response response = prepareRequest.when().put("/updateEmployee.php");
       response.prettyPrint();
       response.then().assertThat().statusCode(200);

   }
@Test
   public  void dGetUpdateEmployee(){

       RequestSpecification request = given().header("Content-Type","application/json").
               header("Authorization",token).queryParam("employee_id",employee_id);

       Response response = request.when().get("/getOneEmployee.php");
       response.then().assertThat().statusCode(200);
       response.prettyPrint();
   }

   @Test
    public void eGetAllEmployee(){
       RequestSpecification request = given().header("Authorization",token).
               header("Content-Type","application/json");

       Response response = request.when().get("/getAllEmployees.php");
        // it returns string of response
       String allEmployees = response.prettyPrint();

       //jsonPath() vs jsonPath
       // jsonPath  is a class that contains method for converting the values into json object
       //jsonPath() is a method belongs to jsonPath class

       //creating object of jsonpath class
       JsonPath js = new JsonPath(allEmployees);

       //retriving the total number of employees
      int count = js.getInt("Employees.size()");
       System.out.println(count);

       //to print only employee id of all the employees
       for (int i = 0; i < count; i++) {
          String empID = js.getString("Employees["+ i + "].employee_id");
           System.out.println(empID);

       }
   }
}
