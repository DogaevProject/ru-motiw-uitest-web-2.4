package ru.motiw.testsWeb.Document;

import com.codeborne.selenide.testng.TextReport;
import com.codeborne.selenide.testng.annotations.Report;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.motiw.data.dataproviders.DocflowAdministration;
import ru.motiw.data.listeners.ScreenShotOnFailListener;
import ru.motiw.web.elements.elementsweb.Documents.EditDocument.EditDocumentCartTabElements;
import ru.motiw.web.model.Administration.Directories.Directories;
import ru.motiw.web.model.Administration.TasksTypes.TasksTypes;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.DocflowAdministration.DictionaryEditor.DictionaryEditor;
import ru.motiw.web.model.DocflowAdministration.DocumentRegistrationCards.DocRegisterCards;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.steps.Administration.Directories.DirectoriesEditSteps;
import ru.motiw.web.steps.Administration.Directories.DirectoriesSteps;
import ru.motiw.web.steps.Administration.Users.UsersSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.FormDocRegisterCardsEditConnectedRoutesSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.FormDocRegisterCardsEditFieldsSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.FormDocRegisterCardsEditGeneralSteps;
import ru.motiw.web.steps.DocflowAdministration.DocumentRegistrationCards.GridDocRegisterCardsSteps;
import ru.motiw.web.steps.Documents.CreateDocument.NewDocumentSteps;
import ru.motiw.web.steps.Home.InternalSteps;
import ru.motiw.web.steps.Login.LoginStepsSteps;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertTrue;
import static ru.motiw.web.steps.Administration.Users.DepartmentSteps.goToURLDepartments;
import static ru.motiw.web.steps.DocflowAdministration.DictionaryEditorSteps.goToURLDictionaryEditor;
import static ru.motiw.web.steps.Documents.CreateDocument.NewDocumentSteps.goToURLNewDocument;


@Listeners({ScreenShotOnFailListener.class, TextReport.class})
@Report
public class CreateDocumentTest extends DocflowAdministration {

    private LoginStepsSteps loginPageSteps;
    private InternalSteps internalPageSteps;
    private UsersSteps userPageSteps;
    private FormDocRegisterCardsEditFieldsSteps formDocRegisterCardsEditFieldsSteps;
    private FormDocRegisterCardsEditConnectedRoutesSteps formDocRegisterCardsEditConnectedRoutesSteps;
    private FormDocRegisterCardsEditGeneralSteps formDocRegisterCardsEditGeneralSteps;
    private NewDocumentSteps newDocumentSteps;
    private EditDocumentCartTabElements editDocumentCartTabElements;


    @BeforeClass
    public void beforeTest() {
        loginPageSteps = page(LoginStepsSteps.class);
        internalPageSteps = page(InternalSteps.class);
        userPageSteps = page(UsersSteps.class);
        formDocRegisterCardsEditFieldsSteps = page(FormDocRegisterCardsEditFieldsSteps.class);
        formDocRegisterCardsEditConnectedRoutesSteps = page(FormDocRegisterCardsEditConnectedRoutesSteps.class);
        formDocRegisterCardsEditGeneralSteps = page(FormDocRegisterCardsEditGeneralSteps.class);
        newDocumentSteps = page(NewDocumentSteps.class);
        editDocumentCartTabElements = page(EditDocumentCartTabElements.class);
    }

    private String urlDoc;

    @Test(priority = 1, dataProvider = "objectDataRCD", dataProviderClass = DocflowAdministration.class)
    public void verificationDocumentCreation(Department[] departments, Employee[] employee, Directories directories, TasksTypes tasksTypes, DictionaryEditor dictionaryEditor,
                                             DocRegisterCards registerCards, Document document) {
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице
        assertTrue(loginPageSteps.isLoggedIn());

        //---------------------------------------------------------------------------------Пользователи и Подразделения
        // Инициализируем страницу и переходим на нее - Администрирование/Пользователи (Подразделения)
        goToURLDepartments().createDepartment(departments[0]);

        userPageSteps.createUser(employee[0].setDepartment(departments[0]))
                .createUser(employee[1].setDepartment(departments[0]))
                .createUser(employee[2].setDepartment(departments[0]));

        //---------------------------------------------------------------------------------Словарь

        // Переход в раздел Администрирование ДО/Редактор словарей
        goToURLDictionaryEditor()
                // Добавляем элементы словаря
                .addDictionaryEditor(dictionaryEditor);

        //---------------------------------------------------------------------------------Справочник
        // Переход в раздел Администрирование/Справочники
        DirectoriesSteps.goToURLDirectories().addObjectTaskTypeList(directories.getObjectTypeName());

        DirectoriesEditSteps directoriesEditSteps = new DirectoriesEditSteps().directoriesEditPage();

        assertTrue("check display the form directories editing", directoriesEditSteps.loadedDirectoriesEditPage());

        // Добавляем поля и сохранеяем объект
        directoriesEditSteps.addAllFieldsTaskTypeList(directories).saveChangesOnObject(directories);

        //---------------------------------------------------------------------------------РКД
        // Переход в раздел Администрирование ДО/Регистрационные карточки документов
        FormDocRegisterCardsEditGeneralSteps editRCD = GridDocRegisterCardsSteps
                .goToURLGridDocRegisterCards().addDocRegisterCards(); // Добавить РКД

        /**
         * Добавляем поля для РКД (регистрационная карточка документа) на вкладке - ПОЛЯ
         */
        formDocRegisterCardsEditFieldsSteps.addFieldsDocRegisterCards(registerCards);

        /**
         * Устанавливаем настройки для РКД (регистрационная карточка документа) на вкладке - МАРШРУТЫ
         */
        formDocRegisterCardsEditConnectedRoutesSteps.checkBoxUseAllPossibleRoutes(registerCards);

        formDocRegisterCardsEditGeneralSteps.addNameDocumentRegisterCards(registerCards);

        // Сохранение настроек РКД
        GridDocRegisterCardsSteps gridDocRegisterCards = editRCD.saveAllChangesInDoc(); // Сохранение всех изменений в РКД
        gridDocRegisterCards.verifyDocRegisterCards(registerCards.getDocRegisterCardsName());

        //-------------------------------------------------------------------------------Создать документ
        goToURLNewDocument().createDocument(document).openCreatedDoc();
        String parentWindowHandler = getWebDriver().getWindowHandle(); //Запоминаем окно в котором находимся
        ArrayList<String> tabs = new ArrayList<>(getWebDriver().getWindowHandles()); //все открытые окна сохраняем в список
        switchTo().window(tabs.get(1)); //переходим во вторую вкладку
        //Сохранить url для перехода по прямой ссылке
        urlDoc = getWebDriver().getCurrentUrl();
        newDocumentSteps.verifyValueCustomFieldsDocument(document.getDocumentFields());
        getWebDriver().close();
        getWebDriver().switchTo().window(parentWindowHandler);
    }

    @Test(dataProvider = "objectDataEditDocument", dataProviderClass = DocflowAdministration.class, dependsOnMethods = "verificationDocumentCreation")
    public void verificationEditDocument(Document document) {
        open(urlDoc);
        // Редактирование документа
        newDocumentSteps.fillCustomFieldsDocument(document.getDocumentFields());
        editDocumentCartTabElements.geSaveButton().click();
        open(urlDoc);
        newDocumentSteps.verifyValueCustomFieldsDocument(document.getDocumentFields());
        // Выход из системы
        internalPageSteps.logout();
        // Проверка - пользователь разлогинен
        assertTrue(loginPageSteps.isNotLoggedIn());
    }

}
