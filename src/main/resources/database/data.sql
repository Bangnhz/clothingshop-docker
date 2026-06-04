-- USERS
INSERT INTO users (username,email,password,full_name,phone,role,status,created_at) VALUES
('admin','admin@gmail.com','123456','Admin','0900000001','ADMIN','ACTIVE',NOW()),
('b','b@gmail.com','123','b','09323141','ADMIN','ACTIVE',NOW()),
('user1','user1@gmail.com','123456','Nguyen Van A','0900000002','CUSTOMER','ACTIVE',NOW()),
('user2','user2@gmail.com','123456','Nguyen Van B','0900000003','CUSTOMER','ACTIVE',NOW()),
('user3','user3@gmail.com','123456','Nguyen Van C','0900000004','CUSTOMER','ACTIVE',NOW()),
('user4','user4@gmail.com','123456','Nguyen Van D','0900000005','CUSTOMER','ACTIVE',NOW()),
('user5','user5@gmail.com','123456','Nguyen Van E','0900000006','CUSTOMER','ACTIVE',NOW()),
('user6','user6@gmail.com','123456','Nguyen Van F','0900000007','CUSTOMER','ACTIVE',NOW()),
('user7','user7@gmail.com','123456','Nguyen Van G','0900000008','CUSTOMER','ACTIVE',NOW()),
('user8','user8@gmail.com','123456','Nguyen Van H','0900000009','CUSTOMER','ACTIVE',NOW()),
('user9','user9@gmail.com','123456','Nguyen Van I','0900000010','CUSTOMER','ACTIVE',NOW());

-- CATEGORIES
INSERT INTO categories (name,description) VALUES
('Quan','Tat ca cac loai quan'),
('Ao','Tat ca cac loai ao'),
('Giay','Tat ca cac loai giay'),
('Dep','Tat ca cac loai dep'),
('Phu kien','Phu kien thoi trang'),
('Ao khoac','Cac loai ao khoac'),
('Do the thao','Do the thao'),
('Quan jean','Quan jean nam nu'),
('Ao thun','Ao thun nam nu'),
('Tui xach','Tui xach thoi trang'),
('Vay','Vay nu thoi trang'),
('Ao so mi','Ao so mi nam nu'),
('Ao hoodie','Unisex hoodie'),
('Quan short','Short thoi trang'),
('Do ngu','Do mac nha');

-- SIZES
INSERT INTO sizes (name) VALUES
('S'),('M'),('L'),('XL'),('XXL'),
('28'),('29'),('30'),('31'),('32');

-- PRODUCTS
INSERT INTO products (name,description,price,category_id,created_at) VALUES
('Baby', 'Do so sinh', 150000, 1, NOW()),
('Boy shirt', 'Ao so mi be trai', 180000, 2, NOW()),
('Girl dress', 'Vay cong chua', 250000, 11, NOW()),
('Set newborn', 'Do so sinh mem', 120000, 1, NOW()),
('Cardigan', 'Ao khoac len', 220000, 6, NOW()),
('Jeans kid', 'Quan jean be trai', 190000, 8, NOW()),
('Baby shoes', 'Giay tap di', 150000, 3, NOW()),
('Body Suit Chip', 'Body chip cotton 100% cho bé sơ sinh', 95000, 1, NOW()),
('Set Newborn 5 món', 'Bộ quà tặng sơ sinh bao gồm bao tay chân, mũ', 155000, 1, NOW()),
('Chăn quấn kén', 'Chăn ủ giúp bé ngủ ngon không giật mình', 180000, 1, NOW()),
('Yếm tam giác cotton', 'Yếm ăn dặm và giữ ấm cổ cho bé', 45000, 1, NOW()),
('Set đồ ngủ dài tay', 'Vải thun lạnh Petit co giãn cực tốt', 135000, 15, NOW()),
('Set Công Tử Bé Trai', 'Áo sơ mi kèm yếm quần kaki cho bé đi tiệc', 285000, 2, NOW()),
('Áo thun Polo Kids', 'Áo phông có cổ phong cách năng động', 120000, 9, NOW()),
('Quần Jean Baggy Kid', 'Jean mềm không gây kích ứng da bé', 195000, 8, NOW()),
('Áo Hoodie nỉ bông', 'Áo khoác giữ ấm mùa đông cho bé', 220000, 13, NOW()),
('Áo minecraft', 'Họa tiết siêu anh hùng vải thoáng mát', 110000, 2, NOW()),
('Váy Công Chúa Voan', 'Váy xòe nhiều lớp cho bé gái diện Tết/sinh nhật', 350000, 11, NOW()),
('Váy Hoa Nhí Vintage', 'Chất vải đũi mát, phong cách Hàn Quốc', 210000, 11, NOW()),
('Set Đầm kèm bờm', 'Đầm lụa cao cấp tặng kèm phụ kiện tóc', 275000, 11, NOW()),
('Váy yếm bò xinh xắn', 'Yếm denim mềm phối với áo phông', 190000, 11, NOW()),
('Legging ren gấu', 'Quần legging ôm phối ren điệu đà', 85000, 8, NOW()),
('Giày tập di chống trượt', 'Đế cao su mềm cho bé bắt đầu tập đi', 150000, 3, NOW()),
('Sandal quai hậu bé trai', 'Chất liệu da mềm, nhẹ chân', 210000, 3, NOW()),
('Bít tất thú bông', 'Tất có hạt chống trượt và tai thú xinh xắn', 35000, 3, NOW()),
('Mũ cói đi biển', 'Mũ rộng vành có dây buộc cho bé gái', 125000, 3, NOW()),
('Balo mầm non mẫu giáo', 'Hình thú 3D nhỏ gọn cho bé đi mẫu giáo', 185000, 10, NOW()),
('Áo khoác phao tai gấu', 'Áo phao siêu nhẹ, giữ ấm tuyệt đối', 320000, 6, NOW()),
('pijama cho bé','áo hình ô tô', 100000, 9,NOW()),
('Bộ len dệt kim', 'Len sợi tự nhiên không gây ngứa', 255000, 6, NOW()),
('Áo gile chần bông', 'Mặc ngoài áo thun giữ ấm ngực cho bé', 145000, 6, NOW()),
('Mũ len quả bông', 'Mũ len bao phủ tai kèm lót lông', 75000, 3, NOW()),
('Set đồ bơi liền thân', 'Chống tia UV bảo vệ da bé dưới nắng', 240000, 2, NOW()),
('Kính mát gọng dẻo', 'Chống gãy, bảo vệ mắt bé khỏi tia cực tím', 110000, 3, NOW()),
('Áo sơ mi cổ trụ', 'Lịch lãm cho bé đi chơi cuối tuần', 160000, 2, NOW());

-- PRODUCT IMAGES
INSERT INTO product_images (image_url,is_main,product_id) VALUES
('onesie.png',1,1),('bestseller_2.png',1,2),('bestseller_3.png',1,3),
('bestseller.png',1,4),('girl_cardigan.png',1,5),('boy_jeans.png',1,6),
('baby_shoes.png',1,7),('bestseller.png',1,8),('newborn_5_mon.jpeg',1,9),
('chan_quan_ken.jpeg',1,10),('yem_an_dam.jpeg',1,11),('set_do_ngu_dai_tay.jpeg',1,12),
('set_cong_tu.jpeg',1,13),('ao_thun_polo_kid.jpeg',1,14),('quan_jean_baggy.jpeg',1,15),
('ao_hoodie_ni_bong.jpeg',0,16),('ao_minecraft.jpeg',0,17),('vay_cong_chua_voan.jpeg',0,18),
('vay_hoa.jpeg',0,19),('set_kem_mu.jpeg',0,20),('vay_yem_bo.jpeg',0,21),
('legging.jpeg',0,22),('giay_tap_di.jpeg',0,23),('sandals_be_trai.jpeg',0,24),
('bit_tat_thu_bong.jpeg',0,25),('mu_di_bien.jpeg',0,26),('balo_mam_non.jpeg',0,27),
('ao_phao.jpeg',0,28),('pijama.jpeg',0,29),('bo_len.jpeg',0,30),('ao_gile.jpeg',0,31),
('mu_len.jpeg',0,32),('set_do_boi.jpeg',0,33),('kinh_mat.jpeg',0,34),
('ao_so_mi.jpeg',0,35);
-- PRODUCT VARIANTS
INSERT INTO product_variants (product_id,size_id,stock_quantity) VALUES
(1,1,50),(1,2,40),(2,1,30),(2,2,25),
(3,1,60),(3,2,55),(4,1,20),(5,2,40),
(6,1,25),(7,1,10),(8,2,50),(8,3,40),
(9,2,60),(10,2,70),(11,2,80),(12,2,100),
(13,2,60),(14,6,40),(15,1,90),
(16,1,50),(16,2,30),(17,2,40),(18,1,25),(19,2,60),
(20,2,40),(21,1,30),(22,2,30),(23,3,25),(24,2,35),
(25,2,45),(26,2,55),(27,1,65),(28,1,20),(29,1,40),
(30,2,30),(31,1,50),(32,6,40),(33,1,70),(34,2,60),(35,1,90);

-- ADDRESSES
INSERT INTO addresses (user_id,address_line,city,district,ward) VALUES
(1,'12 Nguyen Trai','HCM','Q1','Ben Thanh'),
(2,'45 Le Loi','HCM','Q1','Ben Nghe'),
(3,'78 Hai Ba Trung','HCM','Q3','Vo Thi Sau'),
(4,'99 Tran Hung Dao','HCM','Q5','Ward 5'),
(5,'15 Phan Xich Long','HCM','Phu Nhuan','Ward 2'),
(6,'22 Dien Bien Phu','HCM','Binh Thanh','Ward 15'),
(7,'56 Nguyen Van Cu','HCM','Q5','Ward 2'),
(8,'88 CMT8','HCM','Q10','Ward 10'),
(9,'120 Hoang Van Thu','HCM','Tan Binh','Ward 4'),
(10,'200 Cong Hoa','HCM','Tan Binh','Ward 13');

-- CARTS
INSERT INTO carts (user_id,total_price,created_at) VALUES
(1,0,NOW()),(2,0,NOW()),(3,0,NOW()),(4,0,NOW()),(5,0,NOW()),
(6,0,NOW()),(7,0,NOW()),(8,0,NOW()),(9,0,NOW()),(10,0,NOW());

-- CART ITEMS
INSERT INTO cart_items (cart_id,product_variant_id,quantity) VALUES
(1,1,2),(2,2,1),(3,3,1),(4,4,2),(5,5,3),
(6,6,1),(7,7,2),(8,8,1),(9,9,2),(10,10,1);

-- ORDERS
INSERT INTO orders (
    id, 
    user_id, 
    is_guest, 
    email, 
    phone, 
    subtotal, 
    discount_amount, 
    shipping_fee, 
    total_price, 
    voucher_code, 
    order_status, 
    payment_method, 
    payment_status, 
    created_at
) VALUES
(1, 1, FALSE, 'admin@gmail.com', '0900000001', 240000, 0, 30000, 270000, NULL, 'COMPLETED', 'COD', 'PAID', NOW()),
(2, 2, FALSE, 'user1@gmail.com', '0900000002', 180000, 20000, 20000, 180000, 'SALE20', 'SHIPPING', 'COD', 'PENDING', NOW()),
(3, 3, FALSE, 'user2@gmail.com', '0900000003', 150000, 0, 15000, 165000, NULL, 'CONFIRMED', 'BANK_TRANSFER', 'PAID', NOW()),
(4, 4, FALSE, 'user3@gmail.com', '0900000004', 200000, 50000, 0, 150000, 'FREESHIP', 'PENDING', 'MOMO', 'PENDING', NOW()),
(5, 5, FALSE, 'user4@gmail.com', '0900000005', 300000, 0, 30000, 330000, NULL, 'CANCELLED', 'COD', 'PENDING', NOW()),
(6, 6, FALSE, 'user5@gmail.com', '0900000006', 200000, 0, 25000, 225000, NULL, 'COMPLETED', 'BANK_TRANSFER', 'PAID', NOW()),
(7, 7, FALSE, 'user6@gmail.com', '0900000007', 180000, 10000, 15000, 185000, 'PROMO10', 'SHIPPING', 'COD', 'PENDING', NOW()),
(8, 8, FALSE, 'user7@gmail.com', '0900000008', 320000, 0, 30000, 350000, NULL, 'PENDING', 'COD', 'PENDING', NOW()),
(9, 9, FALSE, 'user8@gmail.com', '0900000009', 210000, 21000, 0, 189000, 'DISCOUNT10', 'COMPLETED', 'VNPAY', 'PAID', NOW()),
(10, 10, FALSE, 'user9@gmail.com', '0900000010', 280000, 0, 20000, 300000, NULL, 'CONFIRMED', 'COD', 'PAID', NOW());

-- ORDER ITEMS
INSERT INTO order_items (order_id,product_variant_id,price,quantity) VALUES
(1,1,120000,2),(2,2,180000,1),(3,3,150000,1),(4,4,200000,1),
(5,5,300000,1),(6,6,200000,1),(7,7,180000,1),(8,8,320000,1),
(9,9,210000,1),(10,10,280000,1);

-- RATINGS
INSERT INTO ratings (user_id,product_id,star,comment,created_at) VALUES
(1,1,5,'Good',NOW()),(2,2,4,'Ok',NOW()),(3,3,5,'Nice',NOW()),
(4,4,3,'Normal',NOW()),(5,5,5,'Great',NOW()),(6,6,4,'Good',NOW()),
(7,7,5,'Perfect',NOW()),(8,8,4,'Ok',NOW()),(9,9,5,'Nice',NOW()),
(10,10,3,'Average',NOW());
