-- 🛠️ Updating filters in t_filter for category "Sedans" (id = 4)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (4, 'fuel type', 'Жанармай түрі', 'Тип топлива', 'multi-select', '{"values": ["petrol"]}'),
                                                                              (4, 'year', 'Жылы', 'Год', 'multi-select', '{"values": ["2018", "2019", "2020", "2021", "2022", "2023"]}'),
                                                                              (4, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["Toyota", "Honda", "Nissan", "Mazda", "Hyundai", "Kia", "Subaru", "Volkswagen", "Chevrolet", "Ford", "BMW"]}'),
                                                                              (4, 'color', 'Түс', 'Цвет', 'multi-select', '{"values": ["black", "white", "grey", "red", "blue", "silver"]}'),
                                                                              (4, 'model', 'Модель', 'Модель', 'multi-select', '{"values": ["Camry", "Accord", "Altima", "6", "Sonata", "K5", "Legacy", "Passat", "Malibu", "Fusion", "3 Series"]}'),
                                                                              (4, 'seats', 'Орын саны', 'Количество мест', 'multi-select', '{"values": ["11", "8", "4", "2"]}');

-- 🛠️ Updating filters in t_filter for category "SUVs & Crossovers" (id = 5)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (5, 'fuel type', 'Жанармай түрі', 'Тип топлива', 'multi-select', '{"values": ["petrol", "diesel", "hybrid"]}'),
                                                                              (5, 'year', 'Жылы', 'Год', 'multi-select', '{"values": ["2018", "2019", "2020", "2021", "2022", "2023"]}'),
                                                                              (5, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["Toyota", "Honda", "Nissan", "Mazda", "Hyundai", "Kia", "Subaru", "Volkswagen", "Chevrolet", "Ford", "BMW"]}'),
                                                                              (5, 'color', 'Түс', 'Цвет', 'multi-select', '{"values": ["black", "white", "grey", "red", "blue", "silver", "green"]}'),
                                                                              (5, 'model', 'Модель', 'Модель', 'multi-select', '{"values": ["RAV4", "CR-V", "X-Trail", "CX-5", "Tucson", "Sportage", "Forester", "Tiguan", "Equinox", "Explorer", "X5"]}'),
                                                                              (5, 'seats', 'Орын саны', 'Количество мест', 'multi-select', '{"values": ["5", "7"]}');

-- 🛠️ Updating filters in t_filter for category "Minivans" (id = 6)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (6, 'fuel type', 'Жанармай түрі', 'Тип топлива', 'multi-select', '{"values": ["petrol", "diesel", "hybrid"]}'),
                                                                              (6, 'year', 'Жылы', 'Год', 'multi-select', '{"values": ["2020", "2021", "2022", "2023"]}'),
                                                                              (6, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["Toyota", "Honda", "Kia", "Chrysler", "Hyundai"]}'),
                                                                              (6, 'color', 'Түс', 'Цвет', 'multi-select', '{"values": ["grey", "white", "black", "blue", "silver"]}'),
                                                                              (6, 'model', 'Модель', 'Модель', 'multi-select', '{"values": ["Sienna", "Odyssey", "Carnival", "Pacifica", "Staria"]}'),
                                                                              (6, 'seats', 'Орын саны', 'Количество мест', 'multi-select', '{"values": ["7", "8"]}');

-- 🛠️ Updating filters in t_filter for category "Compact Cars" (id = 7)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (7, 'fuel type', 'Жанармай түрі', 'Тип топлива', 'multi-select', '{"values": ["petrol"]}'),
                                                                              (7, 'year', 'Жылы', 'Год', 'multi-select', '{"values": ["2020", "2021", "2022", "2023"]}'),
                                                                              (7, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["Toyota", "Honda", "Nissan", "Hyundai", "Kia", "Volkswagen"]}'),
                                                                              (7, 'color', 'Түс', 'Цвет', 'multi-select', '{"values": ["red", "white", "yellow", "grey", "black", "blue"]}'),
                                                                              (7, 'model', 'Модель', 'Модель', 'multi-select', '{"values": ["Yaris", "Fit", "Micra", "Accent", "Rio", "Polo"]}'),
                                                                              (7, 'seats', 'Орын саны', 'Количество мест', 'multi-select', '{"values": ["4", "5"]}');

-- 🛠️ Filters for Bicycles
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (9, 'bike type', 'Велосипед түрі', 'Тип велосипеда', 'multi-select', '{"values": ["mountain", "road", "city", "hybrid"]}'),
                                                                              (9, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["Trek", "Giant", "Specialized", "Cannondale"]}'),
                                                                              (9, 'frame size', 'Рама өлшемі', 'Размер рамы', 'multi-select', '{"values": ["17\"", "19\"", "21\"", "23\""]}');

-- 🛠️ Filters for Electric Scooters
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (10, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["Xiaomi", "Segway", "Ninebot", "Razor"]}'),
                                                                              (10, 'max speed', 'Макс жылдамдық', 'Макс скорость', 'multi-select', '{"values": ["20km/h", "25km/h", "30km/h"]}'),
                                                                              (10, 'range', 'Қашықтық', 'Дальность', 'multi-select', '{"values": ["20km", "25km", "30km", "35km"]}');

-- 🛠️ Filters for Cargo Vans
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (18, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["Mercedes", "Ford", "Volkswagen", "Fiat"]}'),
                                                                              (18, 'capacity', 'Сыйымдылық', 'Вместимость', 'multi-select', '{"values": ["5m³", "8m³", "10m³", "12m³"]}'),
                                                                              (18, 'fuel type', 'Жанармай түрі', 'Тип топлива', 'multi-select', '{"values": ["diesel", "petrol"]}');

-- 🛠️ Filters for Laptops
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (23, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["Apple", "Dell", "HP", "Lenovo", "Asus"]}'),
                                                                              (23, 'ram', 'Жедел жад', 'Оперативная память', 'multi-select', '{"values": ["8GB", "16GB", "32GB"]}'),
                                                                              (23, 'storage', 'Жады', 'Накопитель', 'multi-select', '{"values": ["256GB", "512GB", "1TB"]}');

-- 🛠️ Filters for Power Tools
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (39, 'tool type', 'Құрал түрі', 'Тип инструмента', 'multi-select', '{"values": ["drill", "grinder", "hammer drill", "circular saw"]}'),
                                                                              (39, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["Bosch", "Makita", "DeWalt", "Metabo"]}'),
                                                                              (39, 'power', 'Қуат', 'Мощность', 'multi-select', '{"values": ["12V", "18V", "24V"]}');

-- 🛠️ Filters for Wedding Dresses
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (52, 'style', 'Стиль', 'Стиль', 'multi-select', '{"values": ["A-line", "Mermaid", "Ballgown", "Sheath"]}'),
                                                                              (52, 'size', 'Өлшемі', 'Размер', 'multi-select', '{"values": ["S", "M", "L", "XL"]}'),
                                                                              (52, 'color', 'Түс', 'Цвет', 'multi-select', '{"values": ["white", "ivory", "champagne"]}');

-- 🛠️ Filters for Speakers
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (63, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["JBL", "Bose", "Sony", "Marshall"]}'),
                                                                              (63, 'power', 'Қуат', 'Мощность', 'multi-select', '{"values": ["100W", "200W", "300W", "400W"]}'),
                                                                              (63, 'bluetooth', 'Bluetooth', 'Bluetooth', 'multi-select', '{"values": ["yes", "no"]}');

-- 🛠️ Filters for Apartments
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (73, 'rooms', 'Бөлмелер', 'Комнаты', 'multi-select', '{"values": ["1", "2", "3", "4"]}'),
                                                                              (73, 'area', 'Ауданы', 'Площадь', 'range', '{"min": 30, "max": 150}'),
                                                                              (73, 'furnished', 'Жиһаздар', 'Меблировка', 'multi-select', '{"values": ["yes", "no"]}');

-- 🛠️ Filters for Furniture
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (81, 'furniture type', 'Жиһаз түрі', 'Тип мебели', 'multi-select', '{"values": ["sofa", "table", "chair", "bed"]}'),
                                                                              (81, 'material', 'Материал', 'Материал', 'multi-select', '{"values": ["wood", "metal", "plastic", "leather"]}'),
                                                                              (81, 'color', 'Түс', 'Цвет', 'multi-select', '{"values": ["brown", "black", "white", "gray"]}');

-- 🛠️ Filters for Camping Gear
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (91, 'gear type', 'Жабдық түрі', 'Тип снаряжения', 'multi-select', '{"values": ["tent", "sleeping bag", "camping stove", "lantern"]}'),
                                                                              (91, 'capacity', 'Сыйымдылық', 'Вместимость', 'multi-select', '{"values": ["2 persons", "4 persons", "6 persons"]}'),
                                                                              (91, 'waterproof', 'Су өткізбейтін', 'Водонепроницаемый', 'multi-select', '{"values": ["yes", "no"]}');
