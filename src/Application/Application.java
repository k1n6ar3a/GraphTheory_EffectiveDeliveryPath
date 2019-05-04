package Application;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingUtilities;
import Parser.AlgoParser;
import Parser.GraphModel;
import Parser.InterfaceParser;
import Solver.AlgoSolver;
import Solver.InterfaceSolver;
import Writer.AlgoWriter;
import Writer.OutputGraphModel;
import Writer.InterfaceWriter;

/******************************************************
 * @Classe: 	Application
 * 
 * @Resumer:	Cette classe procède à la vérification
 * 				du format des fichiers (input et output),
 * 				démarre ensuite l'analyse du chemin le
 * 				plus court en passant par tous les points
 * 				sans repasser sur le même chemin et 
 * 				sauvegarde les résultats (parcours) dans 
 * 				un fichier (txt).
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public class Application {

	/***************************
	 * Constantes
	 ***************************/
	private static final String 
	TXT = ".txt",
	INPUT_FILE_1 = "input_graph/graph1.txt",
	INPUT_FILE_2 = "input_graph/graph2.txt",
	OUTPUT_FILE_1 = "output_solutions/Result Graph 1 ",
	OUTPUT_FILE_2 = "output_solutions/Result Graph 2 ";

	private static final String 
	MSG_BIENVENU = 	"\n******************************************" +
					"\n*                BIENVENU !              *" +
					"\n*                                        *" +
					"\n*  Voici une application qui permet de   *" +
					"\n*  trouver les parcours qui passent par  *" + 
					"\n*  tous les points une seule fois sans   *" + 
					"\n*  jamais reprendre le même chemin et    *" +
					"\n*  être capable de revenir au même point *" +
					"\n*  de départ.                            *" +
					"\n*                                        *" +
					"\n******************************************\n",
					
	CLOSE_APP = 	"\n******************************************" +
					"\n*         Arrêt de l'application         *" +
					"\n******************************************",
	
	GRAPH_1 = 		"\n******************************************" +
					"\n*        Itinéraires du Graphe #1        *" +
					"\n******************************************",
	
	GRAPH_2 = 		"\n******************************************" +
					"\n*        Itinéraires du Graphe #2        *" +
					"\n******************************************",

	ERREUR_INPUT = "\n********************\n*      ERREUR      *\n********************\n\n" +  
				   "Fichier .txt selectionné est INVALIDE ou NULL ou MANQUANT.\n" + 
				   "Veuillez vous assurer que le fichier d'entrée est de format .txt .\n",
				   
	ERREUR_OUTPUT = "\n********************\n*      ERREUR      *\n********************\n\n" + 
					"Fichier de sauvegarde INVALIDE ou NULL.\n" + 
					"Veuillez vous assurer que le fichier de sauvegarde est de format .txt .\n";


	/*******************************************************
	 * MAIN 
	 * 
	 * @Resumer:	Démarrage d'une nouvelle application dans un 
	 * 				processus séparé pour éviter les conflits de 
	 * 				gestion d'événements. On utilise SwingUtilities 
	 * 				dans le cas où il faudrait mettre en place un GUI.
	 * 
	 * 				
	 * 				*** Normalement, les fichiers en entrés et 
	 * 				sorties sont passés en paramètre (ex: args[0]), 
	 * 				mais pour la démo, on utilise seulement un 
	 * 				String qui pointe vers le répertoire des 
	 * 				fichiers d'entrées et de sorties.
	 *  
	 ******************************************************/
	public static void main(String args[]) {

		//Le systeme démarre le programme dès que possible.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() { 
				System.out.println(MSG_BIENVENU);
				
				//Analyse et sauvegarde des solutions du fichier route1.txt 
				System.out.println(GRAPH_1);
				new Application(INPUT_FILE_1, OUTPUT_FILE_1); 
				
				//Analyse et sauvegarde des solutions du fichier route2.txt 
				System.out.println(GRAPH_2);
				new Application(INPUT_FILE_2, OUTPUT_FILE_2); 
			}
		});
	}

	/*******************************************************
	 * Constructeur - Application
	 * 
	 * @Resumer:	On démarre l'application avec la validation
	 * 				des fichiers d'entrés et de sorties. On
	 * 				étampe le fichier de sauvegarde avec la
	 * 				date et l'heure. 
	 * 
	 * 				Si aucune faute n'est détecté dans la
	 * 				syntaxe du nom des fichiers, on démarre
	 * 				l'analyse de la théorie des graphes.
	 * 
	 * @Param: 		fichier d'entrée, fichier de sauvegarde
	 *  
	 ******************************************************/
	private Application(String inputFile, String outputFile){

		//Étampe le fichier de sauvegarde avec la date et l'heure et l'extension .txt
		String _outputFile = outputFile + getTime() + TXT;
		
		//Validation du fichier d'entrée et de sortie. 
		if(filesValidation(inputFile, _outputFile)) {
			try { startAnalysis(inputFile, _outputFile); } 
			catch (NumberFormatException e) { e.printStackTrace(); } 
			catch (IOException e) { e.printStackTrace(); } 
			catch (Exception e) { e.printStackTrace(); }
		} else {
			System.out.println(CLOSE_APP);
			System.exit(0);
		}
	}
	
	/*******************************************************
	 * Get Time
	 * 
	 * @Resumer:	Récupère la date et l'heure actuelle 
	 * 				pour l'attribuer au fichier output.
	 * 				Cela permet de différencier les 
	 * 				fichiers output du dossier 'results'.
	 * 
	 * @Return:		Date et l'heure (yyyy-mm-dd hh:mm:ss)
	 * 
	 ******************************************************/
	private String getTime() {
		String fileStamp = " - yyyy_MM_dd '('HH'hr' mm'min' ss'sec)'";
		DateFormat dateFormat = new SimpleDateFormat(fileStamp);
		Date date = new Date();
		return dateFormat.format(date); 
	}

	/*******************************************************
	 * Files Validation
	 * 
	 * @Resumer:	Verification de l'existence (non null) 
	 * 				et de l'extension (.txt) du fichier
	 * 				input et du fichier output.
	 * 				
	 * @Param:		fichier d'entrée, fichier de sauvegarde
	 * @Return:		True, si fichiers input/output sont valides
	 * 
	 ******************************************************/
	private boolean filesValidation(String inputFile, String outputFile) {

		if(inputFile != null && inputFile.endsWith(TXT)){
			if(outputFile != null && outputFile.endsWith(TXT)){ 
				return true;
			} else { //Message d'erreur concernant le fichier de sauvegarde
				System.err.println(ERREUR_OUTPUT);	
				return false;
			}
		} else { //Message d'erreur concernant le fichier à lire 
			System.err.println(ERREUR_INPUT);		
			return false;
		}
	}

	/*******************************************************
	 * Start Analysis
	 * 
	 * @Resumer:	C'est le point d'entree de l'application.
	 * 				On effectue une serie d'appels pour
	 * 				trouver une solution au probleme.
	 * 
	 * 				Le programme attend deux arguments:
	 * 				le repertoire du fichier en entree et le 
	 * 				repertoire du fichier de sortie.
	 * 
	 * @Param: 		fichier d'entrée, fichier de sauvegarde
	 * 
	 * @throws 		NumberFormatException, IOException, Exception
	 * 				(Dans ce cas-ci, plus simple que les Try Catch)
	 *  
	 ******************************************************/
	private void startAnalysis(String inputFile, String outputFile) throws NumberFormatException, IOException, Exception{

		InterfaceParser<GraphModel> parser = new AlgoParser();
		InterfaceSolver<GraphModel,OutputGraphModel> solver = new AlgoSolver();
		InterfaceWriter<OutputGraphModel> writer = new AlgoWriter();

		//Analyse les données du fichier input
		GraphModel input = parser.parse(inputFile);

		//Trouve la solution (parcours)
		OutputGraphModel output = solver.solve(input);

		//Ecriture de la solution dans un fichier de sauvegarde
		writer.write(outputFile, output);	
	}
}
