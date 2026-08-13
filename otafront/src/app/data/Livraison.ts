export class Livraison {
    /* 
    PL : paiement à la livraison
    VB : Virement banquaire

    LD : livraison à domicile
    PR : Point de relais
    */
   coursier: string;

   prixPL_LD: number; /* prix paiement à la livraison + livraison à domicile */
   coutPL_LD: number; /* cout réel */

   prixPL_PR: number; /* prix paiement à la livraison + Point de relais */
   coutPL_PR: number; /* cout réel */

   prixVB_LD: number; /* prix virement banquaire + livraison à domicile */
   coutVB_LD: number; /* cout réel */
   
   prixVB_PR: number; /* prix virement banquaire + Point de relais */
   coutVB_PR: number; /* cout réel */

    ville: string;  
    codes: string[];
}
