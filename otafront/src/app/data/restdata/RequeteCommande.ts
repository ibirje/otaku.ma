import { PanierItem } from 'src/app/data/PanierItem';
import { Adresse } from '../adresse';
export class RequeteCommande {
    
    // packet commande :
    // cet objet est utilisé uniquement pour passer une commande
    // les commandes deja crées contiennent une copie des données existantes au moment de la creation de la commande
    /* ************************************************************************************************************* */
    // 1_ items (code + qte)
    // 2_ adresse
    // 3_ type paiement + type livraison
    /* ************************************************************************************************************* */
    items: PanierItem[];
    adresse: Adresse;
    paiement: String;
    livraison: String;
}
