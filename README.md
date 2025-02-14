<p> Структура проекта: <br>

1. Пакет courierendpoint -- для тестов ручек, связанных с курьерами: <br>
   a. CreateCourierEndpointTest -- для тестов ручки создания курьера. <br>
   б. LoginCourierEndpointTest -- для тестов ручки логина курьера. <br>
2. Пакет orderendpoints -- для тестов ручек, связанных с заказами: <br>
   a. CreateOrderEndpointTest -- для тестов ручки создания заказа. <br>
   б. GetOrdersListEndpointTest -- для тестов ручки получения списка заказов. <br>
3. Пакет util: <br>
   a. Подпакет client -- для хранения методов взаимодействия с апи в одном месте <br>
   б. Подпакет model -- для хранения классов-объектов <br>
   в. Constant -- для хранения констант <br>
</p>
<p> Пояснения по исполнению кода: <br>
1. Файл ordersList.json -- используется для теста GetOrdersListEndpointTest. JSON-схему в этом файле получил с помощью нашумевшего DeepSeek. <br>
Если такой способ решения задачи недопустим по каким-либо причинам, прошу указать на это в ревью, т.к. в требованиях к проекту такого рода ограничений не было. <br>
2. В CreateOrderEndpointTest -- подглядел в интернетах как можно вывести параметризованные данные в название теста в отчёте Allure, а то было непонятно, чем отличаются тесты.
</p>
<p> Пояснения по поводу отчёта Allure:<br>
По инструкции проекта необходимо закоммитить ТОЛЬКО ".\target\allure-results\.", однако было бы странно не закоммитить сам отчёт, так что папку с ним я тоже залил</p>
<p>ПРАВКИ: <br>
Формат -- Текст правки/Как я понял/Что я сделал

1. Текст правки -- ⚠️Можно улучшить. Для URL лучше использовать константу. Адрес может использоваться в разных тестах. Если он изменится будет проще его поменять. <br>
   Как я понял -- Вообще, были некоторые сложности с пониманием разницы между ЮРЛ и ЮРИ, но вроде разобрался. Надо вынести ручку курьеров и заказов в отдельные константы<br>
   Что я сделал -- <br>
2. Текст правки -- ⚠️Можно улучшить. Можно использовать @Data и @AllArgsConstructor из библиотеки Lombok<br>
   Как я понял -- Интересно! Применю ко всем дата-классам, потому что таки да, был ужас от количества однотипного кода<br>
   Что я сделал -- <br>
3. Текст правки -- ⚠️Можно улучшить. Целесообразно создавать курьера со случайными данными. Можно использовать готовые решения JavaFaker/DataFaker, так как это сокращает нашу работу.<br>
   Как я понял -- Почитал, попробую встроить в тесты. Если не получится - напишу ниже<br>
   Что я сделал -- <br>
4. Текст правки -- ⚠️Можно улучшить. Вместо числовых кодов ответом, лучше использовать коды из библиотеки static org.apache.http.HttpStatus, так как они там строковые и несут смысл, что повышает читаемость и не надо вспоминать, что есть что. Пример кода, statusCode(SC_CREATED).<br>
   Как я понял -- Понял, звучит разумно, сделаю<br>
   Что я сделал -- <br>
5. Текст правки -- ⛔️Нужно исправить. Должно быть минимум два теста - (Неверный логин, верный пароль), (Некорректный пароль, верный логин)<br>
   Как я понял -- Вот блин, как знал, что так надо сделать, но засомневался и удалил заготовки ;( Сделаю!<br>
   Что я сделал -- <br>
6. Текст правки -- ⚠️Можно улучшить. В allure есть еще одна аннотация для описания теста Description. Добавь ее, пожалуйста, во всех тестах проекта<br>
   Как я понял -- Если честно, не думаю, что здесь это имеет смысл -- проект достаточно простой и назначение тестов понятно из названия, но сделаю<br>
   Что я сделал -- <br>
7. Текст правки -- ⛔️Нужно исправить. Созданный заказ нужно отменить после выполнения теста<br>
   Как я понял -- В беседе когорты обсуждалось, что не особо имеет смысл менять статус заказа с одного на другой (заказ не удаляется после отмены), но раз надо, значит надо ;Т <br>
   Что я сделал -- <br>
</p>
