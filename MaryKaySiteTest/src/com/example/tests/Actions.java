package com.example.tests;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Actions {
	private static WebDriver driver;
	public static String url;
	private static int clickRetryCount = 0;
	private static int typeRetryCount = 0;

	protected Actions(WebDriver driver) {
		Actions.driver = driver;
	}

	public void openURL(String url) throws Exception {
		driver.navigate().to(url);
		waitForPageLoad();
		String currURL = driver.getCurrentUrl().toUpperCase();
		String shortURL = url.substring(url.indexOf("www"), url.length())
				.toUpperCase();
		if (!currURL.contains(shortURL))
			throw new Exception("URL: \"" + url + "\" was opened incorrectly");
	}

	public void click(By by) throws InterruptedException {
		try {
			driver.findElement(by).click();
			clickRetryCount = 0;
		} catch (StaleElementReferenceException ex) {
			while (clickRetryCount < 30) {
				Thread.sleep(200);
				clickRetryCount++;
				click(by);
			}
		} catch (WebDriverException ex) {
			while (clickRetryCount < 3) {
				Thread.sleep(200);
				clickRetryCount++;
				click(by);
			}
		}
	}

	public void clickIfPresent(By by) throws InterruptedException {
		try {
			driver.findElement(by).click();
			clickRetryCount = 0;
		} catch (StaleElementReferenceException ex) {
			while (clickRetryCount < 3) {
				Thread.sleep(200);
				clickRetryCount++;
				clickIfPresent(by);
			}
		} catch (WebDriverException ex) {
			while (clickRetryCount < 3) {
				Thread.sleep(200);
				clickRetryCount++;
				clickIfPresent(by);
			}
		}
	}

	public void clearinputField(By by) throws InterruptedException {
		driver.findElement(by).clear();
		Thread.sleep(500);
	}

	public void closeBrowser() {
		driver.quit();
	}

	public void type(By by, String textToType) throws InterruptedException {
		try {

			String textInField = "";
			if (driver.findElement(by).getTagName().equals("input"))
				textInField = driver.findElement(by).getAttribute("value")
						.toString().toUpperCase();
			else
				textInField = driver.findElement(by).getText().toString()
						.toUpperCase();

			if (!textInField.isEmpty()
					|| driver.findElement(by).getAttribute("type")
							.equals("password")) {
				driver.findElement(by).clear();
				Thread.sleep(50);
				driver.findElement(by).sendKeys(textToType);
			} else {
				driver.findElement(by).clear();
				Thread.sleep(50);
				driver.findElement(by).sendKeys(textToType);
				String typedText = "";

				if (driver.findElement(by).getTagName().equals("input"))
					typedText = driver.findElement(by).getAttribute("value")
							.toString().toUpperCase();
				else
					typedText = driver.findElement(by).getText().toString()
							.toUpperCase();

				if (!typedText.equals(textToType.toUpperCase())
						&& typeRetryCount < 3) {
					typeRetryCount++;
					type(by, textToType);
				}
			}
			typeRetryCount = 0;
		} catch (StaleElementReferenceException ex) {
			while (typeRetryCount < 30) {
				Thread.sleep(200);
				typeRetryCount++;
				type(by, textToType);
			}
		}
	}

	public void typeIfPresent(By by, String textToType)
			throws InterruptedException {
		try {
			if (driver.findElement(by) != null) {
				type(by, textToType);
			}
		} catch (Exception ex) {
		}
	}

	public void typeWithoutClearing(By by, String text)
			throws InterruptedException {

		try {
			driver.findElement(by).sendKeys(text);
			typeRetryCount = 0;
		} catch (StaleElementReferenceException ex) {
			while (typeRetryCount < 30) {
				Thread.sleep(200);
				typeRetryCount++;
				typeWithoutClearing(by, text);
			}
		}
	}

	public void typeRandomNumber(By by, int countOfDigits) {
		String randomValue = "";
		Random randomNumber = new Random();
		for (int i = 0; i < countOfDigits; i++) {
			randomValue = randomValue + randomNumber.nextInt(9);
		}
		driver.findElement(by).clear();
		driver.findElement(by).sendKeys(randomValue);
	}

	public void typeRandomNumberWithoutClearing(By by, int countOfDigits) {
		String randomValue = "";
		Random randomNumber = new Random();
		for (int i = 0; i < countOfDigits; i++) {
			randomValue = randomValue + randomNumber.nextInt(9);
		}
		driver.findElement(by).sendKeys(randomValue);
	}

	public void waitForElementEnabled(By by) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			try {
				WebElement elem = driver.findElement(by);
				if (elem.isEnabled())
					break;
				else
					Thread.sleep(1000);
			} catch (Exception ex) {
				Thread.sleep(1000);
			}
		}
	}

	public void waitForElementPresent(By by, int TimeInSeconds)
			throws InterruptedException {
		for (int i = 0; i < TimeInSeconds; i++) {
			try {
				driver.findElement(by);
				break;
			} catch (Exception ex) {
				Thread.sleep(1000);
			}
		}
	}

	public void waitForNotVisible(By by, int TimeInSeconds)
			throws InterruptedException {
		for (int i = 0; i < TimeInSeconds; i++) {
			try {
				if (driver.findElement(by).isDisplayed()) {
					Thread.sleep(1000);
				} else
					break;
			} catch (Exception ex) {
			}
		}
	}

	public void waitForVisible(By by, int TimeInSeconds)
			throws InterruptedException {
		for (int i = 0; i < TimeInSeconds; i++) {
			try {
				if (!driver.findElement(by).isDisplayed()) {
					Thread.sleep(1000);
				} else
					break;
			} catch (Exception ex) {
			}
		}
	}

	public void waitForNumberOfWindowsToEqual(final int numberOfWindows) {
		WebDriverWait wait = new WebDriverWait(driver, 30, 50);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return (driver.getWindowHandles().size() == numberOfWindows);
			}
		});
	}

	public void waitForTextIsNotNull(By by) throws InterruptedException {
		String textOfElement = "";
		for (int i = 0; i < 30; i++) {
			try {
				if (driver.findElement(by).getTagName().toUpperCase()
						.equals("INPUT"))
					textOfElement = driver.findElement(by)
							.getAttribute("value").toString();
				else
					textOfElement = driver.findElement(by).getText();
				if (textOfElement.isEmpty())
					Thread.sleep(1000);
				else
					break;
			} catch (Exception ex) {
				Thread.sleep(1000);
			}
		}
	}

	public void waitForPageLoad() {
		try {
			String pageLoadState = (String) ((JavascriptExecutor) driver)
					.executeScript("return document.readyState");
			for (int i = 0; i < 60; i++) {
				if (pageLoadState.equals("complete")
						|| pageLoadState.equals("loaded")) {
					break;
				} else {
					Thread.sleep(1000);

					pageLoadState = (String) ((JavascriptExecutor) driver)
							.executeScript("return document.readyState");

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isElementPresent(final By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String randomString(int countOfSymbols) {
		String name = "";
		char letters[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z' };
		for (int i = 0; i < countOfSymbols; i++)
			name += letters[(int) (Math.random() * (letters.length - 1))];
		return name;
	}

	public String randomString_Cyrillic(int countOfSymbols) {
		String name = "";
		char letters[] = { '?', '?', '?', '?', '?', '?', '?', '?', '?', '?',
				'?', '?', '?', '?', '?', '?', '?', '?', '?', '?', '?', '?',
				'?', '?', '?', '?', '?', '?' };
		for (int i = 0; i < countOfSymbols; i++)
			name += letters[(int) (Math.random() * (letters.length - 1))];
		return name;
	}

	public void selectByValue(By by, String value) {
		Select select = new Select(driver.findElement(by));
		select.selectByValue(value);
	}

	public void selectByIndex(By by, int index) {
		Select select = new Select(driver.findElement(by));
		select.selectByIndex(index);
	}

	public void selectByVisibleText(By by, String text)
			throws InterruptedException {
		try {
			Select select = new Select(driver.findElement(by));
			select.selectByVisibleText(text);
		} catch (StaleElementReferenceException ex) {
			Thread.sleep(1000);
			Select select = new Select(driver.findElement(by));
			select.selectByVisibleText(text);
		}
	}

	public void getScreenShot(String path) {
		try {
			TakesScreenshot screenShotDriver = ((TakesScreenshot) driver);
			File screenshot = screenShotDriver.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(path), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String randomDate(int startDate, int dispersion) {
		String date = "";
		date += (int) (Math.random() * 19 + 10) + ".0"
				+ (int) (Math.random() * 8 + 1) + "."
				+ (int) (Math.random() * dispersion + startDate);
		return date;
	}

	public boolean isTextPresent(String Text) {
		try {
			driver.findElement(By.tagName("body")).getText().contains(Text);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getTextOfHiddenElement(By by) {
		WebElement element = driver.findElement(by);
		String script = "var element = arguments[0];"
				+ "return element.textContent;";
		return (String) ((JavascriptExecutor) driver).executeScript(script,
				element);
	}

	public String getTextOfElement(By by) {
		String textOfElement = "";
		WebElement elem = driver.findElement(by);
		if (elem.getTagName().toString().equals("input"))
			textOfElement = elem.getAttribute("value").toString();
		else
			textOfElement = elem.getText();
		return textOfElement;
	}

	public boolean verifyElementIsDisplayed(By by) throws InterruptedException {
		try {
			if (driver.findElement(by).isDisplayed())
				return true;
			else
				return false;
		} catch (Exception ex) {
			return false;
		}
	}
}
