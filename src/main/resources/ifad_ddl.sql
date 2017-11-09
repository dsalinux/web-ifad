CREATE DATABASE  IF NOT EXISTS `ifad` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `ifad`;
-- MySQL dump 10.13  Distrib 5.7.20, for Linux (x86_64)
--
-- Host: localhost    Database: ifad
-- ------------------------------------------------------
-- Server version	5.7.20-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `configuracao`
--

DROP TABLE IF EXISTS `configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuracao` (
  `id` int(11) NOT NULL,
  `bloqueado` bit(1) DEFAULT NULL,
  `data_fim` date DEFAULT NULL,
  `data_inicio` date DEFAULT NULL,
  `questionario_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK45C799AF75BC6BD0` (`questionario_id`),
  CONSTRAINT `FK45C799AF75BC6BD0` FOREIGN KEY (`questionario_id`) REFERENCES `questionario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `foto`
--

DROP TABLE IF EXISTS `foto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `foto` (
  `id` int(11) NOT NULL,
  `bytes` longblob NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `professor`
--

DROP TABLE IF EXISTS `professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `professor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `campus_lotacao` varchar(255) NOT NULL,
  `cargo_funcao` varchar(255) NOT NULL,
  `classe_nivel_atual` varchar(255) DEFAULT NULL,
  `codigo_nivel` varchar(255) DEFAULT NULL,
  `data_ultima_progressao` date DEFAULT NULL,
  `matricula_siape` varchar(255) DEFAULT NULL,
  `nome` varchar(255) NOT NULL,
  `foto_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC3DDD66FAF4E5070` (`foto_id`),
  CONSTRAINT `FKC3DDD66FAF4E5070` FOREIGN KEY (`foto_id`) REFERENCES `foto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `questao`
--

DROP TABLE IF EXISTS `questao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) NOT NULL,
  `dica` varchar(255) DEFAULT NULL,
  `opcoes` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `questionario_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK270C749075BC6BD0` (`questionario_id`),
  CONSTRAINT `FK270C749075BC6BD0` FOREIGN KEY (`questionario_id`) REFERENCES `questionario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `questionario`
--

DROP TABLE IF EXISTS `questionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questionario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `respostas`
--

DROP TABLE IF EXISTS `respostas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `respostas` (
  `professor_id` int(11) NOT NULL,
  `questao_id` int(11) NOT NULL,
  `senha_id` int(11) NOT NULL,
  `data_criacao` datetime NOT NULL,
  `resposta` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`professor_id`,`questao_id`,`senha_id`),
  KEY `FK8B2DBD729473CBE4` (`professor_id`),
  KEY `FK8B2DBD72B369A6C4` (`questao_id`),
  KEY `FK8B2DBD7266004F24` (`senha_id`),
  CONSTRAINT `FK8B2DBD7266004F24` FOREIGN KEY (`senha_id`) REFERENCES `senha` (`id`),
  CONSTRAINT `FK8B2DBD729473CBE4` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`id`),
  CONSTRAINT `FK8B2DBD72B369A6C4` FOREIGN KEY (`questao_id`) REFERENCES `questao` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `senha`
--

DROP TABLE IF EXISTS `senha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `senha` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_finalizacao_resposta` datetime DEFAULT NULL,
  `senha` varchar(255) NOT NULL,
  `turma_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK684225523747524` (`turma_id`),
  CONSTRAINT `FK684225523747524` FOREIGN KEY (`turma_id`) REFERENCES `turma` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipos_respostas`
--

DROP TABLE IF EXISTS `tipos_respostas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipos_respostas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `tipo_resposta` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipos_respostas_item`
--

DROP TABLE IF EXISTS `tipos_respostas_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipos_respostas_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `tipo_respostas_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tipos_respostas_item_tipos_resposta_idx` (`tipo_respostas_id`),
  CONSTRAINT `fk_tipos_respostas_item_tipos_resposta` FOREIGN KEY (`tipo_respostas_id`) REFERENCES `tipos_respostas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `turma`
--

DROP TABLE IF EXISTS `turma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `turma` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `curso` varchar(255) NOT NULL,
  `nome` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `turma_has_professor`
--

DROP TABLE IF EXISTS `turma_has_professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `turma_has_professor` (
  `turma_id` int(11) NOT NULL,
  `professor_id` int(11) NOT NULL,
  KEY `FKF97777B023747524` (`turma_id`),
  KEY `FKF97777B09473CBE4` (`professor_id`),
  CONSTRAINT `FKF97777B023747524` FOREIGN KEY (`turma_id`) REFERENCES `turma` (`id`),
  CONSTRAINT `FKF97777B09473CBE4` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alterar_senha` bit(1) NOT NULL,
  `data_cadastro` datetime NOT NULL,
  `data_desativacao` datetime DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `login` varchar(255) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `permissoes` varchar(255) DEFAULT NULL,
  `senha` varchar(255) NOT NULL,
  `tema` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO ifad.usuario values ('3', b'1', now(), NULL, 'avalicao@ifnmg.edu.br', 'avaliacao', 'Avaliacao', 'ADMIN', '123', 'aristo');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-09 17:08:25


