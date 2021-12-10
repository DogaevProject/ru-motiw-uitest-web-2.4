package ru.motiw.data.dataproviders;

import org.testng.annotations.DataProvider;
import ru.motiw.data.BaseTest;
import ru.motiw.web.model.Administration.Directories.Directories;
import ru.motiw.web.model.Administration.Directories.DirectoriesField;
import ru.motiw.web.model.Administration.FieldsObject.*;
import ru.motiw.web.model.Administration.TaskTypeListEditObject;
import ru.motiw.web.model.Administration.TasksTypes.ComputeModeNumerator;
import ru.motiw.web.model.Administration.TasksTypes.ObligatoryField;
import ru.motiw.web.model.Administration.TasksTypes.SettingsForTaskTypeListFields;
import ru.motiw.web.model.Administration.TasksTypes.TasksTypes;
import ru.motiw.web.model.Administration.TypesOfTables.TypesOfTables;
import ru.motiw.web.model.Administration.Users.*;
import ru.motiw.web.model.Administration.Users.Module;
import ru.motiw.web.model.CorrectionMethod;
import ru.motiw.web.model.OpenFilesForEdit;
import ru.motiw.web.model.ShiftDirection;

import java.util.ArrayList;

import static ru.motiw.web.model.Administration.TasksTypes.ObligatoryField.OBLIGATORY_ON_CREATE;
import static ru.motiw.web.model.Administration.TasksTypes.SettingsForTaskTypeListFields.NO;
import static ru.motiw.web.model.Administration.TasksTypes.SettingsForTaskTypeListFields.YES;

/**
 * Данные раздела - Администрирование
 */
public abstract class Administration extends BaseTest {

    /**
     * Метод создания полностью случайного объекта - "Подразделение"
     */
    public static Department getRandomDepartment() {
        return new Department()
                .setDepartmentName(randomString(20))
                .setConditionalNumber((randomString(20)))
                .setCounter((randomInt(2147483647)))
                .setResetDate(randomDateTime())
                .setNumeratorTemplate("{counter}-{department}-" + " "
                        + randomString(20));
    }

    /**
     * Метод создания полностью случайного объекта - "Пользователь"
     */
    public static Employee getRandomEmployer() {
        String pass = randomString(10);
        String newPass = randomString(10);
        return new Employee()
                .setLastName(randomString(10)).setName(randomString(10)).setPatronymic(randomString(10)) // ФИО
                .setSex(randomEnum(Sex.class))
                .setBirthDate(randomDate())
                .setJobTitle(randomString(20))
                .setLoginName(randomString(10))
                .setPassword(pass).setСonfirmationPassword(pass)
                .setNewPassword(newPass).setNewСonfirmationPassword(newPass)
                .setAdditionalNumber(randomInt(100))
                .setUserForcedSorting(randomInt(100)).setStatus(randomEnum(Status.class))
                .setNeedsPasswordChange(false);
    }

    // Пользователи
    public static ArrayList<Employee> employee = new ArrayList<>();

    public int sizeEmployee() {
        return employee.size();
    }

    /**
     * Инициализация объекта - Пользователь
     *
     * @return коллекция объект - Пользователь
     */
    public static ArrayList<Employee> getEmployees() {
        ArrayList<Employee> employees = employee;
        if (employee.isEmpty()) {
            Employee employee1 = getRandomEmployer().setLastName("пользователь0 " + randomString(5));
            Employee employee2 = getRandomEmployer().setLastName("пользователь1 " + randomString(5));

            employees.add(employee1);
            employees.add(employee2);
        }

        return employees;

    }

    // Подразделение
    public static ArrayList<Department> department = new ArrayList<>();

    public int sizeDepartment() {
        return department.size();
    }

    /**
     * Инициализация объекта - Подразделение
     *
     * @return коллекция объект - Подразделение
     */
    public static ArrayList<Department> getDepartment() {
        ArrayList<Department> departments = department;
        if (department.isEmpty()) {
            Department department1 = getRandomDepartment();
            departments.add(department1);
        }

        return departments;

    }

    /**
     * Параметризация - Инициализируем модель - Справочник (со всеми надстройками)
     */
    @DataProvider
    public Object[][] objectDataDirectories() {

        // 1. СТРОКА (Выбор из списка == Да; Уникальное; Обязательное)
        FieldObject fieldStringIsListChoice = new TypeListFieldsString()
                .setFieldName("Строка (Выбор из списка == Да; Обяз.) " + randomString(10))
                .setFieldID("STRING" + randomIdentifier(5))
                .setObligatory(ObligatoryField.YES) // Обязательное поле
                .setIsUniqueField(YES) // Уникальное
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(SettingsForTaskTypeListFields.YES) // Выбор из списка
                        .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10))); // Список значений

        // 1.1. СТРОКА (Выбор из списка == Нет)
        FieldObject fieldStringIsNotListChoice = new TypeListFieldsString()
                .setFieldName("Строка (Выбор из списка == Нет) " + randomString(10))
                .setFieldID("STRNOTLIST" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(NO) // Выбор из списка
                        .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10))); // Список значений

        // 2. ТЕКСТ
        FieldObject fieldText = new TypeListFieldsText()
                .setFieldName("Текст " + randomString(10))
                .setFieldID("TEXT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsText());

        // 3. ЦЕЛОЕ
        FieldObject fieldInt = new TypeListFieldsInteger()
                .setFieldName("Целое " + randomString(10))
                .setFieldID("INTEGER" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsInteger());

        // 4. ВЕЩЕСТВЕННОЕ
        FieldObject fieldFloat = new TypeListFieldsDouble()
                .setFieldName("Вещественное " + randomString(10))
                .setFieldID("FLOAT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDouble());

        // 5. ДАТА
        FieldObject fieldDate = new TypeListFieldsDate()
                .setFieldName("Дата " + randomString(10))
                .setFieldID("DATE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDate());

        // 6. ФАЙЛ (Редактирование == Да)
        FieldObject fieldFileEdit = new TypeListFieldsFile()
                .setFieldName("Файл (Редактирование == Да) " + randomString(10))
                .setFieldID("FILEDIT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsFile()
                        .setOpenFilesForEdit(OpenFilesForEdit.YES)); // Редактирование файлов - Да

        // 6.1. ФАЙЛ (Редактирование == Нет)
        FieldObject fieldFile = new TypeListFieldsFile()
                .setFieldName("Файл (Редактирование == Нет) " + randomString(10))
                .setFieldID("FILE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsFile()
                        .setOpenFilesForEdit(OpenFilesForEdit.NO)); // Редактирование файлов - Нет

        // 7. ССЫЛКА НА СПР-К
        FieldObject fieldDirectory = new TypeListFieldsDirectory()
                .setFieldName("Ссылка на справочник " + randomString(10))
                .setFieldID("DIRECTORY" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDirectory()
                        .setDirectoryName("Адресная книга")
                        .setNameDirectoryField("Фамилия"));

        // 8. МНОЖЕСТВЕННАЯ ССЫЛКА НА СПР-К
        FieldObject fieldMultiDirectory = new TypeListFieldsMultiDirectory()
                .setFieldName("Множественная ссылка на справочник " + randomString(5))
                .setFieldID("DIRMULTI" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsMultiDirectory()
                        .setDirectoryName("Банк")
                        .setNameDirectoryField("Название"));

        // 9. ЛОГИЧЕСКИЙ
        FieldObject fieldBoolean = new TypeListFieldsBoolean()
                .setFieldName("Логический " + randomString(5))
                .setFieldID("BOOLEAN" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsBoolean());

        // 10. ТЕЛЕФОН
        FieldObject fieldPhone = new TypeListFieldsPhone()
                .setFieldName("Телефон " + randomString(5))
                .setFieldID("PHONE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsPhone());

        // 11. EMAIL
        FieldObject fieldEmail = new TypeListFieldsEmail()
                .setFieldName("Email " + randomString(5))
                .setFieldID("EMAIL" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsEmail());

        // 12. ИЗОБРАЖЕНИЕ
        FieldObject fieldImage = new TypeListFieldsImage()
                .setFieldName("Изображение " + randomString(5))
                .setFieldID("IMAGE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsImage());

        // 13. ЦВЕТ
        FieldObject fieldColor = new TypeListFieldsColor()
                .setFieldName("Цвет " + randomString(5))
                .setFieldID("COLOR" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsColor())
                .setIsUniqueField(YES); // Уникальное

        // 14. ВЛОЖЕННЫЙ СПРАВОЧНИК
        FieldObject fieldEnclosedDirectory = new TypeListFieldsEnclosedDirectory()
                .setFieldName("Вложенный спр-к " + randomString(5))
                .setFieldID("ENCLOSEDDIRECTORY" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsEnclosedDirectory()
                        .setDirectoryName("Банк"));

        // 15. ПОДРАЗДЕЛЕНИЕ
        FieldObject fieldDepartment = new TypeListFieldsDepartment()
                .setFieldName("Подразделение " + randomString(5))
                .setFieldID("DEPARTMENT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDepartment());

        // Инициализируем объект - Справочник
        TaskTypeListEditObject directories = new Directories("wD_Справочник " + randomString(10))

                // Вкладка - Настройки
                .setShareRecords(true) // Общедоступность записей
                .setAccessToRecords(true) // Настройка доступа к записям
                .setMappingDevice(true) // Способ отображения - Линейный ли? true - да; false - иерархический
                .setSearchSettings(true) // true - поиск записей через SOLR; false - поиск записей через БД
                .setTaskTypeListObjectFields(new FieldObject[]{fieldStringIsListChoice, fieldStringIsNotListChoice, fieldText, fieldInt, fieldFloat, fieldDate, fieldFileEdit,
                        fieldFile, fieldDirectory, fieldMultiDirectory, fieldBoolean, fieldPhone, fieldEmail, fieldImage, fieldColor, fieldEnclosedDirectory, fieldDepartment});

        return new Object[][]{

                {
                        directories,
                }
        };

    }

    /**
     * Параметризация - Инициализируем модель - Рег.карточка Справочника и Справочник (со всеми надстройками)
     */
    @DataProvider
    public Object[][] objectRandomDataDirectories() {

        // 1. СТРОКА (Выбор из списка == Нет)
        FieldObject fieldStringIsNotListChoice = new TypeListFieldsString()
                .setFieldName("Строка (Выбор из списка == Нет) " + randomString(10))
                .setFieldID("STRNOTLIST" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(NO)); // Выбор из списка




        // Инициализируем объект - Регистрационная карточка Справочника
        TaskTypeListEditObject registerCardDirectories = new Directories("wD_Справочник " + randomString(10))

                // Вкладка - Настройки
                .setShareRecords(true) // Общедоступность записей
                .setAccessToRecords(true) // Настройка доступа к записям
                .setMappingDevice(true) // Способ отображения - Линейный ли? true - да; false - иерархический
                .setSearchSettings(true) // true - поиск записей через SOLR; false - поиск записей через БД
                .setTaskTypeListObjectFields(new FieldObject[]{fieldStringIsNotListChoice});

        //--------------------------------------------------------------------------------------------------Инициализируем объекты - Записи Справочника
        FieldObject recordOneDirectory = new DirectoriesField()

                // Осуществляем заполнение (наполнение) полей справочника через массив
                .setDirectoryItems(randomString(11))
                .setFieldName(fieldStringIsNotListChoice.getFieldName())
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(NO)); // Выбор из списка;

        FieldObject recordTwoDirectory = new DirectoriesField()

                // Осуществляем заполнение (наполнение) полей справочника через массив
                .setDirectoryItems(randomString(11))
                .setFieldName(fieldStringIsNotListChoice.getFieldName())
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(NO)); // Выбор из списка;


        // Инициализируем объект - Справочник
        Directories directory = new Directories(registerCardDirectories.getObjectTypeName())

                // Осуществляем заполнение (наполнение) полей справочника через массив
                .setDirectoriesFields(new DirectoriesField[] {
                        (DirectoriesField) recordOneDirectory,
                        (DirectoriesField) recordTwoDirectory
                });

        return new Object[][]{

                {
                        registerCardDirectories,
                        directory
                }
        };

    }

    /**
     * Параметризация - Инициализируем модель - Типы задач (со всеми надстройками)
     */
    @DataProvider
    public Object[][] objectDataTasksTypes() {

        //------------------------------------------------------------------------Инициализация полей и настроек объекта - СПРАВОЧНИКИ
        // 1. СТРОКА (Выбор из списка == Да; Уникальное; Обязательное)
        FieldObject fieldStringIsListChoiceDirectories = new TypeListFieldsString()
                .setFieldName("Строка (Выбор из списка == Да; Обяз.) " + randomString(10))
                .setFieldID("STRING" + randomIdentifier(5))
                .setObligatory(ObligatoryField.YES) // Обязательное поле
                .setIsUniqueField(SettingsForTaskTypeListFields.YES) // Уникальное
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(SettingsForTaskTypeListFields.YES) // Выбор из списка
                        .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10))); // Список значений

        // 2. ТЕКСТ
        FieldObject fieldTextDirectory = new TypeListFieldsText()
                .setFieldName("Текст " + randomString(10))
                .setFieldID("TEXT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsText());

        // 3. ЦЕЛОЕ
        FieldObject fieldIntDirectory = new TypeListFieldsInteger()
                .setFieldName("Целое " + randomString(10))
                .setFieldID("INTEGER" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsInteger());

        // Инициализируем объект - Справочник
        TaskTypeListEditObject directories = new Directories("wD_Directories_for_TasksTypes " + randomString(10))

                // Вкладка - Настройки
                .setShareRecords(true) // Общедоступность записей
                .setAccessToRecords(true) // Настройка доступа к записям
                .setMappingDevice(true) // Способ отображения - Линейный ли? true - да; false - иерархический
                .setSearchSettings(true) // true - поиск записей через SOLR; false - поиск записей через БД
                .setTaskTypeListObjectFields(new FieldObject[]{fieldStringIsListChoiceDirectories, fieldTextDirectory, fieldIntDirectory});


        //--------------------------------------------------------------------------Инициализация полей и настроек объекта - ТИПЫ ТАБЛИЦ

        // 1. СТРОКА (Выбор из списка == Да; Обязательное)
        FieldObject fieldStringTypesTable = new TypeListFieldsString()
                .setFieldName("Строка (Выбор из списка == Да) " + randomString(10))
                .setFieldID("STRING" + randomIdentifier(5))
                .setObligatory(ObligatoryField.YES) // Обязательное поле
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(YES) // Выбор из списка
                        .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10))); // Список значений

        // 2. ТЕКСТ
        FieldObject fieldTextTypesTable = new TypeListFieldsText()
                .setFieldName("Текст " + randomString(10))
                .setFieldID("TEXT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsText());

        // 3. ЦЕЛОЕ (Ссылка на объект == Задача)
        FieldObject fieldIntLinkObjectTypesTable = new TypeListFieldsInteger()
                .setFieldName("Целое (Ссылка на объект == Задача) " + randomString(10))
                .setFieldID("INTEGER" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsInteger()
                        .setObjectLink(YES)); // Ссылка на объект


        // Инициализация объекта - Типы таблиц
        TaskTypeListEditObject typesOfTables = new TypesOfTables("wD_TypesOfTables_for_TasksTypes " + randomString(10))
                .setTaskTypeListObjectFields(new FieldObject[]{fieldStringTypesTable, fieldTextTypesTable, fieldIntLinkObjectTypesTable});


        //---------------------------------------------------------------------------------------------------------Инициализация полей и объекта - ТИПЫ ЗАДАЧ

        // 1. СТРОКА (Выбор из списка == Да)
        FieldObject fieldStringIsListChoice = new TypeListFieldsString()
                .setFieldName("Строка (Выбор из списка == Да; Обязательное) " + randomString(5))
                .setFieldID("STRING" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(YES) // Выбор из списка
                        .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10))) // Список значений
                .setObligatory(ObligatoryField.OBLIGATORY_ON_CREATE) // Обязательное при создании
                .setIsHideInSearch(true);

        // 1.1. СТРОКА (Выбор из списка == Нет; Обязательное при завершении)
        FieldObject fieldStringIsNotListChoice = new TypeListFieldsString()
                .setFieldName("Строка (Выбор из списка == Нет; Обяз. при завершении) " + randomString(5))
                .setFieldID("STRNOTLIST" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(NO) // Выбор из списка
                        .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10))) // Список значений
                .setObligatory(ObligatoryField.OBLIGATORY_ON_CLOSE); // Обязательное при завершении

        // 2. ТЕКСТ (Скрывать при поиске)
        FieldObject fieldText = new TypeListFieldsText()
                .setFieldName("Текст " + randomString(10))
                .setFieldID("TEXT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsText())
                .setIsHideInSearch(true); // Скрывать при поиске

        // 3. ЦЕЛОЕ
        FieldObject fieldInt = new TypeListFieldsInteger()
                .setFieldName("Целое " + randomString(10))
                .setFieldID("INTEGER" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsInteger());

        // 4. ВЕЩЕСТВЕННОЕ
        FieldObject fieldFloat = new TypeListFieldsDouble()
                .setFieldName("Вещественное " + randomString(10))
                .setFieldID("FLOAT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDouble());

        // 5. ДАТА
        FieldObject fieldDate = new TypeListFieldsDate()
                .setFieldName("Дата " + randomString(10))
                .setFieldID("DATE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDate());

        // 6. ССЫЛКА НА СПР-К
        FieldObject fieldDirectory = new TypeListFieldsDirectory()
                .setFieldName("Ссылка на справочник " + randomString(10))
                .setFieldID("DIRECTORY" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDirectory()
                        .setDirectoryName(directories.getObjectTypeName()) // Выбираем проинициализированный объект - Справочник
                        .setFieldDirectory(fieldStringIsListChoiceDirectories)  // Выбираем проинициализированный объект - поле Справочника
                        .setDisplayNameTemplate("{STRING}-обычный текст-" + "{" + randomIdentifier(5) + "}"));

        // 7. МНОЖЕСТВЕННАЯ ССЫЛКА НА СПР-К
        FieldObject fieldMultiDirectory = new TypeListFieldsMultiDirectory()
                .setFieldName("Множественная ссылка на справочник " + randomString(5))
                .setFieldID("DIRMULTI" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsMultiDirectory()
                        .setDirectoryName("Банк")
                        .setNameDirectoryField("Адрес")
                        .setDisplayNameTemplate("{STRING}-обычный текст-" + "{" + randomIdentifier(5) + "}"));

        // 8. ЛОГИЧЕСКИЙ
        FieldObject fieldBoolean = new TypeListFieldsBoolean()
                .setFieldName("Логический " + randomString(5))
                .setFieldID("BOOLEAN" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsBoolean());

        // 9. ССЫЛКА НА БИБЛИОТЕКУ
        FieldObject libraryLink = new TypeListFieldsLibraryLink()
                .setFieldName("Ссылка на библиотеку " + randomString(5))
                .setFieldID("LIBLINK" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsLibraryLink());

        // 10. ССЫЛКА НА ЗАДАЧУ
        FieldObject fieldReferenceToTheTask = new TypeListFieldsReferenceTask()
                .setFieldName("Ссылка на задачу " + randomString(5))
                .setFieldID("REFTHETASK" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsReferenceTask());

        // 11. НУМЕРАТОР
        FieldObject fieldNumerator = new TypeListFieldsNumerator()
                .setFieldName("Нумератор " + randomString(5))
                .setFieldID("NUMERATOR" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsNumerator()
                        .setNumeratorTemplate("{counter}-{" + fieldStringIsNotListChoice.getFieldID() + "}-{DD}.{YYYY} "
                                + randomString(15)) // Шаблон нумератора
                        .setComputeMode(ComputeModeNumerator.WHEN_CREATING_TASK)); // Режим вычисления - При создании задачи

        // 12. ССЫЛКА НА ОБЪЕКТ
        FieldObject fieldObjectLink = new TypeListFieldsLink()
                .setFieldName("Ссылка на объект " + randomString(5))
                .setFieldID("OBJECTLINK" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsLink());

        // 13. ТАБЛИЦА
        FieldObject fieldTable = new TypeListFieldsTable()
                .setFieldName("Таблица " + randomString(5))
                .setFieldID("TABLE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsTable()
                        .setTypesOfTables(typesOfTables.getObjectTypeName())
                        .setFieldTable(fieldStringTypesTable)
                        .setNumeratorTemplate("{STRING}-обычный текст-" + "{" + randomIdentifier(5) + "}")); // Шаблон отображения

        // 14. ПОДРАЗДЕЛЕНИЕ
        FieldObject fieldDepartment = new TypeListFieldsDepartment()
                .setFieldName("Подразделение " + randomString(5))
                .setFieldID("DEPARTMENT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDepartment());

        // 15. СОТРУДНИК
        FieldObject fieldEmployee = new TypeListFieldsEmployee()
                .setFieldName("Сотрудник " + randomString(5))
                .setFieldID("EMPLOYEE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsEmployee());


        // Инициализация объекта - Типы задач с настройками
        TaskTypeListEditObject tasksTypes = new TasksTypes("wD_Тип задачи " + randomString(20))

                // Направление смещения даты при попадании на нерабочее время
                .setTaskTypeShiftDirection(ShiftDirection.DATE_MOVES_BACK) // Дата сдвигается назад

                // Корректировка даты
                .setTaskTypeCorrectionMethod(CorrectionMethod.SET_TIME)

                .setIsTaskTypeChangeDisabled(true) // Запретить изменение типа для созданной задачи
                .setOnlyTheSameTypeIWG(true) // Создавать подзадачи ИРГ только родительского типа
                .setIsCloseTaskWithNotReadyCheckpointsDisabled(true) // Запретить закрытие задач с неготовыми контрольными точками
                .setOpenFilesForEdit(OpenFilesForEdit.YES) // Открывать файлы для редактирования
                // Прикреплять файлы:
                .setIsAttachFilesToActionLine(false) // Лента действий (true - есть сигнал, значит производим клик - снимаем настройку; false - оставляем без изменения)
                .setIsAttachFilesToDescription(false) // Описание

                // Поля типа
                .setTaskTypeListObjectFields(new FieldObject[]
                        {fieldStringIsListChoice, fieldStringIsNotListChoice, fieldText, fieldInt,
                                fieldFloat, fieldDate, fieldDirectory, fieldMultiDirectory, fieldBoolean, libraryLink, fieldReferenceToTheTask,
                                fieldNumerator, fieldObjectLink, fieldTable, fieldDepartment, fieldEmployee});

        return new Object[][]{

                {
                        // Справочник
                        directories,
                        // Типы таблиц
                        typesOfTables,
                        // Типы задач
                        tasksTypes,
                }
        };


    }

    //---Администрирование----------------------------------------------------------
    //-----Типы таблиц----------------------------------------------------------

    /**
     * Параметризация - Инициализируем модель - Типы таблиц (со всеми надстройками)
     */
    @DataProvider
    public Object[][] objectDataTypesOfTable() {

        //---------------------------------------------------------------------------------------------------------Инициализация полей и объекта - СПРАВОЧНИКИ

        // 1. СТРОКА (Выбор из списка == Да; Уникальное; Обязательное)
        FieldObject fieldStringIsListChoiceDirectories = new TypeListFieldsString()
                .setFieldName("Строка (Выбор из списка == Да; Обяз.) " + randomString(10))
                .setFieldID("STRING" + randomIdentifier(5))
                .setObligatory(ObligatoryField.YES) // Обязательное поле
                .setIsUniqueField(SettingsForTaskTypeListFields.YES) // Уникальное
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(SettingsForTaskTypeListFields.YES) // Выбор из списка
                        .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10))); // Список значений

        // 2. ТЕКСТ
        FieldObject fieldTextDirectories = new TypeListFieldsText()
                .setFieldName("Текст " + randomString(10))
                .setFieldID("TEXT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsText());

        // 3. ЦЕЛОЕ
        FieldObject fieldIntDirectories = new TypeListFieldsInteger()
                .setFieldName("Целое " + randomString(10))
                .setFieldID("INTEGER" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsInteger());

        // Инициализируем объект - Справочник
        TaskTypeListEditObject directories = new Directories("wD_Directories_for_TypesOfTable " + randomString(10))

                // Вкладка - Настройки
                .setShareRecords(true) // Общедоступность записей
                .setAccessToRecords(true) // Настройка доступа к записям
                .setMappingDevice(true) // Способ отображения - Линейный ли? true - да; false - иерархический
                .setSearchSettings(true) // true - поиск записей через SOLR; false - поиск записей через БД
                .setTaskTypeListObjectFields(new FieldObject[]{fieldStringIsListChoiceDirectories, fieldTextDirectories, fieldIntDirectories});

        //---------------------------------------------------------------------------------------------------------Инициализация полей и настроек объекта - ТИПЫ ТАБЛИЦ

        // 1. СТРОКА (Выбор из списка == Да; Обязательное)
        FieldObject fieldStringIsListChoice = new TypeListFieldsString()
                .setFieldName("Строка (Выбор из списка == Да; Обязательное) " + randomString(5))
                .setFieldID("STRING" + randomIdentifier(5))
                .setObligatory(ObligatoryField.YES) // Обязательное поле
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(SettingsForTaskTypeListFields.YES) // Выбор из списка
                        .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10))); // Список значений

        // 1.1. СТРОКА (Выбор из списка == Нет)
        FieldObject fieldStringIsNotListChoice = new TypeListFieldsString()
                .setFieldName("Строка (Выбор из списка == Нет) " + randomString(10))
                .setFieldID("STRING" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsString()
                        .setIsListChoice(SettingsForTaskTypeListFields.NO) // Выбор из списка
                        .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10))); // Список значений

        // 2. ТЕКСТ
        FieldObject fieldText = new TypeListFieldsText()
                .setFieldName("Текст " + randomString(10))
                .setFieldID("TEXT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsText());

        // 3. ЦЕЛОЕ (Ссылка на объект == Задача)
        FieldObject fieldIntLinkObject = new TypeListFieldsInteger()
                .setFieldName("Целое (Ссылка на объект == Задача) " + randomString(10))
                .setFieldID("INTEGER" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsInteger()
                        .setObjectLink(SettingsForTaskTypeListFields.OBJECT_LINK)); // Ссылка на объект

        // 3.1. ЦЕЛОЕ (Ссылка на объект == Нет)
        FieldObject fieldInt = new TypeListFieldsInteger()
                .setFieldName("Целое (Ссылка на объект == Нет) " + randomString(10))
                .setFieldID("INTEGER" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsInteger()
                        .setObjectLink(SettingsForTaskTypeListFields.NO));

        // 4. ВЕЩЕСТВЕННОЕ
        FieldObject fieldFloat = new TypeListFieldsDouble()
                .setFieldName("Вещественное " + randomString(10))
                .setFieldID("FLOAT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDouble());

        // 5. ДАТА
        FieldObject fieldDate = new TypeListFieldsDate()
                .setFieldName("Дата " + randomString(10))
                .setFieldID("DATE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDate());

        // 6. ФАЙЛ (Редактирование == Да)
        FieldObject fieldFileEdit = new TypeListFieldsFile()
                .setFieldName("Файл (Редактирование == Да) " + randomString(10))
                .setObligatory(OBLIGATORY_ON_CREATE)
                .setFieldID("FILEDIT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsFile()
                        .setOpenFilesForEdit(OpenFilesForEdit.YES)); // Редактирование файлов - Да

        // 6.1. ФАЙЛ (Редактирование == Нет)
        FieldObject fieldFile = new TypeListFieldsFile()
                .setFieldName("Файл (Редактирование == Нет) " + randomString(10))
                .setFieldID("FILE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsFile()
                        .setOpenFilesForEdit(OpenFilesForEdit.NO)); // Редактирование файлов - Нет

        // 7. ССЫЛКА НА СПР-К
        FieldObject fieldDirectory = new TypeListFieldsDirectory()
                .setFieldName("Ссылка на справочник " + randomString(10))
                .setFieldID("DIRECTORY" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDirectory()
                        .setDirectoryName("Адресная книга")
                        .setNameDirectoryField("Имя"));

        // 8. МНОЖЕСТВЕННАЯ ССЫЛКА НА СПР-К
        FieldObject fieldMultiDirectory = new TypeListFieldsMultiDirectory()
                .setFieldName("Множественная ссылка на справочник " + randomString(5))
                .setFieldID("DIRMULTI" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsMultiDirectory()
                        .setDirectoryName(directories.getObjectTypeName())
                        .setFieldDirectory(fieldStringIsListChoiceDirectories));

        // 9. ЛОГИЧЕСКИЙ
        FieldObject fieldBoolean = new TypeListFieldsBoolean()
                .setFieldName("Логический " + randomString(5))
                .setFieldID("BOOLEAN" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsBoolean());

        // 10. ТЕЛЕФОН
        FieldObject fieldPhone = new TypeListFieldsPhone()
                .setFieldName("Телефон " + randomString(5))
                .setFieldID("PHONE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsPhone());

        // 11. EMAIL
        FieldObject fieldEmail = new TypeListFieldsEmail()
                .setFieldName("Email " + randomString(5))
                .setFieldID("EMAIL" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsEmail());

        // 12. ИЗОБРАЖЕНИЕ
        FieldObject fieldImage = new TypeListFieldsImage()
                .setFieldName("Изображение " + randomString(5))
                .setFieldID("IMAGE" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsImage());

        // 13. ЦВЕТ
        FieldObject fieldColor = new TypeListFieldsColor()
                .setFieldName("Цвет " + randomString(5))
                .setFieldID("COLOR" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsColor());

        // 14. ПОДРАЗДЕЛЕНИЕ
        FieldObject fieldDepartment = new TypeListFieldsDepartment()
                .setFieldName("Подразделение " + randomString(5))
                .setFieldID("DEPARTMENT" + randomIdentifier(5))
                .setFieldType(new TypeListFieldsDepartment());


        // Инициализируем объект - Типы таблиц
        TaskTypeListEditObject typesOfTables = new TypesOfTables("wD_Типы таблиц " + randomString(10))

                // Вкладка - Настройки
                .setTaskTypeListObjectFields(new FieldObject[]{fieldStringIsListChoice, fieldStringIsNotListChoice, fieldText, fieldIntLinkObject,
                        fieldInt, fieldFloat, fieldDate, fieldFileEdit, fieldFile, fieldDirectory, fieldMultiDirectory, fieldBoolean, fieldPhone,
                        fieldEmail, fieldImage, fieldColor, fieldDepartment});

        return new Object[][]{
                {
                        // Справочник
                        directories,
                        // Типы таблиц
                        typesOfTables,
                }
        };


    }

}
