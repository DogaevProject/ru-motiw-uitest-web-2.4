package ru.motiw.mobile.elements.Internal;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.motiw.web.steps.BaseSteps;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Просмотрщик Файлов
 */
public class FilesPreviewElementsMobile extends BaseSteps {

    /**
     * Элемент просмотрщика файлов pdfjs, который появляется, когда загружен файл
     *
     * @return
     */
    public SelenideElement getPdfViewer() {
        return $(By.xpath("//div[@class=\"pdfViewer\"]"));
    }


    /**
     * Панель инструментов граф.комменатриев
     *
     * @return
     */
    public SelenideElement getAnnotationControlsToolbar() {
        return $(By.xpath("//div[@class=\"annotation-controls-toolbar\"]"));
    }

    /**
     * Кнопка - с выпадающим листом списка авторов комментариев
     *
     * @return
     */
    public SelenideElement getAnnotationTrigger() {
        return $(By.xpath("//div[contains(@class,\"annotation-layers-trigger\")]"));
    }

    /**
     * Кнопки - переключатель отображения комментариев
     *
     * @return
     */
    public ElementsCollection getAnnotationList() {
        return $$(By.xpath("//div[@class=\"annotation-layers-list\"]/div[contains(@class,\"annotation-list-el\")]"));
    }

    /**
     * Кнопка - переключатель отображения комментариев (отображение комментария включенно)
     *
     * @return
     */
    public SelenideElement getActiveAnnotationTriggerOfUserList(String firstCharFromLastName) {
        return $(By.xpath("//div[@class=\"annotation-layers-list\"]/div[@class=\"annotation-list-el active\" and contains(text(),'" + firstCharFromLastName + "')]"));
    }

    /**
     * Кнопка - переключатель отображения комментариев (отображение комментария выключенно)
     *
     * @return
     */
    public SelenideElement getNotActiveAnnotationTriggerOfUserList(String firstCharFromLastName) {
        return $(By.xpath("//div[@class=\"annotation-layers-list\"]/div[@class=\"annotation-list-el\" and contains(text(),'" + firstCharFromLastName + "')]"));
    }


    public SelenideElement getPan() {
        return $(By.xpath("//*[@class=\"annotation-controls pen-mode-button\"]"));
    }

    public SelenideElement getMarker() {
        return $(By.xpath("//*[@class=\"annotation-controls marker-mode-button\"]"));
    }

    public SelenideElement getEraser() {
        return $(By.xpath("//*[@class=\"annotation-controls erase-mode-button\"]"));
    }

    public SelenideElement getPanActive() {
        return $(By.xpath("//*[@class=\"annotation-controls pen-mode-button active\"]"));
    }

    public SelenideElement getMarkerActive() {
        return $(By.xpath("//*[@class=\"annotation-controls marker-mode-button active\"]"));
    }

    public SelenideElement getEraserActive() {
        return $(By.xpath("//*[@class=\"annotation-controls erase-mode-button active\"]"));
    }


}
