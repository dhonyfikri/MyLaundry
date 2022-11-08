-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 08, 2022 at 09:56 AM
-- Server version: 10.4.13-MariaDB
-- PHP Version: 7.4.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dblaundry`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_laundry`
--

CREATE TABLE `tbl_laundry` (
  `id` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `alamat` text NOT NULL,
  `telepon` varchar(50) NOT NULL,
  `picture` varchar(200) NOT NULL DEFAULT '/laundry/image/image-laundry/none00.jpg',
  `last_update` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_laundry`
--

INSERT INTO `tbl_laundry` (`id`, `nama`, `alamat`, `telepon`, `picture`, `last_update`) VALUES
(105, 'Laundry Mewah', 'Jl. baru jadi', '085744613256', '/laundry/image/image-laundry/img105.jpeg', '2021-01-13'),
(106, 'Laundry Gradakan', 'Jl. Jauh disana', '081554666236', '/laundry/image/image-laundry/img106.jpeg', '2021-01-13'),
(107, 'Laundry Ku Sayang', 'Jl. Rindu doi ku sayang', '087446655592', '/laundry/image/image-laundry/img107.jpeg', '2021-01-13'),
(108, 'Laundry Bussiku', 'Jl. telateni boy', '085266594885', '/laundry/image/image-laundry/img108.jpeg', '2021-01-13'),
(110, 'Laundry Nona Bersih', 'Jl. Candani Asih Resik', '085244167948', '/laundry/image/image-laundry/img110.jpeg', '2021-01-13'),
(111, 'Laundry Akbar', 'Jl. Mangun Jaya', '087445784585', '/laundry/image/image-laundry/img111.jpeg', '2021-01-13'),
(113, 'Laundry Masroh Barok', 'Jl. Adityawarman', '087445635692', '/laundry/image/image-laundry/img113.jpeg', '2021-01-13'),
(114, 'Laundry Jayamahe', 'Jl. Batalyon 3', '085746599532', '/laundry/image/image-laundry/img114.jpeg', '2021-01-13');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user`
--

CREATE TABLE `tbl_user` (
  `id_user` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password_user` varchar(20) NOT NULL,
  `nama_user` varchar(50) NOT NULL,
  `alamat_user` varchar(100) NOT NULL,
  `foto_user` varchar(200) NOT NULL DEFAULT '/laundry/image/image-user/none.jpeg',
  `last_update` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`id_user`, `username`, `password_user`, `nama_user`, `alamat_user`, `foto_user`, `last_update`) VALUES
(13, 'admin', 'admin', 'administrator', 'KumbahKu Laboratory', '/laundry/image/image-user/img13.jpeg', '2021-01-13'),
(14, 'admin2', 'admin', 'Abdul Baihaqi', 'Pekalongan', '/laundry/image/image-user/img14.jpeg', '2021-01-13'),
(15, 'admin3', 'admin', 'Lina Marisa', 'Cirebon', '/laundry/image/image-user/img15.jpeg', '2021-01-13'),
(16, 'admin4', 'admin', 'Budi Santoso', 'Jakarta', '/laundry/image/image-user/img16.jpeg', '2021-01-13'),
(17, 'admin5', 'admin', 'Zaki Ferdiansah', 'Bandung', '/laundry/image/image-user/img17.jpeg', '2021-01-13'),
(18, 'admin6', 'admin', 'Bagus Arya', 'Banyuwangi', '/laundry/image/image-user/img18.jpeg', '2021-01-13'),
(19, 'bayu@iskandar', 'admin', 'Bayu Iskandar Muda Jaya', 'Jl. Ketintang dekat telkom', '/laundry/image/image-user/img19.jpeg', '2021-01-13'),
(20, 'dhy', '123', 'doni', 'ketintang', '/laundry/image/image-user/none.jpeg', '2021-03-17'),
(21, '', '1', 'doni', 'doni', '/laundry/image/image-user/none.jpeg', '2021-03-17'),
(22, '', '1', 'doni', 'doni', '/laundry/image/image-user/none.jpeg', '2021-03-17'),
(23, 'qwe', '1', 'doni', 'doni', '/laundry/image/image-user/none.jpeg', '2021-03-17');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_laundry`
--
ALTER TABLE `tbl_laundry`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_laundry`
--
ALTER TABLE `tbl_laundry`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- AUTO_INCREMENT for table `tbl_user`
--
ALTER TABLE `tbl_user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
