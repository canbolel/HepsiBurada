package WebAutomationBase.step;

import WebAutomationBase.base.BaseTest;
import WebAutomationBase.helper.ElementHelper;
import WebAutomationBase.helper.StoreHelper;
import WebAutomationBase.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;
import java.util.Scanner;

public class BaseSteps extends BaseTest {


  public static int DEFAULT_MAX_ITERATION_COUNT = 150;
  public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

  private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
      .getLogger(BaseSteps.class);

  private static String SAVED_ATTRIBUTE;

  public Actions actions = new Actions(driver);

  public BaseSteps(){

    PropertyConfigurator
        .configure(BaseSteps.class.getClassLoader().getResource("log4j.properties"));
  }

  private WebElement findElement(String key){
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By infoParam = ElementHelper.getElementByElementInfo(elementInfo);
    WebDriverWait webDriverWait = new WebDriverWait(driver, 0);
    WebElement webElement = webDriverWait
        .until(ExpectedConditions.presenceOfElementLocated(infoParam));
    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
        webElement);
    return webElement;
  }

  public void writeToCsv(String filePath, String text) {
    try {
      File file = new File(filePath);
      file.createNewFile();

      FileWriter writer = new FileWriter(filePath, true);
      writer.write(text);
      writer.write("\n");
      writer.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<WebElement> findElements(String key){
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By infoParam = ElementHelper.getElementByElementInfo(elementInfo);
    return driver.findElements(infoParam);
  }

  private void clickElement(WebElement element){
    element.click();
  }

  private void clickElementBy(String key){
    findElement(key).click();
  }

  public WebElement getRandomElement(List<WebElement> elements) {
    Random random = new Random();
    int randomCount = random.nextInt(elements.size());
    while (true) {
      if (randomCount == 0)
        randomCount = random.nextInt(elements.size());
      else
        break;
    }
    return elements.get(randomCount);
  }

  public void hoverElement(WebElement element){
    actions.moveToElement(element).build().perform();
  }

  private void hoverElementBy(String key){
    WebElement webElement = findElement(key);
    actions.moveToElement(webElement).build().perform();
  }

  public By getBy(String key) {
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By infoParam = ElementHelper.getElementByElementInfo(elementInfo);
    return infoParam;
  }
  public String readCsvFile(String path) {
    try {
      File file = new File(path);
      Scanner scanner = new Scanner(file);
      StringBuilder stringBuilder = new StringBuilder();
      while (scanner.hasNextLine()) {
        stringBuilder.append(scanner.nextLine());
        stringBuilder.append('\n');
      }

      return stringBuilder.toString();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public WebElement findElementByKey(String key) {
    By infoParam = getBy(key);
    WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

    WebElement webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(infoParam));
    ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
            webElement);
    return webElement;
  }

  private void sendKeyESC(String key){
    findElement(key).sendKeys(Keys.ESCAPE);

  }

  public List<WebElement> findElementsByKey(String key) {
    WebDriverWait webDriverWait = new WebDriverWait(driver, 60,1000);

    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By infoParam = ElementHelper.getElementByElementInfo(elementInfo);
    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
    return driver.findElements(infoParam);
  }

  private boolean isDisplayed(WebElement element){
    return element.isDisplayed();
  }


  private boolean isDisplayedBy(By by){
    return driver.findElement(by).isDisplayed();
  }

  private String getPageSource(){
    return driver.switchTo().alert().getText();
  }

  public static String getSavedAttribute(){
    return SAVED_ATTRIBUTE;
  }

  public String randomString(int stringLength){

    Random random = new Random();
    char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
    String stringRandom = "";
    for (int i = 0; i < stringLength; i++) {

      stringRandom = stringRandom + String.valueOf(chars[random.nextInt(chars.length)]);
    }

    return stringRandom;
  }

  public WebElement findElementWithKey(String key){
    return findElement(key);
  }

  public String getElementText(String key){
    return findElement(key).getText();
  }

  public String getElementAttributeValue(String key, String attribute){
    return findElement(key).getAttribute(attribute);
  }

  @Step("Print page source")
  public void printPageSource(){
    System.out.println(getPageSource());
  }

  @Step("Hover on <key>")
  public void hoverElement(String key) {
    if (!key.equals("")) {
      WebElement element = findElementByKey(key);
      hoverElement(element);
      waitByMilliSeconds(500);
    }
  }

  public void javaScriptClicker(WebDriver driver, WebElement element){

    JavascriptExecutor jse = ((JavascriptExecutor) driver);
    jse.executeScript("var evt = document.createEvent('MouseEvents');"
        + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
        + "arguments[0].dispatchEvent(evt);", element);
  }

  @Step({"Wait <value> seconds",
      "<int> saniye bekle"})
  public void waitBySeconds(int seconds){
    try {
      logger.info(seconds + " saniye bekleniyor.");
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Step({"Wait <value> milliseconds",
      "<long> milisaniye bekle"})
  public void waitByMilliSeconds(long milliseconds){
    try {
      logger.info(milliseconds + " milisaniye bekleniyor.");
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Step({"Wait for element then click <key>",
      "Elementi bekle ve sonra tıkla <key>"})
  public void checkElementExistsThenClick(String key){
    getElementWithKeyIfExists(key);
    clickElement(key);
  }

  @Step({"Click to element <key>",
      "Elementine tıkla <key>"})
  public void clickElement(String key){
    if (!key.equals("")) {
      WebElement element = findElement(key);
      hoverElement(element);
      waitByMilliSeconds(500);
      clickElement(element);
      logger.info(key + " elementine tıklandı.");
    }
  }

  @Step({"Click to element <key> with focus",
      "<key> elementine focus ile tıkla"})
  public void clickElementWithFocus(String key){
    actions.moveToElement(findElement(key));
    actions.click();
    actions.build().perform();
    logger.info(key + " elementine focus ile tıklandı.");
  }

  @Step({"Check if element <key> exists",
      "Wait for element to load with key <key>",
      "Element var mı kontrol et <key>",
      "Elementin yüklenmesini bekle <key>"})
  public WebElement getElementWithKeyIfExists(String key){
    WebElement webElement;
    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      try {
        webElement = findElementWithKey(key);
        logger.info(key + " elementi bulundu.");
        return webElement;
      } catch (WebDriverException e) {
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element: '" + key + "' doesn't exist.");
    return null;
  }

  @Step({"Go to <url> address",
  "<url> adresine git"})
  public void goToUrl(String url){
    driver.get(url);
    logger.info(url + " adresine gidiliyor.");
  }

  @Step({"Wait for element to load with css <css>",
      "Elementin yüklenmesini bekle css <css>"})
  public void waitElementLoadWithCss(String css){
    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (driver.findElements(By.cssSelector(css)).size() > 0) {
        logger.info(css + " elementi bulundu.");
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element: '" + css + "' doesn't exist.");
  }
  @Step("Wait for <key>")
  public WebElement waitElementLoadByKey(String key) {

    WebElement webElement;
    int loopCount = 0;
    while (loopCount < 10) {
      try {
        webElement = findElementByKey(key);
        //logger.info("Element:'" + key + "' found.");
        return webElement;

      } catch (WebDriverException e) {
      }
      loopCount++;
      waitByMilliSeconds(200);
    }
    Assert.fail("Element: '" + key + "' doesn't exist.");
    return null;
  }


  @Step({"Wait for element to load with xpath <xpath>",
      "Elementinin yüklenmesini bekle xpath <xpath>"})
  public void waitElementLoadWithXpath(String xpath){
    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (driver.findElements(By.xpath(xpath)).size() > 0) {
        logger.info(xpath + " elementi bulundu.");
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element: '" + xpath + "' doesn't exist.");
  }

  @Step({"Check if element <key> exists else print message <message>",
      "Element <key> var mı kontrol et yoksa hata mesajı ver <message>"})
  public void getElementWithKeyIfExistsWithMessage(String key, String message){
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By by = ElementHelper.getElementByElementInfo(elementInfo);

    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (driver.findElements(by).size() > 0) {
        logger.info(key + " elementi bulundu.");
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail(message);
  }

  @Step({"Check if element <key> not exists",
      "Element yok mu kontrol et <key>"})
  public void checkElementNotExists(String key){
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By by = ElementHelper.getElementByElementInfo(elementInfo);

    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (driver.findElements(by).size() == 0) {
        logger.info(key + " elementinin olmadığı kontrol edildi.");
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element '" + key + "' still exist.");
  }

  @Step({"Upload file in project <path> to element <key>",
      "Proje içindeki <path> dosyayı <key> elemente upload et"})
  public void uploadFile(String path, String key){
    String pathString = System.getProperty("user.dir") + "/";
    pathString = pathString + path;
    findElement(key).sendKeys(pathString);
    logger.info(path + " dosyası " + key + " elementine yüklendi.");
  }

  @Step({"Write value <text> to element <key>",
      "<text> textini <key> elemente yaz"})
  public void sendKeys(String text, String key){
    if (!key.equals("")) {
      findElement(key).sendKeys(text);
      logger.info(key + " elementine " + text + " texti yazıldı.");
    }
  }

  @Step({"Click with javascript to css <css>",
      "Javascript ile css tıkla <css>"})
  public void javascriptClickerWithCss(String css){
    Assert.assertTrue("Element bulunamadı", isDisplayedBy(By.cssSelector(css)));
    javaScriptClicker(driver, driver.findElement(By.cssSelector(css)));
    logger.info("Javascript ile " + css + " tıklandı.");
  }

  @Step({"Click with javascript to xpath <xpath>",
      "Javascript ile xpath tıkla <xpath>"})
  public void javascriptClickerWithXpath(String xpath){
    Assert.assertTrue("Element bulunamadı", isDisplayedBy(By.xpath(xpath)));
    javaScriptClicker(driver, driver.findElement(By.xpath(xpath)));
    logger.info("Javascript ile " + xpath + " tıklandı.");
  }

  @Step({"Check if current URL contains the value <expectedURL>",
      "Şuanki URL <url> değerini içeriyor mu kontrol et"})
  public void checkURLContainsRepeat(String expectedURL){
    int loopCount = 0;
    String actualURL = "";
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      actualURL = driver.getCurrentUrl();

      if (actualURL != null && actualURL.contains(expectedURL)) {
        logger.info("Şuanki URL" + expectedURL + " değerini içeriyor.");
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail(
        "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
            + actualURL);
  }

  @Step({"Send TAB key to element <key>",
      "Elemente TAB keyi yolla <key>"})
  public void sendKeyToElementTAB(String key){
    findElement(key).sendKeys(Keys.TAB);
    logger.info(key + " elementine TAB keyi yollandı.");
  }

  @Step({"Send BACKSPACE key to element <key>",
      "Elemente BACKSPACE keyi yolla <key>"})
  public void sendKeyToElementBACKSPACE(String key){
    findElement(key).sendKeys(Keys.BACK_SPACE);
    logger.info(key + " elementine BACKSPACE keyi yollandı.");
  }

  @Step({"Send ESCAPE key to element <key>",
      "Elemente ESCAPE keyi yolla <key>"})
  public void sendKeyToElementESCAPE(String key){
    findElement(key).sendKeys(Keys.ESCAPE);
    logger.info(key + " elementine ESCAPE keyi yollandı.");
  }

  @Step({"Check if element <key> has attribute <attribute>",
      "<key> elementi <attribute> niteliğine sahip mi"})
  public void checkElementAttributeExists(String key, String attribute){
    WebElement element = findElement(key);
    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (element.getAttribute(attribute) != null) {
        logger.info(key + " elementi " + attribute + " niteliğine sahip.");
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element DOESN't have the attribute: '" + attribute + "'");
  }

  @Step({"Check if element <key> not have attribute <attribute>",
      "<key> elementi <attribute> niteliğine sahip değil mi"})
  public void checkElementAttributeNotExists(String key, String attribute){
    WebElement element = findElement(key);

    int loopCount = 0;

    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (element.getAttribute(attribute) == null) {
        logger.info(key + " elementi " + attribute + " niteliğine sahip olmadığı kontrol edildi.");
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element STILL have the attribute: '" + attribute + "'");
  }

  @Step({"Check if <key> element's attribute <attribute> equals to the value <expectedValue>",
      "<key> elementinin <attribute> niteliği <value> değerine sahip mi"})
  public void checkElementAttributeEquals(String key, String attribute, String expectedValue){
    WebElement element = findElement(key);

    String actualValue;
    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      actualValue = element.getAttribute(attribute).trim();
      if (actualValue.equals(expectedValue)) {
        logger.info(
            key + " elementinin " + attribute + " niteliği " + expectedValue + " değerine sahip.");
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element's attribute value doesn't match expected value");
  }

  @Step({"Check if <key> element's attribute <attribute> contains the value <expectedValue>",
      "<key> elementinin <attribute> niteliği <value> değerini içeriyor mu"})
  public void checkElementAttributeContains(String key, String attribute, String expectedValue){
    WebElement element = findElement(key);

    String actualValue;
    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      actualValue = element.getAttribute(attribute).trim();
      if (actualValue.contains(expectedValue)) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element's attribute value doesn't contain expected value");
  }

  @Step({"Write <value> to <attributeName> of element <key>",
      "<value> değerini <attribute> niteliğine <key> elementi için yaz"})
  public void setElementAttribute(String value, String attributeName, String key){
    String attributeValue = findElement(key).getAttribute(attributeName);
    findElement(key).sendKeys(attributeValue, value);
  }

  @Step({"Write <value> to <attributeName> of element <key> with Js",
      "<value> değerini <attribute> niteliğine <key> elementi için JS ile yaz"})
  public void setElementAttributeWithJs(String value, String attributeName, String key){
    WebElement webElement = findElement(key);
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + value + "')",
        webElement);
  }

  @Step({"Clear text of element <key>",
      "<key> elementinin text alanını temizle"})
  public void clearInputArea(String key){
    findElement(key).clear();
  }

  @Step({"Clear text of element <key> with BACKSPACE",
      "<key> elementinin text alanını BACKSPACE ile temizle"})
  public void clearInputAreaWithBackspace(String key){
    WebElement element = findElement(key);
    element.clear();
    element.sendKeys("a");
    actions.sendKeys(Keys.BACK_SPACE).build().perform();
  }

  @Step({"Save attribute <attribute> value of element <key>",
      "<attribute> niteliğini sakla <key> elementi için"})
  public void saveAttributeValueOfElement(String attribute, String key){
    SAVED_ATTRIBUTE = findElement(key).getAttribute(attribute);
    System.out.println("Saved attribute value is: " + SAVED_ATTRIBUTE);
  }

  @Step({"Write saved attribute value to element <key>",
      "Kaydedilmiş niteliği <key> elementine yaz"})
  public void writeSavedAttributeToElement(String key){
    findElement(key).sendKeys(SAVED_ATTRIBUTE);
  }

  @Step({"Check if element <key> contains text <expectedText>",
      "<key> elementi <text> değerini içeriyor mu kontrol et"})
  public void checkElementContainsText(String key, String expectedText){
    Boolean containsText = findElement(key).getText().contains(expectedText);
    Assert.assertTrue("Expected text is not contained", containsText);
  }

  @Step({"Write random value to element <key>",
      "<key> elementine random değer yaz"})
  public void writeRandomValueToElement(String key){
    findElement(key).sendKeys(randomString(15));
  }

  @Step({"Write random value to element <key> starting with <text>",
      "<key> elementine <text> değeri ile başlayan random değer yaz"})
  public void writeRandomValueToElement(String key, String startingText){
    String randomText = startingText + randomString(15);
    findElement(key).sendKeys(randomText);
  }

  @Step({"Print element text by css <css>",
      "Elementin text değerini yazdır css <css>"})
  public void printElementText(String css){
    System.out.println(driver.findElement(By.cssSelector(css)).getText());
  }

  @Step({"Write value <string> to element <key> with focus",
      "<string> değerini <key> elementine focus ile yaz"})
  public void sendKeysWithFocus(String text, String key){
    actions.moveToElement(findElement(key));
    actions.click();
    actions.sendKeys(text);
    actions.build().perform();
  }

  @Step({"Refresh page",
      "Sayfayı yenile"})
  public void refreshPage(){
    driver.navigate().refresh();
  }

  @Step({"Change page zoom to <value>%",
      "Sayfanın zoom değerini değiştir <value>%"})
  public void chromeZoomOut(String value){
    JavascriptExecutor jsExec = (JavascriptExecutor) driver;
    jsExec.executeScript("document.body.style.zoom = '" + value + "%'");
  }

  @Step({"Open new tab",
      "Yeni sekme aç"})
  public void chromeOpenNewTab(){
    ((JavascriptExecutor) driver).executeScript("window.open()");
  }

  @Step({"Focus on tab number <number>",
      "<number> numaralı sekmeye odaklan"})//Starting from 1
  public void chromeFocusTabWithNumber(int number){
    ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
    driver.switchTo().window(tabs.get(number - 1));
  }

  @Step({"Focus on last tab",
      "Son sekmeye odaklan"})
  public void chromeFocusLastTab(){
    ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
    driver.switchTo().window(tabs.get(tabs.size() - 1));
  }

  @Step({"Focus on frame with <key>",
      "Frame'e odaklan <key>"})
  public void chromeFocusFrameWithNumber(String key){
    WebElement webElement = findElement(key);
    driver.switchTo().frame(webElement);
  }

  @Step({"Accept Chrome alert popup",
      "Chrome uyarı popup'ını kabul et"})
  public void acceptChromeAlertPopup(){
    driver.switchTo().alert().accept();
  }

  @Step("Arama alanına <keyword> değerini yaz ve enter butonuna tıkla")
  public void methodDeneme(String keyword){
    waitBySeconds(2);
    driver.getCurrentUrl();
    System.out.println("!!!!!! Url ==========" +driver.getCurrentUrl());
    driver.findElement(By.id("productSearch")).sendKeys(Keys.DOWN);
    waitBySeconds(1);
    driver.findElement(By.id("productSearch")).sendKeys(Keys.ARROW_UP);
    driver.findElement(By.id("productSearch")).sendKeys(Keys.ARROW_UP);
    driver.findElement(By.id("productSearch")).sendKeys(Keys.ARROW_UP);

    driver.findElement(By.id("productSearch")).sendKeys(keyword);
    driver.findElement(By.id("productSearch")).sendKeys(Keys.ENTER);



  }


  @Step("deneme")
  public void deneme()
  {
    System.out.println("deneme");
  }

  @Step("<key> listesinden random sec")
  public void random(String key)
  {
    List<WebElement>elements=findElementsByKey(key);
    Random random=new Random();
    int index=random.nextInt(elements.size());
    elements.get(index).click();
  }

}
