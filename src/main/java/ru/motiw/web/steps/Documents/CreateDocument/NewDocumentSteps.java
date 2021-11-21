package ru.motiw.web.steps.Documents.CreateDocument;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.motiw.web.elements.elementsweb.Documents.CreateDocument.NewDocumentCartTabElements;
import ru.motiw.web.elements.elementsweb.Documents.CreateDocument.NewDocumentRouteTabElements;
import ru.motiw.web.elements.elementsweb.Tasks.TaskForm.ProjectFormElements;
import ru.motiw.web.elements.elementsweb.Tasks.TaskForm.UsersSelectTheFormElements;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.DocflowAdministration.DictionaryEditor.DictionaryEditorField;
import ru.motiw.web.model.DocflowAdministration.DocumentRegistrationCards.*;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Tasks.Project;
import ru.motiw.web.steps.BaseSteps;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.testng.AssertJUnit.fail;
import static ru.motiw.utils.WindowsUtil.newWindowForm;
import static ru.motiw.web.model.URLMenu.CREATE_DOCUMENT;

/**
 * Создать документ
 */
public class NewDocumentSteps extends BaseSteps {

    private ProjectFormElements projectFormElements = page(ProjectFormElements.class);
    private NewDocumentCartTabElements newDocumentCartTabElements = page(NewDocumentCartTabElements.class);
    private NewDocumentRouteTabElements routeTableGridElements = page(NewDocumentRouteTabElements.class);
    private UsersSelectTheFormElements usersSelectTheFormElements = page(UsersSelectTheFormElements.class);


    /**
     * Документы/Создать документ
     *
     * @return NewDocumentSteps
     */
    public static NewDocumentSteps goToURLNewDocument() {
        openSectionOnURL(CREATE_DOCUMENT.getMenuURL());
        return page(NewDocumentSteps.class);
    }

    /**
     * выбор поля - Тип документа
     *
     * @param typeNameDoc передаваемое название Типа документа - выбор в форме Создания документа
     */
    private NewDocumentSteps selFieldDocumentType(DocRegisterCards typeNameDoc) {
        if (typeNameDoc == null) {
            return this;
        } else {
            sleep(1000);
            $(newDocumentCartTabElements.getFieldDocumentType()).shouldBe(visible);
            newDocumentCartTabElements.getFieldDocumentType().click();
            $(By.xpath("//div[@id='treePanel']//li//a/*[text()]")).shouldBe(visible);
            $(By.xpath("//div[@id='treePanel']//li//a/*[text()='" + typeNameDoc.getDocRegisterCardsName() + "']")).click();
        }
        return this;
    }

    /**
     * Заполняем поле - Дата регистрации
     *
     * @param dateRegistration передаваемое значение Дата регистрации - используется для заполнения
     */
    private NewDocumentSteps writeInRegistrationDate(String dateRegistration) {
        if (dateRegistration == null) {
            return this;
        } else {
            waitForFormNewDocumentMask();
            sleep(1000);
            newDocumentCartTabElements.getFieldRegistrationDate().click();
            newDocumentCartTabElements.getInputField().setValue(dateRegistration);
        }
        return this;
    }

    /**
     * Создание нового проекта для документа
     */
    private NewDocumentSteps createProject(Project project) {
        if (project == null) {
            return this;
        } else {
            newDocumentCartTabElements.getNewProject().click();
            getFrameObject($(projectFormElements.getProjectFrame()));
            // выбор поля Проект
            sleep(500);
            projectFormElements.getProjectField().click();
            // заполняем поле Проект (Название проекта)
            projectFormElements.getEditorFieldProject().setValue(project.getNameProject());
            // выбор поля Описание
            projectFormElements.getProjectDescription().click();
            // заполняем поле Описание проекта
            projectFormElements.getEditorDescriptionProject().setValue(project.getDescription());
            projectFormElements.getProjectEnd().click(); //клик в другое поле для перевода фокуса с поля Описание
            projectFormElements.getProjectClient().click();
            projectFormElements.getEditorFieldProject().setValue(project.getСlient());
            projectFormElements.getProjectEnd().click();
            projectFormElements.getEditorFieldProject().setValue(project.getEndDate());
            projectFormElements.getProjectSave().click();
            waitForProjectMask();
            switchTo().defaultContent();
            switchTo().frame($(By.cssSelector("#flow")));
        }
        return this;
    }

    /**
     * Ожидание маски проекта
     */
    private NewDocumentSteps waitForProjectMask() {
        $(By.xpath("//*[contains (@class, 'x-mask x-mask-fixed')]")).waitUntil(disappear, 8000);
        return this;
    }

    /**
     * Ожидание исчезновения маски из DOM в форме создания документа
     */
    private NewDocumentSteps waitForFormNewDocumentMask() {
        $(By.xpath("//div[contains(@class,'ext-el-mask-msg x-mask-loading') or @class='ext-el-mask' and not(@style='display: none;')]")).waitUntil(disappear, 8000);
        return this;
    }

    /**
     * Ввод значения в поле типа "Текст" (Форма документа)
     */
    private NewDocumentSteps selEditText(String nameField, String text) {
        if (text == null) {
            return this;
        } else {
            $(By.xpath("//table//tr/td[1]/div[contains(text(),'" + nameField + "')]/../../td[2]/div/../../td[3]//img")).click();
            getFrameObject($(By.xpath("//*[@class='cke_wysiwyg_frame cke_reset']"))); // Фрейм CKE (расширенный текстовый редактор)
            newDocumentCartTabElements.getCkeBody().setValue(text);
            switchTo().defaultContent();
            switchTo().frame($(By.cssSelector("#flow")));
            newDocumentCartTabElements.getButtonSaveDescription().click();
        }
        return this;
    }

    /**
     * Ввод значения в поле типа "Словарь"
     */
    private NewDocumentSteps selValueDictionary(String nameField, DictionaryEditorField valueDictionary) {
        if (valueDictionary == null) {
            return this;
        } else {
            $(By.xpath("//table//tr/td[1]/div[contains(text(),'" + nameField + "')]/../../td[2]/div")).click();
            newDocumentCartTabElements.getInputField().click();
            $(By.xpath("//div[contains(@class,'x-combo-list')]//*[contains(text(),'" + valueDictionary
                    .getDictionaryEditorElement() + "')][ancestor::div[contains(@style,'visibility: visible')]]")).shouldBe(visible);
            $(By.xpath("//div[contains(@class,'x-combo-list')]//*[contains(text(),'" + valueDictionary
                    .getDictionaryEditorElement() + "')][ancestor::div[contains(@style,'visibility: visible')]]")).click();
        }
        return this;
    }

    /**
     * Общий метод заполнения полей документа
     *
     * @param nameField имя поля документа для заполнения
     * @param valueLine передаваемое значение для заполнения
     */
    private NewDocumentSteps enterValueInFieldDocument(String nameField, String valueLine) {
        if (valueLine == null) {
            return this;
        } else {
            $(By.xpath("//table//tr/td[1]/div[contains(text(),'" + nameField + "')]/../../td[2]/div")).click();
            try {
                $(By.xpath
                        ("//input[contains(@id,'ext-comp')][ancestor::div[contains(@style,'visibility: visible')]]")).shouldBe(visible);
                newDocumentCartTabElements.getInputField().setValue(valueLine);
            } catch (ElementNotFound e) {
                newDocumentCartTabElements.getInput2Field().setValue(valueLine);
            } finally {
                sleep(200);
            }
        }
        return this;
    }

    /**
     * Выбор значения в поле типа "Подразделение"
     */
    private NewDocumentSteps selEditDepartment(String nameField, Department[] department) {
        if (department == null) {
            return this;
        } else {
            for (Department departments : department) {
                $(By.xpath("//table//tr/td[1]/div[contains(text(),'" + nameField + "')]/../../td[2]/div/../../td[3]//img")).click();
                String parentWindowHandler = getWebDriver().getWindowHandle(); // Store your parent window
                switchTo().window(new WebDriverWait(getWebDriver(), 10).until(newWindowForm(By.xpath("//div[@class=\"searchWrapper searchFieldWrapper\"]/input"))));
                newDocumentCartTabElements.getSearchFieldDepartment().setValue(departments.getDepartmentName());
                newDocumentCartTabElements.getSearchFieldDepartment().pressEnter();
                newDocumentCartTabElements.getSelectedCheckBox().click();
                newDocumentCartTabElements.getButtonSave().click();
                getWebDriver().switchTo().window(parentWindowHandler);  // Switch back to parent window
                switchTo().frame($(By.cssSelector("#flow")));
            }
        }
        return this;
    }

    /**
     * Добавление Сотрудник через livesearch - поиск по Фамилии
     *
     * @param nameStr  заполняемое полей - передаваемое поле для заполнения
     * @param employee кол-во передаваемых пользователей
     */
    private NewDocumentSteps selLiveSearchEmployee(String nameStr, Employee[] employee) {
        if (employee == null) {
            return this;
        } else {
            for (Employee employees : employee) {
                $(By.xpath("//table//tr/td[1]/div[contains(text(),'" + nameStr + "')]/../../td[2]/div")).click();
                newDocumentCartTabElements.getInput3Field().setValue(employees.getLastName());
                $(By.xpath("//div[contains(@class,'x-combo-list')]//*[contains(text(),'" + employees
                        .getLastName() + "')][ancestor::div[contains(@style,'visibility: visible')]]")).shouldBe(visible);
                $(By.xpath("//div[contains(@class,'x-combo-list')]//*[contains(text(),'" + employees
                        .getLastName() + "')][ancestor::div[contains(@style,'visibility: visible')]]")).click();
            }
            return this;
        }
    }

    /**
     * Аттачминг файлов в форме
     *
     * @param nameOfFiles названия файлов
     */
    public NewDocumentSteps addAttachFiles(String[] nameOfFiles) {
        if (nameOfFiles != null)
            for (String nameOfFile : nameOfFiles) {

                String mainFilePath = "src" + File.separator + "main" + File.separator +
                        "resources" + File.separator + "attachfiles" + File.separator;

                try {
                    Robot r = new Robot(); //создаем робота для взаимодействия с win-формой
                    newDocumentCartTabElements.getAddFileButton().click();
                    sleep(1000);

                    //закрываем win-форму добавления файла
                    r.keyPress(KeyEvent.VK_ESCAPE);
                    r.keyRelease(KeyEvent.VK_ESCAPE);
                    //TODO ПОЧЕМУ-то не закрывает форму. Может повторно вызывать закрытие после добавления файла?
                } catch (AWTException e) {
                    fail("AWTException");
                }
                newDocumentCartTabElements.getAddFileInput().uploadFile(new File(mainFilePath, nameOfFile));
            }
        return this;
    }

    /**
     * Добавление файлов в форме
     * Возвращение с вкладки "Файлы" на Вкладку "Карточка документа"
     *
     * @param nameOfFiles названия файлов
     */
    public NewDocumentSteps addFiles(String[] nameOfFiles) {
        if (nameOfFiles != null) {
            addAttachFiles(nameOfFiles);
            documentCardTab(); // Возвращаемся с вкладки "Файлы" на Вкладку "Карточка документа"
        }
        return this;
    }

    /**
     * Заполняем пользовательские поля документа
     *
     * @param customField зн-ия полей для заполнения
     */
    public NewDocumentSteps fillCustomFieldsDocument(FieldDocument[] customField) {
        if (customField == null) {
            return this;
        } else
            for (FieldDocument customsField : customField) {
                // 1. ЧИСЛО
                if (customsField.getObjectFieldDocument() instanceof FieldTypeNumberDocument) {
                    enterValueInFieldDocument(customsField.getDocumentFieldName(), customsField.getValueField());
                    // 2. ДАТА
                } else if (customsField.getObjectFieldDocument() instanceof FieldTypeDateDocument) {
                    enterValueInFieldDocument(customsField.getDocumentFieldName(), customsField.getValueField());
                    // 3. СТРОКА
                } else if (customsField.getObjectFieldDocument() instanceof FieldTypeStringDocument && customsField.getUniqueField()) {
                    enterValueInFieldDocument(customsField.getDocumentFieldName(), customsField.getValueField());
                    // 4. ТЕКСТ
                } else if (customsField.getObjectFieldDocument() instanceof FieldTypeTextDocument) {
                    selEditText(customsField.getDocumentFieldName(), customsField.getValueField());
                    // 5. СЛОВАРЬ
                } else if (customsField.getObjectFieldDocument() instanceof FieldTypeDictionaryDocument) {
                    selValueDictionary(customsField.getDocumentFieldName(), customsField.getValueDictionaryEditor());
                    // 6. ПОДРАЗДЕЛЕНИЕ
                } else if (customsField.getObjectFieldDocument() instanceof FieldTypeDepartmentDocument) {
                    selEditDepartment(customsField.getDocumentFieldName(), customsField.getValueDepartment());
                    // 7. СОТРУДНИК
                } else if (customsField.getObjectFieldDocument() instanceof FieldTypeEmployeeDocument) {
                    selLiveSearchEmployee(customsField.getDocumentFieldName(), customsField.getValueEmployee());
                    // 8. ДОКУМЕНТ
                } else if (customsField.getObjectFieldDocument() instanceof FieldTypeDocumentDocument) {
                    // TODO input field Document
                    // 9. НУМЕРАТОР
                } else if (customsField.getObjectFieldDocument() instanceof FieldTypeNumeratorDocument) {
                    // 10. СПРАВОЧНИК
                } else if (customsField.getObjectFieldDocument() instanceof FieldTypeDirectoryDocument) {
                    // TODO input field Directory

                    // TODO добавляем проверку - заполнение значения поля документа
                }
            }
        return this;
    }

    /**
     * Выбираем вкладку - Карточка документа
     */
    private NewDocumentSteps documentCardTab() {
        newDocumentCartTabElements.getDocumentCartTab().click();
        newDocumentCartTabElements.getFieldDocumentType().shouldBe(visible);
        return this;
    }

    /**
     * Выбираем вкладку - Маршруты
     */
    private NewDocumentSteps routeTab() {
        routeTableGridElements.getRouteTab().click();
        $(By.cssSelector("#cb_route_name")).shouldBe(visible);
        return this;
    }

    /**
     * Выбор маршрутной схемы документа (Порядок рассмотрения) по имени
     */
    private NewDocumentSteps routeSelectionByName(String routeScheme) {
        if (routeScheme == null) {
            return this;
        } else {
            routeTableGridElements.getListOfRoutes().click();
            $(By.xpath("//div[contains(@class,'x-combo-list')][ancestor::div[contains(@style,'visibility: visible')]]")).shouldBe(visible);
            $(By.xpath("//div[contains(@class,'x-combo-list')]/*[text()='" + routeScheme + "'][ancestor::div[contains(@style,'visibility: visible')]]")).click();

            getFrameObject($(routeTableGridElements.getFrameRoute())); // уходим во фрейм Маршруты

            $$(By.xpath("//div[contains(@id,'headercontainer')]//span")).shouldHave(size(8));

//            $$(By.xpath("//div[contains(@id,'headercontainer')]//span")).shouldHave(texts("ФИО", "Действие", "", "Подразделение",
//                    "Должность", "Длительность рассмотрения", "Посылать напоминание за", "Обязательно рассматривают")); // todo Раньше по умолчанию ещё была включена колонка "Настройки". Теперь вместо неё проверяем пустой элемент (без текста) - "", - и иногда тут тест падает
        }
        return this;
    }

    /**
     * Сохранить и создать новый документ
     */
    private NewDocumentSteps saveAndCreateNewDocument() {
        switchTo().defaultContent();
        switchTo().frame($(By.cssSelector("#flow")));
        $(By.xpath("//div[@class='x-column-inner']/table[2]")).shouldBe(visible);
        newDocumentCartTabElements.getSaveAndCreateNewDocument().click();
        return this;
    }

    /**
     * Проверяем отображение надписи - Зарегистрировано, документ находится на рассмотрении - после сохранения документа
     */
    private NewDocumentSteps assertVerifyCreateDoc() {
        $(By.xpath("//a[@class='error_message' and @style='text-decoration:none']")).shouldBe(visible);
        return this;
    }


    /**
     * Создать документ
     *
     * @param document атрибуты (значения) документа для заполнения в форме Создания документа
     */
    public NewDocumentSteps createDocument(Document document) {
        selFieldDocumentType(document.getDocumentType()) // выбираем проинициализированный Тип документа
                .addFiles(document.getValueFiles()) // Добавление файлов
                .writeInRegistrationDate(document.getDateRegistration()) // Дата регистрации
                .fillCustomFieldsDocument(document.getDocumentFields()) // заполнение пользовательских полей документа
                .createProject(document.getProject()) // добавляем новый проект
                .routeTab() // Выбор вкладки - Маршруты
                .routeSelectionByName(document.getRouteSchemeForDocument().getNameRouteScheme());

        toAddaUserToRoutingScheme(routeTableGridElements.getAddAUserToBlockDiagram(), document.getRouteSchemeForDocument().getUserRoute(),
                document.getRouteSchemeForDocument().getReviewDuration());

        saveAndCreateNewDocument() // Сохранить и создать новый документ
                .assertVerifyCreateDoc(); // Проверяем создание документа
        return this;
    }

    /**
     * Добавить пользователя в роль через форму выбора пользователей
     *
     * @param addAUserToARole элемент для выбора пользователя (кнопка - Добавить пользователя)
     */
    private void toAddaUserToRoutingScheme(SelenideElement addAUserToARole,
                                           Employee[] userRoutes, String reviewDuration) {
        for (Employee userRoute : userRoutes) {
            addAUserToARole.click(); // Добавить пользователя в маршрут
            // Window PopUp
            String parentWindowHandler = getWebDriver().getWindowHandle(); // Store your parent window
            switchTo().window(new WebDriverWait(getWebDriver(), 10)
                    .until(newWindowForm(By.cssSelector("#serverSearch"))));
            usersSelectTheFormElements.getUserSearchField().setValue(userRoute.getLastName());
            usersSelectTheFormElements.getUserSearchField().pressEnter();
            if (usersSelectTheFormElements.getUserSaveButton().exists()) {
                usersSelectTheFormElements.getAddTheUserInTheFormOfChoice(userRoute).click(); // Добавить пользователя
                usersSelectTheFormElements.getUserSaveButton().click(); // Сохранить выбранных пользователей
            } else
                usersSelectTheFormElements.getAddTheUserInTheFormOfChoice(userRoute).click(); // Добавить пользователя
            switchTo().window(parentWindowHandler);  // Switch back to parent window
            switchTo().frame($(By.cssSelector("#flow")));
            getFrameObject($(routeTableGridElements.getFrameRoute())); // уходим во фрейм Маршруты
            // Проверяем выбранного пользователя в гриде для конкретного блока
            $(By.xpath("//table//td//span[contains(text(),'1.')]/../../../../tr[2]//a[text()='"
                    + userRoute.getLastName() + " " + userRoute.getName() + " "
                    + userRoute.getPatronymic() + "']")).waitUntil(visible, 10000);
        }

        routeTableGridElements.getReviewDuration().click();
        routeTableGridElements.getInputReviewDuration().setValue(reviewDuration);


    }


    /**
     * Переходим в созданный документ по ссылке - Зарегистрировано, документ находится на рассмотрении - после сохранения документа
     */
    public NewDocumentSteps openCreatedDoc() {
        $(By.xpath("//a[@class='error_message' and @style='text-decoration:none']")).click();
        return this;
    }


}

