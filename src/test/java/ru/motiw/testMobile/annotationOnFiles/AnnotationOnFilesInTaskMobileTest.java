package ru.motiw.testMobile.annotationOnFiles;

import com.codeborne.selenide.testng.ScreenShooter;
import com.codeborne.selenide.testng.TextReport;
import com.codeborne.selenide.testng.annotations.Report;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.motiw.data.dataproviders.AnnotationOnFilesInDocumentAndTaskMobile;
import ru.motiw.mobile.elements.FormElementsMobile;
import ru.motiw.mobile.model.AuthorOfAnnotation;
import ru.motiw.mobile.steps.AnnotationOnFilesSteps;
import ru.motiw.mobile.steps.Document.ResolutionStepsMobile;
import ru.motiw.mobile.steps.Folders.GridOfFoldersSteps;
import ru.motiw.mobile.steps.InternalStepsMobile;
import ru.motiw.mobile.steps.LoginStepsMobile;
import ru.motiw.mobile.steps.Tasks.CardTaskStepsMobile;
import ru.motiw.mobile.steps.Tasks.NewTaskStepsMobile;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Tasks.Folder;
import ru.motiw.web.model.Tasks.Task;
import ru.motiw.web.steps.Administration.Users.UsersSteps;
import ru.motiw.web.steps.Home.InternalSteps;
import ru.motiw.web.steps.Login.LoginStepsSteps;
import ru.motiw.web.steps.Tasks.UnionTasksSteps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.motiw.mobile.model.Task.TabsOfTask.FILES_TAB;
import static ru.motiw.web.steps.Administration.Users.DepartmentSteps.goToURLDepartments;
import static ru.motiw.web.steps.Tasks.UnionTasksSteps.goToUnionTasks;

@Listeners({ScreenShooter.class, TextReport.class})
@Report
public class AnnotationOnFilesInTaskMobileTest extends AnnotationOnFilesInDocumentAndTaskMobile {

    private LoginStepsSteps loginPageSteps;
    private LoginStepsMobile loginStepsMobile;
    private InternalStepsMobile internalStepsMobile;
    private GridOfFoldersSteps gridOfFoldersSteps;
    private InternalSteps internalPageSteps;
    private UnionTasksSteps unionTasksSteps;
    private UsersSteps userPageSteps;
    private AnnotationOnFilesSteps annotationOnFilesSteps;
    private FormElementsMobile formElementsMobile;
    private CardTaskStepsMobile cardTaskStepsMobile;
    private NewTaskStepsMobile newTaskStepsMobile;
    private ResolutionStepsMobile resolutionStepsMobile;

    @BeforeClass
    public void beforeTest() {
        loginPageSteps = page(LoginStepsSteps.class);
        loginStepsMobile = page(LoginStepsMobile.class);
        internalStepsMobile = page(InternalStepsMobile.class);
        gridOfFoldersSteps = page(GridOfFoldersSteps.class);
        internalPageSteps = page(InternalSteps.class);
        unionTasksSteps = page(UnionTasksSteps.class);
        userPageSteps = page(UsersSteps.class);
        annotationOnFilesSteps = page(AnnotationOnFilesSteps.class);
        formElementsMobile = page(FormElementsMobile.class);
        cardTaskStepsMobile = page(CardTaskStepsMobile.class);
        newTaskStepsMobile = page(NewTaskStepsMobile.class);
        resolutionStepsMobile = page(ResolutionStepsMobile.class);
    }

    /**
     * Подготовка данных в web и Арм
     *
     * @param departments
     * @param employee
     * @param task
     * @param folders
     */
    @Test(description = "Подготовка данных в web и Арм", dataProvider = "objectDataForPreconditionTestAnnotationInTask", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class)
    public void precondition(Department[] departments, Employee[] employee, Task task, Folder[] folders) {
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице
        //---------------------------------------------------------------------------------Пользователи и Подразделения
        // Инициализируем страницу и переходим на нее - Администрирование/Пользователи (Подразделения)
        goToURLDepartments().createDepartment(departments[0]);
        userPageSteps.createUser(employee[0].setDepartment(departments[0]));
        userPageSteps.createUser(employee[1].setDepartment(departments[0]));
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
        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
        clearBrowserCache(); //чистим кеш, т.к после логаута в вебе пользователь все равно остается залогинен (баг после работы в user/tab/user/uniontasks/)

        //-------------------------------------------------------------------------------Создать задачу в АРМ
        loginStepsMobile
                .loginAs(ADMIN)
                .waitLoadMainPage(ADMIN); // Ожидание открытия главной страницы
        //----------------------------------------------------------------ФОРМА - создания Задачи
        newTaskStepsMobile.goToCreateNewTask().creatingTask(task);
        newTaskStepsMobile.saveTask().verifyThatToastOfNewTaskAppear(); //Сохраняем задачу
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     * Проверка добавления граф.комментария под первым пользователем
     *
     * @param task
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка добавления граф.комментария под первым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnFileInTask", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "precondition")
    public void addAnnotationOnFirstUser(Task task, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        loginStepsMobile
                .loginAs(authorOfAnnotation[0].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[0].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(task.getTaskName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Задача
        cardTaskStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Переходим на кладку "Файлы"
        cardTaskStepsMobile.openTab(FILES_TAB);
        //Комментирование на файле
        annotationOnFilesSteps
                .addCommentOfPenOnPdfFile(authorOfAnnotation[0])
                .addCommentOfMarkerOnPdfFile(authorOfAnnotation[0])
                .validateThat().annotationPenAndMarkerOnPdfExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        // Переходим на вкладку "Файлы"
        cardTaskStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        cardTaskStepsMobile.openTab(FILES_TAB);
        annotationOnFilesSteps
                .validateThat()
                .annotationControlsToolbarForPdfAppears()
                .annotationTriggerForPdfAppears()
                .annotationPenAndMarkerOnPdfExist();
        // Выключение граф.комментария
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf().disableViewAnnotationByAuthorOnPdf(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationOnPdfNotExist();
        // Включение граф.комментария
        annotationOnFilesSteps.enableViewAnnotationByAuthorOnPdf(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationPenAndMarkerOnPdfExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf();
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     * Работаем с граф.комментарием первого пользователя под вторым пользователем
     *
     * @param task
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Работаем с граф.комментарием первого пользователя под вторым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnFileInTask", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "addAnnotationOnFirstUser")
    public void workWithForeignAnnotation(Task task, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        // ------------------ Проверка под Пользователем-Б
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(task.getTaskName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Задача
        cardTaskStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Переходим на вкладку "Файлы"
        cardTaskStepsMobile.openTab(FILES_TAB);
        // ------------------ Проверяем отображение граф.комментария Пользователя-А
        annotationOnFilesSteps.validateThat()
                .annotationTriggerForPdfAppears()
                .annotationPenAndMarkerOnPdfExist();
        // Выключение граф.комментария Пользователя-А
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf()
                .validateThat().triggerOfViewAnnotationByAuthorOnPdfActive(authorOfAnnotation[0].getEmployee());
        annotationOnFilesSteps.disableViewAnnotationByAuthorOnPdf(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationOnPdfNotExist();
        // Включение граф.комментария Пользователя-А
        annotationOnFilesSteps.enableViewAnnotationByAuthorOnPdf(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationPenAndMarkerOnPdfExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf();
        // Пробуем удалить граф.комментарий Пользователя-А
        annotationOnFilesSteps.eraseAnnotationOnPdfFile(authorOfAnnotation[0])
                .validateThat()
                .annotationControlsToolbarForPdfAppears()
                .annotationPenAndMarkerOnPdfExist();
        // Выход из системы
        internalStepsMobile.logout();
    }


    /**
     * Проверка добавления второго граф.комментария под вторым пользователем
     *
     * @param task
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка добавления второго граф.комментария под вторым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnFileInTask", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "workWithForeignAnnotation")
    public void addAnnotationOnSecondUser(Task task, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(task.getTaskName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Задача
        cardTaskStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Переходим на вкладку "Файлы"
        cardTaskStepsMobile.openTab(FILES_TAB);
        // Добавляем комментарий под Пользователем-Б
        annotationOnFilesSteps.validateThat().annotationControlsToolbarForPdfAppears();
        annotationOnFilesSteps.addCommentOfPenOnPdfFile(authorOfAnnotation[1])
                .validateThat().annotationsFirstAndSecondAuthorOnPdfExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        // Переходим на вкладку "Файлы"
        cardTaskStepsMobile.openTab(FILES_TAB);
        cardTaskStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        annotationOnFilesSteps.validateThat().annotationsFirstAndSecondAuthorOnPdfExist();
        // Выключение граф.комментария Пользователя-А, Б
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf().disableViewAnnotationByAuthorOnPdf(authorOfAnnotation[1].getEmployee())
                .validateThat().annotationPenAndMarkerOnPdfExist();
        annotationOnFilesSteps.disableViewAnnotationByAuthorOnPdf(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationOnPdfNotExist();
        // Включение граф.комментария Пользователя-А, Б
        annotationOnFilesSteps.enableViewAnnotationByAuthorOnPdf(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationPenAndMarkerOnPdfExist();
        annotationOnFilesSteps.enableViewAnnotationByAuthorOnPdf(authorOfAnnotation[1].getEmployee())
                .validateThat().annotationsFirstAndSecondAuthorOnPdfExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf();
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     *  Проверка удаления собственного граф.комментария
     *
     * @param task
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка удаления собственного граф.комментария", dataProvider = "objectDataForVerifyingAnnotationOnFileInTask", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "addAnnotationOnSecondUser")
    public void eraseOwnAnnotation(Task task, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(task.getTaskName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Задача
        cardTaskStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Переходим на вкладку "Файлы"
        cardTaskStepsMobile.openTab(FILES_TAB);
        // ------------------ Проверяем удаление граф.комментария Пользователя-Б и отображение граф.комментария Пользователя-А
        annotationOnFilesSteps.eraseAnnotationOnPdfFile(authorOfAnnotation[1])
                .validateThat().annotationPenAndMarkerOnPdfExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        // Переходим на вкладку "Файлы"
        cardTaskStepsMobile.openTab(FILES_TAB);
        cardTaskStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf()
                .validateThat()
                .triggerOfViewAnnotationByAuthorOnPdfNotExist(authorOfAnnotation[1].getEmployee()) // отсутствие кнопки включения/выключения отображения комментария Пользователя-Б
                .triggerOfViewAnnotationByAuthorOnPdfActive(authorOfAnnotation[0].getEmployee())
                .annotationPenAndMarkerOnPdfExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf();
        // Выход из системы
        internalStepsMobile.logout();
    }

}
