package ru.motiw.web.elements.elementsweb.Documents.EditDocument;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Элементы - форма Редактирования документа  - вкладка - Карточка
 */
public class EditDocumentCartTabElements {

    /**
     * Кнопка "Сохранить"
     */
    public SelenideElement geSaveButton() {
        return $(By.xpath("//button[text()=\"Сохранить\"]/ancestor::table"));
    }

    /**
     * Кнопка "Согласовать"
     */
    public SelenideElement getButtonConsideration() {
        return $(By.xpath("//button[text()=\"Согласовать\"]/ancestor::table"));
    }

    /**
     * Кнопка "Согласовать с замечаниями"
     */
    public SelenideElement getButtonConsiderationWithComment() {
        return $(By.xpath("//button[text()=\"Согласовать с замечаниями\"]/ancestor::table"));
    }

    /**
     * Кнопка "Отказаться согласовать"
     */
    public SelenideElement getButtonDenialReview() {
        return $(By.xpath("//button[text()=\"Отказаться согласовать\"]/ancestor::table"));
    }

    /**
     * Кнопка "На доработку"
     */
    public SelenideElement getButtonBackToRevision() {
        return $(By.xpath("//button[text()=\"На доработку\"]/ancestor::table"));
    }

    /**
     * Кнопка "Вернуть на рассмотрение"
     */
    public SelenideElement getButtonBackToConsideration() {
        return $(By.xpath("//button[text()=\"Вернуть на рассмотрение\"]/ancestor::table"));
    }

    /**
     * Кнопка "Прочее"
     */
    public SelenideElement getButtonOthersOperations() {
        return $(By.xpath("//button[text()=\"Прочее...\"]"));
    }


    /**
     * Область редактирования поля типа Текст -  Форма ввода текста перед выполнением операции
     */
    public SelenideElement getCkeBody() {
        return $(By.cssSelector("body"));
    }


    /**
     * Кнопка "OK"  -  Форма ввода текста перед выполнением операции
     */
    public SelenideElement getButtonOkInFormForAddTextBeforeConsideration() {
        return $(By.xpath("//div[contains(@class,\" x-window\")]//button[text()=\"OK\" or text()=\"ОК\"]"));
    }


    /**
     * Кнопка "OK"  -  Форма перед выполнением операции Возрата на рассмотрение с начала мс
     */
    public SelenideElement getButtonOkInFormForReturnToBeginning() {
        return $(By.xpath("//iframe[@id=\"newrouteframe\"]/ancestor::div//button[text()=\"OK\"]"));
    }


    /**
     *  Форма перед выполнением операции Возрата на рассмотрение с начала мс - кнопка снятия признака пропуска блока
     *
     * @param nameOfBlock - название этапа мс
     */
    public SelenideElement turnOnConsiderationForBlockDiagram(String nameOfBlock) {
        return $(By.xpath("//table//td//span[contains(text(),'" + nameOfBlock + "')]/ancestor::tr//img[@title=\"Вернуть блок в список\"]"));
    }


}
