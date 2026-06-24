INSERT INTO "PUBLIC"."USERS" VALUES
(UUID 'dd29483d-0d89-4a51-abc1-e1cbf5bb634d', 'user123@example.com', 'Wen Zi', TRUE, FALSE, 'Ye', '$2a$10$xwHDAutJLoWYBys4z7LV1.shhyfIlq3fLJkeEtSqIgQs28kLJ4hJW', 'ROLE_USER'),
(UUID 'e8b906bf-1073-457a-aceb-87a4bbe01fa4', 'admin123@example.com', 'Admin', TRUE, FALSE, 'Ye', '$2a$10$9jzSCeeoC03MVWzDrKzn9u01BsM6i8wxqLZXx3XC9C2wfh74bWZgK', 'ROLE_USER');            

INSERT INTO "PUBLIC"."CATEGORIES" VALUES
(UUID 'e57ed152-c8cc-49f8-83d6-aeba99e64813', U&'Th\1eddi Trang Nam', 'thoi-trang-nam', NULL),
(UUID 'b843984f-4374-457d-a858-66557eeaba27', U&'\00c1o Thun Nam', 'ao-thun-nam', UUID 'e57ed152-c8cc-49f8-83d6-aeba99e64813');        
    
INSERT INTO "PUBLIC"."PRODUCT_TYPES" VALUES
(UUID '020bf8ed-ec0e-4935-b874-20a690dcee75', TRUE, TRUE, U&'\00c1p Thun');     

INSERT INTO "PUBLIC"."ATTRIBUTES" VALUES
(UUID '75707b78-8285-4c27-be14-1165360014d3', 'Color', 'color'),
(UUID 'd5d9d3f5-3e60-42e8-abf2-f4ac88598edb', 'Size', 'size');       

INSERT INTO "PUBLIC"."ATTRIBUTE_VALUES" VALUES
(UUID '89ef719d-9835-43d1-9328-2a23c3391d3b', U&'\0110en', 'den', UUID '75707b78-8285-4c27-be14-1165360014d3'),
(UUID '698b8040-4450-421b-a357-f88ddc93179e', 'M', 'm', UUID 'd5d9d3f5-3e60-42e8-abf2-f4ac88598edb');    


INSERT INTO "PUBLIC"."PRODUCTS" VALUES
(1, 'VND', U&'\00c1o thun nam ch\1ea5t li\1ec7u 100% cotton co gi\00e3n 4 chi\1ec1u, th\1ea5m h\00fat m\1ed3 h\00f4i t\1ed1t, form d\00e1ng basic d\1ec5 ph\1ed1i \0111\1ed3.', 180000.00, 180000.00, U&'\00c1o Thun Nam Cotton C\1ed5 Tr\00f2n Basic', 4.8, 'ao-thun-nam-cotton-co-tron-basic', 'https://example.com/ao-thun-basic.jpg', UUID 'e57ed152-c8cc-49f8-83d6-aeba99e64813', UUID '020bf8ed-ec0e-4935-b874-20a690dcee75');    

INSERT INTO "PUBLIC"."PRODUCT_VANRIANTS" VALUES
(UUID '75926e1e-0fa4-4b3a-9ca7-a2e607d8c004', U&'\00c1o Thun Basic - \0110en - M', 180000.00, 50, 'TSHIRT-BSC-BLK-M', TRUE, 1),
(UUID '5e764cf1-8ab2-4474-8091-3ed730fca287', U&'\00c1o Thun Basic - Tr\1eafng - L', 180000.00, 45, 'TSHIRT-BSC-WHT-L', TRUE, 1);              

INSERT INTO "PUBLIC"."ASSIGNED_VARIANT_ATTRIBUTES" VALUES
(UUID 'd7c527d6-b4dd-4ffc-8ed2-a370c4b1a5e6', UUID 'd5d9d3f5-3e60-42e8-abf2-f4ac88598edb', UUID '698b8040-4450-421b-a357-f88ddc93179e', UUID '75926e1e-0fa4-4b3a-9ca7-a2e607d8c004'),
(UUID 'a4f733a1-c825-4ed1-812b-e259cb88449f', UUID '75707b78-8285-4c27-be14-1165360014d3', UUID '89ef719d-9835-43d1-9328-2a23c3391d3b', UUID '75926e1e-0fa4-4b3a-9ca7-a2e607d8c004'),
(UUID 'd319be72-9f0c-42e6-b7e8-c9c2c5e6c63c', UUID 'd5d9d3f5-3e60-42e8-abf2-f4ac88598edb', UUID '698b8040-4450-421b-a357-f88ddc93179e', UUID '5e764cf1-8ab2-4474-8091-3ed730fca287'),
(UUID '345e57f9-7253-4b0a-b708-696660cba976', UUID '75707b78-8285-4c27-be14-1165360014d3', UUID '89ef719d-9835-43d1-9328-2a23c3391d3b', UUID '5e764cf1-8ab2-4474-8091-3ed730fca287');              

