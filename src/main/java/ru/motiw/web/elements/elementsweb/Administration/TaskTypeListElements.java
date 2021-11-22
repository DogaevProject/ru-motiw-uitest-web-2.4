package ru.motiw.web.elements.elementsweb.Administration;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Элементы грида - Типов объектов
 * Общая форма гридов (Администрирование / Справочники, Типы таблиц, Типы задач и пр..)
 */
public class TaskTypeListElements {

    @FindBy(xpath = "//input")
    private SelenideElement nameObject;

    @FindBy(xpath = "//div[count(a)=4]/a[2]//span[position()=2]")
    private SelenideElement confirmationButtonObjectDeletion;

    @FindBy(xpath = "//div[count(a)=4]/a[3]//span[position()=2]")
    private SelenideElement cancelButtonObjectDeletion;

    @FindBy(xpath = "(//a[contains(@id,'button')]//span[contains(@style,'image:url')])[1]")
    private SelenideElement addTypesObject;

    @FindBy(xpath = "(//a[contains(@id,'button')]//span[contains(@style,'image:url')])[2]")
    private SelenideElement editTypesObject;

    @FindBy(xpath = "(//a[contains(@id,'button')]//span[contains(@style,'image:url')])[3]")
    private SelenideElement removeTypesObject;

    @FindBy(xpath = "//*[contains (@class, 'message-box')]//a[1]")
    private SelenideElement OkAddObject;


    /**
     * ОК - Подтверждение добавления объекта
     */
    public SelenideElement getOkAddObject() {
        return OkAddObject;
    }

    /**
     * Поле ввода Названия объекта
     */
    public SelenideElement getNameObject() {
        return nameObject;
    }

    /**
     * Кнопка - Да, в диалоговом окне при удалении объекта
     */
    public SelenideElement getConfirmationButtonObjectDeletion() {
        return confirmationButtonObjectDeletion;
    }

    /**
     * Кнопка - Нет, в диалоговом окне при удалении объекта
     */
    public SelenideElement getCancelButtonObjectDeletion() {
        return cancelButtonObjectDeletion;
    }

    /**
     * Добавить объект (кнопка "Добавить" в гриде)
     */
    public SelenideElement getAddTypesObject() {
        return addTypesObject;
    }

    /**
     * Редактировать объект (кнопка "Редактировать" в гриде)
     */
    public SelenideElement getEditTypesObject() {
        return editTypesObject;
    }

    /**
     * Неактивная кнопка "Редактировать" в гриде
     */
    public SelenideElement getEditDisabledButton() {
        return $(By.xpath("//a[@class=\"x-btn x-unselectable x-box-item x-toolbar-item x-btn-default-toolbar-small x-item-disabled x-btn-disabled\" and @data-qtip=\"Редактировать\" and @aria-disabled=\"true\"]"));

    }


    /**
     * Удалить объект (кнопка "Удалить" в гриде)
     */
    public SelenideElement getRemoveTypesObject() {
        return removeTypesObject;
    }

    /**
     * Область вне записей грида (при клике на нее снятие фокуса с выбранной записи)
     */
    public SelenideElement getSpaceWithoutItem() {
        return $(By.xpath("//div[@class=\"x-panel-body x-grid-no-row-lines x-grid-body x-panel-body-default x-panel-body-default x-noborder-rl\"]"));
    }

    /**
     * Все записи в гриде
     */
    public ElementsCollection getAllItems() {
        return $$(By.xpath("//div[@class=\"x-grid-item-container\"]//table"));
    }

}

