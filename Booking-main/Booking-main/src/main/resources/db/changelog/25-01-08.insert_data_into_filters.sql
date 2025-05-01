-- 🚗 Filters for category "Transport" (id = 2)
INSERT INTO t_filter (category_id, name, type, options) VALUES
                                                            (2, 'vehicle type', 'multi-select', '{"values": ["car", "motorcycle", "bicycle", "scooter", "truck"]}'),
                                                            (2, 'year of manufacture', 'range', '{"min": 1990, "max": 2024}'),
                                                            (2, 'fuel type', 'multi-select', '{"values": ["petrol", "diesel", "electric", "hybrid"]}');

-- 📱 Filters for category "Electronics & Gadgets" (id = 21)
INSERT INTO t_filter (category_id, name, type, options) VALUES
                                                            (21, 'device type', 'multi-select', '{"values": ["camera", "video camera", "studio lighting", "gaming console"]}'),
                                                            (21, 'brand', 'multi-select', '{"values": ["Canon", "Sony", "Nikon", "Panasonic", "Godox", "PlayStation", "Xbox"]}');

-- 🛠 Filters for category "Tools & Equipment" (id = 37)
INSERT INTO t_filter (category_id, name, type, options) VALUES
                                                            (37, 'tool type', 'multi-select', '{"values": ["drill", "grinder", "hammer drill", "circular saw"]}'),
                                                            (37, 'manufacturer', 'multi-select', '{"values": ["Bosch", "Makita", "DeWalt", "Metabo"]}');

-- 👗 Filters for category "Clothing & Accessories" (id = 50)
INSERT INTO t_filter (category_id, name, type, options) VALUES
                                                            (50, 'lothing type', 'multi-select', '{"values": ["jacket", "dress", "t-shirt", "pants"]}'),
                                                            (50, 'size', 'multi-select', '{"values": ["XS", "S", "M", "L", "XL"]}'),
                                                            (50, 'color', 'multi-select', '{"values": ["black", "white", "red", "blue"]}');

-- 🎉 Filters for category "Entertainment & Events" (id = 59)
INSERT INTO t_filter (category_id, name, type, options) VALUES
                                                            (59, 'entertainment type', 'multi-select', '{"values": ["karaoke", "party", "concert", "corporate event"]}'),
                                                            (59, 'maximum number of guests', 'range', '{"min": 10, "max": 500}');

-- 🏡 Filters for category "Real Estate" (id = 72)
INSERT INTO t_filter (category_id, name, type, options) VALUES
                                                            (72, 'property type', 'multi-select', '{"values": ["apartment", "house", "office", "warehouse"]}'),
                                                            (72, 'number of Rooms', 'range', '{"min": 1, "max": 10}'),
                                                            (72, 'area (sq.m)', 'range', '{"min": 20, "max": 500}');

-- 🏠 Filters for category "Home & Furniture" (id = 80)
INSERT INTO t_filter (category_id, name, type, options) VALUES
                                                            (80, 'furniture type', 'multi-select', '{"values": ["table", "chair", "sofa", "bed"]}'),
                                                            (80, 'material', 'multi-select', '{"values": ["wood", "metal", "plastic"]}');

-- 🏋️ Filters for category "Outdoor & Sports" (id = 90)
INSERT INTO t_filter (category_id, name, type, options) VALUES
                                                            (90, 'sport type', 'multi-select', '{"values": ["football", "cycling", "skateboarding", "camping"]}'),
                                                            (90, 'required equipment', 'multi-select', '{"values": ["tent", "bicycle", "ball"]}');

