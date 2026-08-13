package ma.otaku.data;

import javax.persistence.Column;

public interface TableWithCode 
{
	@Column(updatable = false)
	public String getCode();
	
	public void setCode(String code);
}
