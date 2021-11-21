package ru.motiw.web.steps.Administration.Users;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.motiw.web.steps.BaseSteps;
import ru.motiw.web.elements.elementsweb.Administration.Users.DepartmentElements;
import ru.motiw.web.model.Administration.Users.Department;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.motiw.web.model.URLMenu.DEPARTMENTS_AND_USERS;


/**
 * Подразделения
 */
public class DepartmentSteps extends BaseSteps {

    private DepartmentElements departmentElements = page(DepartmentElements.class);

    /**
     * Переход в меню - Администрирование/Подразделения
     */
    public static DepartmentSteps goToURLDepartments() {
        openSectionOnURL(DEPARTMENTS_AND_USERS.getMenuURL());
        return page(DepartmentSteps.class);
    }

    /**
     * Выбрать корневое Подразделение для взаимодействия с элементом
     *
     * @return DepartmentSteps
     */
    private DepartmentSteps choiceOfTheParentUnit() {
        departmentElements.getFirstDepartments().click();
        waitForMask();
        return this;
    }

    /**
     * Название Подразделения
     *
     * @param nameDepartment input text
     * @return DepartmentSteps
     */
    private DepartmentSteps setNameDep(String nameDepartment) {
        departmentElements.getNameDepartment().setValue(nameDepartment);
        assertThat(departmentElements.getNameDepartment().getValue(), is(equalTo(nameDepartment)));
        return this;
    }

    /**
     * Условный номер подразделения
     *
     * @param text input text
     * @return DepartmentSteps
     */
    private DepartmentSteps setConditionalNumber(String text) {
        departmentElements.getConditionalNumber().setValue(text);
        assertThat(departmentElements.getConditionalNumber().getValue(), is(equalTo(text)));
        return this;
    }

    /**
     * Значение счетчика подразделения
     *
     * @param counterVal input text
     * @return DepartmentSteps
     */
    private DepartmentSteps setCounter(String counterVal) {
        departmentElements.getCounter().setValue(counterVal);
        assertThat(departmentElements.getCounter().getValue(), is(equalTo(counterVal)));
        return this;
    }

    /**
     * Дата обнуления счетчика подразделения
     *
     * @param resetDateText input text
     * @return DepartmentSteps
     */
    private DepartmentSteps setResetDate(String resetDateText) {
        departmentElements.getResetDate().setValue(resetDateText);
        assertThat(departmentElements.getResetDate().getValue(), is(equalTo(resetDateText)));
        return this;
    }

    /**
     * Дата обнуления счетчика подразделения
     *
     * @param numeratorTemplateText input text
     * @return DepartmentSteps
     */
    private DepartmentSteps setNumeratorTemplate(String numeratorTemplateText) {
        departmentElements.getNumeratorTemplate().setValue(numeratorTemplateText);
        assertThat(departmentElements.getNumeratorTemplate()
                .getValue(), is(equalTo(numeratorTemplateText)));
        return this;
    }

    /**
     * Проверка отображения созданного подразделения
     *
     * @return DepartmentSteps
     */
    private DepartmentSteps verifyCreateDepartment(String departmentName) {
        $(By.xpath("//*[text()='" + departmentName + "'] [ancestor::*[contains(@id,'treeview')]]"))
                .waitUntil(Condition.visible, 10000);
        return this;
    }

    /**
     * Проверка удаления подразделения.
     * Подразделение не должно отображаться в гриде после удаления
     *
     * @return DepartmentSteps
     */
    private DepartmentSteps verifyRemoveDepartment(String DepName) {
        $(By.xpath("//*[text()='" + DepName + "'] [ancestor::*[contains(@id,'treeview')]]"))
                .waitUntil(Condition.disappears, 10000);
        return this;
    }

    /**
     * После каждого клика на подразделение повисает маска, данный метод должен
     * использовваться после каждого клика по подразделению для ожидания
     * невидимости маски
     *
     * @return DepartmentSteps
     */
    protected DepartmentSteps waitForMask() {
        sleep(400);
        $(By.xpath("//*[contains (@class, 'x-mask')]")).waitUntil(Condition.disappear, 30000);
        return this;
    }

    /**
     * Метод ДнД подразделения
     *
     * @return DepartmentSteps
     */
    private DepartmentSteps DnDDepartment(Department source, Department target) {
        SelenideElement sourceElement = $(By
                .xpath("//span[contains(text(),'" + source.getDepartmentName()
                        + "')] [ancestor::tbody[contains(@id,'treeview')]]"));
        SelenideElement targetElement = $(By
                .xpath("//span[contains(text(),'" + target.getDepartmentName()
                        + "')] [ancestor::tbody[contains(@id,'treeview')]]"));
        sourceElement.click();
        sleep(200);
        Actions builder = new Actions(getWebDriver());
        Action drag = builder.clickAndHold(sourceElement).build();
        Action drop = builder.moveToElement(targetElement)
                .release(targetElement).build();
        drag.perform();
        waitForMask();
        drop.perform();
        return this;
    }

    /**
     * Метод ДнД для удаления подразделений
     *
     * @return DepartmentSteps
     */
    private DepartmentSteps dndFirstChildToRoot(Department department) {
        $(By.xpath("//*[contains (@id, 'messagebox')][contains (@class, 'closable')]/div[contains (@id, 'toolbar')]//a[1]//span[2]"))
                .click();
        SelenideElement child = $(By.xpath("//span[contains(text(),'"
                + department.getDepartmentName()
                + "')] [ancestor::tbody[contains(@id,'treeview')]]/parent::div/parent::td/parent::tr/following-sibling::tr/td/div/span"));
        child.click();
        Actions builder = new Actions(getWebDriver());
        Action drag = builder.clickAndHold(child).build();
        Action drop = builder.moveToElement(departmentElements.getFirstDepartments())
                .release(departmentElements.getFirstDepartments()).build();
        drag.perform();
        waitForMask();
        drop.perform();
        checkingMessagesConfirmationOfTheObject(departmentElements.getExpectedMessageTextToDialog(),
                "Сохранить дополнительные полномочия пользователей?", departmentElements.getSavePermissionYes());
        return this;
    }

    /**
     * Выбираем подразделение по имени
     */
    protected DepartmentSteps selectTheParentUnit(Department department) {
        waitForMask();
        $(By.xpath("//span[contains(text(),'" + department.getDepartmentName()
                + "')] [ancestor::tbody[contains(@id,'treeview')]]"))
                .click();
        return this;
    }

    /**
     * Логическая проверка, есть ли у подразделения дочерние подразделение,
     * производится только, после клика кнопки - Удалить подразделение
     */
    private boolean hasChild() {
        try {
            (new WebDriverWait(getWebDriver(), 0, 50))
                    .until(ExpectedConditions.elementToBeClickable(By
                            .xpath("//*[contains (@id, 'messagebox')][contains (@class, 'closable')]/div[contains (@id, 'toolbar')]//a[1]")));
            return true;
        } catch (TimeoutException to) {
            return false;
        }
    }

    /**
     * Метод помогающий подготовить интерфейс к добавлению подразделений
     * (Проверка загрузки страницы, раскрытие всех элементов дерева подразделений)
     */
    public void beforeCreationDepartmentsAndUsers() {
        departmentElements.getFirstDepartments().waitUntil(Condition.visible, 10000);
        unwrapAllNodes(departmentElements.getClosedNode(),
                By.xpath("//tr[not(contains(@class, 'expanded'))]//img[contains (@class, 'expander')]"));
        choiceOfTheParentUnit(); // выбираем корневое подразделение
    }

    /**
     * Создание объекта - Подразделение
     *
     * @param department передаваемые атрибуты подразделения
     */
    public DepartmentSteps createDepartment(Department department) {
        beforeCreationDepartmentsAndUsers();
        if (department.getParentDepartment() != null) {
            selectTheParentUnit(department.getParentDepartment()); // Выбираем подразделение
        }
        departmentElements.getButtonAddDepartment().click(); // Добавить подразделение - Подразделение
        setNameDep(department.getDepartmentName()) // Название подразделения
                .setConditionalNumber(department.getConditionalNumber()) // Условный номер
                .setCounter(department.getCounter()) // Счетчик
                .setResetDate(department.getResetDate()) // Дата обнуления счетчика
                .setNumeratorTemplate(department.getNumeratorTemplate()); // Шаблон нумератора
        departmentElements.getSaveDepartment().click(); // Сохранить подразделение
        verifyCreateDepartment(department.getDepartmentName()); // Проверяем отображение элемента в гриде
        return this;

    }

    /**
     * Подтверждение удаления подразделения
     *
     * @param department передаваемое Название подразделения
     */
    private DepartmentSteps confirmDeletionDepartment(Department department) {
        checkingMessagesConfirmationOfTheObject(departmentElements.getExpectedMessageTextToDialog(),
                "Вы уверены, что хотите удалить подразделение " + department.getDepartmentName() + "" + "?",
                departmentElements.getOKRemoveDelete());
        waitForMask();
        return this;
    }

    /**
     * Метод редактирования имеющегося подразделения
     *
     * @param editedDepartment передаваемые атрибуты полей редактируемого подразделения
     * @param department       передаваемые атрибуты начального подразделения
     */
    public DepartmentSteps editDepartments(Department editedDepartment, Department department) {
        selectTheParentUnit(editedDepartment)
                .departmentElements.getEditDepartment().click();
        setNameDep(department.getDepartmentName())
                .setConditionalNumber(department.getConditionalNumber())
                .setCounter(department.getCounter())
                .setResetDate(department.getResetDate())
                .setNumeratorTemplate(department.getNumeratorTemplate());
        departmentElements.getSaveDepartment().click();
        verifyCreateDepartment(department.getDepartmentName());
        return this;
    }

    /**
     * Удаление подразделений, удаляет подраздделения, даже, когда у удаляемого
     * подразделения есть вложенные
     */
    public DepartmentSteps deleteDepartment(Department department) {
        selectTheParentUnit(department); // Выбираем текущее подразделение в гриде
        departmentElements.getRemoveDepartment().click(); // Удалить подразделение
        confirmDeletionDepartment(department);
        while (hasChild()) { // Пока есть подразделение потомок
            dndFirstChildToRoot(department) // Днд первое подразделение потомок в корень
                    .selectTheParentUnit(department); // Выбираем текущее подразделение в гриде
            departmentElements.getRemoveDepartment().click(); // Удалить подразделение
            confirmDeletionDepartment(department);
        }
        verifyRemoveDepartment(department.getDepartmentName());
        return this;
    }

    /**
     * ДнД подразделения с сохранением дополнительных полномочий
     */
    public DepartmentSteps dndSavePermissions(Department source, Department target) {
        DnDDepartment(source, target).checkingMessagesConfirmationOfTheObject(departmentElements.getExpectedMessageTextToDialog(),
                "Сохранить дополнительные полномочия пользователей?", departmentElements.getSavePermissionYes());
        waitForMask();
        return this;

    }


}
