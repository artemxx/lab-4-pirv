# lab-4-pirv

Используемое окружение: https://github.com/big-data-europe/docker-hadoop, возможно нужно будет ещё поставить питоновские `bs4`, `requests`, `nltk`, `lxml`

Скачиваем репозиторий, делаем `docker-compose up -d`. Чтобы открыть *hadoop* в браузере, узнаём `IP` с помощью `docker inspect namenode | grep "IPAddress"`, а порт с помощью `docker ps`

Заходим в контейнер через `docker exec -it namenode /bin/bash`

Генерируем данные с помощью `python3 data_generator.py input_path filter_path n_urls n_filters`

Переносим всё в распределённую filesystem и запускаем `MapReduce` с помощью скрипта `./run_jobs.sh input_path filter_path`, в файлике `part-r-00000` будет выход редьюса - ответ на задачу


### Materials
Датасет урлов википедии: https://figshare.com/articles/dataset/All_English_WIkipedia_Article_URLs/5422522/1
