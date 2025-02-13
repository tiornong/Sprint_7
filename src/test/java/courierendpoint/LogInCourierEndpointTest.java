package courierendpoint;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.Constant;
import util.client.ScooterServiceClient;
import util.model.Courier;
import util.model.Credentials;


import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class LogInCourierEndpointTest {

    // Для хранения данных последнего использованного курьера
    Courier courier;

    @Before
    public void setUp(){
        RestAssured.baseURI = Constant.TEST_URI;
    }

    @After
    public void tearDown(){
        Credentials credentials = new Credentials(courier.getLogin(), courier.getPassword());
        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);
        ValidatableResponse response = client.login(credentials);
        client.deleteCourier(response.extract().jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void correctLoginTest() {
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        Credentials credentials = new Credentials(courier.getLogin(), courier.getPassword());
        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);

        client.createCourier(courier);
        ValidatableResponse response = client.login(credentials);

        response.assertThat()
                .statusCode(200)
                .body("$", hasKey("id"));
    }

    @Test
    @DisplayName("Попытка залогиниться без логина")
    public void  mandatoryOfLoginBodyParameterTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        Credentials credentials = new Credentials("", courier.getPassword());
        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);

        client.createCourier(courier);
        ValidatableResponse response = client.login(credentials);

        response.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Попытка залогиниться без пароля")
    public void  mandatoryOfPasswordBodyParameterTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        Credentials credentials = new Credentials(courier.getLogin(), "");
        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);

        client.createCourier(courier);
        ValidatableResponse response = client.login(credentials);

        response.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Попытка залогиниться в несуществующего курьера")
    public void nonExistentLoginTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        Credentials credentials = new Credentials("timofeevich", "qwerty");
        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);

        // Создаём и сразу удаляем курьера, что бы быть уверенными в том, что он не существует на момент теста
        client.createCourier(courier);
        client.deleteCourier(client.login(credentials).extract().jsonPath().getString("id"));
        ValidatableResponse response = client.login(credentials);

        response.assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

}
