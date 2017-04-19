package poker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Group: @poker_bot
 * Sean Regan - 13388996 - sean.regan@ucdconnect.ie
 * Junshu Jiang - 16201124 - junshu.jiang@ucdconnect.ie
 * Liam van der Spek - 14365951 - liam.van-der-spek@ucdconnect.ie
 * Eoin Kerr - 13366801 - eoin.kerr@ucdconnect.ie
 * Benjamin Kelly - 14700869 - benjamin.kelly.1@ucdconnect.ie
 */
public class visualHand extends JPanel {
	private static int PIC_WIDTH = 1600;
	private static int PIC_HIGHT = 800;
	private int cardWidth;

	private Image card1;
	private Image card2;
	private Image card3;
	private Image card4;
	private Image card5;


	public visualHand(String c1, String c2, String c3, String c4, String c5) {
		ImageIcon crd1 = new ImageIcon("resources/images/playing_cards/" + c1);
		ImageIcon crd2 = new ImageIcon("resources/images/playing_cards/" + c2);
		ImageIcon crd3 = new ImageIcon("resources/images/playing_cards/" + c3);
		ImageIcon crd4 = new ImageIcon("resources/images/playing_cards/" + c4);
		ImageIcon crd5 = new ImageIcon("resources/images/playing_cards/" + c5);
		card1 = crd1.getImage();
		card2 = crd2.getImage();
		card3 = crd3.getImage();
		card4 = crd4.getImage();
		card5 = crd5.getImage();
		//this.
	}



	public visualHand(Image card1, Image card2, Image card3, Image card4, Image card5) {
		//this.img = img;
		Dimension size = new Dimension(card1.getWidth(null), card1.getHeight(null));
		cardWidth =  (int) size.getWidth();
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(card1, 0, 0, null);
		g.drawImage(card2, 200, 0, null);
		g.drawImage(card3, 400, 0, null);
		g.drawImage(card4, 600, 0, null);
		g.drawImage(card5, 800, 0, null);

	}



	/*conver jPannel to img
	* */
	public BufferedImage createImage(JPanel panel) {
		int w = panel.getWidth();
		int h = panel.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		panel.paint(g);
		return bi;
	}


	/*Class testing method
* */
	public static void main(String[] args) {
		System.out.println("poker.VisualHand.java!");

		/*visualHand vH = new visualHand("2_of_clubs.png", "3_of_clubs.png", "4_of_clubs.png", "6_of_clubs.png", "8_of_clubs.png" );
		JFrame frame = new JFrame();
		frame.getContentPane().setSize(PIC_WIDTH, PIC_HIGHT);
		frame.getContentPane().add(vH);
		frame.pack();
		frame.setVisible(true);*/
		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		HandOfCards hand = new HandOfCards();
		hand.add(deck.get(43));
		hand.add(deck.get(47));
		hand.add(deck.get(3));
		hand.add(deck.get(39));
		hand.add(deck.get(51));
		generateVisual(hand);


	}
	public static visualHand generateVisual(HandOfCards h){
		System.out.println(h.get(0).toString());
		System.out.println(h.get(1).toString());
		System.out.println(h.get(2).toString());
		System.out.println(h.get(3).toString());
		System.out.println(h.get(4).toString());

		visualHand vH = new visualHand(h.get(0).toString() + ".png",h.get(1).toString() + ".png",
				h.get(2).toString() + ".png",h.get(3).toString() + ".png",h.get(4).toString() + ".png");
		JFrame frame = new JFrame();
		frame.getContentPane().setSize(PIC_WIDTH, PIC_HIGHT);
		frame.getContentPane().add(vH);
		//frame.pack();
		frame.setVisible(true);
		return vH;
	}
}
