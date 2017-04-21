package poker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

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
	private int PlayerChips;

	private Image pokerChips;
	private Image card1;
	private Image card2;
	private Image card3;
	private Image card4;
	private Image card5;


	public visualHand(String c1, String c2, String c3, String c4, String c5, int chipAmount) {
		PlayerChips = chipAmount;
		ImageIcon chips = new ImageIcon("resources/images/chips.png");
		//ImageIcon chips = new ImageIcon("resources/images/playing_cards/" + c1);
		ImageIcon crd1 = new ImageIcon("resources/images/playing_cards/" + c1);
		ImageIcon crd2 = new ImageIcon("resources/images/playing_cards/" + c2);
		ImageIcon crd3 = new ImageIcon("resources/images/playing_cards/" + c3);
		ImageIcon crd4 = new ImageIcon("resources/images/playing_cards/" + c4);
		ImageIcon crd5 = new ImageIcon("resources/images/playing_cards/" + c5);
		pokerChips = chips.getImage();
		card1 = crd1.getImage();
		card2 = crd2.getImage();
		card3 = crd3.getImage();
		card4 = crd4.getImage();
		card5 = crd5.getImage();
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
		g.drawImage(createResizedCopy(card1, (int)(card1.getWidth(null)*0.8),(int)(card1.getHeight(null)*0.7),false ), 25, 25, null);
		g.drawImage(createResizedCopy(card2, (int)(card1.getWidth(null)*0.7),(int)(card1.getHeight(null)*0.7),false ), 225, 25, null);
		g.drawImage(createResizedCopy(card3, (int)(card1.getWidth(null)*0.7),(int)(card1.getHeight(null)*0.7),false ), 425, 25, null);
		g.drawImage(createResizedCopy(card4, (int)(card1.getWidth(null)*0.7),(int)(card1.getHeight(null)*0.7),false ), 625, 25, null);
		g.drawImage(createResizedCopy(card5, (int)(card1.getWidth(null)*0.7),(int)(card1.getHeight(null)*0.7),false ), 825, 25, null);
		g.drawImage(createResizedCopy(pokerChips, (int)(pokerChips.getWidth(null)*0.3),(int)(pokerChips.getHeight(null)*0.3),false ), 25, 550, null);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 200));
		g.drawString(" = "+PlayerChips, 255, 725);

	}

	public static JPanel generateVisual(HandOfCards h, int chips){
		//System.out.println(h.get(0).toString());
		//System.out.println(h.get(1).toString());
		//System.out.println(h.get(2).toString());
		//System.out.println(h.get(3).toString());
		//System.out.println(h.get(4).toString());

		visualHand vH = new visualHand(h.get(0).toString() + ".png",h.get(1).toString() + ".png",
				h.get(2).toString() + ".png",h.get(3).toString() + ".png",h.get(4).toString() + ".png", chips);
		//JPanel panel = new JPanel();
		//frame.getContentPane().setSize(PIC_WIDTH, PIC_HIGHT);
		//panel.setSize(PIC_WIDTH, PIC_HIGHT);
		vH.setSize(PIC_WIDTH, PIC_HIGHT);
		vH.setBackground(Color.gray);
		//panel.add(vH);
		//frame.pack();
		//panel.setVisible(true);

		return vH;
	}

	BufferedImage createResizedCopy(Image originalImage,
	                                int scaledWidth, int scaledHeight,
	                                boolean preserveAlpha)
	{
		//System.out.println("resizing...");
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g = scaledBI.createGraphics();
		if (preserveAlpha) {
			g.setComposite(AlphaComposite.Src);
		}
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}


	/*conver jPannel to img
	* */
	public static BufferedImage createImage(JPanel panel) {
		int w = panel.getWidth();
		int h = panel.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		panel.paint(g);
		return bi;
	}


	public static InputStream saveBufferedImageToInputStream(BufferedImage bi) {
		InputStream inputStream = null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", os);
			inputStream = new ByteArrayInputStream(os.toByteArray());
			return inputStream;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static void saveBufferedImageToFile(BufferedImage bi) {
		File outputfile = new File("image.jpg");
		try {
			ImageIO.write(bi, "jpg", outputfile);
			System.out.println("image.jpg made..");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/*Class testing method
* */
	public static void main(String[] args) throws Exception {
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


		//generateVisual(hand, 3000);

		//saveBufferedImageToFile(createImage(generateVisual(hand)));
		//saveBufferedImageToFile(createImage(generateVisual(hand, 3000)));
		TwitterInterpreter.getInstance().tweetPic(saveBufferedImageToInputStream(createImage(generateVisual(hand, 3000))), "testing :^)");


	}
}
