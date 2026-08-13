
export class Const {

    public static FILTRE_PRIX_MAX = 5000;
    public static FILTRE_PRIX_MIN = 0;

    private static MIN_LIST_COUNT = 12;
    private static DEFAULT_LIST_COUNT = 24;
    private static MAX_LIST_COUNT = 50; 

    public static LIST_COUNT = Const.DEFAULT_LIST_COUNT; // nombre max de produits dans une liste

    public static SHOW_EMPTY_TYPES = true;  // categ / theme
    public static SHOW_EMPTY_PRODUITS = true; // details-produit qte < 2
    public static SHOW_LIST_EMPTY_PRODUITS = true; // * deconseillée * affiche les listes qui ne contiennent pas de produits

    public static MIN_LENGTH_RECHERCHE = 4;
}
