-- 🚗 Transport (id = 2)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (2, 'vehicle type', 'Көлік түрі', 'Тип транспорта', 'multi-select', '{"values": ["car", "motorcycle", "bicycle", "scooter", "truck"]}'),
                                                                              (2, 'year of manufacture', 'Шығарылған жылы', 'Год выпуска', 'range', '{"min": 1990, "max": 2024}'),
                                                                              (2, 'fuel type', 'Жанармай түрі', 'Тип топлива', 'multi-select', '{"values": ["petrol", "diesel", "electric", "hybrid"]}');

-- 📱 Electronics & Gadgets (id = 21)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (21, 'device type', 'Құрылғы түрі', 'Тип устройства', 'multi-select', '{"values": ["camera", "video camera", "studio lighting", "gaming console"]}'),
                                                                              (21, 'brand', 'Бренд', 'Бренд', 'multi-select', '{"values": ["Canon", "Sony", "Nikon", "Panasonic", "Godox", "PlayStation", "Xbox"]}');

-- 🛠 Tools & Equipment (id = 37)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (37, 'tool type', 'Құрал түрі', 'Тип инструмента', 'multi-select', '{"values": ["drill", "grinder", "hammer drill", "circular saw"]}'),
                                                                              (37, 'manufacturer', 'Өндіруші', 'Производитель', 'multi-select', '{"values": ["Bosch", "Makita", "DeWalt", "Metabo"]}');

-- 👗 Clothing & Accessories (id = 50)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (50, 'clothing type', 'Киім түрі', 'Тип одежды', 'multi-select', '{"values": ["jacket", "dress", "t-shirt", "pants"]}'),
                                                                              (50, 'size', 'Өлшемі', 'Размер', 'multi-select', '{"values": ["XS", "S", "M", "L", "XL"]}'),
                                                                              (50, 'color', 'Түс', 'Цвет', 'multi-select', '{"values": ["black", "white", "red", "blue"]}');

-- 🎉 Entertainment & Events (id = 59)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (59, 'entertainment type', 'Ойын-сауық түрі', 'Тип развлечения', 'multi-select', '{"values": ["karaoke", "party", "concert", "corporate event"]}'),
                                                                              (59, 'maximum number of guests', 'Қонақтардың ең көп саны', 'Максимальное количество гостей', 'range', '{"min": 10, "max": 500}');

-- 🏡 Real Estate (id = 72)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (72, 'property type', 'Мүлік түрі', 'Тип недвижимости', 'multi-select', '{"values": ["apartment", "house", "office", "warehouse"]}'),
                                                                              (72, 'number of Rooms', 'Бөлмелер саны', 'Количество комнат', 'range', '{"min": 1, "max": 10}'),
                                                                              (72, 'area (sq.m)', 'Ауданы (ш.м)', 'Площадь (кв.м)', 'range', '{"min": 20, "max": 500}');

-- 🏠 Home & Furniture (id = 80)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (80, 'furniture type', 'Жиһаз түрі', 'Тип мебели', 'multi-select', '{"values": ["table", "chair", "sofa", "bed"]}'),
                                                                              (80, 'material', 'Материал', 'Материал', 'multi-select', '{"values": ["wood", "metal", "plastic"]}');

-- 🏋️ Outdoor & Sports (id = 90)
INSERT INTO t_filter (category_id, name, name_kz, name_ru, type, options) VALUES
                                                                              (90, 'sport type', 'Спорт түрі', 'Вид спорта', 'multi-select', '{"values": ["football", "cycling", "skateboarding", "camping"]}'),
                                                                              (90, 'required equipment', 'Қажетті жабдық', 'Необходимое оборудование', 'multi-select', '{"values": ["tent", "bicycle", "ball"]}');
