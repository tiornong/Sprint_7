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


import static org.hamcrest.CoreMatchers.equalTo;


public class CreateCourierEndpointTest {

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
    @DisplayName("Корректное создание курьера")
    public void  createCourierTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");
        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);

        ValidatableResponse response = client.createCourier(courier);

        response.assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Создание курьера с логином-дублем")
    public void unavailabilityOfCreatingTwoIdenticalCourierTest(){
        this.courier = new Courier("timofeevich", "qwerty", "Timofey");

        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);

        // создаём первичного курьера
        client.createCourier(this.courier);
        // пытаемся создать дубль
        ValidatableResponse response = client.createCourier(this.courier);

        response.assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    
    @Test
    @DisplayName("Cоздание курьера с отсутствующим логином")
    public void mandatoryOfLoginBodyParameterTest(){
        this.courier = new Courier("", "qwerty", "Timofey");
        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);
        ValidatableResponse response = client.createCourier(this.courier);

        response.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с отсутствующим паролем")
    public void mandatoryOfPasswordBodyParameterTest(){
        this.courier = new Courier("timofeevich", "", "Timofey");
        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);
        ValidatableResponse response = client.createCourier(this.courier);

        response.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Cоздание курьера с отсутствующим именем")
    public void optionalityOfFirstNameBodyParameterTest(){
        this.courier = new Courier("timofeevich", "qwerty", "");
        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);

        ValidatableResponse response = client.createCourier(courier);

        response.assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

}
