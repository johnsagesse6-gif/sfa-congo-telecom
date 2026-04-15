-- Insertion de quelques clients par défaut
INSERT IGNORE INTO clients (id, nom, adresse, telephone) VALUES (1, 'Yoco Yoco', 'Brazzaville', '060000000');
INSERT IGNORE INTO clients (id, nom, adresse, telephone) VALUES (2, 'Client Standard', 'Pointe-Noire', '050000000');

-- Insertion de quelques articles de base
INSERT IGNORE INTO articles (id, designation, prix_unitaire) VALUES (1, 'Abonnement Internet 10Mbps', 25000);
INSERT IGNORE INTO articles (id, designation, prix_unitaire) VALUES (2, 'Installation Ligne Fixe', 15000);