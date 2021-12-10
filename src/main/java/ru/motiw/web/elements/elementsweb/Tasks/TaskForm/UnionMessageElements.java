package ru.motiw.web.elements.elementsweb.Tasks.TaskForm;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;

/**
 * Форма созданной задачи
 */
public class UnionMessageElements {

    @FindBy(xpath = "//span[text()][ancestor::em[contains(@class,'x-tab')]][ancestor::li[not(@style='display: none;')]][text()='Изолированные рабочие группы']")
    private SelenideElement tabIWG;

    /**
     * Вкладка - Изолированные рабочие группы
     *
     * @return элемент вкладка ИРГ в форме задачи
     */
    public SelenideElement getTabIWG() {
        return tabIWG;
    }


    /**
     * Кнопка "Документ"
     */
    public SelenideElement getButtonGoToDocument() {
        return $(By.xpath("//button[text()=\"Документ\"]"));
    }


    /**
     * Кнопка "Согласовать"
     */
    public SelenideElement getButtonConsideration() {
        return $(By.xpath("//button[text()=\"Согласовать\"]"));
    }

    /**
     * Кнопка "Согласовать с замечаниями"
     */
    public SelenideElement getButtonConsiderationWithComment() {
        return $(By.xpath("//button[text()=\"Согласовать с замечаниями\"]"));
    }

    /**
     * Кнопка "Отказаться согласовать"
     */
    public SelenideElement getButtonDenialReview() {
        return $(By.xpath("//button[text()=\"Отказаться согласовать\"]"));
    }


    /**
     * Кнопка "На доработку"
     */
    public SelenideElement getButtonBackToRevision() {
        return $(By.xpath("//button[text()=\"На доработку\"]"));
    }


}
