package orderendpoint;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import org.junit.Test;

import util.client.ScooterServiceClient;


import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.SC_OK;


@DisplayName("Тесты получение списка заказов")
public class GetOrdersListEndpointTest {

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем получение списка заказов и корректность его структуры")
    public void getCorrectOrdersListTest() {
        ScooterServiceClient client = new ScooterServiceClient();
        ValidatableResponse response = client.getOrders();

        response.assertThat().statusCode(SC_OK);
        response.assertThat().body(matchesJsonSchemaInClasspath("ordersList.json"));
    }
}
