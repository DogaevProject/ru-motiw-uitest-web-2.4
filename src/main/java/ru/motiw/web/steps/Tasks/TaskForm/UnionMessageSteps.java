package ru.motiw.web.steps.Tasks.TaskForm;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.motiw.web.elements.elementsweb.Tasks.TaskForm.UnionMessageElements;
import ru.motiw.web.model.Administration.TasksTypes.TasksTypes;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Tasks.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static ru.motiw.utils.WindowsUtil.newWindowForm;

/**
 * Форма (редактирования) задачи
 */
public class UnionMessageSteps extends UnionMessageNewSteps {

    private UnionMessageElements unionMessageElements = page(UnionMessageElements.class);

    public UnionMessageSteps verifyEnd(String end) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyProject(Project project) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyTaskDescription(String description) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyBegin(String begin) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyImportance(boolean isImportant) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyAuthors(Employee[] authors) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyControllers(Employee[] controllers) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyWorkers(Employee[] workers) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyResppersons(Employee[] resppersons) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyTaskType(TasksTypes tasktype) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyIWG(IWG[] iwg) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyCheckpoints(Checkpoint[] checkpoints) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyReport(boolean isWithReport) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifySecret(boolean isSecret) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyReview(boolean isForReview) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps postAction(Action[] actions) {
        // TODO Auto-generated method stub
        return this;
    }

    public UnionMessageSteps verifyAction() {
        // TODO Auto-generated method stub
        return this;
    }

    /**
     * Проверяем создание задачи
     *
     * @param valueTask атрибуты задачи
     * @return UnionMessageSteps
     */
    public UnionMessageSteps verifyCreateTask(Task valueTask) {
        /*
         Window PopUp. Store your parent window
         */
        String parentWindowHandler = getWebDriver().getWindowHandle();
        getWebDriver().switchTo().window(new WebDriverWait(getWebDriver(), 10).until(newWindowForm(By.xpath("//body[@id='unionmessage']//li//span[text()='Действия']"))));
        ensurePageLoaded();
        if (valueTask == null) {
            return null;
        } else
            verifyEnd(valueTask.getDateEnd())
                    .verifyProject(valueTask.getProject())
                    .verifyTaskDescription(valueTask.getDescription())
                    .verifyBegin(valueTask.getDateBegin())
                    .verifyImportance(valueTask.getIsImportant())
                    .verifyAuthors(valueTask.getAuthors())
                    .verifyControllers(valueTask.getControllers())
                    .verifyWorkers(valueTask.getWorkers())
                    .verifyResppersons(valueTask.getExecutiveManagers())
                    .verifyTaskType(valueTask.getTaskType())
                    .verifyIWG(valueTask.getIWG())
                    // TODO вынести проверки -  .verifyCheckpoints(valueTask.getCheckpoints()) и .verifyIWG(valueTask.getIWG()) в отдельные - для соот.  отдельных методов
                    .verifyCheckpoints(valueTask.getCheckpoints())
                    .verifyReport(valueTask.getIsWithReport())
                    .verifySecret(valueTask.getIsSecret())
                    .verifyReview(valueTask.getIsForReview())
                    .postAction(valueTask.getActions())
                    .verifyAction();

        closeWindow();
        getWebDriver().switchTo().window(parentWindowHandler);  // Switch back to parent window
        return this;
    }

    /**
     * Проверка загрузки формы задачи - отображение вкладок в форме созданной задачи
     */
    public UnionMessageSteps ensurePageLoaded() {
        sleep(2000);
        if ($(unionMessageElements.getTabIWG()).isDisplayed()) {
            checkDisplayedTabsInTheShapeOfAnObject(By.xpath("//span[text()][ancestor::em[contains(@class,'x-tab')]][ancestor::li[not(@style='display: none;')]]"), 11,
                    By.xpath("//span[text()][ancestor::em[contains(@class,'x-tab')]][ancestor::li[not(@style='display: none;')]]"),
                    new String[]{"\u00A0", "Действия", "Описание", "Изолированные рабочие группы", "Файлы", "Планирование", "События", "Контакты", "Журнал", "Связь", "\u00A0"});
        } else {
            checkDisplayedTabsInTheShapeOfAnObject(By.xpath("//span[text()][ancestor::em[contains(@class,'x-tab')]][ancestor::li[not(@style='display: none;')]]"), 10,
                    By.xpath("//span[text()][ancestor::em[contains(@class,'x-tab')]][ancestor::li[not(@style='display: none;')]]"),
                    new String[]{"\u00A0","Действия", "Описание", "Файлы", "Планирование", "События", "Контакты", "Журнал", "Связь", "\u00A0"});
        }
        return this;
    }

    /**
     * Переход в документ
     */
    public UnionMessageSteps goToDocument() {
        unionMessageElements.getButtonGoToDocument().click();
        return this;
    }

    /**
     * Проверяем, что кнопки для согласования документа отображаются
     */
    public UnionMessageSteps verifyButtonsOfConsiderationIsVisible() {
        unionMessageElements.getButtonConsideration().shouldBe(Condition.visible);
        unionMessageElements.getButtonConsiderationWithComment().shouldBe(Condition.visible);
        unionMessageElements.getButtonDenialReview().shouldBe(Condition.visible);
        unionMessageElements.getButtonBackToRevision().shouldBe(Condition.visible);
        return this;
    }

}