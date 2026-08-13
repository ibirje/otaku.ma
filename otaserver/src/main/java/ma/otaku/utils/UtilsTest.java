package ma.otaku.utils;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilsTest {
	
	
	@Test
	public void test()
	{

	}
	
	
	
	//@Test
	public void convert() {
		int a = 0;
		for(String code: codes) {
			
			String s = a == 0 ? "" : " UNION ";
			
			if(code.startsWith("V_"))
				System.out.println(s + "select code, variationID as variationID from variation where code = \""+code+"\" ");
			else 
				System.out.println(s + "select code,  produitID as variationID from produit where code = \""+code+"\" ");
			a++;
		}
	}
	public static final String[] codes = {
			"BA_AC_PC_DB13",
			"V_PC_DB01_05",
			"V_PC_DB01_02",
			"V_PC_DB01_01",
			"V_PC_DB01_03",
			"V_PC_DB01_04",
			"MD_SC_PF_ZD01",
			"DV_FJ_FP_ZD01",
			"BA_BJ_BG_NR01",
			"V_BG_NR01_03",
			"V_BG_NR01_09",
			"V_BG_NR01_05",
			"V_BG_NR01_02",
			"V_BG_NR01_01",
			"V_BG_NR01_08",
			"V_BG_NR01_06",
			"V_BG_NR01_04",
			"BA_BJ_BR_NR01",
			"DV_FJ_FP_OP02",
			"BA_BJ_BG_NR02",
			"BA_BJ_BR_ZD01",
			"BA_AC_PC_ZD01",
			"V_PC_ZD01_03",
			"V_PC_ZD01_01",
			"V_PC_ZD01_02",
			"BA_BJ_BG_AT01",
			"BA_BJ_BR_PO01",
			"V_BR_PO01_01",
			"V_BR_PO01_02",
			"DV_FJ_FP_DB04",
			"DV_FJ_FP_DB06",
			"DV_FJ_FP_PO01",
			"BA_BJ_PD_AC01",
			"BA_BJ_PD_AT01",
			"BA_BJ_PD_ZD01",
			"BA_AC_PC_AT01",
			"BA_AC_PC_ZD02",
			"BA_AC_PC_ZD03",
			"V_PC_ZD03_02",
			"V_PC_ZD03_01",
			"BA_AC_PC_ZD04",
			"BA_AC_PC_ZD05",
			"BA_AC_PC_ZD06",
			"MD_SC_PF_DB01",
			"MD_SC_PF_SM01",
			"MD_SC_PF_OP01",
			"BA_AC_PC_OP01",
			"BA_AC_PC_OP02",
			"DV_FJ_FP_SM01",
			"DV_FJ_FP_SM02",
			"DV_FJ_FP_SM03",
			"V_FP_SM03_14",
			"V_FP_SM03_12",
			"V_FP_SM03_10",
			"V_FP_SM03_08",
			"V_FP_SM03_06",
			"V_FP_SM03_04",
			"V_FP_SM03_02",
			"V_FP_SM03_15",
			"V_FP_SM03_13",
			"V_FP_SM03_11",
			"V_FP_SM03_09",
			"V_FP_SM03_07",
			"V_FP_SM03_05",
			"V_FP_SM03_03",
			"V_FP_SM03_01",
			"V_FP_SM03_16",
			"DV_FJ_FP_DB07",
			"DV_FJ_FP_DB08",
			"BA_BJ_BR_NR02",
			"MD_CA_CC_SM01",
			"MD_CA_CC_NR01",
			"DV_MB_AI_LL01",
			"BA_BJ_BR_OP01",
			"MD_SC_CH_PO01",
			"BA_AC_PC_LL03",
			"DV_FJ_FP_GH01",
			"BA_BJ_MT_DN01",
			"MD_SC_PF_GH01",
			"MD_SC_PF_OP02",
			"MD_SC_PF_O201",
			"BA_AC_PC_ZD07",
			"MD_SC_PF_GN01",
			"BA_AC_PC_WD01",
			"DV_CJ_CL_O201",
			"V_CL_O301_02",
			"V_CL_O301_03",
			"V_CL_O301_01",
			"BA_BJ_BR_FT01",
			"BA_BJ_BR_OV01",
			"BA_AC_PC_OP03",
			"BA_AC_PC_OV02",
			"DM_MD_SC_O101",
			"DV_FJ_FP_OP03",
			"BA_BJ_PD_ES01",
			"BA_AC_PC_HS01",
			"BA_AC_PC_HH01",
			"BA_AC_PC_DB02",
			"BA_AC_PC_DB03",
			"BA_AC_PC_DB04",
			"BA_AC_PC_OV01",
			"BA_AC_PC_OV03",
			"BA_AC_PC_OV04",
			"BA_AC_PC_OV05",
			"BA_AC_PC_OV06",
			"BA_AC_PC_OV07",
			"BA_AC_PC_OV08",
			"BA_AC_PC_ZY01",
			"BA_AC_PC_ZY02",
			"BA_AC_PC_OP04",
			"BA_AC_PC_OP05",
			"BA_AC_PC_OP06",
			"BA_AC_PC_OP07",
			"BA_AC_PC_OP08",
			"BA_AC_PC_OP09",
			"BA_AC_PC_OP10",
			"BA_AC_PC_OP11",
			"BA_AC_PC_OP12",
			"BA_AC_PC_OP17",
			"BA_AC_PC_OP13",
			"BA_AC_PC_OP14",
			"BA_AC_PC_OP15",
			"BA_AC_PC_OP16",
			"BA_AC_PC_NR01",
			"BA_AC_PC_NR02",
			"BA_AC_PC_NR09",
			"BA_AC_PC_NR03",
			"BA_AC_PC_NR04",
			"BA_AC_PC_NR05",
			"BA_AC_PC_NR06",
			"BA_AC_PC_NR07",
			"BA_AC_PC_NR08",
			"BA_AC_PC_ZY03",
			"BA_AC_PC_ZY04",
			"BA_AC_PC_ZY05",
			"BA_AC_PC_ZY06",
			"BA_AC_PC_ZY07",
			"BA_AC_PC_ZY08",
			"BA_AC_PC_ZY09",
			"BA_AC_PC_ZY10",
			"BA_AC_PC_ZY11",
			"BA_AC_PC_MV01",
			"BA_AC_PC_MV02",
			"BA_AC_PC_SM01",
			"BA_AC_PC_SM02",
			"BA_AC_PC_HH02",
			"BA_AC_PC_HH03",
			"BA_AC_PC_HH04",
			"BA_AC_PC_DB05",
			"BA_AC_PC_DB06",
			"BA_AC_PC_DB07",
			"BA_AC_PC_DB08",
			"BA_AC_PC_ZZ01",
			"BA_AC_PC_ZZ02",
			"BA_AC_PC_ZZ13",
			"BA_AC_PC_ZX02",
			"BA_AC_PC_ZZ03",
			"BA_AC_PC_ZZ04",
			"BA_AC_PC_ZZ05",
			"BA_AC_PC_ZZ06",
			"BA_AC_PC_ZZ07",
			"BA_AC_PC_ZZ08",
			"BA_AC_PC_ZZ09",
			"BA_AC_PC_ZZ10",
			"BA_AC_PC_ZZ11",
			"BA_AC_PC_ZZ12",
			"BA_AC_PC_LL01",
			"BA_AC_PC_LL02",
			"BA_AC_PC_PO01",
			"BA_AC_PC_AT02",
			"BA_AC_PC_AT03",
			"BA_AC_PC_AT04",
			"DV_FJ_FP_NR01",
			"DV_FJ_FP_NR02",
			"DV_FJ_FP_NR03",
			"DV_FJ_FP_NR04",
			"DV_FJ_FP_NR05",
			"DV_FJ_FP_NR06",
			"DV_FJ_FP_OV01",
			"DV_FJ_FP_DB23",
			"DV_FJ_FP_DB01",
			"DV_FJ_FP_DB02",
			"DV_FJ_FP_DB03",
			"DV_FJ_FP_DB05",
			"BA_BJ_PD_DB01",
			"BA_BJ_PD_DB02",
			"BA_AC_PC_SF01",
			"BA_AC_PC_SF02",
			"BA_AC_PC_SF03",
			"BA_AC_PC_SF04",
			"BA_AC_PC_SF05",
			"BA_AC_PC_SF06",
			"BA_AC_PC_SF07",
			"BA_AC_PC_SF08",
			"BA_AC_PC_SF09",
			"BA_AC_PC_SF10",
			"BA_AC_PC_LL04",
			"BA_AC_PC_LL05",
			"BA_AC_PC_LL06",
			"BA_AC_PC_LL07",
			"BA_AC_PC_DB09",
			"BA_AC_PC_DB10",
			"BA_AC_PC_DB11",
			"BA_AC_PC_DB12",
			"DV_FJ_FP_OP04",
			"DV_FJ_FP_OP05",
			"DV_FJ_FP_OP01",
			"DV_FJ_FP_OP06",
			"DV_FJ_FP_OP07",
			"DV_FJ_FP_OP08",
			"DV_FJ_FP_SM04",
			"DV_FJ_FP_SM05",
			"DV_FJ_FP_SM06",
			"DV_FJ_FP_DB09",
			"DV_FJ_FP_DB10",
			"DV_FJ_FP_DB11",
			"DV_FJ_FP_DB12",
			"DV_FJ_FP_DB14",
			"DV_FJ_FP_DB15",
			"DV_FJ_FP_DB16",
			"DV_FJ_FP_DB13",
			"DV_FJ_FP_DB17",
			"DV_FJ_FP_DB18",
			"DV_FJ_FP_DB19",
			"DV_FJ_FP_DB20",
			"DV_FJ_FP_DB21",
			"DV_FJ_FP_DB22",
			"BA_BJ_BG_ZD01",
			"BA_AC_PC_ZY12",
			"BA_AC_PC_GH01",
			"BA_AC_PC_PO02",
			"BA_AC_PC_PO03",
			"DV_FJ_FP_OP09",
			"DV_FJ_FP_OP10",
			"DV_FJ_FP_OP11",
			"DV_FJ_FP_OP12",
			"DV_FJ_FP_OP13",
			"DV_FJ_FP_OP14",
			"DV_FJ_FP_OP15",
			"DV_FJ_FP_OP16",
			"DV_FJ_FP_OP17",
			"DV_FJ_FP_OP18",
			"DV_FJ_FP_OP19",
			"BA_BJ_BR_AT02",
			"BA_AC_PC_LL16",
			"BA_AC_PC_LL08",
			"BA_AC_PC_LL09",
			"BA_AC_PC_LL10",
			"BA_AC_PC_LL14",
			"BA_AC_PC_LL11",
			"BA_AC_PC_LL13",
			"BA_AC_PC_LL12",
			"DV_CJ_JV_ZZ02",
			"DV_CJ_JV_DB02",
			"DV_CJ_CL_ZZ01",
			"DV_CJ_JV_ZZ04",
			"DV_CJ_JV_ZZ01",
			"DV_CJ_JV_ZX01",
			"DV_CJ_JV_ZZ05",
			"DV_CJ_JV_SM03",
			"DV_CJ_JV_SM02",
			"DV_CJ_JV_ZZ07",
			"DV_CJ_JV_ZZ03",
			"DV_CJ_JV_ZZ06",
			"DV_CJ_JV_NR02",
			"DV_CJ_JV_ZZ09",
			"DV_CJ_JV_ZZ08",
			"DV_CJ_JV_ZZ10",
			"DV_CJ_JV_ZZ11",
			"DV_CJ_JV_ZZ12",
			"DV_CJ_JV_ZZ13",
			"DV_CJ_JV_ZX02",
			"DV_CJ_JV_ZZ16",
			"DV_CJ_JV_ZZ14",
			"DV_CJ_JV_ZZ17",
			"DV_CJ_JV_ZZ18"
	};
	
	
	
	//@Test
	public void testJasypt() 
	{
		String text = "Sounabanzai is not a hotdog";
		
		Utils utils = new Utils();

		for(int i = 0 ; i < 20 ; i++)
	    {
			long startTime = System.currentTimeMillis();
	    
			String cripted = utils.crypter(text);
		    long criptime = System.currentTimeMillis();
		    
			String decripted = utils.decripter(cripted);
		    long decriptime = System.currentTimeMillis();
			
			decriptime = decriptime - criptime;
			criptime = criptime - startTime;
			System.out.println(cripted+" "+criptime);
			System.out.println(decripted+" "+decriptime);
	    }
	}
  
	/*
	
	@Test
	public void testIsEmailValide()
	{
		Utils utils = new Utils();
		assertEquals(true,utils.isEmailValide("sounabanzai@hotmail.fr"));
	}
	@Test
	public void testIsEmailValide2()
	{
		assertEquals(true,new Utils().isEmailValide("ibirje.b@gmail.com"));
	}
	@Test
	public void testIsEmailValide3()
	{
		assertEquals(true,new Utils().isEmailValide("t_g_f@g.c"));
	}
	@Test
	public void testIsEmailValide4()
	{
		assertEquals(false, new Utils().isEmailValide("_@c_cc.com"));
	}
	@Test
	public void testIsEmailValide5()
	{
		assertEquals(false, new Utils().isEmailValide("buzz@c_omm.com"));
	}
	*/
	
	@Test
	public void testIsEmailValide6()
	{
		//assertEquals(false, new Utils().isEmailValide("weeeeeee@@zebra.com"));
	}
	
	
	/*
	
	@Test
	void testGenereStockCode()
	{
		List<AchatStockDB> ls = new ArrayList<>();

		Utils utils = new Utils();
		
		for(int i=0 ; i < 10; i++)
		{
			AchatStockDB achat = new AchatStockDB(); achat.setCode("1218_XX_XX_XX_0"+(1+(int)(i*1.333333333333333)%11));
			ls.add(achat);
			System.out.println(achat.getCode());
		}
		Calendar cal = Calendar.getInstance();
		
		String month = cal.get(Calendar.MONTH) <10 ? "0"+cal.get(Calendar.MONTH) : ""+cal.get(Calendar.MONTH);
		String year  = (cal.get(Calendar.YEAR)+"").substring(2);
		
		System.out.println("gen \n"+utils.nextCode(month+""+year+"_CO_DE_FO", ls));
		
	}
	@Test
	void testClassName()
	{
		className(AchatStockDB.class);
		className(FournisseurDB.class);
		className(AccesAchatStock.class);
		className(AccesTable.class);
	}
	public void className(Class<?> classe)
	{
		System.out.println(classe.getName());
		System.out.println(classe.getSimpleName());
	}
	*/
	/*
	@Test
	void testGenereAttributCode()
	{
		BeanProduitAttributs bean = new BeanProduitAttributs();
		
		bean.setProduit(new Produit());
		bean.getProduit().setCode("BA_AC_PC_DB01");
		
		bean.setAttributs(new ArrayList<BeanAttributOptions>());
		Utils utils = new Utils();
		
		for(int i = 1 ; i < 5 ; i ++)
		{
			BeanAttributOptions battr = new BeanAttributOptions();
			battr.setAttribut(new Attribut());
			battr.getAttribut().setCode("PC_DB01_0"+i);
			bean.getAttributs().add(battr);
		}
		bean.getAttributs().get(3).getAttribut().setCode(null);
		
		assertEquals("PC_DB01_04", utils.genereAttributCode(bean));
	}
	*/
	
	/*
	@Test
	void test1GenereOptionCode() 
	{
		BeanAttributOptions newb = new BeanAttributOptions();
		Attribut at = new Attribut();
		at.setCode("BR_PO01_01");
		
		OptionAttribut opt1 = new OptionAttribut();
		opt1.setCode(null);
		
		OptionAttribut opt2 = new OptionAttribut();
		opt2.setCode("BR_PO01_01_01");
		
		OptionAttribut opt3 = new OptionAttribut();
		opt3.setCode("BR_PO01_01_02");
		
		ArrayList<OptionAttribut> options = new ArrayList<>();
		
		options.add(opt1);
		options.add(opt2);
		options.add(opt3);

		newb.setAttribut(at);
		newb.setOptions(options);
		
		assertEquals("BR_PO01_01_03", Utils.genereOptionCode(newb));
	}
	
	@Test
	void test2GenereOptionCode()
	{
		BeanAttributOptions newb = new BeanAttributOptions();
		Attribut at = new Attribut();
		at.setCode("BR_PO01_01");
		
		OptionAttribut opt1 = new OptionAttribut();
		opt1.setCode("BR_PO01_01_10");
		
		OptionAttribut opt2 = new OptionAttribut();
		opt2.setCode("BR_PO01_01_01");
		
		OptionAttribut opt3 = new OptionAttribut();
		opt3.setCode("BR_PO01_01_02");
		
		OptionAttribut opt4 = new OptionAttribut();
		opt4.setCode("BR_PO01_01_03");

		OptionAttribut opt5 = new OptionAttribut();
		opt5.setCode("BR_PO01_01_04");

		OptionAttribut opt6 = new OptionAttribut();
		opt6.setCode("BR_PO01_01_05");		

		OptionAttribut opt7 = new OptionAttribut();
		opt7.setCode("BR_PO01_01_06");

		OptionAttribut opt8 = new OptionAttribut();
		opt8.setCode("BR_PO01_01_07");

		OptionAttribut opt9 = new OptionAttribut();
		opt9.setCode("BR_PO01_01_08");

		OptionAttribut opt10 = new OptionAttribut();
		opt10.setCode("BR_PO01_01_09");
		
		ArrayList<OptionAttribut> options = new ArrayList<>();

		options.add(opt1);
		options.add(opt2);
		options.add(opt3);
		
		options.add(opt4);
		options.add(opt5);
		options.add(opt6);

		options.add(opt7);
		options.add(opt8);
		options.add(opt9);

		options.add(opt10);
		
		newb.setAttribut(at);
		newb.setOptions(options);
		
		assertEquals("BR_PO01_01_11", Utils.genereOptionCode(newb));
	}
	
	@Test
	void test3GenereOptionCode()
	{
		BeanAttributOptions newb = new BeanAttributOptions();
		Attribut at = new Attribut();
		at.setCode("BR_PO01_01");
		
		OptionAttribut opt1 = new OptionAttribut();
		opt1.setCode("BR_PO01_01_10");
		
		OptionAttribut opt2 = new OptionAttribut();
		opt2.setCode(null);
		
		OptionAttribut opt3 = new OptionAttribut();
		opt3.setCode("BR_PO01_01_02");
		
		OptionAttribut opt4 = new OptionAttribut();
		opt4.setCode("BR_PO01_01_03");

		OptionAttribut opt5 = new OptionAttribut();
		opt5.setCode(null);

		OptionAttribut opt6 = new OptionAttribut();
		opt6.setCode("BR_PO01_01_05");		

		OptionAttribut opt7 = new OptionAttribut();
		opt7.setCode("BR_PO01_01_06");

		OptionAttribut opt8 = new OptionAttribut();
		opt8.setCode("BR_PO01_01_07");

		OptionAttribut opt9 = new OptionAttribut();
		opt9.setCode(null);

		OptionAttribut opt10 = new OptionAttribut();
		opt10.setCode("BR_PO01_01_09");
		
		ArrayList<OptionAttribut> options = new ArrayList<>();

		options.add(opt1);
		options.add(opt2);
		options.add(opt3);
		
		options.add(opt4);
		options.add(opt5);
		options.add(opt6);

		options.add(opt7);
		options.add(opt8);
		options.add(opt9);

		options.add(opt10);
		
		newb.setAttribut(at);
		newb.setOptions(options);
		
		for(OptionAttribut o : options)
		{
			if(o.getCode() == null)
			{
				o.setCode(Utils.genereOptionCode(newb));
			}
		}
		for(OptionAttribut o : options)
		{
			System.out.println(o.getCode());
		}
	}
	*/

}
















