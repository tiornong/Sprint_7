package util;


public class Constant {
    public static final String TEST_URI = "https://qa-scooter.praktikum-services.ru/";

    // дублирование ручек на текущий момент, в будущем ситуация может измениться
    public static final String COURIER_CREATE_TEST_URL = TEST_URI + "api/v1/courier";
    public static final String COURIER_DELETE_TEST_URL = TEST_URI + "api/v1/courier/";

    public static final String COURIER_LOGIN_TEST_URL = TEST_URI + "api/v1/courier/login";

    // дублирование ручек на текущий момент, в будущем ситуация может измениться
    public static final String ORDERS_LIST_GET_TEST_URL = TEST_URI + "api/v1/orders";
    public static final String ORDER_CREATE_TEST_URL = TEST_URI + "api/v1/orders";

    public static final String ORDER_CANCEL_TEST_URL = TEST_URI + "api/v1/orders/cancel";
}
