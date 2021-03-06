# Repairs Tracker   

* <b>1.15 - 10/11/21</b>
1. Обновление импортов, Gradle + по-мелочи
* <b>1.14 - 19/10/21</b> Версия не менялась
1. Навел порядок в классах. Добавил отображение локации в ресайкл
* <b>1.14 - 19/10/21</b>
1. Упрощена логика загрузки/обновления списка устройств. Теперь: и загрузка и обновление загружает данные из БД по trackId, при этом каждый новый элемент проверяется по id — есть ли в списке отслеживаемых устройств элемент с таким id: если нет — добавляется, если есть — обновляются данные. По факту: если данные в отслеживаемых устройствах изменятся, при старте/обновлении изменения отобразятся; если было добавлено ещё устройство с таким же trackId (например в ремонте комплект устройств, у них общий trackId), оно добавится в список; если удалить из списка устройство из комплекта, при старте/обновлении оно опять отобразится в списке
2. Обновлены библиотеки
3. Список найденных теперь сортируются по TrackId. Будет сортироваться, даже если trackId будет не int like ("123456"), но и string like ("123TRY")
* <b>1.13 - 18/10/21</b>
1. Исправлено: при обновлении списка (по свайпу вниз или по кнопке) элементы в списке не обновлялись, а добавлялись ещё такие же устройства (дублировались).
* <b>1.12 - 28/09/21</b>
1. Глобальный рефакторинг, все классы раскидал по папочкам
2. Переделан поиск по серийому на поиск по TrackId
3. Добавлен permission WRITE_EXTERNAL_STORAGE и проверка на него
4. Исправлены ошибки при получении unit из БД при отсутствии в БД значений для юнита — location, state, name
5. Добавлено отображение trackId в списке найденных и в инфо фрагменте
6. Добавлена иконка приложения
* <b>1.11 - 19/07/21</b>
1. Добавлено: если ремонт завершен, то в InfoFragment отображается соответствующая надпись. То же для элемента списка устройств
2. В списке устройств раньше отображалось количество дней от начала последней операции. Теперь — количество дней от начала ремонта до его окончания или до сегодняшней даты (если ремонт ещё не окончен)
3. В InfoFragment добавлено отображения количества дней в ремонте   
* <b>1.10 - 16/07/21</b>
1. Исправлена логика загрузки юнитов. Раньше при старте программы всегда сообщалось, что "ничего не найдено", после чего загружался список ранее сохраненных устройств. Теперь пишет только если ничего не находит
2. Добавлено обновление списка отслеживаемых устройств по нажатии кнопки "обновить"
3. Добавлено обновление списка отслеживаемых устройств свайпу списка вниз. Пока идет обновление, крутится значок обновления
4. Если устройство уже есть в списке, то добавлено в список не будет, при этом его данные будут обновлены   
* <b>1.09 - 15/07/21</b>
1. Добавлены стринги
2. Добавлено удаление устройства из списка по свайпу элемента списка вправо/влево
* <b>1.08 - 08/06/21</b>
1. Новый механизм перевода. Теперь язки не хранятся в телефоне, а загружаются из БД. Но в отличии от старой версии загружаются не все варианты сразу при загрузке, а только по необходимости, сделано некоторое подобие JOIN у релеационных БД: теперь сразу загружаем список устройств (или событий), затем по загруженному списку делаем перевод, загружая слова в нужном языке вместо идентификаторов. Если язык не найден, то будет присвоен английский вариант, если нет и английского, то оставляем вариант с идентификатором. Теперь если вдруг изменилось имя устройства/локации/статуса или добавилось новое устройство/статус/локация или добавился новый язык в БД, то эти изменения уже будут видны автоматом без установки новой версии
* <b>1.07 - 03/06/21</b>
1. Удалены лишние методы из FireDBHelper, убрана загрузка ненужных параметров для юнита
2. Методы сохранения/загрузки через SharedPreferences перенесены в отдельный класс. Сами методы изменены и теперь не нужно им передавать активити в параметре (не используют, поэтому можно запускать из MainViewModel) 
3. При загрузке приложения загружает сохраненный ранее список серийных номеров. Если список не пустой, то загружает из БД устройства по серийникам из этого списка. Т.е. при старте программы отображается список отслеживаемых устройств, добавленных ранее
* <b>1.06 - 03/06/21</b>
1. Улучшен механизм смены тем: теперь изменение темы происходит БЕЗ перезагрузки устройства. Соответственно всё что было на UI так там и останется, изменятся только цвета
2. Сделаны две темы: темная и светлая, добавлены кастомные атрибуты и цвета для каждой из тем
3. Добавлена тема дле режима "ночь" (такая же, как и темная)
4. Вместо кнопки "Добавить устройство" сделана панелька с 3-я кнопками: изменить тему, обновить(пока не работает), добавить устройство
5. Удален Свитч смены тем, вместо него кнопка на панеле: в темной в виде солнца, в светлой в виде месяца
* <b>1.05 - 02/06/21</b>
1. Упрощена архитектура: теперь идентификаторы и имена на разных языках не загружаются из БД, а хранятся в приложении. Со всеми плюсами и минусами.
2. А значит удалены лисенеры, связанные мьютабл и геттеры. MainViewModel стал сильно меньше
3. Добавлены языки, теперь их 6: английский (по умолчанию), русский, белорусский, немецкий, французский, итальянский
* <b>1.04 - 01/06/21</b>
1. Теперь при нажатии на элемент списка устройств открывается фрагмент с детальной информацией, загружается список событий для выбранного устройства (в обратном хронологическом порядке)
* <b>1.03 - 01/06/21</b>
1. Изменен поиск: раньше в списке устройств отображался результат поиска, теперь при нахождении устройства оно <i>добавляется</i> в список
* <b>1.02 - 27/05/21</b>
1. Добавлен базовый класс для Диалогов, все диалоги будут наследоваться от него
2. Добавлены стили, цвета и селекторы. В viewModel добавлены LiveData и сеттеры к ним. Добавлен адаптер для списка найденных устройств
3. Добавлен диалог поиска (в будущем И добавления в список отслеживаемых устройств). В диалоге вводится серийный номер, при нажатии кнопки "Найти" приложение ищет в БД (в облаке) устройство с таким номеров, если находит, то показывает его в списке устройств, если не находит, показывает соответствующий тост. Для найденного устройства отображаются следующие данные (загружаются из БД): имя, серийный, внутренний серийный (потом не будет отображаться), имя текущей операции и её дата начала, кол-во дней в ремонте
* <b>1.01 - 27/05/21</b>
1. Добавил переключатель темы: темная/светлая. Запоминает состояние при повороте/перезапуске
2. Добавлены константы для работы с БД
* <b>1.00 - 26/05/21</b>
1. Initial commit
