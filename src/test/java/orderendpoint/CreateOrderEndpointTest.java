package orderendpoint;


import io.qameta.allure.Allure;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import util.Constant;
import util.client.ScooterServiceClient;
import util.model.Order;


import java.util.Arrays;

import static org.hamcrest.Matchers.hasKey;


@RunWith(Parameterized.class)
public class CreateOrderEndpointTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    public CreateOrderEndpointTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Текущий цвет – {8}")
    public static Object[][] testData() {
        return new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.",
                        4, "Konoha, 142 apt.", 5,
                        "2020-06-06", "Saske, come back to Konoha", new String[]{}},
                {"Naruto", "Uchiha", "Konoha, 142 apt.",
                        4, "Konoha, 142 apt.", 5,
                        "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK"}},
                {"Naruto", "Uchiha", "Konoha, 142 apt.",
                        4, "Konoha, 142 apt.", 5,
                        "2020-06-06", "Saske, come back to Konoha", new String[]{"GREY"}},
                {"Naruto", "Uchiha", "Konoha, 142 apt.",
                        4, "Konoha, 142 apt.", 5,
                        "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK", "GREY"}},
        };
    }

    @Before
    public void setUp(){
        RestAssured.baseURI = Constant.TEST_URI;

        //для отображения в отчёте параметризованных данных
        String testName = String.format("Создание заказа: цвет %s", Arrays.toString(color));
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName(testName)
        );
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void correctOrderCreateTest(){
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        ScooterServiceClient client = new ScooterServiceClient(Constant.TEST_URI);
        ValidatableResponse response = client.createOrder(order);
        response.assertThat().statusCode(201).body("$", hasKey("track"));
    }

}
