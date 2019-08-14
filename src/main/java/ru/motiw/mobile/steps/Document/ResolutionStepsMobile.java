package ru.motiw.mobile.steps.Document;

import com.codeborne.selenide.CollectionCondition;
import ru.motiw.mobile.elements.Documents.DocumentElementsMobile;
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

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.sleep;

/**
 * Работа с резолюциями
 */
public class ResolutionStepsMobile extends DocumentStepsMobile {
    private DocumentElementsMobile documentElementsMobile = page(DocumentElementsMobile.class);
    private InternalElementsMobile internalElementsMobile = page(InternalElementsMobile.class);
    private GridOfFoldersSteps gridOfFoldersSteps = page(GridOfFoldersSteps.class);
    private TaskElementsMobile taskElementsMobile = page(TaskElementsMobile.class);
    private GridOfFolderElementsMobile gridOfFolderElementsMobile = page(GridOfFolderElementsMobile.class);

    /**
     * Создание резолюции
     */
    public ResolutionStepsMobile createResolution(Document document, Folder folder, TypeOfLocation executionPlace) {
        for (Resolution resolution : document.getResolutionOfDocument()) {
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
                    formElementsMobile.getToolbarOfMenu().waitUntil(visible, 15000);
                }
            }

            getElementOfOperation(getNameOfOperation(TypeOperationsOfDocument.CREATE_RESOLUTION), getCurrentLocation()).click();
            documentElementsMobile.getFormOfResolution().waitUntil(visible, 500);

            // Заполнение полей раб.группа
            choiceUserOnTheRole(
                    resolution.getExecutiveManagers(),
                    documentElementsMobile.getInputEmployeeFieldInFormOfCreateResolution("Ответственный руководитель"));


            // Заполнение текстовых полей "Текст резолюции"
            documentElementsMobile.getTextareaInFormOfCreateResolution("Текст резолюции").setValue(resolution.getTextOfResolution());

            // Подтверждение создания
            internalElementsMobile.getButtonInFormOfExecutionOperations(OperationsOfDocument.CREATE_RESOLUTION_IN_THE_GRID.getNameOperation()).click();

            // Ожидание toast
            internalElementsMobile.getToastWithText().waitUntil(text("Резолюция сохранена"), 30000);
            document.setOnExecution(true); //  Документ на исполнении
        }
        return this;
    }


    /**
     * Создание резолюции
     */
    private ResolutionStepsMobile create(Document document, Folder folder, Resolution resolution, TypeOfLocation executionPlace) {
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
                formElementsMobile.getToolbarOfMenu().waitUntil(visible, 15000);
            }
        }

        getElementOfOperation(getNameOfOperation(TypeOperationsOfDocument.CREATE_RESOLUTION), getCurrentLocation()).click();
        documentElementsMobile.getFormOfResolution().waitUntil(visible, 500);

        // Заполнение полей раб.группа
        choiceUserOnTheRole(
                resolution.getExecutiveManagers(),
                documentElementsMobile.getInputEmployeeFieldInFormOfCreateResolution("Ответственный руководитель"));


        // Заполнение текстовых полей "Текст резолюции"
        documentElementsMobile.getTextareaInFormOfCreateResolution("Текст резолюции").setValue(resolution.getTextOfResolution());

        // Подтверждение создания
        internalElementsMobile.getButtonInFormOfExecutionOperations(OperationsOfDocument.CREATE_RESOLUTION_IN_THE_GRID.getNameOperation()).click();

        // Ожидание toast
        internalElementsMobile.getToastWithText().waitUntil(text("Резолюция сохранена"), 30000);
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


}
