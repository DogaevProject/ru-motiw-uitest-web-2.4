package ru.motiw.mobile.steps.Folders.ValidationStepsFolders;

import ru.motiw.mobile.elements.Internal.GridOfFolderElementsMobile;
import ru.motiw.mobile.steps.Folders.GridOfFoldersSteps;
import ru.motiw.mobile.steps.InternalStepsMobile;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Tasks.Folder;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.refresh;

/**
 * Проверки для грида Папки и объектов, которые он содержит
 */
public class ValidationGridOfFolders extends InternalStepsMobile {

    private GridOfFolderElementsMobile gridOfFolderElementsMobile = page(GridOfFolderElementsMobile.class);
    private GridOfFoldersSteps gridOfFoldersSteps = page(GridOfFoldersSteps.class);

    /**
     * Проверяем отображение созданного объекта в гриде раздела - Папки
     *
     * @param nameOfItem уникальный текст по которому ищем объект в гриде (наименование задачи, наименование типа документа)
     * @param folder     наименование папки в к-й будет содержаться созданный объект
     * @return GridOfFoldersSteps
     */
    public ValidationGridOfFolders itemDisplayed(String nameOfItem, Folder folder) {
        if (!internalElementsMobile.getMainTitle().is(text(folder.getNameFolder()))) {
            gridOfFoldersSteps.openFolder(folder);  // входим в созданную папку
        }
        gridOfFolderElementsMobile.getItemInTheGrid(nameOfItem).waitUntil(visible, 2000); // проверяем отображение созданного объекта в гриде (отображается наименование задачи)
        return this;
    }

    /**
     * Проверяем исчезновение объекта в гриде раздела - Папки
     *
     * @param nameOfItem уникальный текст по которому ищем объект в гриде (наименование задачи, наименование типа документа)
     * @return
     */
    public ValidationGridOfFolders itemDisappear(String nameOfItem, Folder folder) {

        // Если после завершения задачи мы не перешли в папку, то переходим в созданную папку
        if (!internalElementsMobile.getMainTitle().is(text(folder.getNameFolder()))) {
            refresh(); // goToHome() бывает клик не на ту кнопку, поэтому перезагружаем
            goToHome();
            gridOfFoldersSteps.openFolder(folder);  // входим в созданную папку
        }

        // Если пакет amq ещё не пришел, то грид может не обновиться. Поэтому делаем так:
        // 1. проверяем на отображение завершенной задачи в гриде.
        // 2. и если завершенная задача продолжает отображаться, то перезагружаем страницу и проверяем снова.
        if ((gridOfFolderElementsMobile.getItemInTheGrid(nameOfItem).isDisplayed())) {
            refresh();
            try {
                gridOfFolderElementsMobile.getItemHeaderOfGridFolder().waitUntil(appear, 20000);
            } catch (Error e) {
                // обрабатываем случай, когда в папке нет дургих объектов
                gridOfFolderElementsMobile.getTextNoHaveDocumentInGridFolder().shouldBe(visible);
            }
            gridOfFolderElementsMobile.getItemInTheGrid(nameOfItem).shouldNotBe(visible);
        }

        return this;
    }

    /**
     * Проверяем отображение или отсутствие признака нового документа в гриде папки, в зависимости от того, в какой роли текущий пользователь
     *
     * @param document
     * @param currentUser текущий пользователь
     */
    public ValidationGridOfFolders itemHaveMarkOfNewDocumentForCurrentUser(Document document, Employee currentUser) {

        // Автор документа
        if (document.getAuthorOfDocument() != null && document.getAuthorOfDocument() == currentUser) {
            gridOfFolderElementsMobile.getMarkOfNewItem(document.getDocumentType().getDocRegisterCardsName()).shouldNotBe(visible);
        }

        // Рассматривающий документа
        if (document.getRouteSchemeForDocument().getUserRoute() != null) {
            for (Employee userRoute : document.getRouteSchemeForDocument().getUserRoute()) {
                if (userRoute == currentUser) {
                    gridOfFolderElementsMobile.getMarkOfNewItem(document.getDocumentType().getDocRegisterCardsName()).shouldBe(visible);
                }
            }
        }
        return this;
    }

    /**
     * Проверяем отсутствие признака нового документа после возвращения в гриде папки
     *
     * @param document
     */
    public ValidationGridOfFolders markOfNewDocumentDisappeared(Document document, Folder folder) {
        // Если после выполнения операций задачи мы не перешли в папку, то переходим в созданную папку
        if (!internalElementsMobile.getMainTitle().is(text(folder.getNameFolder()))) {
            goToHome();
            gridOfFoldersSteps.openFolder(folder);  // входим в созданную папку
        }

        /*
          Если пакет amq ещё не пришел, то грид может не обновиться. Поэтому делаем так:
          1. проверяем на отображение Признака в гриде.
          2. если Признак продолжает отображаться, то перезагружаем страницу и проверяем снова.
         */
        if ((gridOfFolderElementsMobile.getMarkOfNewItem(document.getDocumentType().getDocRegisterCardsName()).isDisplayed())) {
            refresh();
            gridOfFolderElementsMobile.getItemInTheGrid(document.getDocumentType().getDocRegisterCardsName()).waitUntil(visible, 10000);
            gridOfFolderElementsMobile.getMarkOfNewItem(document.getDocumentType().getDocRegisterCardsName()).shouldNotBe(visible);
        }
        return this;
    }

}
