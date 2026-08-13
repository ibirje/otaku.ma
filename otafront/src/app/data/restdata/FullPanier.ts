import { Produit } from '../Produit';
import { Variation } from '../Variation';
import { PanierItem } from '../PanierItem';

export class FullPanier {
    public produit: Produit;
    public variation: Variation;
    public panier: PanierItem;
    public prix = 0;
}
