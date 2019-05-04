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
 * @Resumer:	Cette classe permet la v�rification en profondeur 
 * 				des donn�es retrouv�es dans le fichier .txt en entr�e.
 * 				On v�rifie si la structure du fichier est respect�e, 
 * 				on s'assure qu'il n'y a pas de caract�res inconnus,
 * 				on v�rifie s'il y a des valeurs dupliqu�es, etc.
 * 
 * 				Dans le cas contraire, on affiche un message d'erreur
 * 				dans la console et le logiciel arr�te pour �viter 
 * 				l'utilisation de ressources inutilement.
 * 
 * 				Enfin, on filtre, r�cup�re et distribue les valeurs du 
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
				"\n*         Arr�t de l'application         *" +
				"\n******************************************",
	ERREUR_SOMMETS = BOITE_ERREUR + "Une erreur est survenu au niveau des SOMMETS.\nAssurez-vous qu'il n'y ait pas de SOMMETS ERRON�S ou NULL et que la NOMENCLATURE soit respect�e.\n" ,
	ERREUR_DEPART = BOITE_ERREUR + "Une erreur est survenu avec la VALEUR DU SOMMET DE D�PART fourni dans le fichier d'entr� (.txt).\n" ,
	ERREUR_GRAPH = BOITE_ERREUR + "Une erreur est survenu avec les VALEURS QUI D�TERMINENT LES PARAM�TRES DU GRAPHE fourni dans le fichier d'entr� (.txt).\nAssurez-vous qu'une solution soit possible !\n" ,
	ERREUR_VALEUR_INFINI = BOITE_ERREUR + "Une erreur est survenu avec la VALEUR INFINI DANS L'ENT�TE fourni dans le fichier d'entr�e (.txt).\n",
	ERREUR_LIGNE_VIDE = BOITE_ERREUR + "Une erreur est survenu car il y a pr�sence d'une LIGNE VIDE dans le fichier d'entr� (.txt).\nAssurez-vous qu'il y ait le symbole '$' a la fin du fichier.\n",
	ERREUR_VALEUR_INVALIDE = BOITE_ERREUR + "Une erreur est survenu � cause d'une VALEUR QUI N'EST PAS INTEGER dans le fichier d'entr� (.txt).\n";

	/***************************
	 * Variable
	 ***************************/
	private String inputFile = null;


	/******************************************************
	 * PARSE
	 * 
	 * @Resumer:	La validation des donn�es du fichier 
	 * 				d'entr� et la construction de la
	 * 				structure de donn�e du graphe
	 * 				commence ici. Si la validation
	 * 				n'a pas �t� r�ussi, on envoi un
	 * 				message d'erreur et on ferme l'app.
	 * 
	 * @Param: 		fichier d'entr�e (.txt)
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
	 * 				On s'assure de respecter la syntaxe et on v�rifie
	 * 				� ce que les valeurs lues ont un sens logique.
	 * 				Sinon, un message d'erreur est affich� dans la 
	 * 				console et le logiciel arrete pour �viter 
	 * 				l'utilisation de ressources inutilement.
	 * 
	 * 				Structure du fichier en entr�e (txt):
	 * 				
	 * 					Ligne 1 = Nombre de sommets
	 * 					Ligne 2 = Valeur non utilis�e pour la d�mo
	 * 					Ligne 3 = Le sommet de d�part
	 * 					Autres  = sommet   destination   distance
	 * 
	 * 				Dans le cas de la d�mo, la distance est 
	 * 				la m�me entre tous les sommets (1 Km).
	 * 
	 * 				Le fichier doit terminer avec le symbole $
	 * 				pour confirmer la fin de lecture des donn�es.
	 * 
	 * @return		True, si aucune erreur n'a �t� rencontr�e
	 * 					
	 ******************************************************/
	private boolean dataIsValid() throws Exception {

		//D�marrage d'un nouveau processus de lecture de fichier .txt
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

		//Tant qu'on peut lire des valeurs, on proc�de
		while (scanner.hasNextInt()) {	

			//On retire les espaces de la ligne	(entre les colonnes)
			colonne = scanner.nextLine().split(SPACE);					

			//1- Nombre de sommets
			if(numeroLigne == FIRST_LINE){

				//Si ce n'est pas une valeur simple OU plus petite ou �gale � z�ro OU plus grande que le nombre de sommets
				nbSommets = Integer.parseInt(colonne[0]);
				if(colonne.length != 1 || nbSommets <= 0 || nbSommets > getVerticesQty()) {
					System.err.println(ERREUR_SOMMETS);
					return false;
				}
			}
			//2- Valeur infifi non utilis�e pour la d�mo
			else if(numeroLigne == SECOND_LINE){

				//Si c'est une valeur qui n'est pas simple OU plus petite � z�ro 
				valeurInfini = Integer.parseInt(colonne[0]);
				if(colonne.length != 1 || valeurInfini < 0) {
					System.err.println(ERREUR_VALEUR_INFINI);
					return false;
				}
			}
			//3- Sommet de d�part
			else if(numeroLigne == THIRD_LINE){

				//Si valeur n'est pas simple OU plus petite ou �gale � z�ro OU plus grande que le nombre de sommets
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
	 * @Resumer:	On r�cup�re tous les sommets du fichier
	 * 				d'entr�e pour ensuite les filtrer et 
	 * 				garder seulement les sommets uniques.
	 * 				
	 * 				Permet de v�rifier si le nombre
	 * 				de sommets uniques correspond � la plus
	 * 				grande valeur (sommet) identifi�e du
	 * 				fichier d'entr�e. 
	 * 
	 * @Return: 	nombre de sommets uniques identifi�es
	 * 
	 ******************************************************/
	private int getVerticesQty() throws FileNotFoundException{

		//D�marrage d'un nouveau processus de lecture de fichier .txt
		Scanner scanner = new Scanner(new File(inputFile));		

		//Tous les sommets du fichier
		List<Integer> sommets = new ArrayList<Integer>();

		//Tous les sommets uniques
		List<Integer> sommetsUniques = new ArrayList<Integer>();	

		while(scanner.hasNextInt()){

			//Si d�tection d'un sommet, on l'ajoute dans la liste 
			if (scanner.hasNextInt()) sommets.add(scanner.nextInt());
			else 					  scanner.next();
		}
		if (scanner != null)
			scanner.close();

		//Boucle qui ins�re dans une liste seulement les valeurs uniques
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
	 * 				enti�rement v�rifi�e les donn�es, on peut 
	 * 				lire rapidement les donn�es du fichier d'entr�e.
	 * 				
	 * 				C'est ici qu'on r�cup�re les valeurs de l'entete
	 * 				du Graphe du fichier .txt pour les envoyer dans la 
	 * 				classe Input. On recupere donc le nombre de sommets,
	 * 				la valeur infini et la sommet de depart.
	 * 				Bref, represente les param�tres de base au Graph. 
	 * 
	 ******************************************************/
	private void setHeadGraph() throws NumberFormatException, IOException{

		graph = new GraphModel();

		//D�marrage d'un nouveau processus de lecture du fichier .txt
		buffReader = new BufferedReader(new FileReader(inputFile));

		//R�cup�re et initialise la quantit� de sommets
		graph.setVerticesQty(Integer.parseInt(buffReader.readLine()));

		//Recupere la valeur infini et la garde en memoire (sans raisons particulieres)
		graph.setInfiniteValor(Integer.parseInt(buffReader.readLine()));

		//R�cup�re et initialise le sommet de d�part
		graph.setStartingPoint(Integer.parseInt(buffReader.readLine()));

	}

	/******************************************************
	 * Set Body Graph
	 * 
	 * @Resumer:	Puisque la fonction dataIsValid() a  
	 * 				enti�rement v�rifi�e les donn�es, on peut 
	 * 				lire rapidement les donn�es du fichier d'entr�e.
	 * 
	 * 				C'est ici qu'on r�cup�re les valeurs du fichier .txt
	 * 				pour les envoyer vers la classe Input. Supporte le
	 * 				processus qui donne la structure et les param�tres
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

				//On ins�re les sommets dans une liste situ�e dans la classe Input
				graph.addVertices(Integer.parseInt(colonne[0]));

				//On ins�re les destinations dans une liste situ�e dans la classe Input
				graph.addDestination(Integer.parseInt(colonne[1]));

				//On ins�re les poids dans une liste situ�e dans la classe Input
				graph.addDistance(Integer.parseInt(colonne[2]));

				//On duplique le sommet de d�part. Solution Temporaire pour parcourir le Graphe
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
	 * @Resumer:	Solution temporaire qui duplique le sommet de d�part
	 * 				et ses sommets adjacents sans oublier son poids afin 
	 * 				de parcourir la totalit� du Graphe pour ensuite revenir 
	 * 				au point de d�part ou sommet voulu selon le fichier .txt.
	 * 
	 * 				Ex: Si on part de 0 � 1, cela repr�sente partir de 1 � 1.
	 * 
	 * @Param: 		String[] arrayLigne
	 * 
	 ******************************************************/
	private void duplicateStartingPoint(String[] arrayLigne) throws Exception{

		//Si le sommet est le m�me que le sommet de d�part, on proc�de
		if(Integer.parseInt(arrayLigne[0]) == graph.getStartingPoint()){

			//On recup�re les m�me informations que celui du sommet de d�part
			graph.addVertices(graph.getStartingPoint() - 1);
			graph.addDestination(Integer.parseInt(arrayLigne[1]));
			graph.addDistance(Integer.parseInt(arrayLigne[2]));
		}
	}
}
