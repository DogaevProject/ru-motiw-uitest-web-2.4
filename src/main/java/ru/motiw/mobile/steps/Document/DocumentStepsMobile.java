package ru.motiw.mobile.steps.Document;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.motiw.mobile.elements.Documents.DocumentElementsMobile;
import ru.motiw.mobile.elements.Internal.GridOfFolderElementsMobile;
import ru.motiw.mobile.elements.Internal.InternalElementsMobile;
import ru.motiw.mobile.elements.Tasks.TaskElementsMobile;
import ru.motiw.mobile.model.Document.OperationsOfDocument;
import ru.motiw.mobile.model.Document.TypeOfLocation;
import ru.motiw.mobile.model.Document.TypeOperationsOfDocument;
import ru.motiw.mobile.steps.AnnotationOnFilesSteps;
import ru.motiw.mobile.steps.CardStepsMobile;
import ru.motiw.mobile.steps.Document.AssertionDocument.AssertDocument;
import ru.motiw.mobile.steps.Document.ValidationStepsDocument.ValidateOperationsOfDocumentStepsMobile;
import ru.motiw.mobile.steps.ValidationStepsMobile.ValidateFilesStepsMobile;

import java.util.ArrayList;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.AssertJUnit.fail;


/**
 * Работа с документами
 */
public class DocumentStepsMobile extends CardStepsMobile {
    private InternalElementsMobile internalElementsMobile = page(InternalElementsMobile.class);
    private TaskElementsMobile taskElementsMobile = page(TaskElementsMobile.class);
    private DocumentElementsMobile documentElementsMobile = page(DocumentElementsMobile.class);
    GridOfFolderElementsMobile gridOfFolderElementsMobile = page(GridOfFolderElementsMobile.class);
    ValidateFilesStepsMobile validateFilesStepsMobile = page(ValidateFilesStepsMobile.class);
    AnnotationOnFilesSteps annotationOnFiles = page(AnnotationOnFilesSteps.class);


    /**
     * Получаем спискок названий операций, которые отображаются в данный момент
     *
     * @return спискок названий операций
     */
    public ArrayList<String> getNameOfCurrentVisibleOperations() {

        ArrayList<String> textOnCurrentVisibleOperations = new ArrayList<>();

        // Извлекаем название операции
        for (SelenideElement elementOfOperation : getElementOfAllOperation(getCurrentLocation())) {
            textOnCurrentVisibleOperations.add(elementOfOperation.getText());
        }

        return textOnCurrentVisibleOperations;
    }


    /**
     * Получаем элемент операции в зависимости от того, где в данный момент операция отображается (конт.меню в гриде папки или форма документа)
     *
     * @return
     */
    public ElementsCollection getElementOfAllOperation(TypeOfLocation currentLocation) {

        ElementsCollection elementOfOperation = null;


        if (currentLocation == TypeOfLocation.GRID_FOLDER) // Если мы в гриде
        {
            elementOfOperation = gridOfFolderElementsMobile.getAllButtonsOfContextMenu();
        } else if (currentLocation == TypeOfLocation.PAGE_CARD) // Если в карточке
        {
                documentElementsMobile.getAllButtonsOfTab().shouldHave(CollectionCondition.sizeGreaterThan(0), 5000);
                elementOfOperation = documentElementsMobile.getAllButtonsOfTab();
        }
        if (elementOfOperation == null) {
            fail("Элемент для взаимодействия с операциями докмуента отсутствует");
        }
        return elementOfOperation;
    }


    /**
     * Получаем элемент операции в зависимости от того, где в данный момент операция отображается (конт.меню в гриде папки или форма документа)
     *
     * @param nameOfOperation
     * @return
     */
    public SelenideElement getElementOfOperation(String nameOfOperation, TypeOfLocation currentLocation) {

        SelenideElement elementOfOperation = null;


        if (currentLocation == TypeOfLocation.GRID_FOLDER) // Если мы в гриде
        {
            elementOfOperation = gridOfFolderElementsMobile.getOperationInContextMenu(nameOfOperation);
        } else if (currentLocation == TypeOfLocation.PAGE_CARD) // Если в карточке
        {
            elementOfOperation = documentElementsMobile.getButtonOfTab(nameOfOperation);
        }

        if (elementOfOperation == null) {
            fail("Элемент для взаимодействия с операциями докмуента отсутствует");
        }
        return elementOfOperation;
    }

    /**
     * Получение названий операций
     * нужно для некоторых операций название которых отличается, в зависимости от того, где в данный момент операция отображается (конт.меню в гриде папки или форма документа)
     *
     * @param operation тип операции
     * @return
     */
    public String getNameOfOperation(TypeOperationsOfDocument operation) {

        String nameOfOperationCreateResolution = null;

        switch (operation) {
            case CREATE_RESOLUTION:
                if (!gridOfFolderElementsMobile.getAllItemsInTheGridOfFolder().isEmpty()) // Если мы в гриде
                {
                    nameOfOperationCreateResolution = OperationsOfDocument.CREATE_RESOLUTION_IN_THE_GRID.getNameOperation();
                } else if (formElementsMobile.getToolbarOfMenu().is(visible)) // Если в карточке
                {
                    nameOfOperationCreateResolution = OperationsOfDocument.CREATE_RESOLUTION_IN_FORM_OF_DOCUMENT.getNameOperation();
                }
        }

        if (nameOfOperationCreateResolution == null) {
            fail("Для операции " + operation.name() + " отсутствует Название ИЛИ не найдено место выполнения операции");
        }
        return nameOfOperationCreateResolution;
    }

    /**
     * Выполнение стандартной операции в форме докумена с вводом текста перед подтверждением операции
     *
     * @param nameOfOperation - название операции
     * @param textForInput    - текст вводимый в инпут перед подтверждением операции
     */
    void executionOperationWithAdditionText(String nameOfOperation, String textForInput) {
        // Находим локатор элемента кнопки операции
        getElementOfOperation(nameOfOperation, getCurrentLocation()).click();
        // Вводим текст в форму
        sleep(500);
        taskElementsMobile.getInputForAddComment().setValue(textForInput);
        // Подтверждаем выполнение действия
        internalElementsMobile.getButtonInFormOfExecutionOperations(nameOfOperation).click();
    }

    /**
     * Проверки операций документа
     *
     * @return
     */
    ValidateOperationsOfDocumentStepsMobile validateThatOperations() {
        return page(ValidateOperationsOfDocumentStepsMobile.class);
    }

    /**
     * Assert'ы для документа и его внутренних объектов
     *
     * @return
     */
    AssertDocument assertThatInDocument() {
        return page(AssertDocument.class);
    }

}
