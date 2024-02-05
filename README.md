Общее описание

В этом задании вам нужно разработать сайт по покупке билетов в кинотеатр (один кинотеатр, а не сеть, для простоты). Подобные ресурсы имеют много логики. Ваша задача написать только:

Регистрацию/Вход;
Вывод киносеансов и фильмов;
Покупку билетов.
Техническое задание

Для реализации нужно использовать: Spring Boot, Thymeleaf, Bootstrap, Liquibase, Sql2o, PostgreSQL (зависимости аналогичны проекту job4j_dreamjob).

Представления:

Главная страница. По аналогии с job4j_dreamjob выводите общую информацию о ресурсе;
Расписание. Выводите сеансы и связанные с ними фильмы. При выборе конкретного сеанса пользователь переходит на страницу покупки билета;
Кинотека. Выводите список фильмов;
Страница покупки билета. Выводите информацию о сеансе и фильм. Также 2 выпадающих списка - один для указания ряда, другой для указания места, и кнопки "Купить", "Отменить";
Страница с результатом успешной покупки билета. Выводите, сообщение пользователю, например, "Вы успешно приобрели билет на такое место ...";
Страница с результатом неудачной покупки билета (билет уже купили). Выводите, сообщение пользователю, например, "Не удалось приобрести билет на заданное место. Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова.". Реализовать подобный функционал нужно аналогично регистрации пользователя;
Страница регистрации. Аналогично job4j_dreamjob;
Страница вход. Аналогично job4j_dreamjob.
Навигационная панель:

Лого. При клике на него выполняется переход на главную страницу;
Расписание. Выводите сеансы и связанные с ними фильмы;
Кинотека. Выводите список фильмов, которые показываются в кинотеатре;
Регистрация/Вход. Если пользователь не вошел в систему;
Имя пользователя/Выйти. Если пользователь вошел в систему.
Разделение прав:

Все пользователи имеют право просматривать информацию на сайте;
Только зарегистрированные пользователю могут покупать билеты. Если пользователь не зарегистрирован и нажимает на кнопку "Купить билет", то его перебрасывает на страницу входа.

Нюансы:

Все таблицы кроме таблицы tickets и users заполняются с помощью скриптов SQL. Подобные данные заполняются администратором, а по заданию панель администратора нам писать НЕ нужно;
Таблица users имеет ограничение уникальности для email, поэтому при создании пользователя нужно возвращать Optional<User>;
Таблица tickets имеет ограничение уникальности unique (session_id, row_number, place_number). При создании билета нужно аналогично возвращать Optional<Ticket>. 