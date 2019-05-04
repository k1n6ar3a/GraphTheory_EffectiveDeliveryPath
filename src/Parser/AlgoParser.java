package Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/******************************************************
 * @Classe: 	Algorithm Parser
 * 
 * @Resumer:	Cette classe permet la vérification en profondeur 
 * 				des données retrouvées dans le fichier .txt en entrée.
 * 				On vérifie si la structure du fichier est respectée, 
 * 				on s'assure qu'il n'y a pas de caractères inconnus,
 * 				on vérifie s'il y a des valeurs dupliquées, etc.
 * 
 * 				Dans le cas contraire, on affiche un message d'erreur
 * 				dans la console et le logiciel arrête pour éviter 
 * 				l'utilisation de ressources inutilement.
 * 
 * 				Enfin, on filtre, récupère et distribue les valeurs du 
 * 				fichier .txt pour supporter la recherche de solutions.
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public class AlgoParser implements InterfaceParser<GraphModel>{

	/***************************
	 * Classe instanciee 
	 ***************************/
	private GraphModel graph = null;
	private BufferedReader buffReader = null;

	/***************************
	 * Constantes
	 ***************************/
	private static final int
	FIRST_LINE = 1,
	SECOND_LINE = 2,
	THIRD_LINE = 3,
	OTHER_LINES = 4;

	private static final String
	BOITE_ERREUR = "\n********************\n*      ERREUR      *\n********************\n\n";

	private static final String 
	SPACE = "(\\s)+",
	SYMBOLE_FIN = "$",
	CLOSE_APP = "\n******************************************" +
				"\n*         Arrêt de l'application         *" +
				"\n******************************************",
	ERREUR_SOMMETS = BOITE_ERREUR + "Une erreur est survenu au niveau des SOMMETS.\nAssurez-vous qu'il n'y ait pas de SOMMETS ERRONÉS ou NULL et que la NOMENCLATURE soit respectée.\n" ,
	ERREUR_DEPART = BOITE_ERREUR + "Une erreur est survenu avec la VALEUR DU SOMMET DE DÉPART fourni dans le fichier d'entré (.txt).\n" ,
	ERREUR_GRAPH = BOITE_ERREUR + "Une erreur est survenu avec les VALEURS QUI DÉTERMINENT LES PARAMÈTRES DU GRAPHE fourni dans le fichier d'entré (.txt).\nAssurez-vous qu'une solution soit possible !\n" ,
	ERREUR_VALEUR_INFINI = BOITE_ERREUR + "Une erreur est survenu avec la VALEUR INFINI DANS L'ENTÊTE fourni dans le fichier d'entrée (.txt).\n",
	ERREUR_LIGNE_VIDE = BOITE_ERREUR + "Une erreur est survenu car il y a présence d'une LIGNE VIDE dans le fichier d'entré (.txt).\nAssurez-vous qu'il y ait le symbole '$' a la fin du fichier.\n",
	ERREUR_VALEUR_INVALIDE = BOITE_ERREUR + "Une erreur est survenu à cause d'une VALEUR QUI N'EST PAS INTEGER dans le fichier d'entré (.txt).\n";

	/***************************
	 * Variable
	 ***************************/
	private String inputFile = null;


	/******************************************************
	 * PARSE
	 * 
	 * @Resumer:	La validation des données du fichier 
	 * 				d'entré et la construction de la
	 * 				structure de donnée du graphe
	 * 				commence ici. Si la validation
	 * 				n'a pas été réussi, on envoi un
	 * 				message d'erreur et on ferme l'app.
	 * 
	 * @Param: 		fichier d'entrée (.txt)
	 * @Return: 	GraphModel
	 * 
	 ******************************************************/
	@Override
	public GraphModel parse(String _inputFile) throws Exception {
		this.inputFile = _inputFile;
		if(dataIsValid()) {
			setHeadGraph();
			setBodyGraph();
		} else {
			System.out.println(CLOSE_APP);
			System.exit(0);
		}
		return graph;
	}

	/******************************************************
	 * Data Validation
	 * 
	 * @Resumer:	Lecture du fichier .txt ligne par ligne.
	 * 				On s'assure de respecter la syntaxe et on vérifie
	 * 				à ce que les valeurs lues ont un sens logique.
	 * 				Sinon, un message d'erreur est affiché dans la 
	 * 				console et le logiciel arrete pour éviter 
	 * 				l'utilisation de ressources inutilement.
	 * 
	 * 				Structure du fichier en entrée (txt):
	 * 				
	 * 					Ligne 1 = Nombre de sommets
	 * 					Ligne 2 = Valeur non utilisée pour la démo
	 * 					Ligne 3 = Le sommet de départ
	 * 					Autres  = sommet   destination   distance
	 * 
	 * 				Dans le cas de la démo, la distance est 
	 * 				la même entre tous les sommets (1 Km).
	 * 
	 * 				Le fichier doit terminer avec le symbole $
	 * 				pour confirmer la fin de lecture des données.
	 * 
	 * @return		True, si aucune erreur n'a été rencontrée
	 * 					
	 ******************************************************/
	private boolean dataIsValid() throws Exception {

		//Démarrage d'un nouveau processus de lecture de fichier .txt
		Scanner scanner = new Scanner(new File(inputFile));

		//Permet d'identifier les valeurs selon la colonne 
		String[] colonne = null;

		int 
		sommet = 0,
		distance = 0,
		nbSommets = 0,
		sommetDepart = 0,
		destination = 0,
		valeurInfini = 0,
		numeroLigne = 1;

		//Tant qu'on peut lire des valeurs, on procède
		while (scanner.hasNextInt()) {	

			//On retire les espaces de la ligne	(entre les colonnes)
			colonne = scanner.nextLine().split(SPACE);					

			//1- Nombre de sommets
			if(numeroLigne == FIRST_LINE){

				//Si ce n'est pas une valeur simple OU plus petite ou égale à zéro OU plus grande que le nombre de sommets
				nbSommets = Integer.parseInt(colonne[0]);
				if(colonne.length != 1 || nbSommets <= 0 || nbSommets > getVerticesQty()) {
					System.err.println(ERREUR_SOMMETS);
					return false;
				}
			}
			//2- Valeur infifi non utilisée pour la démo
			else if(numeroLigne == SECOND_LINE){

				//Si c'est une valeur qui n'est pas simple OU plus petite à zéro 
				valeurInfini = Integer.parseInt(colonne[0]);
				if(colonne.length != 1 || valeurInfini < 0) {
					System.err.println(ERREUR_VALEUR_INFINI);
					return false;
				}
			}
			//3- Sommet de départ
			else if(numeroLigne == THIRD_LINE){

				//Si valeur n'est pas simple OU plus petite ou égale à zéro OU plus grande que le nombre de sommets
				sommetDepart = Integer.parseInt(colonne[0]);
				if(colonne.length != 1 || sommetDepart <= 0 || sommetDepart > nbSommets) {
					System.err.println(ERREUR_DEPART);
					return false;
				}
			}
			//4- Sommet   Destination   Distance
			else if(numeroLigne >= OTHER_LINES){

				// Si la prochaine valeur n'est pas un Integer
				if(!scanner.hasNextInt()){ 	

					//Si la prochaine ligne est vide
					if(!scanner.hasNextLine()) { 	
						System.err.println(ERREUR_LIGNE_VIDE);
						return false;
					}
					//Si ce n'est pas le symbole $
					else if(!scanner.nextLine().contains(SYMBOLE_FIN)) {
						System.err.println(ERREUR_VALEUR_INVALIDE);
						return false;
					}
				}
				sommet = Integer.parseInt(colonne[0]);
				destination = Integer.parseInt(colonne[1]);
				distance = Integer.parseInt(colonne[2]);

				if(colonne.length != 3 || sommet > nbSommets || destination > nbSommets || sommet <= 0 || destination <= 0) {
					System.err.println(ERREUR_GRAPH);
					return false;
				}
			}
			numeroLigne++;
		}
		//Fermeture du scanner
		if (scanner != null) scanner.close();
		return true;
	}

	/******************************************************
	 * Vertices Quantity
	 * 
	 * @Resumer:	On récupère tous les sommets du fichier
	 * 				d'entrée pour ensuite les filtrer et 
	 * 				garder seulement les sommets uniques.
	 * 				
	 * 				Permet de vérifier si le nombre
	 * 				de sommets uniques correspond à la plus
	 * 				grande valeur (sommet) identifiée du
	 * 				fichier d'entrée. 
	 * 
	 * @Return: 	nombre de sommets uniques identifiées
	 * 
	 ******************************************************/
	private int getVerticesQty() throws FileNotFoundException{

		//Démarrage d'un nouveau processus de lecture de fichier .txt
		Scanner scanner = new Scanner(new File(inputFile));		

		//Tous les sommets du fichier
		List<Integer> sommets = new ArrayList<Integer>();

		//Tous les sommets uniques
		List<Integer> sommetsUniques = new ArrayList<Integer>();	

		while(scanner.hasNextInt()){

			//Si détection d'un sommet, on l'ajoute dans la liste 
			if (scanner.hasNextInt()) sommets.add(scanner.nextInt());
			else 					  scanner.next();
		}
		if (scanner != null)
			scanner.close();

		//Boucle qui insère dans une liste seulement les valeurs uniques
		for (int k = 0; k < sommets.size(); k++) {
			if(!sommetsUniques.contains(sommets.get(k)))
				sommetsUniques.add(sommets.get(k));
		}
		return sommetsUniques.size();
	}

	/******************************************************
	 * Set Head Graph
	 * 
	 * @Resumer:	Puisque la fonction dataIsValid() a  
	 * 				entièrement vérifiée les données, on peut 
	 * 				lire rapidement les données du fichier d'entrée.
	 * 				
	 * 				C'est ici qu'on récupère les valeurs de l'entete
	 * 				du Graphe du fichier .txt pour les envoyer dans la 
	 * 				classe Input. On recupere donc le nombre de sommets,
	 * 				la valeur infini et la sommet de depart.
	 * 				Bref, represente les paramètres de base au Graph. 
	 * 
	 ******************************************************/
	private void setHeadGraph() throws NumberFormatException, IOException{

		graph = new GraphModel();

		//Démarrage d'un nouveau processus de lecture du fichier .txt
		buffReader = new BufferedReader(new FileReader(inputFile));

		//Récupère et initialise la quantité de sommets
		graph.setVerticesQty(Integer.parseInt(buffReader.readLine()));

		//Recupere la valeur infini et la garde en memoire (sans raisons particulieres)
		graph.setInfiniteValor(Integer.parseInt(buffReader.readLine()));

		//Récupère et initialise le sommet de départ
		graph.setStartingPoint(Integer.parseInt(buffReader.readLine()));

	}

	/******************************************************
	 * Set Body Graph
	 * 
	 * @Resumer:	Puisque la fonction dataIsValid() a  
	 * 				entièrement vérifiée les données, on peut 
	 * 				lire rapidement les données du fichier d'entrée.
	 * 
	 * 				C'est ici qu'on récupère les valeurs du fichier .txt
	 * 				pour les envoyer vers la classe Input. Supporte le
	 * 				processus qui donne la structure et les paramètres
	 * 				au Graphe. 
	 * 
	 ******************************************************/
	private void setBodyGraph() throws Exception{

		String ligne = null;
		String[] colonne = null;

		//Tant que la lecture de lignes n'est pas null
		while ((ligne = buffReader.readLine()) != null){

			//On retire les espaces de la ligne
			colonne = ligne.split(SPACE);

			//Si la ligne ne contient pas le symbole ''$'', on poursuit
			if(!ligne.contains(SYMBOLE_FIN)){

				//On insère les sommets dans une liste située dans la classe Input
				graph.addVertices(Integer.parseInt(colonne[0]));

				//On insère les destinations dans une liste située dans la classe Input
				graph.addDestination(Integer.parseInt(colonne[1]));

				//On insère les poids dans une liste située dans la classe Input
				graph.addDistance(Integer.parseInt(colonne[2]));

				//On duplique le sommet de départ. Solution Temporaire pour parcourir le Graphe
				duplicateStartingPoint(colonne);
			}
		} 
		try {
			if (buffReader != null) buffReader.close();
		} catch (IOException ex) { ex.printStackTrace(); } 
	}

	/******************************************************
	 * Duplicate Starting Point
	 * 
	 * @Resumer:	Solution temporaire qui duplique le sommet de départ
	 * 				et ses sommets adjacents sans oublier son poids afin 
	 * 				de parcourir la totalité du Graphe pour ensuite revenir 
	 * 				au point de départ ou sommet voulu selon le fichier .txt.
	 * 
	 * 				Ex: Si on part de 0 à 1, cela représente partir de 1 à 1.
	 * 
	 * @Param: 		String[] arrayLigne
	 * 
	 ******************************************************/
	private void duplicateStartingPoint(String[] arrayLigne) throws Exception{

		//Si le sommet est le même que le sommet de départ, on procède
		if(Integer.parseInt(arrayLigne[0]) == graph.getStartingPoint()){

			//On recupère les même informations que celui du sommet de départ
			graph.addVertices(graph.getStartingPoint() - 1);
			graph.addDestination(Integer.parseInt(arrayLigne[1]));
			graph.addDistance(Integer.parseInt(arrayLigne[2]));
		}
	}
}
