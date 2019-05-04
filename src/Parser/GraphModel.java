package Parser;

import java.util.ArrayList;
import java.util.List;

/******************************************************
 * @Classe: 	Graph Model
 * 
 * @Resumer: 	Regroupe l'ensemble des mutateurs et accesseurs
 * 				pour supporter la recherche de chemins (solutions).
 * 				En d'autre mots, c'est le modèle du Graphe en entrée.
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public class GraphModel {

	/********************************
	 * List<Integer> et List<List>
	 ********************************/
	private List<Integer> listeSommets = new ArrayList<>();
	private List<Integer> listeDestination = new ArrayList<>();
	private List<Integer> listePoids = new ArrayList<>();
	private List<List<Integer>> listeInfoSommets = new ArrayList<>();
	

	/***************************
	 * Variables
	 ***************************/
	private int qteSommets = 0;
	private int sommetDepart = 0;
	private int valeurInfini = 0;

	
	/***************************
	 * @Get_Set: Liste Sommets
	 ***************************/
	public void addVertices(int sommets) {
		listeSommets.add(sommets);
	}
	public List<Integer> getListeSommets() {
		return listeSommets;
	}

	/***************************
	 * @Get_Set: Liste Destinations
	 ***************************/
	public void addDestination(int destination) {
		listeDestination.add(destination);
	}
	public List<Integer> getListeDestination() {
		return listeDestination;
	}

	/***************************
	 * @Get_Set: Liste Poids
	 ***************************/
	public void addDistance(int _poids) {
		listePoids.add(_poids);
	}
	public List<Integer> getListePoids() {
		return listePoids;
	}
	
	/***************************
	 * @Get: Liste Info Sommets
	 ***************************/
	public List<List<Integer>> getListeInfoSommets() {
		listeInfoSommets.add(listeSommets);
		listeInfoSommets.add(listeDestination);
		listeInfoSommets.add(listePoids);
		return listeInfoSommets;
	}

	/***************************
	 * @Get_Set: Quantité Sommets
	 ***************************/
	public void setVerticesQty(int _qteSommets) {
		this.qteSommets = _qteSommets;
	}
	public int getQteSommets() {
		return qteSommets;
	}

	/***************************
	 * @Get_Set: Sommet Départ
	 ***************************/
	public void setStartingPoint(int _sommetDepart) {
		this.sommetDepart = _sommetDepart;
	}
	public int getStartingPoint() {
		return sommetDepart;
	}
	//Sommet de départ temporaire qu'on ajuste à la fin afin de parcourir la totalité du graphe.
	//Ex: 0 à 1 représente de 1 à 1 OU 3 à 8 représente de 4 à 8.
	public int getOrigineDepart() {
		return (sommetDepart - 1);
	}
	
	/***************************
	 * @Get_Set: Valeur Infini
	 ***************************/
	public void setInfiniteValor(int _valeurInfini) {
		this.valeurInfini = _valeurInfini;
	}
	public int getValeurInfini() {
		return valeurInfini;
	}
}