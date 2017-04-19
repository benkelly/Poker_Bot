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
	private static int PIC_WIDTH = 800;
	private static int PIC_HIGHT = 800;

	private Image img;


	public visualHand(String img) {
		this(new ImageIcon(img).getImage());
	}

	public visualHand(Image img) {
		this.img = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}



	/*conver jPannel to img
	* */
	public BufferedImage createImage(JPanel panel) {
		int w = panel.getWidth()*5;
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

		///visualHand vH = new visualHand("resources/images/playing_cards/2C.png");

		visualHand vH = new visualHand(new ImageIcon("resources/images/playing_cards/2C.png").getImage());
		JFrame frame = new JFrame();
		frame.getContentPane().add(vH);
		frame.pack();
		frame.setVisible(true);





	}
}
