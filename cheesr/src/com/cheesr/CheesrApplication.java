package com.cheesr;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;

import com.cheesr.store.objects.Cheese;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see wicket.myproject.Start#main(String[])
 */
public class CheesrApplication extends WebApplication {
	private List<Cheese> cheeses = Arrays
			.asList(
					new Cheese(
							"Gouda",
							"Gouda is a yellowish Dutch cheese named after the city of Gouda. The cheese is made from cow's milk. Exported Gouda is usually the young variety (aged between 1 and 6 months, rich yellow in colour and with a red or yellow paraffin wax coating). This cheese is easily sliced on bread.",
							1.65),
					new Cheese(
							"Edam",
							"Edam (Dutch Edammer) is a Dutch cheese named after the town of Edam. Edam cheese has a very mild taste, slightly salty or nutty and almost no smell when compared to other cheeses. Mild Edam is good with fruit such as peaches, melons, apricots and cherries. Aged Edammer is good with traditional \"cheese fruits\" like pears and apples. Like most cheeses, it is also good on crackers and bread. Pinot Noir is a recommended wine to accompany this cheese.",
							1.05),
					new Cheese(
							"Maasdam",
							"Maasdam cheese is a Dutch cheese in a Swiss-style. Made from cow's milk, it is aged for at least 4 weeks. It ripens faster than other Dutch cheeses. Maasdam has internal holes from the ripening process, and a smooth yellow rind. Sometimes it is waxed like Gouda. It is nutty and sweet, but softer than Emmental due to a higher moisture content.",
							2.35),
					new Cheese(
							"Brie",
							"Brie is a soft cows' milk cheese named after Brie, the French province.  It is pale in colour with a slight greyish tinge under crusty white mould; very soft and savoury with a hint of ammonia. The white mouldy rind is moderately tasteful and edible.",
							3.15),
					new Cheese(
							"Buxton Blue",
							"Buxton Blue cheese is an English blue cheese that is a close relative of Blue Stilton. It is made from cow's milk and is lightly veined with a deep russet colouring. It is usually made in a cylindrical shape. This cheese is complimented with a chilled glass of sweet dessert wine or ruby port.",
							0.99),
					new Cheese(
							"Parmesan",
							"Parmesan is a grana, a hard, granular cheese, cooked but not pressed, named after the producing areas of Parma and Reggio Emilia, in Emilia-Romagna, Italy. Uses of the cheese include being grated over pasta, stirred into soup and risotto, and eaten in chunks with balsamic vinegar. It is also a key ingredient in alfredo sauce and pesto.",
							1.99),
					new Cheese(
							"Cheddar",
							"Cheddar cheese is a hard, pale yellow to orange, sharp-tasting cheese originally made in the English village of Cheddar, in Somerset.",
							2.95),
					new Cheese(
							"Roquefort",
							"Roquefort is a ewe's-milk blue cheese from the south of France, and is one of the most famous of all French cheeses. The cheese is white, crumbly and slightly moist, with distinctive veins of blue mold. It has characteristic odor and flavor with a notable taste of butyric acid; the blue veins provide a sharp tang. The overall flavor sensation begins slightly mild, then waxing sweet, then smoky, and fading to a salty finish.",
							1.67),
					new Cheese(
							"Boursin",
							"Boursin Cheese is a soft creamy cheese available in a variety of flavors. Its flavor and texture is somewhat similar to American cream cheese.",
							1.33),
					new Cheese(
							"Camembert",
							"Camembert is a soft, creamy French cheese. When fresh, it is quite crumbly and relatively hard, but it characteristically ripens and becomes more runny and strongly flavoured as it ages. Camembert can be used in many dishes, but it is popularly eaten uncooked on bread or with wine or meat, to enjoy the subtle flavour and texture which does not survive heating. It is usually served at room temperature.",
							1.69),
					new Cheese(
							"Emmental",
							"Emmental is a yellow, medium-hard cheese, with characteristic large holes from Switzerland. It has a piquant, but not really sharp taste. It is often put on top of gratins, dishes which are then put in the oven to let the cheese melt and become golden-brown and crusty. It is also used for fondue.",
							2.39),
					new Cheese(
							"Reblochon",
							"Reblochon is a French cheese from the Alps region of Savoie. Reblochon has a nutty taste that remains in mouth after its soft and uniform centre has been enjoyed. It is an essential ingredient of tartiflette, a Savoyard gratin made from potatoes, cream, onions, and bacon.",
							2.99));

	private List<String> states = Arrays.asList("Alabama", "Alaska", "Arizona",
			"Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida",
			"...", "West Virginia", "Wisconsin", "Wyoming");

	/**
	 * Constructor
	 */
	public CheesrApplication() {
	}

	@Override
	protected void init() {
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return Index.class;
	}

	public List<Cheese> getCheeses() {
		return Collections.unmodifiableList(cheeses);
	}

	public List<String> getStates() {
		return Collections.unmodifiableList(states);
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new CheesrSession(this, request);
	}
	
	public static CheesrApplication get() {
		return (CheesrApplication) WebApplication.get();
	}	
}