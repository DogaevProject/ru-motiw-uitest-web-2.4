package ru.motiw.web.steps.Documents.EditDocument;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import ru.motiw.web.elements.elementsweb.Documents.CreateDocument.NewDocumentRouteTabElements;
import ru.motiw.web.elements.elementsweb.Documents.EditDocument.EditDocumentCartTabElements;
import ru.motiw.web.elements.elementsweb.Documents.EditDocument.EditDocumentResolutionsTabElements;
import ru.motiw.web.elements.elementsweb.Documents.EditDocument.EditDocumentRouteTabElements;
import ru.motiw.web.elements.elementsweb.Documents.Resolution.CreateResolution.CreateResolutionFormElements;
import ru.motiw.web.model.Document.Resolution;
import ru.motiw.web.steps.BaseSteps;
import ru.motiw.web.steps.Documents.Resolution.ResolutionSteps;
import ru.motiw.web.steps.Tasks.TaskForm.UnionMessageSteps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.AssertJUnit.fail;

/**
 * Редактирование документа
 */
public class EditDocumentSteps extends BaseSteps {

    private EditDocumentResolutionsTabElements editDocumentResolutionsTabElements = page(EditDocumentResolutionsTabElements.class);
    private EditDocumentRouteTabElements editDocumentRouteTabElements = page(EditDocumentRouteTabElements.class);
    private CreateResolutionFormElements createResolutionFormElements = page(CreateResolutionFormElements.class);
    private ResolutionSteps resolutionSteps = page(ResolutionSteps.class);
    private NewDocumentRouteTabElements routeTableGridElements = page(NewDocumentRouteTabElements.class);
    private EditDocumentCartTabElements editDocumentCartTabElements = page(EditDocumentCartTabElements.class);


    /**
     * Выбираем вкладку - Маршрут
     */
    public EditDocumentSteps routesTab() {
        editDocumentRouteTabElements.getRoutesTab().click();
        waitForMask();
        editDocumentRouteTabElements.getRoutesTab().waitUntil(visible, 20000);
        getFrameObject($(routeTableGridElements.getFrameRoute())); // уходим во фрейм Маршруты
        return this;
    }

    /**
     * Выбираем вкладку - Резолюции
     */
    public EditDocumentSteps resolutionsTab() {
        editDocumentResolutionsTabElements.getResolutionsTab().click();
        waitForMask();
        editDocumentResolutionsTabElements.getResolutionsTab().waitUntil(visible, 20000);
        return this;
    }


    /**
     * Ожидание невидимости маски
     */
    private void waitForMask() {
        $(By.xpath("//*[contains (@class, 'x-mask')]")).waitUntil(Condition.disappear, 30000);
    }

    /**
     * Проверка статуса резолюции - статус "На исполнении"
     */
    public void statusOfResolutionIsOnExecution(Resolution resolution) {
        editDocumentResolutionsTabElements.getValueInColumnItemOfResolution(resolution.getTextOfResolution(), "На исполнении").shouldBe(visible);
    }

    /**
     * Проверка статуса резолюции - статус "Утверждено"
     */
    public void statusOfResolutionIsApproved(Resolution resolution) {
        editDocumentResolutionsTabElements.getValueInColumnItemOfResolution(resolution.getTextOfResolution(), "Утверждено").shouldBe(visible);
    }

    /**
     * Проверяем, что кнопки для согласования документа отображаются
     */
    public void verifyButtonsOfConsiderationIsVisible() {
        editDocumentCartTabElements.getButtonConsideration().shouldBe(Condition.visible);
        editDocumentCartTabElements.getButtonConsiderationWithComment().shouldBe(Condition.visible);
        editDocumentCartTabElements.getButtonDenialReview().shouldBe(Condition.visible);
        editDocumentCartTabElements.getButtonBackToRevision().shouldBe(Condition.visible);
    }


    /**
     * Проверяем, что кнопки для согласования документа не отображаются
     */
    public void verifyButtonsOfConsiderationIsNotVisible() {
        editDocumentCartTabElements.getButtonConsideration().shouldNotBe(Condition.visible);
        editDocumentCartTabElements.getButtonConsiderationWithComment().shouldNotBe(Condition.visible);
        editDocumentCartTabElements.getButtonDenialReview().shouldNotBe(Condition.visible);
        editDocumentCartTabElements.getButtonBackToRevision().shouldNotBe(Condition.visible);
    }


    /**
     * Создать проект резолюции
     */
    public EditDocumentSteps createProjectOfResolution(Resolution resolution) {
        // Выбираем вкладку - Резолюции
        resolutionsTab();
        // Открываем форму создания резолюции
        sleep(3000);
        editDocumentResolutionsTabElements.getButtonCreateResolution().waitUntil(visible, 5000).click();
        switchTo().frame($(By.cssSelector("#resolution_window")));
        createResolutionFormElements.getElementResolutionFormBody().waitUntil(visible, 10000);
        // Редактирование резолюции
        resolutionSteps.addTextOfResolution(resolution.getTextOfResolution());
        resolutionSteps.deleteAuthor(resolution.getAuthorDefault()); // удаляем админа по умолчанию под которым создаем
        resolutionSteps.addAuthor(resolution.getAuthors());
        resolutionSteps.addExecutiveManager(resolution.getExecutiveManagers());
        //Установить проект = Да
        createResolutionFormElements.getProjectField().click();
        createResolutionFormElements.getProjectFieldButtonForOpenContext().click();
        $(By.xpath("//div[@class=\"x-boundlist x-boundlist-floating x-layer x-boundlist-default x-border-box\"]//li[contains(text(), 'Да')] ")).shouldBe(visible).click();
        $(By.xpath("(//div[@id=\"resolution-form-body\"]//div[text()=\"Проект\"]/ancestor::table//div[@class=\"x-grid-cell-inner \"])[1]")).click();
        // Сохранение
        createResolutionFormElements.getButtonSaveResolution().click();
        return this;
    }

    /**
     * Ввести текст перед выполнением операции и подтвердить действие
     */
    public EditDocumentSteps addTextBeforeConsiderationAndOk(String text) {
        getFrameObject($(By.xpath("//*[@class='cke_wysiwyg_frame cke_reset']"))); // Фрейм CKE (расширенный текстовый редактор)
        editDocumentCartTabElements.getCkeBody().setValue(text);
        switchTo().defaultContent();
        //switchTo().frame($(By.cssSelector("#flow")));
        editDocumentCartTabElements.getButtonOkInFormForAddTextBeforeConsideration().click();
        sleep(500);
        waitForMask();
        return this;
    }


    /**
     * Снять признак пропуска первого блока при возврате на рассмотрение с начала схемы
     */
    public EditDocumentSteps turnOnConsiderationForBlock(String nameOfBlock) {
        switchTo().frame($(By.cssSelector("#newrouteframe")));
        editDocumentCartTabElements.turnOnConsiderationForBlockDiagram(nameOfBlock).waitUntil(visible, 5000).click();
        switchTo().defaultContent();
        return this;
    }





}

