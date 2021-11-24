package ru.motiw.mobile.steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import ru.motiw.mobile.elements.Documents.DocumentElementsMobile;
import ru.motiw.mobile.elements.Internal.FilesPreviewElementsMobile;
import ru.motiw.mobile.model.AuthorOfAnnotation;
import ru.motiw.mobile.model.ControlsForAnnotationOnFile;
import ru.motiw.mobile.steps.ValidationStepsMobile.ValidationAnnotationOnFilesStepsMobile;
import ru.motiw.web.model.Administration.Users.Employee;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static ru.motiw.mobile.model.AuthorOfAnnotation.NumbersOfAnnotation.FirstAnnotation;
import static ru.motiw.mobile.model.AuthorOfAnnotation.NumbersOfAnnotation.SecondAnnotation;
import static ru.motiw.mobile.model.ControlsForAnnotationOnFile.*;
import static ru.motiw.mobile.steps.ValidationStepsMobile.ValidationAnnotationOnFilesStepsMobile.getInitialAuthorOfAnnotation;

/**
 * Комментирование
 */
public class AnnotationOnFilesSteps {

    private FilesPreviewElementsMobile filesPreviewElementsMobile = page(FilesPreviewElementsMobile.class);
    private DocumentElementsMobile documentElementsMobile = page(DocumentElementsMobile.class);

    /**
     * Выбор контрола для комментирования pdf-файла на панели инструментов
     */
    private AnnotationOnFilesSteps getControlForPdf(ControlsForAnnotationOnFile control) {
        switchTo().frame($(By.xpath("//iframe")));  //Переходим во фрейм просмотра файлов
        getControl(control);
        switchTo().defaultContent();  //Уходим из фрейма просмотра файлов
        return this;
    }

    /**
     * Выбор контрола для комментирования на панели инструментов
     */
    private AnnotationOnFilesSteps getControl(ControlsForAnnotationOnFile control) {
        switch (control) {
            case PEN:
                filesPreviewElementsMobile.getPan().click();
                break;
            case MARKER:
                filesPreviewElementsMobile.getMarker().click();
                break;
            case ERASER:
                filesPreviewElementsMobile.getEraser().click();
                break;
        }
        return this;
    }


    /**
     * Выключение контрола для комментирования pdf-файла на панели инструментов
     */
    private AnnotationOnFilesSteps offControlForPdf(ControlsForAnnotationOnFile control) {
        switchTo().frame($(By.xpath("//iframe")));  //Переходим во фрейм просмотра файлов
        offControl(control);
        switchTo().defaultContent();  //Уходим из фрейма просмотра файлов
        return this;
    }

    /**
     * Выключение контрола для комментирования на панели инструментов
     */
    private AnnotationOnFilesSteps offControl(ControlsForAnnotationOnFile control) {
        switch (control) {
            case PEN:
                filesPreviewElementsMobile.getPanActive().click();
                break;
            case MARKER:
                filesPreviewElementsMobile.getMarkerActive().click();
                break;
            case ERASER:
                filesPreviewElementsMobile.getEraserActive().click();
                break;
        }
        return this;
    }

    /**
     * Открывает/Закрывает список кнопок-переключателей отображения комментариев pdf-файла
     */
    public AnnotationOnFilesSteps clickButtonOfListOfAuthorsAnnotationsOnPdf() {
        switchTo().frame($(By.xpath("//iframe")));  //Переходим во фрейм просмотра файлов
        clickButtonOfListOfAuthorsAnnotations();
        switchTo().defaultContent();  //Уходим из фрейма просмотра файлов
        return this;
    }

    /**
     * Открывает/Закрывает список кнопок-переключателей отображения комментариев
     */
    public AnnotationOnFilesSteps clickButtonOfListOfAuthorsAnnotations() {
        sleep(2000);
        filesPreviewElementsMobile.getAnnotationTrigger().waitUntil(Condition.visible, 5000);
        filesPreviewElementsMobile.getAnnotationTrigger().click();
        return this;
    }


    /**
     * Включаем отображение выключенного комментария pdf-файла
     *
     * @param author
     * @return
     */
    public AnnotationOnFilesSteps enableViewAnnotationByAuthorOnPdf(Employee author) {
        switchTo().frame($(By.xpath("//iframe")));  //Переходим во фрейм просмотра файлов
        enableViewAnnotationByAuthor(author);
        switchTo().defaultContent();  //Уходим из фрейма просмотра файлов
        return this;
    }


    /**
     * Включаем отображение выключенного комментария
     *
     * @param author
     * @return
     */
    public AnnotationOnFilesSteps enableViewAnnotationByAuthor(Employee author) {
        filesPreviewElementsMobile.getNotActiveAnnotationTriggerOfUserList(getInitialAuthorOfAnnotation(author)).click();
        validateThat().triggerOfViewAnnotationByAuthorActive(author);
        return this;
    }

    /**
     * Выключаем отображение включенного комментария pdf-файла
     *
     * @param author
     * @return
     */
    public AnnotationOnFilesSteps disableViewAnnotationByAuthorOnPdf(Employee author) {
        switchTo().frame($(By.xpath("//iframe")));  //Переходим во фрейм просмотра файлов
        disableViewAnnotationByAuthor(author);
        switchTo().defaultContent();  //Уходим из фрейма просмотра файлов
        return this;
    }

    /**
     * Выключаем отображение включенного комментария
     *
     * @param author
     * @return
     */
    public AnnotationOnFilesSteps disableViewAnnotationByAuthor(Employee author) {
        filesPreviewElementsMobile.getActiveAnnotationTriggerOfUserList(getInitialAuthorOfAnnotation(author)).click();
        validateThat().triggerOfViewAnnotationByAuthorNotActive(author);
        return this;
    }

    /**
     * Добавления граф.коментария на pdf-файле (ручка)
     */
    public AnnotationOnFilesSteps addCommentOfPenOnPdfFile(AuthorOfAnnotation authorOfAnnotation) {
        addCommentOnPdfFile(authorOfAnnotation, PEN);
        return this;
    }

    /**
     * Добавления граф.коментария на pdf-файле (ручка) в документе с проектом резолюции
     */
    public AnnotationOnFilesSteps addCommentOfPenOnPdfFileWithProjectOfResolution(AuthorOfAnnotation authorOfAnnotation) {
        documentElementsMobile.getPanelProjectOfResolution().waitUntil(visible, 10000); // нужно ждать панель проекта перед проверкой появления панели инструментов
        addCommentOfPenOnPdfFile(authorOfAnnotation);
        return this;
    }

    /**
     * Добавления граф.коментария на pdf-файле (Маркер)
     */
    public AnnotationOnFilesSteps addCommentOfMarkerOnPdfFile(AuthorOfAnnotation authorOfAnnotation) {
        addCommentOnPdfFile(authorOfAnnotation, MARKER);
        return this;
    }

    /**
     * Удаление граф.коментария на pdf-файле (Ластик)
     */
    public AnnotationOnFilesSteps eraseAnnotationOnPdfFile(AuthorOfAnnotation authorOfAnnotation) {
        addCommentOnPdfFile(authorOfAnnotation, ERASER);
        return this;
    }

    /**
     * Добавления граф.коментария на файл (ручка)
     */
    public AnnotationOnFilesSteps addCommentOfPenOnFile(AuthorOfAnnotation authorOfAnnotation) {
        addCommentOnFile(authorOfAnnotation, PEN);
        return this;
    }

    /**
     * Добавления граф.коментария на файл (Маркер)
     */
    public AnnotationOnFilesSteps addCommentOfMarkerOnFile(AuthorOfAnnotation authorOfAnnotation) {
        addCommentOnFile(authorOfAnnotation, MARKER);
        return this;
    }

    /**
     * Удаление граф.коментария на файле (Ластик)
     */
    public AnnotationOnFilesSteps eraseAnnotationOnFile(AuthorOfAnnotation authorOfAnnotation) {
        addCommentOnFile(authorOfAnnotation, ERASER);
        return this;
    }


    /**
     * Добавления граф.коментария на pdf-файл
     */
    private void addCommentOnPdfFile(AuthorOfAnnotation authorOfAnnotation, ControlsForAnnotationOnFile control) {
        validateThat()
                .annotationControlsToolbarForPdfAppears(); // проверяем
        getControlForPdf(control);  // Выбираем нужный инструмент
        // Добавление граф.коментария
        drawCommentOnPdf(authorOfAnnotation, control);
        offControlForPdf(control);  // Выключаем выбор инструмента
        // Ожидание
        sleep(5000);
    }

    /**
     * Добавления граф.коментария на файл
     */
    private void addCommentOnFile(AuthorOfAnnotation authorOfAnnotation, ControlsForAnnotationOnFile control) {
        validateThat()
                .annotationControlsToolbarAppears(); // проверяем
        getControl(control);  // Выбираем нужный инструмент
        // Добавление граф.коментария
        drawComment(authorOfAnnotation, control);
        offControl(control);  // Выключаем выбор инструмента
        // Ожидание
        sleep(5000);
    }


    /**
     * Добавления рисунка
     *
     * @param authorOfAnnotation
     * @param control
     */
    private void drawComment(AuthorOfAnnotation authorOfAnnotation, ControlsForAnnotationOnFile control) {
        SelenideElement sourceElement = $(By
                .xpath("//div[@class=\"x-component x-img image-full-contain x-heighted x-widthed x-layout-fit-item x-size-monitored x-paint-monitored\"]/img"));
        makePaint(authorOfAnnotation, control, sourceElement);
    }

    /**
     * Добавления рисунка на pdf
     *
     * @param authorOfAnnotation
     * @param control
     */
    private void drawCommentOnPdf(AuthorOfAnnotation authorOfAnnotation, ControlsForAnnotationOnFile control) {
        switchTo().frame($(By.xpath("//iframe")));  //Переходим во фрейм просмотра файлов
        SelenideElement sourceElement = $(By
                .xpath("//div[@id=\"viewerContainer\"]//div[@class=\"textLayer\"]/div[contains(text(),\"Тестовое название.pdf\")]"));
        makePaint(authorOfAnnotation, control, sourceElement);
        switchTo().defaultContent();  //Уходим из фрейма просмотра файлов
    }

    /**
     * Добавления рисунка по координатам.
     *
     * @param authorOfAnnotation
     * @param control Инструмент комментирования
     * @param sourceElement  элемент от положения которого строится по координатам нанесение комментария
     */
    private void makePaint(AuthorOfAnnotation authorOfAnnotation, ControlsForAnnotationOnFile control, SelenideElement sourceElement) {
        Actions builder = new Actions(getWebDriver());
        switch (control) {
            case PEN:
                if (authorOfAnnotation.getNumberOfAnnotation() == FirstAnnotation) {
                    builder.moveToElement(sourceElement).moveByOffset(-530, 300).
                            clickAndHold().
                            moveByOffset(100, 10).
                            moveByOffset(-10, -50).
                            moveByOffset(-50, -10).
                            moveByOffset(10, 50).
                            moveByOffset(50, -100).release().perform();
                }

                if (authorOfAnnotation.getNumberOfAnnotation() == SecondAnnotation) {
                    builder.moveToElement(sourceElement).moveByOffset(-530, 200).
                            clickAndHold().
                            moveByOffset(100, 10).
                            moveByOffset(-10, -50).
                            moveByOffset(-50, -10).
                            moveByOffset(10, 50).
                            moveByOffset(50, -100).release().perform();
                }
                break;
            case MARKER:
                if (authorOfAnnotation.getNumberOfAnnotation() == FirstAnnotation) {
                    builder.moveToElement(sourceElement).moveByOffset(-550, 340).
                            clickAndHold().
                            moveByOffset(150, 0).release().perform();
                }

                if (authorOfAnnotation.getNumberOfAnnotation() == SecondAnnotation) {
                    builder.moveToElement(sourceElement).moveByOffset(-550, 240).
                            clickAndHold().
                            moveByOffset(150, 0).release().perform();
                }
                break;
            case ERASER:
                if (authorOfAnnotation.getNumberOfAnnotation() == FirstAnnotation) {
                    builder.moveToElement(sourceElement).moveByOffset(-350, 180).
                            clickAndHold().
                            moveByOffset(-200, 200).release().perform();
                }

                if (authorOfAnnotation.getNumberOfAnnotation() == SecondAnnotation) {
                    builder.moveToElement(sourceElement).moveByOffset(-350, 80).
                            clickAndHold().
                            moveByOffset(-200, 200).release().perform();
                }
                break;
        }
    }


    /**
     * Проверки граф.коментариев на файлах
     */
    public ValidationAnnotationOnFilesStepsMobile validateThat() {
        return page(ValidationAnnotationOnFilesStepsMobile.class);
    }

}
