package ma.otaku.data.produit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "variationoption")
public class VariationOptionDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long variationOptionID;
	private Long variationID;
	private Long optionAttributID;
	
	


	public VariationOptionDB() {}
	
	public VariationOptionDB(Long variationID, Long optionAttributID) {
		this.variationID = variationID;
		this.optionAttributID = optionAttributID;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getVariationOptionID() {
		return variationOptionID;
	}
	public void setVariationOptionID(Long variationOptionID) {
		this.variationOptionID = variationOptionID;
	}
	public Long getVariationID() {
		return variationID;
	}
	public void setVariationID(Long variationID) {
		this.variationID = variationID;
	}
	public Long getOptionAttributID() {
		return optionAttributID;
	}
	public void setOptionAttributID(Long optionAttributID) {
		this.optionAttributID = optionAttributID;
	}

}
