package ru.motiw.web.elements.elementsweb.Documents.EditDocument;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;

/**
 * Элементы - форма Редактирования документа  - вкладка - Резолюции
 */
public class EditDocumentResolutionsTabElements {


    /**
     * Вкладка - Резолюции
     */
    public SelenideElement getResolutionsTab() {
        return $(By.xpath("//li[contains(@id,\"documentResolutionTab\")]//span[@class=\"x-tab-strip-text \"]"));
    }

    /**
     * Элемент грида Резолюций, для ожидания загрузки
     */
    public SelenideElement getElementGridOfResolutionsTab() {
        return $(By.xpath("//div[@id=\"documentResolutionTab\"]//div[@class=\"x-grid3-scroller\"]"));
    }


    /**
     * Создать резолюцию
     */
    public SelenideElement getButtonCreateResolution() {
        return $(By.xpath("//div[@id=\"documentResolutionTab\"]//button[text()='Создать']"));
    }

    /**
     * Значение в Колонке конкретной резолюции в гриде
     * @param textOfResolution - уникальный текст резолции по которому находим нужную запись в гриде
     */
    public SelenideElement getValueInColumnItemOfResolution(String textOfResolution, String expectedText) {
        return $(By.xpath("//div[contains(text(), '" + textOfResolution  + "')]/ancestor::div[contains(@class,\"x-grid3-row ux-maximgb-tg-level-1\")]//div[text()='" + expectedText + "']"));
    }

}
