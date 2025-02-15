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
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Тесты создания курьера")
public class CreateCourierEndpointTest {

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
    @DisplayName("Создание курьера")
    @Description("Создание курьера со всеми валидными параметрами")
    public void  createCourierTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        ScooterServiceClient client = new ScooterServiceClient();

        ValidatableResponse response = client.createCourier(courier);

        response.assertThat()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Дублированный логин")
    @Description("Попытка создать курьера с логином, который уже занят")
    public void unavailabilityOfCreatingTwoIdenticalCourierTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");

        ScooterServiceClient client = new ScooterServiceClient();

        // создаём первичного курьера
        client.createCourier(this.courier);
        // пытаемся создать дубль
        ValidatableResponse response = client.createCourier(this.courier);

        response.assertThat()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    
    @Test
    @DisplayName("Отсутствующий логин")
    @Description("Попытка создать курьера с отсутствующим логином в body")
    public void mandatoryOfLoginBodyParameterTest(){
        this.courier = new Courier("", "qwerty", "Timofey");
        ScooterServiceClient client = new ScooterServiceClient();
        ValidatableResponse response = client.createCourier(this.courier);

        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Отсутствующий пароль")
    @Description("Попытка создать курьера с отсутствующим паролем в body")
    public void mandatoryOfPasswordBodyParameterTest(){
        this.courier = new Courier("timofeevich", "", "Timofey");
        ScooterServiceClient client = new ScooterServiceClient();
        ValidatableResponse response = client.createCourier(this.courier);

        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Отсутствующее имя")
    @Description("Попытка создать курьера с отсутствующим именем в body")
    public void optionalityOfFirstNameBodyParameterTest(){
        this.courier = new Courier("timofeevich", "qwerty", "");
        ScooterServiceClient client = new ScooterServiceClient();

        ValidatableResponse response = client.createCourier(courier);

        response.assertThat()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

}
