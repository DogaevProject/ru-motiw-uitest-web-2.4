package ru.motiw.web.steps.Administration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.motiw.web.elements.elementsweb.DocumantionTestElements;
import ru.motiw.web.steps.BaseSteps;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static ru.motiw.utils.WindowsUtil.newWindowForm;
import static ru.motiw.web.model.URLMenu.MANUALS;

/**
 * Руководства
 */
public class DocumentationTestSteps extends BaseSteps {

    private DocumantionTestElements documentationTestElements = page(DocumantionTestElements.class);


    /*
     *  Проверка Загрузки pdf-страницы
     * */
    private void ensurePageLoaded() {
        documentationTestElements.getPdfPage().shouldBe(Condition.visible);
    }


    /*
     *  Проверка Загрузки html-страницы
     * */
    private void ensureHtmlPageLoaded() {
        documentationTestElements.getHtmlPage().shouldBe(Condition.visible);
    }


    /**
     * раздел: Справка - Руководства
     */
    public static DocumentationTestSteps goToURLManuals() {
        openSectionOnURL(MANUALS.getMenuURL());
        return page(DocumentationTestSteps.class);
    }


    /*
     *  Открываем руководство, Проверяем Загрузку pdf-страницы, Переход в раздел "Руководства"
     * */
    private void checkPdfManuals() {
        sleep(2000);
        //проверяем кол-во ссылок на pdf-мануалы
        documentationTestElements.getAllReferenceToPdfManual().shouldHaveSize(13);
        String parentWindowHandler = getWebDriver().getWindowHandle(); //Запоминаем окно в котором находимся
        //все ссылки на pdf добавляем в список
        List<SelenideElement> elements = new ArrayList<>($$(documentationTestElements.getAllReferenceToPdfManual()));

        for (SelenideElement link : elements) {
            link.click();
            getWebDriver().switchTo().window(new WebDriverWait(getWebDriver(), 10).until(newWindowForm(By.xpath("//embed[@type='application/pdf']"))));
            ensurePageLoaded();
            closeWindow();
            getWebDriver().switchTo().window(parentWindowHandler); // Возращаемся в окно страницы /user/tab/user/manuals
            switchTo().frame($(By.cssSelector("#flow"))); //Переходим во фрейм на странице /user/tab/user/manuals
        }
    }


    private void checkHtmlManuals() {
        //проверяем кол-во ссылок на html-руководства
        documentationTestElements.getAllReferenceToHtmlManual().shouldHaveSize(14);
        String parentWindowHandler = getWebDriver().getWindowHandle(); //Запоминаем окно в котором находимся
        //все ссылки на html добавляем в список
        List<SelenideElement> elements = new ArrayList<>($$(documentationTestElements.getAllReferenceToHtmlManual()));

        for (SelenideElement link : elements) {
            link.click();
            ArrayList<String> tabs = new ArrayList<>(getWebDriver().getWindowHandles()); //все открытые окна сохраняем в список
            switchTo().window(tabs.get(1)); //переходим во второе окно\вкладку

            {
                if (isElementPresent(By.xpath("//frame[@name='hmcontent']"))) {
                    getFrameObject($(documentationTestElements.getFrameHtml())); //  уходим во фрейм hmcontent
                    ensureHtmlPageLoaded();//Проверяем Загрузку html-страницы
                    //checkTextOnHtmlPage();   // Проверяем наличе текста, который отображается на страницах html-мануала
                }
                ensureHtmlPageLoaded(); // на страницах "Термины и определения" и "Описание таблиц базы данных" сразу проверяем загрузку, без перехода во фрейм (его там нет)
            }
            closeWindow();
            getWebDriver().switchTo().window(parentWindowHandler); // Возращаемся в окно страницы /user/tab/user/manuals
            switchTo().frame($(By.cssSelector("#flow"))); //Переходим во фрейм на странице /user/tab/user/manuals
        }
    }


    public void checkManuals() {
        checkPdfManuals();
        checkHtmlManuals();
    }


}