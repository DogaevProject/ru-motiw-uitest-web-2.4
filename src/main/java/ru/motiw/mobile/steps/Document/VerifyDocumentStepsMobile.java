package ru.motiw.mobile.steps.Document;

import ru.motiw.mobile.elements.Internal.InternalElementsMobile;
import ru.motiw.mobile.elements.Tasks.TaskElementsMobile;
import ru.motiw.mobile.steps.Folders.GridOfFoldersSteps;
import ru.motiw.mobile.steps.InternalStepsMobile;
import ru.motiw.mobile.steps.LoginStepsMobile;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Document.Resolution;
import ru.motiw.web.model.Tasks.Folder;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.sleep;

/**
 * Проверка карточки
 */
public class VerifyDocumentStepsMobile extends DocumentStepsMobile {
    private LoginStepsMobile loginStepsMobile = page(LoginStepsMobile.class);
    private InternalStepsMobile internalPageStepsMobile = page(InternalStepsMobile.class);
    private GridOfFoldersSteps gridOfFoldersSteps = page(GridOfFoldersSteps.class);
    private InternalElementsMobile internalElementsMobile = page(InternalElementsMobile.class);
    private TaskElementsMobile taskElementsMobile = page(TaskElementsMobile.class);
    private ResolutionStepsMobile resolutionStepsMobile = page(ResolutionStepsMobile.class);


    /**
     * Проверка созданного документа
     *
     * @param document атрибуты
     * @return VerifyDocumentStepsMobile
     */
    private VerifyDocumentStepsMobile verifyPageOfDocument(Document document, Employee currentUser) throws Exception {
        if (document == null) {
            return null;
        } else
            internalElementsMobile.getMainTitle().shouldHave((text(document.getDocumentType().getDocRegisterCardsName()))); // Название документа в хедере

        /**
         * Ожидание и проверка кнопок тулбара
         */
        resolutionStepsMobile.waitToolbarOfMenu();
        validateThatOperations().visibleWithRightAccessToOperations(document, currentUser);

        /*
         * Проверка Файлов
         */
        validateFilesStepsMobile.verifyFilesInDocument(document);

        return this;
    }

    /**
     * Шаги при проверке карточки документа
     *
     * @param document    документ
     * @param currentUser пользователь
     * @param folders     папки с которыми будем работать
     * @throws Exception
     */
    private void stepsOfVerifyDocument(Document document, Employee currentUser, Folder[] folders) throws Exception {
        loginStepsMobile
                .loginAs(currentUser) // Авторизация под участником рассмотрения документа
                .waitLoadMainPage(currentUser); // Ожидание открытия главной страницы
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openFolder(folders[0]);  // входим в папку
        sleep(500); //ожидание папок;

        // Проверки для Авторов документа и участников резолюции
        if (document.getAuthorOfDocument() == currentUser || assertThatInDocument().currentUserIsExecutiveManagersInResolution(document.getResolutionOfDocument(), currentUser)) {
            stepsOfVerifyExistingDocument(document, currentUser, folders[0]);
        }

        // Проверки для участников рассмотрения
        if (Arrays.asList(document.getRouteSchemeForDocument().getUserRoute()).contains(currentUser)) {
            if (!document.isOnExecution()) {
                stepsOfVerifyExistingDocument(document, currentUser, folders[0]);
            } else
                //Проверяем что документа нет гриде
                gridOfFoldersSteps.validateThatInGrid().itemDisappear(document.getDocumentType().getDocRegisterCardsName(), folders[0]);
        }

        // Выход из системы
        internalPageStepsMobile.logout();
    }

    /**
     * Общие шаги при проверке документа, который доступен пользователю для взаимодействия
     *
     * @param document
     * @param currentUser
     * @param folder
     * @throws Exception
     */
    private void stepsOfVerifyExistingDocument(Document document, Employee currentUser, Folder folder) throws Exception {

        // Проверяем отображение созданного документа в гриде папки
        gridOfFoldersSteps.validateThatInGrid().itemDisplayed(document.getDocumentType().getDocRegisterCardsName(), folder);

        // Проверяем отображение или отсутствие признака нового документа в гриде папки
        gridOfFoldersSteps.validateThatInGrid().itemHaveMarkOfNewDocumentForCurrentUser(document, currentUser);

        // Проверяем доступные операции с документом из грида
        validateThatOperations().accessedInTheGrid(document, currentUser);

        //Переход в документ из грида
        gridOfFoldersSteps.openItemInGrid(document.getDocumentType().getDocRegisterCardsName(), folder); // todo нужно чтобы шаблон отображения в арме имел более уникальное значение чем Тип документа.  Открывать записи по такому уникальному значению.
        //----------------------------------------------------------------ФОРМА - Документ
        verifyPageOfDocument(document, currentUser);

        //----------------------------------------------------------------ГРИД - Папка
        // Проверяем отсутствие признака нового документа после возвращения в грид папки
        gridOfFoldersSteps.validateThatInGrid().markOfNewDocumentDisappeared(document, folder);
    }

    /**
     * Проверяем карточку под разными пользователями
     *
     * @param document документ
     * @param folders  папки с которыми будем работать
     * @return
     * @throws Exception
     */
    public VerifyDocumentStepsMobile verifyDocumentOnDifferentUsers(Document document, Folder[] folders) throws Exception {
        if (document == null || folders == null) {
            return null;
        }

        // Проверки для Автора
        if (!(document.getAuthorOfDocument() == null)) {
            stepsOfVerifyDocument(document, document.getAuthorOfDocument(), folders);
        }

        // Проверки для каждого рассматривающиего
        if (!(document.getRouteSchemeForDocument().getUserRoute() == null)) {
            for (Employee employee : document.getRouteSchemeForDocument().getUserRoute()) {
                stepsOfVerifyDocument(document, employee, folders);
            }
        }

        // Проверки для участников резолюции
        if (!(document.getResolutionOfDocument() == null) && document.isOnExecution()) {
            for (Resolution resolution : document.getResolutionOfDocument()) {
                for (Employee executiveManager : resolution.getExecutiveManagers()) {
                    stepsOfVerifyDocument(document, executiveManager, folders);
                }
            }
        }
        return this;
    }

}
