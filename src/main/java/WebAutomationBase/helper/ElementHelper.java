package WebAutomationBase.helper;

import WebAutomationBase.model.ElementInfo;
import org.openqa.selenium.By;

public class ElementHelper
{

    public static By getElementByElementInfo(ElementInfo elementInfo) {
        By element = null;
        if (elementInfo.getType().equals("css"))
            element = By.cssSelector(elementInfo.getValue());
        else if (elementInfo.getType().equals("id"))
            element = By.id(elementInfo.getValue());
        else if (elementInfo.getType().equals("xpath"))
            element = By.xpath(elementInfo.getValue());

        return element;
    }
}