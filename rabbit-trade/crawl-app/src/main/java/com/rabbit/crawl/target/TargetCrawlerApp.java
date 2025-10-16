package com.rabbit.crawl.target;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

//System.setProperty("webdriver.edge.driver", "/usr/local/bin/msedgedriver");

public class TargetCrawlerApp {

  private static final int SLEEP = 10;

  public static void main(String[] args) {
    System.setProperty("webdriver.edge.driver", "/usr/local/bin/msedgedriver");

    // 2. Configure browser options
    EdgeOptions options = new EdgeOptions();
    options.addArguments("--headless"); // Chạy không giao diện
    options.addArguments("--disable-gpu"); // Tắt tăng tốc phần cứng
    options.addArguments("--remote-allow-origins=*");
    options.setExperimentalOption("excludeSwitches",
            Arrays.asList("enable-automation"));
    options.addArguments(
            "--start-maximized",
            "--disable-notifications",
            "--disable-blink-features=AutomationControlled",
            "user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36 Edg/139.0.0.0"
    );

    // 3. Initialize WebDriver
    WebDriver driver = new EdgeDriver(options);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    try {
      // 4. Navigate to Target page
      String targetUrl = "https://www.target.com/s?searchTerm=mondo+llama+081&moveTo=product-list-grid&Nao=0";
      System.out.println("Loading page: " + targetUrl);
      driver.get(targetUrl);

//      TimeUnit.SECONDS.sleep(SLEEP);
      // 5. Wait for products to load
      System.out.println("Waiting for products to load...");
      wait.until(ExpectedConditions.presenceOfElementLocated(
              By.cssSelector("div[data-test='product-grid']")));

      // 6. Find all products
      List<WebElement> products = driver.findElements(
              By.cssSelector("div[data-test='product-grid'] > div"));
      System.out.println("Found " + products.size() + " products:");

      // 7. Extract and print product info
      for (WebElement product : products) {
        try {
          String name = product.findElement(
                          By.cssSelector("a[data-test='product-title']"))
                  .getText();

          String price = "Price not available";
          try {
            price = product.findElement(
                            By.cssSelector("span[data-test='current-price']"))
                    .getText();
          } catch (Exception e) {
            // Alternative price selector
            try {
              price = product.findElement(
                              By.cssSelector("div[data-test='current-price']"))
                      .getText();
            } catch (Exception ex) {
              // If still not found
            }
          }

          // Bổ sung: Lấy hình ảnh sản phẩm
          String imageUrl = "";
          try {
            imageUrl = product.findElement(By.cssSelector("picture img")).getAttribute("src");
          } catch (Exception e) {
            try {
              imageUrl = product.findElement(By.cssSelector("picture source")).getAttribute("srcset");
            } catch (Exception ignored) {}
          }

          // Bổ sung: Lấy mã DPCI
          String dpci = "";

          wait.until(ExpectedConditions.presenceOfElementLocated(
                  By.cssSelector("div[data-test='item-details-specifications'")));
          // Tìm thẻ specifications
          WebElement specsDiv = driver.findElement(By.cssSelector("div[data-test='item-details-specifications']"));
         dpci = extractDPCI(specsDiv);

          System.out.println("\nProduct: " + name);
          System.out.println("Price: " + price);
          System.out.println("Image URL: " + imageUrl);
          System.out.println("DPCI: " + dpci);
          System.out.println("----------------------------");

          // Small delay between products
          TimeUnit.SECONDS.sleep(SLEEP);

        } catch (Exception e) {
          System.out.println("Error processing one product: " + e.getMessage());
        }
      }

    } catch (Exception e) {
      System.err.println("Error during crawling: " + e.getMessage());
      e.printStackTrace();
    } finally {
      // 8. Close the browser
      driver.quit();
      System.out.println("\nCrawling completed. Browser closed.");
    }
  }

  public static String extractDPCI(WebElement specificationsDiv) {
    try {
      // Cách 1: Dùng XPath để tìm div chứa text "Item Number (DPCI)"
      WebElement dpciElement = specificationsDiv.findElement(
              By.xpath(".//div[contains(., 'Item Number (DPCI)')]"));

      // Lấy toàn bộ text và tách lấy phần mã DPCI
      String fullText = dpciElement.getText();
      return fullText.split(":")[1].trim().split("<")[0]; // Kết quả: 081-22-3854

    } catch (Exception e) {
      try {
        // Cách 2: Duyệt qua tất cả các div con nếu XPath không hoạt động
        List<WebElement> divs = specificationsDiv.findElements(By.cssSelector("div > div"));
        for (WebElement div : divs) {
          if (div.getText().contains("Item Number (DPCI)")) {
            return div.getText().split(":")[1].trim();
          }
        }
      } catch (Exception ex) {
        System.err.println("Không thể trích xuất DPCI: " + ex.getMessage());
      }
      return "Not available";
    }
  }
}
