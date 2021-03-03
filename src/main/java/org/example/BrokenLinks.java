package org.example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BrokenLinks {

    public static void main(String[] args) throws Exception{
        verifyBrokenLinks();
    }

    public static void verifyBrokenLinks() throws Exception{
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.deadlinkcity.com/");
        Thread.sleep(5000);

        Set<String> brokenlinkUrls = new HashSet<String>();
        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println(links.size());

        for (WebElement link : links) {
            try {
                String linkURL = link.getAttribute("href");
                URL url = new URL(linkURL);
                if(linkURL.contains("mailto")) continue;

                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() != 200) {
                    brokenlinkUrls.add(linkURL);
                }
                httpURLConnection.disconnect();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        driver.quit();

        for (String brokenLinkUrl : brokenlinkUrls) {
            System.err.println(brokenLinkUrl);
        }
    }

}
