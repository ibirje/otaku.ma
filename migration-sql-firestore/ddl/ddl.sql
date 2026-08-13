-- otaku_ma.admin definition

CREATE TABLE `admin` (
  `adminID` int(11) NOT NULL AUTO_INCREMENT,
  `adminRoleID` int(11) NOT NULL,
  `nom` varchar(60) NOT NULL,
  `prenom` varchar(60) NOT NULL,
  `email` varchar(255) NOT NULL,
  `CIN` varchar(12) NOT NULL,
  PRIMARY KEY (`adminID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;


-- otaku_ma.admin_droit definition

CREATE TABLE `admin_droit` (
  `adminDroitID` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `droit` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`adminDroitID`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;


-- otaku_ma.admin_role definition

CREATE TABLE `admin_role` (
  `adminRoleID` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`adminRoleID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;


-- otaku_ma.avis definition

CREATE TABLE `avis` (
  `avisID` int(11) NOT NULL AUTO_INCREMENT,
  `clientID` int(11) NOT NULL,
  `produitID` int(11) NOT NULL,
  `note` int(2) NOT NULL,
  `commentaire` text NOT NULL,
  `class` varchar(255) NOT NULL,
  PRIMARY KEY (`avisID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- otaku_ma.categorie definition

CREATE TABLE `categorie` (
  `categorieID` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `nombreProduits` int(11) NOT NULL DEFAULT '0',
  `qte` int(11) NOT NULL DEFAULT '0',
  `pendingQte` int(11) NOT NULL DEFAULT '0',
  `activeQte` int(11) NOT NULL DEFAULT '0',
  `categorieParent` int(11) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `description` text,
  `isActive` tinyint(1) NOT NULL DEFAULT '1',
  `smallImage` varchar(255) DEFAULT NULL,
  `mediumImage` varchar(255) DEFAULT NULL,
  `largeImage` varchar(255) DEFAULT 'https://images2.imgbox.com/4a/f7/irxfjmVw_o.jpg',
  `extra1` varchar(255) DEFAULT NULL,
  `extra2` varchar(255) DEFAULT NULL,
  `extra3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`categorieID`),
  UNIQUE KEY `code` (`code`),
  KEY `nom` (`nom`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;


-- otaku_ma.client definition

CREATE TABLE `client` (
  `clientID` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pseudo` varchar(60) DEFAULT NULL,
  `telephone1` varchar(100) DEFAULT NULL,
  `telephone2` varchar(100) DEFAULT NULL,
  `dateNaissance` date DEFAULT NULL,
  `notes` text,
  `isActive` tinyint(1) NOT NULL DEFAULT '1',
  `etat` varchar(100) NOT NULL DEFAULT 'NORMAL',
  `panierCount` int(11) NOT NULL DEFAULT '0',
  `extra` varchar(255) DEFAULT NULL,
  `dateCreation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`clientID`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=192 DEFAULT CHARSET=latin1;


-- otaku_ma.client_adresse definition

CREATE TABLE `client_adresse` (
  `adresseclientID` int(11) NOT NULL AUTO_INCREMENT,
  `clientID` int(11) NOT NULL,
  `etat` varchar(100) DEFAULT NULL,
  `prenom` varchar(100) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `organisation` varchar(100) DEFAULT NULL,
  `adresse1` varchar(255) NOT NULL,
  `adresse2` varchar(255) DEFAULT NULL,
  `ville` varchar(100) NOT NULL,
  `codePostal` varchar(100) DEFAULT NULL,
  `telephone1` varchar(100) DEFAULT NULL,
  `telephone2` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `extra` varchar(255) DEFAULT NULL,
  `notes` text,
  PRIMARY KEY (`adresseclientID`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=latin1;


-- otaku_ma.client_pending definition

CREATE TABLE `client_pending` (
  `clientpID` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `motdepasse` varchar(255) NOT NULL,
  `emailkey` varchar(255) NOT NULL,
  `facebookuid` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `dateInsertion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`clientpID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;


-- otaku_ma.coursier definition

CREATE TABLE `coursier` (
  `coursierID` int(11) NOT NULL AUTO_INCREMENT,
  `coursier` varchar(255) NOT NULL,
  `prixPL_LD` double DEFAULT NULL,
  `coutPL_LD` double DEFAULT NULL,
  `prixPL_PR` double DEFAULT NULL,
  `coutPL_PR` double DEFAULT NULL,
  `prixVB_LD` double DEFAULT NULL,
  `coutVB_LD` double DEFAULT NULL,
  `prixVB_PR` double DEFAULT NULL,
  `coutVB_PR` double DEFAULT NULL,
  `seuilPrixLivraisonGratuite` double NOT NULL DEFAULT '300',
  `ville` varchar(120) DEFAULT NULL,
  `codes` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`coursierID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;


-- otaku_ma.produit_audit definition

CREATE TABLE `produit_audit` (
  `auditProduitID` int(11) NOT NULL AUTO_INCREMENT,
  `operation` varchar(20) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `produitID` int(11) NOT NULL,
  `old_code` varchar(100) DEFAULT NULL,
  `old_nom` varchar(255) DEFAULT NULL,
  `old_keywords` varchar(255) DEFAULT NULL,
  `old_description` text,
  `old_shortDescription` varchar(255) DEFAULT NULL,
  `old_prixUnite` decimal(14,4) DEFAULT NULL,
  `old_prixPromo` decimal(14,4) DEFAULT NULL,
  `old_dateFinPromo` date DEFAULT NULL,
  `old_coutMoyen` decimal(14,4) DEFAULT NULL,
  `old_QTE` int(11) DEFAULT NULL,
  `old_thumbnail` varchar(255) DEFAULT NULL,
  `old_image1` varchar(255) DEFAULT NULL,
  `old_image2` varchar(255) DEFAULT NULL,
  `old_image3` varchar(255) DEFAULT NULL,
  `old_categorieID` int(11) DEFAULT NULL,
  `old_themeID` int(11) DEFAULT NULL,
  `old_isActive` tinyint(1) DEFAULT NULL,
  `new_code` varchar(100) DEFAULT NULL,
  `new_nom` varchar(255) DEFAULT NULL,
  `new_keywords` varchar(255) DEFAULT NULL,
  `new_description` text,
  `new_shortDescription` varchar(255) DEFAULT NULL,
  `new_prixUnite` decimal(14,4) DEFAULT NULL,
  `new_prixPromo` decimal(14,4) DEFAULT NULL,
  `new_dateFinPromo` date DEFAULT NULL,
  `new_coutMoyen` decimal(14,4) DEFAULT NULL,
  `new_QTE` int(11) DEFAULT NULL,
  `new_thumbnail` varchar(255) DEFAULT NULL,
  `new_image1` varchar(255) DEFAULT NULL,
  `new_image2` varchar(255) DEFAULT NULL,
  `new_image3` varchar(255) DEFAULT NULL,
  `new_categorieID` int(11) DEFAULT NULL,
  `new_themeID` int(11) DEFAULT NULL,
  `new_isActive` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`auditProduitID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- otaku_ma.sku_audit definition

CREATE TABLE `sku_audit` (
  `auditSkuID` int(11) NOT NULL AUTO_INCREMENT,
  `operation` varchar(20) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `skuID` int(11) DEFAULT NULL,
  `old_variationID` int(11) DEFAULT NULL,
  `old_achatID` int(11) DEFAULT NULL,
  `old_description` varchar(255) DEFAULT NULL,
  `old_qte` int(11) DEFAULT NULL,
  `old_code` varchar(100) DEFAULT NULL,
  `new_variationID` int(11) DEFAULT NULL,
  `new_achatID` int(11) DEFAULT NULL,
  `new_description` varchar(255) DEFAULT NULL,
  `new_qte` int(11) DEFAULT NULL,
  `new_code` varchar(100) DEFAULT NULL,
  `user` varchar(100) DEFAULT NULL,
  `userip` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`auditSkuID`),
  KEY `skuID` (`skuID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- otaku_ma.theme definition

CREATE TABLE `theme` (
  `themeID` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL,
  `nom` varchar(255) CHARACTER SET utf8 NOT NULL,
  `nombreProduits` int(11) NOT NULL DEFAULT '0',
  `qte` int(11) NOT NULL DEFAULT '0',
  `pendingQte` int(11) NOT NULL DEFAULT '0',
  `activeQte` int(11) NOT NULL DEFAULT '0',
  `description` text,
  `isActive` tinyint(1) NOT NULL DEFAULT '1',
  `smallImage` varchar(255) DEFAULT NULL,
  `mediumImage` varchar(255) DEFAULT NULL,
  `largeImage` varchar(255) DEFAULT 'https://images2.imgbox.com/4a/f7/irxfjmVw_o.jpg',
  `extra1` varchar(255) DEFAULT NULL,
  `extra2` varchar(255) DEFAULT NULL,
  `extra3` varchar(255) DEFAULT NULL,
  `themeParent` int(11) DEFAULT NULL,
  PRIMARY KEY (`themeID`),
  KEY `nom` (`nom`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1;


-- otaku_ma.variation_audit definition

CREATE TABLE `variation_audit` (
  `auditvariationID` int(11) NOT NULL AUTO_INCREMENT,
  `operation` varchar(20) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `variationID` int(11) NOT NULL,
  `old_produitID` int(11) DEFAULT NULL,
  `old_code` varchar(100) DEFAULT NULL,
  `old_nom` varchar(255) DEFAULT NULL,
  `old_isActive` tinyint(1) DEFAULT NULL,
  `old_thumbnail` varchar(255) DEFAULT NULL,
  `old_image` varchar(255) DEFAULT NULL,
  `old_prixUnite` decimal(14,4) DEFAULT NULL,
  `old_prixPromo` decimal(14,4) DEFAULT NULL,
  `old_qte` int(11) DEFAULT NULL,
  `new_produitID` int(11) DEFAULT NULL,
  `new_code` varchar(100) DEFAULT NULL,
  `new_nom` varchar(255) DEFAULT NULL,
  `new_isActive` tinyint(1) DEFAULT NULL,
  `new_thumbnail` varchar(255) DEFAULT NULL,
  `new_image` varchar(255) DEFAULT NULL,
  `new_prixUnite` decimal(14,4) DEFAULT NULL,
  `new_prixPromo` decimal(14,4) DEFAULT NULL,
  `new_qte` int(11) DEFAULT NULL,
  `user` varchar(100) DEFAULT NULL,
  `userip` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`auditvariationID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- otaku_ma.admin_login definition

CREATE TABLE `admin_login` (
  `adminLoginID` int(11) NOT NULL AUTO_INCREMENT,
  `adminID` int(11) NOT NULL,
  `pseudo` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `question` text NOT NULL,
  `reponse` text NOT NULL,
  PRIMARY KEY (`adminLoginID`),
  UNIQUE KEY `pseudo` (`pseudo`),
  KEY `fk_admin` (`adminID`),
  CONSTRAINT `fk_admin` FOREIGN KEY (`adminID`) REFERENCES `admin` (`adminID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;


-- otaku_ma.admin_roledroit definition

CREATE TABLE `admin_roledroit` (
  `roleDroitID` int(11) NOT NULL AUTO_INCREMENT,
  `adminRoleID` int(11) NOT NULL,
  `adminDroitID` int(11) NOT NULL,
  PRIMARY KEY (`roleDroitID`),
  KEY `fk_adminRoleID` (`adminRoleID`),
  KEY `fk_adminDroitID` (`adminDroitID`),
  CONSTRAINT `fk_adminDroitID` FOREIGN KEY (`adminDroitID`) REFERENCES `admin_droit` (`adminDroitID`),
  CONSTRAINT `fk_adminRoleID` FOREIGN KEY (`adminRoleID`) REFERENCES `admin_role` (`adminRoleID`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;


-- otaku_ma.client_login definition

CREATE TABLE `client_login` (
  `clientloginID` int(11) NOT NULL AUTO_INCREMENT,
  `clientID` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `motdepasse` varchar(255) NOT NULL,
  `facebookuid` varchar(255) DEFAULT NULL,
  `recupkey` varchar(255) DEFAULT NULL,
  `recupkeydate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`clientloginID`),
  UNIQUE KEY `facebookuid` (`facebookuid`),
  KEY `fk_clientloginclient` (`clientID`),
  CONSTRAINT `fk_clientloginclient` FOREIGN KEY (`clientID`) REFERENCES `client` (`clientID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=latin1;


-- otaku_ma.client_session definition

CREATE TABLE `client_session` (
  `clientsessionID` int(11) NOT NULL AUTO_INCREMENT,
  `clientID` int(11) NOT NULL,
  `cookie` varchar(128) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`clientsessionID`),
  KEY `fk_clientsession` (`clientID`),
  CONSTRAINT `fk_clientsession` FOREIGN KEY (`clientID`) REFERENCES `client` (`clientID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- otaku_ma.commande definition

CREATE TABLE `commande` (
  `commandeID` int(11) NOT NULL AUTO_INCREMENT,
  `clientID` int(11) NOT NULL,
  `itemCount` int(11) NOT NULL,
  `code` varchar(255) NOT NULL,
  `etat` varchar(20) NOT NULL DEFAULT 'ACCEPTATION',
  `prixPieces` decimal(14,4) NOT NULL DEFAULT '0.0000',
  `prixLivraison` decimal(14,4) NOT NULL DEFAULT '0.0000',
  `coutLivraison` decimal(14,4) NOT NULL DEFAULT '0.0000',
  `dateCommande` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `dateAccepte` timestamp NULL DEFAULT NULL,
  `datePrepare` timestamp NULL DEFAULT NULL,
  `dateEnvoi` timestamp NULL DEFAULT NULL,
  `dateFin` timestamp NULL DEFAULT NULL,
  `paye` tinyint(1) NOT NULL DEFAULT '0',
  `notes` varchar(255) DEFAULT NULL,
  `prenom` varchar(100) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `adresse1` varchar(255) NOT NULL,
  `adresse2` varchar(255) DEFAULT NULL,
  `ville` varchar(100) NOT NULL,
  `telephone1` varchar(100) NOT NULL,
  `codePostal` varchar(20) NOT NULL,
  `livraison` varchar(255) NOT NULL,
  PRIMARY KEY (`commandeID`),
  KEY `fk_commande_clientid` (`clientID`),
  KEY `etat` (`etat`),
  CONSTRAINT `fk_commande_clientid` FOREIGN KEY (`clientID`) REFERENCES `client` (`clientID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=latin1;


-- otaku_ma.commandeitem definition

CREATE TABLE `commandeitem` (
  `commandeitemID` int(11) NOT NULL AUTO_INCREMENT,
  `commandeID` int(11) NOT NULL,
  `variationID` int(11) DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `thumbnail` varchar(255) NOT NULL,
  `prixUnite` decimal(14,4) NOT NULL,
  `qte` int(11) NOT NULL,
  PRIMARY KEY (`commandeitemID`),
  KEY `fk_cmdid_cmd` (`commandeID`),
  CONSTRAINT `fk_cmdid_cmd` FOREIGN KEY (`commandeID`) REFERENCES `commande` (`commandeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=228 DEFAULT CHARSET=latin1;


-- otaku_ma.droitusedroit definition

CREATE TABLE `droitusedroit` (
  `dudID` int(11) NOT NULL AUTO_INCREMENT,
  `adminDroitID` int(11) NOT NULL,
  `usedDroitID` int(11) NOT NULL,
  PRIMARY KEY (`dudID`),
  KEY `fk_droitID` (`adminDroitID`),
  KEY `fk_usedDroitID` (`usedDroitID`),
  CONSTRAINT `fk_droitID` FOREIGN KEY (`adminDroitID`) REFERENCES `admin_droit` (`adminDroitID`) ON DELETE CASCADE,
  CONSTRAINT `fk_usedDroitID` FOREIGN KEY (`usedDroitID`) REFERENCES `admin_droit` (`adminDroitID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=latin1;


-- otaku_ma.fournisseur definition

CREATE TABLE `fournisseur` (
  `fournisseurID` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `titre` varchar(255) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `categorie1` int(11) NOT NULL,
  `categorie2` int(11) DEFAULT NULL,
  `categorie3` int(11) DEFAULT NULL,
  `service` varchar(255) NOT NULL,
  `description` text,
  `telephone` varchar(60) DEFAULT NULL,
  `watsapp` varchar(60) DEFAULT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `site` varchar(255) DEFAULT NULL,
  `hasLivraison` tinyint(1) NOT NULL DEFAULT '1',
  `email` varchar(255) DEFAULT NULL,
  `prix` decimal(14,4) DEFAULT NULL,
  `MOQ` int(11) DEFAULT NULL,
  `maxOQ` int(11) DEFAULT NULL,
  PRIMARY KEY (`fournisseurID`),
  UNIQUE KEY `code` (`code`),
  KEY `fk_fourniCategorie1` (`categorie1`),
  FULLTEXT KEY `titre` (`titre`),
  CONSTRAINT `fk_fourniCategorie1` FOREIGN KEY (`categorie1`) REFERENCES `categorie` (`categorieID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;


-- otaku_ma.produit definition

CREATE TABLE `produit` (
  `produitID` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `description` text,
  `shortDescription` varchar(255) DEFAULT NULL,
  `prixUnite` decimal(14,4) DEFAULT NULL,
  `prixPromo` decimal(14,4) NOT NULL,
  `dateDebutPromo` date DEFAULT NULL,
  `dateFinPromo` date DEFAULT NULL,
  `QTE` int(11) NOT NULL DEFAULT '0' COMMENT 'calculée après CRUD Variation',
  `pendingQte` int(11) NOT NULL DEFAULT '0',
  `lockedQte` int(11) NOT NULL DEFAULT '0',
  `thumbnail` varchar(255) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `image3` varchar(255) DEFAULT NULL,
  `categorieID` int(11) NOT NULL,
  `themeID` int(11) NOT NULL,
  `isActive` tinyint(1) NOT NULL DEFAULT '1',
  `hasVariations` tinyint(1) NOT NULL DEFAULT '0',
  `extra1` varchar(255) DEFAULT NULL,
  `extra2` varchar(255) DEFAULT NULL,
  `extra3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`produitID`),
  UNIQUE KEY `code` (`code`),
  UNIQUE KEY `nom_2` (`nom`),
  KEY `fk_prod_categorie` (`categorieID`),
  KEY `fk_prod_theme` (`themeID`),
  FULLTEXT KEY `nom` (`nom`),
  CONSTRAINT `fk_prod_categorie` FOREIGN KEY (`categorieID`) REFERENCES `categorie` (`categorieID`),
  CONSTRAINT `fk_prod_theme` FOREIGN KEY (`themeID`) REFERENCES `theme` (`themeID`)
) ENGINE=InnoDB AUTO_INCREMENT=425 DEFAULT CHARSET=latin1;


-- otaku_ma.produit_stats definition

CREATE TABLE `produit_stats` (
  `produitstatsID` int(11) NOT NULL AUTO_INCREMENT,
  `produitID` int(11) NOT NULL,
  `variationID` int(11) DEFAULT NULL,
  `achatstockQte` int(11) NOT NULL DEFAULT '0',
  `achatstockPrixTotal` decimal(14,4) NOT NULL DEFAULT '0.0000',
  `commandeQte` int(11) NOT NULL DEFAULT '0',
  `commandePrixTotal` decimal(14,4) NOT NULL DEFAULT '0.0000',
  `fraisSup` decimal(14,4) NOT NULL DEFAULT '0.0000',
  `lockedQte` int(11) NOT NULL DEFAULT '0',
  `stockQte` int(11) NOT NULL DEFAULT '0',
  `stockPrixMoyen` decimal(14,4) NOT NULL DEFAULT '0.0000',
  `pendingQte` int(11) NOT NULL DEFAULT '0',
  `panierQte` int(11) NOT NULL DEFAULT '0',
  `favorisQte` int(11) NOT NULL DEFAULT '0',
  `casseQte` int(11) NOT NULL DEFAULT '0',
  `perteQte` int(11) NOT NULL DEFAULT '0',
  `visiteCount` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`produitstatsID`),
  KEY `fk_produitid_prstats` (`produitID`),
  CONSTRAINT `fk_produitid_prstats` FOREIGN KEY (`produitID`) REFERENCES `produit` (`produitID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=727 DEFAULT CHARSET=utf8;


-- otaku_ma.sku_stats definition

CREATE TABLE `sku_stats` (
  `skuStatsID` int(11) NOT NULL AUTO_INCREMENT,
  `variationID` int(11) NOT NULL,
  `authAdminID` int(11) NOT NULL,
  `type` varchar(60) NOT NULL DEFAULT 'PRODUIT',
  `achatAdminID` int(11) NOT NULL,
  PRIMARY KEY (`skuStatsID`),
  KEY `fk_skustats_achatAdmin` (`achatAdminID`),
  CONSTRAINT `fk_skustats_achatAdmin` FOREIGN KEY (`achatAdminID`) REFERENCES `admin` (`adminID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- otaku_ma.token definition

CREATE TABLE `token` (
  `tokenID` int(11) NOT NULL AUTO_INCREMENT,
  `adminID` int(11) NOT NULL,
  `pseudo` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `debut` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fin` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`tokenID`),
  KEY `fk_adminid` (`adminID`),
  CONSTRAINT `fk_adminid` FOREIGN KEY (`adminID`) REFERENCES `admin` (`adminID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;


-- otaku_ma.variation definition

CREATE TABLE `variation` (
  `variationID` int(11) NOT NULL AUTO_INCREMENT,
  `produitID` int(11) NOT NULL,
  `code` varchar(100) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `isActive` tinyint(1) NOT NULL DEFAULT '1',
  `thumbnail` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `prixUnite` decimal(14,4) DEFAULT NULL,
  `prixPromo` decimal(14,4) DEFAULT NULL,
  `coutMoyen` decimal(14,4) DEFAULT NULL,
  `prixAchatMoyen` decimal(14,4) NOT NULL DEFAULT '0.0000',
  `QTE` int(11) NOT NULL DEFAULT '0' COMMENT 'calculée après CRUD sku',
  `pendingQte` int(11) NOT NULL DEFAULT '0',
  `lockedQte` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`variationID`),
  KEY `fk_vari_produit` (`produitID`),
  KEY `code` (`code`),
  CONSTRAINT `fk_vari_produit` FOREIGN KEY (`produitID`) REFERENCES `produit` (`produitID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=latin1;


-- otaku_ma.achatstock definition

CREATE TABLE `achatstock` (
  `achatStockID` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `fournisseurID` int(11) NOT NULL,
  `dateInsertion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `type` varchar(13) NOT NULL DEFAULT 'nationnale',
  `dateCommande` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `dateLivraison` timestamp NULL DEFAULT NULL,
  `qte` int(11) DEFAULT NULL,
  `prixTotal` double NOT NULL,
  `fraisSupplementaires` double NOT NULL,
  `description` varchar(255) NOT NULL,
  `authAdminID` int(11) NOT NULL,
  `associe` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`achatStockID`),
  KEY `fk_fournisseurID` (`fournisseurID`) USING BTREE,
  KEY `fk_achatAdminID` (`authAdminID`),
  CONSTRAINT `fk_achatAdminID` FOREIGN KEY (`authAdminID`) REFERENCES `admin` (`adminID`),
  CONSTRAINT `fk_fournisseurID` FOREIGN KEY (`fournisseurID`) REFERENCES `fournisseur` (`fournisseurID`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;


-- otaku_ma.achatstockitem definition

CREATE TABLE `achatstockitem` (
  `achatStockItemID` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `variationID` int(11) NOT NULL,
  `achatStockID` int(11) NOT NULL,
  `prixUnite` double NOT NULL,
  `qte` int(11) NOT NULL,
  `associe` tinyint(1) NOT NULL DEFAULT '0',
  `coutTotal` decimal(14,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`achatStockItemID`),
  KEY `fk_variation` (`variationID`),
  KEY `fk_achatStock` (`achatStockID`),
  CONSTRAINT `fk_achatStock` FOREIGN KEY (`achatStockID`) REFERENCES `achatstock` (`achatStockID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=489 DEFAULT CHARSET=latin1;


-- otaku_ma.attribut definition

CREATE TABLE `attribut` (
  `attributID` int(11) NOT NULL AUTO_INCREMENT,
  `produitID` int(11) DEFAULT NULL,
  `code` varchar(100) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `type` varchar(36) DEFAULT 'particulier',
  `description` text,
  PRIMARY KEY (`attributID`),
  UNIQUE KEY `code` (`code`),
  KEY `fk_attribut_produit` (`produitID`),
  CONSTRAINT `fk_attribut_produit` FOREIGN KEY (`produitID`) REFERENCES `produit` (`produitID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;


-- otaku_ma.casse definition

CREATE TABLE `casse` (
  `casseID` int(11) NOT NULL AUTO_INCREMENT,
  `produitID` int(11) NOT NULL,
  `variationID` int(11) DEFAULT NULL,
  `qte` int(11) NOT NULL DEFAULT '0',
  `dateinsertion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cause` varchar(255) DEFAULT NULL,
  `etat` varchar(60) NOT NULL DEFAULT 'CASSE',
  PRIMARY KEY (`casseID`),
  KEY `produitID` (`produitID`),
  CONSTRAINT `casse_ibfk_1` FOREIGN KEY (`produitID`) REFERENCES `produit` (`produitID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- otaku_ma.optionattribut definition

CREATE TABLE `optionattribut` (
  `optionAttributID` int(11) NOT NULL AUTO_INCREMENT,
  `attributID` int(11) NOT NULL,
  `code` varchar(60) NOT NULL,
  `nom` varchar(120) DEFAULT NULL,
  `couleur` varchar(12) DEFAULT NULL,
  `description` text,
  `isActive` tinyint(1) NOT NULL DEFAULT '1',
  `ordre` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`optionAttributID`),
  UNIQUE KEY `code` (`code`),
  KEY `attributID` (`attributID`),
  CONSTRAINT `fk_option_attribut` FOREIGN KEY (`attributID`) REFERENCES `attribut` (`attributID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=latin1;


-- otaku_ma.panier definition

CREATE TABLE `panier` (
  `panierID` int(11) NOT NULL AUTO_INCREMENT,
  `clientID` int(11) NOT NULL,
  `produitID` int(11) NOT NULL,
  `variationID` int(11) DEFAULT NULL,
  `qte` int(11) NOT NULL,
  PRIMARY KEY (`panierID`),
  KEY `fk_panier_clientid` (`clientID`),
  KEY `fk_panier_produitid` (`produitID`),
  CONSTRAINT `fk_panier_clientid` FOREIGN KEY (`clientID`) REFERENCES `client` (`clientID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_panier_produitid` FOREIGN KEY (`produitID`) REFERENCES `produit` (`produitID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=385 DEFAULT CHARSET=latin1;


-- otaku_ma.perte definition

CREATE TABLE `perte` (
  `perteID` int(11) NOT NULL,
  `produitID` int(11) NOT NULL,
  `variationID` int(11) DEFAULT NULL,
  `dateinsertion` int(11) NOT NULL,
  `qte` int(11) NOT NULL,
  `cause` varchar(255) NOT NULL,
  `etat` int(60) NOT NULL,
  KEY `fk_perte_produitid` (`produitID`),
  CONSTRAINT `fk_perte_produitid` FOREIGN KEY (`produitID`) REFERENCES `produit` (`produitID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- otaku_ma.sku definition

CREATE TABLE `sku` (
  `skuID` int(11) NOT NULL AUTO_INCREMENT,
  `variationID` int(11) DEFAULT NULL,
  `achatStockItemID` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT 'default:variation nom',
  `QTE` int(11) NOT NULL,
  `isActive` tinyint(1) NOT NULL DEFAULT '0',
  `code` varchar(100) DEFAULT NULL COMMENT 'catid-prid-varid-stid',
  `dateInsertion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `motif` text,
  `authAdminID` int(11) NOT NULL,
  PRIMARY KEY (`skuID`),
  UNIQUE KEY `code` (`code`),
  KEY `fk_sku_variation` (`variationID`),
  KEY `fk_achatstockitem` (`achatStockItemID`),
  KEY `fk_adminloginID` (`authAdminID`),
  FULLTEXT KEY `description` (`description`),
  FULLTEXT KEY `description_2` (`description`),
  CONSTRAINT `fk_achatstockitem` FOREIGN KEY (`achatStockItemID`) REFERENCES `achatstockitem` (`achatStockItemID`),
  CONSTRAINT `fk_adminloginID` FOREIGN KEY (`authAdminID`) REFERENCES `admin_login` (`adminLoginID`)
) ENGINE=InnoDB AUTO_INCREMENT=1657 DEFAULT CHARSET=latin1;


-- otaku_ma.variationoption definition

CREATE TABLE `variationoption` (
  `variationOptionID` int(11) NOT NULL AUTO_INCREMENT,
  `variationID` int(11) NOT NULL,
  `optionAttributID` int(11) NOT NULL,
  PRIMARY KEY (`variationOptionID`),
  KEY `fk_vrop_variation` (`variationID`),
  KEY `fk_vrop_option` (`optionAttributID`),
  CONSTRAINT `fk_vrop_option` FOREIGN KEY (`optionAttributID`) REFERENCES `optionattribut` (`optionAttributID`) ON DELETE CASCADE,
  CONSTRAINT `fk_vrop_variation` FOREIGN KEY (`variationID`) REFERENCES `variation` (`variationID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=latin1;