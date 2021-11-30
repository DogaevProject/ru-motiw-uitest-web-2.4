package ru.motiw.web.elements.elementsweb.Documents.CreateDocument;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
/**
 * Элементы - форма Создания документа (Документы / Создать документ) - вкладка - Маршруты
 */
public class NewDocumentRouteTabElements {

    @FindBy(css = "#routeframe")
    private SelenideElement frameRoute;

    @FindBy(xpath = "(//span[contains(@class,'x-tab-strip')][ancestor::li[contains(@id,'documentRoutes')]])[2]")
    private SelenideElement routeTab;

    @FindBy(css = "#cb_route_name")
    private SelenideElement orderOfConsideration;

    @FindBy(xpath = "//input[@id='cb_route_name']/../img")
    private SelenideElement listOfRoutes;

    @FindBy(xpath = "//table//td//span[contains(text(),'1.')]/../../..//img[@onclick]")
    private SelenideElement addAUserToBlockDiagram;

    @FindBy(xpath = "//table//td//span[contains(text(),'1.')]/ancestor::tr/td[5]")
    private SelenideElement reviewDuration;

    @FindBy(xpath = "//tr[contains(@id,'numberfield')]//input")
    private SelenideElement inputReviewDuration;

    /**
     * Добавить пользователя
     */
    public SelenideElement getAddAUserToBlockDiagram() {
        return addAUserToBlockDiagram;
    }

    /**
     * Добавить пользователя - элемент в строке конкретного этапа мс
     * @param nameOfBlock - название этапа мс
     */
    public SelenideElement getAddAUserToBlockDiagram(String nameOfBlock) {
        return $(By.xpath("//table//td//span[contains(text(),'" + nameOfBlock + "')]/ancestor::tr//span[contains(text(),'1.')]/../../..//img[@onclick]"));
    }

    /**
     * Поле для выбора пользователей из выпадающего списка блока. Элемент в строке конкретного этапа мс.
     * @param nameOfBlock - название этапа мс
     */
    public SelenideElement getFieldForOpenContextMenuOfBlockDiagram(String nameOfBlock) {
        return $(By.xpath("//table//td//span[contains(text(),'" + nameOfBlock + "')]/ancestor::tr/following::tr[1]/td[1]//span[@class='x-tree-node-text ']"));
    }

    /**
     * Элемент для открытия выпадающего списка блока.
     */
    public SelenideElement openContextMenuOfBlockDiagram() {
        return $(By.xpath("//div[@class='x-trigger-index-0 x-form-trigger x-form-arrow-trigger x-form-trigger-first']"));
    }

    /**
     * Выбор пользователя в выпадающем списке блока.
     */
    public SelenideElement getUserInContextMenuOfBlockDiagram(String userName) {
        return $(By.xpath("//div[@class=\"x-boundlist x-boundlist-floating x-layer x-boundlist-default x-border-box\"]//li[contains(text(),'" + userName + "')]"));
    }


    /**
     * Выбор поля - Порядок рассмотрения
     */
    public SelenideElement getOrderOfConsideration() {
        return orderOfConsideration;
    }

    /**
     * Выбор списка - Маршруты
     */
    public SelenideElement getListOfRoutes() {
        return listOfRoutes;
    }

    /**
     * Фрейм - МС
     */
    public SelenideElement getFrameRoute() {
        return frameRoute;
    }

    /**
     * Вкладка - Маршрут
     */
    public SelenideElement getRouteTab() {
        return routeTab;
    }

    /**
     * Длительность рассмотрения
     */
    public SelenideElement getReviewDuration() {
        return reviewDuration;
    }

    /**
     * Длительность рассмотрения - поле в строке конкретного этапа мс
     * @param nameOfBlock - название этапа мс
     */
    public SelenideElement getReviewDuration(String nameOfBlock) {
        return $(By.xpath("//span[contains(text(),'" + nameOfBlock + "')]/ancestor::tr/td[5]"));
    }

    /**
     * Поле ввода - Длительность рассмотрения
     */
    public SelenideElement getInputReviewDuration() {
        return inputReviewDuration;
    }

}
