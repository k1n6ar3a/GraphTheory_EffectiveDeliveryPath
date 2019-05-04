package Solver;

import Parser.GraphModel;

/******************************************************
 * @Classe: 	Interface Solver
 * 
 * @Resumer: 	L'interface du solveur présente une 
 * 				définition de méthode pour effectuer une 
 * 				tâche donnée. Le solveur utilise la sortie 
 * 				produite par un objet Parser et renvoie un objet. 
 * 				L'objet n'est pas défini pour permettre une plus 
 * 				grande flexibilité. En revanche, la flexibilité 
 * 				accrue présente également des inconvénients. 
 * 				Le lanceur de sorts doit connaître le type d'objet 
 * 				qui sera préalablement renvoyé par le solveur.
 * 
 * 					public class ConcreteSolver implements Solver<InObj, OutObj> { ... }
 * 
 * 					Solver<InObj,OutObj> solver = new ConcreteSolver();
 * 
 * 				L’objet d’entrée doit correspondre à celui généré 
 * 				par l’objet Parser qui est la remise des données.
 * 
 * @Param:		Un modèle qui doit être redéfini par les 
 * 				implémentations de l'interface Parser.
 * 
 * @Auteur:		Alexandre Laroche
 * 
 ******************************************************/
public interface InterfaceSolver<E,T> {

	/******************************************************
	 * Solve
	 * 
	 * @Resumer:	Terminez la tâche requise en fonction de 
	 * 				l'entrée E et renvoyez un objet en sortie T. 
	 * 				La méthode doit renvoyer null si la tâche 
	 * 				ne peut pas être complétée correctement.
	 * 
	 * @Param: 		Graph d'entrée
	 * @return		Un objet défini par l'utilisateur, 
	 * 				ou null si quelque chose s'est mal passé.
	 * 
	 ******************************************************/
	public T solve(GraphModel inputGraph);
}
