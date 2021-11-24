package ru.motiw.web.steps.Documents.EditDocument;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import ru.motiw.web.elements.elementsweb.Documents.EditDocument.EditDocumentResolutionsTabElements;
import ru.motiw.web.elements.elementsweb.Documents.Resolution.CreateResolution.CreateResolutionFormElements;
import ru.motiw.web.model.Document.Resolution;
import ru.motiw.web.steps.BaseSteps;
import ru.motiw.web.steps.Documents.Resolution.ResolutionSteps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.AssertJUnit.fail;

/**
 * Редактирование документа
 */
public class EditDocumentSteps extends BaseSteps {

    private EditDocumentResolutionsTabElements editDocumentResolutionsTabElements = page(EditDocumentResolutionsTabElements.class);
    private CreateResolutionFormElements createResolutionFormElements = page(CreateResolutionFormElements.class);
    private ResolutionSteps resolutionSteps = page(ResolutionSteps.class);


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


}

