package ru.motiw.web.steps.Tasks;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.motiw.web.elements.elementsweb.Tasks.UnionTasks.EditFormFoldersElements;
import ru.motiw.web.elements.elementsweb.Tasks.UnionTasks.UnionTasksElements;
import ru.motiw.web.model.Tasks.Folder;
import ru.motiw.web.model.Tasks.Task;
import ru.motiw.web.steps.BaseSteps;
import ru.motiw.web.steps.Home.InternalSteps;

import java.util.ArrayList;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static ru.motiw.utils.ElementUtil.scrollToAndClick;
import static ru.motiw.utils.WindowsUtil.openInNewWindow;
import static ru.motiw.web.model.URLMenu.TASKS;

/**
 * Шаги - отчет - грид задач - ЗАДАЧИ / ЗАДАЧИ
 */
public class UnionTasksSteps extends BaseSteps {

    UnionTasksElements unionTasksElements = page(UnionTasksElements.class);
    EditFormFoldersElements editFormFoldersElements = page(EditFormFoldersElements.class);

    /**
     * Переход в Задачи/Задачи
     */
    public static UnionTasksSteps goToUnionTasks() {
        openSectionOnURL(TASKS.getMenuURL());
        return page(UnionTasksSteps.class);
    }

    /**
     * Проверка загрузки страницы
     */
    private UnionTasksSteps ensurePageLoaded() {
        sleep(500);
        $(By.xpath("//div[@id='ext-comp-1001-bodyWrap']")).shouldBe(visible); // Панель ПУГЗ
        $(By.xpath("//div[contains(@data-ref,'bodyWrap')]/child::div[contains(@id,'taskpagingtoolbar')]"))
                .shouldBe(visible); // Панель Навигации по гриду
        return this;
    }

    /**
     * Поиск задачи
     */
    private UnionTasksSteps findTask(String taskName) {
        initializationInternalPage().searchFacilityOnTheGrid(taskName);
        return this;
    }

    /**
     * Ожидание маски грида - Задачи/Задачи
     */
    private UnionTasksSteps waitMaskForGridTask() {
        sleep(2000); // todo если не поставить sleep, то на linux из-за этого метода падает следующий вызываемый метод. На винде ошибки нет. Возможно, проблема в chromedriver-е 78 версии для linux, нужно будет проверить на новых версиях.
        $(By.xpath("//div[@class='x-mask x-border-box']")).shouldNotBe(visible);
        return this;
    }


    /**
     * Открытие формы задачи в гриде по гриппировке Папка через поиск по гриду
     * Задачи/Задачи
     *
     * @param task   Название задачи
     * @param folder папка для поиска задачи в гриде
     */
    public void openExistingTaskInTheFolderThroughTheSearch(Task task, Folder folder) {
        waitMaskForGridTask();
        ensurePageLoaded();
        $(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),'" + parseNameFolder(folder.getNameFolder())[0] + "')]")).click();
        switchTo().defaultContent();
        findTask(task.getTaskName());
        switchTo().frame($(By.cssSelector("#flow")));
        waitMaskForGridTask();
        $$(By.xpath("//a[contains(@href,'/user/unionmessage') and contains(text(),'" + task.getTaskName() + "')]"))
                .first().shouldBe(visible);
        SelenideElement link = $(By.xpath("//a[text()='" + task.getTaskName() + "']"));
        openInNewWindow(link.getAttribute("href"));
    }

    /**
     * Открытие формы задачи в гриде по гриппировке Папка
     *
     * @param task   Название задачи
     * @param folder папка для поиска задачи в гриде
     */
    public void openAnExistingTaskInFolder(Task task, Folder folder) {
        waitMaskForGridTask();
        ensurePageLoaded();
        $(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),'" + parseNameFolder(folder.getNameFolder())[0] + "')]")).click();
        waitMaskForGridTask();
        $$(By.xpath("//a[contains(@href,'/user/unionmessage') and text()='" + task.getTaskName() + "']"))
                .first().shouldBe(visible);
        SelenideElement link = $(By.xpath("//a[text()='" + task.getTaskName() + "']"));
        openInNewWindow(link.getAttribute("href"));
    }

    /**
     * Инициализируем внутренюю стр. (InternalElementsMobile)
     */
    private InternalSteps initializationInternalPage() {
        return page(InternalSteps.class);
    }

    /**
     * Вводим имя папки
     *
     * @param nameFolder зн-ие для формирования имени папки
     */
    private UnionTasksSteps enterTheNameOfTheFolder(String nameFolder, Folder folder) {
        try {
            editFormFoldersElements.getFolderName().waitUntil(visible, 5000);
        } catch (ElementNotFound e) {
            switchTo().defaultContent();
            switchTo().frame($(By.cssSelector("#flow")));
            editFormFoldersElements.getCloseEditFormOfFolder().click();
            openEditFormOfFolder(folder);
            editFormFoldersElements.getFolderName().waitUntil(visible, 5000);
        }
        editFormFoldersElements.getFolderName().clear();
        editFormFoldersElements.getFolderName().setValue(nameFolder);
        return this;
    }

    /**
     * Метод помогающий подготовить интерфейс к взаимодействию с объектом - Папка
     * (Проверка загрузки страницы, раскрытие всех элементов дерева подразделений, выбор группировки
     * Папка)
     *
     * @param countPanelGrouping передаваемое зн-ия - кол-во содержащихся элементов на ПУГЗ
     *                           (Панель управления группировкой задач)
     */

    /* todo
     * try  со старым с 2.1 .xpath("//div[@id='tree_folders']//div[contains(@id,'extdd')]//a//span")));
     * для чего нужно непонятно, но без этого не работает
     */
    public UnionTasksSteps beforeAddFolder(int countPanelGrouping) {
        ensurePageLoaded();
        try {
            (new WebDriverWait(getWebDriver(), 0, 50))
                    .until(ExpectedConditions.presenceOfElementLocated(By
                            .xpath("//div[@id='tree_folders']//div[contains(@id,'extdd')]//a//span")));
        } catch (WebDriverException e) {

            selectTheGroupInTheGridForUserComplete(unionTasksElements.getPanelGroupingTasks(), countPanelGrouping);

            unionTasksElements.getGroupingFolder().click(); // выбрать группировка - Папка
        }
        waitMaskForGridTask();
        $(By.xpath("//div[contains(@class,' x-tree-icon x-tree')]")).shouldBe(visible); // отображение пиктограмм Папка
        unwrapAllNodes(unionTasksElements.getPlusSubsites(),
                By.xpath("//img[contains(@class,'x-tree-ec-icon') and contains(@class,'plus')]"));
        unionTasksElements.getFolderInTheGroup().first().shouldBe(visible);
        return this;
    }


    /**
     * Выбираем группировку на ПУГЗ (панель управления группировкой задач)
     *
     * @param panelGrouping      элемент панели ПУГЗ
     * @param countPanelGrouping кол-во элементов на пенели управления группировкой задачи
     */
    public void selectTheGroupInTheGridForUserComplete(SelenideElement panelGrouping, int countPanelGrouping) {
        waitMaskForGridTask();
        panelGrouping.click();
        $$(By.xpath("//ul[@id='clickcombo-1003-picker-listEl']//li[contains(@class, 'x-boundlist-item')]"))
                .shouldHaveSize(countPanelGrouping); // проверяем, кол-во зн-ий в панели группировок
    }

    /**
     * Выбираем папку в иерархии папок по Имени
     *
     * @param folder передаваемые атрибуты папки
     */
    private void selectTheParentFolder(Folder folder) {
        $(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),'"
                + parseNameFolder(folder.getNameFolder())[0] + "')]")).shouldBe(visible).click();
        waitMaskForGridTask();
        $(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),'"
                + parseNameFolder(folder.getNameFolder())[0] + "')]")).shouldBe(visible).contextClick();
    }

    /**
     * Открытие Формы редактирования папки
     */
    private void openEditFormOfFolder(Folder folder) {
        if (folder.getParentFolder() != null) {
            selectTheParentFolder(folder.getParentFolder()); // Выбираем родительскую папку папку и выводим КМ для взаимодействия с папкой
        } else {
            waitMaskForGridTask();
            unionTasksElements.getFolderInTheGroup().first().contextClick();
        }
        try {
            unionTasksElements.getAddFolder().click(); // Добавить папку
        } catch (ElementNotFound e) {
            if (folder.getParentFolder() != null) {
                selectTheParentFolder(folder.getParentFolder()); // Выбираем родительскую папку папку и выводим КМ для взаимодействия с папкой
            } else {
                waitMaskForGridTask();
                unionTasksElements.getFolderInTheGroup().first().contextClick();
            }
            unionTasksElements.getAddFolder().click(); // Добавить папку
        }
        getFrameObject($(By.xpath("//iframe[contains(@src,'/user/smart_folder')]"))); // уходим во фрейм окна - Редактирование папки
    }

    /**
     * Добавление объекта - Папка
     *
     * @param folders кол-во передаваемых папок с атрибутами (настройки СП (смарт-папки); ОП (общие папки)..)
     */
    public void addFolders(Folder[] folders) {
        if (folders != null) {
            for (Folder folder : folders) {
                openEditFormOfFolder(folder);
                enterTheNameOfTheFolder(folder.getNameFolder(), folder);
                if (folder.isUseFilter() & folder.isChooseRelativeValue()) {
                    editFormFoldersElements.getCheckUseFilter().click();
                    setTheConditionOfFiltration(folder.getFilterField(), folder.isChooseRelativeValue());
                }
                if (folder.isSharedFolder()) editFormFoldersElements.getCheckFolderSharedFilter().click();
                if (folder.isAddSharedFolderForAll()) editFormFoldersElements.getCheckFolderAddForAll().click();
                if (folder.isAddSharedFolderForNewUsers())
                    editFormFoldersElements.getCheckAddSharedFolderForNewUsers().click();

                editFormFoldersElements.getSaveСhangesInTheCustomFolder().click();
                refresh();
                switchTo().defaultContent();
                switchTo().frame($(By.cssSelector("#flow")));
                unwrapAllNodes(unionTasksElements.getPlusSubsites(),
                        By.xpath("//tr[@aria-expanded=\"false\"]//div[contains(@class,\"plus x-tree-expander\")]"));
                checkDisplayCreateAFolderInTheGrid(folder.getNameFolder(), folder.isUseFilter()); // проверяем созданную Папку
            }
        }
    }

    /**
     * Удаление объекта - Папка
     */
    public void deleteFolders(Folder[] folders) {
        for (Folder folder : folders) {
            sleep(2000);
            unwrapAllNodes(unionTasksElements.getPlusSubsites(),
                    By.xpath("//tr[@aria-expanded=\"false\"]//div[contains(@class,\"plus x-tree-expander\")]")); //  не работает

            // Все элементы папок
            ElementsCollection elementsCollection = $$(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),\"\")]"));

            // Помещаем все названия папок в массив
            ArrayList<String> namesOfFolders = new ArrayList<>();
            for (SelenideElement element : elementsCollection) {
                namesOfFolders.add(element.getText().split("[(/0)]")[0]);
            }

            // Если папка существует, то удаляем её
            if (namesOfFolders.contains(folder.getNameFolder() + " ")) {
                waitMaskForGridTask();
                SelenideElement elementOfFolder = $(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),'"
                        + parseNameFolder(folder.getNameFolder())[0] + "')]"));
                elementOfFolder.shouldBe(visible).contextClick();

                unionTasksElements.getDeleteFolder().waitUntil(visible, 2000).click(); // Удалить папку
                $(By.xpath("//span[text()=\"Да\"]/ancestor::span[@class=\"x-btn-wrap x-btn-wrap-default-small \"]")).waitUntil(visible, 2000).click();
                waitMaskForGridTask();
                elementOfFolder.shouldNotBe(visible); // Проверяем что удаленная папка не отображается на Панели управления группировкой задач (ПУГЗ)
            }
        }
    }


    /**
     * Удаление объекта - Папка (удаление по названию папки)
     */
    public void deleteAllFolders(String partOfNameFolder) {
        // Удаление всех папок созданных автотестами
        ElementsCollection elementsCollection = $$(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),'" + partOfNameFolder + "')]")); // подставлякм название папки

        ArrayList<String> stringsText = new ArrayList<>();
        for (SelenideElement element : elementsCollection) {
            stringsText.add(element.getText());
        }

        for (String nameFolder : stringsText) {
            unwrapAllNodes(unionTasksElements.getPlusSubsites(),
                    By.xpath("//tr[@aria-expanded=\"false\"]//div[contains(@class,\"plus x-tree-expander\")]"));
            SelenideElement elementOfFolder = $(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),'"
                    + parseNameFolder(nameFolder)[0] + "')]"));
            scrollToAndClick(elementOfFolder);
            sleep(1000);
            elementOfFolder.contextClick();
            sleep(2000);
            unionTasksElements.getDeleteFolder().click(); // Удалить папку

            $(By.xpath("//div[contains(@class,\"x-component x-window-text x-box-item x-component-default\")]")).waitUntil(visible, 4000);
            $(By.xpath("//span[text()=\"Да\"]/ancestor::span[@class=\"x-btn-wrap x-btn-wrap-default-small \"]")).click();
            sleep(3000);
            elementOfFolder.shouldNotBe(visible); // Проверяем что удаленная папка не отображается на Панели управления группировкой задач (ПУГЗ)
        }
    }

    /**
     * Формируем условие фильтра - Начало (относительное значение == Сегодня)
     *
     * @param field                передаваемое навание поля для формирования условия фильтрации
     * @param relativeImportanceOf относительное зн-ие для условия папки
     */
    private UnionTasksSteps setTheConditionOfFiltration(String field, boolean relativeImportanceOf) {
        $(By.xpath("//table[contains(@id,'treeview')][2]//td[1]//span")).click();
        // Выбираем поле для фильтрации
        clearTextInInputViaHotKeys($(By.xpath("//div[@id='sffieldcombochooser']//input")));
        $(By.xpath("//div[@id='sffieldcombochooser']//input")).setValue(field);
        $(By.xpath("//table[contains(@id,'treeview')][2]//td[3]//div")).click();   // Выбор поля - Значение
        if (relativeImportanceOf) {
            $(By.xpath("//input[contains(@id,'checkbox') and (@type='checkbox')]//ancestor::span")).click();
        }
        return this;
    }

    /**
     * Проверяем отображение созданных папок на Панели управления группировкой задач (ПУГЗ)
     *
     * @param folder     передаем название папки
     * @param useAFilter передаем параметр установленного зн-ия - Использовать фильтр
     */
    private void checkDisplayCreateAFolderInTheGrid(String folder, boolean useAFilter) {
        if (useAFilter) {
            $(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),'"
                    + parseNameFolder(folder)[0] + "')]")).waitUntil(visible, 10000);
            $(By.xpath("//b[contains(text(), '" + parseNameFolder(folder)[0] + "')]/ancestor::div[contains(@class,'x-grid-cell')]" +
                    "/child::div[contains(@class, \"folder\")] ")).waitUntil(visible, 10000).shouldBe(visible);
        } else
            $(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),'" + parseNameFolder(folder)[0] + "')]"))
                    .waitUntil(visible, 10000);

    }

    /**
     * Проверяем отображение созданной ОП (Общая папка) у нового пользователя
     *
     * @param folder             атрибуты объекта Папка
     * @param countPanelGrouping счетчик - кол-ва значений группировки на ПУГЗ (Панель управления группировкой задач)
     */
    public void checkTheMapASharedFolderFromTheNewlyCreatedUser(Folder folder, int countPanelGrouping) {
        beforeAddFolder(countPanelGrouping);
        checkDisplayCreateAFolderInTheGrid(folder.getNameFolder(), folder.isUseFilter()); // Проверяем созданные Папки
    }

    /**
     * Метод удаляет (0/0/0) в названии папки
     *
     * @param folder передаваемое значение названия папки (имя целиком со счетчиками)
     */
    private String[] parseNameFolder(String folder) {
        if (folder == null)
            return null;
        String[] splitNameFolderGrid;
        String nameFolderParse = $(By.xpath("//span[contains(@class,'x-tree-node-text ')]/b[contains(text(),'"
                + folder + "')]"))
                .getText();
        splitNameFolderGrid = nameFolderParse.split("[(/0)]");
        return splitNameFolderGrid;
    }

}