-- Insert root category
INSERT INTO t_category (id, name, parent_id) VALUES
    (1, 'All', NULL);

-- Insert first-level subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (2, 'Transport', 1),
                                                 (21, 'Electronics & Gadgets', 1),
                                                 (37, 'Tools & Equipment', 1),
                                                 (50, 'Clothing & Accessories', 1),
                                                 (59, 'Entertainment & Events', 1),
                                                 (72, 'Real Estate', 1),
                                                 (80, 'Home & Furniture', 1),
                                                 (90, 'Outdoor & Sports', 1);

-- "Transport" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (3, 'Cars', 2),
                                                 (8, 'Bicycles & Scooters', 2),
                                                 (13, 'Motorcycles', 2),
                                                 (17, 'Moving & Delivery Vehicles', 2);

-- "Cars" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (4, 'Sedans', 3),
                                                 (5, 'SUVs & Crossovers', 3),
                                                 (6, 'Minivans', 3),
                                                 (7, 'Compact Cars', 3);

-- "Bicycles & Scooters" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (9, 'Bicycles', 8),
                                                 (10, 'Electric Scooters', 8),
                                                 (11, 'Kick Scooters', 8);

-- "Moving & Delivery Vehicles" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (18, 'Cargo Vans', 17),
                                                 (19, 'Pickup Trucks', 17),
                                                 (20, 'Motorized Carts', 17);

-- "Electronics & Gadgets" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (22, 'Computers & Accessories', 21),
                                                 (27, 'Photo & Video Equipment', 21),
                                                 (32, 'Home Appliances', 21);

-- "Computers & Accessories" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (23, 'Laptops', 22),
                                                 (24, 'Tablets', 22),
                                                 (25, 'Gaming Consoles', 22),
                                                 (26, 'Projectors', 22);

-- "Photo & Video Equipment" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (28, 'Cameras', 27),
                                                 (29, 'Lenses', 27),
                                                 (30, 'Drones', 27),
                                                 (31, 'Lighting Equipment', 27);

-- "Home Appliances" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (33, 'Vacuum Cleaners', 32),
                                                 (34, 'Microwaves', 32),
                                                 (35, 'Coffee Machines', 32),
                                                 (36, 'Air Conditioners', 32);

-- "Tools & Equipment" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (38, 'Construction Tools', 37),
                                                 (42, 'Garden Equipment', 37);

-- "Construction Tools" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (39, 'Power Tools', 38),
                                                 (40, 'Hand Tools', 38),
                                                 (41, 'Scaffolding & Ladders', 38);

-- "Garden Equipment" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (43, 'Lawn Mowers', 42),
                                                 (44, 'Trimmers', 42),
                                                 (45, 'Chainsaws', 42);

-- "Clothing & Accessories" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (51, 'Event Wear', 50),
                                                 (55, 'Accessories', 50);

-- "Event Wear" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (52, 'Wedding Dresses', 51),
                                                 (53, 'Suits', 51),
                                                 (54, 'Costumes', 51);

-- "Accessories" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (56, 'Watches', 55),
                                                 (57, 'Handbags', 55),
                                                 (58, 'Jewelry', 55);

-- "Entertainment & Events" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (60, 'Music & Sound Equipment', 59),
                                                 (61, 'Party Supplies', 59),
                                                 (62, 'Sports Equipment', 59);

-- "Music & Sound Equipment" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (63, 'Speakers', 60),
                                                 (64, 'Microphones', 60),
                                                 (65, 'Lighting Equipment', 60);

-- "Party Supplies" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (66, 'Decorations', 61),
                                                 (67, 'Costumes & Masks', 61),
                                                 (68, 'Photo Booths', 61);

-- "Sports Equipment" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (69, 'Fitness Equipment', 62),
                                                 (70, 'Outdoor Sports Gear', 62);

-- "Real Estate" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (73, 'Apartments for Rent', 72),
                                                 (74, 'Houses for Rent', 72),
                                                 (75, 'Vacation Rentals', 72),
                                                 (76, 'Event Venues', 72);

-- "Home & Furniture" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (81, 'Furniture Rental', 80),
                                                 (82, 'Kitchen Appliances', 80),
                                                 (83, 'Home Decor', 80);

    -- "Outdoor & Sports" subcategories
INSERT INTO t_category (id, name, parent_id) VALUES
                                                 (91, 'Camping Gear', 90),
                                                 (92, 'Fishing Equipment', 90),
                                                 (93, 'Bicycles & Skateboards', 90),
                                                 (94, 'Winter Sports Gear', 90);
