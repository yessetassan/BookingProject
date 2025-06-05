-- Root category
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
    (1, 'All', 'Жалпы', 'Все', NULL);

-- First-level subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (2, 'Transport', 'Көлік', 'Транспорт', 1),
                                                                   (21, 'Electronics & Gadgets', 'Электроника және гаджеттер', 'Электроника и гаджеты', 1),
                                                                   (37, 'Tools & Equipment', 'Құралдар мен жабдықтар', 'Инструменты и оборудование', 1),
                                                                   (50, 'Clothing & Accessories', 'Киім және аксессуарлар', 'Одежда и аксессуары', 1),
                                                                   (59, 'Entertainment & Events', 'Ойын-сауық және іс-шаралар', 'Развлечения и мероприятия', 1),
                                                                   (72, 'Real Estate', 'Жылжымайтын мүлік', 'Недвижимость', 1),
                                                                   (80, 'Home & Furniture', 'Үй және жиһаз', 'Дом и мебель', 1),
                                                                   (90, 'Outdoor & Sports', 'Ашық ауада және спорт', 'На свежем воздухе и спорт', 1);

-- Transport subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (3, 'Cars', 'Көліктер', 'Автомобили', 2),
                                                                   (8, 'Bicycles & Scooters', 'Велосипедтер және самокаттар', 'Велосипеды и самокаты', 2),
                                                                   (13, 'Motorcycles', 'Мотоциклдер', 'Мотоциклы', 2),
                                                                   (17, 'Moving & Delivery Vehicles', 'Жүк және жеткізу көліктері', 'Грузовые и доставочные машины', 2);

-- Cars subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (4, 'Sedans', 'Седандар', 'Седаны', 3),
                                                                   (5, 'SUVs & Crossovers', 'Жол талғамайтын және кроссоверлер', 'Внедорожники и кроссоверы', 3),
                                                                   (6, 'Minivans', 'Минивэндер', 'Минивэны', 3),
                                                                   (7, 'Compact Cars', 'Шағын көліктер', 'Компактные автомобили', 3);

-- Bicycles & Scooters subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (9, 'Bicycles', 'Велосипедтер', 'Велосипеды', 8),
                                                                   (10, 'Electric Scooters', 'Электр самокаттар', 'Электросамокаты', 8),
                                                                   (11, 'Kick Scooters', 'Самокаттар', 'Самокаты', 8);

-- Moving & Delivery Vehicles subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (18, 'Cargo Vans', 'Жүк фургондары', 'Грузовые фургоны', 17),
                                                                   (19, 'Pickup Trucks', 'Пикаптар', 'Пикапы', 17),
                                                                   (20, 'Motorized Carts', 'Моторлы арбалар', 'Моторизованные тележки', 17);

-- Electronics & Gadgets subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (22, 'Computers & Accessories', 'Компьютерлер және керек-жарақтар', 'Компьютеры и аксессуары', 21),
                                                                   (27, 'Photo & Video Equipment', 'Фото және видео жабдықтар', 'Фото- и видеооборудование', 21),
                                                                   (32, 'Home Appliances', 'Үйге арналған тұрмыстық техника', 'Бытовая техника', 21);

-- Computers & Accessories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (23, 'Laptops', 'Ноутбуктер', 'Ноутбуки', 22),
                                                                   (24, 'Tablets', 'Планшеттер', 'Планшеты', 22),
                                                                   (25, 'Gaming Consoles', 'Ойын консолдері', 'Игровые приставки', 22),
                                                                   (26, 'Projectors', 'Проекторлар', 'Проекторы', 22);

-- Photo & Video Equipment
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (28, 'Cameras', 'Камералар', 'Камеры', 27),
                                                                   (29, 'Lenses', 'Объективтер', 'Объективы', 27),
                                                                   (30, 'Drones', 'Дрондар', 'Дроны', 27),
                                                                   (31, 'Lighting Equipment', 'Жарық жабдықтары', 'Осветительное оборудование', 27);

-- Home Appliances
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (33, 'Vacuum Cleaners', 'Шаңсорғыштар', 'Пылесосы', 32),
                                                                   (34, 'Microwaves', 'Микротолқынды пештер', 'Микроволновки', 32),
                                                                   (35, 'Coffee Machines', 'Кофеқайнатқыштар', 'Кофемашины', 32),
                                                                   (36, 'Air Conditioners', 'Кондиционерлер', 'Кондиционеры', 32);

-- Tools & Equipment
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (38, 'Construction Tools', 'Құрылыс құралдары', 'Строительные инструменты', 37),
                                                                   (42, 'Garden Equipment', 'Бақша жабдықтары', 'Садовое оборудование', 37);

-- Construction Tools
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (39, 'Power Tools', 'Электр құралдары', 'Электроинструменты', 38),
                                                                   (40, 'Hand Tools', 'Қол құралдары', 'Ручной инструмент', 38),
                                                                   (41, 'Scaffolding & Ladders', 'Орман мен баспалдақтар', 'Строительные леса и лестницы', 38);

-- Garden Equipment
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (43, 'Lawn Mowers', 'Шөп шабатын машиналар', 'Газонокосилки', 42),
                                                                   (44, 'Trimmers', 'Триммерлер', 'Триммеры', 42),
                                                                   (45, 'Chainsaws', 'Бензопылдар', 'Бензопилы', 42);
-- "Clothing & Accessories" subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (51, 'Event Wear', 'Салтанатты киімдер', 'Праздничная одежда', 50),
                                                                   (55, 'Accessories', 'Аксессуарлар', 'Аксессуары', 50);

-- "Event Wear" subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (52, 'Wedding Dresses', 'Келін көйлектері', 'Свадебные платья', 51),
                                                                   (53, 'Suits', 'Костюмдер', 'Костюмы', 51),
                                                                   (54, 'Costumes', 'Киімдер', 'Карнавальные костюмы', 51);
-- "Accessories" subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (56, 'Watches', 'Сағаттар', 'Часы', 55),
                                                                   (57, 'Handbags', 'Қол сөмкелері', 'Сумки', 55),
                                                                   (58, 'Jewelry', 'Зергерлік бұйымдар', 'Ювелирные изделия', 55);

-- "Entertainment & Events" subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (60, 'Music & Sound Equipment', 'Музыка және дыбыс жабдықтары', 'Музыкальное и звуковое оборудование', 59),
                                                                   (61, 'Party Supplies', 'Мерекелік керек-жарақтар', 'Товары для праздников', 59),
                                                                   (62, 'Sports Equipment', 'Спорт жабдықтары', 'Спортивное оборудование', 59);

-- "Music & Sound Equipment" subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (63, 'Speakers', 'Дыбыс зорайтқыштар', 'Колонки', 60),
                                                                   (64, 'Microphones', 'Микрофондар', 'Микрофоны', 60),
                                                                   (65, 'Lighting Equipment', 'Жарық жабдықтары', 'Осветительное оборудование', 60);

-- "Party Supplies" subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (66, 'Decorations', 'Безендірулер', 'Украшения', 61),
                                                                   (67, 'Costumes & Masks', 'Костюмдер мен бетперделер', 'Костюмы и маски', 61),
                                                                   (68, 'Photo Booths', 'Фотокабиналар', 'Фотобудки', 61);

-- "Sports Equipment" subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (69, 'Fitness Equipment', 'Фитнес жабдықтары', 'Фитнес-оборудование', 62),
                                                                   (70, 'Outdoor Sports Gear', 'Ашық ауадағы спорт жабдықтары', 'Спортивное снаряжение для улицы', 62);

-- "Real Estate" subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (73, 'Apartments for Rent', 'Пәтерлерді жалға беру', 'Квартиры в аренду', 72),
                                                                   (74, 'Houses for Rent', 'Үйлерді жалға беру', 'Дома в аренду', 72),
                                                                   (75, 'Vacation Rentals', 'Демалысқа арналған жалға берілетін үйлер', 'Аренда для отдыха', 72),
                                                                   (76, 'Event Venues', 'Іс-шаралар өтетін орындар', 'Площадки для мероприятий', 72);

-- "Home & Furniture" subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (81, 'Furniture Rental', 'Жиһазды жалға беру', 'Аренда мебели', 80),
                                                                   (82, 'Kitchen Appliances', 'Асүй техникасы', 'Кухонная техника', 80),
                                                                   (83, 'Home Decor', 'Үйді безендіру', 'Декор для дома', 80);

-- "Outdoor & Sports" subcategories
INSERT INTO t_category (id, name, name_kz, name_ru, parent_id) VALUES
                                                                   (91, 'Camping Gear', 'Лагерьге арналған жабдықтар', 'Снаряжение для кемпинга', 90),
                                                                   (92, 'Fishing Equipment', 'Балық аулау жабдықтары', 'Рыболовное снаряжение', 90),
                                                                   (93, 'Bicycles & Skateboards', 'Велосипедтер мен скейтбордтар', 'Велосипеды и скейтборды', 90),
                                                                   (94, 'Winter Sports Gear', 'Қысқы спорт жабдықтары', 'Снаряжение для зимнего спорта', 90);
