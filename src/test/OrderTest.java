package test;

import com.google.gson.Gson;
import main.client.Client;
import main.dto.*;
import main.utils.Utils;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class OrderTest {

    Utils utils = new Utils();

    //CreateOrder tests
    @Test
    public void invalidRequestShouldReturnErr() throws IOException {

        Gson gson = new Gson();

        Client cli = new Client();
        String token = utils.getToken(cli);
        OrderRequest or = new OrderRequest(null, null, null, null, null, null, null);
        String response = cli.createOrder(or, token);

        GenericResponse gr = gson.fromJson(response, GenericResponse.class);

        assertEquals("Invalid request error. One or more mandatory fields are missing.", gr.getMessage());
        assertEquals("1001", gr.getCode());

    }

    @Test
    public void requestMissingAmountShouldReturnErr() throws IOException {

        Gson gson = new Gson();

        Client cli = new Client();
        String token = utils.getToken(cli);
        OrderRequest or = new OrderRequest(null, "description", "javauser", "www.google.com", "www.stackoverflow.com", "www.notifications.com", "TUVIEJA");
        String response = cli.createOrder(or, token);

        GenericResponse gr = gson.fromJson(response, GenericResponse.class);

        assertEquals("Invalid request error. One or more mandatory fields are missing.", gr.getMessage());
        assertEquals("1001", gr.getCode());
    }

    @Test
    public void requestMissingUserNameShouldReturnErr() throws IOException {

        Gson gson = new Gson();

        Client cli = new Client();
        String token = utils.getToken(cli);
        OrderRequest or = new OrderRequest("1.12", "description", "", "www.google.com", "www.stackoverflow.com", "www.notifications.com", "TUVIEJA");
        String response = cli.createOrder(or, token);

        GenericResponse gr = gson.fromJson(response, GenericResponse.class);

        assertEquals("Invalid request error. One or more mandatory fields are missing.", gr.getMessage());
        assertEquals("1001", gr.getCode());
    }

    @Test
    public void requestMissingCallbackFailShouldReturnErr() throws IOException {

        Gson gson = new Gson();

        Client cli = new Client();
        String token = utils.getToken(cli);
        OrderRequest or = new OrderRequest("1.12", "description", "javauser", "", "www.stackoverflow.com", "www.notifications.com", "TUVIEJA");
        String response = cli.createOrder(or, token);

        GenericResponse gr = gson.fromJson(response, GenericResponse.class);

        assertEquals("Invalid request error. One or more mandatory fields are missing.", gr.getMessage());
        assertEquals("1001", gr.getCode());
    }

    @Test
    public void requestMissingCallbackSuccessShouldReturnErr() throws IOException {

        Gson gson = new Gson();

        Client cli = new Client();
        String token = utils.getToken(cli);
        OrderRequest or = new OrderRequest("1.12", "description", "javauser", "https://google.com.ar", "", "www.notifications.com", "TUVIEJA");
        String response = cli.createOrder(or, token);

        GenericResponse gr = gson.fromJson(response, GenericResponse.class);

        assertEquals("Invalid request error. One or more mandatory fields are missing.", gr.getMessage());
        assertEquals("1001", gr.getCode());
    }

    @Test
    public void requestMissingDescriptionNotificationUrlAndOriginShouldCreateOrder() throws IOException {

        Gson gson = new Gson();

        Client cli = new Client();
        String token = utils.getToken(cli);
        OrderRequest or = new OrderRequest("1.12", null, "javauser", "https://google.com.ar", "www.falso.com", null, null);
        String response = cli.createOrder(or, token);

        OrderResponse orderResponse = gson.fromJson(response, OrderResponse.class);

        assertEquals("Order", orderResponse.getType());
        assertNotNull(orderResponse.getId());
        assertNotNull(orderResponse.getUuid());
    }

    //GetOrder Tests
    @Test
    public void validOrderIDShouldReturnAnOrder() throws IOException {

        Gson gson = new Gson();

        Client cli = new Client();
        String token = utils.getToken(cli);

        String response = cli.getOrder("0fec34b8-b407-4381-8729-efb64c09fca5", token);

        GetOrderResponse getOrderResponse = gson.fromJson(response, GetOrderResponse.class);

        assertEquals("0fec34b8-b407-4381-8729-efb64c09fca5", getOrderResponse.getOrderId());
        assertEquals("PENDING", getOrderResponse.getStatus());
        assertEquals("27e0c30b-0e09-4d3b-ad56-6cfaa126e381", getOrderResponse.getRefNumber());
        assertEquals(12.12, getOrderResponse.getAmount());

    }

    @Test
    public void nullOrderIdShouldReturnErr() throws IOException {

        Gson gson = new Gson();

        Client cli = new Client();
        String token = utils.getToken(cli);

        String response = cli.getOrder(null, token);
        GenericResponse gr = gson.fromJson(response, GenericResponse.class);

        assertEquals("1006", gr.getCode());
        assertEquals("no record found", gr.getMessage());
    }

    @Test
    public void emptyOrderIdShouldReturnErr() throws IOException {

        Gson gson = new Gson();

        Client cli = new Client();
        String token = utils.getToken(cli);

        String response = cli.getOrder("", token);
        GenericResponse gr = gson.fromJson(response, GenericResponse.class);

        assertNotNull(gr.getMessage());
    }

}
