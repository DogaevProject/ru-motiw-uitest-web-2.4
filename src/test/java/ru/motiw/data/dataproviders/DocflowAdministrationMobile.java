package ru.motiw.data.dataproviders;

import org.testng.annotations.DataProvider;
import ru.motiw.data.BaseTest;
import ru.motiw.mobile.model.FilesForAttachment;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.DocflowAdministration.DocumentRegistrationCards.DocRegisterCards;
import ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor.BlockOfRouteScheme;
import ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor.RouteSchemeEditor;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Document.ExecutionOfDocument;
import ru.motiw.web.model.Document.Resolution;
import ru.motiw.web.model.Tasks.Folder;

import static ru.motiw.data.dataproviders.Administration.getRandomDepartment;
import static ru.motiw.data.dataproviders.Administration.getRandomEmployer;
import static ru.motiw.data.dataproviders.Tasks.getRandomProject;
import static ru.motiw.mobile.model.Document.TypeOperationsOfDocument.*;
import static ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor.TypesBlockOfRouteScheme.ANY_BOSS;
import static ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor.TypesBlockOfRouteScheme.ANY_WORK_GROUP;


/**
 * Данные раздела - Администрирование ДО - для тестов документов в АРМ
 */
public abstract class DocflowAdministrationMobile extends BaseTest {

    // Инициализация объекта - Названия Файлов задачи
    private String[] file = new String[]{
            FilesForAttachment.FILE_1.getNameFile(),
            FilesForAttachment.FILE_2.getNameFile(),
            FilesForAttachment.FILE_3.getNameFile(),
    };

    //---------------------------------------------------------------------------------------------------------- Инициализируем объект - Подразделение и Пользователь
    private Department[] department = new Department[]{getRandomDepartment()};

    private Employee[] employee = new Employee[]{getRandomEmployer(), getRandomEmployer(), getRandomEmployer(), getRandomEmployer(), getRandomEmployer()};

    // Инициализация РКД и её настроек
    private DocRegisterCards registerCards = new DocRegisterCards("wD_Тестовая карточка " + randomString(20))
            .setCheckBoxUseAllPossibleRoutes(true); // Использовать все возможные маршруты

    private DocRegisterCards registerCards_2 = new DocRegisterCards("wD_Тестовая карточка " + randomString(20))
            .setCheckBoxUseAllPossibleRoutes(true); // Использовать все возможные маршруты


    //----------------------------------------------------------------------------------------------------------- Инициализация Документа
    private Document document = new Document()

            .setDocumentType(registerCards) // Тип документа
            .setAuthorOfDocument(EMPLOYEE_ADMIN)
            .setDateRegistration(randomDateTime()) // Дата регистрации
            .setProject(getRandomProject()) // Инициализируем проект документа
            .setValueFiles(new String[]{file[0], file[1]})
            .setRouteSchemeForDocument(new RouteSchemeEditor()
                    .setRouteScheme("Согласование входящей корреспонденции - Постановка задачи")
                    .setReviewDuration(randomInt(999))
                    .setUserRoute(new Employee[]{employee[0]}) // Добавляем в маршрут созданного пользователя
            )
            .setExecutionOfDocument(new ExecutionOfDocument[]
                    {
                            new ExecutionOfDocument(1, EMPLOYEE_ADMIN, CREATE_RESOLUTION),
                            new ExecutionOfDocument(2, employee[1], CLOSE_EXECUTION),
                            new ExecutionOfDocument(3, employee[2], CLOSE_EXECUTION),
                            new ExecutionOfDocument(4, EMPLOYEE_ADMIN, RETURN_TO_EXECUTION),
                            new ExecutionOfDocument(5, EMPLOYEE_ADMIN, CLOSE_EXECUTION)

                    } // Выполняемые операции в карточке документа
            )
            .setResolutionOfDocument(new Resolution[]{
                            (Resolution) new Resolution()
                                    .setTextOfResolution(randomString(20))
                                    .setAuthorDefault(EMPLOYEE_ADMIN)
                                    .setExecutiveManagers(new Employee[]{employee[1]}),
                            (Resolution) new Resolution()
                                    .setTextOfResolution(randomString(20))
                                    .setAuthorDefault(EMPLOYEE_ADMIN)
                                    .setExecutiveManagers(new Employee[]{employee[2]})

                    } // Резолюция
            );


    //----------------------------------------------------------------------------------------------------------- Инициализация Документа для согласования
    private Document documentForConsideration_1 = new Document()

            .setDocumentType(registerCards) // Тип документа
            .setAuthorOfDocument(EMPLOYEE_ADMIN)
            .setDateRegistration(randomDateTime()) // Дата регистрации
            .setProject(getRandomProject()) // Инициализируем проект документа
            .setValueFiles(new String[]{file[0]})
            .setRouteSchemeForDocument(new RouteSchemeEditor()
                    .setRouteScheme("Маршрут согласования договора")
                    .setReviewDuration(randomInt(999))
                    .setUserRouteWithCoupleBlocks(new BlockOfRouteScheme[]{
                            new BlockOfRouteScheme("Произвольная рабочая группа", new Employee[]{employee[0]}, ANY_WORK_GROUP),
                            new BlockOfRouteScheme("Произвольный начальник", employee[1], ANY_BOSS)
                    }) // Добавляем в маршрут созданного пользователя
            );

    private Document documentForConsideration_2 = new Document()

            .setDocumentType(registerCards_2) // Тип документа
            .setAuthorOfDocument(EMPLOYEE_ADMIN)
            .setDateRegistration(randomDateTime()) // Дата регистрации
            .setProject(getRandomProject()) // Инициализируем проект документа
            .setValueFiles(new String[]{file[0]})
            .setRouteSchemeForDocument(new RouteSchemeEditor()
                    .setRouteScheme("Маршрут согласования договора")
                    .setReviewDuration(randomInt(999))
                    .setUserRouteWithCoupleBlocks(new BlockOfRouteScheme[]{
                            new BlockOfRouteScheme("Произвольная рабочая группа", new Employee[]{employee[0]}, ANY_WORK_GROUP),
                            new BlockOfRouteScheme("Произвольный начальник", employee[1], ANY_BOSS)
                    }) // Добавляем в маршрут созданного пользователя
            );

    // ----------------------------------------------------------------------------------------------------------- Инициализация Папки
    private Folder[] folder = getRandomArrayFolders();

    /**
     * Метод создания полностью случайного массива объектов - "Папка"
     *
     * @return folders с атрибутами полей объекта - Папка
     */
    public static Folder[] getRandomArrayFolders() {
        return new Folder[]{
                new Folder()
                        .setNameFolder("wD_Box " + randomString(10)) // Зн-ие НЕ изменять - используется в проверке - checkDisplayCreateAFolderInTheGrid()
                        .setUseFilter(randomBoolean())
                        .setChooseRelativeValue(randomBoolean())
                        .setSharedFolder(randomBoolean())
                        .setAddSharedFolderForAll(randomBoolean())
                        .setAddSharedFolderForNewUsers(randomBoolean()),
        };
    }

    /**
     * Параметризация - Инициализируем модель - Объекты необходимые для  проверки создания документа
     *
     * @return массив параметров объектов системы
     */
    @DataProvider
    public Object[][] objectDataForVerifyingCreateDocument() {
        return new Object[][]{
                {
                        //переменная объекта - ПОДРАЗДЕЛЕНИЕ
                        department,
                        //переменная объекта - ПОЛЬЗОВАТЕЛЬ
                        employee,
                        //переменная объекта - РКД
                        registerCards,
                        //переменная объекта - ДОКУМЕНТ
                        document,
                        //переменная объекта - Папка
                        folder
                }
        };
    }


    /**
     * Параметризация - Инициализируем модель - Объекты необходимые для проверки согласования документа
     *
     * @return массив параметров объектов системы
     */
    @DataProvider
    public Object[][] objectDataForConsiderationDocument() {
        return new Object[][]{
                {
                        //переменная объекта - ПОДРАЗДЕЛЕНИЕ
                        department,
                        //переменная объекта - ПОЛЬЗОВАТЕЛЬ
                        employee,
                        //переменная объекта - РКД
                        registerCards,
                        registerCards_2,
                        //переменная объекта - ДОКУМЕНТ
                        documentForConsideration_1,
                        documentForConsideration_2,
                        //переменная объекта - Папка
                        folder
                }
        };
    }

}
