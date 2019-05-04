# Graph Theory - Effective Delivery Path

Le projet actuel consiste à développer une application (java) sans interface graphique qui récupère une série de points de livraisons dans un fichier txt et qui affiche au livreur les itinéraires lui permettant de passer par tous les points une seule fois, sans jamais reprendre le même chemin une deuxième fois et qui permet au livreur de revenir au même point de départ. Les itinéraires identifiés sont ensuite sauvegardés dans un second fichier txt avec les distances entre les points de livraison afin de permettre au livreur d'imprimer les itinéraires et de les avoir en main propre. L'application supporte sans problème les routes ayant un sens unique, mais ne supporte pas pour l'instant les cul-de-sac qui forcent le livreur à repasser sur un chemin déjà emprunté.  

## Démarrer l'application

Le démarrage de l'application se fait à partir de la classe Application.java.
Les itinéraires (solutions) des fichiers txt seront affichés dans la console de votre IDE (Java). Pour la démonstration, deux fichiers txt contenant les points de livraison sont fournis dans le dossier input_graph du projet. Les solutions seront sauvegardées dans ce cas-ci dans deux fichiers txt (avec une étampe contenant la date et l'heure) qu'on peut retrouver dans le dossier output_solutions.

## Carte des points de livraison

![alt text](https://github.com/k1n6ar3a/GraphTheory_EffectiveDeliveryPath/blob/master/delivery_point_map/DeliveryPointMap.png)

## Structure du fichier en entrée

L'application supporte pour l'instant seulement les fichiers de type '.txt'.
La structure du fichier d'entrée doit être similaire à cet exemple:
	
	3
	50
	1
	1	2	1
	1	3	1
	2	3	1
	2	1	1
	3	2	1
	3	1	1
	$

	1- La première ligne donne le nombre de point de livraison.
	2- La deuxième ligne n'est pas utilisée pour cette démo.
	3- La troisième ligne identifie le point de départ.
	4- Les autres lignes représentent:

			- La première colonne représente un point de livraison.
			- La deuxième colonne représente sa liaison avec un autre point de livraison. 
			- La troisième colonne représente la distance entre les deux points de livraison.

	Le fichier doit finir avec le symbole '$' pour terminer la lecture des données.
	Les distances entre les points de livraison ont été mises à 1 Km pour simplifier la démo. 
	
	Les résultats de l'exemple ci-dessus devrait donner:

		Distance total du parcours: 3 Km

			1  --(1)-->  2  --(1)-->  3  --(1)-->  1

		Distance total du parcours: 3 Km

			1  --(1)-->  3  --(1)-->  2  --(1)-->  1

## Auteur

- Nom:   Alexandre Laroche
- Date:  Automne 2017