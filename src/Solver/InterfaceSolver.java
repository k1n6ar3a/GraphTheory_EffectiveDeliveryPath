package Solver;

import Parser.GraphModel;

/******************************************************
 * @Classe: 	Interface Solver
 * 
 * @Resumer: 	L'interface du solveur pr�sente une 
 * 				d�finition de m�thode pour effectuer une 
 * 				t�che donn�e. Le solveur utilise la sortie 
 * 				produite par un objet Parser et renvoie un objet. 
 * 				L'objet n'est pas d�fini pour permettre une plus 
 * 				grande flexibilit�. En revanche, la flexibilit� 
 * 				accrue pr�sente �galement des inconv�nients. 
 * 				Le lanceur de sorts doit conna�tre le type d'objet 
 * 				qui sera pr�alablement renvoy� par le solveur.
 * 
 * 					public class ConcreteSolver implements Solver<InObj, OutObj> { ... }
 * 
 * 					Solver<InObj,OutObj> solver = new ConcreteSolver();
 * 
 * 				L�objet d�entr�e doit correspondre � celui g�n�r� 
 * 				par l�objet Parser qui est la remise des donn�es.
 * 
 * @Param:		Un mod�le qui doit �tre red�fini par les 
 * 				impl�mentations de l'interface Parser.
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public interface InterfaceSolver<E,T> {

	/******************************************************
	 * Solve
	 * 
	 * @Resumer:	Terminez la t�che requise en fonction de 
	 * 				l'entr�e E et renvoyez un objet en sortie T. 
	 * 				La m�thode doit renvoyer null si la t�che 
	 * 				ne peut pas �tre compl�t�e correctement.
	 * 
	 * @Param: 		Graph d'entr�e
	 * @return		Un objet d�fini par l'utilisateur, 
	 * 				ou null si quelque chose s'est mal pass�.
	 * 
	 ******************************************************/
	public T solve(GraphModel inputGraph);
}
