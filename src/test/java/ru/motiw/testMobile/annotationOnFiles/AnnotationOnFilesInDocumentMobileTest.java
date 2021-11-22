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
import ru.motiw.mobile.steps.ValidationStepsMobile.ValidateFilesStepsMobile;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.DocflowAdministration.DocumentRegistrationCards.DocRegisterCards;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Tasks.Folder;
import ru.motiw.web.steps.Administration.Users.UsersSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.FormDocRegisterCardsEditConnectedRoutesSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.FormDocRegisterCardsEditGeneralSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.GridDocRegisterCardsSteps;
import ru.motiw.web.steps.Home.InternalSteps;
import ru.motiw.web.steps.Login.LoginStepsSteps;
import ru.motiw.web.steps.Tasks.UnionTasksSteps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.motiw.mobile.model.Document.TypeOfLocation.GRID_FOLDER;
import static ru.motiw.web.steps.Administration.Users.DepartmentSteps.goToURLDepartments;
import static ru.motiw.web.steps.Documents.CreateDocument.NewDocumentSteps.goToURLNewDocument;
import static ru.motiw.web.steps.Tasks.UnionTasksSteps.goToUnionTasks;

@Listeners({ScreenShooter.class, TextReport.class})
@Report
public class AnnotationOnFilesInDocumentMobileTest extends AnnotationOnFilesInDocumentAndTaskMobile {

    private LoginStepsSteps loginPageSteps;
    private LoginStepsMobile loginStepsMobile;
    private InternalStepsMobile internalStepsMobile;
    private GridOfFoldersSteps gridOfFoldersSteps;
    private InternalSteps internalPageSteps;
    private UnionTasksSteps unionTasksSteps;
    private UsersSteps userPageSteps;
    private FormDocRegisterCardsEditConnectedRoutesSteps formDocRegisterCardsEditConnectedRoutesSteps;
    private FormDocRegisterCardsEditGeneralSteps formDocRegisterCardsEditGeneralSteps;
    private AnnotationOnFilesSteps annotationOnFilesSteps;
    private FormElementsMobile formElementsMobile;
    private ResolutionStepsMobile resolutionStepsMobile;
    private ValidateFilesStepsMobile validateFilesStepsMobile;

    @BeforeClass
    public void beforeTest() {
        loginPageSteps = page(LoginStepsSteps.class);
        loginStepsMobile = page(LoginStepsMobile.class);
        internalStepsMobile = page(InternalStepsMobile.class);
        gridOfFoldersSteps = page(GridOfFoldersSteps.class);
        internalPageSteps = page(InternalSteps.class);
        unionTasksSteps = page(UnionTasksSteps.class);
        userPageSteps = page(UsersSteps.class);
        formDocRegisterCardsEditConnectedRoutesSteps = page(FormDocRegisterCardsEditConnectedRoutesSteps.class);
        formDocRegisterCardsEditGeneralSteps = page(FormDocRegisterCardsEditGeneralSteps.class);
        annotationOnFilesSteps = page(AnnotationOnFilesSteps.class);
        formElementsMobile = page(FormElementsMobile.class);
        resolutionStepsMobile = page(ResolutionStepsMobile.class);
        validateFilesStepsMobile = page(ValidateFilesStepsMobile.class);
    }

    /**
     * Подготовка данных в web-интерфейсе и Арм
     */
    @Test(description = "Подготовка данных в web и Арм", dataProvider = "objectDataForPreconditionTestAnnotationInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class)
    public void precondition(Department[] departments, Employee[] employee,
                             DocRegisterCards[] registerCards, Document documentWithPdf, Document documentWithImg, Document documentWithPdfAndImg, Folder[] folders) {
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
        //---------------------------------------------------------------------------------РКД-1
        // Переход в раздел Администрирование ДО/Регистрационные карточки документов
        FormDocRegisterCardsEditGeneralSteps editRCD = GridDocRegisterCardsSteps
                .goToURLGridDocRegisterCards().addDocRegisterCards(); // Добавить РКД

        /**
         * Устанавливаем настройки для РКД (регистрационная карточка документа) на вкладке - МАРШРУТЫ
         */
        formDocRegisterCardsEditConnectedRoutesSteps.checkBoxUseAllPossibleRoutes(registerCards[0]);

        formDocRegisterCardsEditGeneralSteps.addNameDocumentRegisterCards(registerCards[0]);

        // Сохранение настроек РКД
        GridDocRegisterCardsSteps gridDocRegisterCards = editRCD.saveAllChangesInDoc(); // Сохранение всех изменений в РКД
        gridDocRegisterCards.verifyDocRegisterCards(registerCards[0].getDocRegisterCardsName());
        //---------------------------------------------------------------------------------РКД-2
        // Переход в раздел Администрирование ДО/Регистрационные карточки документов
        FormDocRegisterCardsEditGeneralSteps editRCD2 = GridDocRegisterCardsSteps
                .goToURLGridDocRegisterCards().addDocRegisterCards(); // Добавить РКД

        /**
         * Устанавливаем настройки для РКД (регистрационная карточка документа) на вкладке - МАРШРУТЫ
         */
        formDocRegisterCardsEditConnectedRoutesSteps.checkBoxUseAllPossibleRoutes(registerCards[1]);

        formDocRegisterCardsEditGeneralSteps.addNameDocumentRegisterCards(registerCards[1]);

        // Сохранение настроек РКД
        GridDocRegisterCardsSteps gridDocRegisterCards2 = editRCD2.saveAllChangesInDoc(); // Сохранение всех изменений в РКД
        gridDocRegisterCards2.verifyDocRegisterCards(registerCards[1].getDocRegisterCardsName());

        //---------------------------------------------------------------------------------РКД-3
        // Переход в раздел Администрирование ДО/Регистрационные карточки документов
        FormDocRegisterCardsEditGeneralSteps editRCD3 = GridDocRegisterCardsSteps
                .goToURLGridDocRegisterCards().addDocRegisterCards(); // Добавить РКД

        /**
         * Устанавливаем настройки для РКД (регистрационная карточка документа) на вкладке - МАРШРУТЫ
         */
        formDocRegisterCardsEditConnectedRoutesSteps.checkBoxUseAllPossibleRoutes(registerCards[2]);

        formDocRegisterCardsEditGeneralSteps.addNameDocumentRegisterCards(registerCards[2]);

        // Сохранение настроек РКД
        GridDocRegisterCardsSteps gridDocRegisterCards3 = editRCD3.saveAllChangesInDoc(); // Сохранение всех изменений в РКД
        gridDocRegisterCards3.verifyDocRegisterCards(registerCards[2].getDocRegisterCardsName());

        //-------------------------------------------------------------------------------Создать документ
        goToURLNewDocument().createDocument(documentWithPdf);
        goToURLNewDocument().createDocument(documentWithImg);
        goToURLNewDocument().createDocument(documentWithPdfAndImg);
        // Выход
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
        clearBrowserCache(); //чистим кеш, т.к после логаута в вебе пользователь все равно остается залогинен (баг после работы в user/tab/user/uniontasks/)

        //-------------------------------------------------------------------------------Создать резолюцию в АРМ
        loginStepsMobile
                .loginAs(ADMIN)
                .waitLoadMainPage(ADMIN); // Ожидание открытия главной страницы
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openFolder(folders[0]);
        resolutionStepsMobile.createAllResolutionsInDocument(documentWithPdf, folders[0], GRID_FOLDER);
        resolutionStepsMobile.createAllResolutionsInDocument(documentWithImg, folders[0], GRID_FOLDER);
        resolutionStepsMobile.createAllResolutionsInDocument(documentWithPdfAndImg, folders[0], GRID_FOLDER);
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     * Проверка добавления граф.комментария на Pdf под первым пользователем
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка добавления граф.комментария на Pdf под первым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnPdfFileInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "precondition")
    public void addAnnotationOnPdfFromFirstUser(Document documentWithPdf, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        loginStepsMobile
                .loginAs(authorOfAnnotation[0].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[0].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithPdf.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        //Комментирование на файле
        annotationOnFilesSteps
                .addCommentOfPenOnPdfFile(authorOfAnnotation[0])
                .addCommentOfMarkerOnPdfFile(authorOfAnnotation[0])
                .validateThat().annotationPenAndMarkerOnPdfExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
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
     * Проверка добавления граф.комментария на png-файле под первым пользователем
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка добавления граф.комментария на png-файле под первым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnPngFileInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "precondition")
    public void addAnnotationOnImageFromFirstUser(Document documentWithImg, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        loginStepsMobile
                .loginAs(authorOfAnnotation[0].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[0].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithImg.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        //Комментирование на файле
        annotationOnFilesSteps
                .addCommentOfPenOnFile(authorOfAnnotation[0])
                .addCommentOfMarkerOnFile(authorOfAnnotation[0])
                .validateThat().annotationPenAndMarkerExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        annotationOnFilesSteps
                .validateThat()
                .annotationControlsToolbarAppears()
                .annotationTriggerAppears()
                .annotationPenAndMarkerExist();
        // Выключение граф.комментария
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations().disableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationNotExist();
        // Включение граф.комментария
        annotationOnFilesSteps.enableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationPenAndMarkerExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations();
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     * Проверка добавления граф.комментария на двух файлах (Pdf и Png) одновременно под первым пользователем
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка добавления граф.комментария на двух файлах (Pdf и Png) одновременно под первым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnPdfAndPngFilesInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "precondition")
    public void addAnnotationOnTwoFilesFromFirstUser(Document documentWithPdfAndPng, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        loginStepsMobile
                .loginAs(authorOfAnnotation[0].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[0].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithPdfAndPng.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // ------------------  Комментирование на png-файле
        annotationOnFilesSteps
                .addCommentOfPenOnFile(authorOfAnnotation[0])
                .addCommentOfMarkerOnFile(authorOfAnnotation[0])
                .validateThat().annotationPenAndMarkerExist();
        // ------------------  Комментирование на pdf-файле
        validateFilesStepsMobile.switchToNextFile(1, 2);
        annotationOnFilesSteps
                .addCommentOfPenOnPdfFile(authorOfAnnotation[0])
                .addCommentOfMarkerOnPdfFile(authorOfAnnotation[0])
                .validateThat().annotationPenAndMarkerOnPdfExist();
        // ------------------  Проверяем после перехода между файлами через кнопку "Вперед"
        validateFilesStepsMobile.switchToNextFile(2, 2);
        // Проверяем png-файл
        annotationOnFilesSteps
                .validateThat()
                .annotationControlsToolbarAppears()
                .annotationPenAndMarkerExist();
        // Проверяем pdf-файл
        validateFilesStepsMobile.switchToNextFile(1, 2);
        annotationOnFilesSteps
                .validateThat()
                .annotationControlsToolbarForPdfAppears()
                .annotationPenAndMarkerOnPdfExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Проверяем png-файл
        annotationOnFilesSteps
                .validateThat()
                .annotationControlsToolbarAppears()
                .annotationTriggerAppears()
                .annotationPenAndMarkerExist();
        // Выключение граф.комментария
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations().disableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationNotExist();
        // Включение граф.комментария
        annotationOnFilesSteps.enableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationPenAndMarkerExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations();
        // Проверяем pdf-файл
        validateFilesStepsMobile.switchToNextFile(1, 2);
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
     * Работаем с граф.комментарием на Pdf первого пользователя под вторым пользователем
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Работаем с граф.комментарием на Pdf первого пользователя под вторым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnPdfFileInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "addAnnotationOnPdfFromFirstUser")
    public void workWithForeignAnnotationOnPdf(Document documentWithPdf, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        // ------------------ Проверка под Пользователем-Б
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithPdf.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
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
                .annotationPenAndMarkerOnPdfExist();
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     * Работаем с граф.комментарием на png-файле первого пользователя под вторым пользователем
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Работаем с граф.комментарием на png-файле первого пользователя под вторым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnPngFileInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "addAnnotationOnImageFromFirstUser")
    public void workWithForeignAnnotationOnImage(Document documentWithImg, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        // ------------------ Проверка под Пользователем-Б
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithImg.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // ------------------ Проверяем отображение граф.комментария Пользователя-А
        annotationOnFilesSteps.validateThat()
                .annotationTriggerAppears()
                .annotationPenAndMarkerExist();
        // Выключение граф.комментария Пользователя-А
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations()
                .validateThat().triggerOfViewAnnotationByAuthorActive(authorOfAnnotation[0].getEmployee());
        annotationOnFilesSteps.disableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationNotExist();
        // Включение граф.комментария Пользователя-А
        annotationOnFilesSteps.enableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationPenAndMarkerExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations();
        // Пробуем удалить граф.комментарий Пользователя-А
        annotationOnFilesSteps.eraseAnnotationOnFile(authorOfAnnotation[0])
                .validateThat()
                .annotationPenAndMarkerExist();
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     * Работаем с граф.комментарием на двух файлах (Pdf и Png) первого пользователя под вторым пользователем
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Работаем с граф.комментарием на двух файлах (Pdf и Png) первого пользователя под вторым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnPdfAndPngFilesInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "addAnnotationOnTwoFilesFromFirstUser")
    public void workWithForeignAnnotationOnTwoFiles(Document documentWithPdfAndPng, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        // ------------------ Проверка под Пользователем-Б
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithPdfAndPng.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // ------------------ Проверяем работу с граф.комментарием на png Пользователя-А
        annotationOnFilesSteps.validateThat()
                .annotationTriggerAppears()
                .annotationPenAndMarkerExist();
        // Выключение граф.комментария Пользователя-А
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations()
                .validateThat().triggerOfViewAnnotationByAuthorActive(authorOfAnnotation[0].getEmployee());
        annotationOnFilesSteps.disableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationNotExist();
        // Включение граф.комментария Пользователя-А
        annotationOnFilesSteps.enableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationPenAndMarkerExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations();
        // Пробуем удалить граф.комментарий Пользователя-А
        annotationOnFilesSteps.eraseAnnotationOnFile(authorOfAnnotation[0])
                .validateThat()
                .annotationPenAndMarkerExist();
        // ------------------ Проверяем работу с граф.комментарием на pdf Пользователя-А
        validateFilesStepsMobile.switchToNextFile(1, 2);
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
                .annotationPenAndMarkerOnPdfExist();
        // Выход из системы
        internalStepsMobile.logout();
    }


    /**
     * Проверка добавления второго граф.комментария на Pdf под вторым пользователем
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка добавления второго граф.комментария на Pdf под вторым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnPdfFileInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "workWithForeignAnnotationOnPdf")
    public void addAnnotationOnPdfFromSecondUser(Document documentWithPdf, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithPdf.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Добавляем комментарий под Пользователем-Б
        annotationOnFilesSteps.validateThat().annotationControlsToolbarForPdfAppears();
        annotationOnFilesSteps.addCommentOfPenOnPdfFile(authorOfAnnotation[1])
                .validateThat().annotationsFirstAndSecondAuthorOnPdfExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
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
     * Проверка добавления второго граф.комментария на png-файле под вторым пользователем
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка добавления второго граф.комментария на png-файле под вторым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnPngFileInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "workWithForeignAnnotationOnImage")
    public void addAnnotationOnImageFromSecondUser(Document documentWithImg, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithImg.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Добавляем комментарий под Пользователем-Б
        annotationOnFilesSteps.validateThat().annotationControlsToolbarAppears();
        annotationOnFilesSteps.addCommentOfPenOnFile(authorOfAnnotation[1])
                .validateThat().annotationsFirstAndSecondAuthorExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        annotationOnFilesSteps.validateThat().annotationsFirstAndSecondAuthorExist();
        // Выключение граф.комментария Пользователя-А, Б
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations().disableViewAnnotationByAuthor(authorOfAnnotation[1].getEmployee())
                .validateThat().annotationPenAndMarkerExist();
        annotationOnFilesSteps.disableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationNotExist();
        // Включение граф.комментария Пользователя-А, Б
        annotationOnFilesSteps.enableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationPenAndMarkerExist();
        annotationOnFilesSteps.enableViewAnnotationByAuthor(authorOfAnnotation[1].getEmployee())
                .validateThat().annotationsFirstAndSecondAuthorExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations();
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     * Проверка добавления второго граф.комментария на двух файлах (Pdf и Png) под вторым пользователем
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка добавления второго граф.комментария на двух файлах (Pdf и Png) под вторым пользователем", dataProvider = "objectDataForVerifyingAnnotationOnPdfAndPngFilesInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "workWithForeignAnnotationOnTwoFiles")
    public void addAnnotationOnTwoFilesFromSecondUser(Document documentWithPdfAndPng, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithPdfAndPng.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Добавляем комментарий на png под Пользователем-Б
        annotationOnFilesSteps.validateThat().annotationControlsToolbarAppears();
        annotationOnFilesSteps.addCommentOfPenOnFile(authorOfAnnotation[1])
                .validateThat().annotationsFirstAndSecondAuthorExist();

        // Добавляем комментарий на pdf под Пользователем-Б
        validateFilesStepsMobile.switchToNextFile(1, 2);
        annotationOnFilesSteps.validateThat().annotationControlsToolbarForPdfAppears();
        annotationOnFilesSteps.addCommentOfPenOnPdfFile(authorOfAnnotation[1])
                .validateThat().annotationsFirstAndSecondAuthorOnPdfExist();

        // ------------------  Проверяем после перехода между файлами через кнопку "Вперед"
        validateFilesStepsMobile.switchToNextFile(2, 2);
        // Проверяем png-файл
        annotationOnFilesSteps
                .validateThat()
                .annotationControlsToolbarAppears()
                .annotationsFirstAndSecondAuthorExist();
        // Проверяем pdf-файл
        validateFilesStepsMobile.switchToNextFile(1, 2);
        annotationOnFilesSteps
                .validateThat()
                .annotationControlsToolbarForPdfAppears()
                .annotationsFirstAndSecondAuthorOnPdfExist();

        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Проверяем png-файл
        annotationOnFilesSteps.validateThat().annotationsFirstAndSecondAuthorExist();
        // Выключение граф.комментария Пользователя-А, Б
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations().disableViewAnnotationByAuthor(authorOfAnnotation[1].getEmployee())
                .validateThat().annotationPenAndMarkerExist();
        annotationOnFilesSteps.disableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationNotExist();
        // Включение граф.комментария Пользователя-А, Б
        annotationOnFilesSteps.enableViewAnnotationByAuthor(authorOfAnnotation[0].getEmployee())
                .validateThat().annotationPenAndMarkerExist();
        annotationOnFilesSteps.enableViewAnnotationByAuthor(authorOfAnnotation[1].getEmployee())
                .validateThat().annotationsFirstAndSecondAuthorExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations();
        // Проверяем pdf-файл
        validateFilesStepsMobile.switchToNextFile(1, 2);
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
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
     * Проверка удаления собственного граф.комментария на Pdf
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка удаления собственного граф.комментария на Pdf", dataProvider = "objectDataForVerifyingAnnotationOnPdfFileInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "addAnnotationOnPdfFromSecondUser")
    public void eraseOwnAnnotationOnPdf(Document documentWithPdf, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithPdf.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // ------------------ Проверяем удаление граф.комментария Пользователя-Б и отображение граф.комментария Пользователя-А
        annotationOnFilesSteps.eraseAnnotationOnPdfFile(authorOfAnnotation[1])
                .validateThat().annotationPenAndMarkerOnPdfExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf()
                .validateThat()
                .triggerOfViewAnnotationByAuthorOnPdfNotExist(authorOfAnnotation[1].getEmployee()) // отсутствие кнопки включения/выключения отображения комментария Пользователя-Б
                .triggerOfViewAnnotationByAuthorOnPdfActive(authorOfAnnotation[0].getEmployee())
                .annotationPenAndMarkerOnPdfExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf();
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     * Проверка удаления собственного граф.комментария на png-файле
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка удаления собственного граф.комментария на png-файле", dataProvider = "objectDataForVerifyingAnnotationOnPngFileInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "addAnnotationOnImageFromSecondUser")
    public void eraseOwnAnnotationOnImage(Document documentWithImg, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithImg.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // ------------------ Проверяем удаление граф.комментария Пользователя-Б и отображение граф.комментария Пользователя-А
        annotationOnFilesSteps.eraseAnnotationOnFile(authorOfAnnotation[1])
                .validateThat().annotationPenAndMarkerExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations()
                .validateThat()
                .triggerOfViewAnnotationByAuthorNotExist(authorOfAnnotation[1].getEmployee()) // отсутствие кнопки включения/выключения отображения комментария Пользователя-Б
                .triggerOfViewAnnotationByAuthorActive(authorOfAnnotation[0].getEmployee())
                .annotationPenAndMarkerExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations();
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     * Проверка удаления собственного граф.комментария на двух файлах (Pdf и Png)
     * Удаляем на двух файлах одновременно без перезагрузки страницы.
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка удаления собственного граф.комментария на двух файлах (Pdf и Png)", dataProvider = "objectDataForVerifyingAnnotationOnPdfAndPngFilesInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "addAnnotationOnTwoFilesFromSecondUser")
    public void eraseOwnAnnotationOnTwoFilesAllAtOnce(Document documentWithPdfAndPng, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        loginStepsMobile
                .loginAs(authorOfAnnotation[1].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[1].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithPdfAndPng.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // ------------------ Проверяем удаление граф.комментария на png Пользователя-Б и отображение граф.комментария Пользователя-А
        annotationOnFilesSteps.eraseAnnotationOnFile(authorOfAnnotation[1])
                .validateThat().annotationPenAndMarkerExist();
        // ------------------  Проверяем комментарии на png-файле после перехода между файлами через кнопку "Вперед"
        validateFilesStepsMobile.switchToNextFile(1, 2);
        validateFilesStepsMobile.switchToNextFile(2, 2);
        annotationOnFilesSteps
                .validateThat().annotationPenAndMarkerExist();
        // ------------------ Проверяем удаление граф.комментария на pdf Пользователя-Б и отображение граф.комментария Пользователя-А
        validateFilesStepsMobile.switchToNextFile(1, 2);
        annotationOnFilesSteps.eraseAnnotationOnPdfFile(authorOfAnnotation[1])
                .validateThat().annotationPenAndMarkerOnPdfExist();
        // ------------------  Проверяем после перехода между файлами через кнопку "Вперед"
        validateFilesStepsMobile.switchToNextFile(2, 2);
        validateFilesStepsMobile.switchToNextFile(1, 2);
        annotationOnFilesSteps
                .validateThat().annotationPenAndMarkerOnPdfExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Проверяем png-файл
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations()
                .validateThat()
                .triggerOfViewAnnotationByAuthorNotExist(authorOfAnnotation[1].getEmployee()) // отсутствие кнопки включения/выключения отображения комментария Пользователя-Б
                .triggerOfViewAnnotationByAuthorActive(authorOfAnnotation[0].getEmployee())
                .annotationPenAndMarkerExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotations();
        // Проверяем pdf-файл
        validateFilesStepsMobile.switchToNextFile(1, 2);
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf()
                .validateThat()
                .triggerOfViewAnnotationByAuthorOnPdfNotExist(authorOfAnnotation[1].getEmployee())
                .triggerOfViewAnnotationByAuthorOnPdfActive(authorOfAnnotation[0].getEmployee())
                .annotationPenAndMarkerOnPdfExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf();
        // Выход из системы
        internalStepsMobile.logout();
    }

    /**
     * Проверка удаления собственного граф.комментария на двух файлах (Pdf и Png)
     * Удаляем на двух файлах поочередно, с перезагрузкой страницы после удаления на комментария на каждом файле
     *
     * @param folders
     * @param authorOfAnnotation
     * @throws Exception
     */
    @Test(description = "Проверка удаления собственного граф.комментария на двух файлах (Pdf и Png)", dataProvider = "objectDataForVerifyingAnnotationOnPdfAndPngFilesInDocument", dataProviderClass = AnnotationOnFilesInDocumentAndTaskMobile.class, dependsOnMethods = "eraseOwnAnnotationOnTwoFilesAllAtOnce")
    public void eraseOwnAnnotationOnTwoFilesOneAtATime(Document documentWithPdfAndPng, Folder[] folders, AuthorOfAnnotation[] authorOfAnnotation) throws Exception {
        internalStepsMobile.goToAuthorizationPage();
        loginStepsMobile
                .loginAs(authorOfAnnotation[0].getEmployee())
                .waitLoadMainPage(authorOfAnnotation[0].getEmployee()); // Ожидание открытия главной страницы
        gridOfFoldersSteps.openFolder(folders[0]);
        //----------------------------------------------------------------ГРИД - Папка
        gridOfFoldersSteps.openItemInGrid(documentWithPdfAndPng.getDocumentType().getDocRegisterCardsName(), folders[0]);
        //----------------------------------------------------------------ФОРМА - Документ
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // ------------------ Проверяем удаление граф.комментария на png Пользователя-А
        annotationOnFilesSteps.eraseAnnotationOnFile(authorOfAnnotation[0])
                .validateThat().annotationNotExist();
        // ------------------  Проверяем комментарии на png-файле после перехода между файлами через кнопку "Вперед"
        validateFilesStepsMobile.switchToNextFile(1, 2);
        validateFilesStepsMobile.switchToNextFile(2, 2);
        annotationOnFilesSteps
                .validateThat().annotationNotExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Проверяем png-файл
        annotationOnFilesSteps.validateThat().annotationTriggerNotExit();
        // Проверяем pdf-файл
        validateFilesStepsMobile.switchToNextFile(1, 2);
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf()
                .validateThat()
                .triggerOfViewAnnotationByAuthorOnPdfNotExist(authorOfAnnotation[1].getEmployee())
                .triggerOfViewAnnotationByAuthorOnPdfActive(authorOfAnnotation[0].getEmployee())
                .annotationPenAndMarkerOnPdfExist();
        annotationOnFilesSteps.clickButtonOfListOfAuthorsAnnotationsOnPdf();
        // ------------------ Проверяем удаление граф.комментария на pdf Пользователя-А
        annotationOnFilesSteps.eraseAnnotationOnPdfFile(authorOfAnnotation[0])
                .validateThat().annotationOnPdfNotExist();
        // ------------------  Проверяем после перехода между файлами через кнопку "Вперед"
        validateFilesStepsMobile.switchToNextFile(2, 2);
        validateFilesStepsMobile.switchToNextFile(1, 2);
        annotationOnFilesSteps
                .validateThat().annotationOnPdfNotExist();
        // ------------------  Проверяем после перезагрузки страницы
        refresh();
        resolutionStepsMobile.waitToolbarOfMenu();  // Ожидание тулбара
        // Проверяем pdf-файл
        validateFilesStepsMobile.switchToNextFile(1, 2);
        annotationOnFilesSteps
                .validateThat()
                .annotationTriggerForPdfNotExit()
                .annotationOnPdfNotExist();
        // Выход из системы
        internalStepsMobile.logout();
    }
}