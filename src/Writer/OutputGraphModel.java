package Writer;

import java.util.ArrayList;
import java.util.List;

/******************************************************
 * @Classe:		Output Graph Model
 * 
 * @Resumer: 	Regroupe l'ensemble des mutateurs et accesseurs
 * 				pour supporter l'impression de la solution finale.
 * 				En d'autre mots, c'est le modèle du Graphe en sortie.
 * 
 * @Auteur:		Alexandre Laroche
 * 
 *******************************************************/
public class OutputGraphModel {

	/********************************
	 * List<Integer> et List<List>
	 ********************************/
	private List<List<Integer>> listeChemins = new ArrayList<>();
	private List<Integer> listeNbSommets = new ArrayList<>();
	private List<List<Integer>> listeInfoSommets = new ArrayList<>();

	/***************************
	 * Variable 
	 ***************************/
	private int sommetDepart = 0;

	
	/***************************
	 * @Get_Set: Liste Chemins
	 ***************************/
	public void setListeChemins(List<Integer> resultat){
		listeChemins.add(resultat);
	}
	public List<List<Integer>> getPathList(){
		return listeChemins;
	}

	/***************************
	 * @Get_Set: Liste Sommets
	 ***************************/
	public void setListeSommets(int nbSommets){
		listeNbSommets.add(nbSommets);
	}
	public List<Integer> getListeSommets(){
		return listeNbSommets;
	}
	
	/*******************************
	 * @Get_Set: Liste Info Sommets
	 *******************************/
	public void setListeInfoSommets(List<List<Integer>> _listeInfoSommets) {
		this.listeInfoSommets = _listeInfoSommets;
	}
	public List<List<Integer>> getVerticesInfo() {
		return listeInfoSommets;
	}

	/***************************
	 * @Get_Set: Sommet Départ
	 ***************************/
	public void setSommetDepart(int _sommetDepart) {
		this.sommetDepart = _sommetDepart;
	}
	public int getSommetDepart() {
		return sommetDepart;
	}
	public int getSourceDepart(){
		return getSommetDepart() - 1;
	}
}