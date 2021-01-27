package ru.motiw.mobile.steps.Tasks;

import com.codeborne.selenide.CollectionCondition;
import org.openqa.selenium.By;
import ru.motiw.mobile.elements.Tasks.NewTaskFormElementsMobile;
import ru.motiw.mobile.elements.Tasks.TaskElementsMobile;
import ru.motiw.mobile.model.Task.InnerGroupTabs;
import ru.motiw.mobile.model.Task.TabsOfTask;
import ru.motiw.mobile.steps.CardStepsMobile;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

/**
 * Шаги в карточке задачи
 */
public class CardTaskStepsMobile extends CardStepsMobile {

    protected TaskElementsMobile taskElementsMobile = page(TaskElementsMobile.class);
    protected NewTaskFormElementsMobile newTaskFormElementsMobile = page(NewTaskFormElementsMobile.class);

    /*
     * Открытие/Закрытие закладки группы полей на вкладке "Описание"
     * */
    public void selectGroupTab(InnerGroupTabs nameOfGroup) {
        $(By.xpath("//div[contains(@id,'object') and not(contains(@class,\"x-hidden-display\"))]//div[contains(text(),'" + nameOfGroup.getNameOfGroupTab() + "')]//ancestor::div[contains(@class,\"x-unselectable x-paneltitle x-component\")]")).click();
    }


    /**
     * Переход между вкладками.
     * Проверка наличия кнопок на тулбаре меню после перехода на вкладку.
     * TODO  Проверка отображения подписей под кнопками и хинтов
     *
     * @param nameOfTabs Название вкладки на тулбаре
     */
    public CardTaskStepsMobile openTab(TabsOfTask nameOfTabs) {
        // Ожидание и проверка элементов меню тулбара задачи
        verifyMenuOfTask();
        switch (nameOfTabs.getNameTab()) {
            case "Файлы":
                //Переходим на вкладку "Файлы"
                taskElementsMobile.getButtonOfFilesTab().waitUntil(visible, 2000).click();

                //проверка на отображение
                taskElementsMobile.getButtonOfFinalizeExecution().shouldBe(visible);
                taskElementsMobile.getElementAmongButtonsOfMenu().shouldBe(visible);

                taskElementsMobile.getPressedButtonOfFiles().shouldBe(visible);
                taskElementsMobile.getButtonOfActionsTab().shouldBe(visible);
                taskElementsMobile.getButtonOfDescriptionTab().shouldBe(visible);

                //проверка на Неотображение
                taskElementsMobile.getButtonOfAddAction().shouldNotBe(visible);
                taskElementsMobile.getButtonOfSave().shouldNotBe(visible);
                break;
            case "Действия":
                //Переходим на вкладку "Действия"

                taskElementsMobile.getButtonOfActionsTab().waitUntil(visible, 2000).click();

                //проверка на отображение
                taskElementsMobile.getButtonOfAddAction().shouldBe(visible);
                taskElementsMobile.getButtonOfFinalizeExecution().shouldBe(visible);
                taskElementsMobile.getElementAmongButtonsOfMenu().shouldBe(visible);

                taskElementsMobile.getPressedButtonOfActions().shouldBe(visible);
                taskElementsMobile.getButtonOfFilesTab().shouldBe(visible);
                taskElementsMobile.getButtonOfDescriptionTab().shouldBe(visible);

                //проверка на Неотображение
                taskElementsMobile.getButtonOfSave().shouldNotBe(visible);
                break;
            case "Описание":

                //Переходим на вкладку "Описание"
                taskElementsMobile.getButtonOfDescriptionTab().waitUntil(visible, 2000).click();

                //проверка на отображение
                taskElementsMobile.getButtonOfSave().shouldBe(visible);
                taskElementsMobile.getButtonOfFinalizeExecution().shouldBe(visible);
                taskElementsMobile.getElementAmongButtonsOfMenu().shouldBe(visible);

                taskElementsMobile.getPressedButtonOfDescription().waitUntil(visible, 2000);
                taskElementsMobile.getButtonOfFilesTab().shouldBe(visible);
                taskElementsMobile.getButtonOfActionsTab().shouldBe(visible);

                //проверка на Неотображение
                taskElementsMobile.getButtonOfAddAction().shouldNotBe(visible);
                break;
            default:
                throw new IllegalArgumentException("Неверное название вкладки:" + nameOfTabs.getNameTab());
        }
        return this;
    }

    // Ожидание и проверка элементов меню тулбара задачи
    void verifyMenuOfTask() {
        $(formElementsMobile.getToolbarOfMenu()).waitUntil(visible, 50000);
        taskElementsMobile.getMenuOfTask().shouldHaveSize(11); // 11 элементов - это вместе со скрытыми элементами.
    }


    // Ожидание и проверка элементов в форме задачи на вкладке Описание
    public void verifyElementsOnDescriptionPage() {
        newTaskFormElementsMobile.getCollectionElementsFormOfTask().shouldHave(CollectionCondition.size(7), 10000);
    }

}
