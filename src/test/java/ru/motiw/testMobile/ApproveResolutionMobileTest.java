package ru.motiw.testMobile;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.testng.ScreenShooter;
import com.codeborne.selenide.testng.TextReport;
import com.codeborne.selenide.testng.annotations.Report;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.motiw.data.dataproviders.TestsOfResolutionsMobile;
import ru.motiw.mobile.elements.FormElementsMobile;
import ru.motiw.mobile.model.AuthorOfAnnotation;
import ru.motiw.mobile.steps.AnnotationOnFilesSteps;
import ru.motiw.mobile.steps.Document.ExecutionDocumentStepsMobile;
import ru.motiw.mobile.steps.Document.ResolutionStepsMobile;
import ru.motiw.mobile.steps.Document.VerifyDocumentStepsMobile;
import ru.motiw.mobile.steps.Folders.GridOfFoldersSteps;
import ru.motiw.mobile.steps.InternalStepsMobile;
import ru.motiw.mobile.steps.LoginStepsMobile;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
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
public class ApproveResolutionMobileTest extends TestsOfResolutionsMobile {

    private LoginStepsSteps loginPageSteps;
    private LoginStepsMobile loginStepsMobile;
    private GridOfFoldersSteps gridOfFoldersSteps;
    private InternalSteps internalPageSteps;
    private InternalStepsMobile internalStepsMobile;
    private UnionTasksSteps unionTasksSteps;
    private UsersSteps userPageSteps;
    private FormDocRegisterCardsEditConnectedRoutesSteps formDocRegisterCardsEditConnectedRoutesSteps;
    private FormDocRegisterCardsEditGeneralSteps formDocRegisterCardsEditGeneralSteps;
    private EditDocumentSteps editDocumentSteps;
    private ResolutionStepsMobile resolutionStepsMobile;
    private AnnotationOnFilesSteps annotationOnFilesSteps;

    @BeforeClass
    public void beforeTest() {
        loginPageSteps = page(LoginStepsSteps.class);
        loginStepsMobile = page(LoginStepsMobile.class);
        gridOfFoldersSteps = page(GridOfFoldersSteps.class);
        internalPageSteps = page(InternalSteps.class);
        internalStepsMobile = page(InternalStepsMobile.class);
        unionTasksSteps = page(UnionTasksSteps.class);
        userPageSteps = page(UsersSteps.class);
        formDocRegisterCardsEditConnectedRoutesSteps = page(FormDocRegisterCardsEditConnectedRoutesSteps.class);
        formDocRegisterCardsEditGeneralSteps = page(FormDocRegisterCardsEditGeneralSteps.class);
        editDocumentSteps = page(EditDocumentSteps.class);
        resolutionStepsMobile = page(ResolutionStepsMobile.class);
        annotationOnFilesSteps = page(AnnotationOnFilesSteps.class);
    }

    private String urlDocInWeb;
    private String urlDocInArm;

    @Test(dataProvider = "objectDataForCreateProjectsInWeb", dataProviderClass = TestsOfResolutionsMobile.class)
    public void createFirstProjectInWeb(Department[] departments, Employee[] employee,
                                        DocRegisterCards registerCards, Document document, Folder[] folders) {
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице


        //---------------------------------------------------------------------------------Пользователи и Подразделения
        // Инициализируем страницу и переходим на нее - Администрирование/Пользователи (Подразделения)
        goToURLDepartments().createDepartment(departments[0]);

        userPageSteps.createUser(employee[0].setDepartment(departments[0]));
        userPageSteps.createUser(employee[1].setDepartment(departments[0]));
        userPageSteps.createUser(employee[2].setDepartment(departments[0]));
        userPageSteps.createUser(employee[3].setDepartment(departments[0]));

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

        //*
        //* Устанавливаем настройки для РКД (регистрационная карточка документа) на вкладке - МАРШРУТЫ
        //*
        formDocRegisterCardsEditConnectedRoutesSteps.checkBoxUseAllPossibleRoutes(registerCards);

        formDocRegisterCardsEditGeneralSteps.addNameDocumentRegisterCards(registerCards);

        // Сохранение настроек РКД
        GridDocRegisterCardsSteps gridDocRegisterCards = editRCD.saveAllChangesInDoc(); // Сохранение всех изменений в РКД
        gridDocRegisterCards.verifyDocRegisterCards(registerCards.getDocRegisterCardsName());


        //-------------------------------------------------------------------------------Создать документ и Переход в созданный документ по ссылке
        goToURLNewDocument()
                .createDocument(document)
                .openCreatedDoc();

        // Редактирование созданного документа
        String parentWindowHandler = getWebDriver().getWindowHandle(); //Запоминаем окно в котором находимся
        ArrayList<String> tabs = new ArrayList<>(getWebDriver().getWindowHandles()); //все открытые окна сохраняем в список
        switchTo().window(tabs.get(1)); //переходим во вторую вкладку
        //Сохранить url для перехода по прямой ссылке
        urlDocInWeb = getWebDriver().getCurrentUrl();
        editDocumentSteps.waitDocument();
        // Создание проекта
        editDocumentSteps.createProjectOfResolution(document.getResolutionOfDocument()[0]);
        getWebDriver().close();
        getWebDriver().switchTo().window(parentWindowHandler);
        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }

    @Test(dataProvider = "objectDataForVerifyingApproveFirstProjectResolution", dataProviderClass = TestsOfResolutionsMobile.class, dependsOnMethods = "createFirstProjectInWeb")
    public void verifyingApproveProjectResolutionFirstUser(Employee employee, Document document, Folder folder) {
        loginStepsMobile
                .loginAs(employee)
                .waitLoadMainPage(employee); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folder);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.validateThatInGrid().itemDisplayed(document.getDocumentType().getDocRegisterCardsName(), folder);
        // Переход в документ из грида
        gridOfFoldersSteps.openItemInGrid(document.getDocumentType().getDocRegisterCardsName(), folder);

        //----------------------------------------------------------------ФОРМА - Документ
        //Сохранить url для перехода по прямой ссылке
        urlDocInArm = getWebDriver().getCurrentUrl();
        // Ожидание и проверка кнопок тулбара
        resolutionStepsMobile.waitToolbarOfMenu();
        // Выполнение операций
        resolutionStepsMobile.approveProjectOfResolution();
        // Проверяем что проект утвержден
        internalStepsMobile.logout();
        loginStepsMobile
                .loginAs(employee)
                .waitLoadMainPage(employee); // Ожидание открытия главной страницы
        open(urlDocInArm);
        // Ожидание и проверка кнопок тулбара
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();
        resolutionStepsMobile.verifyThatNotHavePanelProjectOfResolution();
        // Выход из системы
        internalStepsMobile.logout();
    }


    @Test(dataProvider = "objectDataForCreateProjectsInWeb", dataProviderClass = TestsOfResolutionsMobile.class, dependsOnMethods = "verifyingApproveProjectResolutionFirstUser")
    public void createSecondProjectInWeb(Department[] departments, Employee[] employee,
                                         DocRegisterCards registerCards, Document document, Folder[] folders) {
        open(Configuration.baseUrl);
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице
        open(urlDocInWeb);
        editDocumentSteps.waitDocument();
        // Создание проекта
        editDocumentSteps.createProjectOfResolution(document.getResolutionOfDocument()[1]);
        refresh();
        editDocumentSteps.createProjectOfResolution(document.getResolutionOfDocument()[2]);
        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }


    @Test(dataProvider = "objectDataForVerifyingApproveSecondProjectResolution", dataProviderClass = TestsOfResolutionsMobile.class, dependsOnMethods = "createSecondProjectInWeb")
    public void verifyingApproveProjectResolutionSecondUser(Employee employee, Document document, Folder folder) {
        loginStepsMobile
                .loginAs(employee)
                .waitLoadMainPage(employee); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folder);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.validateThatInGrid().itemDisplayed(document.getDocumentType().getDocRegisterCardsName(), folder);
        // Переход в документ из грида
        gridOfFoldersSteps.openItemInGrid(document.getDocumentType().getDocRegisterCardsName(), folder);

        //----------------------------------------------------------------ФОРМА - Документ
        // Ожидание и проверка кнопок тулбара
        resolutionStepsMobile.waitToolbarOfMenu();
        // Выполнение операций
        resolutionStepsMobile.approveProjectOfResolution();
        // Проверяем что проект утвержден
        internalStepsMobile.logout();
        loginStepsMobile
                .loginAs(employee)
                .waitLoadMainPage(employee); // Ожидание открытия главной страницы
        open(urlDocInArm);
        // Ожидание и проверка кнопок тулбара
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();
        resolutionStepsMobile.verifyThatNotHavePanelProjectOfResolution();
        // Выход из системы
        internalStepsMobile.logout();
    }

    @Test(dataProvider = "objectDataForVerifyingApproveThirdProjectResolution", dataProviderClass = TestsOfResolutionsMobile.class, dependsOnMethods = "verifyingApproveProjectResolutionSecondUser")
    public void verifyingApproveProjectResolutionWithAnnotationOnFiles(Employee employee, Document document, Folder folder, AuthorOfAnnotation[] authorOfAnnotation) {
        loginStepsMobile
                .loginAs(employee)
                .waitLoadMainPage(employee); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folder);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.validateThatInGrid().itemDisplayed(document.getDocumentType().getDocRegisterCardsName(), folder);
        // Переход в документ из грида
        gridOfFoldersSteps.openItemInGrid(document.getDocumentType().getDocRegisterCardsName(), folder);

        //----------------------------------------------------------------ФОРМА - Документ
        // Ожидание и проверка кнопок тулбара
        resolutionStepsMobile.waitToolbarOfMenu();
        //Комментирование на файле
        annotationOnFilesSteps
                .addCommentOfPenOnPdfFileWithProjectOfResolution(authorOfAnnotation[0]);
        // Выполнение операций
        resolutionStepsMobile.approveProjectOfResolution();
        // Проверяем что проект утвержден
        internalStepsMobile.logout();
        loginStepsMobile
                .loginAs(employee)
                .waitLoadMainPage(employee); // Ожидание открытия главной страницы
        open(urlDocInArm);
        // Ожидание и проверка кнопок тулбара
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();
        resolutionStepsMobile.verifyThatNotHavePanelProjectOfResolution();
        // Выход из системы
        internalStepsMobile.logout();
    }

    @Test(dataProvider = "objectDataForVerifyingApproveFirstProjectResolution", dataProviderClass = TestsOfResolutionsMobile.class, dependsOnMethods = "verifyingApproveProjectResolutionWithAnnotationOnFiles")
    public void verifyingStatusApprovedProjects(Employee employee, Document document, Folder folder) {
        // Проверка в веб статусов утвержденных резолюции
        open(Configuration.baseUrl);
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице
        open(urlDocInWeb);
        editDocumentSteps.waitDocument();
        editDocumentSteps.resolutionsTab();
        editDocumentSteps.statusOfResolutionIsOnExecution(document.getResolutionOfDocument()[0]);
        editDocumentSteps.statusOfResolutionIsOnExecution(document.getResolutionOfDocument()[1]);
        editDocumentSteps.statusOfResolutionIsApproved(document.getResolutionOfDocument()[2]);
        // Выход
        internalPageSteps.logout();
    }

}
