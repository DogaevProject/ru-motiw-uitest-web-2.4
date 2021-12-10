package ru.motiw.testsWeb.Document;

import com.codeborne.selenide.testng.TextReport;
import com.codeborne.selenide.testng.annotations.Report;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.motiw.data.dataproviders.ConsiderationDocflow;
import ru.motiw.data.listeners.ScreenShotOnFailListener;
import ru.motiw.web.elements.elementsweb.Documents.EditDocument.EditDocumentCartTabElements;
import ru.motiw.web.elements.elementsweb.Documents.EditDocument.EditDocumentRouteTabElements;
import ru.motiw.web.model.Administration.TasksTypes.TasksTypes;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Administration.Users.Status;
import ru.motiw.web.model.DocflowAdministration.DocumentRegistrationCards.DocRegisterCards;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Tasks.Folder;
import ru.motiw.web.model.Tasks.Task;
import ru.motiw.web.steps.Administration.TasksTypes.TaskTypesEditSteps;
import ru.motiw.web.steps.Administration.Users.UsersSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.*;
import ru.motiw.web.steps.Documents.EditDocument.EditDocumentSteps;
import ru.motiw.web.steps.Home.InternalSteps;
import ru.motiw.web.steps.Login.LoginStepsSteps;
import ru.motiw.web.steps.Tasks.TaskForm.UnionMessageSteps;
import ru.motiw.web.steps.Tasks.UnionTasksSteps;

import java.util.ArrayList;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertTrue;
import static ru.motiw.utils.WindowsUtil.newWindowForm;
import static ru.motiw.web.steps.Administration.TasksTypes.TaskTypesSteps.goToURLTaskTypes;
import static ru.motiw.web.steps.Administration.Users.DepartmentSteps.goToURLDepartments;
import static ru.motiw.web.steps.Documents.CreateDocument.NewDocumentSteps.goToURLNewDocument;
import static ru.motiw.web.steps.Tasks.UnionTasksSteps.goToUnionTasks;


@Listeners({ScreenShotOnFailListener.class, TextReport.class})
@Report
public class ConsiderationDocumentTest extends ConsiderationDocflow {

    private LoginStepsSteps loginPageSteps;
    private InternalSteps internalPageSteps;
    private UsersSteps userPageSteps;
    private FormDocRegisterCardsEditFieldsSteps formDocRegisterCardsEditFieldsSteps;
    private FormDocRegisterCardsEditConnectedRoutesSteps formDocRegisterCardsEditConnectedRoutesSteps;
    private FormDocRegisterCardsEditGeneralSteps formDocRegisterCardsEditGeneralSteps;
    private UnionTasksSteps unionTasksSteps;
    private UnionMessageSteps unionMessageSteps;
    private TaskTypesEditSteps formEditTaskTypes;
    private FormDocRegisterCardsEditTasksSteps formDocRegisterCardsEditTasksSteps;
    private EditDocumentSteps editDocumentSteps;
    private EditDocumentCartTabElements editDocumentCartTabElements;
    private EditDocumentRouteTabElements editDocumentRouteTabElements;


    @BeforeClass
    public void beforeTest() {
        loginPageSteps = page(LoginStepsSteps.class);
        internalPageSteps = page(InternalSteps.class);
        userPageSteps = page(UsersSteps.class);
        formDocRegisterCardsEditFieldsSteps = page(FormDocRegisterCardsEditFieldsSteps.class);
        formDocRegisterCardsEditConnectedRoutesSteps = page(FormDocRegisterCardsEditConnectedRoutesSteps.class);
        formDocRegisterCardsEditGeneralSteps = page(FormDocRegisterCardsEditGeneralSteps.class);
        unionTasksSteps = page(UnionTasksSteps.class);
        unionMessageSteps = page(UnionMessageSteps.class);
        formEditTaskTypes = page(TaskTypesEditSteps.class);
        formDocRegisterCardsEditTasksSteps = page(FormDocRegisterCardsEditTasksSteps.class);
        editDocumentSteps = page(EditDocumentSteps.class);
        editDocumentCartTabElements = page(EditDocumentCartTabElements.class);
        editDocumentRouteTabElements = page(EditDocumentRouteTabElements.class);
    }

    private String urlDocInWeb_1;


    @Test(dataProvider = "objectDataRCD", dataProviderClass = ConsiderationDocflow.class)
    public void preconditionInWeb(Department[] departments, Employee[] employee, TasksTypes tasksTypes,
                                  DocRegisterCards registerCards, Document document, Folder[] folders) throws Exception {
        loginPageSteps.loginAs(ADMIN);
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

        // ---------------- ТИПЫ ЗАДАЧ
        goToURLTaskTypes().addObjectTaskTypeList(tasksTypes.getObjectTypeName());

        formEditTaskTypes.addAllFieldsTaskTypeList(tasksTypes)
                .saveChangesOnObject(tasksTypes);

        //---------------------------------------------------------------------------------РКД
        // Переход в раздел Администрирование ДО/Регистрационные карточки документов
        FormDocRegisterCardsEditGeneralSteps editRCD = GridDocRegisterCardsSteps
                .goToURLGridDocRegisterCards().addDocRegisterCards(); // Добавить РКД

        /**
         * Устанавливаем настройки для РКД-1 (регистрационная карточка документа) на вкладке - МАРШРУТЫ
         */
        formDocRegisterCardsEditConnectedRoutesSteps.checkBoxUseAllPossibleRoutes(registerCards);

        formDocRegisterCardsEditGeneralSteps.addNameDocumentRegisterCards(registerCards);

        /*
         * Устанавливаем настройки для РКД (регистрационная карточка документа) на вкладке - ПОЛЯ
         */
        formDocRegisterCardsEditFieldsSteps.addFieldsDocRegisterCards(registerCards);

        /*
         * Устанавливаем настройки для РКД (регистрационная карточка документа) на вкладке - ЗАДАЧИ
         */
        formDocRegisterCardsEditTasksSteps
                .setCopyingFieldsWhenCreatingTask(registerCards)
                .setSettingsFieldsDocumentContaining(registerCards)
                .toSetTheTypeOfReviewTasks(registerCards, "Тип задачи по рассмотрению") // Тип задачи по рассмотрению документа
                .toSetTheTypeOfReviewTasks(registerCards, "Тип задачи по исполнению"); // Тип задачи по рассмотрению документа


        // Сохранение настроек РКД
        GridDocRegisterCardsSteps gridDocRegisterCards = editRCD.saveAllChangesInDoc(); // Сохранение всех изменений в РКД
        gridDocRegisterCards.verifyDocRegisterCards(registerCards.getDocRegisterCardsName());

        //-------------------------------------------------------------------------------Создать документ
        goToURLNewDocument().createDocument(document).openCreatedDoc();
        String parentWindowHandler = getWebDriver().getWindowHandle(); //Запоминаем окно в котором находимся
        ArrayList<String> tabs = new ArrayList<>(getWebDriver().getWindowHandles()); //все открытые окна сохраняем в список
        switchTo().window(tabs.get(1)); //переходим во вторую вкладку
        //Сохранить url для перехода по прямой ссылке
        urlDocInWeb_1 = getWebDriver().getCurrentUrl();
        getWebDriver().close();
        getWebDriver().switchTo().window(parentWindowHandler);

        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }


    @Test(dataProvider = "objectDataForConsideration", dataProviderClass = ConsiderationDocflow.class, dependsOnMethods = "preconditionInWeb")
    public void verifyConsideration_1(Employee[] employee, Document document, Task task, Folder[] folders) {
        loginPageSteps.loginAs(employee[0]);
        goToUnionTasks();
        unionTasksSteps.beforeAddFolder(21);
        unionTasksSteps.openAnExistingTaskInFolder(new Task().setTaskName(task.getTaskName()), folders[0]);

        String parentWindowHandler = getWebDriver().getWindowHandle();
        getWebDriver().switchTo().window(new WebDriverWait(getWebDriver(), 10).until(newWindowForm(By.xpath("//body[@id='unionmessage']//li//span[text()='Действия']"))));

        // проверка кнопок для согласования в задаче
        unionMessageSteps.verifyButtonsOfConsiderationIsVisible();
        // переход в документ
        unionMessageSteps.goToDocument();
        // проверка кнопок для согласования в документе
        editDocumentSteps.verifyButtonsOfConsiderationIsVisible();

        // выполнение операции
        sleep(2000); // todo только после ожидания срабатыает клик, что конкретно ждать не ясно
        editDocumentCartTabElements.getButtonConsideration().click();
        editDocumentSteps.addTextBeforeConsiderationAndOk("Согласовать");

        getWebDriver().switchTo().window(parentWindowHandler);  // окно задачи закрывается
        open(urlDocInWeb_1);
        editDocumentSteps.routesTab();
        editDocumentRouteTabElements.statusBlockDiagramIsReviewed("Произвольная рабочая группа", employee[0].getLastName()).shouldBe(visible);
        editDocumentSteps.verifyButtonsOfConsiderationIsNotVisible();

        // Проверяем что задачи нет в Папке
        goToUnionTasks();
        unionTasksSteps.verifyThatTaskNotExistingInTheFolder(new Task().setTaskName(task.getTaskName()), folders[0]);
        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }


    @Test(dataProvider = "objectDataForConsideration", dataProviderClass = ConsiderationDocflow.class, dependsOnMethods = "verifyConsideration_1")
    public void verifyConsideration_2(Employee[] employee, Document document, Task task, Folder[] folders) {
        loginPageSteps.loginAs(employee[1]);
        goToUnionTasks();
        unionTasksSteps.beforeAddFolder(21);
        unionTasksSteps.openAnExistingTaskInFolder(new Task().setTaskName(task.getTaskName()), folders[0]);

        String parentWindowHandler = getWebDriver().getWindowHandle();
        getWebDriver().switchTo().window(new WebDriverWait(getWebDriver(), 10).until(newWindowForm(By.xpath("//body[@id='unionmessage']//li//span[text()='Действия']"))));

        // проверка кнопок для согласования в задаче
        unionMessageSteps.verifyButtonsOfConsiderationIsVisible();
        // переход в документ
        unionMessageSteps.goToDocument();
        // проверка кнопок для согласования в документе
        editDocumentSteps.verifyButtonsOfConsiderationIsVisible();

        // выполнение операции
        sleep(2000); // todo только после ожидания срабатыает клик, что конкретно ждать не ясно
        editDocumentCartTabElements.getButtonBackToRevision().click();
        editDocumentSteps.addTextBeforeConsiderationAndOk("На доработку");

        getWebDriver().switchTo().window(parentWindowHandler);  // окно задачи закрывается
        open(urlDocInWeb_1);
        editDocumentSteps.routesTab();
        editDocumentRouteTabElements.statusBlockDiagramIsReviewed("Произвольная рабочая группа", employee[0].getLastName()).shouldBe(visible);
        editDocumentRouteTabElements.statusBlockDiagramIsBackToRevision("Произвольный начальник", employee[1].getLastName()).shouldBe(visible);
        editDocumentSteps.verifyButtonsOfConsiderationIsNotVisible();
        // Проверяем что задачи нет в Папке
        goToUnionTasks();
        unionTasksSteps.verifyThatTaskNotExistingInTheFolder(new Task().setTaskName(task.getTaskName()), folders[0]);
        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }


    @Test(dataProvider = "objectDataForConsideration", dataProviderClass = ConsiderationDocflow.class, dependsOnMethods = "verifyConsideration_2")
    public void verifyConsideration_3(Employee[] employee, Document document, Task task, Folder[] folders) {
        loginPageSteps.loginAs(ADMIN);
        open(urlDocInWeb_1);
        // выполнение операции
        editDocumentCartTabElements.getButtonBackToConsideration().click();
        editDocumentCartTabElements.getButtonOkInFormForAddTextBeforeConsideration().waitUntil(visible, 1000).click();
        editDocumentSteps.turnOnConsiderationForBlock("Произвольная рабочая группа");
        editDocumentCartTabElements.getButtonOkInFormForReturnToBeginning().click();
        $(By.xpath("//div[text()=\"Документ возвращен на рассмотрение\"]")).waitUntil(visible, 5000);

        editDocumentSteps.routesTab();
        editDocumentRouteTabElements.statusBlockDiagramIsOnReview("Произвольная рабочая группа", employee[0].getLastName()).shouldBe(visible);
        editDocumentRouteTabElements.statusBlockDiagramIsReviewed("Произвольный начальник", employee[1].getLastName()).shouldNotBe(visible);
        editDocumentSteps.verifyButtonsOfConsiderationIsNotVisible();

        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }

    @Test(dataProvider = "objectDataForConsideration", dataProviderClass = ConsiderationDocflow.class, dependsOnMethods = "verifyConsideration_3")
    public void verifyConsideration_4(Employee[] employee, Document document, Task task, Folder[] folders) {
        loginPageSteps.loginAs(employee[0]);
        goToUnionTasks();
        unionTasksSteps.beforeAddFolder(21);
        unionTasksSteps.openAnExistingTaskInFolder(new Task().setTaskName(task.getTaskName()), folders[0]);

        String parentWindowHandler = getWebDriver().getWindowHandle();
        getWebDriver().switchTo().window(new WebDriverWait(getWebDriver(), 10).until(newWindowForm(By.xpath("//body[@id='unionmessage']//li//span[text()='Действия']"))));

        // проверка кнопок для согласования в задаче
        unionMessageSteps.verifyButtonsOfConsiderationIsVisible();
        // переход в документ
        unionMessageSteps.goToDocument();
        // проверка кнопок для согласования в документе
        editDocumentSteps.verifyButtonsOfConsiderationIsVisible();

        // выполнение операции
        sleep(2000); // todo только после ожидания срабатыает клик, что конкретно ждать не ясно
        editDocumentCartTabElements.getButtonConsiderationWithComment().click();
        editDocumentSteps.addTextBeforeConsiderationAndOk("Согласовать с замечаниями");

        getWebDriver().switchTo().window(parentWindowHandler);  // окно задачи закрывается
        open(urlDocInWeb_1);
        editDocumentSteps.routesTab();
        editDocumentRouteTabElements.statusBlockDiagramIsReviewedWithComment("Произвольная рабочая группа", employee[0].getLastName()).shouldBe(visible);
        editDocumentSteps.verifyButtonsOfConsiderationIsNotVisible();
        // Проверяем что задачи нет в Папке
        goToUnionTasks();
        unionTasksSteps.verifyThatTaskNotExistingInTheFolder(new Task().setTaskName(task.getTaskName()), folders[0]);
        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }

    @Test(dataProvider = "objectDataForConsideration", dataProviderClass = ConsiderationDocflow.class, dependsOnMethods = "verifyConsideration_4")
    public void verifyConsideration_5(Employee[] employee, Document document, Task task, Folder[] folders) {
        loginPageSteps.loginAs(employee[1]);
        goToUnionTasks();
        unionTasksSteps.beforeAddFolder(21);
        unionTasksSteps.openAnExistingTaskInFolder(new Task().setTaskName(task.getTaskName()), folders[0]);

        String parentWindowHandler = getWebDriver().getWindowHandle();
        getWebDriver().switchTo().window(new WebDriverWait(getWebDriver(), 10).until(newWindowForm(By.xpath("//body[@id='unionmessage']//li//span[text()='Действия']"))));

        // проверка кнопок для согласования в задаче
        unionMessageSteps.verifyButtonsOfConsiderationIsVisible();
        // переход в документ
        unionMessageSteps.goToDocument();
        // проверка кнопок для согласования в документе
        editDocumentSteps.verifyButtonsOfConsiderationIsVisible();

        // выполнение операции
        sleep(2000); // todo только после ожидания срабатыает клик, что конкретно ждать не ясно
        editDocumentCartTabElements.getButtonDenialReview().click();
        editDocumentSteps.addTextBeforeConsiderationAndOk("Отказаться согласовать");

        getWebDriver().switchTo().window(parentWindowHandler);  // окно задачи закрывается
        open(urlDocInWeb_1);
        editDocumentSteps.routesTab();
        editDocumentRouteTabElements.statusBlockDiagramIsReviewedWithComment("Произвольная рабочая группа", employee[0].getLastName()).shouldBe(visible);
        editDocumentRouteTabElements.statusBlockDiagramIsDenialReview("Произвольный начальник", employee[1].getLastName()).shouldBe(visible);
        editDocumentSteps.verifyButtonsOfConsiderationIsNotVisible();
        // Проверяем что задачи нет в Папке
        goToUnionTasks();
        unionTasksSteps.verifyThatTaskNotExistingInTheFolder(new Task().setTaskName(task.getTaskName()), folders[0]);
        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }

}
