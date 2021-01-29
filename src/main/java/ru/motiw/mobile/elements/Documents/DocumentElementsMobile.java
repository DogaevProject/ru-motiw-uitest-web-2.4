package ru.motiw.mobile.elements.Documents;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;

/**
 * Форма созданного документа
 */
public class DocumentElementsMobile {

    @FindBy(xpath = "//div[contains(@id,\"ext-formdialog\") and contains(@class,\"x-dialog\") and not (contains(@class,\"x-hidden\"))]")
    private SelenideElement formOfResolution;

    @FindBy(xpath = "//div[contains(@id,\"ext-resolutionlist-item\")]")
    private ElementsCollection itemInResolutionList;

    @FindBy(xpath = "//div[@class=\"x-component x-label resolution-list-text x-layout-auto-item\"]//div[@class=\"x-innerhtml\"]")
    private ElementsCollection textOfResolutionInItemInResolutionList;

    @FindBy(xpath = "//div[@class=\"x-component x-label resolution-list-authors x-layout-box-item x-layout-hbox-item\"]//div[@class=\"x-innerhtml\"]")
    private ElementsCollection authorsOfResolutionInItemInResolutionList;

    @FindBy(xpath = "//div[contains(@id,'object') and not(contains(@class,\"x-hidden-display\")) and not(contains(@id,\"ext-object-toolbar\"))]//div[contains(@class,\"x-component x-button x-icon-align-top x-widthed x-has-icon x-has-text\") and not(contains(@class,\"x-hidden-display\"))]")
    private ElementsCollection allButtonsOfTab;

    /**
     * Форма резолюции отображаемаемая в данный момент
     *
     * @return
     */
    public SelenideElement getFormOfResolution() {
        return formOfResolution;
    }

    /**
     * Карточки предпросмотра резолюции в списке резолюций
     *
     * @return
     */
    public ElementsCollection getItemInResolutionList() {
        return itemInResolutionList;
    }

    /**
     * Текст резолюции в списке резолюций
     *
     * @return все элементы из списка всех резолюций
     */
    public ElementsCollection getTextOfResolutionInItemInResolutionList() {
        return textOfResolutionInItemInResolutionList;
    }

    /**
     * Автор резолюции в списке резолюций
     *
     * @return все элементы из списка всех резолюций
     */
    public ElementsCollection getAuthorsOfResolutionInItemInResolutionList() {
        return authorsOfResolutionInItemInResolutionList;
    }

    /**
     * Набор элементов всех кнопкок на тулбаре
     *
     * @return все элементы из списка всех резолюций
     */
    public ElementsCollection getAllButtonsOfTab() {
        return allButtonsOfTab;
    }


    /**
     * Автор резолюции в конкретной карточке списка резолюций
     *
     * @param textOfResolution Текст резолюции
     * @return
     */
    public SelenideElement getAuthorsOfResolutionInCertainItem(String textOfResolution) {
        return $(By.xpath("//div[contains(text(),'" + textOfResolution + "')]/ancestor::div[contains(@id,\"ext-resolutionlist-item\")]" +
                "//div[@class=\"x-component x-label resolution-list-authors x-layout-box-item x-layout-hbox-item\"]" +
                "//div[@class=\"x-innerhtml\"]"));
    }

    /**
     * Текст резолюции в конкретной карточке списка резолюций
     *
     * @param textOfResolution Текст резолюции
     * @return
     */
    public SelenideElement getTextOfResolutionInCertainItem(String textOfResolution) {
        return $(By.xpath("//div[@class=\"x-component x-label resolution-list-text x-layout-auto-item\"]//div[contains(text(),'" + textOfResolution + "')]"));
    }

    /**
     * Кнопки на тулбаре
     *
     * @param nameOfButton название кнопки
     * @return
     */
    public SelenideElement getButtonOfTab(String nameOfButton) {
        return $(By.xpath("//div[contains(@id,'object') and not(contains(@class,\"x-hidden-display\")) and not(contains(@id,\"ext-object-toolbar\"))]" +
                "//div[text()='" + nameOfButton + "']//ancestor::div[contains(@class,\"x-component x-button x-icon-align-top x-widthed x-has-icon\") and not(contains(@class,\"x-hidden-display\"))]"));
    }

    /**
     * input поля Тип Сотрудник в форме резолюции
     */
    public SelenideElement getInputEmployeeFieldInFormOfCreateResolution(String nameField) {
        return $(By.xpath("//div[contains(@id,\"ext-formdialog\") and contains(@class,\"x-dialog\") and not (contains(@class,\"x-hidden\"))]" +
                "//span[text()='" + nameField + "']//ancestor::div[contains(@class,\"x-field\")]//div[@class=\"x-input-el\"]"));

    }

    /**
     * textarea в форме резолюции
     */
    public SelenideElement getTextareaInFormOfCreateResolution(String nameField) {
        return $(By.xpath("//div[contains(@id,\"ext-formdialog\") and contains(@class,\"x-dialog\") and not (contains(@class,\"x-hidden\"))]" +
                "//span[text()='" + nameField + "']//ancestor::div[contains(@class,\"x-field\")]//textarea"));

    }


    /**
     *  Панель проекта резолюции внизу
     */
    public SelenideElement getPanelProjectOfResolution() {
        return $(By.xpath("//div[contains(@id, \"ext-resolution-approvepanel\")]"));

    }


    /**
     *  Кнопка утверждения на Панели проекта резолюции внизу
     */
    public SelenideElement getButtonForApproveOnPanelProjectOfResolution() {
        return $(By.xpath("//div[contains(@id, \"ext-resolution-approvepanel\")]//div[@class=\"x-container x-component x-layout-box-item x-layout-hbox-item\"]"));

    }

}
