-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 06, 2025 at 12:37 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nixo_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `news`
--

CREATE TABLE `news` (
  `id` int(11) NOT NULL,
  `sharer_id` int(11) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `likes` int(11) DEFAULT 0,
  `status` varchar(20) DEFAULT 'pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `news`
--

INSERT INTO `news` (`id`, `sharer_id`, `title`, `content`, `image_url`, `created_at`, `likes`, `status`) VALUES
(4, NULL, '2nd attemp', 'entry in database and show news', NULL, '2025-10-29 09:08:26', 1, 'pending'),
(6, NULL, '3rd attemp', 'database handling with CRUD\r\n', NULL, '2025-10-29 10:01:07', 0, 'pending'),
(7, NULL, '4th attemp', 'Database interaction with CRUD\r\nUpdated News\r\nLets have long news\r\nNew line for timepass\r\nagain doing same timep[ass\r\ncurrently watching T20i \r\nIndia vs Australia', NULL, '2025-10-29 10:05:27', 0, 'pending'),
(8, NULL, 'Nixo: Simplifying the Digital Experience', 'Nixo is a unified platform that streamlines application deployment, monitoring, and collaboration â enabling teams to build and scale faster with less complexity.', NULL, '2025-10-29 10:42:30', 1, 'pending'),
(9, NULL, 'Your Smart Productivity Partner (title updated)', 'Nixo leverages AI to automate repetitive tasks, manage schedules, and provide real-time insights, helping individuals and teams achieve more in less time.', NULL, '2025-10-29 10:43:17', 3, 'pending'),
(10, NULL, 'Cloud: Scalable. Secure. Seamless.', 'Nixo offers next-generation cloud hosting built for performance and security, empowering developers to deploy and manage applications effortlessly.', NULL, '2025-10-29 10:43:37', 1, 'pending'),
(11, NULL, 'Redefining Digital Finance', 'Nixo is a secure fintech platform that simplifies personal and business transactions with instant payments, budgeting tools, and transparent analytics.', NULL, '2025-10-29 10:43:52', 3, 'pending'),
(12, NULL, 'Connect Beyond Boundaries ', 'Updated - Nixo is a modern social hub where communities grow through meaningful interactions, shared interests, and innovative content-sharing tools.', NULL, '2025-10-29 10:44:08', 5, 'pending'),
(13, NULL, 'New News today', 'Breaking News\r\ntoday login and register is completed', NULL, '2025-10-30 11:30:21', 2, 'pending'),
(14, 4, 'New News Today', 'Updated Chatgpt chat for fast response\r\nedit thay che', NULL, '2025-10-30 14:07:16', 3, 'pending'),
(17, 4, 'Why more mega-prisons wonÃ¢ÂÂt fix AlabamaÃ¢ÂÂs crisis', 'In 2024 alone, 277 people died in Alabama prisons Dakarai Larriett, US Senate candidate in Alabama, says. Building mega-prisons, naming one after governor Kay Ivy, is not the solution.', NULL, '2025-11-04 12:11:46', 1, 'pending'),
(18, 1, 'News by Admin', 'This is admin ki special notice', NULL, '2025-11-05 11:47:06', 1, 'pending');

-- --------------------------------------------------------

--
-- Table structure for table `news_likes`
--

CREATE TABLE `news_likes` (
  `id` int(11) NOT NULL,
  `news_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `news_likes`
--

INSERT INTO `news_likes` (`id`, `news_id`, `user_id`, `created_at`) VALUES
(7, 14, 4, '2025-10-31 07:04:27'),
(8, 13, 4, '2025-10-31 07:04:31'),
(9, 12, 4, '2025-10-31 07:04:33'),
(11, 14, 3, '2025-11-04 11:58:11'),
(12, 10, 3, '2025-11-04 11:58:13'),
(13, 17, 4, '2025-11-04 12:11:52'),
(15, 13, 5, '2025-11-05 09:58:04'),
(17, 14, 5, '2025-11-05 10:29:40'),
(18, 18, 1, '2025-11-05 11:47:09');

-- --------------------------------------------------------

--
-- Table structure for table `role_master`
--

CREATE TABLE `role_master` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `role_master`
--

INSERT INTO `role_master` (`role_id`, `role_name`) VALUES
(1, 'admin'),
(3, 'receiver'),
(2, 'sharer');

-- --------------------------------------------------------

--
-- Table structure for table `sharer_requests`
--

CREATE TABLE `sharer_requests` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `reason` text DEFAULT NULL,
  `status` varchar(20) DEFAULT 'pending',
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sharer_requests`
--

INSERT INTO `sharer_requests` (`id`, `user_id`, `reason`, `status`, `created_at`) VALUES
(1, 4, 'demo sharer', 'rejected', '2025-10-31 12:52:07'),
(2, 3, 'accept the request', 'approved', '2025-10-31 12:53:18'),
(3, 4, 'asdsd', 'rejected', '2025-11-04 12:51:35'),
(4, 4, 'demo - request\r\n', 'rejected', '2025-11-04 17:49:48'),
(5, 5, 'demo 2 sharer', 'rejected', '2025-11-05 17:12:52'),
(6, 5, 'again demo2 sharer', 'approved', '2025-11-05 17:21:08'),
(7, 4, 'demo1 want to sharer', 'approved', '2025-11-05 17:49:37');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` int(11) NOT NULL,
  `status` enum('active','inactive','pending') DEFAULT 'active',
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `name`, `email`, `password`, `role_id`, `status`, `created_at`, `updated_at`) VALUES
(1, 'Krishna', 'kp123@gmail.com', '123', 1, 'active', '2025-10-30 15:59:23', '2025-10-30 20:18:34'),
(3, 'krishna', 'kp111@gmail.com', '123', 2, 'active', '2025-10-30 16:14:47', '2025-11-04 17:50:48'),
(4, 'demo1', 'demo1@gmail.com', '123', 2, 'active', '2025-10-30 19:30:51', '2025-11-05 17:50:28'),
(5, 'Demo2', 'demo2@gmail.com', '123', 2, 'active', '2025-11-05 15:27:39', '2025-11-05 17:21:30');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `news`
--
ALTER TABLE `news`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_news_sharer` (`sharer_id`);

--
-- Indexes for table `news_likes`
--
ALTER TABLE `news_likes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_news_user` (`news_id`,`user_id`),
  ADD KEY `fk_nl_user` (`user_id`);

--
-- Indexes for table `role_master`
--
ALTER TABLE `role_master`
  ADD PRIMARY KEY (`role_id`),
  ADD UNIQUE KEY `role_name` (`role_name`);

--
-- Indexes for table `sharer_requests`
--
ALTER TABLE `sharer_requests`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `role_id` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `news`
--
ALTER TABLE `news`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `news_likes`
--
ALTER TABLE `news_likes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `role_master`
--
ALTER TABLE `role_master`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `sharer_requests`
--
ALTER TABLE `sharer_requests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `news`
--
ALTER TABLE `news`
  ADD CONSTRAINT `fk_news_sharer` FOREIGN KEY (`sharer_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `news_likes`
--
ALTER TABLE `news_likes`
  ADD CONSTRAINT `fk_nl_news` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_nl_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `sharer_requests`
--
ALTER TABLE `sharer_requests`
  ADD CONSTRAINT `sharer_requests_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role_master` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
