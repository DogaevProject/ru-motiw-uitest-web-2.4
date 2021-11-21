package ru.motiw.web.steps;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.Collection;

import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public abstract class BaseSteps {

    private static final String http = "http://";
    public static final String PDA_PAGE_URL = http + "pda.motiwtest4.test.lan";
    public static final String MOBILE_PAGE_URL = baseUrl + "/m";

    @FindBy(xpath = "//ul[@class='x-list-plain']/../ancestor::div[not(contains(@style,'display: none'))]//li")
    private ElementsCollection collectionOfListItems;

    /**
     * Закрываем окно
     */
    protected void closeWindow() {
        getWebDriver().close();
    }

    /**
     * Переход во фрейм объекта системы для взаимодействия
     *
     * @param frameObject локатор frame
     */
    protected void getFrameObject(SelenideElement frameObject) {
        switchTo().frame(frameObject);
    }

    /**
     * Проверяем отображения текста в диалоге (alert) и взаиможействуем с объектом, если Сообщение истенно - взаимодействуем
     * -подтверждаем удаление, отменяем удаление, подтверждаем сохранение
     *
     * @param element             getText() из диалогового окна, собственно, сообщение к-е показывается
     * @param expectedMessageText проверяемое сообщение
     * @param webElementButton    подтверждение (взаимодействие над объектом)
     */
    protected String checkingMessagesConfirmationOfTheObject(SelenideElement element, String expectedMessageText,
                                                             SelenideElement webElementButton) {
        String actualMessageText = element.shouldBe(Condition.exactText(expectedMessageText)).getText();
        if (expectedMessageText != null && expectedMessageText.equals(actualMessageText)) {
            webElementButton.click();
            return expectedMessageText;
        }
        return null;
    }

    /**
     * Проверяем кол-во вкладок в форме редактирования объекта и их имена (отображение)
     *
     * @param tabsLocator     locator element tabs
     * @param numberOfTabs    передаваемое кол-во вкладок в форме редактирования объекта
     * @param tabsNameLocator locator element name tabs
     * @param tabNames        передаваемое имя вкладок для проверки отображение в форме
     */
    protected static void checkDisplayedTabsInTheShapeOfAnObject(By tabsLocator, int numberOfTabs, By tabsNameLocator, String[] tabNames) {
        $$(tabsLocator).shouldBe(CollectionCondition.size(numberOfTabs)); // проверка отображения кол-во вкладок
        $$(tabsNameLocator).shouldHave(CollectionCondition.exactTexts(tabNames));
    }

    /**
     * Проверка созданного объекта в гриде
     *
     * @param objectName имя передаваемого объекта
     */
    protected void checkOutTheObjectCreation(String objectName) {
        SelenideElement element = $(By.xpath("//*[text()='" + objectName + "'][ancestor::table]"))
                .waitUntil(Condition.visible, 10000);
        element.scrollTo();
    }

    /**
     * Выбор объекта в гриде
     *
     * @param objectName имя передаваемого объекта
     */
    protected void clickTheObjectInGrid(String objectName) {
        SelenideElement element = $(By.xpath("//*[text()='" + objectName + "'][ancestor::table]"))
                .waitUntil(Condition.visible, 10000);
        element.scrollTo();
        $(By.xpath("//*[text()='" + objectName + "'][ancestor::table]")).click();
    }

    /**
     * Заполнение текстового поля, с предварительной очисткой
     * <input type="text">
     *
     * @param input     идентификация поля по элементу в DOM
     * @param textField передаваемое значение текста
     */
    protected void inputField(SelenideElement input, String textField) {
        input.setValue(textField);
        assertThat(input.getValue(), is(equalTo("" + textField + "")));
    }

    /**
     * Коллекция значений настроек полей
     *
     * @return коллекция элементов настроек для полей
     */
    protected ElementsCollection getCollectionOfListItems() {
        return collectionOfListItems;
    }

    /**
     * Получить разницу между двумя списками строк (ArrayLists)
     *
     * @param shouldBeList Список строк, которые должны отображаться
     * @param currentList  Список строк, которые действительно отображаются в интерфейсе
     * @return
     */
    protected String getReportOfDifferenceBetweenTwoArrayLists(ArrayList<String> shouldBeList, ArrayList<String> currentList) {

        Collection shouldBeCollection = new ArrayList(shouldBeList); //  Список строк, которые должны отображаться
        Collection currentCollection = new ArrayList(currentList);  //  Список строк, которые отображаются в интерфейсе

        Collection ShouldBeButNotFound = new ArrayList(shouldBeList); //  Список строк, которые должны отображаться, но не отображаются
        Collection stringThatNotShouldBeButCurrentlyFind = new ArrayList(currentList); //  Список строк, которые не должны отображаться, но отображаются

        // Получаем разницу между строками, которые должны отображаться и действительно отображаемыми в данный момент
        ShouldBeButNotFound.removeAll(currentCollection);
        stringThatNotShouldBeButCurrentlyFind.removeAll(shouldBeCollection);


        return String.valueOf(" Список элементов, которые присутствуют - Current list: " + currentCollection + "\n"
                + " Список элементов, которых не должно быть, но они есть  - Wrong Values in Current list: " + stringThatNotShouldBeButCurrentlyFind + "\n"
                + " Список элементов, которые должны быть, но не их нет - ShouldBe But Not Found: " + ShouldBeButNotFound + "\n"
                + " Ожидаемый список элементов - Full exp list: " + shouldBeList);
    }


    /**
     * Наличия элемента
     *
     * @param locator передаваемый локатор элемента для представления
     */
    public static boolean isElementPresent(By locator) {
        try {
            sleep(300);
            getWebDriver().findElement(locator);
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        } catch (StaleElementReferenceException ignored) {
            return false;
        }
    }


    /**
     * Проверка значений в чебоксах для последующего сравнения с передаваемым значением методом assertTrue
     */
    public boolean verifyCheckboxIsSelected(boolean state, SelenideElement inputCheckbox) {
        if (state) {
            inputCheckbox.shouldBe(selected);
            return true;
        } else {
            inputCheckbox.shouldNotBe(selected);
            return true;
        }
    }


    public boolean isElementVisible(By by) {
        try {
            getWebDriver().findElement(by).isDisplayed();
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        } catch (ElementNotVisibleException ignored) {
            return false;
        } catch (StaleElementReferenceException ignored) {
            return false;
        }
    }

    public boolean isElementTextPresent(By by, String text) {
        try {
            if (getWebDriver().findElement(by).getText().equals(text)) {
                return true;
            }
            return false;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Реализация переход по разделам системы напрямую по точному URL
     *
     * @param URL передаваемая ссылка для навигации по меню
     */
    public static void openSectionOnURL(String URL) {
        switchTo().defaultContent();
        open(URL);
        assertEquals(baseUrl + URL, getWebDriver().getCurrentUrl());
        switchTo().frame($(By.cssSelector("#flow")));
    }

    /**
     * Развернем все ветви объекта
     *
     * @param knot  локатор узла если есть как таковой
     * @param nodes локатор элемента для взаиммодействия
     */
    protected void unwrapAllNodes(SelenideElement knot, By nodes) {
        try {
            while (isElementPresent(nodes)) {
                if (isElementPresent(nodes)) {
                    knot.click();
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Очищаем поле через Ctrl+a+BACK_SPACE
     *
     */
    public void clearTextInInputViaHotKeys(SelenideElement element) {
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
    }

}
