package ru.motiw.data.dataproviders;

import org.testng.annotations.DataProvider;
import ru.motiw.data.BaseTest;
import ru.motiw.mobile.model.FilesForAttachment;
import ru.motiw.web.model.AccessRights;
import ru.motiw.web.model.Administration.Directories.Directories;
import ru.motiw.web.model.Administration.FieldsObject.*;
import ru.motiw.web.model.Administration.TaskTypeListEditObject;
import ru.motiw.web.model.Administration.TasksTypes.ObligatoryField;
import ru.motiw.web.model.Administration.TasksTypes.SettingsForTaskTypeListFields;
import ru.motiw.web.model.Administration.TasksTypes.TasksTypes;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.CorrectionMethod;
import ru.motiw.web.model.DocflowAdministration.DictionaryEditor.DictionaryEditor;
import ru.motiw.web.model.DocflowAdministration.DictionaryEditor.DictionaryEditorField;
import ru.motiw.web.model.DocflowAdministration.DocumentRegistrationCards.*;
import ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor.RouteSchemeEditor;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.OpenFilesForEdit;
import ru.motiw.web.model.ShiftDirection;

import static ru.motiw.data.dataproviders.Administration.getRandomDepartment;
import static ru.motiw.data.dataproviders.Administration.getRandomEmployer;
import static ru.motiw.data.dataproviders.Tasks.getRandomProject;
import static ru.motiw.web.model.Administration.TasksTypes.SettingsForTaskTypeListFields.YES;

/**
 * Данные раздела - Администрирование ДО
 */
public abstract class DocflowAdministration extends BaseTest {

    /**
     * Метод создания полностью случайного объекта - "Редактор словарей"
     */
    public DictionaryEditor getRandomDictionaryEditor() {

        return new DictionaryEditor("wD_Словарь " + randomString(15), /* Название словаря*/ randomEnum(AccessRights.class) /* Уровень доступа*/,
                (new DictionaryEditorField[]{

                        new DictionaryEditorField()
                                .setDictionaryEditorElement(randomString(15)) // Название элемента словаря
                                .setDescriptionDictionItem(randomString(80) + "\n" + randomString(15)), // Описание элемента словаря

                        new DictionaryEditorField()
                                .setDictionaryEditorElement(randomString(15)) // Название элемента словаря
                                .setDescriptionDictionItem(randomString(15) + "\n" + randomString(50)), // Описание элемента словаря

                        new DictionaryEditorField()
                                .setDictionaryEditorElement(randomString(15)) // Название элемента словаря
                                .setDescriptionDictionItem(randomString(30) + "\n" + randomString(15)), // Описание элемента словаря

                }));
    }


    //---------------------------------------------------------------------------------------------------------- Инициализируем объект - Подразделение и Пользователь
    Department[] department = new Department[]{getRandomDepartment()};

    Employee[] employee = new Employee[]{getRandomEmployer(), getRandomEmployer(), getRandomEmployer()};

    //----------------------------------------------------------------------------------------------------Инициализация поля объекта и объект - Справочник
    /*
    1. СТРОКА (Выбор из списка == Да; Уникальное; Обязательное)
    */
    FieldObject fieldStringIsListChoiceDirectory = new TypeListFieldsString()
            .setFieldName("Строка (Выбор из списка == Да; Обяз.) " + randomString(10))
            .setFieldID("STRING" + randomIdentifier(5))
            .setObligatory(ObligatoryField.YES) // Обязательное поле
            .setIsUniqueField(SettingsForTaskTypeListFields.YES) // Уникальное
            .setFieldType(new TypeListFieldsString()
                    .setIsListChoice(SettingsForTaskTypeListFields.YES) // Выбор из списка
                    .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10)));
    /*
     2. ТЕКСТ
      */
    FieldObject fieldTextDirectory = new TypeListFieldsText()
            .setFieldName("Текст " + randomString(10))
            .setFieldID("TEXT" + randomIdentifier(5))
            .setFieldType(new TypeListFieldsText());

    // Будешь плохо кодить, придет Java и съест твою память

    /*
     3. ЦЕЛОЕ
      */
    FieldObject fieldIntDirectory = new TypeListFieldsInteger()
            .setFieldName("Целое " + randomString(10))
            .setFieldID("INTEGER" + randomIdentifier(5))
            .setFieldType(new TypeListFieldsInteger());


    TaskTypeListEditObject directories = new Directories("wD_Справочник_for_DRC " + randomString(10)) /* Название справочника*/
            // Вкладка - Настройки
            .setShareRecords(true) // Общедоступность записей
            .setAccessToRecords(true) // Настройка доступа к записям
            .setMappingDevice(true) // Способ отображения - Линейный ли? true - да; false - иерархический
            .setSearchSettings(true) // true - поиск записей через SOLR; false - поиск записей через БД
            .setTaskTypeListObjectFields(new FieldObject[]{fieldStringIsListChoiceDirectory, fieldTextDirectory, fieldIntDirectory});

    //----------------------------------------------------------------------------------------------------Инициализация поля объекта и объект - Типы задач
    // 1. СТРОКА (Выбор из списка == Да; Обязательное)
    FieldObject fieldStringIsListChoiceTasksTypes = new TypeListFieldsString()
            .setFieldName("Строка (Выбор из списка == Да; Обязательное) " + "|транс. 1| " + randomString(5))
            .setFieldID("STRING" + randomIdentifier(5))
            .setFieldType(new TypeListFieldsString()
                    .setIsListChoice(YES) // Выбор из списка
                    .setValuesList(randomString(10) + "\n" + randomString(10) + "\n" + randomString(10))) // Список значений
            .setObligatory(ObligatoryField.OBLIGATORY_ON_CREATE) // Обязательное при создании
            .setIsHideInSearch(true);

    // 2. ТЕКСТ (Скрывать при поиске)
    FieldObject fieldTextTasksTypes = new TypeListFieldsText()
            .setFieldName("Текст " + "|транс. 2| " + randomString(10))
            .setFieldID("TEXT" + randomIdentifier(5))
            .setFieldType(new TypeListFieldsText())
            .setIsHideInSearch(true); // Скрывать при поиске

    // 3. ЦЕЛОЕ
    FieldObject fieldIntTasksTypes = new TypeListFieldsInteger()
            .setFieldName("Целое " + "|транс. 3| " + randomString(10))
            .setFieldID("INTEGER" + randomIdentifier(5))
            .setFieldType(new TypeListFieldsInteger());

    // 4. ВЕЩЕСТВЕННОЕ
    FieldObject fieldFloatTasksTypes = new TypeListFieldsDouble()
            .setFieldName("Вещественное " + randomString(10))
            .setFieldID("FLOAT" + randomIdentifier(5))
            .setFieldType(new TypeListFieldsDouble());

    // 5. ДАТА
    FieldObject fieldDateTasksTypes = new TypeListFieldsDate()
            .setFieldName("Дата " + "|транс. 4| " + randomString(10))
            .setFieldID("DATE" + randomIdentifier(5))
            .setFieldType(new TypeListFieldsDate());

    // Инициализация объекта - Типы задач с настройками
    TaskTypeListEditObject tasksTypes = new TasksTypes("wD_Тип задачи_for_DRC (verify трансл. значений полей) "
            + randomString(5))

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
                    {fieldStringIsListChoiceTasksTypes, fieldTextTasksTypes, fieldIntTasksTypes,
                            fieldFloatTasksTypes, fieldDateTasksTypes});

    //----------------------------------------------------------------------------------------------------Инициализация объекта - Словарь
    DictionaryEditorField elementOne = new DictionaryEditorField()
            .setDictionaryEditorElement(randomString(10))
            .setDescriptionDictionItem(randomString(15) + "\n" + randomString(30));

    DictionaryEditorField elementTwo = new DictionaryEditorField()
            .setDictionaryEditorElement(randomString(10))
            .setDescriptionDictionItem(randomString(50) + "\n" + randomString(30));

    DictionaryEditorField elementThree = new DictionaryEditorField()
            .setDictionaryEditorElement(randomString(10))
            .setDescriptionDictionItem(randomString(30) + "\n" + randomString(30));

    DictionaryEditor dictionaryEditor = getRandomDictionaryEditor()
            .setDictionaryEditorFields(new DictionaryEditorField[]{elementOne, elementTwo,
                    elementThree});

    /*
     =========================Инициализация полей объекта - Документ
     */
        /*
         1. ЧИСЛО
         */
    FieldDocument fieldNumber = new FieldTypeNumberDocument()
            .setDocumentFieldName("Число " + "|транс. 3| " + randomString(5)) // Имя поля документа
            .setFieldIdentifierDoc("NUMBER" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeNumberDocument()) // Обязательное поле == Обязательное при создании
            .setEditableField(true) // Обязательное при редактировании (true == Да; false == Нет)
            .setObligatoryFieldDoc(ObligatoryFieldDocument.REQUIRED_WHEN_CREATION);

    /*
      2. ДАТА (Текущая дата == Текущая дата; Изменяемое при создании == Да)
     */
    FieldDocument fieldCurrentDate = new FieldTypeDateDocument()
            .setDocumentFieldName("Дата (Текущая дата == Да; Изменяемое при создании == Да) " + "|транс. 4| " + randomString(5)) // Имя поля документа
            .setFieldIdentifierDoc("DATE" + randomIdentifier(5)) // Идентификатор поля
            .setEditableField(true)
            .setObjectFieldDocument(new FieldTypeDateDocument()
                    .setDefaultValue(SettingsForDocumentFields.CURRENT_DATE)
                    .setEditionAvailableWhileCreation(true)); // Изменяемое при создании

    /*
      2.1. ДАТА (Текущая дата == Нет; Изменяемое при создании == Нет)
     */
    FieldDocument notTheCurrentDate = new FieldTypeDateDocument()
            .setDocumentFieldName("Дата (Изменяемое при создании == Нет; Текущая дата == Нет) " + randomString(5)) // Имя поля документа
            .setFieldIdentifierDoc("DATE" + randomIdentifier(5)) // Идентификатор поля
            .setEditableField(true)
            .setObjectFieldDocument(new FieldTypeDateDocument()
                    .setDefaultValue(SettingsForDocumentFields.NO)
                    .setEditionAvailableWhileCreation(false)); // Изменяемое при создании == Нет

    /*
      3. СТРОКА (Уникальное == Да)
     */
    FieldDocument fieldUniqueString = new FieldTypeStringDocument()
            .setDocumentFieldName("Строка (Уникальное) " + "|транс. 1| " + randomString(5)) // Имя поля документа
            .setFieldIdentifierDoc("STRING" + randomIdentifier(5)) // Идентификатор поля
            .setEditableField(true)
            .setUniqueField(true) // Уникальное поле
            .setObjectFieldDocument(new FieldTypeStringDocument()
                    .setFieldLength(randomInt(999))); // Длина поля

    /*
      3.1 СТРОКА; Выбор только из спр-ка == Да
     */
    FieldDocument fieldStringOnlyYesDirectory = new FieldTypeStringDocument()
            .setDocumentFieldName("Строка (Выбор из спр-ка == Да) " + randomString(5)) // Имя поля документа
            .setFieldIdentifierDoc("STONLYESDIR" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeStringDocument()
                    .setSelectOnlyFromDictionary(SettingsForDocumentFields.YES) // Выбор только из справочника (true == Да; false == Нет)
                    .setDirectoryName(directories.getObjectTypeName())
                    .setDirectoryTemplate("{" + fieldStringIsListChoiceDirectory.getFieldName() + "}" + " - "
                            + "{" + fieldIntDirectory.getFieldName() + "}" + " " + randomString(15))
                    .setFieldLength(randomInt(999))) // Длина поля
            .setEditableField(true);

    /*
      3.2 СТРОКА; Выбор только из спр-ка == Нет
     */
    FieldDocument fieldStringOnlyNoDirectory = new FieldTypeStringDocument()
            .setDocumentFieldName("Строка (Выбор из спр-ка == Нет) " + randomString(5)) // Имя поля документа
            .setFieldIdentifierDoc("STONLNODIR" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeStringDocument()
                    .setSelectOnlyFromDictionary(SettingsForDocumentFields.NO) // Выбор только из справочника (true == Да; false == Нет)
                    .setDirectoryName(directories.getObjectTypeName())
                    .setDirectoryTemplate("{" + fieldStringIsListChoiceDirectory.getFieldName() + "}" + " - "
                            + "{" + fieldIntDirectory.getFieldName() + "}" + " " + randomString(15))
                    .setFieldLength(randomInt(999))) // Длина поля
            .setEditableField(true);

    /*
      4. ТЕКСТ
     */
    FieldDocument fieldText = new FieldTypeTextDocument()
            .setDocumentFieldName("Текст " + "|транс. 2| " + randomString(5))
            .setFieldIdentifierDoc("TEXT" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeTextDocument())
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)

    /*
      4.1. ТЕКСТ; Выбор только из спр-ка == Да
     */
    FieldDocument fieldTextOnlyYesDirectory = new FieldTypeTextDocument()
            .setDocumentFieldName("Текст (Выбор из спр-ка == Да) " + randomString(5))
            .setFieldIdentifierDoc("TEXONLYYESDIR" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeTextDocument()
                    .setSelectOnlyFromDictionary(SettingsForDocumentFields.YES)
                    .setDirectoryName(directories.getObjectTypeName())
                    .setDirectoryTemplate("{" + fieldStringIsListChoiceDirectory.getFieldName() + "}" + " - "
                            + "{" + fieldIntDirectory.getFieldName() + "}" + " " + randomString(15)))
            .setEditableField(true) // Обязательное при редактировании (true == Да; false == Нет)
            .setHideInTablesField(true); // Скрывать в таблицах

    /*
      5. СЛОВАРЬ
     */
    FieldDocument fieldDictionary = new FieldTypeDictionaryDocument()
            .setDocumentFieldName("Словарь " + randomString(5))
            .setFieldIdentifierDoc("DICTIONARY" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeDictionaryDocument()
                    .setDictionaryEditor(dictionaryEditor)) // Выбор проинициализированный объект - Словарь
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)

    /*
      6. ПОДРАЗДЕЛЕНИЕ
     */
    FieldDocument fieldDepartment = new FieldTypeDepartmentDocument()
            .setDocumentFieldName("Подразделение " + randomString(5))
            .setFieldIdentifierDoc("DEPARTMENT" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeDepartmentDocument())
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)

    /*
      7. СОТРУДНИК
     */
    FieldDocument fieldEmployee = new FieldTypeEmployeeDocument()
            .setDocumentFieldName("Сотрудник " + "трансляция в ОР в зад. по докумен." + randomString(5))
            .setFieldIdentifierDoc("EMPLOYEE") // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeEmployeeDocument())
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)

    /*
      7.1. СОТРУДНИК (Контролер типа == Да; Текущий пользователь == Да)
     */
    FieldDocument fieldEmployeeSuperviserAndDefaultValue = new FieldTypeEmployeeDocument()
            .setDocumentFieldName("Сотрудник (Контролер типа; Текущий пользователь) " + randomString(5))
            .setFieldIdentifierDoc("EMPDEFAULTVALUY" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeEmployeeDocument()
                    .setDefaultValue(SettingsForDocumentFields.CURRENT_USER) // Значение по умолчанию == Текущий пользователь
                    .setDocumentSuperviser(true)) // Контролер документа == Да
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)

    /*
      7.2. СОТРУДНИК (Для сведения == Да)
     */
    FieldDocument fieldEmployeeForInformation = new FieldTypeEmployeeDocument()
            .setDocumentFieldName("Сотрудник (Для сведения) " + randomString(5))
            .setFieldIdentifierDoc("EMFORINFORMATION" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeEmployeeDocument()
                    .setForInformation(true))// Для сведения == Да
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)

    /*
      8. ДОКУМЕНТ
     */
    FieldDocument fieldDocument = new FieldTypeDocumentDocument()
            .setDocumentFieldName("Документ " + randomString(5))
            .setFieldIdentifierDoc("DOCUMENT" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeDocumentDocument()
                    .setDisplayNameTemplate("{" + fieldUniqueString.getFieldIdentifierDoc() + "}; " + "{"
                            + randomIdentifier(10) + "}; " + randomString(10)))
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)

    /*
      8.1. ДОКУМЕНТ (Правила поиска)
     */
    FieldDocument fieldDocumentSearchRules = new FieldTypeDocumentDocument()
            .setDocumentFieldName("Документ (Правила поиска) " + randomString(5))
            .setFieldIdentifierDoc("DOCSEARCHRUL" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeDocumentDocument()
                    .setDisplayNameTemplate("{" + randomIdentifier(10) + "}; " + "{" + randomIdentifier(10) + "}; " + randomString(10))
                    .setSearchSimiliarDocuments(true) // Искать похожие документы
                    .setSearchRules("DOCUMENT_STATE" + "=" + "0;")) // Правила поиска
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)

    /*
      9. НУМЕРАТОР
     */
    FieldDocument fieldNumerator = new FieldTypeNumeratorDocument()
            .setDocumentFieldName("Нумератор " + randomString(5))
            .setFieldIdentifierDoc("NUMERATOR" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeNumeratorDocument()
                    .setNumeratorTemplateDoc("{" + fieldUniqueString.getFieldIdentifierDoc() + "}-{counter}-{counter("
                            + fieldDepartment.getFieldIdentifierDoc() + ",%04d)}-[8]-{DD}.{YYYY} "
                            + randomString(10))
                    .setEditionAvailableWhileCreation(true)) // Изменяемое при создании
            .setObligatoryFieldDoc(ObligatoryFieldDocument.REQUIRED_WHEN_CREATION) // Обязательное поле == Обязательное при создании
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)

    /*
     10. СПРАВОЧНИК (Одна запись)
     */
    FieldDocument fieldDirectory = new FieldTypeDirectoryDocument()
            .setDocumentFieldName("Справочник " + randomString(5))
            .setFieldIdentifierDoc("DIRECTORY" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeDirectoryDocument()
                    .setNameDirectoryDoc(directories.getObjectTypeName())
                    .setDirectoryTemplate("{" + fieldStringIsListChoiceDirectory.getFieldName() + "}" + " - "
                            + "{" + fieldIntDirectory.getFieldName() + "}" + " " + randomString(15))
                    .setDirectoryEntriesSelection(SettingsForDocumentFields.SINGLE_RECORD)) // Одна запись
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)

    /*
     11. МН. СПРАВОЧНИК (Несколько записей)
    */
    FieldDocument fieldMultiDirectory = new FieldTypeDirectoryDocument()
            .setDocumentFieldName("Множественный справочник " + randomString(5))
            .setFieldIdentifierDoc("DIRMULTI" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeDirectoryDocument()
                    .setNameDirectoryDoc(directories.getObjectTypeName()) // Задаем проинициализированный спр-к
                    .setDirectoryTemplate("{" + fieldStringIsListChoiceDirectory.getFieldName() + "}" + ";"
                            + "{" + fieldTextDirectory.getFieldName() + "}" + ";"
                            + "{" + fieldIntDirectory.getFieldName() + "}" + "; " + randomString(15))
                    .setDirectoryEntriesSelection(SettingsForDocumentFields.MULTIPLE_RECORDS)) // Выбор кол-во записей спр-ка
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)*/

    // Инициализация РКД и её настроек
    DocRegisterCards registerCards = new DocRegisterCards("wD_Тестовая карточка " + randomString(20))

            // Статус документа
            .setDocumentStatesOnReview("На рассмотрении " + randomString(20)) // - На рассмотрении
            .setDocumentStatesReviewed("Рассмотрен " + randomString(20)) // - Рассмотрен
            .setDocumentStatesOnApproval("На подписании " + randomString(20)) // - На подписании
            .setDocumentStatesOnExecution("На исполнении " + randomString(20)) // - На исполнении
            .setDocumentStatesInArchive("В архиве " + randomString(20)) // - В архиве

            .setDisplayNameTemplate("{" + fieldNumerator.getFieldIdentifierDoc() + "}, " + "{" + fieldUniqueString.getFieldIdentifierDoc() + "} "
                    + randomString(15)) // Шаблон отображения

            // Направление смещения при попадании на нерабочее время
            .setDocRegisterCardsShiftDirection(ShiftDirection.DATE_MOVES_FORWARD) // Дата сдвигается назад

            // Настройки по умолчанию при отправке документа на доработку:
            .setAtFirstRevisionScheme(false) // Возврат на доработку с начала текущей схемы
            .setForCompletionInTighterPoint(false) // Возврат на доработку в ту же точку рассмотрения
            .setOnCompletionTheNewScheme(true) // Возврат на доработку с новой схемой

            .setOpenFilesForEditDoc(OpenFilesForEdit.YES) // Открывать файлы для редактирования

            .setAutoСalculationNumeratorFields(AutoCalculationOfNumeratorFields.YES) // Автоматическое вычисление полей-нумераторов

            .setAccessDoc(AccessRights.AVAILABLE_TO_ALL) // выбираем права Доступа к РКД

            // Изменение признака "Окончательная версия"
            .setDocAuthorFinalVersionFiles(SettingsFinalVersion.NO) // Автор документа
            .setUserWithEditRightFinalVersionFiles(SettingsFinalVersion.VALUE_IS_NOT_DEFINED) // Пользователь с правами редактирования
            .setDocTypeControllerFinalVersionFiles(SettingsFinalVersion.NO) // Контролер типа документа

            // Редактирование своих документов
            .setEditionOwnDocumentsOnReview(EditionOwnDocuments.USER_RIGHT_EDIT_THEIR_DOCUMENTS) //  - На рассмотрении
            .setEditionOwnDocumentsOnExecution(EditionOwnDocuments.YES) // - На исполнении
            .setEditionOwnDocumentsInArchive(EditionOwnDocuments.NO) // - В архиве

            // Доступ к разделам документа при просмотре/редактировании
            .setAccessToSectionsDocumentRoute(false) // - Маршрут
            .setAccessToSectionsDocumentFiles(false) // - Файлы
            .setAccessToSectionsDocumentResolution(false) // - Резолюции
            .setAccessToSectionsDocumentLog(false) // - Журнал

            .setCreationOfLinkedDocuments(CreationOfLinkedDocuments.USERS_WITH_RIGHT) // Создание связанных документов

            .setCheckBoxUseAllPossibleRoutes(true) // Использовать все возможные маршруты

            // Типы полей документа
            .setFieldDocumentFields(new FieldDocument[]{fieldNumber, fieldCurrentDate, notTheCurrentDate, fieldUniqueString, fieldStringOnlyYesDirectory,
                    fieldStringOnlyNoDirectory, fieldText, fieldTextOnlyYesDirectory, fieldDictionary, fieldDepartment, fieldEmployee,
                    fieldEmployeeSuperviserAndDefaultValue, fieldEmployeeForInformation, fieldDocument, fieldDocumentSearchRules, fieldNumerator,
                    fieldDirectory, fieldMultiDirectory})

            // Копирование полей при создании задачи
            .setCopyingFieldsWhenCreatingATask(fieldStringIsListChoiceTasksTypes.getFieldID() + "=" + fieldUniqueString.getFieldIdentifierDoc() + ";"
                    + " " + fieldTextTasksTypes.getFieldID() + "=" + fieldText.getFieldIdentifierDoc() + ";"
                    + " " + fieldIntTasksTypes.getFieldID() + "=" + fieldNumber.getFieldIdentifierDoc() + ";"
                    + " " + fieldDateTasksTypes.getFieldID() + "=" + fieldCurrentDate.getFieldIdentifierDoc() + ";"
                    + " " + "TASK_DESCRIPTION" + "=" + fieldText.getFieldIdentifierDoc() + ";")

            // Тип задачи по рассмотрению / исполнению документа
            .setTheTypeOfTaskToReviewAndExecutionOfDocument(tasksTypes.getObjectTypeName())

            // Поля документа, содержащие...:
            .setDecisionMakersOfTasks(fieldEmployee.getFieldIdentifierDoc()); // ответственных руководителей задач

    /**
     * Параметризация - Инициализируем модель - Документ
     *
     * @return массив параметров объектов системы
     */
    @DataProvider
    public Object[][] objectDataRCD() {
        //----------------------------------------------------------------------------------------------------------- Инициализация Документа
        Document document = new Document()

                .setDocumentType(registerCards) // Тип документа
                .setDateRegistration(randomDateTime()) // Дата регистрации
                .setProject(getRandomProject()) // Инициализируем проект документа

                // Осуществляем заполнение (наполнение) полей документа через массив
                .setDocumentFields(new FieldDocument[]{
                                (FieldDocument) fieldNumber.setValueField(randomInt(999)),
                                (FieldDocument) fieldCurrentDate.setValueField(randomDateTime()),

                                (FieldDocument) fieldUniqueString.setValueField(randomString(50)),
                                (FieldDocument) fieldStringOnlyNoDirectory.setValueField(randomString(25)),

                                (FieldDocument) fieldText.setValueField(randomString(200)),

                                (FieldDocument) fieldDictionary.setValueDictionaryEditor(elementOne),

                                (FieldDocument) fieldDepartment.setValueDepartment(new Department[]{department[0]}),

                                (FieldDocument) fieldEmployee.setValueEmployee(new Employee[]{employee[0]}),
                                (FieldDocument) fieldEmployeeForInformation.setValueEmployee(new Employee[]{employee[1], employee[2]})
                        }
                )
                .setRouteSchemeForDocument(new RouteSchemeEditor()
                        .setRouteScheme("Согласование входящей корреспонденции - Постановка задачи")
                        .setReviewDuration(randomInt(999))
                        .setUserRoute(new Employee[]{employee[0]})
                );

        return new Object[][]{
                {
                        //пременная объекта - ПОДРАЗДЕЛЕНИЕ
                        department,
                        //пременная объекта - ПОЛЬЗОВАТЕЛЬ
                        employee,
                        //пременная объекта - СПРАВОЧНИКИ
                        directories,
                        //пременная объекта - ТИПЫ ЗАДАЧ
                        tasksTypes,
                        //пременная объекта - СЛОВАРЬ
                        dictionaryEditor,
                        //пременная объекта - РКД
                        registerCards,
                        //пременная объекта - ДОКУМЕНТ
                        document
                }
        };

    }


    /**
     * Параметризация - Инициализируем модель - Документ-2
     *
     * @return массив параметров объектов системы
     */
    @DataProvider
    public Object[][] objectDataEditDocument() {


        //----------------------------------------------------------------------------------------------------------- Инициализация Документа
         /*
         ЧИСЛО
         */
        FieldDocument fieldNumber_2 = new FieldTypeNumberDocument()
                .setDocumentFieldName(fieldNumber.getDocumentFieldName()) // Имя поля документа
                .setObjectFieldDocument(new FieldTypeNumberDocument()) // Обязательное поле == Обязательное при создании
                .setEditableField(true) // Обязательное при редактировании (true == Да; false == Нет)
                .setObligatoryFieldDoc(ObligatoryFieldDocument.REQUIRED_WHEN_CREATION);


        /*
         СТРОКА (Уникальное == Да)
         */
        FieldDocument fieldUniqueString_2 = new FieldTypeStringDocument()
                .setDocumentFieldName(fieldUniqueString.getDocumentFieldName()) // Имя поля документа
                .setEditableField(true)
                .setUniqueField(true) // Уникальное поле
                .setObjectFieldDocument(new FieldTypeStringDocument()
                        .setFieldLength(randomInt(999))); // Длина поля


        Document document = new Document()
                .setDocumentType(registerCards) // Тип документа
                // Осуществляем заполнение (наполнение) полей документа через массив
                .setDocumentFields(new FieldDocument[]{
                                (FieldDocument) fieldNumber_2.setValueField(randomInt(999)),
                        }
                )
                .setRouteSchemeForDocument(new RouteSchemeEditor()
                        .setRouteScheme("Согласование входящей корреспонденции - Постановка задачи")
                        .setReviewDuration(randomInt(999))
                        .setUserRoute(new Employee[]{employee[0]})


                );

        return new Object[][]{
                {
                        //пременная объекта - ДОКУМЕНТ
                        document
                }
        };

    }


}
