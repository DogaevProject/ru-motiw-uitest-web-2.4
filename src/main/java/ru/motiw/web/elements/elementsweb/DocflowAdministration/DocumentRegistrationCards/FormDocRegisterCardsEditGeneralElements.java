package ru.motiw.web.elements.elementsweb.DocflowAdministration.DocumentRegistrationCards;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

/**
 * Форма редактирования РКД - ОБЩЕЕ
 */
public class FormDocRegisterCardsEditGeneralElements {


    @FindBy(xpath = "(//div[contains(@id,'tabbar')]/following-sibling::*)[1]//a[1]//span")
    private SelenideElement generalTab;

    @FindBy(xpath = "//div[contains(@id,'textfield') and @data-ref='bodyEl']//input")
    private SelenideElement setNameDocRegCards;

    @FindBy(xpath = "(//table[@id][ancestor::fieldset[contains(@id,'fieldset')]]//td[3]/div)[1]")
    private SelenideElement documentStatesOnReview;

    @FindBy(xpath = "(//table[@id][ancestor::fieldset[contains(@id,'fieldset')]]//td[3]/div)[2]")
    private SelenideElement documentStatesReviewed;

    @FindBy(xpath = "(//table[@id][ancestor::fieldset[contains(@id,'fieldset')]]//td[3]/div)[3]")
    private SelenideElement documentStatesOnApproval;

    @FindBy(xpath = "(//table[@id][ancestor::fieldset[contains(@id,'fieldset')]]//td[3]/div)[5]")
    private SelenideElement documentStatesOnExecution;

    @FindBy(xpath = "(//table[@id][ancestor::fieldset[contains(@id,'fieldset')]]//td[3]/div)[6]")
    private SelenideElement documentStatesInArchive;

    @FindBy(xpath = "//input[contains(@id,'textfield') and contains(@name,'status_text')][ancestor::fieldset[contains(@id,'fieldset')]]")
    private SelenideElement setValueDocumentStates;

    @FindBy(xpath = "(//div[contains(@id,'radiofield-')]//span[@data-ref])[2]")
    private SelenideElement dateDoesNotMove;

    @FindBy(xpath = "(//div[contains(@id,'radiofield-')]//span[@data-ref])[6]")
    private SelenideElement dateMovesForward;

    /*
    @FindBy(xpath = "(//div[contains(@id,'radiofield-')]//span[@data-ref])[3]")
    private SelenideElement dateMovesBack;
    */

    @FindBy(xpath = "(//div[contains(@id,'radio-')]//span[@data-ref])[10]")
    private SelenideElement dateMovesBack;

/*
    @FindBy(xpath = "(//span[contains(@id,'checkbox')]//../input[@role='checkbox']/../span)[1]")
    private SelenideElement atFirstRevisionScheme;

    @FindBy(xpath = "(//span[contains(@id,'checkbox')]//../input[@role='checkbox']/../span)[2]")
    private SelenideElement forCompletionInTighterPoint;

    @FindBy(xpath = "(//span[contains(@id,'checkbox')]//../input[@role='checkbox']/../span)[3]")
    private SelenideElement onCompletionTheNewScheme;
*/


    @FindBy(xpath = "(//span[contains(@id,'checkbox')]//../input/..)[1]")
    private SelenideElement atFirstRevisionScheme;

    @FindBy(xpath = "(//span[contains(@id,'checkbox')]//../input/..)[2]")
    private SelenideElement forCompletionInTighterPoint;

    @FindBy(xpath = "(//span[contains(@id,'checkbox')]//../input/..)[3]")
    private SelenideElement onCompletionTheNewScheme;

    @FindBy(xpath = "(//input[contains(@id,'combo') and (@placeholder)])[1]")
    private SelenideElement openFilesForEdit;

    @FindBy(xpath = "(//input[contains(@id,'combo') and (@placeholder)])[2]")
    private SelenideElement autoСalculationNumeratorFields;

    @FindBy(xpath = "(//a[contains(@id,'button')][ancestor::div[contains(@id,'toolbar') and contains(@class,'x-toolbar x-docked x-toolbar-default')]]//span[string-length(text())>=3])[1]")
    private SelenideElement saveAllChangesInDocument;

    /**
     * Вкладка - Общее
     */
    public SelenideElement getGeneralTab() {
        return generalTab;
    }

    /**
     * Название регистрационной карточки документа
     */
    public SelenideElement getNameDocRegCards() {
        return setNameDocRegCards;
    }

    /**
     * Статусы документа: На рассмотрении
     */
    public SelenideElement getDocumentStatesOnReview() {
        return documentStatesOnReview;
    }

    /**
     * Статусы документа: Рассмотрен
     */
    public SelenideElement getDocumentStatesReviewed() {
        return documentStatesReviewed;
    }

    /**
     * Статусы документа: На подписании
     */
    public SelenideElement getDocumentStatesOnApproval() {
        return documentStatesOnApproval;
    }

    /**
     * Статусы документа: На исполнении
     */
    public SelenideElement getDocumentStatesOnExecution() {
        return documentStatesOnExecution;
    }

    /**
     * Статусы документа: В архив
     */
    public SelenideElement getDocumentStatesInArchive() {
        return documentStatesInArchive;
    }

    /**
     * Статусы документа
     */
    public SelenideElement getValueDocumentStates() {
        return setValueDocumentStates;
    }


    /**
     * Направление смещения даты при попадании на нерабочее время:
     * Дата не меняется
     */
    public SelenideElement getDateDoesNotMove() {
        return dateDoesNotMove;
    }

    /**
     * Направление смещения даты при попадании на нерабочее время
     * Дата сдвигается вперед
     */
    public SelenideElement getDateMovesForward() {
        return dateMovesForward;
    }

    /**
     * Направление смещения даты при попадании на нерабочее время
     * Дата сдвигается назад
     */
    public SelenideElement getDateMovesBack() {
        return dateMovesBack;
    }

    /**
     * Настройки по умолчанию при отправке документа на доработку
     * Возврат на доработку с начала текущей схемы
     */
    public SelenideElement getAtFirstRevisionScheme() {
        return atFirstRevisionScheme;
    }

    /**
     * Настройки по умолчанию при отправке документа на доработку
     * Возврат на доработку в ту же точку рассмотрения
     */
    public SelenideElement getForCompletionInTighterPoint() {
        return forCompletionInTighterPoint;
    }

    /**
     * Настройки по умолчанию при отправке документа на доработку
     * Возврат на доработку с новой схемой
     */
    public SelenideElement getOnCompletionTheNewScheme() {
        return onCompletionTheNewScheme;
    }

    /**
     * Открывать файлы для редактирования
     */
    public SelenideElement getOpenFilesForEdit() {
        return openFilesForEdit;
    }

    /**
     * Автоматическое вычисление полей-нумераторов
     */
    public SelenideElement getAutoСalculationNumeratorFields() {
        return autoСalculationNumeratorFields;
    }

    /**
     * Сохранить - все изменения по документу
     */
    public SelenideElement getSaveAllChangesInDocument() {
        return saveAllChangesInDocument;
    }
}
