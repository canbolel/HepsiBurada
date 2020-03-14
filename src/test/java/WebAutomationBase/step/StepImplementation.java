package WebAutomationBase.step;

import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class StepImplementation extends BaseSteps {

    String cartValue;
    String price;

    @Step("Click login link")
    public void clickLoginLink() {
        hoverElement("loginHover");
        WebElement loginButton = findElementByKey("logInHoverButton");
        loginButton.click();
    }
    @Step("Write <value> to <textArea>")
    public void writeEmailValue(String value, String textArea) {
        WebElement fillText = findElementByKey(textArea);
        fillText.sendKeys(value);
    }
    @Step("Check login status")
    public void checkLogIn(){
        WebElement logCheck=findElementByKey("loggedInCheck");
        Assert.assertTrue(logCheck.getText().equals("Hesabım"));
    }
    @Step("Check cart Status")
    public void checkCart(){
        WebElement cartCount=findElementByKey("cartCount");
        Assert.assertTrue(cartCount.getText().equals("0"));
    }

    @Step("Click random category")
    public void clickRandomCategory() {
        List<WebElement> elements = findElementsByKey("topCategory");
        WebElement topCategory = getRandomElement(elements);
        actions.moveToElement(topCategory).build().perform();
        waitBySeconds(2);
        List<WebElement> subCategories = findElementsByKey("subCategory");
        WebElement subCategory = getRandomElement(subCategories);
        subCategory.click();
    }
    @Step("Select random brand")
    public void selectRandomBrand() {
        List<WebElement> elements = findElementsByKey("brandBracket");
        WebElement brand = getRandomElement(elements);
        brand.click();
    }
    @Step("Enter minimum <min> and maximum <max> prices")
    public void enterMinAndMaxPrice(String min,String max){
        WebElement minValue =findElementByKey("priceMin");
        minValue.sendKeys(min);
        WebElement maxValue =findElementByKey("priceMax");
        maxValue.sendKeys(max);
        WebElement priceEnter=findElementByKey("priceEntry");
        priceEnter.click();
    }

    @Step("Select random product")
    public void selectRandomProduct() {
        List<WebElement> elements = findElementsByKey("productList");
        WebElement element = getRandomElement(elements);
        getProductPrice(element);
        element.click();
    }
    public void getProductPrice(WebElement element) {
        WebElement priceLabel = element.findElement(getBy("productPriceInProductList"));
        String priceTxt = priceLabel.getText().split(" ")[0].replace(".", "").replace(",", ".");

        writeToCsv("information.csv", priceTxt);
    }
    public String getPriceInProductDetail() {
        WebElement priceInProductDetail = findElementByKey("priceInProductDetail");
        return priceInProductDetail.getText().split(" ")[0].replace(".", "").replace(",", ".");
    }

    public void getNameInProductDetail(){
        WebElement productName =findElementByKey("productName");

        String name = productName.getText();
        writeToCsv("information.csv",name);
    }


    @Step("Fiyat kontrolü yap")
    public void checkPrice() {
        String text = readCsvFile("information.csv");
        String priceTemp = text.split("\n")[0];
        getNameInProductDetail();
        Assert.assertEquals(priceTemp, getPriceInProductDetail());
    }

    @Step("Add product to <key>")
    public void addToCart(String key){
        WebElement addButton=findElementByKey(key);
        addButton.click();
    }

    @Step("Cart <key> count")
    public void cartCount(String key){
        WebElement cart=findElementByKey(key);
        cartValue=cart.getAttribute("value");
    }
    @Step("Click on <key> button")
    public void clickButton(String key){
        waitBySeconds(3);
        WebElement element=findElementByKey(key);
        element.click();
    }
    @Step("Get the price of <key>")
    public void getPrice(String key){
        WebElement element=findElementByKey(key);
        price=element.getText();
        System.out.println(price);
    }
    @Step("Check if the price <key> tripled <triple>")
    public void triplePrice(String key, String triple){
        WebElement element = findElementByKey(key);
        String price = element.getText().split(" ")[0];
        WebElement totalPriceLabel = findElementByKey(triple);
        String totalPrice = totalPriceLabel.getText().split(" ")[0];
        Assert.assertEquals(price, totalPrice);
    }
    @Step("Cart total <cartKey> and shipping price <shippingKey>")
    public void cartTotalAndShipping(String cartKey, String shippingKey) {
        WebElement element = findElementByKey(cartKey);
        String totalPrice = element.getText();
        WebElement shipping = findElementByKey(shippingKey);
        String shippingPrice = shipping.getText();
        writeToCsv("information.csv", totalPrice + "-" + shippingPrice);
        //* Click on "cartDelete" button
    }
    @Step("Clear all addresses in <key>")
    public void clearAddresses(String key) {
        List<WebElement> elements = findElementsByKey(key);
        int size = elements.size();
        int i = 0;
        while (i < size / 2) {
            clickElementByKey("btnDeleteAddress");
            clickElementByKey("btnConfirmDelete");
            i++;
            waitBySeconds(3);
        }
    }
    @Step("Send <keys> to the <textArea>")
    public void sendKeysToTextarea(String keys,String textArea){
        WebElement element=findElementByKey(textArea);
        element.click();
        WebElement textSpace = findElementByKey(keys);
        textSpace.sendKeys(keys);
    }
    @Step("Write <value> value to <key> element")
    public void writeValueToElement(String value, String key) {
        WebElement element = findElementByKey(key);
        element.sendKeys(value);
    }
    @Step("Click element by <key>")
    public void clickElementByKey(String key) {
        waitElementLoadByKey(key).click();
    }








}