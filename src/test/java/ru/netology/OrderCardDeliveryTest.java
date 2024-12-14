package ru.netology;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class OrderCardDeliveryTest {

    public String generateData(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    String validDate = generateData(4, "dd.MM.yyyy");

    @BeforeEach
    void setup() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    void shouldSendForm() {

        $("[placeholder='Город']").setValue("Уфа");
        $("[data-test-id='date'] input").sendKeys("\b\b\b\b\b\b\b\b\b\b");
        $("[data-test-id='date'] input").setValue(validDate);
        $("[name='name']").setValue("Сидоров Николай");
        $("[name='phone']").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(text("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification']")
                .shouldHave(text("Успешно!"), text("Встреча успешно забронирована на " + validDate));

        String actualText = $("[data-test-id='notification']").getText();
        String expectedText = "Успешно!\nВстреча успешно забронирована на " + validDate;

        assertEquals(expectedText, actualText.trim());
    }

}
