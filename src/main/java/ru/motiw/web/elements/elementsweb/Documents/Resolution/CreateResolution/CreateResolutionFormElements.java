package ru.motiw.web.elements.elementsweb.Documents.Resolution.CreateResolution;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Элементы - форма создания Резолюции
 */
public class CreateResolutionFormElements {


    /**
     * Элемент загруженной формы Резолюции, для ожидания загрузки формы
     */
    public SelenideElement getElementResolutionFormBody() {
        return $(By.xpath("//div[@id=\"resolution-form-body\"]"));
    }


    /**
     * Кнопка открытия формы Добавления пользователя в роль "Ответственные руководители"
     */
    public SelenideElement getButtonToAddFormUsersInExecutiveManagerField() {
        return $(By.xpath("//div[@id=\"resolution-form-body\"]//div[text()=\"Ответственные руководители\"]/ancestor::table//div[@class=\"x-sel-users-button\"]"));
    }

    /**
     * поле "Авторы"
     */
    public SelenideElement fieldToAddAuthorField() {
        return $(By.xpath("(//div[@id=\"resolution-form-body\"]//div[text()=\"Авторы\"]/ancestor::table//div[@class=\"x-grid-cell-inner \"])[2]"));
    }

    /**
     * поле "Авторы" - кнопка удаления пользователя
     */
    public SelenideElement buttonToDeleteUserInAuthorField(String user) {
        return $(By.xpath("(//div[@id=\"resolution-form-body\"]//div[text()=\"Авторы\"]/ancestor::table//div[@class=\"x-grid-cell-inner \"])[2]//a[contains(text(), " + user + ")]/preceding-sibling::div"));
    }

    /**
     * поле "Ответственные руководители"
     */
    public SelenideElement fieldToAddExecutiveManagerField() {
        return $(By.xpath("(//div[@id=\"resolution-form-body\"]//div[text()=\"Ответственные руководители\"]/ancestor::table//div[@class=\"x-grid-cell-inner \"])[2]"));
    }

    /**
     * Поле Проект
     */
    public SelenideElement getProjectField() {
        return $(By.xpath("(//div[@id=\"resolution-form-body\"]//div[text()=\"Проект\"]/ancestor::table//div[@class=\"x-grid-cell-inner \"])[2]"));
    }

    /**
     * Поле Проект - Открытие контекстного списка
     */
    public SelenideElement getProjectFieldButtonForOpenContext() {
        return $(By.xpath("//div[@class=\"x-editor x-small-editor x-grid-editor x-grid-cell-editor x-layer x-editor-default x-border-box\"]//div[@class=\"x-form-trigger x-form-trigger-default x-form-arrow-trigger x-form-arrow-trigger-default  x-form-trigger-focus\"]"));
    }


    /**
     * поле "Текст резолюций" - для активации поля
     */
    public SelenideElement fieldToActivateTextOfResolution() {
        return $(By.xpath("(//div[@id=\"resolution-form-body\"]//div[text()=\"Текст резолюции\"]/ancestor::table//div[@class=\"x-grid-cell-inner \"])[2]"));
    }

    /**
     * поле "Текст резолюций" - для ввода значения
     */
    public SelenideElement fieldToAddTextOfResolution() {
        return $(By.xpath("//div[@id=\"resolution-form-body\"]//textarea"));
    }

    /**
     * Сохранить
     */
    public SelenideElement getButtonSaveResolution() {
        return $(By.xpath("//span[@class=\"x-btn-inner x-btn-inner-default-small\" and text()=\"Сохранить\"]/ancestor::span[@data-ref=\"btnEl\"]"));
    }

}
