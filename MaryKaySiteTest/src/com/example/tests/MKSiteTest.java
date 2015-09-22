package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class MKSiteTest {

	public static void main(String[] args) throws Exception {

		driver.manage().window().maximize();

		action.openURL("http://www.marykay.ru");
		action.click(By.id("ctl13_ctl03_extNav_listItemLink_0"));
		
		action.waitForElementPresent(By.id("PlaceHolderMain_ctl01_ctl09_QuantityTextBox"), 30);
		

		action.type(By.id("PlaceHolderMain_ctl01_ctl09_QuantityTextBox"), "5");

		action.click(By.id("PlaceHolderMain_ctl01_atb867_AddToCartButton"));
		
		action.click(By.id("PlaceHolderMain_YourBag1_ProductList_RemoveButton_0"));
		
		System.out.println("Script has been completed successfully");
		action.closeBrowser();
	}

}
