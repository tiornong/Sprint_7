package courierendpoint;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import io.restassured.response.ValidatableResponse;

import org.junit.After;
import org.junit.Test;

import util.client.ScooterServiceClient;

import util.model.Courier;
import util.model.Credentials;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.equalTo;


@DisplayName("Тесты логина курьера")
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
    @DisplayName("Логин курьера")
    @Description("Логин существующего курьера со всеми корректными параметрами")
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
    @DisplayName("Отсутствие логина")
    @Description("Попытка логина курьера при отсутствующем логине")
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
    @DisplayName("Отсутствие пароля")
    @Description("Попытка логина курьера при отсутствующем пароле")
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
    @DisplayName("Несуществующий курьер")
    @Description("Попытка залогиниться в несуществующего курьера. Важно -- в ходе теста создаётся и сразу же удаляется курьер, это делается для гарантии отсутствия курьера на момент проведения теста")
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
    @DisplayName("Неправильный пароль")
    @Description("Попытка залогиниться с неправильным паролем")
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
    @DisplayName("Неправильный логин")
    @Description("Попытка залогиниться с неправильным логином")
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
