package tests.image_test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.visual_regression_tracker.sdk_java.TestRunOptions;
import io.visual_regression_tracker.sdk_java.VisualRegressionTracker;
import io.visual_regression_tracker.sdk_java.VisualRegressionTrackerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.base.BasePage;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$x;
import static common.screenshot.ScreenshotUtils.makeScreenshotAsString;

public class VRTFirstTest {

	private VisualRegressionTracker visualRegressionTracker;
	BasePage basePage = new BasePage();

	public static final SelenideElement amountField = $x("//input[@data-qa-node='amount']");
	public static final SelenideElement mobileReplenishmentForm = $x("//form[@method='post']");

	@BeforeEach
	public void setUp() {
		final VisualRegressionTrackerConfig config = new VisualRegressionTrackerConfig(
			"http://localhost:4200",
			"7df0eae2-32e9-4786-bc79-66327962cd4d",
			"PMVMC0491343W2NNBGQFRR19W7B9",
			"master"
		);

		visualRegressionTracker = new VisualRegressionTracker(config);

		WebDriverManager.chromedriver().setup();
		Configuration.browser = "chrome";
		Configuration.browserSize = "1200x800";
	}

	@Test
	public void testFullPage() throws Exception {
		basePage.goToURL("https://next.privat24.ua/mobile");

		Thread.sleep(3000);

		//Ждем отбражение компонента
		mobileReplenishmentForm.shouldBe(Condition.visible);

		//TODO: Делаем изменения на странице - раскомментировать ПОСЛЕ ЭТАЛОННОГО СНИМКА
//		amountField.setValue("100");

		visualRegressionTracker.track(
			"Пополнение мобильного",
			makeScreenshotAsString($x("//section[@class='content_3MDkKI6-g6']")),
			defaultOpts()
		);
	}


	private TestRunOptions defaultOpts() {
		return TestRunOptions.builder().diffTollerancePercent(0).build();
	}
}
