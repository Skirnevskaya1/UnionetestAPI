package api;

import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegressTests {
    private final static String URL = "https://auth.unionetest.ru/api/v1/signin";
    static private Cookies cookies;
    public Response response;
    public String av;
    public String key;


    public String a() {

        UserData userData = new UserData(Constanta.phoneNumber, Constanta.pass);
        response =
                given()
                        .header("Content-Type", "application/json")
                        .body(userData)
                        .when()
                        .post("https://auth.unionetest.ru/api/v1/signin")
                        .then()
                        .statusCode(200)
                        .log().all()
                        //.header("Content-Type", notNullValue())
                        .extract().response();
        String[] values = response.jsonPath().getList("values").toArray(new String[0]);
        List<String> key = response.jsonPath().getList("Set-Cookie");
        String cookieValue;
        // Получаем значение заголовка Set-Cookie
        //List<String> setCookies = response.getHeaders().getValues("Set-Cookie");

        // Печатаем все значения заголовка Set-Cookie
        for (String cookie : key) {
            System.out.println("Set-Cookie: " + cookie);
        }

        // Если вы хотите получить конкретное значение, например, имя cookie
        for (String cookie : key) {
            if (cookie.startsWith("key")) {
                cookieValue = cookie.split(";")[0]; // Получаем значение cookie без атрибутов
                System.out.println("Your cookie value: " + cookieValue);
            }
        }

        return values[0];
        //System.out.println(av);
        //String[] authValues = values.split(",");
        //String[] expectedValues = {"application/json"}; // замените на ожидаемые значения
        // assertThat(authValues, arrayContainingInAnyOrder(expectedValues));
    }

    @Order(2)
    @Test
    public void b() {
        String av = a();
        System.out.println(av);
        //   Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecification200());
        response = given()
                .cookie("key", av)
                .queryParam("Authorization", av)
                .header("Content-Type", "application/json")
                .body("{}")
                .when()
                .post("https://api-buildernext-assmnt.unionetest.ru/api/v3/competency")
                .then()
                .statusCode(201)
                //.header("Content-Type", notNullValue()) // проверка, что заголовок Authorization присутствует
                .log().all()
                .extract().response();
        String competencyBlockId = response.jsonPath().getString("_id");
        String competencyBlock = response.getBody().asString();

        // Установка переменных коллекции в Postman
        System.setProperty("competency_block_id", competencyBlockId);
        System.setProperty("competency_block", competencyBlock);
    }

//
//    @Order(1)
//    @Test
//    public void a() {
//
//        UserData userData = new UserData(Constanta.phoneNumber, Constanta.pass);
//        response =
//                given()
//                        .header("Content-Type", "application/json")
//                        .body(userData)
//                        .when()
//                        .post("https://auth.unionetest.ru/api/v1/signin")
//                        .then()
//                        .statusCode(200)
//                        .log().cookies()
//                        .extract().response();
//        //.getDetailedCookies();
//        values = response.jsonPath().getString("values");
//        Assert.assertNotNull(values);
//        System.out.println("value= " + values);
//    }

//    @Order(2)
//    @Test
//    public void b() {
//        //   Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecification200());
//        given()
//                .queryParam("Authorization", values)
//                //.cookies("authorization", values)
//                .header("Content-Type", "application/json")
//                .when()
//                .post("https://api-buildernext-assmnt.unionetest.ru/api/v3/competency")
//                .then()
//                .statusCode(201)
//                .log().all()
//                .extract().response().jsonPath().get("_id");
//    }

//    @Test
//    @Order(3)
//    public void get_id_draft_block_compet() {
//        given()
//                .cookies(cookies)
//                .header("Content-Type", "application/json")
//                .body("")
//                .when()
//                .get("https://api-buildernext-assmnt.unionedev.ru/api/v3/competency/{{competency_block_id}}")
//                .peek()
//                .then()
//                .statusCode(200)
//                .log().all()
//                .extract().response().jsonPath().get("_id");
//    }
//
//    @Test
//    @Order(4)
//    public void rename_draft_compet() {
//        given()
//                .cookies(cookies)
//                .header("Content-Type", "application/json")
//                .body("")
//                .when()
//                .get("https://api-buildernext-assmnt.unionedev.ru/api/v3/competency/update/{{competency_id}}")
//                .peek()
//                .then()
//                .statusCode(200)
//                .log().all()
//                .extract().response().jsonPath().get("title");
//    }


//    @Test
//    @Order(11)
//    public void check_authorization_content_manager1() {
//        File file = new File("temp/file.json");
//        Response response = RestAssured
//                .given()
//                .header("Content-Type", "application/json")
//                .body(file)
//                .when()
//                .post("https://auth.unionetest.ru/api/v1/signin")
//                .then()
//                .statusCode(200)
//                .log().all()
//                .extract().response();
//        String values = response.jsonPath().getString("values");
//        Assert.assertNotNull(values);
//    }


}
