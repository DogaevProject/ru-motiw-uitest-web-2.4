package ru.motiw.web.elements.elementsweb.Documents.EditDocument;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Элементы - форма Редактирования документа  -  вкладка - Маршрут
 */
public class EditDocumentRouteTabElements {

    /**
     * Вкладка - Маршрут
     */
    public SelenideElement getRoutesTab() {
        return $(By.xpath("//li[contains(@id,\"documentRoutes\")]//span[@class=\"x-tab-strip-text \"]"));
    }

    /**
     * Вкладка Маршрут -  значение в колонке Статус для рассмотревшего пользователя в строке конкретного этапа мс = Рассмотрен
     *
     * @param nameOfBlock - название этапа мс
     */
    public SelenideElement statusBlockDiagramIsReviewed(String nameOfBlock, String userName) {
        return $(By.xpath("//table//td//span[contains(text(),'" + nameOfBlock + "')]/ancestor::tr/following::tr[1]//a[contains(text(),'" + userName  + "')]/ancestor::tr//td[10]/div[text()='Рассмотрен']"));
    }

    /**
     * Вкладка Маршрут -  значение в колонке Статус для рассмотревшего пользователя в строке конкретного этапа мс = Рассмотрен (с замечаниями)
     *
     * @param nameOfBlock - название этапа мс
     */
    public SelenideElement statusBlockDiagramIsReviewedWithComment(String nameOfBlock, String userName) {
        return $(By.xpath("//table//td//span[contains(text(),'" + nameOfBlock + "')]/ancestor::tr/following::tr[1]//a[contains(text(),'" + userName  + "')]/ancestor::tr//td[10]/div[text()='Рассмотрен (с замечаниями)']"));
    }

    /**
     * Вкладка Маршрут -  значение в колонке Статус для рассмотревшего пользователя в строке конкретного этапа мс = Отказ согласовать
     *
     * @param nameOfBlock - название этапа мс
     */
    public SelenideElement statusBlockDiagramIsDenialReview(String nameOfBlock, String userName) {
        return $(By.xpath("//table//td//span[contains(text(),'" + nameOfBlock + "')]/ancestor::tr/following::tr[1]//a[contains(text(),'" + userName  + "')]/ancestor::tr//td[10]/div[text()='Отказ согласовать']"));
    }

    /**
     * Вкладка Маршрут -  значение в колонке Статус для рассмотревшего пользователя в строке конкретного этапа мс = Документ отправлен на доработку
     *
     * @param nameOfBlock - название этапа мс
     */
    public SelenideElement statusBlockDiagramIsBackToRevision(String nameOfBlock, String userName) {
        return $(By.xpath("//table//td//span[contains(text(),'" + nameOfBlock + "')]/ancestor::tr/following::tr[1]//a[contains(text(),'" + userName  + "')]/ancestor::tr//td[10]/div[text()='Документ отправлен на доработку']"));
    }

}
