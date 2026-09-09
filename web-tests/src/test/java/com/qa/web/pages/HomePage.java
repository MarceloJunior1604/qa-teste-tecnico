package com.qa.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private static final String URL = "https://blogdoagi.com.br/";

    private final By searchIcon = By.cssSelector("#ast-desktop-header a.astra-search-icon");
    private final By searchField = By.cssSelector("input.search-field");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        driver.get(URL);
        return this;
    }

    public SearchResultsPage searchFor(String term) {
        waitForClickable(searchIcon).click();
        var field = waitForVisible(searchField);
        field.sendKeys(term);
        field.submit();
        return new SearchResultsPage(driver);
    }
}
