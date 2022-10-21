-- TEMPLATE
INSERT INTO `template` (`id`, `created_date`, `updated_date`, `type`)
VALUES ( 1, '2022-08-03 01:01:22.215212', '2022-08-03 01:02:29.269108', 'BASIC'),
       ( 2, '2022-08-03 02:02:32.490739', '2022-08-17 05:33:20.697407', 'BASIC'),
       ( 3, '2022-08-11 09:35:22.773119', '2022-08-12 01:12:55.121914', 'BASIC'),
       ( 4, '2022-08-03 01:01:22.215212', '2022-08-03 01:02:29.269108', 'BASIC'),
       ( 5, '2022-08-03 02:02:32.490739', '2022-08-17 05:33:20.697407', 'BASIC'),
       ( 6, '2022-08-11 09:35:22.773119', '2022-08-12 01:12:55.121914', 'BASIC'),
       ( 7, '2022-08-03 01:01:22.215212', '2022-08-03 01:02:29.269108', 'BASIC'),
       ( 8, '2022-08-03 02:02:32.490739', '2022-08-17 05:33:20.697407', 'BASIC'),
       ( 9, '2022-08-11 09:35:22.773119', '2022-08-12 01:12:55.121914', 'BASIC'),
       (10, '2022-08-03 01:01:22.215212', '2022-08-03 01:02:29.269108', 'BASIC'),
       (11, '2022-08-03 02:02:32.490739', '2022-08-17 05:33:20.697407', 'BASIC'),
       (12, '2022-08-11 09:35:22.773119', '2022-08-12 01:12:55.121914', 'BASIC'),
       (13, '2022-08-03 01:01:22.215212', '2022-08-03 01:02:29.269108', 'BASIC'),
       (14, '2022-08-03 02:02:32.490739', '2022-08-17 05:33:20.697407', 'BASIC'),
       (15, '2022-08-11 09:35:22.773119', '2022-08-12 01:12:55.121914', 'BASIC'),
       (16, '2022-08-03 01:01:22.215212', '2022-08-03 01:02:29.269108', 'BASIC'),
       (17, '2022-08-03 02:02:32.490739', '2022-08-17 05:33:20.697407', 'BASIC'),
       (18, '2022-08-11 09:35:22.773119', '2022-08-12 01:12:55.121914', 'BASIC'),
       (19, '2022-08-03 01:01:22.215212', '2022-08-03 01:02:29.269108', 'BASIC'),
       (20, '2022-08-03 02:02:32.490739', '2022-08-17 05:33:20.697407', 'BASIC'),
       (21, '2022-08-11 09:35:22.773119', '2022-08-12 01:12:55.121914', 'BASIC'),
       (22, '2022-08-03 01:01:22.215212', '2022-08-03 01:02:29.269108', 'BASIC'),
       (23, '2022-08-03 02:02:32.490739', '2022-08-17 05:33:20.697407', 'BASIC');


INSERT INTO `editorial` (`id`, `created_date`, `updated_date`, `status`, `title`, `uuid`, `template_id`, `editor_id`, `image_url`, `description`)
VALUES
    (1, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 'OPENED', 'Adidas vs Nike: The NFT Proxy Wars.', '3a7566fb-b5a1-4746-80b1-3eaa8a286a3e', 1, 10, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/25/image+37.png', 'Who doesn’t love a good rivalry? McDonald’s & Burger King, Coke & Pepsi, British Airways & Virgin Atlantic, Barcelona & Real Madrid facing off in El Clasico, Manchester United & Liverpool’s fiery clashes, and Australia & England competing for the Ashes may spring to mind. Alternatively, DC Comics and Marvel Comics have been locked in an 80+ year battle to control the comics space.\n\nAnd then you have Adidas & Nike. Two juggernauts who for nearly 6 decades influenced sportswear and shaped the cultural landscape and now jostle for domination over a market worth upwards of $300 billion.\n\nThe battle between the swoosh vs the three stripes evolved over the decades, whether it was through design, ads, or sponsorships. Recently that battle has spilled over into a new arena with Adidas and Nike both entering the Web3 space in December 2021, and there’s no sign of the rivalry slowing down.'),
    (2, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 'OPENED', 'What is on the Otherside?', '4a7566fb-b5a1-4746-80b1-3eaa8a286a3e'          , 2, 11, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/25/image+38.png', 'On the 16th of July 2022, 4,500 users - better known as “voyagers” - from across the globe, took part in what is being dubbed The First Trip.\n\nAnd no, we’re not talking about your first heavy weekend in Berghain. Where did they go you ask? Otherside - a Metaverse forged by Yuga Labs, the company behind NFT franchise, The Bored Ape Yacht Club.\n\nSo what makes Otherside and this event so significant? And why should non-crypto heads be paying attention? Let’s dive in.'),
	(3, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 'OPENED', 'What is a smart contract?', '5a7566fb-b5a1-4746-80b1-3eaa8a286a3e'          , 3, 12, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/25/Frame+1000001551.png', 'Smart contracts are an integral part of NFT infrastructure - without them NFTs cannot operate. They are made up of code that allow the storage and display of information, visible to anyone, as well as the execution of pre-defined transactions. Following from the nature of blockchain, smart contracts are immutable, meaning they can’t be changed. If a smart contract is published containing code errors, it will remain that way forever.\n\nSmart contracts are used to determine certain actions, such as verifying ownership, showing token metadata, and enabling and recording transfers. They can also be extended to handle other useful and relevant use cases, such as royalty payments for an artist in future sales of an NFT. A useful example is royalty payments - because the smart contract can trace back all the way to the original artist, it is a great way of enabling perpetual royalty payments for artists long after they initially sell their artwork.');


INSERT INTO `content` (`id`, `created_date`, `updated_date`, `enabled`, `image_url`, `key_name`, `link`, `template_id`, `text`, `type`)
VALUES
    ( 1, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/25/001.png'                 , 'block1' , NULL,  1, NULL, 'BLOCK'),
    ( 2, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/25/002.png'                 , 'block2' , NULL,  2, NULL, 'BLOCK'),
    ( 3, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/25/003.png'                 , 'block3' , NULL,  3, NULL, 'BLOCK'),
	( 4, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details11.png', 'block4' , NULL,  4, NULL, 'BLOCK'),
	( 5, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details12.png', 'block5' , NULL,  5, NULL, 'BLOCK'),
	( 6, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details13.png', 'block6' , NULL,  6, NULL, 'BLOCK'),
    ( 7, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details14.png', 'block7' , NULL,  7, NULL, 'BLOCK'),
    ( 8, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details15.png', 'block8' , NULL,  8, NULL, 'BLOCK'),
    ( 9, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details16.png', 'block9' , NULL,  9, NULL, 'BLOCK'),
    (10, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details17.png', 'block10', NULL, 10, NULL, 'BLOCK'),
    (11, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details18.png', 'block11', NULL, 11, NULL, 'BLOCK'),
    (12, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details19.png', 'block12', NULL, 12, NULL, 'BLOCK'),
    (13, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details20.png', 'block13', NULL, 13, NULL, 'BLOCK'),
    (14, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details1.png',  'block14', NULL, 14, NULL, 'BLOCK'),
    (15, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details2.png',  'block15', NULL, 15, NULL, 'BLOCK'),
    (16, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details3.png',  'block16', NULL, 16, NULL, 'BLOCK'),
    (17, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details4.png',  'block17', NULL, 17, NULL, 'BLOCK'),
    (18, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details5.png',  'block18', NULL, 18, NULL, 'BLOCK'),
    (19, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details6.png',  'block19', NULL, 19, NULL, 'BLOCK'),
    (20, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details7.png',  'block20', NULL, 20, NULL, 'BLOCK'),
    (21, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details8.png',  'block21', NULL, 21, NULL, 'BLOCK'),
    (22, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details9.png',  'block22', NULL, 22, NULL, 'BLOCK'),
    (23, '2022-08-22 01:22:55', '2022-08-22 01:22:55', 1, 'https://mos-s3-dev.s3.ap-northeast-2.amazonaws.com/images/2022/8/24/Collection_Details10.png', 'block23', NULL, 23, NULL, 'BLOCK');
