package tests.image_test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import common.screenshot.ScreenshotUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.base.BasePage;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static com.codeborne.selenide.Selenide.$x;
import static common.screenshot.ScreenshotUtils.getBytes;
import static io.qameta.allure.Allure.label;

public class AshotFirstTest {

    public static final SelenideElement amountField = $x("//input[@data-qa-node='amount']");
    public static final SelenideElement mobileReplenishmentForm = $x("//form[@method='post']");

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.browserSize = "1200x800";
    }

    @Test
    public void testFullPage() throws Exception {

		// Allure Screen Diff Plugin
		label("testType", "screenshotDiff");

		ScreenshotUtils screenshotUtils = new ScreenshotUtils();
        BasePage basePage = new BasePage();

		//переход на страницу
        basePage.goToURL("https://next.privat24.ua/mobile");

		Thread.sleep(3000);

		//Ждем отбражение компонента
		mobileReplenishmentForm.shouldBe(Condition.visible);

        //TODO: Делаем изменения на странице - раскомментировать ПОСЛЕ ЭТАЛОННОГО СНИМКА
		amountField.setValue("100");

		//делаем эталонный скрин ОЖИДАЕМЫЙ и потом последующие все скрины (ФАКТИЧЕСКИЕ)
        final Screenshot actual = screenshotUtils.makeScreenshot($x("//section[@class='content_3MDkKI6-g6']"));

		//TODO: ОЖИДАЕМЫЙ: сохраняем его название = класс + id, закомментировать ПОСЛЕ ЭТАЛОННОГО СНИМКА
//        screenshotUtils.write(actual.getImage(), FirstImageTest.class, "mobile");

        //вычитываем ОЖИДАЕМЫЙ результат
        final BufferedImage expected = screenshotUtils.read(AshotFirstTest.class, "mobile");

        // сравниваем ОЖИДАЕМЫЙ и ФАКТИЧЕСКИЙ
        ImageDiff diff = new ImageDiffer().makeDiff(actual.getImage(), expected);
        screenshotUtils.write(diff.getMarkedImage(), AshotFirstTest.class, "diff");

        //Добавляем все три скрина
        addScreenshot("expected", expected);
        addScreenshot("actual", actual.getImage());
        addScreenshot("diff", diff.getMarkedImage());
    }

    // аттачим к Allure result
    @Attachment(value = "{name}", type = "image/png")
    public byte[] addScreenshot(final String name, final BufferedImage image) throws IOException {
        return getBytes(image);
    }
}
