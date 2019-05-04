package Writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/******************************************************
 * @Classe: 	Algorithm Writter
 * 
 * @Resumer:	On recupere les parcours et on ajuste la syntaxe 
 * 				pour inserer des fleches entre chaque sommets
 * 				pour afficher la distance entre les sommets. 
 * 				Ensuite on imprime les r�sultats dans un 
 * 				fichier .txt sauvegard� � l'endroit voulu.
 * 
 * 				Cette classe n'est pas tr�s optimis�e.
 * 				Puisqu'un sommet, sa destination et sa
 * 				distance sont dans trois listes s�par�es,
 * 				cela complique le travail. Il aurait �t�
 * 				pr�f�rable de cr�er une instance pour chaque
 * 				sommet qui garde en m�moire l'information au
 * 				lieux de parcourir les trois listes.
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public class AlgoWriter implements InterfaceWriter<OutputGraphModel>{

	/***************************
	 * Classe instanciee 
	 ***************************/
	private OutputGraphModel graphOutput = null;
	private String outputFile = null;
	private BufferedWriter buffWriter = null;

	/***************************
	 * Constantes
	 ***************************/
	private static final String
	CLOSE_APP = "\n******************************************" +
				"\n*         Arr�t de l'application         *" +
				"\n******************************************",
	BOITE_ERREUR = "\n********************\n*      ERREUR      *\n********************\n\n",
	MSG_DISTANCE_TOTAL = "\nDistance total du parcours: ",
	ERREUR_GRAPH = BOITE_ERREUR + "Le graph en sortie est invalide. Il est impossible d'effectuer l'�criture.",
	ERREUR_SOLUTION = BOITE_ERREUR + "Assurez-vous qu'il y ait une solution possible ! " +
									 "\nIl est fort qu'il y ait un circuit en boucle.\n";

	/***************************
	 * Variables
	 ***************************/
	int distance = 0;
	int distanceTotal = 0;
	int indexInfoSommets = 0;
	int indexReinitialiser = 0;

	/******************************************************
	 * Write
	 * 
	 * @Resumer:	C'est ici que commence la classe.
	 * 				On appel la m�thode qui d�marre le 
	 * 				processus de sauvegarde sur fichier txt.
	 * 				
	 * 				Si le Graph en sortie est invalide, on affiche
	 * 				un message d'erreur et on ferme l'app.
	 * 
	 * @Param: 		outputFile et Output Graph
	 * 
	 ******************************************************/
	@Override
	public void write(String _outputFile, OutputGraphModel _graphOutput) throws Exception {
		this.outputFile = _outputFile;
		this.graphOutput = _graphOutput;
		
		if(_graphOutput != null) {
			initWriter();
		} else {
			System.err.println(ERREUR_GRAPH);
			System.out.println(CLOSE_APP);
			System.exit(0);
		}
	}

	/******************************************************
	 * Initialize Writer
	 * 
	 * @Resumer:	On initialise le processus d'�criture sur fichier .txt.
	 * 				On appel ensuite la m�thode pour ins�rer les fl�che
	 * 				entre les sommets des parcours identifi�s.
	 * 		
	 * 				Si il y a erreur dans la solution , on affiche
	 * 				un message d'erreur et on ferme l'app.
	 *  
	 ******************************************************/
	private void initWriter() throws Exception{

		//Creation d'un nouveau processus d'ecriture (sauvegarde) sur fichier (.txt)
		buffWriter = new BufferedWriter(new FileWriter(outputFile));

		try{
			for(int i = 0; i < graphOutput.getPathList().size(); i++){

				//On retire la valeur de d�part dupliqu� temporaire pour la remplacer par sa vrai valeur
				graphOutput.getPathList().get(i).remove(graphOutput.getSourceDepart());
				graphOutput.getPathList().get(i).add(0, graphOutput.getSommetDepart());

				//Insertion d'une fleche entre les sommets
				addArrow(i);
			}
		}catch (Exception e){
			System.err.println(ERREUR_SOLUTION);
			System.out.println(CLOSE_APP);
			System.exit(0);
		}
		try {
			if (buffWriter != null) buffWriter.close();
		} catch (IOException e) { e.printStackTrace(); }
	}

	/******************************************************
	 * Separate Arrow
	 * 
	 * @Resumer:	C'est ici qu'on ins�re une fl�che entre les sommets 
	 * 				des parcours (solutions) identifi�s. 
	 * 
	 * 				On appel la m�thode qui permet d'identifier la distance
	 * 				entre les sommets et la distance total du parcours. 
	 * 				Le poids entre les sommets est ins�r� dans la fl�che.
	 * 
	 * 				On appel la m�thode qui �crit les r�sultats finaux
	 * 				dans un fichier .txt.
	 * 
	 * @Param: 		index de la boucle
	 *  
	 ******************************************************/
	private void addArrow(int i){

		//Nouvelle construction pour afficher les resultats finaux
		StringBuilder resultat = new StringBuilder(128);

		//Pour le chemin s�lectionn�, on garde en m�moire les sommets 
		for (int sommet : graphOutput.getPathList().get(i)) {// O(n)

			//Supporte la r�initialisation du poids entre les diff�rents parcours
			indexReinitialiser++;

			//Insertion d'une fl�che apr�s le premier sommet 
			if (resultat.length() > 0)
				resultat.append("  --(" + getDistance(i) + ")-->  "); 

			//Insere une fleche et une distance entre chaques sommets
			resultat.append(sommet);

			//Supporte la r�initialisation de la distance entre les diff�rents parcours
			resetDistance(i);
		}
		//D�marre le processus d'�criture
		fileWriter(resultat, i);
	}

	/******************************************************
	 * Reset Distance
	 * 
	 * @Resumer:	Permet de r�initialiser les index qui supporte 
	 * 				l'affichage de la distance entre les sommets.
	 * 
	 * 				La r�initialisation permet d'�viter de m�langer
	 * 				le calcul de la distance entre les diff�rents parcours.
	 * 
	 * @Param: 		index de la boucle
	 *  
	 ******************************************************/
	private void resetDistance(int i){

		//Si l'index equivaut au nombre de sommets que le parcours poss�de, on proc�de
		if(indexReinitialiser > graphOutput.getPathList().get(i).size()){// O(1)

			//On remet � z�ro les index pour pour ne pas m�langer les poids des diff�rents parcours
			indexInfoSommets = 0;
			indexReinitialiser = 0;

			//On s'assure de reinitialiser le poids total entre chaque parcours
			distanceTotal = 0;
		}
	}

	/******************************************************
	 * Get Distance
	 * 
	 * @Resumer:	On r�cup�re la distance entre les sommets 
	 * 				des diff�rents parcours et on calcul la 
	 * 				distance total de ceux-ci.
	 * 
	 * 				Tous les sommets, les destinations et les 
	 * 				distances sont dans une liste (getListeInfoSommets()). 
	 * 
	 * 					La position 0 = liste sommets
	 * 					La position 1 = liste destinations
	 * 					La position 2 = liste poids
	 * 
	 * @Param: 			index de la boucle
	 *  
	 ******************************************************/
	private int getDistance(int i){

		//Pour toutes les branches du graphe identifi�es au d�but du programme
		for (int j = 0; j < graphOutput.getVerticesInfo().get(0).size(); j++) {

			//Si chaque paire de sommets respecte la position courante et la destination du parcours 
			if(graphOutput.getVerticesInfo().get(0).get(j).equals(graphOutput.getPathList().get(i).get(indexInfoSommets)) && 
					graphOutput.getVerticesInfo().get(1).get(j).equals(graphOutput.getPathList().get(i).get(indexInfoSommets + 1))){

				//On identifie le poids entre deux sommets avec l'index
				distance = graphOutput.getVerticesInfo().get(2).get(j);

				//On additionne tous les poids ensemble pour obtenir le poids total
				distanceTotal += distance;	
			}	
		}
		indexInfoSommets++;	
		return distance;
	}

	/******************************************************
	 * File Writer
	 * 
	 * @Resumer:	C'est avec la boucle de la m�thode pr�c�dente
	 * 				qu'on �crit tous les r�sultats un � la suite
	 * 				de l'autre avec la quantit� de sommets parcouru.
	 *
	 * @Param: 		Resultat et index de la boucle
	 *  
	 ******************************************************/
	private void fileWriter(StringBuilder resultat, int i){

		//Affiche dans la Console les diff�rents parcours, leurs sommets, le poids entre les sommets et le poids total du parcours
		System.out.println(MSG_DISTANCE_TOTAL + distanceTotal + "\n");
		System.out.println(resultat);

		try {
			//Ecriture du poids total du chemin parcouru dans le fichier .txt
			buffWriter.newLine();
			buffWriter.write(MSG_DISTANCE_TOTAL + distanceTotal);
			buffWriter.newLine();
			buffWriter.newLine();

			//Ecriture des parcours dans le fichier .txt
			buffWriter.write(resultat.toString());
			buffWriter.newLine();
			buffWriter.newLine();

		} catch (IOException e1) { e1.printStackTrace(); }
	}
}
