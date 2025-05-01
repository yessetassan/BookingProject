-- 🛠️ Updating filters in t_filter for category "Sedans" (id = 4)
INSERT INTO t_filter (category_id, name, type, options) VALUES
                                                            (4, 'fuel type', 'multi-select', '{"values": ["petrol"]}'),
                                                            (4, 'year', 'multi-select', '{"values": ["2018", "2019", "2020", "2021", "2022", "2023"]}'),
                                                            (4, 'brand', 'multi-select', '{"values": ["Toyota", "Honda", "Nissan", "Mazda", "Hyundai", "Kia", "Subaru", "Volkswagen", "Chevrolet", "Ford", "BMW"]}'),
                                                            (4, 'color', 'multi-select', '{"values": ["black", "white", "grey", "red", "blue", "silver"]}'),
                                                            (4, 'model', 'multi-select', '{"values": ["Camry", "Accord", "Altima", "6", "Sonata", "K5", "Legacy", "Passat", "Malibu", "Fusion", "3 Series"]}'),
                                                            (4, 'seats', 'multi-select', '{"values": ["11", "8", "4", "2"]}');
