package ru.motiw.testMobile;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.testng.ScreenShooter;
import com.codeborne.selenide.testng.TextReport;
import com.codeborne.selenide.testng.annotations.Report;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.motiw.data.dataproviders.DocflowAdministrationMobile;
import ru.motiw.mobile.elements.Internal.InternalElementsMobile;
import ru.motiw.mobile.steps.Document.DocumentStepsMobile;
import ru.motiw.mobile.steps.Folders.GridOfFoldersSteps;
import ru.motiw.mobile.steps.InternalStepsMobile;
import ru.motiw.mobile.steps.LoginStepsMobile;
import ru.motiw.web.elements.elementsweb.Documents.EditDocument.EditDocumentRouteTabElements;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Administration.Users.Status;
import ru.motiw.web.model.DocflowAdministration.DocumentRegistrationCards.DocRegisterCards;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Tasks.Folder;
import ru.motiw.web.steps.Administration.Users.UsersSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.FormDocRegisterCardsEditConnectedRoutesSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.FormDocRegisterCardsEditGeneralSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.GridDocRegisterCardsSteps;
import ru.motiw.web.steps.Documents.EditDocument.EditDocumentSteps;
import ru.motiw.web.steps.Home.InternalSteps;
import ru.motiw.web.steps.Login.LoginStepsSteps;
import ru.motiw.web.steps.Tasks.UnionTasksSteps;

import java.util.ArrayList;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertTrue;
import static ru.motiw.web.steps.Administration.Users.DepartmentSteps.goToURLDepartments;
import static ru.motiw.web.steps.Documents.CreateDocument.NewDocumentSteps.goToURLNewDocument;
import static ru.motiw.web.steps.Tasks.UnionTasksSteps.goToUnionTasks;

@Listeners({ScreenShooter.class, TextReport.class})
@Report
public class ConsiderationDocumentMobileTest extends DocflowAdministrationMobile {

    private LoginStepsSteps loginPageSteps;
    private InternalSteps internalPageSteps;
    private UnionTasksSteps unionTasksSteps;
    private UsersSteps userPageSteps;
    private FormDocRegisterCardsEditConnectedRoutesSteps formDocRegisterCardsEditConnectedRoutesSteps;
    private FormDocRegisterCardsEditGeneralSteps formDocRegisterCardsEditGeneralSteps;
    private LoginStepsMobile loginStepsMobile;
    private GridOfFoldersSteps gridOfFoldersSteps;
    private InternalStepsMobile internalStepsMobile;
    private DocumentStepsMobile documentStepsMobile;
    private InternalElementsMobile internalElementsMobile;
    private EditDocumentRouteTabElements editDocumentRouteTabElements;
    private EditDocumentSteps editDocumentSteps;

    @BeforeClass
    public void beforeTest() {
        loginPageSteps = page(LoginStepsSteps.class);
        internalPageSteps = page(InternalSteps.class);
        unionTasksSteps = page(UnionTasksSteps.class);
        userPageSteps = page(UsersSteps.class);
        formDocRegisterCardsEditConnectedRoutesSteps = page(FormDocRegisterCardsEditConnectedRoutesSteps.class);
        formDocRegisterCardsEditGeneralSteps = page(FormDocRegisterCardsEditGeneralSteps.class);
        loginStepsMobile = page(LoginStepsMobile.class);
        gridOfFoldersSteps = page(GridOfFoldersSteps.class);
        internalStepsMobile = page(InternalStepsMobile.class);
        documentStepsMobile = page(DocumentStepsMobile.class);
        internalElementsMobile = page(InternalElementsMobile.class);
        editDocumentRouteTabElements = page(EditDocumentRouteTabElements.class);
        editDocumentSteps = page(EditDocumentSteps.class);
    }

    private String urlDocInWeb_1;
    private String urlDocInWeb_2;


    @Test(dataProvider = "objectDataForConsiderationDocument", dataProviderClass = DocflowAdministrationMobile.class)
    public void preconditionInWeb(Department[] departments, Employee[] employee,
                                  DocRegisterCards registerCards_1, DocRegisterCards registerCards_2, Document document_1, Document document_2, Folder[] folders) {
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице


        //---------------------------------------------------------------------------------Пользователи и Подразделения
        // Инициализируем страницу и переходим на нее - Администрирование/Пользователи (Подразделения)
        goToURLDepartments();
        userPageSteps.createUser(employee[1].setStatus(Status.BOSS)); // в главном подразделении добавляем начальника
        goToURLDepartments().createDepartment(departments[0]);
        userPageSteps.createUser(employee[0].setDepartment(departments[0]));

        //---------------------------------------------------------------- Задачи/Задачи
        goToUnionTasks();
        unionTasksSteps.beforeAddFolder(21);
        // Добавляем Папки(/у)
        unionTasksSteps.addFolders(new Folder[]{folders[0].setNameFolder("wD_Smart_Box " + randomString(4))
                .setUseFilter(true)
                .setFilterField("Начало")
                .setChooseRelativeValue(true)
                .setSharedFolder(true)
                .setAddSharedFolderForAll(true) // признак "Добавить всем", чтобы было попадание задачи по докумету в папку  Пользователя, которого добавляем в блок МС
                .setAddSharedFolderForNewUsers(false)});
        //---------------------------------------------------------------------------------РКД
        // Переход в раздел Администрирование ДО/Регистрационные карточки документов
        FormDocRegisterCardsEditGeneralSteps editRCD = GridDocRegisterCardsSteps
                .goToURLGridDocRegisterCards().addDocRegisterCards(); // Добавить РКД

        /**
         * Устанавливаем настройки для РКД-1 (регистрационная карточка документа) на вкладке - МАРШРУТЫ
         */
        formDocRegisterCardsEditConnectedRoutesSteps.checkBoxUseAllPossibleRoutes(registerCards_1);

        formDocRegisterCardsEditGeneralSteps.addNameDocumentRegisterCards(registerCards_1);

        // Сохранение настроек РКД
        GridDocRegisterCardsSteps gridDocRegisterCards = editRCD.saveAllChangesInDoc(); // Сохранение всех изменений в РКД
        gridDocRegisterCards.verifyDocRegisterCards(registerCards_1.getDocRegisterCardsName());


        // Переход в раздел Администрирование ДО/Регистрационные карточки документов
        FormDocRegisterCardsEditGeneralSteps editRCD_2 = GridDocRegisterCardsSteps
                .goToURLGridDocRegisterCards().addDocRegisterCards(); // Добавить РКД

        /**
         * Устанавливаем настройки для РКД-2 (регистрационная карточка документа) на вкладке - МАРШРУТЫ
         */
        formDocRegisterCardsEditConnectedRoutesSteps.checkBoxUseAllPossibleRoutes(registerCards_2);

        formDocRegisterCardsEditGeneralSteps.addNameDocumentRegisterCards(registerCards_2);

        // Сохранение настроек РКД
        GridDocRegisterCardsSteps gridDocRegisterCards_2 = editRCD_2.saveAllChangesInDoc(); // Сохранение всех изменений в РКД
        gridDocRegisterCards_2.verifyDocRegisterCards(registerCards_2.getDocRegisterCardsName());


        //-------------------------------------------------------------------------------Создать документ
        goToURLNewDocument().createDocument(document_1).openCreatedDoc();
        String parentWindowHandler = getWebDriver().getWindowHandle(); //Запоминаем окно в котором находимся
        ArrayList<String> tabs = new ArrayList<>(getWebDriver().getWindowHandles()); //все открытые окна сохраняем в список
        switchTo().window(tabs.get(1)); //переходим во вторую вкладку
        //Сохранить url для перехода по прямой ссылке
        urlDocInWeb_1 = getWebDriver().getCurrentUrl();
        getWebDriver().close();
        getWebDriver().switchTo().window(parentWindowHandler);

        goToURLNewDocument().createDocument(document_2).openCreatedDoc();
        parentWindowHandler = getWebDriver().getWindowHandle(); //Запоминаем окно в котором находимся
        tabs = new ArrayList<>(getWebDriver().getWindowHandles()); //все открытые окна сохраняем в список
        switchTo().window(tabs.get(1)); //переходим во вторую вкладку
        //Сохранить url для перехода по прямой ссылке
        urlDocInWeb_2 = getWebDriver().getCurrentUrl();
        getWebDriver().close();
        getWebDriver().switchTo().window(parentWindowHandler);

        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }

    @Test(dataProvider = "objectDataForConsiderationDocument", dataProviderClass = DocflowAdministrationMobile.class, dependsOnMethods = "preconditionInWeb")
    public void verifyConsideration_1(Department[] departments, Employee[] employee, DocRegisterCards registerCards_1, DocRegisterCards registerCards_2, Document document_1, Document document_2, Folder[] folders) {
        loginStepsMobile
                .loginAs(employee[0]) // Авторизация под участником рассмотрения документа
                .waitLoadMainPage(employee[0]); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.validateThatInGrid().itemDisplayed(document_1.getDocumentType().getDocRegisterCardsName(), folders[0]);
        // ---------------------------------------------------------------- Выполнение операций в карточке документа
        // Переход в документ из грида
        gridOfFoldersSteps.openItemInGrid(document_1.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        // Выполнение операций
        // Ожидание и проверка кнопок тулбара
        documentStepsMobile.waitToolbarOfMenu();
        documentStepsMobile.executionOperationWithAdditionText("Согласовать", "Согласовать");// согласовать
        internalElementsMobile.getToastWithText().waitUntil(text("Рассмотрен"), 20000);
        refresh();
        //Проверяем что документа нет гриде
        gridOfFoldersSteps.validateThatInGrid().itemDisappear(document_1.getDocumentType().getDocRegisterCardsName(), folders[0]);

        // Переход в документ из грида
        gridOfFoldersSteps.openItemInGrid(document_2.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        // Выполнение операций
        // Ожидание и проверка кнопок тулбара
        documentStepsMobile.waitToolbarOfMenu();
        documentStepsMobile.executionOperationWithAdditionText("Согласовать с замечаниями", "Согласовать с замечаниями");// согласовать
        internalElementsMobile.getToastWithText().waitUntil(text("Рассмотрен (с замечаниями)"), 20000);
        refresh();
        //Проверяем что документа нет гриде
        gridOfFoldersSteps.validateThatInGrid().itemDisappear(document_2.getDocumentType().getDocRegisterCardsName(), folders[0]);


        // Выход из системы
        internalStepsMobile.logout();

    }

    @Test(dataProvider = "objectDataForConsiderationDocument", dataProviderClass = DocflowAdministrationMobile.class, dependsOnMethods = "verifyConsideration_1")
    public void verifyConsideration_2(Department[] departments, Employee[] employee, DocRegisterCards registerCards_1, DocRegisterCards registerCards_2, Document document_1, Document document_2, Folder[] folders) {
        loginStepsMobile
                .loginAs(employee[1]) // Авторизация под участником рассмотрения документа
                .waitLoadMainPage(employee[1]); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.validateThatInGrid().itemDisplayed(document_1.getDocumentType().getDocRegisterCardsName(), folders[0]);
        // ---------------------------------------------------------------- Выполнение операций в карточке документа
        // Переход в документ из грида
        gridOfFoldersSteps.openItemInGrid(document_1.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        // Выполнение операций
        // Ожидание и проверка кнопок тулбара
        documentStepsMobile.waitToolbarOfMenu();
        documentStepsMobile.executionOperationWithAdditionText("На доработку", "На доработку");
        internalElementsMobile.getToastWithText().waitUntil(text("Возврат на доработку"), 20000);
        refresh();
        //Проверяем что документа нет гриде
        gridOfFoldersSteps.validateThatInGrid().itemDisappear(document_1.getDocumentType().getDocRegisterCardsName(), folders[0]);

        // Переход в документ из грида
        gridOfFoldersSteps.openItemInGrid(document_2.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        // Выполнение операций
        // Ожидание и проверка кнопок тулбара
        documentStepsMobile.waitToolbarOfMenu();
        documentStepsMobile.executionOperationWithAdditionText("Отказаться согласовать", "Отказаться согласоват");
        internalElementsMobile.getToastWithText().waitUntil(text("Отказ согласовать"), 20000);
        refresh();
        //Проверяем что документа нет гриде
        gridOfFoldersSteps.validateThatInGrid().itemDisappear(document_2.getDocumentType().getDocRegisterCardsName(), folders[0]);

        // Выход из системы
        internalStepsMobile.logout();
    }

    @Test(dataProvider = "objectDataForConsiderationDocument", dataProviderClass = DocflowAdministrationMobile.class, dependsOnMethods = "verifyConsideration_2")
    public void verifyStatusOfConsiderationInWeb(Department[] departments, Employee[] employee, DocRegisterCards registerCards_1, DocRegisterCards registerCards_2, Document document_1, Document document_2, Folder[] folders) {
        open(Configuration.baseUrl);
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице
        // проверка статусов в мс
        open(urlDocInWeb_1);
        editDocumentSteps.routesTab();
        editDocumentRouteTabElements.statusBlockDiagramIsReviewed("Произвольная рабочая группа", employee[0].getLastName()).shouldBe(visible);
        editDocumentRouteTabElements.statusBlockDiagramIsBackToRevision("Произвольный начальник", employee[1].getLastName()).shouldBe(visible);

        open(urlDocInWeb_2);
        editDocumentSteps.routesTab();
        editDocumentRouteTabElements.statusBlockDiagramIsReviewedWithComment("Произвольная рабочая группа", employee[0].getLastName()).shouldBe(visible);
        editDocumentRouteTabElements.statusBlockDiagramIsDenialReview("Произвольный начальник", employee[1].getLastName()).shouldBe(visible);

        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }
}
