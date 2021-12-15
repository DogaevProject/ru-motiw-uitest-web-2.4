package ru.motiw.testsWeb.Tasks;

import com.codeborne.selenide.testng.TextReport;
import com.codeborne.selenide.testng.annotations.Report;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.motiw.data.dataproviders.Tasks;
import ru.motiw.data.listeners.ScreenShotOnFailListener;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Tasks.Folder;
import ru.motiw.web.model.Tasks.Task;
import ru.motiw.web.steps.Administration.Users.UsersSteps;
import ru.motiw.web.steps.Home.InternalSteps;
import ru.motiw.web.steps.Login.LoginStepsSteps;
import ru.motiw.web.steps.Tasks.TaskForm.UnionMessageSteps;
import ru.motiw.web.steps.Tasks.UnionTasksSteps;

import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertTrue;
import static ru.motiw.utils.WindowsUtil.newWindowForm;
import static ru.motiw.web.steps.Administration.Users.DepartmentSteps.goToURLDepartments;
import static ru.motiw.web.steps.Tasks.TaskForm.UnionMessageNewSteps.goToURLUnionMessageNew;
import static ru.motiw.web.steps.Tasks.UnionTasksSteps.goToUnionTasks;

@Listeners({ScreenShotOnFailListener.class, TextReport.class})
@Report
public class CreateTaskTest extends Tasks {

    private LoginStepsSteps loginPageSteps;
    private InternalSteps internalPageSteps;
    private UsersSteps userPageSteps;
    private UnionMessageSteps unionMessageSteps;
    private UnionTasksSteps unionTasksSteps;

    @BeforeClass
    public void beforeTest() {
        loginPageSteps = page(LoginStepsSteps.class);
        internalPageSteps = page(InternalSteps.class);
        userPageSteps = page(UsersSteps.class);
        unionMessageSteps = page(UnionMessageSteps.class);
        unionTasksSteps = page(UnionTasksSteps.class);
    }

    // Папка
    Folder[] folder = getRandomArrayFolders();

    /**
     * Проверка создания обычной задачи
     *
     * @param department    атрибуты пользователя
     * @param author        атрибуты пользователя Автор
     * @param resppers      атрибуты пользователя Ответственный руководитель
     * @param controller    атрибуты пользователя Контролер
     * @param worker        атрибуты пользователя Исполнитель
     * @param IWGWorker     атрибуты пользователя Исполнитель задачи ИРГ
     * @param IWGResppers   атрибуты пользователя ОР задачи ИРГ
     * @param IWGСontroller атрибуты пользователя
     * @param task          атрибуты - значения задачи
     */
    @Test(priority = 1, dataProvider = "objectDataTask", dataProviderClass = Tasks.class)
    public void verifyCreateTask(Department department, Employee[] author, Employee[] resppers, Employee[] controller, Employee[] worker,
                                 Employee[] IWGWorker, Employee[] IWGResppers, Employee[] IWGСontroller, Task task) {
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице

        //---------------------------------------------------------------- Задачи/Задачи
        goToUnionTasks();
        unionTasksSteps.beforeAddFolder(21);
        // Добавляем Папки(/у)
        unionTasksSteps.addFolders(new Folder[]{folder[0].setNameFolder("wD_Smart_Box " + randomString(4))
                .setUseFilter(true).setFilterField("Начало").setChooseRelativeValue(true)
                .setSharedFolder(false).setAddSharedFolderForAll(false).setAddSharedFolderForNewUsers(false)});

        // Создаем подразделения для пользователей
        goToURLDepartments().beforeCreationDepartmentsAndUsers();
        userPageSteps.createDepartment(department);


        userPageSteps.createUser(author[0].setDepartment(department))
                // Ответственные руководители задачи
                .createUser(resppers[0].setDepartment(department))
                // Контролеры задачи
                .createUser(controller[0].setDepartment(department))
                // Исполнители задачи
                .createUser(worker[0].setDepartment(department));
        //TODO Создание задачи по типу отличном от типа Обычный

        // Инициализация и переход на страницу - Задачи/Создать задачу
        goToURLUnionMessageNew().creatingTask(task).saveTask();

        /*
         Проверяем отображение созданной задачи в гриде.
         Инициализация и переход на страницу - Задачи/Создать задачу
          */
        goToUnionTasks();
        unionTasksSteps.openAnExistingTaskInFolder(task, folder[0]);

        unionMessageSteps.verifyTask(task);

        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }

    /**
     * Проверка редактирования обычной задачи
     *
     * @param task   атрибуты - значения задачи
     */
    @Test(priority = 2, dataProvider = "objectDataTask_2", dataProviderClass = Tasks.class)
    public void verifyEditTask(Task task, Task editTask) {
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице
        // Инициализация и переход на страницу - Задачи/Создать задачу
        goToURLUnionMessageNew().creationOfATaskCheckpoints(task);

        goToUnionTasks();
        unionTasksSteps.openAnExistingTaskInFolder(task, folder[0]);

        String parentWindowHandler = getWebDriver().getWindowHandle();
        getWebDriver().switchTo().window(new WebDriverWait(getWebDriver(), 10).until(newWindowForm(By.xpath("//body[@id='unionmessage']//li//span[text()='Действия']"))));
        unionMessageSteps.verifyEditTask(editTask).clickSaveTask();
        getWebDriver().close();
        getWebDriver().switchTo().window(parentWindowHandler);  // Switch back to parent window

        goToUnionTasks();
        unionTasksSteps.openAnExistingTaskInFolder(editTask, folder[0]);
        unionMessageSteps.verifyTask(editTask);

        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }


    /**
     * Проверяем создание задачи типа ИРГ
     *
     * @param department            подразделение, в к-е будет добавлятсья пользователь
     * @param author                Авторы задачи
     * @param responsibleLeaders    Ответственные руководители задачи
     * @param controller            Контролеры задачи
     * @param worker                Исполнители задачи
     * @param IWGWorker             Исполнители задачи ИРГ
     * @param IWGResponsibleLeaders ОР задачи ИРГ
     * @param IWGController         Контролеры задачи
     * @param task                  Задача со всеми её параметрами
     */
    @Test(priority = 3, dataProvider = "objectDataTask")
    public void verifyCreateIWGTask(Department department, Employee[] author, Employee[] responsibleLeaders, Employee[] controller, Employee[] worker,
                                    Employee[] IWGWorker, Employee[] IWGResponsibleLeaders, Employee[] IWGController, Task task) {
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице
        assertTrue(loginPageSteps.isLoggedIn());

        // Создаем подразделения для пользователей
        goToURLDepartments().beforeCreationDepartmentsAndUsers();
        userPageSteps.createDepartment(department);

        userPageSteps.createUser(responsibleLeaders[0].setDepartment(department))
                // Исполнители задачи ИРГ
                .createUser(IWGWorker[0].setDepartment(department))
                // Ответственные руководители задачи ИРГ
                .createUser(IWGResponsibleLeaders[0].setDepartment(department))
                // Контролеры задачи ИРГ
                .createUser(IWGController[0].setDepartment(department));

        // Инициализация и переход на страницу - Задачи/Создать задачу
        goToURLUnionMessageNew().creatingTaskWithTheTaskOfIWG(task);

        /*
         Проверяем отображение созданной задачи в гриде.
         Инициализация и переход на страницу - Задачи/Создать задачу
          */
        goToUnionTasks();
        unionTasksSteps.openAnExistingTaskInFolder(task, folder[0]);

        unionMessageSteps.verifyTask(task);

        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }

    @Test(priority = 4, dataProvider = "objectDataTask")
    public void checkTheCreationOfATaskCheckpoints(Department department, Employee[] author, Employee[] resppers, Employee[] controller, Employee[] worker,
                                                   Employee[] IWGWorker, Employee[] IWGResppers, Employee[] IWGСontroller, Task task) {
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице
        assertTrue(loginPageSteps.isLoggedIn());

        // Инициализация и переход на страницу - Задачи/Создать задачу
        goToURLUnionMessageNew().creationOfATaskCheckpoints(task);

        /*
         Проверяем отображение созданной задачи в гриде.
         Инициализация и переход на страницу - Задачи/Создать задачу
          */
        goToUnionTasks();
        unionTasksSteps.openExistingTaskInTheFolderThroughTheSearch(task, folder[0]);

        unionMessageSteps.verifyTask(task);

        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }


}

