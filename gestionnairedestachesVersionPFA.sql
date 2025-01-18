-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 15 jan. 2025 à 00:30
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestionnairedestaches`
--

-- --------------------------------------------------------

--
-- Structure de la table `project`
--

CREATE TABLE `project` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `project_owner_id` int(11) DEFAULT NULL,
  `project_team_id` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `project`
--

INSERT INTO `project` (`id`, `nom`, `project_owner_id`, `project_team_id`, `description`) VALUES
(67, 'P1 Hamza', 16, 20, 'P1Description'),
(68, 'P2 Hamza', 16, 19, 'P2 Description'),
(69, 'P1 Aitahmed', 17, 22, 'P1 Description'),
(70, 'P1 Amina', 19, 16, 'P1 Description'),
(71, 'P1 ibtissam', 18, 19, 'P1 Description'),
(72, 'P1 Said', 20, 21, 'P1 Description'),
(73, 'P2 Said', 20, 16, 'P2 Description'),
(74, 'P1 Karim', 21, 16, 'P1 Description'),
(75, 'P2 Karim', 21, NULL, 'P2 Description'),
(78, 'P3 Hamza', 16, NULL, 'P3 No Description No Need  Team');

-- --------------------------------------------------------

--
-- Structure de la table `task`
--

CREATE TABLE `task` (
  `id` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `project_task_id` int(11) DEFAULT NULL,
  `user_task_id` int(11) DEFAULT NULL,
  `task_done` bit(1) NOT NULL,
  `utilisateur_task_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `task`
--

INSERT INTO `task` (`id`, `description`, `nom`, `project_task_id`, `user_task_id`, `task_done`, `utilisateur_task_id`) VALUES
(53, 'T1 Description', 'T1 P1', 67, 17, b'1', NULL),
(54, 'T2 Description', 'T2 P1', 67, 19, b'1', NULL),
(55, 'T3 Description', 'T3 P3', 67, 22, b'1', NULL),
(56, 'T4 Description', 'T4 P4', 67, 21, b'1', NULL),
(57, 'T1 Descrition', 'T1 P2', 68, 16, b'0', NULL),
(59, 'T2 Descrition', 'T2 P2', 68, 18, b'0', NULL),
(60, 'T3 Descrition', 'T3 P2', 68, 17, b'1', NULL),
(61, 'T4 Descrition', 'T4 P2', 68, 22, b'1', NULL),
(66, 'T1 Description', 'T 1 P1', 69, NULL, b'0', NULL),
(67, 'T2 Description', 'T 2 P1', 69, NULL, b'0', NULL),
(68, 'T3 Description', 'T 3 P1', 69, NULL, b'0', NULL),
(69, 'T4 Description', 'T 4 P1', 69, NULL, b'0', NULL),
(70, 'T1 Description', 'T1  P1', 71, NULL, b'0', NULL),
(71, 'T2 Description', 'T2  P1', 71, NULL, b'0', NULL),
(72, 'T3 Description', 'T3  P1', 71, NULL, b'0', NULL),
(73, 'T4 Description', 'T4  P1', 71, NULL, b'0', NULL),
(74, 'T1 Description', 'T1   P1', 70, NULL, b'0', NULL),
(75, 'T2 Description', 'T2   P1', 70, NULL, b'0', NULL),
(76, 'T3 Description', 'T3   P1', 70, NULL, b'0', NULL),
(77, 'T4 Description', 'T4   P1', 70, NULL, b'0', NULL),
(78, 'T1 Description', 'T1-P1', 72, NULL, b'0', NULL),
(79, 'T2 Description', 'T2-P1', 72, NULL, b'0', NULL),
(80, 'T3 Description', 'T3-P1', 72, NULL, b'0', NULL),
(81, 'T4 Description', 'T4-P1', 72, NULL, b'0', NULL),
(86, 'T1 Description', 'T1&P1', 73, NULL, b'0', NULL),
(87, 'T2 Description', 'T2&P1', 73, NULL, b'0', NULL),
(88, 'T3 Description', 'T3&P1', 73, NULL, b'0', NULL),
(89, 'T4 Description', 'T4&P1', 73, NULL, b'0', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `team`
--

CREATE TABLE `team` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `leader_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `team`
--

INSERT INTO `team` (`id`, `nom`, `leader_id`) VALUES
(16, 'T1 Hamza', 16),
(17, 'T2 Hamza', 16),
(18, 'T1 Aitahmed', 17),
(19, 'T2 Aitahmed', 17),
(20, 'T1 ibtissam', 18),
(21, 'T2 ibtissam', 18),
(22, 'T1 Amina', 19),
(23, 'T1 Karim', 21),
(24, 'T2 Karim', 21),
(25, 'test team', 16);

-- --------------------------------------------------------

--
-- Structure de la table `team_members`
--

CREATE TABLE `team_members` (
  `teams_id` int(11) NOT NULL,
  `members_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `team_members`
--

INSERT INTO `team_members` (`teams_id`, `members_id`) VALUES
(20, 16),
(20, 17),
(20, 19),
(20, 20),
(20, 21),
(20, 22),
(19, 16),
(19, 17),
(19, 18),
(19, 20),
(20, 21),
(19, 22),
(19, 23),
(22, 16),
(22, 17),
(22, 18),
(22, 21),
(22, 23),
(21, 16),
(21, 17),
(21, 18),
(21, 19),
(21, 20),
(21, 22),
(21, 23),
(17, 16),
(17, 18),
(17, 19),
(17, 20),
(17, 21),
(17, 22),
(17, 23),
(17, 17),
(17, 24),
(16, 18),
(16, 19),
(16, 20),
(16, 22),
(16, 21),
(16, 23),
(16, 16),
(16, 25),
(16, 27),
(16, 24),
(16, 26),
(16, 17),
(16, 29);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `email`, `password`, `role`, `username`, `photo`, `first_name`, `gender`, `last_name`, `date_of_birth`) VALUES
(16, 'hamza@gmail.com', '$2a$10$vvIsfW6rkwxknDYOJ6b.fuwINLYplRuJvfCozCJqIwbI/3UJlRXPS', NULL, 'Hamza.Aitahmed', 'images/user/user-1.jpg', 'hamza', 'Male', 'Aitahmed', '2003-01-09'),
(17, 'aitahmed@gmail.com', NULL, NULL, 'Ait Ahmed', 'images/user/user-2.jpg', NULL, NULL, NULL, '2024-07-10'),
(18, 'user@gmail.com', NULL, NULL, 'old user', 'images/user/user-3.jpg', NULL, NULL, NULL, '2024-07-05'),
(19, 'Amina@gmail.com', NULL, NULL, 'Amina', 'images/user/user-4.jpg', NULL, NULL, NULL, NULL),
(20, 'Said@gmail.com', NULL, NULL, 'Said', 'images/user/11.jpg', NULL, NULL, NULL, NULL),
(21, 'Karim@gmail.com', NULL, NULL, 'Karim', 'images/user/07.jpg', NULL, NULL, NULL, NULL),
(22, 'Reda@gmail.com', NULL, NULL, 'Reda', 'images/user/08.jpg', NULL, NULL, NULL, NULL),
(23, 'Khadija@gmail.com', NULL, NULL, 'Khadija', 'images/user/user-6.jpg', NULL, NULL, NULL, NULL),
(24, 'user9@gmail.com', '123', 'USER', 'old user', 'images/user/inconnu.jpg', NULL, NULL, NULL, '2024-07-06'),
(25, 'hamzaaitahmed003@gmail.com', '123', 'USER', 'hamza#03', 'images/user/inconnu.jpg', NULL, NULL, NULL, NULL),
(26, 'hamzaaitahmed030@gmail.com', '123', 'USER', 'hamzaTest', 'images/user/inconnu.jpg', NULL, NULL, NULL, NULL),
(27, 'hamzaaitahmed023@gmail.com', '123', 'USER', 'hamza003', 'images/user/inconnu.jpg', NULL, NULL, NULL, NULL),
(28, 'hamzaaitahmed03@gmail.com', '123', 'USER', 'Hamza.aitahmed', 'images/user/inconnu.jpg', NULL, NULL, NULL, NULL),
(29, 'messouk@gmail.com', '123', 'USER', 'youness messouk', 'images/user/inconnu.jpg', NULL, NULL, NULL, NULL),
(30, 'user123@gmail.com', '123', 'USER', 'user123', 'images/user/inconnu.jpg', NULL, NULL, NULL, NULL),
(31, 'admin@gmail.com', '$2a$10$vvIsfW6rkwxknDYOJ6b.fuwINLYplRuJvfCozCJqIwbI/3UJlRXPS', 'USER', 'admin', 'images/user/11.jpg', NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_hwpqw2xxca9vf4e2i4pemqa2l` (`nom`),
  ADD KEY `FKr9bla10to32v31i1ny6g0usy2` (`project_owner_id`),
  ADD KEY `FK7mc6oph44sqsib3scogeyfm4g` (`project_team_id`);

--
-- Index pour la table `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_rmwdufunmsq2qenvnl7fl72ur` (`nom`),
  ADD KEY `FK59ieikk11a5cpod5a7xh5jpv3` (`project_task_id`),
  ADD KEY `FKmoiq7f7vc7oca8ayxj5f0piml` (`user_task_id`),
  ADD KEY `FK3h98r4uh0a9j540swpqca5pcq` (`utilisateur_task_id`);

--
-- Index pour la table `team`
--
ALTER TABLE `team`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_42yi1itine2l3443ifceadlto` (`nom`),
  ADD KEY `FKbxs8rhdluvnucyymbjowulrl6` (`leader_id`);

--
-- Index pour la table `team_members`
--
ALTER TABLE `team_members`
  ADD KEY `FKbgffnx0q37cxji0csajwwcw3y` (`members_id`),
  ADD KEY `FKrtuq4eqoei67xi2vacbcrmvma` (`teams_id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_rma38wvnqfaf66vvmi57c71lo` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `project`
--
ALTER TABLE `project`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=85;

--
-- AUTO_INCREMENT pour la table `task`
--
ALTER TABLE `task`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT pour la table `team`
--
ALTER TABLE `team`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `FK7mc6oph44sqsib3scogeyfm4g` FOREIGN KEY (`project_team_id`) REFERENCES `team` (`id`),
  ADD CONSTRAINT `FKr9bla10to32v31i1ny6g0usy2` FOREIGN KEY (`project_owner_id`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `FK3h98r4uh0a9j540swpqca5pcq` FOREIGN KEY (`utilisateur_task_id`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `FK59ieikk11a5cpod5a7xh5jpv3` FOREIGN KEY (`project_task_id`) REFERENCES `project` (`id`),
  ADD CONSTRAINT `FKmoiq7f7vc7oca8ayxj5f0piml` FOREIGN KEY (`user_task_id`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `team`
--
ALTER TABLE `team`
  ADD CONSTRAINT `FKbxs8rhdluvnucyymbjowulrl6` FOREIGN KEY (`leader_id`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `team_members`
--
ALTER TABLE `team_members`
  ADD CONSTRAINT `FKbgffnx0q37cxji0csajwwcw3y` FOREIGN KEY (`members_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKrtuq4eqoei67xi2vacbcrmvma` FOREIGN KEY (`teams_id`) REFERENCES `team` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
