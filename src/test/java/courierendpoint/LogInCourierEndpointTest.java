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


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class LogInCourierEndpointTest {

    // Для хранения данных последнего использованного курьера
    Courier courier;

    @After
    public void tearDown(){
        Credentials credentials = new Credentials(courier.getLogin(), courier.getPassword());
        ScooterServiceClient client = new ScooterServiceClient();
        ValidatableResponse response = client.login(credentials);
        client.deleteCourier(response.extract().jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void correctLoginTest() {
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        Credentials credentials = new Credentials(courier.getLogin(), courier.getPassword());
        ScooterServiceClient client = new ScooterServiceClient();

        client.createCourier(courier);
        ValidatableResponse response = client.login(credentials);

        response.assertThat()
                .statusCode(SC_OK)
                .body("$", hasKey("id"));
    }

    @Test
    @DisplayName("Попытка залогиниться без логина")
    public void  mandatoryOfLoginBodyParameterTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        Credentials credentials = new Credentials("", courier.getPassword());
        ScooterServiceClient client = new ScooterServiceClient();

        client.createCourier(courier);
        ValidatableResponse response = client.login(credentials);

        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Попытка залогиниться без пароля")
    public void  mandatoryOfPasswordBodyParameterTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        Credentials credentials = new Credentials(courier.getLogin(), "");
        ScooterServiceClient client = new ScooterServiceClient();

        client.createCourier(courier);
        ValidatableResponse response = client.login(credentials);

        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Попытка залогиниться в несуществующего курьера")
    public void nonExistentLoginTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        Credentials credentials = new Credentials("timofeevich", "qwerty");
        ScooterServiceClient client = new ScooterServiceClient();

        // Создаём и сразу удаляем курьера, что бы быть уверенными в том, что он не существует на момент теста
        client.createCourier(courier);
        client.deleteCourier(client.login(credentials).extract().jsonPath().getString("id"));
        ValidatableResponse response = client.login(credentials);

        response.assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Попытка залогиниться с неправильным паролем")
    public void correctOnlyPasswordTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        Credentials credentials = new Credentials(courier.getLogin(), "ytrewq");
        ScooterServiceClient client = new ScooterServiceClient();

        client.createCourier(courier);
        ValidatableResponse response = client.login(credentials);

        response.assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Попытка залогиниться с неправильным логином")
    public void correctOnlyLoginTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        Credentials credentials = new Credentials("hciveefomit", courier.getPassword());
        ScooterServiceClient client = new ScooterServiceClient();

        client.createCourier(courier);
        ValidatableResponse response = client.login(credentials);

        response.assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

}
