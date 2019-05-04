package Parser;

import java.io.IOException;

/******************************************************
 * @Classe: 	Interface Parser
 * 
 * @Resumer: 	L’interface Parser définit la méthode par 
 * 				laquelle un fichier d’entrée sera interprété 
 * 				et un objet de sortie sera renvoyé. 
 * 				
 * 				Pour plus de flexibilité, l'objet de sortie 
 * 				produit à partir de la méthode n'est pas défini. 
 * 				L'inconvénient est que deux classes spécialisées 
 * 				peuvent produire des sorties différentes. 
 * 				Le propriétaire devra savoir à l'avance quel 
 * 				type d'objet sera produit par la méthode lors 
 * 				de la déclaration de l'objet:
 * 
 * 					public class ConcreteParser implements Parser<MyObject> { ... }
 * 
 * 					Parser<MyObject> parser = new ConcreteParse();
 * 
 * @Param:		Un modèle qui doit être redéfini par les 
 * 				implémentations de l'interface Parser.
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public interface InterfaceParser<E> {

	/******************************************************
	 * Parse
	 * 
	 * @Resumer:	La méthode d'analyse est conçue pour analyser 
	 * 				un fichier d'entrée et générer un objet de sortie 
	 * 				défini par le modèle <E>. La méthode doit renvoyer 
	 * 				null si le fichier ne peut pas être lu ou si la 
	 * 				structure du fichier est différente de celle attendue.
	 * 
	 * @Param: 		fichier d'entrée (txt)
	 * @return		Un objet produit à partir du fichier d'entrée, 
	 * 				ou null en cas de problème.
	 * 
	 ******************************************************/
	public E parse(String inputFile) throws NumberFormatException, IOException, Exception;
}
