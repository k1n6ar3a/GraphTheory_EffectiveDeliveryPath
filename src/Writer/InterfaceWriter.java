package Writer;

import java.io.IOException;

/******************************************************
 * @Classe: 	Interface Writer
 * 
 * @Resumer: 	L'interface Writer d�finit une m�thode 
 *				our �crire des objets d�finis par l'utilisateur 
 *				dans un fichier. Les classes impl�mentant 
 *				l'interface Writer doivent d�finir explicitement 
 *				l'objet qu'elles vont �crire.
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
 * @Param:		L'objet d�fini par l'utilisateur qui sera �crit dans un fichier.
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public interface InterfaceWriter<T> {

	/******************************************************
	 * Write
	 * 
	 * @Resumer:	Ecriture des donn�es de l'objet dans le 
	 * 				fichier de sortie
	 * 
	 * @Param: 		Fichier de sortie (txt) et les donn�es �
	 * 				�crire dans le fichier de sortie.
	 * 
	 ******************************************************/
	public void write(String outputFile, T output) throws IOException, Exception;
}
