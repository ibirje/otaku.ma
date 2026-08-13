package ma.otaku.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;

import org.junit.Test;

import ma.otaku.business.produits.OptionAttribut;
import ma.otaku.data.produit.OptionAttributDB;

public class OptionAttributTest {

	
	@Test
	public void randstring() {
		RandomString rand = new RandomString(6);
		HashSet<String> strings = new HashSet<String>();
		
		for(int i = 0 ; i<10;i++) {
			String str = rand.nextString();
				System.out.println(str);
			strings.add(str);
		}
	}
	
	
	//@Test
	void testEqualsObject1() {
		
		OptionAttribut option = new OptionAttribut();
		option.setCode("PD_DB01_01_01");
		option.setNom("Argent");
		option.setCouleur(null);
		
		OptionAttributDB optionDB = new OptionAttributDB();
		optionDB.setCode("PD_DB01_01_01");
		optionDB.setNom("Argent");
		optionDB.setCouleur(null);

		assertEquals(true, option.equals(new OptionAttribut(optionDB)));
	}
	//@Test
	public void test() {
		int[] range = {1 ,5 ,7 ,3 ,4 ,4,8};
		int needed_sum = 8;
		
		if(range == null || range.length == 0 )fail("liste vide");
		int i = 0;
		int j = range.length - 1 ;
		while(i < j )
		{
			if( range[j]+range[i] > needed_sum )
				j--;
			else if(range[i] + range[j] < 8)
				i++;
			else
			{ 
				System.out.println("["+i+"] "+range[i]+" + ["+j+"] "+range[j]+" = "+needed_sum );
				break;
			}
				
		}
		//fail("Not yet implemented");
	}

	/*
	@Test
	void testEqualsObject3() {
		fail("Not yet implemented");
	}
	@Test
	void testEqualsObject4() {
		fail("Not yet implemented");
	}
	@Test
	void testEqualsObject5() {
		fail("Not yet implemented");
	}
*/
}
