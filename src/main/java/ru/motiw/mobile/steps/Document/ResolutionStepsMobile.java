package ru.motiw.mobile.steps.Document;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.UIAssertionError;
import org.openqa.selenium.By;
import ru.motiw.mobile.elements.Documents.DocumentElementsMobile;
import ru.motiw.mobile.elements.Internal.FilesPreviewElementsMobile;
import ru.motiw.mobile.elements.Internal.GridOfFolderElementsMobile;
import ru.motiw.mobile.elements.Internal.InternalElementsMobile;
import ru.motiw.mobile.elements.Tasks.TaskElementsMobile;
import ru.motiw.mobile.model.Document.OperationsOfDocument;
import ru.motiw.mobile.model.Document.TypeOfLocation;
import ru.motiw.mobile.model.Document.TypeOperationsOfDocument;
import ru.motiw.mobile.steps.Folders.GridOfFoldersSteps;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Document.Resolution;
import ru.motiw.web.model.Tasks.Folder;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Работа с резолюциями
 */
public class ResolutionStepsMobile extends DocumentStepsMobile {
    private DocumentElementsMobile documentElementsMobile = page(DocumentElementsMobile.class);
    private InternalElementsMobile internalElementsMobile = page(InternalElementsMobile.class);
    private GridOfFoldersSteps gridOfFoldersSteps = page(GridOfFoldersSteps.class);
    private TaskElementsMobile taskElementsMobile = page(TaskElementsMobile.class);
    private GridOfFolderElementsMobile gridOfFolderElementsMobile = page(GridOfFolderElementsMobile.class);
    private FilesPreviewElementsMobile filesPreviewElementsMobile = page(FilesPreviewElementsMobile.class);

    /**
     * Ожидание панели проекта резолюции расположенной внизу
     */
    public ResolutionStepsMobile waitPanelProjectOfResolution() {
        documentElementsMobile.getPanelProjectOfResolution().waitUntil(visible, 10000);
        return this;
    }


    /**
     * Массовое создание всех резолюций, которые содержаться в объекте Document
     */
    public ResolutionStepsMobile createAllResolutionsInDocument(Document document, Folder folder, TypeOfLocation executionPlace) {
        for (Resolution resolution : document.getResolutionOfDocument()) {
            createResolution(document, folder, resolution, executionPlace);
        }
        return this;
    }


    /**
     * Создание резолюции
     */
    private ResolutionStepsMobile createResolution(Document document, Folder folder, Resolution resolution, TypeOfLocation executionPlace) {
        sleep(2000); // При массовом создании нужно ожидание перед определением места, где мы сейчас находимся. т.к  после создания резолции происходит переход между страницами
        // ---------------------------------------------------------------- Выполнение операций из грида папки в конт.меню операций
        if (executionPlace == TypeOfLocation.GRID_FOLDER) {
            // Если конт.меню операций не открыто (в случае повторного выполнения операции скрыто), то открываем меню операций
            if (!(gridOfFolderElementsMobile.getContextMenu().is(visible))) {
                gridOfFoldersSteps.clickContextMenuForItemInGrid(document.getDocumentType().getDocRegisterCardsName());
            }
        }

        // ---------------------------------------------------------------- Выполнение операций в карточке документа
        if (executionPlace == TypeOfLocation.PAGE_CARD) {
            // Если мы не в карточке документа в котором должны будем выполнить операцию, то переходим в неё (т.к после создания резолюции происходит переход к следующему документу или в грид)
            if (!internalElementsMobile.getMainTitle().is(text(document.getDocumentType().getDocRegisterCardsName()))) // Название документа в хедере
            {
                gridOfFoldersSteps.goToHome();
                // Переход в документ из грида
                gridOfFoldersSteps.openItemInGrid(document.getDocumentType().getDocRegisterCardsName(), folder);
                // Ожидание кнопок тулбара
                waitToolbarOfMenu();
            }
        }
        try {
            getElementOfOperation(getNameOfOperation(TypeOperationsOfDocument.CREATE_RESOLUTION), getCurrentLocation()).click();
        } catch (UIAssertionError e) {
            refresh();
            getElementOfOperation(getNameOfOperation(TypeOperationsOfDocument.CREATE_RESOLUTION), getCurrentLocation()).click();
        }
        documentElementsMobile.getFormOfResolution().waitUntil(visible, 500);

        // Заполнение полей раб.группа
        documentElementsMobile.getInputEmployeeFieldInFormOfCreateResolution("Ответственный руководитель").waitUntil(visible, 20000);
        choiceUserOnTheRole(
                resolution.getExecutiveManagers(),
                documentElementsMobile.getInputEmployeeFieldInFormOfCreateResolution("Ответственный руководитель"));


        // Заполнение текстовых полей "Текст резолюции"
        documentElementsMobile.getTextareaInFormOfCreateResolution("Текст резолюции").setValue(resolution.getTextOfResolution());

        // Подтверждение создания
        internalElementsMobile.getButtonInFormOfExecutionOperations(OperationsOfDocument.CREATE_RESOLUTION_IN_THE_GRID.getNameOperation()).click();

        // Ожидание закрытия Формы резолюции
        documentElementsMobile.getFormOfResolution().waitUntil(not(visible), 30000);
        refresh();
        document.setOnExecution(true); //  Документ на исполнении
        return this;
    }

    /**
     * Проверка резолюции
     *
     * @return
     */
    public ResolutionStepsMobile verifyCreatedResolution(Resolution resolution) {
        documentElementsMobile.getButtonOfTab(OperationsOfDocument.LIST_OF_RESOLUTION.getNameOperation()).click(); // Открываем список
        documentElementsMobile.getItemInResolutionList().shouldBe(CollectionCondition.sizeGreaterThan(0), 20000);

        // Проверяем текст резолюции в списке резолюций
        documentElementsMobile.getTextOfResolutionInCertainItem(resolution.getTextOfResolution()).shouldBe(visible);
        // Проверяем автора резолюции в списке резолюций
        documentElementsMobile.getAuthorsOfResolutionInCertainItem(resolution.getTextOfResolution()).shouldHave(text(resolution.getAuthorDefault().getLastName()));
        documentElementsMobile.getButtonOfTab(OperationsOfDocument.LIST_OF_RESOLUTION.getNameOperation()).click(); // Закрываем список

        return this;
    }

    /**
     * Утверждение проекта резолюции по панели расположенной внизу
     */
    public ResolutionStepsMobile approveProjectOfResolution() {
        documentElementsMobile.getPanelProjectOfResolution().waitUntil(visible, 10000);
        documentElementsMobile.getButtonForApproveOnPanelProjectOfResolution().waitUntil(visible, 10000).click();
        documentElementsMobile.getPanelProjectOfResolution().waitUntil(not(visible), 20000);
        return this;
    }


    /**
     * Утверждение проекта резолюции по панели расположенной внизу
     * TODO Проверка текста резолюции на панели
     *
     */
   /* public ResolutionStepsMobile approveProjectOfResolution(Resolution resolution) {
        documentElementsMobile.getPanelProjectOfResolution().waitUntil(visible, 10000);

        // TODO Проверка текста резолюции на панели

        documentElementsMobile.getButtonForApproveOnPanelProjectOfResolution().waitUntil(visible, 10000).click();
        documentElementsMobile.getPanelProjectOfResolution().waitUntil(not(visible), 10000);
        return this;
    }*/

    /**
     * Проверка отсутствия панели для утверждения проекта резолюции расположенной внизу
     */
    public ResolutionStepsMobile verifyThatNotHavePanelProjectOfResolution() {
        $(By.xpath("//iframe")).waitUntil(exist, 20000);
        switchTo().frame($(By.xpath("//iframe")));  //Переходим во фрейм просмотра файлов
        filesPreviewElementsMobile.getPdfViewer().waitUntil(exist, 20000);
        sleep(2000);
        switchTo().defaultContent();  //Уходим из фрейма просмотра файлов
        documentElementsMobile.getPanelProjectOfResolution().shouldNotBe(visible);
        return this;
    }


}
