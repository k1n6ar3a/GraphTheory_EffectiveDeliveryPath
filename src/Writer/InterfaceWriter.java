package Writer;

import java.io.IOException;

/******************************************************
 * @Classe: 	Interface Writer
 * 
 * @Resumer: 	L'interface Writer définit une méthode 
 *				our écrire des objets définis par l'utilisateur 
 *				dans un fichier. Les classes implémentant 
 *				l'interface Writer doivent définir explicitement 
 *				l'objet qu'elles vont écrire.
 * 
 * 					public class MyWriter implements Writer<MyObject> { ... }
 * 
 * 				The caller will need to know what type of 
 * 				objects it will be writing. This limits the 
 * 				run-time flexibility since two different 
 * 				implementations of the Writer interface will 
 * 				write to file two different kinds of object.
 * 
 * 					Writer<AnObject> writer = new AnObjectWriter();
 * 
 * @Param:		L'objet défini par l'utilisateur qui sera écrit dans un fichier.
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public interface InterfaceWriter<T> {

	/******************************************************
	 * Write
	 * 
	 * @Resumer:	Ecriture des données de l'objet dans le 
	 * 				fichier de sortie
	 * 
	 * @Param: 		Fichier de sortie (txt) et les données à
	 * 				écrire dans le fichier de sortie.
	 * 
	 ******************************************************/
	public void write(String outputFile, T output) throws IOException, Exception;
}
