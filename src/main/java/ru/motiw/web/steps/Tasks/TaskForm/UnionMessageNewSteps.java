package ru.motiw.web.steps.Tasks.TaskForm;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.motiw.web.elements.elementsweb.Tasks.TaskForm.*;
import ru.motiw.web.model.Administration.TasksTypes.TasksTypes;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Tasks.Checkpoint;
import ru.motiw.web.model.Tasks.IWG;
import ru.motiw.web.model.Tasks.Project;
import ru.motiw.web.model.Tasks.Task;
import ru.motiw.web.steps.BaseSteps;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static ru.motiw.utils.WindowsUtil.newWindowForm;
import static ru.motiw.web.model.URLMenu.CREATE_TASK;

/**
 * Форма - СОЗДАТЬ ЗАДАЧУ
 */
public class UnionMessageNewSteps extends BaseSteps {

    private ProjectFormElements projectFormElements = page(ProjectFormElements.class);
    private InsetDescriptionTaskFormElements insetDescriptionTaskFormElements = page(InsetDescriptionTaskFormElements.class);
    private IWGFormElements iwgFormElements = page(IWGFormElements.class);
    private UsersSelectTheFormElements usersSelectTheFormElements = page(UsersSelectTheFormElements.class);
    private InsetPlanningTaskFormElements insetPlanningTaskFormElements = page(InsetPlanningTaskFormElements.class);
    private InsetDetailsTaskFormElements insetDetailsTaskFormElements = page(InsetDetailsTaskFormElements.class);

    /**
     * Переход в Задачи/Создать задачу напрямую по ссылке
     *
     * @return UnionMessageNewSteps вощвращаем стр. для дальнейшего взаимодействия
     * с елементами на странице
     */
    public static UnionMessageNewSteps goToURLUnionMessageNew() {
        openSectionOnURL(CREATE_TASK.getMenuURL());
        return page(UnionMessageNewSteps.class);
    }

    /**
     * Ожидание маски быстрого поиска при вводе шаблона поиска
     */
    private UnionMessageNewSteps waitForLiveSearchMask() {
        sleep(700);
        $(By.xpath("//*[contains (@class, 'loading-indicator')]")).shouldNotBe(Condition.visible);
        return this;
    }

    /**
     * Ожидание маски задачи
     */
    private UnionMessageNewSteps waitForTaskMask() {
        sleep(500);
        $(By.xpath("//*[contains (@class, 'ext-el-mask')]")).shouldNotBe(Condition.visible);
        return this;
    }

    /**
     * Ожидание маски проекта
     */
    private UnionMessageNewSteps waitForProjectMask() {
        sleep(300);
        $(By.xpath("//*[contains (@class, 'x-mask x-mask-fixed')]")).shouldNotBe(Condition.visible);
        return this;
    }

    /**
     * Добавление пользователей в роль задачи, через livesearch - Поиск по фамилии
     *
     * @param employees       массив передаваемых пользователей (Фамилия пользователя)
     * @param fieldCustomRole передаваемая выбираемая роль в задаче
     * @param valueField      заполнение поля (input) Фамилией пользователя
     */
    protected void choiceUsersThroughTheSearchLiveSurname(Employee[] employees, SelenideElement fieldCustomRole, SelenideElement valueField) {
        if (employees != null) {
            for (Employee employee : employees) {
                $(fieldCustomRole).shouldNotBe(Condition.disabled);
                fieldCustomRole.click();
                valueField.setValue(employee.getLastName());
                $(By.xpath("//div[contains (@style, 'visible')]//*[contains (text(), '" + employee
                        .getLastName() + "')]")).shouldBe(Condition.visible);
                $(By.xpath("//div[contains (@style, 'visible')]//*[contains (text(), '" + employee.getLastName() + "')]")).click();
                waitForTaskMask();
            }
        }
    }

    /**
     * Добавление Пользователей (ОР, Исполнители и т.д..) через livesearch, ввод SPACE и поиск пользователя из выпадающего списка
     * (контекстное меню)
     *
     * @param employees       массив передаваемых пользователей (Фамилия пользователя)
     * @param fieldCustomRole передаваемая выбираемая роль в задаче
     * @param valueField      заполнение поля (input) Фамилией пользователя
     */
    protected void choiceUsersThroughTheSearchLiveForSpace(Employee[] employees, SelenideElement fieldCustomRole, SelenideElement valueField) {
        if (employees != null) {
            for (Employee employee : employees) {
                $(fieldCustomRole).shouldBe(Condition.visible);
                fieldCustomRole.click();
                valueField.sendKeys(Keys.SPACE);
                waitForLiveSearchMask();
                $(By.xpath("//div[contains (@style, 'visible')]//*[contains (text(), '" + employee.getLastName() + "')]")).click();
                waitForTaskMask();
            }
        }
    }

    /**
     * Вводим название задачи
     *
     * @param taskName строковый параметр для передачи Названия задачи
     */
    private UnionMessageNewSteps setTaskName(String taskName) {
        insetDescriptionTaskFormElements.getClickTaskName().click();
        insetDescriptionTaskFormElements.getEditorField().setValue(taskName);
        return this;
    }

    /**
     * Клик сохранить задачу - Ожидание маски
     */
    private UnionMessageNewSteps clickSaveTask() {
        insetDescriptionTaskFormElements.getPlanningDescription().click();
        insetDescriptionTaskFormElements.getButtonCreateTask().click();
        waitForTaskMask();
        return this;
    }

    /**
     * Проверка что появилось окно и ссылка на созданную задачу
     */
    private UnionMessageNewSteps assertWindowTaskCreated() {
        $(By.xpath("//span[@class='ext-mb-text'][contains(text(),'Создана задача №')]")).waitUntil(Condition.visible, 20000);
        insetDescriptionTaskFormElements.getOKButtonInConfirmationFormTaskCreation().click();
        return this;
    }

    /**
     * Установка типа задачи
     *
     * @param taskType передаваемое имя типа задачи
     */
    private UnionMessageNewSteps setTaskType(TasksTypes taskType) {
        if (taskType == null) {
            return this;
        } else {
            $(insetDescriptionTaskFormElements.getFieldTaskType()).shouldBe(Condition.visible);
        }
        insetDescriptionTaskFormElements.getFieldTaskType().click();
        insetDescriptionTaskFormElements.getEditorField().click();
        $(By.xpath("//*[contains (@class, 'combo-list')][contains (@style, 'visible')]//*[contains (text(), '" + taskType.getObjectTypeName() + "')]"))
                .click();
        waitForTaskMask();
        return this;

    }

    /**
     * Создание нового проекта
     *
     * @param project передаваемые параметры атрибуты проекта
     */
    private UnionMessageNewSteps createProject(Project project) {
        if (project == null) {
            return this;
        } else {
            insetDescriptionTaskFormElements.getButtonNewProject().click();
            getFrameObject($(By.xpath("//iframe[@src='/user/project']")));
            // выбор поля Проект
            sleep(500);
            projectFormElements.getProjectField().click();
            // заполняем поле Проект (Название проекта)
            projectFormElements.getEditorFieldProject().setValue(project.getNameProject());
            // выбор поля Описание
            projectFormElements.getProjectDescription().click();
            // заполняем поле Описание проекта
            projectFormElements.getEditorDescriptionProject().setValue(project.getDescription());
            projectFormElements.getProjectEnd().click(); //снимаем фокус с поля Описание
            projectFormElements.getProjectClient().click();
            projectFormElements.getEditorFieldProject().setValue(project.getСlient());
            projectFormElements.getProjectEnd().click();
            projectFormElements.getEditorFieldProject().setValue(project.getEndDate());
            projectFormElements.getProjectSave().click();
            waitForProjectMask();
            switchTo().defaultContent();
            switchTo().frame($(By.cssSelector("#flow")));
            ;
        }
        return this;
    }

    /**
     * Ввод описания
     */
    private UnionMessageNewSteps setTaskDescription(String descript) {
        if (descript == null) {
            return this;
        } else {
            insetDescriptionTaskFormElements.getDescriptionTask().click();
            getFrameObject($(By.xpath("//iframe[@class='cke_wysiwyg_frame cke_reset']")));
            insetDescriptionTaskFormElements.getCkeBody().setValue(descript);
            switchTo().defaultContent();
            switchTo().frame($(By.cssSelector("#flow")));
            insetDescriptionTaskFormElements.getButtonSaveDescription().click();
        }
        return this;
    }

    /**
     * Ввод даты начала
     */
    private UnionMessageNewSteps setDataBegin(String begin) {
        if (begin == null) {
            return this;
        } else {
            insetDescriptionTaskFormElements.getBeginField().click();
            insetDescriptionTaskFormElements.getEditorField().setValue(begin);
        }
        return this;
    }

    /**
     * Ввод даты окончания задачи
     */
    private UnionMessageNewSteps setEnd(String end) {
        if (end == null) {
            return this;
        } else {
            sleep(1000);
            insetDescriptionTaskFormElements.getEndField().click();
            insetDescriptionTaskFormElements.getEditorField().setValue(end);

        }
        return this;
    }

    /**
     * Установка признака важности
     */
    private UnionMessageNewSteps setImportance(boolean isImportant) {
        insetDescriptionTaskFormElements.getPriority().click();
        insetDescriptionTaskFormElements.getEditorField().click();
        if (isImportant) {
            insetDescriptionTaskFormElements.getImportantTask().click();
        } else {
            insetDescriptionTaskFormElements.getSimpleTask().click();
        }
        return this;
    }

    /**
     * Установка Контрольных точек
     */
    private UnionMessageNewSteps setCheckpoints(Checkpoint[] checkpoints) {
        if (checkpoints == null) {
            return this;
        } else
            switchTo().defaultContent();
        switchTo().frame($(By.cssSelector("#flow")));
        insetPlanningTaskFormElements.getPlanningTab().click(); // Выбор вкладки - Планирование
        waitForTaskMask(); // Ожидание маски
        for (Checkpoint checkpoint : checkpoints) {
            insetPlanningTaskFormElements.getButtonAddCheckpoint().click(); // Добавить КТ
            insetPlanningTaskFormElements.getCheckpointDateField().click();
            insetDescriptionTaskFormElements.getEditorField().setValue(checkpoint.getDate());
            insetPlanningTaskFormElements.getCheckpointDescriptionField().click();
            getFrameObject($(By.xpath("//iframe[@class='cke_wysiwyg_frame cke_reset']")));
            insetDescriptionTaskFormElements.getCkeBody().setValue(checkpoint.getDescription());
            switchTo().defaultContent();
            switchTo().frame($(By.cssSelector("#flow")));
            insetDescriptionTaskFormElements.getButtonSaveDescription().click();
            insetPlanningTaskFormElements.getCheckpointNameField().click();
            clearTextInInputViaHotKeys(insetDescriptionTaskFormElements.getEditorField());
            insetDescriptionTaskFormElements.getEditorField().setValue(checkpoint.getName()).pressEnter(); // Заполняем Название КТ
            if (checkpoint.getIsReady()) {
                insetPlanningTaskFormElements.getCheckboxReadyFirst().click();
            }
        }
        return this;
    }

    /**
     * Установка признака с докладом
     */
    private UnionMessageNewSteps setReport(boolean isWithReport) {

        if (!isWithReport) {
            insetDetailsTaskFormElements.getAdditionalTab().click();
            insetDetailsTaskFormElements.getCheckboxWithReport().click();
        }
        return this;
    }

    /**
     * Установка признака секретная
     */
    private UnionMessageNewSteps setSecret(boolean isSecret) {
        if (isSecret) {
            insetDetailsTaskFormElements.getAdditionalTab().click();
            insetDetailsTaskFormElements.getCheckboxSecretTask().click();
        }
        return this;
    }

    /**
     * Установка признака только для ознакомления
     */
    private UnionMessageNewSteps setReview(boolean isForReview) {
        if (isForReview) {
            insetDetailsTaskFormElements.getAdditionalTab().click();
            insetDetailsTaskFormElements.getCheckboxOnlyForView().click();
        }
        return this;
    }

    /**
     * Установка признака - Системные действия в родительской задаче
     *
     * @param sysActionsInParentTask передаваемое булево зн-ия, для установки соответстующей настройки
     */
    private UnionMessageNewSteps iwgSysActionsInParentTask(boolean sysActionsInParentTask) {
        if (sysActionsInParentTask) {
            iwgFormElements.getInputIwgSysActionsInParentTask().click();
        }
        return this;
    }

    /**
     * Добавление пользователя в указанную роль (ОР / Исполнители ...) через форму выбора пользователей
     *
     * @param employeeIWG     Фамилия пользователя ИРГ
     * @param addAUserToARole элемент для выбора пользователя
     */
    private void addUserIWG(SelenideElement addAUserToARole, Employee employeeIWG) {
        addAUserToARole.click(); // добавить ОР ИРГ
        // Window PopUp
        String parentWindowHandler = getWebDriver().getWindowHandle(); // Store your parent window
        switchTo().window(new WebDriverWait(getWebDriver(), 10)
                .until(newWindowForm(By.cssSelector("#serverSearch"))));
        usersSelectTheFormElements.getUserSearchField().setValue(employeeIWG.getLastName());
        usersSelectTheFormElements.getUserSearchField().pressEnter();
        waitForTaskMask();
        usersSelectTheFormElements.getAddTheUserInTheFormOfChoice(employeeIWG).click(); // Добавить пользователя
        usersSelectTheFormElements.getUserSaveButton().click(); // Сохранить выбранных пользователей
        switchTo().window(parentWindowHandler);  // Switch back to parent window
        switchTo().frame($(By.cssSelector("#flow")));
        getFrameObject($(By.xpath("//iframe[contains(@src,'/user/editiwg')]")));
    }

    /**
     * Добавление ИРГ из массива
     */
    private UnionMessageNewSteps addTasksIWG(IWG[] iwg) {
        if (iwg == null) {
            return this;
        } else {
            // Общий массив ИРГ
            for (IWG anIwg : iwg) {
                insetDescriptionTaskFormElements.getButtonAddIWG().click(); // Добавить ИРГ
                getFrameObject($(By.xpath("//iframe[contains(@src,'/user/editiwg')]"))); // переходим во Фрейм формы добавления - ИРГ
               sleep(2000);
                $(iwgFormElements.getButtonIwgSave()).shouldBe(Condition.visible);
                iwgFormElements.getInputIwgName().setValue(anIwg.getNameIWG()); // Название ИРГ
                iwgFormElements.getInputIwgTaskType().click();
                $(By.xpath("//*[text()='" + anIwg.getTasksTypes().getObjectTypeName() + "']")).click();
                iwgSysActionsInParentTask(anIwg.getIsSystemActionsInParentTask()); // Системные действия в родительской задаче
                // ОР ИРГ
                if (anIwg.getRespPersons() == null) {
                    iwgFormElements.getButtonIwgCancel().click(); // Отменить создание/редактирование ИРГ
                    continue;
                } else {
                    for (int riwg = 0; riwg < (anIwg.getRespPersons().length); riwg++) {
                        addUserIWG(iwgFormElements.getButtonIwgAddRespPerson(),
                                anIwg.getRespPersons()[riwg]);
                    }
                }
                // Исполнители ИРГ
                if (anIwg.getWorkers() == null) {
                    iwgFormElements.getButtonIwgSave().click();
                    continue;
                } else {
                    for (int wiwg = 0; wiwg < (anIwg.getWorkers().length); wiwg++) {
                        addUserIWG(iwgFormElements.getButtonIwgAddWorker(),
                                anIwg.getWorkers()[wiwg]);
                    }
                }
                // Контролеры ИРГ
                if (anIwg.getControllers() == null) {
                    iwgFormElements.getButtonIwgSave().click();
                    continue;
                } else {
                    for (int сiwg = 0; сiwg < (anIwg.getControllers().length); сiwg++) {
                        addUserIWG(iwgFormElements.getButtonIwgAddController(),
                                anIwg.getControllers()[сiwg]);
                    }
                }
                iwgFormElements.getButtonIwgSave().click();
                verifyCreateIWG(anIwg.getNameIWG()); // Проверяем отображение ИРГ в гриде
            }
            switchTo().frame($(By.cssSelector("#flow"))); // возвращаемся в основной фрейм для дальнейшей работы в задаче
        }
        return this;
    }

    /**
     * Проверяем отображение добавленной подзадачи ИРГ в гриде ИРГ (ДО сохранения!)
     *
     * @param nameIWG передаем название ИРГ
     */
    private UnionMessageNewSteps verifyCreateIWG(String nameIWG) {
        switchTo().defaultContent();
        switchTo().frame($(By.cssSelector("#flow")));
        $(By.xpath("//div[@id='tab_iwg']//tbody//td[5]//div[text()='" + nameIWG + "']")).waitUntil(visible, 7000);
        return this;
    }


    /**
     * Проверка Загрузки страницы - форма Создания задачи - отображение вкладок в форме задачи
     */
    private UnionMessageNewSteps ensurePageLoaded() {
        sleep(2000); // todo если не постаивть sleep, то на linux из-за этого метода падает следующий вызываемый метод. На винде ошибки нет. Возможно, проблема в chromedriver-е 78 версии для linux, нужно будет проверить на новых версиях.
        List<SelenideElement> elementsTabsTask = new ArrayList<>();
        for (SelenideElement element : $$(By.xpath("//span[text()][ancestor::em[contains(@class,'x-tab')]][ancestor::li[not(@style='display: none;')]]"))) {
            elementsTabsTask.add(element);
        }
        for (int i = 0; i < elementsTabsTask.size(); i++) {
            String nameTab = elementsTabsTask.get(i).getText();
            elementsTabsTask.get(i).shouldBe(Condition.visible).shouldHave(text(nameTab));
        }
        return this;
    }

    /**
     * Создание обычной задачи
     *
     * @param task передаваемые атрибуты задачи
     */
    public UnionMessageNewSteps creatingTask(Task task) {
        ensurePageLoaded();
        setEnd(task.getDateEnd());
        createProject(task.getProject());
        setTaskName(task.getTaskName())
                .setTaskDescription(task.getDescription())
                .setDataBegin(task.getDateBegin())
                .setImportance(task.getIsImportant());
        // выбор пользователя по ФИО - Авторы - через searchlive
        choiceUsersThroughTheSearchLiveSurname(task.getAuthors(), insetDescriptionTaskFormElements.getAuthorsField(),
                insetDescriptionTaskFormElements.getEditorField());
        // выбор пользователя - Контролер - через searchlive
        choiceUsersThroughTheSearchLiveForSpace(task.getControllers(), insetDescriptionTaskFormElements.getСontrollersField(),
                insetDescriptionTaskFormElements.getEditorField());
        // выбор пользователя - Исполнителей - через searchlive
        choiceUsersThroughTheSearchLiveForSpace(task.getWorkers(), insetDescriptionTaskFormElements.getWorkersField(),
                insetDescriptionTaskFormElements.getEditorField());
        // выбор пользователя - Ответственные руководители - через searchlive
        choiceUsersThroughTheSearchLiveForSpace(task.getExecutiveManagers(), insetDescriptionTaskFormElements.getExecutiveManagersField(),
                insetDescriptionTaskFormElements.getEditorField());
        setTaskType(task.getTaskType()) // выбор - Тип задачи
                .setReport(task.getIsWithReport())
                .setSecret(task.getIsSecret())
                .setReview(task.getIsForReview());

        return this;
    }

    /**
     * Сохранить задачу
     *
     * @return UnionMessageSteps страница UnionMessageSteps (Задачи / Задачи)
     */
    public UnionMessageSteps saveTask() {
        clickSaveTask()
                .assertWindowTaskCreated();
        return page(UnionMessageSteps.class);
    }

    /**
     * Создание обычной задачи с задачей ИРГ
     *
     * @param task передаются все необходимые атрибуты задачи
     */
    public void creatingTaskWithTheTaskOfIWG(Task task) {
        ensurePageLoaded();
        setTaskName(task.getTaskName());
        // выбор пользователя - Ответственные руководители - через searchlive
        choiceUsersThroughTheSearchLiveForSpace(task.getExecutiveManagers(), insetDescriptionTaskFormElements.getExecutiveManagersField(),
                insetDescriptionTaskFormElements.getEditorField());
        setTaskType(task.getTaskType())
                .addTasksIWG(task.getIWG())
                .setReport(task.getIsWithReport())
                .setSecret(task.getIsSecret())
                .setReview(task.getIsForReview());
        saveTask();
    }

    /**
     * Создание обычной задачи с КТ
     *
     * @param task передаются все необходимые атрибуты задачи
     */
    public void creationOfATaskCheckpoints(Task task) {
        ensurePageLoaded();
        setTaskName(task.getTaskName())
                .setEnd(task.getDateEnd())
                .setCheckpoints(task.getCheckpoints()); // Контрольные точки
        saveTask();
    }


}








