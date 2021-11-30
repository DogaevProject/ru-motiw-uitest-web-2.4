package ru.motiw.mobile.steps.Document;

import ru.motiw.mobile.elements.Internal.InternalElementsMobile;
import ru.motiw.mobile.elements.Tasks.TaskElementsMobile;
import ru.motiw.mobile.model.Document.OperationsOfDocument;
import ru.motiw.mobile.model.Document.TypeOfLocation;
import ru.motiw.mobile.steps.Folders.GridOfFoldersSteps;
import ru.motiw.mobile.steps.InternalStepsMobile;
import ru.motiw.mobile.steps.LoginStepsMobile;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Document.ExecutionOfDocument;
import ru.motiw.web.model.Document.Resolution;
import ru.motiw.web.model.Tasks.Folder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;

/**
 * Проверка выполнения действий
 */
public class ExecutionDocumentStepsMobile extends DocumentStepsMobile {
    private LoginStepsMobile loginStepsMobile = page(LoginStepsMobile.class);
    private InternalStepsMobile internalStepsMobile = page(InternalStepsMobile.class);
    private GridOfFoldersSteps gridOfFoldersSteps = page(GridOfFoldersSteps.class);
    private InternalElementsMobile internalElementsMobile = page(InternalElementsMobile.class);
    private TaskElementsMobile taskElementsMobile = page(TaskElementsMobile.class);
    private ResolutionStepsMobile resolutionStepsMobile = page(ResolutionStepsMobile.class);



    /**
     * Выполнение различных действий из грида папки в конт.меню операций
     *
     * @param document
     * @param executionOfDocument
     */
    private void executionInGrid(Document document, Folder folder, ExecutionOfDocument executionOfDocument, TypeOfLocation executionPlace) {
        // Открываем меню операций
        gridOfFoldersSteps.clickContextMenuForItemInGrid(document.getDocumentType().getDocRegisterCardsName());
        // Выполняем операции
        executionOperations(document, folder, executionOfDocument, executionPlace);
        // Проверяем, что Кнопка открытия конт.меню появилась
        gridOfFolderElementsMobile.getButtonOfOpenContextMenu(document.getDocumentType().getDocRegisterCardsName()).shouldBe(visible);
        // Проверяем, что конт.меню закрылось
        gridOfFolderElementsMobile.getContextMenu().shouldNotBe(visible);
    }


    /**
     * Выполнение различных действий в карточке
     *
     * @param
     * @param document
     * @param executionOfDocument
     */
    private void executionInFormOfDocument(Document document, Folder folder, ExecutionOfDocument executionOfDocument, TypeOfLocation executionPlace) {
        // Ожидание и проверка кнопок тулбара
        resolutionStepsMobile.waitToolbarOfMenu();
        // Выполнение операций
        executionOperations(document, folder, executionOfDocument, executionPlace); // также как visibleWithRightAccessToOperations - локаторы кнопкок операций зависят от того, где мы находимся
    }

    /**
     * Выполнение операций
     *
     * @param executionOfDocument
     */
    private void executionOperations(Document document, Folder folder, ExecutionOfDocument executionOfDocument, TypeOfLocation executionPlace) {
        switch (executionOfDocument.getTypeExecutionOperation()) {

            case CREATE_RESOLUTION:
                    resolutionStepsMobile.createAllResolutionsInDocument(document, folder, executionPlace);
                break;

            case MOVE_TO_EXECUTION:
                getElementOfOperation(OperationsOfDocument.MOVE_TO_EXECUTION.getNameOperation(), getCurrentLocation()).click();
                internalElementsMobile.getToastWithText().shouldHave(text("В данный момент создание задачи по исполнению в разработке"));
                break;

            case MOVE_TO_ARCHIVE:
                executionOperationWithAdditionText(OperationsOfDocument.MOVE_TO_ARCHIVE.getNameOperation(), OperationsOfDocument.MOVE_TO_ARCHIVE.getNameOperation());
                internalElementsMobile.getToastWithText().waitUntil(text("в архив"), 10000); // Выводимый текст может отличаться, поэтому проверяем только общую часть текста сообщений "в архив".
                break;

            case RETURN_TO_EXECUTION:
                executionOperationWithAdditionText(OperationsOfDocument.RETURN_TO_EXECUTION.getNameOperation(), OperationsOfDocument.RETURN_TO_EXECUTION.getNameOperation());
                internalElementsMobile.getToastWithText().waitUntil(text("Возврат на доработку"), 10000);
                for (Resolution resolution : document.getResolutionOfDocument()) {
                    if (resolution.isReportOfExecution()) {
                        resolution.setReportOfExecution(false); //  Выставляем для каждой резолюции признак того, что Отчет по исполнению резолюции возвращен на доработку (не отправлен).
                    }
                }
                break;

            case CLOSE_EXECUTION:
                executionOperationWithAdditionText(OperationsOfDocument.CLOSE_EXECUTION.getNameOperation(), OperationsOfDocument.CLOSE_EXECUTION.getNameOperation());
                internalElementsMobile.getToastWithText().waitUntil(text("Выполнение завершено"), 20000);
                // Выставляем признак "Отчет по исполнению Документа отправлен", если операцию выполнил "Ответственные руководитель" резолюции
                if (executionOfDocument.getExecutiveUser() != document.getAuthorOfDocument()) {
                    for (Resolution resolution : document.getResolutionOfDocument()) {
                        for (Employee executiveManagerInResolution : resolution.getExecutiveManagers())
                            if (executionOfDocument.getExecutiveUser() == executiveManagerInResolution) {
                                resolution.setReportOfExecution(true); //  Выставляем признак для резолюции "Отчет по исполнению Документа отправлен"
                            }
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("Неверное название типа операции:" + executionOfDocument.getTypeExecutionOperation());
        }
    }


    /**
     * Шаги при проверке выполнения действий в карточке документа
     *
     * @param document
     * @param employee
     * @param folder
     * @param executionOfDocument
     * @throws Exception
     */
    private void stepsOfExecutionDocument(Document document, Employee employee, Folder folder, ExecutionOfDocument
            executionOfDocument, TypeOfLocation executionPlace) {
        loginStepsMobile
                .loginAs(employee) // Авторизация под участником рассмотрения документа
                .waitLoadMainPage(employee); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folder);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.validateThatInGrid().itemDisplayed(document.getDocumentType().getDocRegisterCardsName(), folder);

        // ---------------------------------------------------------------- Выполнение операций из грида папки в конт.меню операций
        if (executionPlace == TypeOfLocation.GRID_FOLDER) {
            executionInGrid(document, folder, executionOfDocument, executionPlace);
        }

        // ---------------------------------------------------------------- Выполнение операций в карточке документа
        if (executionPlace == TypeOfLocation.PAGE_CARD) {
            // Переход в документ из грида
            gridOfFoldersSteps.openItemInGrid(document.getDocumentType().getDocRegisterCardsName(), folder);
            //----------------------------------------------------------------ФОРМА - Документ
            // Выполнение операций
            executionInFormOfDocument(document, folder, executionOfDocument, executionPlace);
        }
        // Выход из системы
        internalStepsMobile.logout();

        // Проверка выполненых операций
        verifyExecutionOnDifferentUsers(document, folder, executionOfDocument);
    }


    /**
     * Проверяем выполнение действий под разными пользователями
     *
     * @param document             документ
     * @param folder               папка с которой будем работать
     * @param typeOfExecutionPlace место откуда будет выполнена операция с документом
     * @return
     */
    public ExecutionDocumentStepsMobile executionOnDifferentUsers(Document document, Folder
            folder, TypeOfLocation typeOfExecutionPlace) {
        if (document == null || folder == null) {
            return null;
        }

        if (document.getExecutionOfDocument() != null) {
            List<Integer> allExecutionNumber = new ArrayList<>(); // Все порядковые номера выполняемых операций

            //  Находим все порядковые номера выполняемых операций
            for (ExecutionOfDocument execution : document.getExecutionOfDocument()) {
                allExecutionNumber.add(execution.getExecutionNumber());
            }

            // Cортируем по возрастанию все порядковые номера выполняемых операций
            Collections.sort(allExecutionNumber);

            // Обработка выполняемых операций согласно их порядковым номерам
            for (Integer executionNumberInAscendingOrder : allExecutionNumber) {
                for (ExecutionOfDocument executionOfDocument : document.getExecutionOfDocument()) {
                    if (executionNumberInAscendingOrder.equals(executionOfDocument.getExecutionNumber())) {
                        stepsOfExecutionDocument(document, executionOfDocument.getExecutiveUser(), folder, executionOfDocument, typeOfExecutionPlace);
                    }
                }
            }
        }
        return this;
    }

    /**
     * Проверка раннее выполненых операций под разными пользователями
     *
     * @param document
     * @param folder              папка с которой будем работать
     * @param executionOfDocument раннее выполненая операция
     * @return
     */
    public ExecutionDocumentStepsMobile verifyExecutionOnDifferentUsers(Document document, Folder
            folder, ExecutionOfDocument executionOfDocument) {

        if (document == null || folder == null) {
            return null;
        }

        if (!(document.getAuthorOfDocument() == null)) {
            stepsOfVerifyExecutionDocument(document, document.getAuthorOfDocument(), folder, executionOfDocument);
        }

        //Проверки для каждого рассматривающиего
        if (!(document.getRouteSchemeForDocument().getUserRoute() == null)) {
            for (Employee employee : document.getRouteSchemeForDocument().getUserRoute()) {
                stepsOfVerifyExecutionDocument(document, employee, folder, executionOfDocument);
            }
        }

        //Проверки для участников резолюции
        if (!(document.getResolutionOfDocument() == null) && document.isOnExecution()) {
            for (Resolution resolution : document.getResolutionOfDocument()) {
                for (Employee executiveManager : resolution.getExecutiveManagers()) {
                    stepsOfVerifyExecutionDocument(document, executiveManager, folder, executionOfDocument);
                }
            }
        }
        return this;
    }

    /**
     * Шаги при проверке раннее выполненых операций
     *
     * @param document
     * @param employee            пользователь под которым проверяем
     * @param folder              папка с которой будем работать
     * @param executionOfDocument раннее выполненая операция
     */
    private void stepsOfVerifyExecutionDocument(Document document, Employee employee, Folder
            folder, ExecutionOfDocument executionOfDocument) {
        loginStepsMobile
                .loginAs(employee) // Авторизация под участником рассмотрения документа
                .waitLoadMainPage(employee); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folder);
        //----------------------------------------------------------------ГРИД - Папка

        // Проверка раннее выполненых операций
        verifyExecutionOfDocument(document, folder, executionOfDocument, employee);

        // Выход из системы
        internalStepsMobile.logout();

    }

    /**
     * Проверка раннее выполненных операций в карточке или гриде
     *
     * @param document
     * @param folder
     * @param executionOfDocument
     */
    private void verifyExecutionOfDocument(Document document, Folder folder, ExecutionOfDocument
            executionOfDocument, Employee currentUser) {

        // Проверки для Авторов документа и участников резолюции
        if (document.getAuthorOfDocument() == currentUser || assertThatInDocument().currentUserIsExecutiveManagersInResolution(document.getResolutionOfDocument(), currentUser)) {
            switch (executionOfDocument.getTypeExecutionOperation()) {
                case CREATE_RESOLUTION:
                    // Проверяем наличие доступных операций с документом
                    stepsOfVerifyOperationForDocument(document, currentUser, folder);
                    // Проверка резолюции
                    for (Resolution resolution : document.getResolutionOfDocument())
                        resolutionStepsMobile.verifyCreatedResolution(resolution);
                    break;

                case MOVE_TO_EXECUTION:
                    // Проверяем наличие доступных операций с документом
                    stepsOfVerifyOperationForDocument(document, currentUser, folder);
                    break;

                case MOVE_TO_ARCHIVE:
                    //Проверяем что документа нет гриде
                    gridOfFoldersSteps.validateThatInGrid().itemDisappear(document.getDocumentType().getDocRegisterCardsName(), folder);
                    break;

                case RETURN_TO_EXECUTION:
                    // Проверяем наличие доступных операций с документом
                    stepsOfVerifyOperationForDocument(document, currentUser, folder);
                    break;

                case CLOSE_EXECUTION:
                    //Автор закрывает - документа нет гриде
                    if (executionOfDocument.getExecutiveUser() == document.getAuthorOfDocument()) {
                        gridOfFoldersSteps.validateThatInGrid().itemDisappear(document.getDocumentType().getDocRegisterCardsName(), folder);
                    } else
                        //Исполнитель резолюции закрывает - документ остается в гриде
                        for (Resolution resolution : document.getResolutionOfDocument()) {
                            for (Employee executiveManagerInResolution : resolution.getExecutiveManagers())
                                if (executionOfDocument.getExecutiveUser() == executiveManagerInResolution) {
                                    // Проверяем наличие доступных операций с документом
                                    stepsOfVerifyOperationForDocument(document, currentUser, folder);
                                }
                        }
                    break;

                default:
                    break;
            }
        }

        // Проверки для участников рассмотрения
        if (Arrays.asList(document.getRouteSchemeForDocument().getUserRoute()).contains(currentUser)) {
            switch (executionOfDocument.getTypeExecutionOperation()) {
                case CREATE_RESOLUTION:
                    //Проверяем что документа нет гриде
                    gridOfFoldersSteps.validateThatInGrid().itemDisappear(document.getDocumentType().getDocRegisterCardsName(), folder);
                    break;

                case MOVE_TO_EXECUTION:
                    // Проверяем наличие доступных операций с документом
                    stepsOfVerifyOperationForDocument(document, currentUser, folder);
                    break;

                case MOVE_TO_ARCHIVE:
                    //Проверяем что документа нет гриде
                    gridOfFoldersSteps.validateThatInGrid().itemDisappear(document.getDocumentType().getDocRegisterCardsName(), folder);
                    break;

                case RETURN_TO_EXECUTION:
                    //Проверяем что документа нет гриде
                    gridOfFoldersSteps.validateThatInGrid().itemDisappear(document.getDocumentType().getDocRegisterCardsName(), folder);
                    break;

                case CLOSE_EXECUTION:
                    //Проверяем что документа нет гриде
                    gridOfFoldersSteps.validateThatInGrid().itemDisappear(document.getDocumentType().getDocRegisterCardsName(), folder);

                    break;

                default:
                    break;
            }
        }
    }

    /**
     * Шаги при проверке операций в гриде (открытия конт. меню) и на тулбаре карточки документа (переход в карточку)
     *
     * @param document
     * @param folder
     */
    private void stepsOfVerifyOperationForDocument(Document document, Employee currentUser, Folder folder) {
        //---------------------------------------------------------------- Грид папки
        gridOfFoldersSteps.validateThatInGrid().itemDisplayed(document.getDocumentType().getDocRegisterCardsName(), folder);
        // Проверяем доступные операции с документом из грида
        validateThatOperations().accessedInTheGrid(document, currentUser);
        //Переход в документ из грида
        gridOfFoldersSteps.openItemInGrid(document.getDocumentType().getDocRegisterCardsName(), folder);
        //----------------------------------------------------------------ФОРМА - Документ
        // Ожидание и проверка кнопок тулбара
        resolutionStepsMobile.waitToolbarOfMenu();;
        validateThatOperations().visibleWithRightAccessToOperations(document, currentUser);

    }


}
