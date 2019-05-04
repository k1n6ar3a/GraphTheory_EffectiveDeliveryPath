package Parser;

import java.io.IOException;

/******************************************************
 * @Classe: 	Interface Parser
 * 
 * @Resumer: 	L�interface Parser d�finit la m�thode par 
 * 				laquelle un fichier d�entr�e sera interpr�t� 
 * 				et un objet de sortie sera renvoy�. 
 * 				
 * 				Pour plus de flexibilit�, l'objet de sortie 
 * 				produit � partir de la m�thode n'est pas d�fini. 
 * 				L'inconv�nient est que deux classes sp�cialis�es 
 * 				peuvent produire des sorties diff�rentes. 
 * 				Le propri�taire devra savoir � l'avance quel 
 * 				type d'objet sera produit par la m�thode lors 
 * 				de la d�claration de l'objet:
 * 
 * 					public class ConcreteParser implements Parser<MyObject> { ... }
 * 
 * 					Parser<MyObject> parser = new ConcreteParse();
 * 
 * @Param:		Un mod�le qui doit �tre red�fini par les 
 * 				impl�mentations de l'interface Parser.
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public interface InterfaceParser<E> {

	/******************************************************
	 * Parse
	 * 
	 * @Resumer:	La m�thode d'analyse est con�ue pour analyser 
	 * 				un fichier d'entr�e et g�n�rer un objet de sortie 
	 * 				d�fini par le mod�le <E>. La m�thode doit renvoyer 
	 * 				null si le fichier ne peut pas �tre lu ou si la 
	 * 				structure du fichier est diff�rente de celle attendue.
	 * 
	 * @Param: 		fichier d'entr�e (txt)
	 * @return		Un objet produit � partir du fichier d'entr�e, 
	 * 				ou null en cas de probl�me.
	 * 
	 ******************************************************/
	public E parse(String inputFile) throws NumberFormatException, IOException, Exception;
}
