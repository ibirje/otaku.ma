package ma.otaku.data.stats;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "produit_stats")
public class ProduitStats {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long produitstatsID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long produitID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long variationID;

	private Long achatstockQte;
	private Double achatstockPrixTotal;

	private Long commandeQte;
	private Double commandePrixTotal;
	private Long lockedQte;
	private Double fraisSup;

	private Long stockQte;
	private Double stockPrixMoyen;

	private Long pendingQte;
	private Long panierQte;
	private Long favorisQte;
	private Long casseQte;
	private Long perteQte;
	private Long visiteCount;
	
	public ProduitStats() {
		// TODO Auto-generated constructor stub
	}

	public ProduitStats(Long produitID, Long achatstockQte, Double achatstockPrixTotal, Long commandeQte, 
	Double commandePrixTotal, Long stockQte, Double stockPrixTotal, Long pendingQte, Long panierQte, 
	Long casseQte, Long perteQte, Long lockedQte) {
		super();
		this.produitID = produitID;
		this.achatstockQte = achatstockQte;
		this.achatstockPrixTotal = achatstockPrixTotal;
		this.commandeQte = commandeQte;
		this.commandePrixTotal = commandePrixTotal;
		this.stockQte = stockQte;
		this.stockPrixMoyen = stockPrixTotal;
		this.pendingQte = pendingQte;
		this.panierQte = panierQte;
		this.casseQte = casseQte;
		this.perteQte = perteQte;
		this.lockedQte = lockedQte;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(insertable = true, updatable = false)
	public Long getProduitstatsID() {
		return produitstatsID;
	}
	public void setProduitstatsID(Long produitstatsID) {
		this.produitstatsID = produitstatsID;
	}
	@Column(insertable = true, updatable = false)
	public Long getProduitID() {
		return produitID;
	}
	public void setProduitID(Long produitID) {
		this.produitID = produitID;
	}
	@Column(insertable = true, updatable = false)
	public Long getVariationID() {
		return variationID;
	}
	public void setVariationID(Long variationID) {
		this.variationID = variationID;
	}
	public Long getAchatstockQte() {
		return achatstockQte != null ? achatstockQte : 0;
	}
	public void setAchatstockQte(Long achatstockQte) {
		this.achatstockQte = achatstockQte;
	}

	public Long getCommandeQte() {
		return commandeQte != null ? commandeQte : 0;
	}
	public void setCommandeQte(Long commandeQte) {
		this.commandeQte = commandeQte;
	}
	public Double getCommandePrixTotal() {
		return commandePrixTotal != null ? commandePrixTotal : 0;
	}
	public void setCommandePrixTotal(Double commandePrix) {
		this.commandePrixTotal = commandePrix;
	}
	public Long getLockedQte() {
		return lockedQte != null ? lockedQte : 0;
	}

	public void setLockedQte(Long lockedQte) {
		this.lockedQte = Math.max(0, lockedQte);
	}

	public Double getFraisSup() {
		return fraisSup  != null ? fraisSup : 0;
	}

	public void setFraisSup(Double fraisSup) {
		this.fraisSup = fraisSup;
	}

	public Long getStockQte() {
		return stockQte != null ? stockQte : 0;
	}
	public void setStockQte(Long stockQte) {
		this.stockQte = stockQte;
	}
	public Double getStockPrixMoyen() {
		return stockPrixMoyen != null ? stockPrixMoyen : 0;
	}
	public void setStockPrixMoyen(Double stockPrix) {
		this.stockPrixMoyen = stockPrix;
	}
	public Long getPendingQte() {
		return pendingQte != null ? pendingQte : 0;
	}
	public void setPendingQte(Long pendingQte) {
		this.pendingQte = pendingQte;
	}
	public Double getAchatstockPrixTotal() {
		return achatstockPrixTotal != null ? achatstockPrixTotal : 0;
	}
	public void setAchatstockPrixTotal(Double achatstockPrixTotal) {
		this.achatstockPrixTotal = achatstockPrixTotal;
	}
	public Long getPanierQte() {
		return panierQte != null ? panierQte : 0;
	}
	public void setPanierQte(Long panierQte) {
		this.panierQte = panierQte;
	}
	public Long getFavorisQte() {
		return favorisQte != null ? favorisQte : 0;
	}
	public void setFavorisQte(Long favorisQte) {
		this.favorisQte = favorisQte;
	}
	public Long getCasseQte() {
		return casseQte != null ? casseQte : 0;
	}
	public void setCasseQte(Long casseQte) {
		this.casseQte = casseQte;
	}
	public Long getPerteQte() {
		return perteQte != null ? perteQte : 0;
	}
	public void setPerteQte(Long perteQte) {
		this.perteQte = perteQte;
	}
	public Long getVisiteCount() {
		return visiteCount != null ? visiteCount : 0;
	}
	public void setVisiteCount(Long visiteCount) {
		this.visiteCount = visiteCount;
	}

	public boolean compare(ProduitStats p) {
		return  
		this.produitID 			 ==  p.getProduitID() 		  &&
		this.variationID 		 ==  p.getVariationID() 	  &&
		this.achatstockQte 		 ==  p.getAchatstockQte()     &&
		this.achatstockPrixTotal ==  p.getAchatstockPrixTotal() &&
		this.commandeQte 		 ==  p.getCommandeQte() 	  &&
		this.commandePrixTotal   ==  p.getCommandePrixTotal() &&
		this.stockQte 			 ==  p.getStockQte() 		  &&
		this.stockPrixMoyen 	 ==  p.getStockPrixMoyen()    &&
		this.pendingQte 		 ==  p.getPendingQte()		  &&
		this.panierQte 			 ==  p.getPanierQte() 		  &&
		this.casseQte		 	 ==  p.getCasseQte() 		  &&
		this.perteQte 			 ==  p.getPerteQte();
	}

	public Long removedStock() {
		return getCasseQte() + getPerteQte() + getCommandeQte() + getLockedQte();
	}

	public Long acceptedStock() {
		return getAchatstockQte() - getPendingQte();
	}

	public long activeStock() {
		return acceptedStock() - removedStock();
	}
	public Double achatStockPrixMoyen() { return achatstockQte == 0 ? 0 : achatstockPrixTotal / achatstockQte; }
    public Double stockPrixTotal() {return stockPrixMoyen * stockQte; }

    public double commandePrixMoyen() { return commandeQte == 0 ? 0 : commandePrixTotal / commandeQte; }

    public Double commandeCoutTotal(){ return commandeQte == 0 ? 0 : achatStockPrixMoyen() * (stockQte + commandeQte) - stockPrixTotal(); }
    public Double commandeCoutMoyen() { return commandeQte == 0 ? 0 : commandeCoutTotal() / commandeQte; }
    public Double investissementMoyen() {  return commandeCoutMoyen() + fraisSup;  }
    public Double revenuMoyen() { return commandePrixMoyen() - investissementMoyen();  }
    public Double revenuTotal() { return revenuMoyen() * commandeQte; }
	
    /**
     * revenutotal = commandeQte * ( commandePrixTotal / commandeQte - ( commandeCoutTotal/ commandeQte + fraisSUp ))
     * 
     */
	
	
	public void log() {
		System.err.println("-------------------------------");
		System.err.println(this);
		System.err.println("-------------------------------");
	}

	@Override
	public String toString() {
		return "ProduitStats [produitstatsID=" + produitstatsID + ", produitID=" + produitID + ", variationID="
				+ variationID + ", achatstockQte=" + achatstockQte + ", achatstockPrixTotal=" + achatstockPrixTotal
				+ ", commandeQte=" + commandeQte + ", commandePrixTotal=" + commandePrixTotal + ", lockedQte="
				+ lockedQte + ", fraisSup=" + fraisSup + ", stockQte=" + stockQte + ", stockPrixMoyen=" + stockPrixMoyen
				+ ", pendingQte=" + pendingQte + ", panierQte=" + panierQte + ", favorisQte=" + favorisQte
				+ ", casseQte=" + casseQte + ", perteQte=" + perteQte + ", visiteCount=" + visiteCount + "]";
	}
	
}
 