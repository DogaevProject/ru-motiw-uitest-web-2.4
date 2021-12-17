package ru.motiw.mobile.steps.Folders;

import com.codeborne.selenide.ex.ElementNotFound;
import ru.motiw.mobile.elements.Internal.GridOfFolderElementsMobile;
import ru.motiw.mobile.steps.Folders.ValidationStepsFolders.ValidationGridOfFolders;
import ru.motiw.mobile.steps.InternalStepsMobile;
import ru.motiw.web.model.Tasks.Folder;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.fail;
import static ru.motiw.utils.ElementUtil.scrollToAndClick;

/*
 * Страница грида - Папка
 */

public class GridOfFoldersSteps extends InternalStepsMobile {

    private GridOfFolderElementsMobile gridOfFolderElementsMobile = page(GridOfFolderElementsMobile.class);

    /**
     * Открытие папки
     */
    public void openFolder(Folder folder) {
        try {
            sleep(500);
            gridOfFolderElementsMobile.getFolder(folder.getNameFolder()).click();
        } catch (ElementNotFound notFoundFolder) {
            notFoundFolder.printStackTrace();
            fail("Не найдена папка");
        }

        try {
            // Ждем элемент с названием группировки Грида Папки
            gridOfFolderElementsMobile.getItemHeaderOfGridFolder().waitUntil(appear, 5000);
        } catch (Throwable notFoundItemHeaderOfGridFolder) {
            try {
                // Ждем элемент с Текст "Документов нет" в Гриде Папки
                gridOfFolderElementsMobile.getTextNoHaveDocumentInGridFolder().waitUntil(visible, 2000);
            } catch (ElementNotFound notFoundTextNoHaveDocumentInGridFolder) {
                notFoundItemHeaderOfGridFolder.printStackTrace();
                notFoundTextNoHaveDocumentInGridFolder.printStackTrace();
                fail("Не найден элемент в гриде папки");
            }
        }
    }


    /**
     * Открываем объект в гриде папки
     *
     * @param nameOfItem уникальный текст по которому ищем объект в гриде (наименование задачи, наименование типа документа)
     * @return
     */
    public GridOfFoldersSteps openItemInGrid(String nameOfItem, Folder folder) {

        if (!internalElementsMobile.getMainTitle().is(text(folder.getNameFolder()))) // Если мы не в папке, то переходим в неё
        {
            openFolder(folder);  // входим в созданную папку
        }

        gridOfFolderElementsMobile.getItemInTheGrid(nameOfItem).click(); // Открываем по записи, которая содержит Тип документа.

        sleep(500);
        return page(GridOfFoldersSteps.class);
    }


    /**
     * Нажатие на кнопку конт.меню операций для объекта (задачи\документа) в гриде папки
     *
     * @param nameOfItem - текст по которому находим объект в гриде
     */
    public void clickContextMenuForItemInGrid(String nameOfItem) {
        gridOfFolderElementsMobile.getItemInTheGrid(nameOfItem).waitUntil(visible, 5000);
        if ((gridOfFolderElementsMobile.getButtonOfCloseContextMenu(nameOfItem).is(visible))) {
            try {
                // click ContextMenu
                gridOfFolderElementsMobile.getButtonOfCloseContextMenu(nameOfItem).click(); // иногда КМ само закрывается
            } catch (ElementNotFound ignored) {
            } finally {
                // Проверяем, что Кнопка открытия конт.меню появилась
                gridOfFolderElementsMobile.getButtonOfOpenContextMenu(nameOfItem).shouldBe(visible);
            }
            // Проверяем, что конт.меню закрылось
            gridOfFolderElementsMobile.getContextMenu().shouldNotBe(visible);
        } else {
            gridOfFolderElementsMobile.getButtonOfOpenContextMenu(nameOfItem).waitUntil(visible, 5000);
            // Скроллинг до объекта (задачи\документа) в гриде и клик
            scrollToAndClick(gridOfFolderElementsMobile.getButtonOfOpenContextMenu(nameOfItem));
            // Ждем скрытие маски на кнопке Открытия/закрытия конт.меню
            gridOfFolderElementsMobile.getMaskOnButtonOfCloseContextMenu(nameOfItem).waitUntil(not(visible), 5000);
            // Проверяем, что Кнопка закрытия конт.меню появилась
            gridOfFolderElementsMobile.getButtonOfCloseContextMenu(nameOfItem).shouldBe(visible);

            // Проверяем отображение текста во всплывающем Toast-е "Нет доступных операций" или Конт.меню операций
            if (!internalElementsMobile.getToastWithText().is(text("Нет доступных операций"))) {
                gridOfFolderElementsMobile.getContextMenu().shouldBe(visible);
            }
        }
    }

    /**
     * Проверки для грида Папки и объектов, которые он содержит
     *
     * @return
     */
    public ValidationGridOfFolders validateThatInGrid() {
        return page(ValidationGridOfFolders.class);
    }
}

