import { Categorie } from './../Categorie';
import { FullAttribut } from './FullAttribut';
import { FullVariation } from './FullVariation';
import { Produit } from '../Produit';
import { Theme } from '../Theme';

export class FullProduit {

    public produit: Produit;
    public theme: Theme;
    public variations: [FullVariation];
    public attributs: [FullAttribut];
    public categories: [Categorie];
}
