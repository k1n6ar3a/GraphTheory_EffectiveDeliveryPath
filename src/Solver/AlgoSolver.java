package Solver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

import Parser.GraphModel;
import Writer.OutputGraphModel;

/******************************************************
 * @Classe: 	Algorithm Solver
 * 
 * @Resumer: 	Cette classe s'occupe de supporter le processus
 * 				qui permet de parcourir un Graphe et ce, à partir 
 * 				de n'importe quel sommets et qui peut finir à
 * 				partir de n'importe quel sommets.
 * 
 * 				Cette classe représente l'algorithme qui parcours 
 * 				et identifie les chemins possibles de l'arbre. 
 * 
 * @Implements: Solver<Input,Output>
 * 
 * @Source: 	https://gist.github.com/rajeakshay/e5b046fb1b2746c039720026e6c44eef
 * 				http://www.sanfoundry.com/java-program-represent-graph-adjacency-list/
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public class AlgoSolver implements InterfaceSolver<GraphModel, OutputGraphModel> {

	/***************************
	 * Classe instanciee
	 ***************************/
	private GraphModel inputGraph = null;
	private OutputGraphModel outputGraph = null;

	/***************************
	 * Map<Integer,List<Integer>>
	 ***************************/
	private HashMap<Integer, List<Integer>> map;

	/*************************************
	 * Constante 
	 *************************************/
	private static final String
	BOITE_ERREUR = "\n********************\n*      ERREUR      *\n********************\n\n";

	private static final String 
	CLOSE_APP = "\n******************************************" +
				"\n*         Arrêt de l'application         *" +
				"\n******************************************",
	ERREUR_SOMMET = BOITE_ERREUR + "Il s'est produit une erreur avec L'INSERTION D'UN SOMMET dans la liste.\nUn des sommets est MANQUANT ou NULL.\n",
	ERREUR_LISTES = BOITE_ERREUR + "Il s'est produit une erreur au niveau de L'ÉGALITÉ DES LISTES (SOMMETS/DESTINATIONS/POIDS).\nIl est donc possible qu'un sommet soit DÉTACHÉ DU GRAPHE.\n",
	ERREUR_GRAPH = BOITE_ERREUR + "Le graph en entrée est invalide. Il est impossible d'effectuer l'analyse.";

	/******************************************************
	 * Solve
	 * 
	 * @Resumer: 	C'est ici que commence la classe.
	 * 				On appel la méthode qui donne la structure au Graphe
	 * 				et l'autre méthode pour parcourir le Graphe.
	 * 				
	 * 				Si le Graph en entrée est invalide, on affiche
	 * 				un message d'erreur et on ferme l'app.
	 * 
	 * @Param: 		Le graph comportant toute les données du fichier d'entrée (txt)
	 * 
	 ******************************************************/
	@Override
	public OutputGraphModel solve(GraphModel _inputGraph) {

		this.inputGraph = _inputGraph;
		outputGraph = new OutputGraphModel();
		map = new HashMap<>();

		if(_inputGraph != null) {
			try {
				initGraphVertices();
				initGraphPath();
			} catch (Exception e) { e.printStackTrace(); }
		} else {
			System.err.println(ERREUR_GRAPH);
			System.out.println(CLOSE_APP);
			System.exit(0);
		}
		return outputGraph;
	}

	/******************************************************
	 * Initialization of Graph Vertices
	 * 
	 * @Resumer: 	C'est ici qu'on initialise tous les sommets et leurs
	 * 				voisins afin de donner les branches au Graphe.
	 * 				
	 * 				Si une erreur est détectée au niveau de la liste
	 * 				des sommets, des destinations ou des distances,
	 * 				On envoi un message d'erreur et on ferme l'app.
	 * 
	 ******************************************************/
	public void initGraphVertices() throws Exception {	

		//Tant que chaque sommet peut atteindre une destination
		if(inputGraph.getListeSommets().size() == inputGraph.getListeDestination().size()){

			//Ajout des sommets et de leurs voisins
			for (int i = 0; i < inputGraph.getListeSommets().size(); i++)
				addNeighbour(inputGraph.getListeSommets().get(i), inputGraph.getListeDestination().get(i));
		} else {
			//Message d'erreur concernant la non égalité des listes (sommets/destinations/distance)
			System.err.println(ERREUR_LISTES);
			System.out.println(CLOSE_APP);
			System.exit(0);
		}	
	}

	/******************************************************
	 * Initialization of Graph Path
	 * 
	 * @Resumer: 	On donne le sommet de départ et d'arrivée (en fonction
	 * 				de l'information du fichier .txt) du Graphe.
	 * 				On récupère tous les chemins identifiées pour permettre
	 * 				aux methodes d'envoyer les bons chemins à la classe Output.
	 * 
	 ******************************************************/
	private void initGraphPath() throws Exception {

		//Liste qui garde en mémoire les différents parcours composés de plusieurs sommets.
		//C'est ici qu'on indique de quel sommet on part et de quel sommet on fini.
		List<List<Integer>> listeChemins = findPossiblePath(inputGraph.getOrigineDepart(), inputGraph.getStartingPoint());  

		//Envoi l'information concernant la sommet de départ et le poids à la classe Output
		outputGraph.setSommetDepart(inputGraph.getStartingPoint());
		outputGraph.setListeInfoSommets(inputGraph.getListeInfoSommets());

		//Pour tous les chemins possible, garde les chemins qui commencent et finissent par le même sommet
		for (int i = 0; i < listeChemins.size(); i++) 
			
			//Si la dimension du chemin possède le même nombre de sommets du Graphe + 1 (Ex: 1 à 1)
			if(listeChemins.get(i).size() == inputGraph.getQteSommets() + 1){// O(1)

				//Insertion des chemins (qui respect la demande) dans la classe Output
				outputGraph.setListeChemins(listeChemins.get(i));
				outputGraph.setListeSommets(listeChemins.get(i).size());
			}

		//Garde en mémoire les chemins qui ne commencent et finissent pas nécessairement par le même sommet
		alternativePath(listeChemins);
	}

	/******************************************************
	 * Alternative Path
	 * 
	 * @Resumer: 	Selon le fichier .txt, si le départ n'est pas le 
	 * 				même que celui d'arrivée, c'est ici qu'on procède
	 * 				à l'envoie des solutions alternatives vers la classe 
	 * 				Output. Bref, cette option permet d'identifier les 
	 * 				parcours possibles d'un sommet à un autre sans que
	 * 				le sommet de départ soit le même que celui de fin. 
	 * 
	 * @Param: List<List<Integer>> listeChemins
	 * 
	 ******************************************************/
	private void alternativePath(List<List<Integer>> listeChemins){

		//Liste qui garde en mémoire les chemins alternatifs
		List<Integer> cheminsAlternatifs = new ArrayList<>();

		//Pour la liste complète des chemins identifiés
		for (List<Integer> listeValeurs : listeChemins) 

			//Processus de comparaison pour garder les chemins qui visitent le plus de sommet
			if (listeValeurs.size() > cheminsAlternatifs.size())
				cheminsAlternatifs = listeValeurs;		

		//On s'assure d'éviter les chemins dupliqués
		if(!outputGraph.getPathList().contains(cheminsAlternatifs)){

			//Insertion des chemins (qui respect la demande) dans la classe Output
			outputGraph.setListeChemins(cheminsAlternatifs);
			outputGraph.setListeSommets(cheminsAlternatifs.size());
		}
	}

	/******************************************************
	 * Add Neighbor
	 * 
	 * @Resumer:	On insere les sommets et leurs voisins dans une liste
	 * 				de type HashMap. Donne la structure au Graphe.
	 * 
	 * @Param: 		sommet et destination
	 * 
	 ******************************************************/
	private void addNeighbour(int sommet, int destination) {

		//Si ne contient pas le sommet, on l'ajout avec une nouvelle liste composé de ses voisins 
		if(!map.containsKey(sommet))
			map.put(sommet, new ArrayList<Integer>());

		//Pour le sommet identifié, on ajoute son voisin
		map.get(sommet).add(destination);
	}

	/******************************************************
	 * Find Possible Path
	 * 
	 * @Resumer:	On donne les outils nécessaire pour supporter la 
	 * 				méthode récursive qui identifie les chemins possible
	 * 				du Graphe en fonction des paramètres reçus.
	 * 
	 * @Param: 		sommet et destination
	 * @Return: 	List<List<Integer>> parcourChemin
	 * 
	 * @Source:		http://www.java2s.com/Code/Java/JDK-6/UsingDequeasastack.htm
	 * 				https://stackoverflow.com/questions/12524826/why-should-i-use-deque-over-stack
	 * 
	 ******************************************************/
	private List<List<Integer>> findPossiblePath(int sommet, int destination) throws Exception {

		//Liste qui garde en les chemins et ses sommets
		List<List<Integer>> parcourChemin = new ArrayList<>();

		//Nouveau ArrayDeque pour garder en memoire les bons chemins (comme une pile)
		Deque<Integer> tentativesChemins = new ArrayDeque<Integer>();

		//Marque tous les sommets comme non visités. +1 car on revient au sommet de depart.
		boolean[] visited = new boolean[inputGraph.getQteSommets() + 1];

		//Méthode d'assistance récursive pour obtenir tous les chemins
		recursivPathAttempt(sommet, destination, visited, tentativesChemins, parcourChemin);

		return parcourChemin;
	}

	/******************************************************
	 * Recursiv Path Attempt
	 * 
	 * @Resumer:	On configure une méthode récursive pour sortir tous 
	 * 				les chemins d'un sommet à une destination
	 * 				qui garde en mémoire le chemin parcouru et les 
	 * 				sommets visités.
	 * 
	 * @Param: 		int sommet, int destination, boolean[] visited, 
	 * 				Deque<Integer> tentativeChemins et 
	 * 				List<List<Integer>> parcourChemin
	 * 
	 ******************************************************/
	private void recursivPathAttempt(int sommet, int destination, boolean[] visited, 
			Deque<Integer> tentativesChemins, List<List<Integer>> parcourChemin) throws Exception {

		//Marque le sommet actuel
		visited[sommet] = true;

		//Garde en memoire le sommet 
		tentativesChemins.add(sommet);

		//Si sommet identique à la destination, on insere une nouvelle liste qui contient un chemin 
		if (sommet == destination) 
			parcourChemin.add(new ArrayList<Integer>(tentativesChemins));

		//Sinon le sommet est different de la destination
		else {
			//Si le HashMap contien le sommet en mémoire, on procède
			if (map.containsKey(sommet)) {

				//Pour tous les sommets gardés en mémoire
				for (Integer unSommet : map.get(sommet)) {

					//Si sommet n'a pas été visité, on rappelle la méthode pour obtenir plus de chemins
					if (!visited[unSommet]) 
						recursivPathAttempt(unSommet, destination, visited, tentativesChemins, parcourChemin);
				}
			}else{
				//Affiche un message d'erreur concernant un des sommets de la liste
				System.err.println(ERREUR_SOMMET);
				System.out.println(CLOSE_APP);
				System.exit(0);
			}
		}

		//On retire le dernier element non valide de la collection
		tentativesChemins.removeLast();

		//Initialise le sommet comme non visité
		visited[sommet] = false;
	}
}
