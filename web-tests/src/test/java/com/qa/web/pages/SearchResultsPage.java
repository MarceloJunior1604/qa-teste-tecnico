package com.qa.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class SearchResultsPage extends BasePage {

    private final By heading = By.cssSelector("h1.page-title.ast-archive-title");
    private final By postTitles = By.cssSelector("h2.entry-title a");
    private final By noResultsMessage = By.cssSelector("section.no-results p");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public String getHeading() {
        return waitForVisible(heading).getText();
    }

    public List<String> getResultTitles() {
        return driver.findElements(postTitles).stream()
                .map(el -> el.getText())
                .toList();
    }

    public boolean hasResults() {
        return !driver.findElements(postTitles).isEmpty();
    }

    public String getNoResultsMessage() {
        return waitForVisible(noResultsMessage).getText();
    }
}
