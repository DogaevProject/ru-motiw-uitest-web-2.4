package ru.motiw.web.steps.Administration.Users;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.UIAssertionError;
import org.openqa.selenium.*;
import org.testng.AssertJUnit;
import ru.motiw.web.elements.elementsweb.Administration.Users.DepartmentElements;
import ru.motiw.web.elements.elementsweb.Administration.Users.UsersElements;
import ru.motiw.web.elements.elementsweb.Internal.InternalElements;
import ru.motiw.web.elements.elementsweb.Tasks.UnionTasks.UnionTasksElements;
import ru.motiw.web.model.Administration.Users.*;
import ru.motiw.web.model.Administration.Users.Module;
import ru.motiw.web.steps.Home.InternalSteps;
import ru.motiw.web.steps.Tasks.UnionTasksSteps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static ru.motiw.web.model.URLMenu.INTERNAL_MENU;
import static ru.motiw.web.steps.Tasks.UnionTasksSteps.goToUnionTasks;

/**
 * Пользователи
 */
public class UsersSteps extends DepartmentSteps {

    private UsersElements usersElements = page(UsersElements.class);
    private DepartmentElements departmentElements = page(DepartmentElements.class);
    private InternalElements internalElements = page(InternalElements.class);
    private UnionTasksSteps unionTasksSteps = page(UnionTasksSteps.class);
    private UnionTasksElements unionTasksElements = page(UnionTasksElements.class);

    /**
     * Возврат логин пользовтаеля
     *
     * @return строковое значение из поля Имя пользователя (login)
     */
    public String getAttrLoginName() {
        sleep(1000);
        usersElements.getNameUserLogin().click();
        return usersElements.getVisibleEditor().attr("value");
    }

    /**
     * Общий метод заполнения поля в форме реквизитов пользователя
     *
     * @param field передаваемая поле (елемент) - в форме реквизитов пользователя
     * @param text  передаваемая - текстовое зн-ие для ввода
     * @return UsersPageStep
     */
    private UsersSteps setEntryField(SelenideElement field, String text) {
        field.click();
        field.setValue(text);
        return this;
    }

    /**
     * Выбор пола пользователя
     *
     * @param sex передаваемое зн-ие, если true == Мужчина
     * @return UsersSteps
     */
    private UsersSteps sex(Sex sex) {
        if (sex == Sex.MAN) {
            usersElements.getCheckMan().click();
        } else {
            usersElements.getCheckWoman().click();
        }
        return this;
    }

    /**
     * Имя пользователя
     *
     * @param userName передаваемое Имя пользователя
     */
    private UsersSteps setNameUserLogin(String userName) {
        usersElements.getNameUserLogin().click();
        usersElements.getVisibleEditor().setValue(userName);
        return this;
    }

    /**
     * Пароль
     *
     * @param passwordUser передаваемый - пароль - пользователя
     */
    private UsersSteps setPasswordUser(String passwordUser) {
        usersElements.getPasswordUser().click();
        usersElements.getVisibleEditor().setValue(passwordUser);
        usersElements.getVisibleEditor().pressEnter();
        return this;
    }

    /**
     * Подтверждение пароля
     *
     * @param confirmationPassword передаваемое значение - подтверждение пароля - пользователя
     */
    private UsersSteps setConfirmationPassword(String confirmationPassword) {
        usersElements.getСonfirmationPassword().click();
        usersElements.getVisibleEditor().setValue(confirmationPassword);
        return this;
    }

    /**
     * Статус - Начальник
     */
    private UsersSteps setStatus(Status status) {
        usersElements.getStatus().click();
        if (status == Status.BOSS) {
            usersElements.getVisibleEditor().sendKeys(Keys.chord(Keys
                    .ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER));
        } else if (status == Status.CLERK) {
            usersElements.getVisibleEditor().sendKeys(Keys.chord(Keys
                    .ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER));
        }
        return this;
    }

    /**
     * Заполнение строковых полей в форме реквизитов пользователя
     *
     * @param clickField локатор элемента поля - выбор поля
     * @param field      действия производимые над выбранным полем (очистить, заполнить зн-ие)
     * @param valueField
     */
    private void rangeOfFieldsAndFillingInDetails(SelenideElement clickField, SelenideElement field, String valueField) {
        clickField.click();
        field.setValue(valueField);
    }

    /**
     * Сменить пароль при следующем входе
     */
    private UsersSteps needPasswordChange(boolean needsPassChange) {
        if (!needsPassChange) {
            usersElements.getNeedPasswordChange().click();
        }
        return this;
    }

    /**
     * Нажатие кнопки - Сохранить пользователя
     */
    private UsersSteps saveUser() {
        usersElements.getSave().click();
        waitForMask();
        return this;
    }

    /**
     * Проверяем что пользователь отображается в гриде
     *
     * @param user передаваемые атрибуты пользователя
     */
    private UsersSteps verifyUserInGrid(Employee user) {
        $(By.xpath("//tbody[contains(@id,'gridview')]/tr//a[text()='"
                + user.getLastName() + " " + user.getName() + " "
                + user.getPatronymic() + "']")).waitUntil(visible, 15000);
        return this;
    }

    /**
     * Проверяем что пользователь отображается в гриде
     *
     * @param user передаваемые атрибуты пользователя
     */
    private boolean userIsInGrid(Employee user) {
        waitForMask();
        return $(By.xpath("//tbody[contains(@id,'gridview')]/tr//a[text()='"
                + user.getLastName() + " " + user.getName() + " "
                + user.getPatronymic() + "']")).isDisplayed();
    }

    /**
     * Выбор пользователя в гриде
     *
     * @param user передаваемые реквизиты пользователя
     * @return UsersSteps
     */
    private UsersSteps selectTheUserInTheGrid(Employee user) {
        $(By.xpath("//a[contains (text(), '" + user.getLastName() + "')]")).shouldBe(visible);
        $(By.xpath("//a[contains (text(), '" + user.getLastName()
                + "')]/parent::div/parent::td/preceding-sibling::td"))
                .click();
        return this;
    }

    /**
     * Выбор пользователя (открытие формы редактирования пользователя) в гриде
     *
     * @param user передаваемые реквизиты пользователя
     * @return UsersSteps
     */
    private UsersSteps clickEditUserFormByName(Employee user) {
        $(By
                .xpath("//a[contains (text(), '" + user.getLastName() + "')]")).shouldBe(visible);
        $(By.xpath("//a[contains(@onclick,'openEditUserForm')][contains(text(),'" + user.getLastName() + "')]"))
                .click();
        return this;
    }

    /**
     * В окне добавления псевдонима в подразделение ищем нужный департамент
     */
    private UsersSteps searchDepartmentForAlias(Department department) {
        getFrameObject($(By.xpath("//*[contains (@src, 'selectdep')]")));
        sleep(2000);
        $(usersElements.getFieldSearchDepForAlias()).shouldBe(visible);
        usersElements.getFieldSearchDepForAlias().click();
        usersElements.getFieldSearchDepForAlias().sendKeys(department.getDepartmentName());
        usersElements.getButtonSearchDepForAlias().pressEnter();
        return this;
    }

    /**
     * Проверка наличия на странице псевдонима для подразделения
     */
    private UsersSteps assertHasAlias(Employee user, Department department) {
        assertTrue($(By.xpath("/*//*[contains (text(), '" + user.getLastName() + " " + user.getName() + " "
                + user.getPatronymic() + " (для задач по " + department.getDepartmentName() + ")')]")).isDisplayed());
        return this;
    }

    /**
     * Метод устновки прав на модуль
     *
     * @param module параметр установки прав на определенные модули системы
     */
    public UsersSteps selModule(Module module) {
        if (module == Module.DOCFLOW) {
            usersElements.getWorkflow().click();
        }
        if (module == Module.WORKFLOW) {
            usersElements.getDocflow().click();
        }
        return this;
    }

    /**
     * Метод удаления пользователей
     */
    public UsersSteps deleteUser(Employee user) {
        if (user != null) {
            beforeCreationDepartmentsAndUsers();
            switchTo().defaultContent();
            initializationInternalPage().searchFacilityOnTheGrid(user.getLastName());
            switchTo().frame($(By.cssSelector("#flow")));
            waitForMask();
            selectTheUserInTheGrid(user); // выбираем пользователя в гриде
            usersElements.getRemoveUser().click(); // Нажимаем удалить пользователя
            confirmDeletionUser(user);
        }
        return this;
    }

    /**
     * Подтверждение удаления пользователя
     *
     * @param user передаваемые атрибуты пользователя
     */
    private DepartmentSteps confirmDeletionUser(Employee user) {
        checkingMessagesConfirmationOfTheObject(departmentElements.getExpectedMessageTextToDialog(),
                "Вы уверены, что хотите удалить пользователя " + user.getLastName() + " "
                        + user.getName() + " " + user.getPatronymic() + "" + "?", departmentElements.getOKRemoveDelete());
        waitForMask();
        $(By.xpath("//tbody[contains(@id,'gridview')]/tr//a[text()='"
                + user.getLastName() + " " + user.getName() + " "
                + user.getPatronymic() + "']")).shouldNotBe(visible);
        return this;
    }

    /**
     * Проверяем, что текущий пользователь Docflow. Отсутствует п.м. Создать задачу
     *
     * @return InternalElementsMobile
     */
    public void checkUserDocflow() {
        switchTo().defaultContent();
        open(baseUrl + INTERNAL_MENU.getMenuURL());
        internalElements.getMenuTask().click();
        assertFalse($(By.xpath("//*[@id='L_INFORMER_CREATETASK-menupoint']")).isDisplayed());
    }

    /**
     * Проверяем, что текущий пользователь Workflow. Отсутствует п.м. Поиск документов и Документы
     *
     * @return InternalElementsMobile
     */
    public void checkUserWorkflow(int countPanelGrouping) {
        switchTo().defaultContent();
        open(baseUrl + INTERNAL_MENU.getMenuURL());
        $(By.xpath("//*[@id='doc-search']/a")).shouldNotBe(Condition.visible);
        internalElements.getMenuDocument().shouldNotBe(Condition.visible);
        goToUnionTasks();
        unionTasksSteps.selectTheGroupInTheGridForUserComplete(unionTasksElements.getPanelGroupingTasks(), countPanelGrouping);

    }

    /**
     * Инициализация класса - Внутреняя страница
     */
    private InternalSteps initializationInternalPage() {
        return page(InternalSteps.class);
    }

    /**
     * Создание псевдонима пользователя, методу передается пользователь,
     * которому будет создан псевдоним и подразделение в котором будет создан псевдоним
     */
    public void createAndCheckAliasForDep(Employee user, Department department) {
        switchTo().defaultContent();
        initializationInternalPage().searchFacilityOnTheGrid(user.getLastName());
        switchTo().frame($(By.cssSelector("#flow")));
        waitForMask();
        selectTheUserInTheGrid(user);
        usersElements.getCreateAnAlias().click();
        searchDepartmentForAlias(department);
        usersElements.getSelectedCheckBox().click();
        usersElements.getButtonSaveAlias().click();
        checkIsAlias(user, department);
    }

    /**
     * Проверяем наличие псевдонима в Подразделении. Методу передается
     * пользователь оригинал, и подразделение для которого нужно проверить наличие псевдонима
     * данного пользователя
     *
     * @param user       атрибуты пользователя
     * @param department атрибуты подразделения
     */
    private void checkIsAlias(Employee user, Department department) {
        switchTo().defaultContent();
        initializationInternalPage().searchFacilityOnTheGrid(user.getLastName());
        switchTo().frame($(By.cssSelector("#flow")));
        waitForMask();
        assertHasAlias(user, department);
    }

    /**
     * Заполнение полей карточки пользователя при создании и редактировании
     *
     * @param user
     */
    private void fillFieldsOfCardUser(Employee user) {
        usersElements.getLastName().waitUntil(visible, 5000);
        setEntryField(usersElements.getLastName(), user.getLastName()) // Фамилия
                .setEntryField(usersElements.getName(), user.getName()) // Имя
                .setEntryField(usersElements.getPatronymic(), user.getPatronymic()) // Отчество
                .sex(user.getSex())
                .setEntryField(usersElements.getBirthDateField(), user.getBirthDate()) // Дата рождения
                .setEntryField(usersElements.getJobTitle(), user.getJobTitle()) // Должность
                .setNameUserLogin(user.getLoginName())
                .setPasswordUser(user.getPassword())
                .setConfirmationPassword(user.getСonfirmationPassword())
                .setStatus(user.getStatus())
                .selModule(user.getModule());
        rangeOfFieldsAndFillingInDetails(usersElements.getAdditionalNumber(), usersElements.getVisibleEditor(),
                user.getAdditionalNumber()); // Доп. номер
        rangeOfFieldsAndFillingInDetails(usersElements.getUserForcedSorting(), usersElements.getVisibleEditor(),
                user.getUserForcedSorting()); // Порядок пользователя при принудительной сортировке
        needPasswordChange(user.getNeedsPasswordChange());
    }

    public void beforeCreationDepartmentsAndUsers() {
        super.beforeCreationDepartmentsAndUsers();
    }

    /**
     * Метод Добавления пользователей. В качестве параметра передается объект
     * создаваемого пользователя и подразделение, в которое он будет помещен
     *
     * @param user атрибуты пользователя
     */
    public UsersSteps createUser(Employee user) {
        beforeCreationDepartmentsAndUsers();
        if (user.getDepartment() != null) {
            selectTheParentUnit(user.getDepartment()); // Выбираем подразделение
        }
        try {
            usersElements.getButtonAddUser().click(); // Добавить пользователя
            switchTo().frame($(By.xpath("//iframe[contains(@id,'component-')]")));
            usersElements.getLastName().waitUntil(visible, 10000);
        } catch (UIAssertionError | NotFoundException e) {
            // Обработка случая зависания открытия формы
            goToURLDepartments();
            //выполняем действия повторно
            beforeCreationDepartmentsAndUsers();
            if (user.getDepartment() != null) {
                selectTheParentUnit(user.getDepartment()); // Выбираем подразделение
            }
            usersElements.getButtonAddUser().click(); // Добавить пользователя
            switchTo().frame($(By.xpath("//iframe[contains(@id,'component-')]")));
        }
        fillFieldsOfCardUser(user);
        try {
            saveUser();
        } catch (AssertionError e) {
            // Обработка случая зависания ответа сервера в момент сохранения
            goToURLDepartments();
            //выполняем действия повторно
            beforeCreationDepartmentsAndUsers();
            if (user.getDepartment() != null) {
                selectTheParentUnit(user.getDepartment()); // Выбираем подразделение
            }
            // проверяем сохранился ли добавленный пользователь в гриде
            if (!userIsInGrid(user)) {
                // если нет, то пробуем добавить снова
                usersElements.getButtonAddUser().click(); // Добавить пользователя
                switchTo().frame($(By.xpath("//iframe[contains(@id,'component-')]")));
                fillFieldsOfCardUser(user);
                saveUser();
            }
        }
        switchTo().defaultContent();
        switchTo().frame($(By.cssSelector("#flow")));
        AssertJUnit.assertTrue(userIsInGrid(user));
        return this;
    }

    /**
     * Метод Редактирования пользователей. В качестве параметра передается объект
     * созданного пользователя и его подразделение, в котором он был создан
     *
     * @param user     атрибуты пользователя
     * @param editUser атрибуты нового пользователя
     */
    public UsersSteps editUser(Employee user, Employee editUser) {
        selectTheParentUnit(user.getDepartment());
        clickEditUserFormByName(user); // выбираем пользователя в гриде для редактирования
        getFrameObject($(By.xpath("//iframe[contains(@id,'component-')]"))); // Переходим в iframe формы добавления пользователя
        fillFieldsOfCardUser(editUser);
        try {
            saveUser();
        } catch (AssertionError e) {
            // Обработка случая зависания ответа сервера в момент сохранения
            goToURLDepartments();
            //выполняем действия повторно
            beforeCreationDepartmentsAndUsers();
            selectTheParentUnit(user.getDepartment());
            // проверяем сохранился ли отредактированный пользователь в гриде
            if (!userIsInGrid(editUser)) {
                // если нет, то пробуем редактировать снова
                clickEditUserFormByName(user); // выбираем пользователя в гриде для редактирования
                getFrameObject($(By.xpath("//iframe[contains(@id,'component-')]"))); // Переходим в iframe формы добавления пользователя
                fillFieldsOfCardUser(editUser);
                saveUser();
            }
        }
        switchTo().defaultContent();
        switchTo().frame($(By.cssSelector("#flow")));
        AssertJUnit.assertTrue(userIsInGrid(editUser));
        return this;
    }
}
