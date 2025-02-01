package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    private void fillForm(String name, String phone, boolean agreement) {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys(name);
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(phone);
        if (agreement) {
            driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        }
        driver.findElement(By.cssSelector(".button")).click();
    }

    @Test
    void sendValidValues() {
        fillForm("Дарья Иваненко", "+79031110022", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.getText().trim());
    }

    @Test
    void sendInvalidNameInLatin() {
        fillForm("Daria Ivanenko", "+79031110022", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub"));
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test
    void sendInvalidNameWithSymbols() {
        fillForm("Дарья! Ив@ненко", "+79031110022", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub"));
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test
    void sendFormWithoutName() {
        fillForm("", "+79031110022", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub"));
        Assertions.assertEquals("Поле обязательно для заполнения", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWithoutPlus() {
        fillForm("Дарья Иваненко", "79031110022", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWithTenChars() {
        fillForm("Дарья Иваненко", "+7903110022", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWithTwelveChars() {
        fillForm("Дарья Иваненко", "+790311002222", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWithOneChar() {
        fillForm("Дарья Иваненко", "1", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWithTwoChars() {
        fillForm("Дарья Иваненко", "12", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWithLetters() {
        fillForm("Дарья Иваненко", "+790311100AB", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendFormWithoutPhone() {
        fillForm("Дарья Иваненко", "", true);
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Поле обязательно для заполнения", result.getText().trim());
    }

    @Test
    void sendWithoutAgreement() {
        fillForm("Дарья Иваненко", "+79031110022", false);
        WebElement result = driver.findElement(By.cssSelector("label.input_invalid"));
        Assertions.assertTrue(result.isDisplayed());
    }
}