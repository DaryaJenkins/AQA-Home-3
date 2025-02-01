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

    @Test
    void sendValidValues() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дарья Иваненко");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79030001122");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.getText().trim());
    }

    @Test
    void sendInvalidNameInLatin() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Daria Ivanenko");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79030001122");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub"));
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test
    void sendInvalidNameWithSymbols() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дарья! Иваненк@");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79030001122");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub"));
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дарья Иваненко");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("79030001122");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWith10Chars() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дарья Иваненко");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7903000112");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWith12Chars() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дарья Иваненко");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+790300011222");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWith1Char() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дарья Иваненко");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("1");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWith2Chars() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дарья Иваненко");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("12");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendInvalidPhoneWithLetters() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дарья Иваненко");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7903000AB12");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void sendWithoutAgreement() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дарья Иваненко");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79030001122");
        driver.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("label.input_invalid"));
        Assertions.assertTrue(result.isDisplayed());
    }
}