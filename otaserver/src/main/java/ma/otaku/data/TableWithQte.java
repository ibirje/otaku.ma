package ma.otaku.data;

import javax.persistence.Column;

public interface TableWithQte {

	@Column(insertable = false)
	public Long getQte();
	
	public void setQte(Long qte);
}
