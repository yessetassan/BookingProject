-- 🏎️ Inserting sedan items into t_item with different years
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
                                                                                                        (4, 9, 'Toyota Camry 2022', 'Comfortable sedan for city and highway.', 25000.00, TRUE,
                                                                                                         '{"brand": "Toyota", "model": "Camry", "year": 2022, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "white"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                        (4, 10, 'Honda Accord 2021', 'Reliable and fuel-efficient sedan.', 26000.00, TRUE,
                                                                                                         '{"brand": "Honda", "model": "Accord", "year": 2021, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "grey"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                        (4, 11, 'Nissan Altima 2020', 'Spacious sedan with advanced safety features.', 24500.00, TRUE,
                                                                                                         '{"brand": "Nissan", "model": "Altima", "year": 2020, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "white"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                        (4, 12, 'Mazda 6 2019', 'Stylish and sporty sedan.', 27000.00, TRUE,
                                                                                                         '{"brand": "Mazda", "model": "6", "year": 2019, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "red"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                        (4, 9, 'Hyundai Sonata 2023', 'Modern design with smart features.', 25500.00, TRUE,
                                                                                                         '{"brand": "Hyundai", "model": "Sonata", "year": 2023, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "black"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                        (4, 10, 'Kia K5 2022', 'Elegant and powerful sedan.', 25800.00, TRUE,
                                                                                                         '{"brand": "Kia", "model": "K5", "year": 2022, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "white"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                        (4, 11, 'Subaru Legacy 2021', 'All-wheel drive sedan for all weather conditions.', 26500.00, TRUE,
                                                                                                         '{"brand": "Subaru", "model": "Legacy", "year": 2021, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "silver"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                        (4, 12, 'Volkswagen Passat 2020', 'German engineering for a smooth ride.', 27500.00, TRUE,
                                                                                                         '{"brand": "Volkswagen", "model": "Passat", "year": 2020, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "black"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                        (4, 9, 'Chevrolet Malibu 2018', 'American classic with a modern touch.', 24800.00, TRUE,
                                                                                                         '{"brand": "Chevrolet", "model": "Malibu", "year": 2018, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "grey"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                        (4, 10, 'Ford Fusion 2023', 'Balanced performance and technology.', 26000.00, TRUE,
                                                                                                         '{"brand": "Ford", "model": "Fusion", "year": 2023, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "blue"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                        (4, 11, 'BMW 3 Series 2019', 'Luxury sports sedan with advanced technology.', 32000.00, TRUE,
                                                                                                         '{"brand": "BMW", "model": "3 Series", "year": 2019, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "black"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 🚙 Inserting SUV & Crossover items into t_item
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
                                                                                                                                                                           (5, 9, 'Toyota RAV4 2022', 'Popular crossover with great fuel economy.', 30000.00, TRUE,
                                                                                                                                                                            '{"brand": "Toyota", "model": "RAV4", "year": 2022, "fuel type": "hybrid", "transmission": "automatic", "seats": 5, "color": "blue"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (5, 10, 'Honda CR-V 2021', 'Spacious and reliable SUV.', 31000.00, TRUE,
                                                                                                                                                                            '{"brand": "Honda", "model": "CR-V", "year": 2021, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "white"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (5, 11, 'Nissan X-Trail 2020', 'Comfortable SUV for family trips.', 29000.00, TRUE,
                                                                                                                                                                            '{"brand": "Nissan", "model": "X-Trail", "year": 2020, "fuel type": "diesel", "transmission": "automatic", "seats": 7, "color": "grey"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (5, 12, 'Mazda CX-5 2019', 'Stylish crossover with premium feel.', 32000.00, TRUE,
                                                                                                                                                                            '{"brand": "Mazda", "model": "CX-5", "year": 2019, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "red"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (5, 9, 'Hyundai Tucson 2023', 'Modern design with smart features.', 30500.00, TRUE,
                                                                                                                                                                            '{"brand": "Hyundai", "model": "Tucson", "year": 2023, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "black"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (5, 10, 'Kia Sportage 2022', 'Elegant and practical SUV.', 31500.00, TRUE,
                                                                                                                                                                            '{"brand": "Kia", "model": "Sportage", "year": 2022, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "white"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (5, 11, 'Subaru Forester 2021', 'All-wheel drive for all conditions.', 33000.00, TRUE,
                                                                                                                                                                            '{"brand": "Subaru", "model": "Forester", "year": 2021, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "green"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (5, 12, 'Volkswagen Tiguan 2020', 'German engineering in compact SUV.', 34000.00, TRUE,
                                                                                                                                                                            '{"brand": "Volkswagen", "model": "Tiguan", "year": 2020, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "silver"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (5, 9, 'Chevrolet Equinox 2018', 'American SUV with modern features.', 29500.00, TRUE,
                                                                                                                                                                            '{"brand": "Chevrolet", "model": "Equinox", "year": 2018, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "grey"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (5, 10, 'Ford Explorer 2023', 'Powerful SUV with advanced tech.', 35000.00, TRUE,
                                                                                                                                                                            '{"brand": "Ford", "model": "Explorer", "year": 2023, "fuel type": "petrol", "transmission": "automatic", "seats": 7, "color": "blue"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (5, 11, 'BMW X5 2019', 'Luxury SUV with premium features.', 42000.00, TRUE,
                                                                                                                                                                            '{"brand": "BMW", "model": "X5", "year": 2019, "fuel type": "diesel", "transmission": "automatic", "seats": 5, "color": "black"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 🚐 Inserting Minivan items into t_item
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
                                                                                                                                                                           (6, 10, 'Toyota Sienna 2022', 'Comfortable minivan for large families.', 35000.00, TRUE,
                                                                                                                                                                            '{"brand": "Toyota", "model": "Sienna", "year": 2022, "fuel type": "hybrid", "transmission": "automatic", "seats": 7, "color": "grey"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (6, 11, 'Honda Odyssey 2021', 'Spacious interior with premium features.', 36000.00, TRUE,
                                                                                                                                                                            '{"brand": "Honda", "model": "Odyssey", "year": 2021, "fuel type": "petrol", "transmission": "automatic", "seats": 8, "color": "white"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (6, 12, 'Kia Carnival 2023', 'Modern design with sliding doors.', 37000.00, TRUE,
                                                                                                                                                                            '{"brand": "Kia", "model": "Carnival", "year": 2023, "fuel type": "petrol", "transmission": "automatic", "seats": 7, "color": "black"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (6, 9, 'Chrysler Pacifica 2020', 'Stylish minivan with stowable seats.', 34000.00, TRUE,
                                                                                                                                                                            '{"brand": "Chrysler", "model": "Pacifica", "year": 2020, "fuel type": "hybrid", "transmission": "automatic", "seats": 7, "color": "blue"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (6, 10, 'Hyundai Staria 2023', 'Futuristic design with premium comfort.', 38000.00, TRUE,
                                                                                                                                                                            '{"brand": "Hyundai", "model": "Staria", "year": 2023, "fuel type": "diesel", "transmission": "automatic", "seats": 8, "color": "silver"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 🚗 Inserting Compact Car items into t_item
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
                                                                                                                                                                           (7, 11, 'Toyota Yaris 2022', 'Compact and fuel-efficient city car.', 22000.00, TRUE,
                                                                                                                                                                            '{"brand": "Toyota", "model": "Yaris", "year": 2022, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "red"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (7, 12, 'Honda Fit 2021', 'Spacious interior despite compact size.', 23000.00, TRUE,
                                                                                                                                                                            '{"brand": "Honda", "model": "Fit", "year": 2021, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "white"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (7, 9, 'Nissan Micra 2020', 'Cute and agile city car.', 21000.00, TRUE,
                                                                                                                                                                            '{"brand": "Nissan", "model": "Micra", "year": 2020, "fuel type": "petrol", "transmission": "automatic", "seats": 4, "color": "yellow"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (7, 10, 'Hyundai Accent 2023', 'Reliable compact sedan.', 22500.00, TRUE,
                                                                                                                                                                            '{"brand": "Hyundai", "model": "Accent", "year": 2023, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "grey"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (7, 11, 'Kia Rio 2022', 'Stylish compact with good features.', 23500.00, TRUE,
                                                                                                                                                                            '{"brand": "Kia", "model": "Rio", "year": 2022, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "black"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE'),
                                                                                                                                                                           (7, 12, 'Volkswagen Polo 2021', 'German quality in compact form.', 24000.00, TRUE,
                                                                                                                                                                            '{"brand": "Volkswagen", "model": "Polo", "year": 2021, "fuel type": "petrol", "transmission": "automatic", "seats": 5, "color": "blue"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 🚲 Inserting Bicycle items
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
    (9, 10, 'Mountain Bike Trek 2023', 'Professional mountain bike with 21 speeds', 5000.00, TRUE,
     '{"brand": "Trek", "model": "Marlin 5", "year": 2023, "type": "mountain", "frame_size": "19\"", "color": "black"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 🛴 Inserting Electric Scooter items
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
    (10, 11, 'Xiaomi Mi Electric Scooter 3', 'Lightweight electric scooter with 30km range', 4500.00, TRUE,
     '{"brand": "Xiaomi", "model": "Mi 3", "year": 2022, "max_speed": "25km/h", "range": "30km", "color": "black"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 🚐 Inserting Cargo Van items
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
    (18, 9, 'Mercedes Sprinter 2021', 'Large cargo van with 8m³ capacity', 35000.00, TRUE,
     '{"brand": "Mercedes", "model": "Sprinter", "year": 2021, "capacity": "8m³", "fuel_type": "diesel", "transmission": "manual"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 💻 Inserting Laptop items
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
    (23, 10, 'MacBook Pro 14\" 2023', 'Powerful laptop with M2 Pro chip, 16GB RAM', 8000.00, TRUE,
     '{"brand": "Apple", "model": "MacBook Pro", "year": 2023, "processor": "M2 Pro", "ram": "16GB", "storage": "512GB"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 🔧 Inserting Power Tool items
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
    (39, 11, 'Bosch Professional Drill', 'Powerful cordless drill with 2 batteries', 2500.00, TRUE,
     '{"brand": "Bosch", "model": "GBH 18V-26", "type": "hammer drill", "power": "18V", "max_drill_size": "26mm"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 👰 Inserting Wedding Dress items
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
    (52, 9, 'A-line Wedding Dress', 'Elegant A-line dress with lace details, size M', 15000.00, TRUE,
     '{"style": "A-line", "color": "white", "size": "M", "material": "lace"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 🔊 Inserting Speaker items
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
    (63, 10, 'JBL PartyBox 310', 'Portable party speaker with light effects', 7000.00, TRUE,
     '{"brand": "JBL", "model": "PartyBox 310", "power": "240W", "bluetooth": "yes", "battery": "yes"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 🏢 Inserting Apartment items
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
    (73, 11, '2-room apartment in city center', 'Modern 2-room apartment, 65m², fully furnished', 15000.00, TRUE,
     '{"rooms": 2, "area": "65m²", "floor": "5", "furnished": "yes", "location": "city center"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- 🛋️ Inserting Furniture items
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
    (81, 9, 'Leather Sofa Set', '3-seater leather sofa with 2 armchairs', 5000.00, TRUE,
     '{"type": "sofa set", "material": "leather", "color": "brown", "pieces": "3"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');

-- ⛺ Inserting Camping Gear items
INSERT INTO t_item (category_id, owner_id, name, description, price_per_day, available, attributes, created_at, is_deleted_by_admin, is_approved, is_rejected, status) VALUES
    (91, 10, '4-person Tent', 'Waterproof tent for 4 people with rainfly', 2500.00, TRUE,
     '{"type": "tent", "capacity": "4 persons", "waterproof": "yes", "weight": "5kg"}', CURRENT_TIMESTAMP, false, true, false, 'ACTIVE');


